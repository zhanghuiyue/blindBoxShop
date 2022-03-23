package cn.lili.modules.order.order.entity.vo;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubstitutionOrderSearchParams extends PageVO {

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态, ALL全部 UNPAID 未付款,PAID 已付款,COMPLETED 已完成,CANCELLED 已取消" )
    private String orderStatus;
}
