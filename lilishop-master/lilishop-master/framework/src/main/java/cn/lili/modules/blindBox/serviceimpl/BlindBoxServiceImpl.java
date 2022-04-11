package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.*;
import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCouponDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxGoodsDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.blindBox.enums.BlindBoxTypeEnum;
import cn.lili.modules.blindBox.mapper.BlindBoxMapper;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.blindBox.service.BlindBoxPrizeService;
import cn.lili.modules.blindBox.service.BlindBoxService;

import cn.lili.modules.blindBox.service.TribeService;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.enums.ExtractStatusEnum;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.promotion.entity.dos.Coupon;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.entity.dto.search.CouponSearchParams;
import cn.lili.modules.promotion.entity.enums.MemberCouponStatusEnum;
import cn.lili.modules.promotion.entity.enums.PromotionsStatusEnum;
import cn.lili.modules.promotion.service.CouponService;
import cn.lili.modules.promotion.service.MemberCouponService;
import cn.lili.mybatis.util.PageUtil;
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
import java.util.stream.Collectors;

/**
 * 盲盒相关业务层实现
 */
@Service
public class BlindBoxServiceImpl extends ServiceImpl<BlindBoxMapper, BlindBox> implements BlindBoxService {

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

    @Autowired
    private TribeService tribeService;

    @Autowired
    private CouponService couponService;

    /**
     * 盲盒列表查询
     * @return List<BlindBox>
     */
    @Override
    public List<BlindBox> queryBlindBoxCategoryList() {
        LambdaQueryWrapper<BlindBox> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(BlindBox::getSortOrder);
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
        BlindBoxGoodsVO blindBoxGoodsVO = new BlindBoxGoodsVO();
        //根据订单号查询出订单
        Tribe tribe = tribeService.getBaseMapper().selectById(extractParam.getId());
        if(tribe == null){
            throw new ServiceException(ResultCode.BLIND_BOX_NOT_EXIT_ERROR);
        }else if(ExtractStatusEnum.EXTRACT.getState().equals(tribe.getExtractStatus())){
            throw new ServiceException(ResultCode.BLIND_BOX_EXTRACT_ERROR);
        }
        List<BlindBoxGoods> blindBoxGoods = null;
        if(BlindBoxTypeEnum.CHARGE.name().equals(tribe.getBlindBoxType())) {
            //取出类型id，查询出商品列表
            blindBoxGoods = blindBoxGoodsService.queryList(tribe.getBlindBoxId());
            //抽奖
            List<BlindBoxGoodsDTO> boxGoods = extract(blindBoxGoods,tribe.getNum());
            //记录奖品
            LambdaQueryWrapper<BlindBox> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(tribe.getBlindBoxId())) {
                queryWrapper.eq(BlindBox::getId, tribe.getBlindBoxId());
            }
            BlindBox blindBox = this.baseMapper.selectOne(queryWrapper);
            blindBoxPrizeService.batchAddPrize(bulidPrizeList(boxGoods, blindBox,currentUser.getId()));
            blindBoxGoodsVO.setBlindBoxGoodsDTOS(boxGoods);
        }else if(BlindBoxTypeEnum.FREE.name().equals(tribe.getBlindBoxType())){
            LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
            List<Coupon> couponList = couponService.getBaseMapper().selectList(queryWrapper);
            List<Coupon> filterCoupons = filterCoupon(couponList,extractParam.getId());
            List<BlindBoxCouponDTO> blindBoxCouponDTOS = extractCoupon(filterCoupons,tribe.getNum());
            //发优惠券
            List<CouponSearchParams> couponSearchParams = new ArrayList<>();
            for (BlindBoxCouponDTO blindBoxCouponDTO:blindBoxCouponDTOS) {
                CouponSearchParams searchParams = new CouponSearchParams();
                searchParams.setId(blindBoxCouponDTO.getCouponId());
                couponSearchParams.add(searchParams);
            }
            blindBoxGoodsVO.setBlindBoxCouponDTOS(blindBoxCouponDTOS);
            memberCouponService.receiveBuyerCouponList(couponSearchParams);
        }
        //修改抽取的状态
        tribe.setExtractStatus(ExtractStatusEnum.EXTRACT.getState());
        tribeService.getBaseMapper().updateById(tribe);
        return blindBoxGoodsVO;
    }

    /**
     * 过滤优惠券
     * @param couponList
     * @param memberId
     * @return
     */
    private List<Coupon> filterCoupon(List<Coupon> couponList,String memberId){
        List<Coupon> coupons = new ArrayList<>();
        for (Coupon coupon:couponList) {
            LambdaQueryWrapper<MemberCoupon> queryWrapper = new LambdaQueryWrapper<MemberCoupon>()
                    .eq(MemberCoupon::getCouponId, coupon.getId())
                    .eq(MemberCoupon::getMemberId, memberId);
            long haveCoupons = memberCouponService.count(queryWrapper);
            if (coupon.getPublishNum() != 0 && coupon.getReceivedNum()!=null && coupon.getReceivedNum() < coupon.getPublishNum()) {
                if (!coupon.getCouponLimitNum().equals(0) && haveCoupons < coupon.getCouponLimitNum()) {
                    coupons.add(coupon);
                }
            }

        }
        return coupons;
    }
    /**
     * 查询盲盒列表
     * @param blindBoxCategorySearchPara
     * @return
     */
    @Override
    public BlindBoxVO queryBlindBoxList(BlindBoxCategorySearchParam blindBoxCategorySearchPara) {
        BlindBoxVO blindBoxVO = new BlindBoxVO();
        QueryWrapper queryWrapper = blindBoxCategorySearchPara.queryWrapper();
        List<BlindBox> blindBoxList = this.baseMapper.selectList(queryWrapper);
        List<BlindBoxDTO> blindBoxDTOList = new ArrayList<>();
        for (BlindBox blindBox : blindBoxList) {
            BlindBoxDTO blindBoxDTO = new BlindBoxDTO();
            BeanUtil.copyProperties(blindBox, blindBoxDTO);
            blindBoxDTOList.add(blindBoxDTO);
        }
        blindBoxVO.setBlindBoxDTOList(blindBoxDTOList);
        return blindBoxVO;
    }

    /**
     * 构建prideList
     * @param blindBoxGoods
     * @param blindBox
     * @return
     */
    private List<Prize> bulidPrizeList(List<BlindBoxGoodsDTO> blindBoxGoods, BlindBox blindBox, String memberId){
        List<Prize> prizeList = new ArrayList<>();
        for (BlindBoxGoodsDTO boxGoods :blindBoxGoods) {
            Prize prize= new Prize();
            prize.setBlindBoxId(blindBox.getId());
            prize.setGoodsId(boxGoods.getId());
            prize.setImage(blindBox.getImage());
            prize.setName(blindBox.getName());
            prize.setMemberId(memberId);
            prize.setSubstitutionFlag("0");
            prize.setSubstitutionNum(0);
            prizeList.add(prize);
        }
        return prizeList;
    }

    /**
     * 优惠券的抽取
     * @param couponList
     * @param num
     * @return
     */
    private List<BlindBoxCouponDTO> extractCoupon(List<Coupon> couponList , int num){
        List<BlindBoxCouponDTO> blindBoxCouponDTOS = new ArrayList<>();
        Integer[] arr = new Integer[couponList.size()];
        ArrayList<Double> posibilitys= new ArrayList<>();
        for (Coupon coupon:couponList) {
            posibilitys.add(coupon.getProbability());
        }
        for(int j=0;j<num;j++) {
            BlindBoxCouponDTO blindBoxGoodsDTO = new BlindBoxCouponDTO();
            Coupon coupon = couponList.get(lottery(posibilitys));
            BeanUtil.copyProperties(couponList.get(lottery(posibilitys)),blindBoxGoodsDTO);
            blindBoxGoodsDTO.setStartTime(new Date());
            blindBoxGoodsDTO.setCouponId(coupon.getId());
            blindBoxCouponDTOS.add(blindBoxGoodsDTO);
        }
        return blindBoxCouponDTOS;
    }

    /**
     * 商品的抽取
     * @param blindBoxGoods
     * @param num
     * @return
     */
    private List<BlindBoxGoodsDTO> extract(List<BlindBoxGoods> blindBoxGoods,int num){
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
        return blindBoxGoodsDTOS;
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
    public IPage<BlindBox> getBlindBoxCategoryByPage(BoxSearchParams searchParams) {
        LambdaQueryWrapper<BlindBox> queryWrapper = new LambdaQueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(searchParams.getName())) {
            queryWrapper.like(BlindBox::getName, searchParams.getName());
        }
        return this.page(PageUtil.initPage(searchParams), queryWrapper);
    }

    @Override
    public boolean addBlindBox(BlindBoxDTO blindBoxDTO) {
        BlindBox blindBox = new BlindBox();
        BeanUtil.copyProperties(blindBoxDTO, blindBox);
        if (getOne(new LambdaQueryWrapper<BlindBox>().eq(BlindBox::getName, blindBox.getName())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_NAME_EXIST_ERROR);
        }
        return this.save(blindBox);
    }

    @Override
    public boolean updateBlindBox(BlindBoxDTO blindBoxDTO) {
        BlindBox blindBox = new BlindBox();
        BeanUtil.copyProperties(blindBoxDTO, blindBox);
        if (getOne(new LambdaQueryWrapper<BlindBox>().eq(BlindBox::getName, blindBox.getName()).ne(BlindBox::getId, blindBox.getId())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_NAME_EXIST_ERROR);
        }
        return this.updateById(blindBox);
    }

    @Override
    public boolean deleteBlindBox(String id) {
        checkBind(id);
        return this.removeById(id);
    }

    @Override
    public void batchDeleteBlindBox(List<String> ids) {
        this.baseMapper.deleteBatchIds(ids);
        List priceIds = blindBoxPriceService.batchQuery(ids);
        blindBoxPriceService.batchDelete(priceIds);
    }


    @Override
    public boolean blindBoxDisable(String id, boolean disable) {
        BlindBox blindBox = this.checkExist(id);
        //如果是要禁用，则需要先判定绑定关系
        if (Boolean.TRUE.equals(disable)) {
            checkBind(id);
        }
        blindBox.setDeleteFlag(disable);
        return updateById(blindBox);
    }



    /**
     * 验证绑定关系
     * @param id
     */
    private void checkBind(String id){
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Price::getBlindBoxId,id);
        List<Price> priceList = blindBoxPriceService.getBaseMapper().selectList(queryWrapper);
        if(!priceList.isEmpty()) {
            List<String> names = priceList.stream().map(Price::getName).collect(Collectors.toList());
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_USE_DISABLE_ERROR,
                    JSONUtil.toJsonStr(names));
        }
    }

    /**
     * 校验是否存在
     *
     * @param id 分类ID
     * @return
     */
    private BlindBox checkExist(String id) {
        BlindBox blindBox = getById(id);
        if (blindBox == null) {
            log.error("盲盒ID为" + id + "的盲盒不存在");
            throw new ServiceException(ResultCode.BLIND_BOX_NOT_EXIST);
        }
        return blindBox;
    }
}
