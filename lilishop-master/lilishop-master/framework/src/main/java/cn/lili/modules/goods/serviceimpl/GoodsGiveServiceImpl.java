package cn.lili.modules.goods.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.service.BlindBoxService;
import cn.lili.modules.blindBox.service.TribeService;
import cn.lili.modules.goods.entity.dos.*;
import cn.lili.modules.goods.entity.dto.GiveGoodsDTO;
import cn.lili.modules.goods.entity.enums.ExchangeStatusEnum;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.entity.vos.GiveGoodsVO;
import cn.lili.modules.goods.mapper.GiveGoodsMapper;
import cn.lili.modules.goods.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private TribeService tribeService;
    @Autowired
    private BlindBoxService blindBoxService;

    /**
     * 赠送商品
     * @param giveGoodsDTO
     */
    @Override
    @Transactional
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
        giveGoods.setWarehouseTribeId(giveGoodsDTO.getId());
        this.save(giveGoods);
        LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiveGoods::getGiveCode,giveCode);
        GiveGoods goods = this.baseMapper.selectOne(queryWrapper);
        if(!"3".equals(giveGoodsDTO.getGiveGoodsType())) {
            LambdaUpdateWrapper<Warehouse> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Warehouse::getGoodsId, giveGoodsDTO.getGiveGoodsId());
            if (StringUtils.isNotBlank(goods.getGiveSkuId())) {
                updateWrapper.eq(Warehouse::getSkuId, giveGoodsDTO.getGiveSkuId());
            }
            updateWrapper.eq(Warehouse::getMemberId, currentUser.getId());
            updateWrapper.eq(Warehouse::getGoodsType, giveGoodsDTO.getGiveGoodsType());
            updateWrapper.set(Warehouse::getGiveStatus, GiveStatusEnum.GIVE.name());
            warehouseService.update(updateWrapper);
        }else {
            LambdaUpdateWrapper<Tribe> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Tribe::getBlindBoxId, giveGoodsDTO.getGiveGoodsId());
            updateWrapper.eq(Tribe::getMemberId, currentUser.getId());
            updateWrapper.eq(Tribe::getBlindBoxType, giveGoodsDTO.getBlindBoxType());
            updateWrapper.set(Tribe::getGiveStatus, GiveStatusEnum.GIVE.name());
            tribeService.update(updateWrapper);
        }
        return goods.getId();
    }

    @Override
    @Transactional
    public void cancel(GiveGoodsDTO giveGoodsDTO) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiveGoods::getGiveMemberId,currentUser.getId());
        queryWrapper.eq(GiveGoods::getGiveGoodsId,giveGoodsDTO.getGiveGoodsId());
        if(StringUtils.isNotBlank(giveGoodsDTO.getGiveSkuId())){
            queryWrapper.eq(GiveGoods::getGiveSkuId,giveGoodsDTO.getGiveSkuId());
        }
        queryWrapper.eq(GiveGoods::getGiveGoodsType,giveGoodsDTO.getGiveGoodsType());
        queryWrapper.eq(GiveGoods::getGiveStatus, GiveStatusEnum.GIVE.name());
        GiveGoods giveGoods = this.baseMapper.selectOne(queryWrapper);
        if(giveGoods == null){
            throw new ServiceException(ResultCode.GOODS_NOT_EXIT_ERROR);
        }
        giveGoods.setGiveStatus(GiveStatusEnum.CANCELGIVE.name());
        this.baseMapper.updateById(giveGoods);
        if(!"3".equals(giveGoodsDTO.getGiveGoodsType())) {
            Warehouse updateWarehouse = new Warehouse();
            updateWarehouse.setId(giveGoodsDTO.getId());
            updateWarehouse.setGiveStatus(GiveStatusEnum.CANCELGIVE.name());
            warehouseService.getBaseMapper().updateById(updateWarehouse);
        }else {
            Tribe tribe = new Tribe();
            tribe.setId(giveGoodsDTO.getId());
            tribe.setGiveStatus(GiveStatusEnum.CANCELGIVE.name());
            tribeService.getBaseMapper().updateById(tribe);
        }
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
        }else if("3".equals(goods.getGiveGoodsType())){
            BlindBox blindBox =  blindBoxService.getById(goods.getGiveGoodsId());
            giveGoodsVO.setGoodsName(blindBox.getName());
            giveGoodsVO.setSmall(blindBox.getImage());
        }else {
            Goods good =  goodsService.getById(goods.getGiveGoodsId());
            giveGoodsVO.setSmall(good.getSmall());
            GoodsSku goodsSku = goodsSkuService.getById(goods.getGiveSkuId());
            giveGoodsVO.setGoodsName(goodsSku.getGoodsName());
            giveGoodsVO.setSpecs(goodsSku.getSimpleSpecs());
        }
        return giveGoodsVO;
    }

    /**
     * 商品兑换
     * @param giveCode 赠送码
     */
    @Override
    @Transactional
    public void giveGoodsExchange(String giveCode) {
        LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GiveGoods::getGiveCode,giveCode);
        GiveGoods goods = this.baseMapper.selectOne(queryWrapper);
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        if(currentUser.getId().equals(goods.getGiveMemberId())){
            throw new ServiceException(ResultCode.GOODS_EXCHANGE_MEMBER_ERROR);
        }
        if(!GiveStatusEnum.GIVE.name().equals(goods.getGiveStatus())){
            throw new ServiceException(ResultCode.GOODS_EXCHANGE_ERROR);
        }
        if(ExchangeStatusEnum.EXCHANGE.name().equals(goods.getExchangeStatus())){
            throw new ServiceException(ResultCode.GOODS_EXCHANGE_ERROR);
        }
        goods.setGiveStatus(GiveStatusEnum.GIVEED.name());
        goods.setExchangeStatus(ExchangeStatusEnum.EXCHANGE.name());
        goods.setGivedMemberId(currentUser.getId());
        this.baseMapper.updateById(goods);
        if(!"3".equals(goods.getGiveGoodsType())) {
            Warehouse updateWarehouse = new Warehouse();
            updateWarehouse.setId(goods.getWarehouseTribeId());
            updateWarehouse.setGiveStatus(GiveStatusEnum.GIVEED.name());
            warehouseService.getBaseMapper().updateById(updateWarehouse);
            Warehouse insertWarehouse = new Warehouse(currentUser.getId());
            insertWarehouse.setSubstitutionFlag("1");
            insertWarehouse.setGoodsType(goods.getGiveGoodsType());
            insertWarehouse.setGoodsId(goods.getGiveGoodsId());
            insertWarehouse.setSkuId(goods.getGiveSkuId());
            warehouseService.getBaseMapper().insert(insertWarehouse);
        }else {
            Tribe updateTribe = new Tribe();
            updateTribe.setId(goods.getWarehouseTribeId());
            updateTribe.setGiveStatus(GiveStatusEnum.GIVEED.name());
            tribeService.getBaseMapper().updateById(updateTribe);
            Tribe tribe = new Tribe();
            tribe.setGiveStatus(GiveStatusEnum.UNGIVE.name());
            tribe.setExtractStatus(ExchangeStatusEnum.UNEXCHANGE.name());
            tribe.setBlindBoxId(goods.getGiveGoodsId());
            BlindBox blindBox =  blindBoxService.getById(goods.getGiveGoodsId());
            BeanUtil.copyProperties(blindBox,tribe);
            tribeService.getBaseMapper().insert(tribe);
        }
    }

    @Override
    public void systemCancel(String giveCode, String reason) {
        GiveGoods giveGoods = this.getByGiveCode(giveCode);
        giveGoods.setGiveStatus(GiveStatusEnum.CANCELGIVE.name());
        giveGoods.setCancelReason(reason);
        this.updateById(giveGoods);
    }

    @Override
    public GiveGoods getByGiveCode(String giveCode) {
        return this.getOne(new LambdaQueryWrapper<GiveGoods>().eq(GiveGoods::getGiveCode, giveCode));
    }


}
