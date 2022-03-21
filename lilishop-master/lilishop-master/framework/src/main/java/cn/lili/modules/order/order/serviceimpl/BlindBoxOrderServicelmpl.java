package cn.lili.modules.order.order.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.modules.message.entity.dos.StoreMessage;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.mapper.BlindBoxOrderMapper;
import cn.lili.modules.order.order.mapper.OrderMapper;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BlindBoxOrderServicelmpl extends ServiceImpl<BlindBoxOrderMapper, BlindBoxOrder> implements BlindBoxOrderService {
    @Override
    public BlindBoxOrder createOrder(BlindBoxOrder order) {
        this.baseMapper.insert(order);
        return order;
    }

    @Override
    public BlindBoxOrder queryOrder(String sn) {
        QueryWrapper<BlindBoxOrder> queryWrapper = new QueryWrapper<>();
        //消息id查询
        if (CharSequenceUtil.isNotEmpty(sn)) {
            queryWrapper.eq("sn",sn);
        }
        return this.baseMapper.selectOne(queryWrapper);
    }
}
