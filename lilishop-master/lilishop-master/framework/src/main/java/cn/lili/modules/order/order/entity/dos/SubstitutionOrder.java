package cn.lili.modules.order.order.entity.dos;

import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.enums.PayStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_substitution_order")
@ApiModel(value = "订单")
@NoArgsConstructor
public class SubstitutionOrder extends BaseEntity {

    private static final long serialVersionUID = 2233811628066468683L;
    @ApiModelProperty("订单编号")
    private String sn;

    @ApiModelProperty(value = "会员ID")
    private String memberId;

    @ApiModelProperty(value = "用户名")
    private String memberName;

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    /**
     * @see PayStatusEnum
     */
    @ApiModelProperty(value = "付款状态")
    private String payStatus;

    @ApiModelProperty(value = "第三方付款流水号")
    private String receivableNo;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty(value = "付款金额")
    private Double payAmount;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "订单取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    @ApiModelProperty(value = "支付方式返回的交易号")
    private String payOrderNo;

    @ApiModelProperty(value = "'置换商品编号用逗号隔开")
    private String goodsIdPath;

    @ApiModelProperty(value = "'购买商品编号")
    private String buyGoodsId;

    @ApiModelProperty(value = "'购买商品skuid")
    private String skuId;
}
