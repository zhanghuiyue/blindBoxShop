package cn.lili.modules.goods.service;

import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.ReplaceOrder;
import cn.lili.modules.goods.entity.dos.Warehouse;
import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.modules.goods.entity.dto.search.WareHouseSearchParams;
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



    /**
     * 仓库查询
     *
     * @param wareHouseSearchParams 查询参数
     * @return 仓库列表
     */
    List<WarehouseDTO> queryByParams(WareHouseSearchParams wareHouseSearchParams);

    /**
     * 添加待置换商品订单
     * @param id
     * @param warehouseDTOs
     */
    void addReplaceOrder(String id ,List<WarehouseDTO> warehouseDTOs);


    /**
     * 置换记录查询
     *
     * @return 置换列表
     */
    List<ReplaceOrder> getReplaceOrderList();


}
