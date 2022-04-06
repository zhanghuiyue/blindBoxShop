package cn.lili.modules.goods.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_give_goods")
@ApiModel(value = "赠送商品")
public class GiveGoods extends BaseEntity {

        @ApiModelProperty(value = "赠送码")
        private String giveCode;

        @ApiModelProperty(value = "赠送商品编号")
        private String giveGoodsId;

        @ApiModelProperty(value = "赠送商品sku编号")
        private String giveSkuId;

        @ApiModelProperty(value = "赠送商品类型，0表示奖品，1表示置换商品，2表示购买商品,3表示盲盒")
        private String giveGoodsType;

        @ApiModelProperty(value = "赠送人")
        private String giveMemberId;

        @ApiModelProperty(value = "被赠送人")
        private String givedMemberId;

        @ApiModelProperty(value = "仓库id或者部落id")
        private String warehouseTribeId;

        @ApiModelProperty(value = "兑换状态，UNEXCHANGE：未兑换，EXCHANGE:兑换")
        private String exchangeStatus;

        @ApiModelProperty(value = "赠送状态，GIVE：赠送，UNGIVE:取消赠送")
        private String giveStatus;

        @ApiModelProperty(value = "取消赠送理由")
        private String cancelReason;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @TableField(fill = FieldFill.INSERT)
        @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis")
        @ApiModelProperty(value = "赠送开始时间")
        private Date startTime;

}
