package cn.lili.modules.blindBox.entity.dos;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品品牌
 *
 * @author pikachu
 * @since 2020-02-18 15:18:56
 */
@Data
@TableName("li_blind_box_banner")
@ApiModel(value = "盲盒推荐banner")
public class Banner extends BaseEntity {


    private static final long serialVersionUID =  -4878132663540847325L;

    @NotEmpty(message = "banner名称不能为空")
    @Length(max = 20, message = "banner名称应该小于20长度字符")
    @ApiModelProperty(value = "banner名称", required = true)
    private String name;


    @NotEmpty(message = "banner图片不能为空")
    @Length(max = 255, message = "banner图片地址长度超过255字符")
    @ApiModelProperty(value = "banner图片", required = true)
    private String image;

    @NotNull(message = "排序值不能为空")
    @Max(value = 999,message = "排序值最大999")
    @ApiModelProperty(value = "排序值")
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "跳转地址")
    private String urlInfo;

}