package cn.lili.modules.blindBox.service;


import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dto.BannerPageDTO;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 盲盒推荐banner业务层
 *
 * @author lilei
 * @since 2022-03-21 16:18:56
 */
public interface BannerService extends IService<Banner> {

    /**
     * 根据条件分页获盲盒推荐banner列表
     *
     * @param page 条件参数
     * @return banner列表
     */
    IPage<Banner> getBannersByPage(BannerPageDTO page);

    /**
     * 删除banner图片
     *
     * @param ids bannerId
     */
    void deleteBanners(List<String> ids);


    /**
     * 添加banner图片
     *
     * @param bannerVO banner图片信息
     * @return 添加结果
     */
    boolean addBanner(BannerVO bannerVO);

    /**
     * 更新banner
     *
     * @param bannerVO banner信息
     * @return 更新结果
     */
    boolean updateBanner(BannerVO bannerVO);

    /**
     * 更新banner是否可用
     *
     * @param bannerId bannerID
     * @param disable 是否不可用
     * @return 更新结果
     */
    boolean brandDisable(String bannerId, boolean disable);

    /**
     * 得到可用推荐banner合集
     *
     */

    List<Banner> getBannersList();


}