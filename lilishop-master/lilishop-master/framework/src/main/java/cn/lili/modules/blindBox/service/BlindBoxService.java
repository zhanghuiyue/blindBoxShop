package cn.lili.modules.blindBox.service;

import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.distribution.entity.dos.Distribution;
import cn.lili.modules.distribution.entity.dto.DistributionSearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BlindBoxService extends IService<BlindBoxCategory> {

    List<BlindBoxCategory> queryBlindBoxCategoryList();
}
