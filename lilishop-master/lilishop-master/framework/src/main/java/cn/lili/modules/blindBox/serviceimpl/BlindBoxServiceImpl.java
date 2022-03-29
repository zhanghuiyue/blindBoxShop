package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dos.Prize;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxGoodsDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.blindBox.mapper.BlindBoxCategoryMapper;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.blindBox.service.BlindBoxPrizeService;
import cn.lili.modules.blindBox.service.BlindBoxService;

import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.Trade;
import cn.lili.modules.order.order.entity.enums.ExtractStatusEnum;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.modules.order.order.service.TradeService;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.entity.dto.search.MemberCouponSearchParams;
import cn.lili.modules.promotion.entity.enums.MemberCouponStatusEnum;
import cn.lili.modules.promotion.service.MemberCouponService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 盲盒相关业务层实现
 */
@Service
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxCategoryMapper,BlindBoxCategory> implements BlindBoxService {

    @Autowired
    private BlindBoxOrderService orderService;

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private BlindBoxGoodsService blindBoxGoodsService;

    @Autowired
    private BlindBoxPrizeService blindBoxPrizeService;

    @Autowired
    private BlindBoxPriceService blindBoxPriceService;

    /**
     * 盲盒列表查询
     * @return List<BlindBoxCategory>
     */
    @Override
    public List<BlindBoxCategory> queryBlindBoxCategoryList() {
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(BlindBoxCategory::getSortOrder);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 盲盒下单
     * @param orderParam 订单相关参数
     * @return BlindBoxOrder
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlindBoxOrder createOrder(OrderParam orderParam) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //创建交易
        BlindBoxOrder order = new BlindBoxOrder(orderParam);
        order.setMemberId(currentUser.getId());
        order.setMemberName(currentUser.getUsername());
        order.setSn(SnowFlake.getIdStr());
        //修改优惠卷状态
        if(StringUtils.isNotBlank(orderParam.getCouponId())) {
            order.setCouponId(orderParam.getCouponId());
            //检验优惠券是否过期，是否被使用
            LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<MemberCoupon>();
            queryWrapper.eq(MemberCoupon::getMemberId,currentUser.getId());
            queryWrapper.eq(MemberCoupon::getCouponId,orderParam.getCouponId());
            queryWrapper.eq(MemberCoupon::getMemberCouponStatus,MemberCouponStatusEnum.NEW.name());
            queryWrapper.ge(MemberCoupon::getEndTime,new Date());
            MemberCoupon memberCoupon = memberCouponService.getOne(queryWrapper);
            if (memberCoupon == null) {
                throw new ServiceException(ResultCode.COUPON_EXPIRED);
            }
            memberCouponService.updateMemberCouponByCouponId(currentUser.getId(), orderParam.getCouponId());
        }
        orderService.createOrder(order);
        return order;
    }

    @Override
    public BlindBoxGoodsVO blindBoxExtract(ExtractParam extractParam) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //根据订单号查询出订单
        BlindBoxOrder blindBoxOrder = orderService.queryOrder(extractParam.getSn());
        if(blindBoxOrder == null){
            throw new ServiceException(ResultCode.ORDER_NOT_EXIT_ERROR);
        }else if(PayStatusEnum.UNPAID.equals(blindBoxOrder.getPayStatus())) {
            throw new ServiceException(ResultCode.ORDER_NOT_PAY_ERROR);
        }else if(ExtractStatusEnum.EXTRACT.getState().equals(blindBoxOrder.getExtractStatus())){
            throw new ServiceException(ResultCode.ORDER_EXTRACT_ERROR);
        }
        //取出类型id，查询出商品列表
        List<BlindBoxGoods> blindBoxGoods = blindBoxGoodsService.queryList(blindBoxOrder.getBlindBoxCategory());
        //抽奖
        BlindBoxGoodsVO boxGoods = extract(blindBoxGoods,blindBoxOrder.getGoodsNum());
        //修改抽取的状态
        LambdaUpdateWrapper<BlindBoxOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BlindBoxOrder::getSn, blindBoxOrder.getSn());
        updateWrapper.set(BlindBoxOrder::getExtractStatus, ExtractStatusEnum.EXTRACT.getState());
        orderService.update(updateWrapper);
        //记录奖品
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(extractParam.getBlindBoxCategory())) {
            queryWrapper.eq(BlindBoxCategory::getId, extractParam.getBlindBoxCategory());
        }
        BlindBoxCategory blindBoxCategory = this.baseMapper.selectOne(queryWrapper);
        blindBoxPrizeService.batchAddPrize(bulidPrizeList(blindBoxGoods,blindBoxCategory,currentUser.getId()));
        return boxGoods;
    }

    /**
     * 查询盲盒列表
     * @param blindBoxCategorySearchPara
     * @return
     */
    @Override
    public BlindBoxCategoryVO queryBlindBoxList(BlindBoxCategorySearchParam blindBoxCategorySearchPara) {
        BlindBoxCategoryVO blindBoxCategoryVO = new BlindBoxCategoryVO();
        QueryWrapper queryWrapper = blindBoxCategorySearchPara.queryWrapper();
        List<BlindBoxCategory> blindBoxCategoryList = this.baseMapper.selectList(queryWrapper);
        List<BlindBoxCategoryDTO> blindBoxCategoryDTOList = new ArrayList<>();
        for (BlindBoxCategory blindBoxCategory:blindBoxCategoryList) {
            BlindBoxCategoryDTO blindBoxCategoryDTO = new BlindBoxCategoryDTO();
            BeanUtil.copyProperties(blindBoxCategory,blindBoxCategoryDTO);
            blindBoxCategoryDTOList.add(blindBoxCategoryDTO);
        }
        blindBoxCategoryVO.setBlindBoxCategoryDTOList(blindBoxCategoryDTOList);
        return blindBoxCategoryVO;
    }

    /**
     * 构建prideList
     * @param blindBoxGoods
     * @param blindBoxCategory
     * @return
     */
    private List<Prize> bulidPrizeList(List<BlindBoxGoods> blindBoxGoods,BlindBoxCategory blindBoxCategory,String memberId){
        List<Prize> prizeList = new ArrayList<>();
        for (BlindBoxGoods boxGoods :blindBoxGoods) {
            Prize prize= new Prize();
            prize.setBlindBoxCategory(blindBoxCategory.getId());
            prize.setGoodsId(boxGoods.getId());
            prize.setImage(blindBoxCategory.getImage());
            prize.setName(blindBoxCategory.getName());
            prize.setMemberId(memberId);
            prize.setSubstitutionFlag("0");
            prize.setSubstitutionNum(0);
            prizeList.add(prize);
        }
        return prizeList;
    }
    /**
     * 商品的抽取
     * @param blindBoxGoods
     * @param num
     * @return
     */
    private BlindBoxGoodsVO extract(List<BlindBoxGoods> blindBoxGoods,int num){
        BlindBoxGoodsVO blindBoxGoodsVO = new BlindBoxGoodsVO();
        List<BlindBoxGoodsDTO> blindBoxGoodsDTOS = new ArrayList<>();
        Integer[] arr = new Integer[blindBoxGoods.size()];
        ArrayList<Double> posibilitys= new ArrayList<>();
        for (BlindBoxGoods boxGoods:blindBoxGoods) {
            posibilitys.add(boxGoods.getProbability());
        }
        for(int j=0;j<num;j++) {
            BlindBoxGoodsDTO blindBoxGoodsDTO = new BlindBoxGoodsDTO();
            BeanUtil.copyProperties(blindBoxGoods.get(lottery(posibilitys)),blindBoxGoodsDTO);
            blindBoxGoodsDTOS.add(blindBoxGoodsDTO);
        }
        blindBoxGoodsVO.setBlindBoxGoodsDTOS(blindBoxGoodsDTOS);
        return blindBoxGoodsVO;
    }

    private int lottery(List<Double> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }
        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        double random =threadLocalRandom.nextDouble(0,1);
        sortOrignalRates.add(random);
        Collections.sort(sortOrignalRates);
        return sortOrignalRates.indexOf(random);
    }
    @Override
    public IPage<BlindBoxCategory> getBlindBoxCategoryByPage(BoxSearchParams searchParams) {
        return this.page(PageUtil.initPage(searchParams), searchParams.queryWrapper());
    }

    @Override
    public void addBlindBox(BlindBoxCategoryDTO blindBoxCategoryDTO) {
        BlindBoxCategory blindBoxCategory = new BlindBoxCategory();
        BeanUtil.copyProperties(blindBoxCategoryDTO,blindBoxCategory);
        this.baseMapper.insert(blindBoxCategory);
    }

    @Override
    public void updateBlindBox(BlindBoxCategoryDTO blindBoxCategoryDTO) {
        BlindBoxCategory blindBoxCategory = new BlindBoxCategory();
        BeanUtil.copyProperties(blindBoxCategoryDTO,blindBoxCategory);
        LambdaUpdateWrapper<BlindBoxCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BlindBoxCategory::getId, blindBoxCategoryDTO.getId());
        this.baseMapper.update(blindBoxCategory,updateWrapper);
    }

    @Override
    @Transactional
    public void deleteBlindBox(String id) {
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<BlindBoxCategory>();
        queryWrapper.eq(BlindBoxCategory::getId,id);
        this.baseMapper.delete(queryWrapper);
        blindBoxPriceService.deleteByCategoryId(id);
    }

    @Override
    public void batchDeleteBlindBox(List<String> ids) {
        this.baseMapper.deleteBatchIds(ids);
        List priceIds = blindBoxPriceService.batchQuery(ids);
        blindBoxPriceService.batchDelete(priceIds);
    }


}
