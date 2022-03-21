package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dto.BannerPageDTO;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import cn.lili.modules.blindBox.mapper.BannerMapper;
import cn.lili.modules.blindBox.service.BannerService;
import cn.lili.modules.goods.entity.dos.Brand;
import cn.lili.modules.goods.entity.dos.CategoryBrand;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.vos.BrandVO;
import cn.lili.modules.goods.service.BrandService;

import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 盲盒推荐 banner图业务层实现
 *
 * @author lilei
 * @since 2022-03-20 16:18:56
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {


    @Override
    public IPage<Banner> getBannersByPage(BannerPageDTO page) {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        if (page.getName() != null) {
            queryWrapper.like(Banner::getName, page.getName());
        }
        return this.page(PageUtil.initPage(page), queryWrapper);
    }


    @Override
    public boolean addBanner(BannerVO bannerVO) {

        if (getOne(new LambdaQueryWrapper<Banner>().eq(Banner::getName, bannerVO.getName())) != null) {
            throw new ServiceException(ResultCode.BRAND_NAME_EXIST_ERROR);
        }
        return this.save(bannerVO);
    }

    @Override
    public boolean updateBanner(BannerVO bannerVO) {
        this.checkExist(bannerVO.getId());
        if (getOne(new LambdaQueryWrapper<Banner>().eq(Banner::getName, bannerVO.getName()).ne(Banner::getId, bannerVO.getId())) != null) {
            throw new ServiceException(ResultCode.BRAND_NAME_EXIST_ERROR);
        }
        return this.updateById(bannerVO);
    }

    @Override
    public boolean brandDisable(String bannerId, boolean disable) {
        Banner banner = this.checkExist(bannerId);
        //如果是要禁用，则需要先判定绑定关系
        if (Boolean.TRUE.equals(disable)) {
            List<String> ids = new ArrayList<>();
            ids.add(bannerId);

        }
        banner.setDeleteFlag(disable);
        return updateById(banner);
    }

    @Override
    public void deleteBanners(List<String> ids) {
        this.removeByIds(ids);
    }


    /**
     * 校验是否存在
     *
     * @param bannerId 品牌ID
     * @return 品牌
     */
    private Banner checkExist(String bannerId) {
        Banner banner = getById(bannerId);
        if (banner == null) {
            log.error("品牌ID为" + bannerId + "的BANNER不存在");
            throw new ServiceException(ResultCode.BANNER_NOT_EXIST);
        }
        return banner;
    }

}