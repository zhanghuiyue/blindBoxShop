package cn.lili.modules.order.order.service;

import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dos.SubstitutionOrder;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSearchParams;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSimpleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SubstitutionOrderService extends IService<SubstitutionOrder> {

    /**
     * 置换订单查询
     *
     * @param substitutionOrderSearchParams 订单状态
     * @return
     */
    SubstitutionOrderSimpleVO querySubstitutionOrder(SubstitutionOrderSearchParams substitutionOrderSearchParams);
}
