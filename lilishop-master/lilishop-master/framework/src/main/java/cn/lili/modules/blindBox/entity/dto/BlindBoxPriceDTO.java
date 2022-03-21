package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlindBoxPriceDTO {
    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "原价")
    private Double originalPrice;

    @ApiModelProperty(value = "优惠价")
    private Double discount;

    @ApiModelProperty(value = "数量")
    private Integer num;

}
