package cn.lili.modules.blindBox.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "盲盒")
@TableName("li_blind_box")
@NoArgsConstructor
public class BlindBoxCategory extends BaseEntity {

    private static final long serialVersionUID = -4878132663540847325L;
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

    @ApiModelProperty(value = "盲盒类型，FREE免费，CHARGE收费")
    private String blindBoxType;

}
