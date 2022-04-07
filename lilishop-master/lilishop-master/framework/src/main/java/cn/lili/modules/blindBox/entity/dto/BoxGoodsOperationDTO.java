package cn.lili.modules.blindBox.entity.dto;

import cn.lili.common.validation.EnumValue;
import cn.lili.modules.goods.entity.dto.GoodsParamsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 盲盒商品编辑DTO
 *
 * @author pikachu
 * @since 2020-02-24 19:27:20
 */
@Data
@ToString
public class BoxGoodsOperationDTO implements Serializable {

    private static final long serialVersionUID = -509667581371776913L;

    @ApiModelProperty(hidden = true)
    private String goodsId;

    @ApiModelProperty(value = "商品价格", required = true)
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为负数")
    @Max(value = 99999999, message = "商品价格不能超过99999999")
    private Double price;

    @ApiModelProperty(value = "品牌id")
    @Min(value = 0, message = "品牌值不正确")
    private String brandId;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotEmpty(message = "商品名称不能为空")
    @Length(max = 50, message = "商品名称不能超过50个字符")
    private String goodsName;

    @ApiModelProperty(value = "商品详情")
    private String intro;

    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能为负数")
    @Max(value = 99999999, message = "库存不能超过99999999")
    private Integer quantity;

    @ApiModelProperty(value = "商品图片")
    private List<String> goodsGalleryList;


    @ApiModelProperty(value = "商品单位", required = true)
    private String goodsUnit;

    @ApiModelProperty(value = "商品描述")
    private String info;

    /**
     * @see cn.lili.modules.goods.entity.enums.GoodsTypeEnum
     */
    @ApiModelProperty(value = "商品类型")
    @EnumValue(strValues = {"PHYSICAL_GOODS", "VIRTUAL_GOODS", "E_COUPON"}, message = "商品类型参数值错误")
    private String goodsType;


    @ApiModelProperty(value = "抽奖概率", required = true)
    @NotNull(message = "抽奖概率不能为空")
    @Min(value = 0, message = "抽奖概率不能为负数")
    @Max(value = 1, message = "抽奖概率不能超过1")
    private Double  probability;


    @ApiModelProperty(value = "盲盒id")
    private String blindBoxId;

    @Max(value = 99999999, message = "元气豆超过99999999")
    @ApiModelProperty(value = "元气豆")
    private Integer sinewyBean;

    @ApiModelProperty(value = "品牌介绍")
    private String brandIntro;

    @ApiModelProperty(value = "品牌名称")
    private String name;

}
