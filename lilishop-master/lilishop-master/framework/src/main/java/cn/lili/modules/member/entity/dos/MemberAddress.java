package cn.lili.modules.member.entity.dos;

import cn.lili.common.security.sensitive.Sensitive;
import cn.lili.common.security.sensitive.enums.SensitiveStrategy;
import cn.lili.common.validation.Phone;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 会员地址
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Data
@TableName("li_member_address")
@ApiModel(value = "会员地址")
public class MemberAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会员ID", hidden = true)
    private String memberId;

    @NotEmpty(message = "收货人姓名不能为空")
    @ApiModelProperty(value = "收货人姓名")
    private String name;

    @Phone
    @ApiModelProperty(value = "手机号码")
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "地址名称， '，'分割")
    private String consigneeAddressPath;

    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "地址id，'，'分割 ")
    private String consigneeAddressIdPath;

    @NotEmpty(message = "详细地址不能为空")
    @ApiModelProperty(value = "详细地址")
    private String detail;

    @ApiModelProperty(value = "是否为默认收货地址")
    private Boolean isDefault;

    @ApiModelProperty(value = "地址别名")
    private String alias;

    @ApiModelProperty(value = "经度")
    private String lon;

    @ApiModelProperty(value = "纬度")
    private String lat;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getConsigneeAddressPath() {
        return consigneeAddressPath;
    }

    public void setConsigneeAddressPath(String consigneeAddressPath) {
        this.consigneeAddressPath = consigneeAddressPath;
    }

    public String getConsigneeAddressIdPath() {
        return consigneeAddressIdPath;
    }

    public void setConsigneeAddressIdPath(String consigneeAddressIdPath) {
        this.consigneeAddressIdPath = consigneeAddressIdPath;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}