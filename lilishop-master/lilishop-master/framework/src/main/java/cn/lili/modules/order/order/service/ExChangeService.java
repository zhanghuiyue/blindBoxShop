package cn.lili.modules.order.order.service;



import cn.lili.modules.order.cart.entity.vo.TradeParams;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.Trade;
import cn.lili.modules.order.order.entity.dto.ExChangeTradeDTO;
import cn.lili.modules.order.order.entity.vo.ExChangeParams;

/**
 * 兑换商品业务层
 *
 * @author Chopper
 * @since 2020-03-23 12:29 下午
 */
public interface ExChangeService {


    /**
     * 兑换池加入一个商品
     *
     * @param skuId    要写入的skuId
     * @param num      要加入兑换池的数量
     * @param cartType 兑换池类型
     * @param cover    是否覆盖兑换池的数量，如果为否则累加，否则直接覆盖
     */
    void add(String skuId, Integer num, String cartType, Boolean cover);



    /**
     * 创建交易
     * 1.获取兑换池类型，不同的兑换池类型有不同的订单逻辑
     * 兑换池类型：兑换池、立即购买、虚拟商品、拼团、积分
     * 2.校验用户的收件人信息
     * 3.设置交易的基础参数
     * 4.交易信息存储到缓存中
     * 5.创建交易
     * 6.清除兑换池选择数据
     *
     * @param tradeParams 创建交易参数
     * @return 交易信息
     */
    Trade createTrade(TradeParams tradeParams);



}
