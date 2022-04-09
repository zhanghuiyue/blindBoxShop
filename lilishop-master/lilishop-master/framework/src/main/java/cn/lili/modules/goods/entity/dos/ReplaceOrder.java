package cn.lili.modules.goods.entity.dos;

import cn.lili.modules.goods.entity.enums.*;
import cn.lili.modules.goods.entity.vos.ReplaceDetailVO;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@TableName("li_replace_order")
@ApiModel(value = "置换记录")
public class ReplaceOrder extends BaseEntity {

    private static final long serialVersionUID = -8236865838438521426L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "置换抵扣价格", required = true)
    private Double deduction;

    @ApiModelProperty(value = "置换差价", required = true)
    private Double disparity ;

    @ApiModelProperty(value = "实际支付金额", required = true)
    private Double actualAmount ;

    @ApiModelProperty(value = "会员ID")
    private String memberId;

    /**
     * 待付款 已完成 已取消
     */
    @ApiModelProperty(value = "置换状态", required = true)
    private String orderStatus;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @ApiModelProperty(value = "sn")
    private String sn;

    @ApiModelProperty(value = "置换订单详情")
    private List<ReplaceDetailVO> replaceDetailList;

    public ReplaceOrder() {
    }

}
