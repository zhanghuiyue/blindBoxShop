package cn.lili.modules.blindBox.entity.dos;

import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.enums.SeckillStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 秒杀活动实体类
 *
 * @author Chopper
 * @since 2020-03-19 10:44 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("li_box_seckill")
@ApiModel(value = "盲盒秒杀活动活动")
@NoArgsConstructor
@ToString(callSuper = true)
public class BoxSeckill extends BaseEntity {

    private static final long serialVersionUID = -9116425737163730836L;

    @ApiModelProperty(value = "开启几点场 例如：6，8，12")
    @NotNull(message = "活动时间段不能为空")
    private String hours;

    @ApiModelProperty(value = "活动开始时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || yyyy/MM/dd HH:mm:ss|| yyyy/MM/dd ||epoch_millis")
    private Date endTime;

    @NotEmpty(message = "活动名称不能为空")
    @ApiModelProperty(value = "活动名称", required = true)
    private String promotionName;



    /**
     * @return 秒杀状态
     * @see SeckillStatusEnum
     */
    public String getPromotionStatus() {
        if (endTime == null) {
            return startTime != null ? SeckillStatusEnum.START.name() : SeckillStatusEnum.CLOSE.name();
        }
        Date now = new Date();
        if (now.before(startTime)) {
            return SeckillStatusEnum.NEW.name();
        } else if (endTime.before(now)) {
            return SeckillStatusEnum.END.name();
        } else if (now.before(endTime)) {
            return SeckillStatusEnum.START.name();
        }
        return SeckillStatusEnum.CLOSE.name();
    }
}