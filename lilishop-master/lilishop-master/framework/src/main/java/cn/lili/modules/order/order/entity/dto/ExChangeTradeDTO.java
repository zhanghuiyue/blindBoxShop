package cn.lili.modules.order.order.entity.dto;

import cn.lili.modules.member.entity.dos.MemberAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 购物车视图
 *
 * @author Chopper
 * @since 2020-03-25 2:30 下午
 */
@Data
public class ExChangeTradeDTO implements Serializable {

    private static final long serialVersionUID = -3137165707807057810L;

    @ApiModelProperty(value = "sn")
    private String sn;

    /**
     * 收货地址
     */
    private MemberAddress memberAddress;


    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 买家名称
     */
    private String memberName;

    /**
     * 买家id
     */
    private String memberId;

    /**
     * 兑换池中的商品
     */
    private String  skuId;

}
