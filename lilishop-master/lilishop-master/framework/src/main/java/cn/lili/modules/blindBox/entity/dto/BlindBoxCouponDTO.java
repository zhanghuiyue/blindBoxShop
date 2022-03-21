package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class BlindBoxCouponDTO {
    @ApiModelProperty(value = "面额")
    private Double price;

    @ApiModelProperty(value = "折扣")
    private Double discount;

    @ApiModelProperty(value = "消费门槛")
    private Double consumeThreshold;

    @ApiModelProperty(value = "使用起始时间")
    private Date startTime;

    @ApiModelProperty(value = "使用截止时间")
    private Date endTime;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "从哪个模版领取的优惠券")
    private String couponId;

}
