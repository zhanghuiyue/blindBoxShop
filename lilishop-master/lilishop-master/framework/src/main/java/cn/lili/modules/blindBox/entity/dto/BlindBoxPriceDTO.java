package cn.lili.modules.blindBox.entity.dto;

import io.swagger.annotations.ApiModelProperty;

public class BlindBoxPriceDTO {
    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "原价")
    private Double originalPrice;

    @ApiModelProperty(value = "优惠价")
    private Double discount;

    @ApiModelProperty(value = "数量")
    private Integer num;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
