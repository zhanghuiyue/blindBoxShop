package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlindBoxDTO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类图标")
    private String image;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "排序值")
    private String sortOrder;

    @ApiModelProperty(value = "盲盒的分类名称")
    private String categoryName;

    @ApiModelProperty(value = "盲盒的分类id")
    private String categoryId;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "盲盒类型，FREE免费，CHARGE收费")
    private String blindBoxType;
}
