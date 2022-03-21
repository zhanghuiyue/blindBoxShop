package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import com.baomidou.mybatisplus.extension.service.IService;


public interface BlindBoxPriceService extends IService<Price> {

    BlindBoxPriceVO queryPriceByCategory(String memberId, String categoryId);
}
