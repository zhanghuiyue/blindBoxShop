package cn.lili.modules.blindBox.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@ApiModel(value = "盲盒价格")
@TableName("li_price")
@Getter
@Setter
@NoArgsConstructor
public class Price extends BaseEntity {

    private static final long serialVersionUID = -4878132663540847325L;
    @ApiModelProperty(value = "价格")
    private Double price;

    @ApiModelProperty(value = "原价")
    private Double originalPrice;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "种类id")
    private String blindBoxCategory;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getBlindBoxCategory() {
        return blindBoxCategory;
    }

    public void setBlindBoxCategory(String blindBoxCategory) {
        this.blindBoxCategory = blindBoxCategory;
    }
}