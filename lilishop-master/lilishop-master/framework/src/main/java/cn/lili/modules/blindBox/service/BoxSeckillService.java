package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.blindBox.entity.dos.BoxSeckillApply;
import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 秒杀业务层
 *
 * @author Chopper
 * @since 2020/11/18 9:45 上午
 */
public interface BoxSeckillService extends IService<BoxSeckill> {

    /**
     * 按条件查询盲盒秒杀
     * @param pageByParams
     * @return
     */
    IPage<BoxSeckill> queryByParams(SeckillBoxSearchParams pageByParams);

    /**
     * 保存盲盒秒杀
     * @param boxSeckill
     * @return
     */
    boolean saveBoxSeckill(BoxSeckill boxSeckill);

    /**
     * 更新盲盒秒杀
     * @param boxSeckill
     * @return
     */
    boolean updateBoxSeckill(BoxSeckill boxSeckill);


    /**
     * 移除促销活动
     *
     * @param ids 促销活动id集合
     * @return 是否移除成功
     */
    boolean deleteBoxSeckill(List<String> ids);

    /**
     * 更新秒杀状态
     * 如果要更新秒杀状态为关闭，startTime和endTime置为空即可
     *
     * @param ids       促销id集合
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否更新成功
     */
    boolean updateStatus(List<String> ids, Long startTime, Long endTime);




}