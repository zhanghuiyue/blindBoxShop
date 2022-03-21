package cn.lili.modules.order.order.service;

import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BlindBoxOrderService  extends IService<BlindBoxOrder> {

    BlindBoxOrder createOrder(BlindBoxOrder order);
    BlindBoxOrder queryOrder(String sn);
}
