package cn.lili.modules.goods.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 赠送商品vo
 */
@Data
public class GiveGoodsVO {

    @ApiModelProperty(value = "赠送码")
    private String giveCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品规格")
    private String specs;

    @ApiModelProperty(value = "小图路径")
    private String small;
}
