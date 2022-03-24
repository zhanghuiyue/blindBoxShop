package cn.lili.modules.order.order.entity.vo;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        AuthUser currentUser = UserContext.getCurrentUser();
        //关键字查询

        if (currentUser != null) {
            wrapper.eq( "a.member_id", currentUser.getId());

        }

        //按标签查询
        if (CharSequenceUtil.isNotEmpty(orderStatus)) {
            if(!"ALL".equals(orderStatus)){
                wrapper.eq( "a.order_status", orderStatus);
            }
        }
        return wrapper;
    }
}
