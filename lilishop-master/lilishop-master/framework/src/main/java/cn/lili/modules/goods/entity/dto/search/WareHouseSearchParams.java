package cn.lili.modules.goods.entity.dto.search;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

/**
 * 仓库查询条件
 *
 * @author pikachu
 * @since 2020-02-24 19:27:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WareHouseSearchParams extends PageVO {

    private static final long serialVersionUID = 2544015852728566887L;

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

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (CharSequenceUtil.isNotEmpty(id)) {
            queryWrapper.in("id", Arrays.asList(id.split(",")));
        }

        if (CharSequenceUtil.isNotEmpty(goodsId)) {
            queryWrapper.eq("goods_id", goodsId);
        }
        if (CharSequenceUtil.isNotEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }

        if (CharSequenceUtil.isNotEmpty(goodsType)) {
            queryWrapper.eq("goods_type", goodsType);
        }
        if (CharSequenceUtil.isNotEmpty(memberId)) {
            queryWrapper.eq("member_id", memberId);
        }
        if (CharSequenceUtil.isNotEmpty(giveStatus)) {
            queryWrapper.eq("give_status", giveStatus);
        }
        if (CharSequenceUtil.isNotEmpty(exchangeStatus)) {
            queryWrapper.eq("exchange_status", exchangeStatus);
        }

        if (CharSequenceUtil.isNotEmpty(pickUpGoodsStatus)) {
            queryWrapper.eq("pick_up_goods_status", pickUpGoodsStatus);
        }
        if (CharSequenceUtil.isNotEmpty(substitutionStatus)) {
            queryWrapper.eq("substitution_status", substitutionStatus);
        }
        if (CharSequenceUtil.isNotEmpty(substitutionFlag)) {
            queryWrapper.eq("substitution_flag", substitutionFlag);
        }
        if (CharSequenceUtil.isNotEmpty(receiveStatus)) {
            queryWrapper.eq("receive_status", receiveStatus);
        }
        queryWrapper.eq("delete_flag", false);
        return queryWrapper;
    }




}
