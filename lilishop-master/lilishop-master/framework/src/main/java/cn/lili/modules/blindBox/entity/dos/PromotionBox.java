package cn.lili.modules.blindBox.entity.dos;

import cn.hutool.core.bean.BeanUtil;
import cn.lili.common.enums.PromotionTypeEnum;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.promotion.entity.dos.PointsGoods;
import cn.lili.modules.promotion.entity.dto.KanjiaActivityGoodsDTO;
import cn.lili.modules.promotion.entity.enums.PromotionsScopeTypeEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 促销活动商品实体类
 *
 * @author Chopper
 * @since 2020-03-19 10:44 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_promotion_box")
@ApiModel(value = "秒杀盲盒")
@NoArgsConstructor
public class PromotionBox extends BaseEntity {

    private static final long serialVersionUID = 4150737500248136108L;



    @ApiModelProperty(value = "盲盒id")
    private String boxId;


    @ApiModelProperty(value = "商品名称")
    private String boxName;

    @ApiModelProperty(value = "缩略图")
    private String thumbnail;

    @ApiModelProperty(value = "活动开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis")
    private Date endTime;

    @ApiModelProperty(value = "活动id")
    private String seckillId;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "卖出的商品数量")
    private Integer num;

    @ApiModelProperty(value = "原价")
    private Double originalPrice;

    @ApiModelProperty(value = "促销价格")
    private Double price;


    @ApiModelProperty(value = "限购数量")
    private Integer limitNum;

    @ApiModelProperty(value = "促销库存")
    private Integer quantity;


    public PromotionBox(BlindBoxCategory blindBoxCategory) {
        if (blindBoxCategory != null) {
            BeanUtil.copyProperties(blindBoxCategory, this, "id", "price");
            this.boxId = blindBoxCategory.getId();
            this.originalPrice = blindBoxCategory.getPrice();
        }
    }

}