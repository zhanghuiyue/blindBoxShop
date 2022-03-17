package cn.lili.modules.blindBox.mapper;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlindBoxCategoryMapper extends BaseMapper<BlindBoxCategory> {

    @Select("SELECT id， image,name,sort_order,label FROM li_blind_box_category order by sort_order desc")
    List<BlindBoxCategory> queryBlindBoxCategoryList();
}
