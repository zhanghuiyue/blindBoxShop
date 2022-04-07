package cn.lili.modules.blindBox.entity.dos;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.blindBox.entity.dto.BoxGoodsOperationDTO;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.entity.enums.GoodsTypeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 商品
 *
 * @author pikachu
 * @since 2020-02-23 9:14:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_blind_box_goods")
@ApiModel(value = "盲盒商品")
public class BoxGoods extends BaseEntity {

    private static final long serialVersionUID = 370683495251252601L;

    @ApiModelProperty(value = "商品名称")
    @NotEmpty(message = "商品名称不能为空")
    @Length(max = 100, message = "商品名称太长，不能超过100个字符")
    private String goodsName;

    @ApiModelProperty(value = "商品价格", required = true)
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格不能为负数")
    @Max(value = 99999999, message = "商品价格不能超过99999999")
    private Double price;


    @ApiModelProperty(value = "计量单位")
    private String goodsUnit;



    @ApiModelProperty(value = "详情")
    private String intro;

    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;

    @Max(value = 99999999, message = "库存不能超过99999999")
    @ApiModelProperty(value = "库存")
    private Integer quantity;


    @ApiModelProperty(value = "缩略图路径")
    private String logo;

    @ApiModelProperty(value = "小图路径")
    private String small;

    @ApiModelProperty(value = "原图路径")
    private String big;

    @ApiModelProperty(value = "下架原因")
    private String underMessage;

    @ApiModelProperty(value = "抽中概率", required = true)
    @NotNull(message = "商品抽中概率不能为空")
    @Min(value = 0, message = "商品抽中概率不能为负数")
    @Max(value = 1, message = "商品价格不能超过1")
    private Double probability;

    @ApiModelProperty(value = "盲盒id")
    private String blindBoxId;

    @ApiModelProperty(value = "审核状态")
    private String authFlag;
    /**
     * @see GoodsTypeEnum
     */
    @ApiModelProperty(value = "商品类型", required = true)
    private String goodsType;

    @Max(value = 99999999, message = "元气豆超过99999999")
    @ApiModelProperty(value = "元气豆")
    private Integer sinewyBean;


    @ApiModelProperty(value = "品牌介绍")
    private String brandIntro;

    @ApiModelProperty(value = "品牌名称")
    private String name;


    public BoxGoods() {
    }

    public BoxGoods(BoxGoodsOperationDTO boxGoodsOperationDTO) {
        this.goodsName = boxGoodsOperationDTO.getGoodsName();
        this.goodsUnit = boxGoodsOperationDTO.getGoodsUnit();
        this.intro = boxGoodsOperationDTO.getIntro();
        this.price = boxGoodsOperationDTO.getPrice();
        this.goodsType = boxGoodsOperationDTO.getGoodsType();
        this.probability =boxGoodsOperationDTO.getProbability();
        this.blindBoxId = boxGoodsOperationDTO.getBlindBoxId();
        this.sinewyBean = boxGoodsOperationDTO.getSinewyBean();
        this.brandIntro =boxGoodsOperationDTO.getBrandIntro();
        this.name = boxGoodsOperationDTO.getName();
    }

    public String getIntro() {
        if (CharSequenceUtil.isNotEmpty(intro)) {
            return HtmlUtil.unescape(intro);
        }
        return intro;
    }


}