package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.entity.dto.TribePageDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TribeService extends IService<Tribe> {

    /**
     * 根据条件分页获盲盒部落列表
     *
     * @param page 条件参数
     * @return banner列表
     */
    IPage<Tribe> getTribesByPage(TribePageDTO page);

}
