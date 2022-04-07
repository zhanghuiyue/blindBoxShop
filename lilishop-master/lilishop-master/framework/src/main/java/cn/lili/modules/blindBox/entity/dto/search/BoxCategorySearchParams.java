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
public class BoxCategorySearchParams extends PageVO {

    private static final long serialVersionUID = 2544015852728566887L;



    @ApiModelProperty(value = "盲盒分类名称")
    private String name;



}
