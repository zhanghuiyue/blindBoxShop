package cn.lili.modules.blindBox.entity.vo;

import cn.lili.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExtractParam {

    @ApiModelProperty("订单编号")
    @NotBlank(message = "订单编号为空")
    private String sn;

    @ApiModelProperty(value = "种类id")
    private String blindBoxCategory;


}
