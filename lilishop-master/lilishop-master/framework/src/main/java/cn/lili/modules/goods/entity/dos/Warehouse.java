package cn.lili.modules.goods.entity.dos;

import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.modules.goods.entity.enums.*;
import cn.lili.mybatis.BaseEntity;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("li_warehouse")
@ApiModel(value = "盲盒仓库")
public class Warehouse extends BaseEntity {

    private static final long serialVersionUID = -8236865838438521426L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    @ApiModelProperty(value = "商品sku编号")
    private String skuId;

    @ApiModelProperty(value = "商品类型，0表示奖品，1表示置换商品，2表示购买商品")
    private String goodsType;

    @ApiModelProperty(value = "会员编号")
    private String memberId;

    /**
     * 见GiveStatusEnum
     */
    @ApiModelProperty(value = "赠送状态，GIVE：赠送，UNGIVE:未赠送")
    private String giveStatus;
    /**
     * 见ExchangeStatusEnum
     */
    @ApiModelProperty(value = "商品的兑换状态，UNEXCHANGE：未兑换，EXCHANGE:兑换")
    private String exchangeStatus;

    @ApiModelProperty(value = "兑换标识，0：可以兑换，1:不可以兑换")
    private String exchangeFlag;
    /**
     * 见PickUpGoodsStatusEnum
     */
    @ApiModelProperty(value = "提货状态，PICKUPGOODS：提货，UNPICKUPGOODS:未提货")
    private String pickUpGoodsStatus;

    @ApiModelProperty(value = "提货标识，0：可以提货，1:不可以提货")
    private String pickUpGoodsFlag;

    /**
     * 见SubstitutionStatusEnum
     */
    @ApiModelProperty(value = "置换状态，SUBSTITUTION：置换，UNSUBSTITUTION:未置换")
    private String substitutionStatus;

    @ApiModelProperty(value = "置换标识，0：可以置换，1:不可以置换")
    private String substitutionFlag;

    /**
     * 见ReceiveStatusEnum
     */
    @ApiModelProperty(value = "领取状态，RECEIVE：领取，UNRECEIVE:未领取")
    private String receiveStatus;

    public Warehouse() {
    }

    public Warehouse(String memberId) {
        this.memberId = memberId;
        this.giveStatus = GiveStatusEnum.UNGIVE.name();
        this.exchangeStatus =ExchangeStatusEnum.UNEXCHANGE.name();
        this.pickUpGoodsStatus = PickUpGoodsStatusEnum.UNPICKUPGOODS.name();
        this.substitutionStatus = SubstitutionStatusEnum.UNSUBSTITUTION.name();
        this.receiveStatus = ReceiveStatusEnum.RECEIVE.name();
    }


    public Warehouse(WarehouseDTO warehouseDTO) {
        this.id = warehouseDTO.getId();
        this.goodsId = warehouseDTO.getGoodsId();
        this.skuId = warehouseDTO.getSkuId();
        this.goodsType = warehouseDTO.getGoodsType();
        this.memberId = warehouseDTO.getMemberId();
        this.giveStatus = warehouseDTO.getGiveStatus();
        this.exchangeStatus = warehouseDTO.getExchangeStatus();
        this.pickUpGoodsStatus = warehouseDTO.getPickUpGoodsStatus();
        this.substitutionStatus = warehouseDTO.getSubstitutionStatus();
        this.receiveStatus =warehouseDTO.getReceiveStatus();

    }
}
