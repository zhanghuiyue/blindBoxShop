package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.vo.BlindBoxGoodsVO;
import cn.lili.modules.blindBox.entity.vo.ExtractParam;
import cn.lili.modules.blindBox.entity.vo.OrderParam;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.Trade;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlindBoxService extends IService<BlindBoxCategory> {

    List<BlindBoxCategory> queryBlindBoxCategoryList();

    BlindBoxOrder createOrder(OrderParam orderParam);

    BlindBoxGoodsVO blindBoxExtract(ExtractParam extractParam);
}
