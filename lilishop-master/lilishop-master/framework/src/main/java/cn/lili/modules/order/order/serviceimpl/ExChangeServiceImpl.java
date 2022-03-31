package cn.lili.modules.order.order.serviceimpl;


import cn.lili.cache.Cache;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.SnowFlake;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.member.entity.dos.MemberAddress;
import cn.lili.modules.member.service.MemberAddressService;
import cn.lili.modules.order.cart.entity.vo.CartSkuVO;
import cn.lili.modules.order.cart.render.TradeBuilder;
import cn.lili.modules.order.order.entity.dos.ExChangeTrade;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.OrderItem;
import cn.lili.modules.order.order.entity.dos.Trade;
import cn.lili.modules.order.order.entity.dto.ExChangeTradeDTO;
import cn.lili.modules.order.order.entity.enums.ExChangeTypeEnum;
import cn.lili.modules.order.order.entity.vo.ExChangeParams;
import cn.lili.modules.order.order.service.ExChangeService;
import cn.lili.modules.order.order.service.OrderItemService;
import cn.lili.modules.order.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 兑换商品业务层实现
 *
 * @author Chopper
 * @since 2020-03-23 12:29 下午
 */
@Slf4j
@Service
public class ExChangeServiceImpl implements ExChangeService {

    static String errorMessage = "兑换商品，请稍后重试";

    /**
     * 商品信息
     */
    @Autowired
    private GoodsSkuService goodsSkuService;

    /**
     * 缓存
     */
    @Autowired
    private Cache<Object> cache;

    /**
     * 会员地址
     */
    @Autowired
    private MemberAddressService memberAddressService;

    /**
     * 订单信息
     */
    @Autowired
    private OrderService orderService;


    /**
     * 子订单
     */
    @Autowired
    private OrderItemService orderItemService;


    /**
     *
     * @param skuId    要写入的skuId
     * @param num      要加入兑换池的数量
     */
    @Override
    public void add(String skuId, Integer num) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if (num <= 0) {
            throw new ServiceException(ResultCode.EXCHANGE_ERROR);
        }
        ExChangeTypeEnum exChanageTypeEnum = getCartType();
        GoodsSku dataSku = checkGoods(skuId);
        ExChangeTradeDTO exChangeTradeDTO = new ExChangeTradeDTO();
        exChangeTradeDTO.setMemberId(currentUser.getId());
        exChangeTradeDTO.setMemberName(currentUser.getUsername());
        exChangeTradeDTO.setSkuId(skuId);
        //检测是否可购买
        checkSetGoodsQuantity(skuId, 1);
        //检测缓存是否存在，不存在就写入缓存
        this.resetExChangeTradeDTO( exChangeTradeDTO);

    }


    /**
     * 校验商品有效性，判定失效和库存
     *
     * @param skuId 商品skuId
     */
    private GoodsSku checkGoods(String skuId) {
        GoodsSku dataSku = this.goodsSkuService.getGoodsSkuByIdFromCache(skuId);
        if (dataSku == null) {
            throw new ServiceException(ResultCode.GOODS_NOT_EXIST);
        }
        if (!GoodsAuthEnum.PASS.name().equals(dataSku.getAuthFlag()) || !GoodsStatusEnum.UPPER.name().equals(dataSku.getMarketEnable())) {
            throw new ServiceException(ResultCode.GOODS_NOT_EXIST);
        }
        return dataSku;
    }


    /**
     * 读取当前会员购物原始数据key
     *
     * @param exChangeTypeEnum 获取方式
     * @return 当前会员购物原始数据key
     */
    private String getOriginKey(ExChangeTypeEnum exChangeTypeEnum) {

        //缓存key，默认使用购物车
        if (exChangeTypeEnum != null) {
            AuthUser currentUser = UserContext.getCurrentUser();
            return exChangeTypeEnum.getPrefix() + currentUser.getId();
        }
        throw new ServiceException(ResultCode.ERROR);
    }

    /**
     * 获取购物车类型
     *
     * @return
     */
    private ExChangeTypeEnum getCartType() {
        //默认购物车
        ExChangeTypeEnum cartTypeEnum = ExChangeTypeEnum.BUY_NOW;
        return cartTypeEnum;
    }


    public void resetExChangeTradeDTO(ExChangeTradeDTO exChangeTradeDTO) {
        cache.put(this.getOriginKey(ExChangeTypeEnum.BUY_NOW), exChangeTradeDTO);
    }


    /**
     * 检查并设置兑换池商品数量
     *
     * @param skuId     商品id
     * @param num       购买数量
     */
    private void checkSetGoodsQuantity( String skuId, Integer num) {
        Integer enableStock = goodsSkuService.getStock(skuId);

        //如果sku的可用库存小于等于0或者小于用户购买的数量，则不允许购买
        if (enableStock <= 0 || enableStock < num) {
            throw new ServiceException(ResultCode.GOODS_SKU_QUANTITY_NOT_ENOUGH);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createExChangeTrade(ExChangeParams exChangeParams) {


        //判断商品库存
        GoodsSku  goodsSku = this.goodsSkuService.getById(exChangeParams.getSkuId());
        AuthUser currentUser = UserContext.getCurrentUser();
        MemberAddress memberAddress = this.memberAddressService.getMemberAddress(exChangeParams.getAddressID());
       if(memberAddress==null){

           memberAddress =this.memberAddressService.getDefaultMemberAddress();
       }
        //订单无收货地址校验
        if ( memberAddress== null) {
            throw new ServiceException(ResultCode.MEMBER_ADDRESS_NOT_EXIST);
        }
        //判断库存
        checkSetGoodsQuantity(goodsSku.getId() ,1);

        String sn = "o" + SnowFlake.getId();
        Order order = new Order() ;
        order.setSn(sn) ;
        order.setStoreId(goodsSku.getStoreId());
        order.setMemberId(currentUser.getId());
        order.setMemberName(currentUser.getUsername());
        order.setOrderStatus("UNPAID");
        order.setPayStatus("UNPAID");
        order.setConsigneeName(memberAddress.getName());
        order.setConsigneeMobile(memberAddress.getMobile());
        order.setConsigneeAddressPath(memberAddress.getConsigneeAddressPath());
        order.setConsigneeAddressIdPath(memberAddress.getConsigneeAddressIdPath());
        order.setConsigneeDetail(memberAddress.getDetail());
        order.setStoreName(goodsSku.getStoreName());
        order.setDeliveryMethod("LOGISTICS");

        order.setFreightPrice(0.00);
        order.setDiscountPrice(0.00);
        order.setGoodsNum(1);
        order.setCanReturn(false);
        order.setOrderType("NORMAL");

        OrderItem  orderItem =  new OrderItem();
        orderItem.setOrderSn(sn);
        orderItem.setSn("oi" + SnowFlake.getId());
        orderItem.setGoodsId(goodsSku.getGoodsId());
        orderItem.setSkuId(goodsSku.getId());
        orderItem.setNum(1);
        orderItem.setImage(goodsSku.getThumbnail());
        orderItem.setGoodsName(goodsSku.getGoodsName());

        //判断支付方式
         if(exChangeParams.getPayWay().equals("MONEY")){

             order.setPaymentMethod("MONEY");
             order.setFlowPrice(goodsSku.getPrice());
             order.setGoodsPrice(goodsSku.getPrice());
             order.setSinewyBean(0);

             orderItem.setUnitPrice(goodsSku.getPrice());
             orderItem.setSinewyBean(0);
             orderItem.setSubTotal(goodsSku.getPrice());
             orderItem.setGoodsPrice(goodsSku.getPrice());
             orderItem.setFlowPrice(goodsSku.getPrice());

         }else if (exChangeParams.getPayWay().equals("BEAN")){

             order.setPaymentMethod("BEAN");
             order.setFlowPrice(0.00);
             order.setGoodsPrice(0.00);
             order.setSinewyBean(goodsSku.getSinewyBean());

             orderItem.setUnitPrice(0.00);
             orderItem.setSinewyBean(goodsSku.getSinewyBean());
             orderItem.setGoodsPrice(0.00);
             orderItem.setFlowPrice(0.00);

        }
        this.orderService.save(order);
        this.orderItemService.save(orderItem);

        return order ;
    }


    public ExChangeTradeDTO readExChanageDTO() {
        ExChangeTradeDTO exChangeTradeDTO = (ExChangeTradeDTO) cache.get(this.getOriginKey(ExChangeTypeEnum.BUY_NOW));
        if (exChangeTradeDTO == null) {
            exChangeTradeDTO = new ExChangeTradeDTO();
            AuthUser currentUser = UserContext.getCurrentUser();
            exChangeTradeDTO.setMemberId(currentUser.getId());
            exChangeTradeDTO.setMemberName(currentUser.getUsername());
        }
        if (exChangeTradeDTO.getMemberAddress() == null) {
            exChangeTradeDTO.setMemberAddress(this.memberAddressService.getDefaultMemberAddress());
        }
        return exChangeTradeDTO;
    }



}
