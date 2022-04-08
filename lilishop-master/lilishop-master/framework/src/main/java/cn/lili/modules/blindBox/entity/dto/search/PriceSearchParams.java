package cn.lili.modules.blindBox.entity.dto.search;

import cn.lili.common.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 价格查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PriceSearchParams extends PageVO implements Serializable {

    private static final long serialVersionUID = 2544015852728566887L;

    @ApiModelProperty(value = "盲盒名称")
    private String name;

    @ApiModelProperty(value = "盲盒id")
    private String blindBoxId;

}
