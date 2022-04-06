package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class BlindBoxVO {
    @ApiModelProperty(value = "盲盒类型列表")
    public List<BlindBoxDTO> blindBoxDTOList;
}
