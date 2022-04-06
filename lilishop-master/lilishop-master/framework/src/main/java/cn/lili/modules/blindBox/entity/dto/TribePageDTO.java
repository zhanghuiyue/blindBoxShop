package cn.lili.modules.blindBox.entity.dto;

import cn.lili.common.vo.PageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TribePageDTO extends PageVO {

    private static final long serialVersionUID = 8906820486037326039L;

    @ApiModelProperty(value = "类型，ALL查询全部，0已赠送，1表示已领取")
    private String type;

}
