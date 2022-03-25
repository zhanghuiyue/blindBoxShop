package cn.lili.modules.blindBox.entity.dto.search;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

/**
 * 盲盒查询条件
 *
 * @author pikachu
 * @since 2020-02-24 19:27:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BoxSearchParams extends PageVO {

    private static final long serialVersionUID = 2544015852728566887L;



    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "盲盒编号")
    private String id;

    @ApiModelProperty(value = "价格,可以为范围，如10_1000")
    private String price;



    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (CharSequenceUtil.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (CharSequenceUtil.isNotEmpty(id)) {
            queryWrapper.in("id", Arrays.asList(id.split(",")));
        }
        queryWrapper.eq("delete_flag", false);
        this.betweenWrapper(queryWrapper);
        return queryWrapper;
    }

    private <T> void betweenWrapper(QueryWrapper<T> queryWrapper) {
        if (CharSequenceUtil.isNotEmpty(price)) {
            String[] s = price.split("_");
            if (s.length > 1) {
                queryWrapper.between("price", s[0], s[1]);
            } else {
                queryWrapper.ge("price", s[0]);
            }
        }
    }


}
