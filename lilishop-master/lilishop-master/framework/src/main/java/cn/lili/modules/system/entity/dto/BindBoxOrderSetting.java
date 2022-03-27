package cn.lili.modules.system.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单设置
 *
 * @author Chopper
 * @since 2020/11/17 7:59 下午
 */
@Data
public class BindBoxOrderSetting implements Serializable {

    private static final long serialVersionUID = -2628613596000114786L;
    @ApiModelProperty(value = "自动取消 分钟")
    private Integer autoCancel;
}
