package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dos.Prize;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlindBoxPrizeService extends IService<Prize> {

    /**
     * 添加奖品
     * @param prize
     * @return
     */
    boolean batchAddPrize(List<Prize> prize);
}
