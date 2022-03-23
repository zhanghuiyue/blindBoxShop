package cn.lili.modules.order.order.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubstitutionGoodsDTO {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品价格")
    private Double price;

    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "小图路径")
    private String small;
}
