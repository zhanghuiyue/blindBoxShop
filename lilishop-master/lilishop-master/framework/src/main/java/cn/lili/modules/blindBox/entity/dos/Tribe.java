package cn.lili.modules.blindBox.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "盲盒部落")
@TableName("li_tribe")
@NoArgsConstructor
public class Tribe extends BaseEntity {

    private static final long serialVersionUID =  -4878132663540847325L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类图标")
    private String image;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "盲盒类型，FREE免费，CHARGE收费")
    private String blindBoxType;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "会员编号")
    private String memberId;

    @ApiModelProperty(value = "赠送状态，GIVE：赠送，UNGIVE:未赠送，CANCELGIVE：取消赠送，AUTOUNGIVE：自动取消赠送，GIVEED已赠送")
    private String giveStatus;

    @ApiModelProperty(value = "抽取的状态，0表示未抽取，1表示已抽取")
    private String extractStatus;

    @ApiModelProperty(value = "盲盒的状态，VALID表示有效状态，UNVALID表示无效状态")
    private String status;

    @ApiModelProperty(value = "种类id")
    private String blindBoxId;

}
