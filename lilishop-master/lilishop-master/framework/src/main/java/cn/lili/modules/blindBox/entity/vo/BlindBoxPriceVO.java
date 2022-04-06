package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dto.BlindBoxCouponDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class BlindBoxPriceVO {

    @ApiModelProperty(value = "盲盒价格列表")
    public List<BlindBoxPriceDTO> blindBoxPriceDTOList;

    @ApiModelProperty(value = "盲盒可用优惠券列表")
    public List<BlindBoxCouponDTO> canUseCouponList;

    @ApiModelProperty(value = "盲盒不可用优惠券列表")
    public List<BlindBoxCouponDTO> unUseCouponList;
}
