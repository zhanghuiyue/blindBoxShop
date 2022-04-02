package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dto.BlindBoxCouponDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxGoodsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BlindBoxGoodsVO {

    @ApiModelProperty(value = "盲盒商品信息列表")
    private List<BlindBoxGoodsDTO> blindBoxGoodsDTOS;

    @ApiModelProperty(value = "优惠券列表")
    private List<BlindBoxCouponDTO> blindBoxCouponDTOS;
}
