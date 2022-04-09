package cn.lili.modules.goods.entity.dto;

import cn.lili.modules.goods.entity.dos.Warehouse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarehouseDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    @ApiModelProperty(value = "商品sku编号")
    private String skuId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "小图路径")
    private String small;


    @ApiModelProperty(value = "元气豆")
    private Integer sinewyBean;

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
    /**
     * 见PickUpGoodsStatusEnum
     */
    @ApiModelProperty(value = "提货状态，PICKUPGOODS：提货，UNPICKUPGOODS:未提货")
    private String pickUpGoodsStatus;
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


    public WarehouseDTO(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.goodsId = warehouse.getGoodsId();
        this.skuId = warehouse.getSkuId();
        this.goodsType = warehouse.getGoodsType();
        this.memberId = warehouse.getMemberId();
        this.giveStatus = warehouse.getGiveStatus();
        this.exchangeStatus = warehouse.getExchangeStatus();
        this.pickUpGoodsStatus = warehouse.getPickUpGoodsStatus();
        this.substitutionStatus = warehouse.getSubstitutionStatus();
        this.receiveStatus = warehouse.getReceiveStatus();

    }



}
