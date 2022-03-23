package cn.lili.modules.order.order.entity.dto;

import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SubstitutionOrderDTO {

    @ApiModelProperty("订单编号")
    private String sn;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "规格信息json", hidden = true)
    private String specs;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "小图路径")
    private String small;

    @ApiModelProperty(value = "置换商品编号")
    private String goodsIdPath;

    @ApiModelProperty(value = "置换商品列表")
    private List<SubstitutionGoodsDTO> substitutionGoodsDTOList;

    @ApiModelProperty(value = "支付金额")
    private Double payAmount;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

}
