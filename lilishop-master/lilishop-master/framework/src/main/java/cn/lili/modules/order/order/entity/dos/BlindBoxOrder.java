package cn.lili.modules.order.order.entity.dos;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.blindBox.entity.vo.OrderParam;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.enums.DeliveryMethodEnum;
import cn.lili.modules.order.cart.entity.vo.CartVO;
import cn.lili.modules.order.order.entity.enums.*;
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
@TableName("li_blind_box_order")
@ApiModel(value = "订单")
@NoArgsConstructor
public class BlindBoxOrder extends BaseEntity {

    private static final long serialVersionUID = 2233811628066468683L;
    @ApiModelProperty("订单编号")
    private String sn;

    @ApiModelProperty(value = "种类id")
    private String blindBoxCategory;

    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "会员ID")
    private String memberId;

    @ApiModelProperty(value = "用户名")
    private String memberName;

    /**
     * @see OrderStatusEnum
     */
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @ApiModelProperty(value = "抽取状态")
    private String extractStatus;

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

    @ApiModelProperty(value = "总价格")
    private Double flowPrice;

    @ApiModelProperty(value = "商品价格")
    private Double goodsPrice;

    @ApiModelProperty(value = "优惠的金额")
    private Double discountPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "订单取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    @ApiModelProperty(value = "支付方式返回的交易号")
    private String payOrderNo;

    @ApiModelProperty(value = "优惠券id")
    private String couponId;

    @ApiModelProperty(value = "购买方式")
    private String buyWay;

    public BlindBoxOrder(OrderParam orderParam) {
        String oldId = this.getId();
        BeanUtil.copyProperties(orderParam, this);
        setId(oldId);

        //设置默认支付状态
        this.setOrderStatus(OrderStatusEnum.UNPAID.name());
        this.setPayStatus(PayStatusEnum.UNPAID.name());
        this.setExtractStatus(ExtractStatusEnum.UNEXTRACT.getState());
        this.setBuyWay(BuyWayEnum.CASH.name());
    }
}
