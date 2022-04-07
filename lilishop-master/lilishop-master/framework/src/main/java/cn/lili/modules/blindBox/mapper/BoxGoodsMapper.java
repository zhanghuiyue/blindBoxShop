package cn.lili.modules.blindBox.mapper;

import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.vos.GoodsVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 规格项数据处理层
 *
 * @author pikachu
 * @since 2020-02-18 15:18:56
 */
public interface BoxGoodsMapper extends BaseMapper<BoxGoods> {


}