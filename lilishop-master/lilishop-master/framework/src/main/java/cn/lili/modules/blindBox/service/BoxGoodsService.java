package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.blindBox.entity.dto.BoxGoodsOperationDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品业务层
 *
 * @author pikachu
 * @since 2020-02-23 16:18:56
 */
public interface BoxGoodsService extends IService<BoxGoods> {




    /**
     * 添加盲盒商品
     *
     * @param boxGoodsOperationDTO 商品查询条件
     */
    void addBoxGoods(BoxGoodsOperationDTO boxGoodsOperationDTO);



}