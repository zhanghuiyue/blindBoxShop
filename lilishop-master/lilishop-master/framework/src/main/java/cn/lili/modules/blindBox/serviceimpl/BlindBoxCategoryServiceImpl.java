package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxCategorySearchParams;
import cn.lili.modules.blindBox.mapper.BlindBoxCategoryMapper;
import cn.lili.modules.blindBox.mapper.BlindBoxMapper;
import cn.lili.modules.blindBox.service.BlindBoxCategoryService;
import cn.lili.modules.blindBox.service.BlindBoxService;
import cn.lili.modules.goods.entity.dos.Brand;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlindBoxCategoryServiceImpl extends ServiceImpl<BlindBoxCategoryMapper, BlindBoxCategory> implements BlindBoxCategoryService {
    /**
     * 缓存
     */
    @Autowired
    private Cache cache;

    @Autowired
    private BlindBoxService blindBoxService;

    @Override
    public List<BlindBoxCategory> getBlindBoxCategoryList() {
        List<BlindBoxCategory> categoryList = (List<BlindBoxCategory>) cache.get(CachePrefix.BOXCATEGORY.getPrefix());
        if(categoryList!= null){
            return categoryList ;
        }
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlindBoxCategory::getDeleteFlag,"0");
        queryWrapper.orderByAsc(BlindBoxCategory::getSortOrder);
        List<BlindBoxCategory> blindBoxCategoryList = this.baseMapper.selectList(queryWrapper);
        if(!blindBoxCategoryList.isEmpty()){
            cache.put(CachePrefix.BOXCATEGORY.getPrefix(), blindBoxCategoryList);
        }
        return blindBoxCategoryList;
    }

    @Override
    public IPage<BlindBoxCategory> getBlindBoxCategoryByPage(BoxCategorySearchParams boxCategorySearchParams) {
        LambdaQueryWrapper<BlindBoxCategory> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(boxCategorySearchParams.getName())) {
            queryWrapper.like(BlindBoxCategory::getCategoryName, boxCategorySearchParams.getName());
        }
        return this.page(PageUtil.initPage(boxCategorySearchParams), queryWrapper);
    }

    @Override
    public boolean addBlindBoxCategory(BlindBoxCategoryDTO blindBoxCategoryDTO) {
        if (getOne(new LambdaQueryWrapper<BlindBoxCategory>().eq(BlindBoxCategory::getCategoryName, blindBoxCategoryDTO.getCategoryName())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_NAME_EXIST_ERROR);
        }
        return this.save(blindBoxCategoryDTO);
    }

    @Override
    public boolean updateBlindBoxCategory(BlindBoxCategoryDTO blindBoxCategoryDTO) {
        this.checkExist(blindBoxCategoryDTO.getId());
        if (getOne(new LambdaQueryWrapper<BlindBoxCategory>().eq(BlindBoxCategory::getCategoryName, blindBoxCategoryDTO.getCategoryName()).ne(BlindBoxCategory::getId, blindBoxCategoryDTO.getId())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_NAME_EXIST_ERROR);
        }
        return this.updateById(blindBoxCategoryDTO);
    }

    @Override
    public boolean deleteBlindBoxCategory(String id) {
        checkBind(id);
        return this.removeById(id);
    }

    /**
     * 验证绑定关系
     * @param id
     */
    private void checkBind(String id){
        LambdaQueryWrapper<BlindBox> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlindBox::getCategoryId,id);
        List<BlindBox> blindBoxList = blindBoxService.getBaseMapper().selectList(queryWrapper);
        if(!blindBoxList.isEmpty()) {
            List<String> names = blindBoxList.stream().map(BlindBox::getName).collect(Collectors.toList());
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_USE_DISABLE_ERROR,
                    JSONUtil.toJsonStr(names));
        }
    }


    @Override
    public boolean blindBoxCategoryDisable(String id, boolean disable) {
        BlindBoxCategory blindBoxCategory = this.checkExist(id);
        //如果是要禁用，则需要先判定绑定关系
        if (Boolean.TRUE.equals(disable)) {
            checkBind(id);
        }
        blindBoxCategory.setDeleteFlag(disable);
        return updateById(blindBoxCategory);
    }


    /**
     * 校验是否存在
     *
     * @param id 分类ID
     * @return
     */
    private BlindBoxCategory checkExist(String id) {
        BlindBoxCategory blindBoxCategory = getById(id);
        if (blindBoxCategory == null) {
            log.error("分类ID为" + id + "的分类不存在");
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_NOT_EXIST);
        }
        return blindBoxCategory;
    }
}
