package cn.lili.modules.blindBox.mapper;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dos.Price;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PriceMapper extends BaseMapper<Price> {

    @Select("SELECT price，original_price, num,name,blind_box_category FROM li_price where blind_box_category=#{blindBoxCategoryId} ")
    List<Price> queryPriceList(@Param("blindBoxCategoryId") String blindBoxCategoryId);
}