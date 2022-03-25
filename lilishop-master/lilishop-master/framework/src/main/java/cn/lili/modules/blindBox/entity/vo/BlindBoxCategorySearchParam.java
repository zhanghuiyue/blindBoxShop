package cn.lili.modules.blindBox.entity.vo;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BlindBoxCategorySearchParam {

    @ApiModelProperty(value = "盲盒类型")
    public String type;

    @ApiModelProperty(value = "查询最低价")
    public Double minPrice;

    @ApiModelProperty(value = "查询最高价")
    public Double maxPrice;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        //按标签查询
        if (CharSequenceUtil.isNotEmpty(type)) {
            if(!"ALL".equals(type)){
                wrapper.eq( "label",type );
            }
        }
        if(minPrice!=null && minPrice>0){
            wrapper.ge("price",minPrice);
        }
        if(maxPrice!=null && maxPrice>0){
            wrapper.le("price",maxPrice);
        }
        return wrapper;
    }
}
