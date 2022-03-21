package cn.lili.modules.blindBox.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderParam {
    @ApiModelProperty(value = "盲盒总价格")
    private Double flowPrice;

    @ApiModelProperty(value = "盲盒原价")
    private Double goodsPrice;

    @ApiModelProperty(value = "盲盒优惠金额")
    private String discountPrice;

    @ApiModelProperty(value = "种类id")
    private String blindBoxCategory;

    @ApiModelProperty(value = "种类名称")
    private String name;

    @ApiModelProperty(value = "从哪个模版领取的优惠券")
    private String couponId;

    @ApiModelProperty(value = "盲盒的数量")
    private String goodsNum;
}
