package cn.lili.modules.order.order.entity.vo;

import cn.lili.common.vo.PageResult;
import cn.lili.modules.order.order.entity.dto.SubstitutionGoodsDTO;
import cn.lili.modules.order.order.entity.dto.SubstitutionOrderDTO;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 置换订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubstitutionOrderSimpleVO extends PageResult implements Serializable {

    private static final long serialVersionUID = -8402457457074092957L;

    @ApiModelProperty(value = "置换订单列表")
    private List<SubstitutionOrderDTO> substitutionOrderDTOList;

}
