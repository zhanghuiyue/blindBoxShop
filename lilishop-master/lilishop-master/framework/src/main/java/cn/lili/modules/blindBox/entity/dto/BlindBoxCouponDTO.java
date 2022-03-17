package cn.lili.modules.blindBox.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

public class BlindBoxCouponDTO {
    @ApiModelProperty(value = "面额")
    private Double price;

    @ApiModelProperty(value = "折扣")
    private Double discount;

    @ApiModelProperty(value = "消费门槛")
    private Double consumeThreshold;

    @ApiModelProperty(value = "使用起始时间")
    private Date startTime;

    @ApiModelProperty(value = "使用截止时间")
    private Date endTime;


    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "从哪个模版领取的优惠券")
    private String couponId;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getConsumeThreshold() {
        return consumeThreshold;
    }

    public void setConsumeThreshold(Double consumeThreshold) {
        this.consumeThreshold = consumeThreshold;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
