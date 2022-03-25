package cn.lili.modules.blindBox.tool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.blindBox.enums.SeckillStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;
import java.util.function.Consumer;


/**
 * 秒杀活动通用验证类
 *
 * @author paulG
 * @since 2020/8/18
 **/
public class SeckillTools {

    public static final String START_TIME_COLUMN = "start_time";
    public static final String END_TIME_COLUMN = "end_time";
    public static final String PLATFORM_ID = "platform";
    public static final String PLATFORM_NAME = "platform";

    /**
     * 参数验证
     * 1、活动起始时间必须大于当前时间
     * 2、验证活动开始时间是否大于活动结束时间
     *
     * @param startTime 活动开始时间
     * @param endTime   活动结束时间
     */
    public static void checkPromotionTime(Date startTime, Date endTime) {

        if (startTime == null) {
            throw new ServiceException(ResultCode.PROMOTION_TIME_NOT_EXIST);
        }

        DateTime now = DateUtil.date();

        //如果活动起始时间小于现在时间
        if (now.after(startTime)) {
            throw new ServiceException(ResultCode.PROMOTION_START_TIME_ERROR);
        }
        //如果活动结束时间小于现在时间
        if (endTime != null && now.after(endTime)) {
            throw new ServiceException(ResultCode.PROMOTION_END_TIME_ERROR);
        }

        //开始时间不能大于结束时间
        if (endTime != null && startTime.after(endTime)) {
            throw new ServiceException(ResultCode.PROMOTION_TIME_ERROR);
        }
    }




    public static <T> Consumer<QueryWrapper<T>> queryPromotionStatus(SeckillStatusEnum seckillStatusEnum) {
        switch (seckillStatusEnum) {
            case NEW:
                return (QueryWrapper<T> t) -> t.nested(i -> i.gt(START_TIME_COLUMN, new Date()).gt(END_TIME_COLUMN, new Date()));
            case START:
                return (QueryWrapper<T> t) -> t.nested(i -> i.le(START_TIME_COLUMN, new Date()).ge(END_TIME_COLUMN, new Date()));
            case END:
                return (QueryWrapper<T> t) -> t.nested(i -> i.lt(START_TIME_COLUMN, new Date()).lt(END_TIME_COLUMN, new Date()));
            case CLOSE:
                return (QueryWrapper<T> t) -> t.nested(i -> i.isNull(START_TIME_COLUMN).isNull(END_TIME_COLUMN));
            default:
                return null;
        }
    }



}
