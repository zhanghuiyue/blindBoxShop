package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒仓库业务层
 */
public interface WarehouseService extends IService<Warehouse> {

    /**
     * 查询仓库信息
     * @param goods 赠品
     * @return
     */
    Warehouse queryWarehouse(GiveGoods goods);
}
