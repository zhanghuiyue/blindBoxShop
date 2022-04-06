package cn.lili.modules.blindBox.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@ApiModel(value = "盲盒价格")
@TableName("li_prize")
@Getter
@Setter
@NoArgsConstructor
public class Prize extends BaseEntity {

    private static final long serialVersionUID = -4878132663540847325L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    @ApiModelProperty(value = "会员ID")
    private String memberId;

    @ApiModelProperty(value = "置换标识，0表示已置换")
    private String substitutionFlag;

    @ApiModelProperty(value = "置换次数")
    private int substitutionNum;

    @ApiModelProperty(value = "盲盒id")
    private String blindBoxId;

    @ApiModelProperty(value = "分类图标")
    private String image;

    @ApiModelProperty(value = "分类名称")
    private String name;
}
