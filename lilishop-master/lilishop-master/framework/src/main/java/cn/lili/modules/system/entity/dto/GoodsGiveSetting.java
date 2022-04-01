package cn.lili.modules.system.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class GoodsGiveSetting implements Serializable {

    private static final long serialVersionUID = -2628613596000114786L;
    @ApiModelProperty(value = "自动取消 小时")
    private Integer autoCancel;
}
