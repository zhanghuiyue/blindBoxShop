package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Trade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒相关业务层
 */
public interface BlindBoxService extends IService<BlindBoxCategory> {

    /**
     * 盲盒列表查询
     * @return List<BlindBoxCategory>
     */
    List<BlindBoxCategory> queryBlindBoxCategoryList();

    /**
     * 创建盲盒订单
     * @param orderParam
     * @return BlindBoxOrder
     */
    BlindBoxOrder createOrder(OrderParam orderParam);

    /**
     * 盲盒的抽取
     * @param extractParam
     * @return BlindBoxGoodsVO
     */
    BlindBoxGoodsVO blindBoxExtract(ExtractParam extractParam);

    /**
     * 搜索页查询盲盒列表
     * @param blindBoxCategorySearchPara
     * @return BlindBoxCategoryVO
     */
    BlindBoxCategoryVO queryBlindBoxList(BlindBoxCategorySearchParam blindBoxCategorySearchPara);

    /**
     * 分页查询盲盒列表
     * @param boxSearchParams
     * @return IPage<BlindBoxCategory>
     */
    IPage<BlindBoxCategory> getBlindBoxCategoryByPage(BoxSearchParams boxSearchParams);

    /**
     * 添加盲盒
     * @param blindBoxCategoryDTO
     */
    void addBlindBox(BlindBoxCategoryDTO blindBoxCategoryDTO);

    /**
     * 更新盲盒
     * @param blindBoxCategoryDTO
     */
    void updateBlindBox(BlindBoxCategoryDTO blindBoxCategoryDTO);

    /**
     * 删除盲盒
     * @param id
     */
    void deleteBlindBox(String id);

    /**
     * 批量删除盲盒
     * @param ids
     */
    void batchDeleteBlindBox(List<String> ids);
}
