package cn.lili.modules.order.order.service;

import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dto.BoxOrderSearchParams;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.BoxOrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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


    /**
     * 订单查询
     *
     * @param boxOrderSearchParams 查询参数
     * @return 简短订单列表
     */
    List<BoxOrderSimpleVO> queryByParams(BoxOrderSearchParams boxOrderSearchParams);


    /**
     * 查询订单详情
     *
     * @param  sn 查询参数
     * @return 简短订单列表
     */

    BoxOrderSimpleVO queryDetailBySn(String sn);


    /**
     * 根据订单sn逻辑删除订单
     *
     * @param sn 订单sn
     */
    void deleteOrderBySn(String sn);
}
