package cn.lili.modules.blindBox.entity.dto.search;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.enums.SeckillStatusEnum;
import cn.lili.modules.blindBox.tool.SeckillTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 秒杀活动查询通用类
 *
 * @author paulG
 * @since 2020/8/21
 **/
@Data
public class SeckillBoxSearchParams  extends PageVO implements Serializable {

    private static final long serialVersionUID = -4052716630253333681L;

    @ApiModelProperty(value = "活动名称")
    private String promotionName;

    @ApiModelProperty(value = "活动开始时间")
    private Long startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Long endTime;

    /**
     * @see SeckillStatusEnum
     */
    @ApiModelProperty(value = "活动状态 如需同时判断多个活动状态','分割")
    private String promotionStatus;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = this.baseQueryWrapper();

        if (CharSequenceUtil.isNotEmpty(promotionStatus)) {
            queryWrapper.and(i -> {
                for (String status : promotionStatus.split(",")) {
                    i.or(SeckillTools.queryPromotionStatus(SeckillStatusEnum.valueOf(status)));
                }
            });
        }
        if (CharSequenceUtil.isNotEmpty(promotionName)) {

            queryWrapper.like("promotion_name", promotionName);
        }
        return queryWrapper;
    }
    public <T> QueryWrapper<T> baseQueryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (startTime != null) {
            queryWrapper.ge("start_time", new Date(startTime));
        }
        if (endTime != null) {
            queryWrapper.le("end_time", new Date(endTime));
        }

        queryWrapper.eq("delete_flag", false);
        return queryWrapper;
    }

}
