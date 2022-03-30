package cn.lili.modules.promotion.entity.dto.search;

import cn.lili.common.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 会员优惠券
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberCouponQuery extends PageVO implements Serializable {

    private static final long serialVersionUID = 4566880169478260409L;

    @ApiModelProperty(value = "查询类型，0表示可用优惠券，1表示不可用优惠券")
    private String queryType;

    @ApiModelProperty(value = "不可用类型，0表示已使用，1表示已过期")
    private String unUseType;

}
