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
import cn.lili.modules.blindBox.service.BlindBoxPrizeService;
import cn.lili.modules.blindBox.service.BlindBoxService;

import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.Trade;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlindBoxOrder createOrder(OrderParam orderParam) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //创建交易
        BlindBoxOrder order = new BlindBoxOrder(orderParam);
        order.setMemberId(currentUser.getId());
        order.setMemberName(currentUser.getUsername());
        order.setSn(SnowFlake.getIdStr());
        orderService.createOrder(order);
        //修改优惠卷状态
        if(StringUtils.isNotBlank(orderParam.getCouponId())) {
            //检验优惠券是否过期，是否被使用
            MemberCouponSearchParams searchParams = new MemberCouponSearchParams();
            searchParams.setMemberCouponStatus(MemberCouponStatusEnum.NEW.name());
            searchParams.setMemberId(currentUser.getId());
            searchParams.setId(orderParam.getCouponId());
            searchParams.setEndTime(System.currentTimeMillis());
            MemberCoupon memberCoupon = memberCouponService.getMemberCoupon(searchParams);
            if (memberCoupon == null) {
                throw new ServiceException(ResultCode.COUPON_EXPIRED);
            }
            memberCouponService.updateMemberCouponByCouponId(currentUser.getId(), orderParam.getCouponId());
        }
        return order;
    }

    @Override
    public BlindBoxGoodsVO blindBoxExtract(ExtractParam extractParam) {
        //根据订单号查询出订单
        BlindBoxOrder blindBoxOrder = orderService.queryOrder(extractParam.getSn());
        if(blindBoxOrder == null){
            throw new ServiceException(ResultCode.ORDER_NOT_EXIT_ERROR);
        }else if(!"PAID".equals(blindBoxOrder.getPayStatus())) {
            throw new ServiceException(ResultCode.ORDER_NOT_PAY_ERROR);
        }else if("1".equals(blindBoxOrder.getExtractStatus())){
            throw new ServiceException(ResultCode.ORDER_EXTRACT_ERROR);
        }
        //取出类型id，查询出商品列表
        List<BlindBoxGoods> blindBoxGoods = blindBoxGoodsService.queryList(blindBoxOrder.getBlindBoxCategory());
        //抽奖
        BlindBoxGoodsVO boxGoods = extract(blindBoxGoods,blindBoxOrder.getGoodsNum());
        //记录奖品
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(extractParam.getBlindBoxCategory())) {
            queryWrapper.eq(BlindBoxCategory::getId, extractParam.getBlindBoxCategory());
        }
        BlindBoxCategory blindBoxCategory = this.baseMapper.selectOne(queryWrapper);
        blindBoxPrizeService.batchAddPrize(bulidPrizeList(blindBoxGoods,blindBoxCategory));
        return boxGoods;
    }

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
    public List<Prize> bulidPrizeList(List<BlindBoxGoods> blindBoxGoods,BlindBoxCategory blindBoxCategory){
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        List<Prize> prizeList = new ArrayList<>();
        for (BlindBoxGoods boxGoods :blindBoxGoods) {
            Prize prize= new Prize();
            prize.setBlindBoxCategory(blindBoxCategory.getId());
            prize.setGoodsId(boxGoods.getId());
            prize.setImage(blindBoxCategory.getImage());
            prize.setName(blindBoxCategory.getName());
            prize.setMemberId(currentUser.getId());
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
    public BlindBoxGoodsVO extract(List<BlindBoxGoods> blindBoxGoods,int num){
        BlindBoxGoodsVO blindBoxGoodsVO = new BlindBoxGoodsVO();
        List<BlindBoxGoodsDTO> blindBoxGoodsDTOS = new ArrayList<>();
        Integer[] arr = new Integer[blindBoxGoods.size()];
        for (int i=0; i<blindBoxGoods.size();i++) {
            arr[i] = blindBoxGoods.get(i).getProbability();
        }
        Arrays.sort(arr);
        Random random = new Random();
        int flag = -1;
        for(int j=0;j<=num;j++) {
            int rn = random.nextInt(100);
            for(int k=0;k<arr.length;k++){
                if(rn>arr[k-1] && rn<=arr[k]){
                    flag=k;
                    BlindBoxGoodsDTO blindBoxGoodsDTO = new BlindBoxGoodsDTO();
                    BeanUtil.copyProperties(blindBoxGoods.get(flag),blindBoxGoodsDTO);
                    blindBoxGoodsDTOS.add(blindBoxGoodsDTO);
                    break;
                }
            }
        }
        blindBoxGoodsVO.setBlindBoxGoodsDTOS(blindBoxGoodsDTOS);
        return blindBoxGoodsVO;
    }

    @Override
    public IPage<BlindBoxCategory> getBlindBoxCategoryByPage(BoxSearchParams searchParams) {
        return this.page(PageUtil.initPage(searchParams), searchParams.queryWrapper());
    }


}
