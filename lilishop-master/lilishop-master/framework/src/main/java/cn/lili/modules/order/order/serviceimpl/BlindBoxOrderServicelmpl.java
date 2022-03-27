package cn.lili.modules.order.order.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.modules.message.entity.dos.StoreMessage;
import cn.lili.modules.order.order.aop.OrderLogPoint;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.mapper.BlindBoxOrderMapper;
import cn.lili.modules.order.order.mapper.OrderMapper;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 盲盒订单业务层实现
 */
@Service
public class BlindBoxOrderServicelmpl extends ServiceImpl<BlindBoxOrderMapper, BlindBoxOrder> implements BlindBoxOrderService {
    /**
     * 创建订单
     * @param order 订单
     * @return BlindBoxOrder
     */
    @Override
    public BlindBoxOrder createOrder(BlindBoxOrder order) {
        this.baseMapper.insert(order);
        return order;
    }

    /**
     * 查询订单
     * @param sn 订单编号
     * @return BlindBoxOrder
     */
    @Override
    public BlindBoxOrder queryOrder(String sn) {
        QueryWrapper<BlindBoxOrder> queryWrapper = new QueryWrapper<>();
        //消息id查询
        if (CharSequenceUtil.isNotEmpty(sn)) {
            queryWrapper.eq("sn",sn);
        }
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 系统取消订单
     * @param orderSn 订单编号
     * @param reason  错误原因
     */
    @Override
    @OrderLogPoint(description = "'订单['+#orderSn+']系统取消，原因为：'+#reason", orderSn = "#orderSn")
    @Transactional(rollbackFor = Exception.class)
    public void systemCancel(String orderSn, String reason) {
        BlindBoxOrder order = this.getBySn(orderSn);
        order.setOrderStatus(OrderStatusEnum.CANCELLED.name());
        order.setCancelReason(reason);
        this.updateById(order);
        //orderStatusMessage(order); todo 不清楚发消息的用途
    }

    /**
     * 根据订单号获取订单信息
     * @param orderSn 订单编号
     * @return BlindBoxOrder
     */
    @Override
    public BlindBoxOrder getBySn(String orderSn) {
        return this.getOne(new LambdaQueryWrapper<BlindBoxOrder>().eq(BlindBoxOrder::getSn, orderSn));
    }
}
