package cn.lili.modules.blindBox.serviceimpl;


import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.mapper.BoxSeckillMapper;
import cn.lili.modules.blindBox.service.BoxSeckillService;
import cn.lili.modules.blindBox.tool.SeckillTools;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 盲盒秒杀活动业务层实现
 *
 * @author lilei
 * @since 2022/03/22
 */
@Service
@Slf4j
public class BoxSeckillServiceImpl extends ServiceImpl<BoxSeckillMapper, BoxSeckill> implements BoxSeckillService {


    @Override
    public IPage<BoxSeckill> queryByParams(SeckillBoxSearchParams seckillBoxSearchParams) {
        return this.page(PageUtil.initPage(seckillBoxSearchParams), seckillBoxSearchParams.queryWrapper());
    }


    public boolean saveBoxSeckill(BoxSeckill boxSeckill) {

        this.checkPromotions(boxSeckill) ;
        boolean result = this.save(boxSeckill);
        return  result ;
    }

    public void checkPromotions(BoxSeckill boxSeckill) {
        SeckillTools.checkPromotionTime(boxSeckill.getStartTime(), boxSeckill.getEndTime());
    }

    public boolean updateBoxSeckill(BoxSeckill boxSeckill) {

        this.checkPromotions(boxSeckill);
        this.checkStatus(boxSeckill);
        boolean result = this.saveOrUpdate(boxSeckill);


        return result;
    }

    /**
     * 检查秒杀状态
     *
     * @param boxSeckill 秒杀盲盒实体
     */
    public void checkStatus(BoxSeckill boxSeckill) {
        BoxSeckill byId = this.getById(boxSeckill.getId());
        if (byId == null) {
            throw new ServiceException(ResultCode.BOX_ACTIVITY_ERROR);
        }
    }

    /**
     * 移除秒杀活动
     *
     * @param ids 促销活动id集合
     * @return 是否移除成功
     */
    @Override

    public boolean deleteBoxSeckill(List<String> ids) {

        return this.removeByIds(ids);
    }

    /**
     * 更新秒杀状态
     * 如果要更新促销状态为关闭，startTime和endTime置为空即可
     *
     * @param ids       促销id集合
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否更新成功
     */
    @Override

    public boolean updateStatus(List<String> ids, Long startTime, Long endTime) {
        List<BoxSeckill> boxSeckillList = this.list(new QueryWrapper<BoxSeckill>().in("id", ids));
        for (BoxSeckill t : boxSeckillList) {
            if (startTime != null && endTime != null) {
                t.setStartTime(new Date(startTime));
                t.setEndTime(new Date(endTime));
            } else {
                t.setStartTime(null);
                t.setEndTime(null);
            }
            this.checkStatus(t);
          /*  this.updateSeckillBox(t);*/

        }
        if (startTime != null && endTime != null) {
            return this.update(new UpdateWrapper<BoxSeckill>().in("id", ids).set("start_time", new Date(startTime)).set("end_time", new Date(endTime)));
        } else {
            return this.update(new UpdateWrapper<BoxSeckill>().in("id", ids).set("start_time", null).set("end_time", null));
        }
    }


}