package cn.lili.modules.order.order.service;

import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 盲盒订单业务层实现
 */
public interface BlindBoxOrderService  extends IService<BlindBoxOrder> {

    /**
     * 创建订单
     * @param order 订单
     * @return  BlindBoxOrder
     */
    BlindBoxOrder createOrder(BlindBoxOrder order);

    /**
     * 查询订单
     * @param sn
     * @return
     */
    BlindBoxOrder queryOrder(String sn);
    /**
     * 系统取消订单
     *
     * @param orderSn 订单编号
     * @param reason  错误原因
     */
    void systemCancel(String orderSn, String reason);

    /**
     * 根据sn查询
     *
     * @param orderSn 订单编号
     * @return 订单信息
     */
    BlindBoxOrder getBySn(String orderSn);
}
