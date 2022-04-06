package cn.lili.modules.goods.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GiveGoodsDTO {

    @ApiModelProperty(value = "赠送商品编号")
    private String giveGoodsId;

    @ApiModelProperty(value = "赠送商品sku编号")
    private String giveSkuId;

    @ApiModelProperty(value = "赠送商品类型，0表示奖品，1表示置换商品，2表示购买商品,3表示盲盒")
    private String giveGoodsType;

    @ApiModelProperty(value = "盲盒类型，FREE免费，CHARGE收费")
    private String blindBoxType;

    @ApiModelProperty(value = "id")
    private String id;
}
