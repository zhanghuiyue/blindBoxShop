package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlindBoxPriceDTO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "原价")
    private Double originalPrice;

    @ApiModelProperty(value = "优惠价")
    private Double discount;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "盲盒id")
    private String blindBoxId;

    @ApiModelProperty(value = "价格名称")
    private String name;

    @ApiModelProperty(value = "排序值")
    private String sortOrder;
}
