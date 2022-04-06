package cn.lili.modules.blindBox.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒价格业务层
 */
public interface BlindBoxPriceService extends IService<Price> {

    /**
     * 根据盲盒分类查询价格列表
     * @param memberId 会员编号
     * @param categoryId 种类id
     * @return BlindBoxPriceVO
     */
    BlindBoxPriceVO queryPriceByCategory(String memberId, String categoryId);

    /**
     * 分页查询价格
     * @return
     */
    IPage<Price> queryPriceByPage(PageVO pageVO);

    /**
     * 添加价格
     * @param blindBoxPriceDTO
     */
    void batchAdd(List<BlindBoxPriceDTO> blindBoxPriceDTO);

    /**
     * 更新价格
     * @param blindBoxPriceDTO
     */
    void update(BlindBoxPriceDTO blindBoxPriceDTO);

    /**
     * 删除价格
     * @param categoryId
     */
    void deleteByCategoryId(String categoryId );

    /**
     * 删除价格
     * @param id
     */
    void deleteById(String id );

    /**
     * ids
     * @return BlindBoxPriceVO
     */
     List<String> batchQuery(List<String> categoryIds);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<String> ids);
}
