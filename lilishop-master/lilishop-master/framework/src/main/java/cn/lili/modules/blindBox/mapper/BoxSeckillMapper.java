package cn.lili.modules.blindBox.mapper;

import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.promotion.entity.dos.Seckill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * 秒杀活动数据处理层
 *
 * @author lilei
 * @since 2022/03/22
 */
public interface BoxSeckillMapper extends BaseMapper<BoxSeckill> {

    /**
     * 修改秒杀活动数量
     *
     * @param seckillId 秒杀活动ID
     */
    @Update("UPDATE li_box_seckill SET goods_num =( SELECT count( id ) FROM li_seckill_apply WHERE seckill_id = #{seckillId} ) WHERE id = #{seckillId}")
    void updateSeckillGoodsNum(String seckillId);
}