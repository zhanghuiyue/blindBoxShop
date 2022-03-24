package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlindBoxCategoryDTO {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类图标")
    private String image;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "排序值")
    private String sortOrder;

    @ApiModelProperty(value = "盲盒的标签")
    private String label;

    @ApiModelProperty(value = "价格")
    private Double price;
}
