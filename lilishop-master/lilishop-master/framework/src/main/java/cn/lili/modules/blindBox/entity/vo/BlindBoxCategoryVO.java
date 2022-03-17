package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BlindBoxCategoryVO {
    @ApiModelProperty(value = "盲盒类型列表")
    public List<BlindBoxCategoryDTO> blindBoxCategoryDTOList;
}
