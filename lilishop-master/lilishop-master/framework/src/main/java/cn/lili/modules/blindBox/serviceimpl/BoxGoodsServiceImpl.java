package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.cache.Cache;
import cn.lili.cache.CachePrefix;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.properties.RocketmqCustomProperties;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.blindBox.entity.dto.BoxGoodsOperationDTO;
import cn.lili.modules.blindBox.mapper.BoxGoodsMapper;
import cn.lili.modules.blindBox.service.BoxGoodsService;
import cn.lili.modules.goods.entity.dos.Category;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dos.GoodsGallery;
import cn.lili.modules.goods.entity.dto.GoodsOperationDTO;
import cn.lili.modules.goods.entity.dto.GoodsParamsDTO;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.modules.goods.entity.vos.GoodsSkuVO;
import cn.lili.modules.goods.entity.vos.GoodsVO;
import cn.lili.modules.goods.mapper.GoodsMapper;
import cn.lili.modules.goods.service.CategoryService;
import cn.lili.modules.goods.service.GoodsGalleryService;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.member.entity.dos.MemberEvaluation;
import cn.lili.modules.member.entity.enums.EvaluationGradeEnum;
import cn.lili.modules.member.service.MemberEvaluationService;
import cn.lili.modules.store.entity.dos.FreightTemplate;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.FreightTemplateService;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.GoodsSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.mybatis.util.PageUtil;
import cn.lili.rocketmq.RocketmqSendCallbackBuilder;
import cn.lili.rocketmq.tags.GoodsTagsEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ?????????????????????
 *
 * @author pikachu
 * @since 2020-02-23 15:18:56
 */
@Service
public class BoxGoodsServiceImpl extends ServiceImpl<BoxGoodsMapper, BoxGoods> implements BoxGoodsService {

    /**
     * ??????
     */
    @Autowired
    private SettingService settingService;

    /**
     * ????????????
     */
    @Autowired
    private GoodsGalleryService goodsGalleryService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBoxGoods(BoxGoodsOperationDTO boxGoodsOperationDTO) {
        BoxGoods boxGoods = new BoxGoods(boxGoodsOperationDTO);
        //????????????
        this.checkGoods(boxGoods);
        //???goods????????????
        this.setGoodsGalleryParam(boxGoodsOperationDTO.getGoodsGalleryList().get(0), boxGoods);
        //????????????
        this.save(boxGoods);

        //????????????
        if (boxGoodsOperationDTO.getGoodsGalleryList() != null && !boxGoodsOperationDTO.getGoodsGalleryList().isEmpty()) {
            this.goodsGalleryService.add(boxGoodsOperationDTO.getGoodsGalleryList(), boxGoods.getId());
        }
    }


    /**
     * ??????????????????
     * ??????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????
     * ????????????????????????
     * ??????????????????????????????
     * ?????????????????????????????????
     *
     * @param boxGoods ??????
     */
    private void checkGoods(BoxGoods boxGoods) {

        //????????????????????????--?????????????????????
        if (boxGoods.getId() != null) {
            this.checkExist(boxGoods.getId());
        } else {
            boxGoods.setQuantity(0);
        }
        //??????????????????????????????????????????
        Setting setting = settingService.get(SettingEnum.GOODS_SETTING.name());
        GoodsSetting goodsSetting = JSONUtil.toBean(setting.getSettingValue(), GoodsSetting.class);
        //??????????????????
        boxGoods.setAuthFlag(Boolean.TRUE.equals(goodsSetting.getGoodsCheck()) ? GoodsAuthEnum.TOBEAUDITED.name() : GoodsAuthEnum.PASS.name());
    }


    /**
     * ????????????????????????
     *
     * @param boxGoodsId ??????id
     * @return ????????????
     */
    private BoxGoods checkExist(String boxGoodsId) {
        BoxGoods boxGoods = getById(boxGoodsId);
        if (boxGoods == null) {
            log.error("??????ID???" + boxGoodsId + "??????????????????");
            throw new ServiceException(ResultCode.GOODS_NOT_EXIST);
        }
        return boxGoods;
    }

    /**
     * ????????????????????????
     *
     * @param origin ??????
     * @param boxGoods  ??????
     */
    private void setGoodsGalleryParam(String origin, BoxGoods boxGoods) {
        GoodsGallery goodsGallery = goodsGalleryService.getGoodsGallery(origin);
        boxGoods.setBig(goodsGallery.getOriginal());
        boxGoods.setSmall(goodsGallery.getSmall());
        boxGoods.setLogo(goodsGallery.getThumbnail());
    }

}