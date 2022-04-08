package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒相关业务层
 */
public interface BlindBoxService extends IService<BlindBox> {

    /**
     * 盲盒列表查询
     * @return List<BlindBox>
     */
    List<BlindBox> queryBlindBoxCategoryList();

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
     * @return BlindBoxVO
     */
    BlindBoxVO queryBlindBoxList(BlindBoxCategorySearchParam blindBoxCategorySearchPara);

    /**
     * 分页查询盲盒列表
     * @param boxSearchParams
     * @return IPage<BlindBox>
     */
    IPage<BlindBox> getBlindBoxCategoryByPage(BoxSearchParams boxSearchParams);

    /**
     * 添加盲盒
     * @param blindBoxDTO
     */
    boolean addBlindBox(BlindBoxDTO blindBoxDTO);

    /**
     * 更新盲盒
     * @param blindBoxDTO
     */
    boolean updateBlindBox(BlindBoxDTO blindBoxDTO);

    /**
     * 删除盲盒
     * @param id
     */
    boolean deleteBlindBox(String id);

    /**
     * 批量删除盲盒
     * @param ids
     */
    void batchDeleteBlindBox(List<String> ids);

    /**
     * 禁用盲盒
     * @param id
     * @param disable
     * @return
     */
    boolean blindBoxDisable(String id, boolean disable);
}
