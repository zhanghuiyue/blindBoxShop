package cn.lili.modules.blindBox.service;

import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxCategorySearchParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
public interface BlindBoxCategoryService extends IService<BlindBoxCategory> {

    /**
     * 得到BlindBoxCategory合集
     *
     */

    List<BlindBoxCategory> getBlindBoxCategoryList();


    /**
     * BlindBoxCategory
     * @return
     */
    IPage<BlindBoxCategory> getBlindBoxCategoryByPage(BoxCategorySearchParams boxCategorySearchParams);

    /**
     * 添加盲盒分类
     * @param blindBoxCategoryDTO
     * @return
     */
    boolean addBlindBoxCategory(BlindBoxCategoryDTO blindBoxCategoryDTO);

    /**
     * 更新盲盒分类
     * @param blindBoxCategoryDTO
     * @return
     */
    boolean updateBlindBoxCategory(BlindBoxCategoryDTO blindBoxCategoryDTO);

    /**
     * 删除盲盒分类
     * @param id 盲盒类型编号
     * @return
     */
    boolean deleteBlindBoxCategory(String id);

    /**
     * 禁用盲盒分类
     * @param id
     * @param disable
     * @return
     */
    boolean blindBoxCategoryDisable(String id, boolean disable);
}
