package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.entity.dto.TribePageDTO;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TribeService extends IService<Tribe> {

    /**
     * 根据条件分页获盲盒部落列表
     *
     * @param page 条件参数
     * @return IPage<Tribe>列表
     */
    IPage<Tribe> getTribesByPage(TribePageDTO page);

    /**
     * 查询部落列表
     * @return
     */
    List<Tribe> queryTribelist();
}
