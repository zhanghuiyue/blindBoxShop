package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlindBoxGoodsDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "小图路径")
    private String small;

    @ApiModelProperty(value = "商品价格")
    private Double price;

    @ApiModelProperty(value = "品牌图标")
    private String logo;

    @ApiModelProperty(value = "详情")
    private String intro;

    @ApiModelProperty(value = "大图标路径")
    private String big;

}
