package cn.lili.modules.goods.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.dto.GiveGoodsDTO;
import cn.lili.modules.goods.entity.enums.ExchangeStatusEnum;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.entity.vos.GiveGoodsVO;
import cn.lili.modules.goods.mapper.BlindBoxGoodsMapper;
import cn.lili.modules.goods.mapper.GiveGoodsMapper;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.goods.service.GoodsGiveService;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.goods.service.GoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 商品赠送业务层实现
 */
@Service
public class GoodsGiveServiceImpl extends ServiceImpl<GiveGoodsMapper, GiveGoods> implements GoodsGiveService {

    @Autowired
    private BlindBoxGoodsService blindBoxGoodsService;
    @Autowired
    private GoodsSkuService goodsSkuService;
    @Autowired
    private GoodsService goodsService;
    /**
     * 赠送商品
     * @param giveGoodsDTO
     */
    @Override
    public String give(GiveGoodsDTO giveGoodsDTO) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        GiveGoods giveGoods = new GiveGoods();
        String giveCode = "PR"+UUID.randomUUID().toString();
        giveGoods.setGiveCode(giveCode);
        BeanUtil.copyProperties(giveGoodsDTO,giveGoods);
        giveGoods.setExchangeStatus(ExchangeStatusEnum.UNEXCHANGE.name());
        giveGoods.setGiveStatus(GiveStatusEnum.GIVE.name());
        giveGoods.setStartTime(new Date());
        giveGoods.setGiveMemberId(currentUser.getId());
        this.save(giveGoods);
        LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiveGoods::getGiveCode,giveCode);
        GiveGoods goods = this.baseMapper.selectOne(queryWrapper);
        return goods.getId();
    }

    /**
     * 查询赠送商品信息
     * @param id
     * @return
     */
    @Override
    public GiveGoodsVO giveGoodsQuery(String id) {
        GiveGoodsVO giveGoodsVO = new GiveGoodsVO();
        GiveGoods goods = this.baseMapper.selectById(id);
        giveGoodsVO.setGiveCode(goods.getGiveCode());
        //0表示奖品
        if("0".equals(goods.getGiveGoodsType())){
            BlindBoxGoods blindBoxGoods = blindBoxGoodsService.getById(goods.getGiveGoodsId());
            BeanUtil.copyProperties(blindBoxGoods,giveGoodsVO);
        }else {
            Goods good =  goodsService.getById(goods.getGiveGoodsId());
            giveGoodsVO.setSmall(good.getSmall());
            GoodsSku goodsSku = goodsSkuService.getById(goods.getGiveSkuId());
            giveGoodsVO.setGoodsName(giveGoodsVO.getGiveCode());
            giveGoodsVO.setSpecs(goodsSku.getSimpleSpecs());
        }
        return giveGoodsVO;
    }

    /**
     * 商品兑换
     * @param giveCode 赠送码
     */
    @Override
    public void giveGoodsExchange(String giveCode) {
        LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiveGoods::getGiveCode,giveCode);
        GiveGoods goods = this.baseMapper.selectOne(queryWrapper);
        if(GiveStatusEnum.UNGIVE.name().equals(goods.getGiveStatus())){
            throw new ServiceException(ResultCode.GOODS_EXCHANGE_ERROR);
        }

    }


}
