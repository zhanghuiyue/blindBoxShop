package cn.lili.modules.blindBox.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 秒杀活动商品视图对象
 *
 * @author paulG
 * @since 2020/8/26
 **/
@Data
public class BoxSeckillBoxVO implements Serializable {

    private static final long serialVersionUID = 5170316685407828228L;

    @ApiModelProperty(value = "活动id")
    private String seckillId;

    @ApiModelProperty(value = "时刻")
    private Integer timeLine;

    @ApiModelProperty(value = "盲盒id")
    private String boxId;

    @ApiModelProperty(value = "以积分渠道购买需要积分数量")
    private Integer point;

    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "盲盒图片")
    private String image;

    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "促销数量")
    private Integer quantity;

    @ApiModelProperty(value = "已售数量")
    private Integer salesNum;

    @ApiModelProperty(value = "商品原始价格")
    private Double originalPrice;

}
