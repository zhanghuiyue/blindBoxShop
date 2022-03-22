package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("li_blind_box_goods")
@ApiModel(value = "盲盒商品")
public class BlindBoxGoods extends BaseIdEntity {

    private static final long serialVersionUID = -8236865838438521426L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "审核信息")
    private String authMessage;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "品牌图标")
    private String logo;

    @ApiModelProperty(value = "品牌名称")
    private String name;

    @ApiModelProperty(value = "计量单位")
    private String goodsUnit;

    @ApiModelProperty(value = "商品详情")
    private String intro;

    @ApiModelProperty(value = "商品价格")
    private Double price;

    @ApiModelProperty(value = "审核状态")
    private Double authFlag;

    @ApiModelProperty(value = "小图路径")
    private String small;

    @ApiModelProperty(value = "大图路径")
    private String big;

    @ApiModelProperty(value = "库存")
    private Integer quantity;

    @ApiModelProperty(value = "中奖概率")
    private Integer probability;

    @ApiModelProperty(value = "种类id")
    private Float blindBoxCategory;

    @ApiModelProperty(value = "商品规格")
    private String specs;
}
