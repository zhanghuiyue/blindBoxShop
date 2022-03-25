package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
