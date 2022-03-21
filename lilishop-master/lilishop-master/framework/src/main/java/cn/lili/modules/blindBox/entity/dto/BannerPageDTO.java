package cn.lili.modules.blindBox.entity.dto;

import cn.lili.common.vo.PageVO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * banner图片dto
 *
 * @author lilei
 * @since 2022-03-21 15:18:56
 */
@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel(description = "Banner图片dto")
public class BannerPageDTO extends PageVO {

    private static final long serialVersionUID = 8906820486037326039L;

    @ApiModelProperty(value = "banner名称")
    private String name;
}
