package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Trade;
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

    BlindBoxOrder createOrder(OrderParam orderParam);

    BlindBoxGoodsVO blindBoxExtract(ExtractParam extractParam);

    BlindBoxCategoryVO queryBlindBoxList(BlindBoxCategorySearchParam blindBoxCategorySearchPara);
}
