package cn.lili.modules.order.order.entity.vo;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.order.order.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单简略信息
 * 用于订单列表查看
 *
 * @author Chopper
 * @since 2020-08-17 20:28
 */
@Data
public class BoxOrderSimpleVO {

    @ApiModelProperty("value = 订单编号")
    private String sn;

    @ApiModelProperty(value = "总价格")
    private Double flowPrice;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "购买数量")
    private String num;

    @ApiModelProperty(value = "优惠金额")
    private Double discountPrice;

    @ApiModelProperty(value = "商品价格")
    private String goodsPrice;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;

    @ApiModelProperty(value = "商品名称")
    @Setter
    private String name;



}
