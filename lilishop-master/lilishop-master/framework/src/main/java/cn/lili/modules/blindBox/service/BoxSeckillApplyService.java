package cn.lili.modules.blindBox.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.BoxSeckillApply;

import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillApplyVO;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillBoxVO;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillTimelineVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 秒杀申请业务层
 *
 * @author lilei
 * @since 2022/03/22 9:45 上午
 */
public interface BoxSeckillApplyService extends IService<BoxSeckillApply> {


    /**
     * 添加盲盒秒杀活动申请
     * 检测是否商品是否同时参加多个活动
     * 将秒杀商品信息存入秒杀活动中
     * 保存秒杀活动商品，促销商品信息
     *
     * @param boxSeckillId       秒杀活动编号
     * @param seckillApplyList 秒杀活动申请列表
     */
    void addBoxSeckillApply(String boxSeckillId, List<BoxSeckillApplyVO> seckillApplyList);

    /**
     * 分页查询限时请购申请列表
     *
     * @param queryParam 秒杀活动申请查询参数
     * @param pageVo     分页参数
     * @return 限时请购申请列表
     */
    IPage<BoxSeckillApply> getBoxSeckillApplyPage(SeckillBoxSearchParams queryParam, PageVO pageVo);



    /**
     * 获取当天秒杀活动信息列表（时刻及对应时刻下的盲盒）
     *
     * @return 秒杀活动信息列表
     */
    List<BoxSeckillTimelineVO> getBoxSeckillTimeline();


    /**
     * 获取当天某个时刻的秒杀活动商品列表
     *
     * @param timeline 指定时刻
     * @return 秒杀活动商品列表
     */
    List<BoxSeckillBoxVO> getSeckillBox(Integer timeline);

}