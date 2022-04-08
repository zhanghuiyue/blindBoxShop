package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BlindBoxCategoryVo {

    @ApiModelProperty(value = "盲盒分类列表")
    public List<BlindBoxCategoryDTO> blindBoxCategoryDTOS;
}
