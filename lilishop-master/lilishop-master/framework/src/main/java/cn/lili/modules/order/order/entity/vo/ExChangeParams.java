package cn.lili.modules.order.order.entity.vo;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.modules.order.cart.entity.dto.StoreRemarkDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易参数
 *
 * @author paulG
 * @since 2021/2/23
 **/
@Data
public class ExChangeParams implements Serializable {

    private static final long serialVersionUID = -8383072817737513063L;

    @ApiModelProperty(value = "支付方式")
    private String payWay;

    /**
     * @see ClientTypeEnum
     */
    @ApiModelProperty(value = "客户端：H5/移动端 PC/PC端,WECHAT_MP/小程序端,APP/移动应用端")
    private String client;

    @ApiModelProperty(value = "SKUID商品信息")
    private String skuId;


    @ApiModelProperty(value = "收货地址ID")
    private String addressID;



}
