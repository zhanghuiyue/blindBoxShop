package cn.lili.modules.goods.entity.dos;

import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("li_replace_detail")
@ApiModel(value = "置换详细")
public class ReplaceDetail extends BaseEntity {

    private static final long serialVersionUID = -8236865838438521426L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "订单编号")
    private String orderId;

    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    @ApiModelProperty(value = "商品sku编号")
    private String skuId;

    @ApiModelProperty(value = "抵扣价格", required = true)
    private Double price;

    @ApiModelProperty(value = "置换时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replaceTime;

    @ApiModelProperty(value = "sn")
    private String sn;

    /**
     * 待付款 已完成 已取消
     */
    @ApiModelProperty(value = "置换状态", required = true)
    private String orderStatus;

    public ReplaceDetail() {
    }


}
