package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dto.GiveGoodsDTO;
import cn.lili.modules.goods.entity.vos.GiveGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 商品赠送业务层
 */
public interface GoodsGiveService extends IService<GiveGoods> {

    /**
     * 赠送商品
     * @param giveGoodsDTO
     */
    String give(GiveGoodsDTO giveGoodsDTO);

    /**
     * 赠送商品查询
     * @param id
     * @return
     */
    GiveGoodsVO giveGoodsQuery(String id);

    /**
     * 赠送商品兑换
     * @param giveCode 赠送码
     */
    void giveGoodsExchange(String giveCode);
}
