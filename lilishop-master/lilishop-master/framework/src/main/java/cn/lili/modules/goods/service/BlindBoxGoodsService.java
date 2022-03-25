package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒商品业务层
 */
public interface BlindBoxGoodsService extends IService<BlindBoxGoods> {

    List<BlindBoxGoods> queryList(String categoryId);

    List<BlindBoxGoods> batchQueryById(List<String> goodsId);

    BlindBoxGoods queryProductDetails(String goodsId);
}
