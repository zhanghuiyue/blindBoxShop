package cn.lili.modules.goods.entity.vos;

import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dos.ReplaceDetail;
import cn.lili.modules.goods.entity.dto.GoodsParamsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 置换订单 详细VO
 *
 * @author pikachu
 * @since 2020-02-26 23:24:13
 */
@Data
public class ReplaceDetailVO extends ReplaceDetail {

    private static final long serialVersionUID = 6377623919990713567L;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "小图路径")
    private String small;

}
