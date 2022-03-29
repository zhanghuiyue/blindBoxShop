package cn.lili.modules.order.order.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.message.entity.dos.StoreMessage;
import cn.lili.modules.order.order.aop.OrderLogPoint;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.OrderItem;
import cn.lili.modules.order.order.entity.dto.BoxOrderSearchParams;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.vo.BoxOrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.mapper.BlindBoxOrderMapper;
import cn.lili.modules.order.order.mapper.OrderMapper;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.OrderItemService;
import cn.lili.modules.order.order.service.OrderService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 盲盒订单业务层实现
 */
@Service
public class BlindBoxOrderServicelmpl extends ServiceImpl<BlindBoxOrderMapper, BlindBoxOrder> implements BlindBoxOrderService {


    /**
     * 商品订单
     */
    @Autowired
    private OrderService orderService;

    /**
     * 订单货物
     */
    @Autowired
    private OrderItemService orderItemService;


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

    @Override
    public List<BoxOrderSimpleVO> queryByParams(BoxOrderSearchParams boxOrderSearchParams) {
        QueryWrapper queryWrapper = boxOrderSearchParams.queryWrapper();

        queryWrapper.orderByDesc("o.id");
        List<BoxOrderSimpleVO> boxList = this.baseMapper.getBoxOrderSimple(queryWrapper);
        List<BoxOrderSimpleVO> goodsList = this.baseMapper.getGoodsOrderSimple(queryWrapper);
        boxList.addAll(goodsList);
        return boxList;
    }

    /**
     * 复合查询订单
     * @param sn 订单编号
     * @return BlindBoxOrder
     */
    @Override
    public BoxOrderSimpleVO queryDetailBySn(String sn) {

        BoxOrderSimpleVO boxOrderSimpleVO =  this.baseMapper.getBoxOrderDetail(sn) ;
        if(null ==boxOrderSimpleVO ) {

            boxOrderSimpleVO =  this.baseMapper.getGoodsOrderDetail(sn) ;

        }
        return boxOrderSimpleVO;
    }


    /**
     * 根据订单sn逻辑删除订单
     *
     * @param sn 订单sn
     */
    public  void deleteOrderBySn(String sn) {

        BlindBoxOrder blindBoxOrder = this.getBySn(sn);
        Order order = this.orderService.getBySn(sn);

        if (blindBoxOrder == null &&order==null ) {
            log.error("订单号为" + sn + "的订单不存在！");
            throw new ServiceException();
        }

        if(order!=null) {
            LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Order::getSn, sn).set(Order::getDeleteFlag, true);
            this.orderService.update(updateWrapper);
            LambdaUpdateWrapper<OrderItem> orderItemLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            orderItemLambdaUpdateWrapper.eq(OrderItem::getOrderSn, sn).set(OrderItem::getDeleteFlag, true);
            this.orderItemService.update(orderItemLambdaUpdateWrapper);
        }else if(blindBoxOrder!=null) {
            LambdaUpdateWrapper<BlindBoxOrder> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(BlindBoxOrder::getSn, sn).set(BlindBoxOrder::getDeleteFlag, true);
            this.update(updateWrapper);
        }
    }
}
