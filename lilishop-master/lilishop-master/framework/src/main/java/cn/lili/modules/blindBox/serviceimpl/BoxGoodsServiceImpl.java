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
 * 商品业务层实现
 *
 * @author pikachu
 * @since 2020-02-23 15:18:56
 */
@Service
public class BoxGoodsServiceImpl extends ServiceImpl<BoxGoodsMapper, BoxGoods> implements BoxGoodsService {

    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    /**
     * 商品相册
     */
    @Autowired
    private GoodsGalleryService goodsGalleryService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBoxGoods(BoxGoodsOperationDTO boxGoodsOperationDTO) {
        BoxGoods boxGoods = new BoxGoods(boxGoodsOperationDTO);
        //检查商品
        this.checkGoods(boxGoods);
        //向goods加入图片
        this.setGoodsGalleryParam(boxGoodsOperationDTO.getGoodsGalleryList().get(0), boxGoods);
        //添加商品
        this.save(boxGoods);

        //添加相册
        if (boxGoodsOperationDTO.getGoodsGalleryList() != null && !boxGoodsOperationDTO.getGoodsGalleryList().isEmpty()) {
            this.goodsGalleryService.add(boxGoodsOperationDTO.getGoodsGalleryList(), boxGoods.getId());
        }
    }


    /**
     * 检查商品信息
     * 如果商品是虚拟商品则无需配置配送模板
     * 如果商品是实物商品需要配置配送模板
     * 判断商品是否存在
     * 判断商品是否需要审核
     * 判断当前用户是否为店铺
     *
     * @param boxGoods 商品
     */
    private void checkGoods(BoxGoods boxGoods) {

        //检查商品是否存在--修改商品时使用
        if (boxGoods.getId() != null) {
            this.checkExist(boxGoods.getId());
        } else {
            boxGoods.setQuantity(0);
        }
        //获取商品系统配置决定是否审核
        Setting setting = settingService.get(SettingEnum.GOODS_SETTING.name());
        GoodsSetting goodsSetting = JSONUtil.toBean(setting.getSettingValue(), GoodsSetting.class);
        //是否需要审核
        boxGoods.setAuthFlag(Boolean.TRUE.equals(goodsSetting.getGoodsCheck()) ? GoodsAuthEnum.TOBEAUDITED.name() : GoodsAuthEnum.PASS.name());
    }


    /**
     * 判断商品是否存在
     *
     * @param boxGoodsId 商品id
     * @return 商品信息
     */
    private BoxGoods checkExist(String boxGoodsId) {
        BoxGoods boxGoods = getById(boxGoodsId);
        if (boxGoods == null) {
            log.error("商品ID为" + boxGoodsId + "的商品不存在");
            throw new ServiceException(ResultCode.GOODS_NOT_EXIST);
        }
        return boxGoods;
    }

    /**
     * 添加商品默认图片
     *
     * @param origin 图片
     * @param boxGoods  商品
     */
    private void setGoodsGalleryParam(String origin, BoxGoods boxGoods) {
        GoodsGallery goodsGallery = goodsGalleryService.getGoodsGallery(origin);
        boxGoods.setBig(goodsGallery.getOriginal());
        boxGoods.setSmall(goodsGallery.getSmall());
        boxGoods.setLogo(goodsGallery.getThumbnail());
    }

}