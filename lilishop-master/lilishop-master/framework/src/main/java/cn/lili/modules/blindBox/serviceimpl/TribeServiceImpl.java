package cn.lili.modules.blindBox.serviceimpl;


import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.entity.dto.BannerPageDTO;
import cn.lili.modules.blindBox.entity.dto.TribePageDTO;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import cn.lili.modules.blindBox.enums.StatusEnum;
import cn.lili.modules.blindBox.mapper.BannerMapper;
import cn.lili.modules.blindBox.mapper.TribeMapper;
import cn.lili.modules.blindBox.service.BannerService;
import cn.lili.modules.blindBox.service.TribeService;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.order.order.entity.enums.ExtractStatusEnum;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * 盲盒部落图业务层实现
 *
 */
@Service
public class TribeServiceImpl extends ServiceImpl<TribeMapper, Tribe> implements TribeService {


    @Override
    public IPage<Tribe> getTribesByPage(TribePageDTO page) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        LambdaQueryWrapper<Tribe> queryWrapper = new LambdaQueryWrapper<>();
        //类型，ALL查询全部，0已赠送，1表示已领取
        if ("0".equals(page.getType())) {
            queryWrapper.eq(Tribe::getGiveStatus, GiveStatusEnum.GIVE.name());
        }else if("1".equals(page.getType())){
            queryWrapper.eq(Tribe::getGiveStatus, GiveStatusEnum.GIVEED.name());
        }
        queryWrapper.eq(Tribe::getMemberId,currentUser.getId());
        queryWrapper.eq(Tribe::getExtractStatus, ExtractStatusEnum.UNEXTRACT.getState());
        queryWrapper.eq(Tribe::getStatus, StatusEnum.VALID.name());
        return this.page(PageUtil.initPage(page), queryWrapper);
    }


}