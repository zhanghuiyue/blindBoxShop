package cn.lili.modules.goods.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.blindBox.service.BoxGoodsService;
import cn.lili.modules.goods.entity.dos.*;
import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.modules.goods.entity.dto.search.WareHouseSearchParams;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.entity.vos.ReplaceDetailVO;
import cn.lili.modules.goods.mapper.WarehouseMapper;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.goods.service.ReplaceDetailService;
import cn.lili.modules.goods.service.ReplaceOrderService;
import cn.lili.modules.goods.service.WarehouseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 盲盒仓库业务层实现
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    /**
     * 盲盒商品业务层
     */
    @Autowired
    private BoxGoodsService boxGoodsService;

    /**
     *
     * 商品业务层
     */
    @Autowired
    private GoodsSkuService goodsSkuService;

    /**
     *
     * 置换详情业务层
     */
    @Autowired
    private ReplaceDetailService replaceDetailService;

    /**
     *
     * 置换订单业务层
     */
    @Autowired
    private ReplaceOrderService replaceOrderService;



    @Override
    public Warehouse queryWarehouse(GiveGoods goods) {
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Warehouse::getMemberId,goods.getGiveMemberId());
        queryWrapper.eq(Warehouse::getGoodsId,goods.getGiveGoodsId());
        if(StringUtils.isNotBlank(goods.getGiveSkuId())){
            queryWrapper.eq(Warehouse::getSkuId,goods.getGiveSkuId());
        }
        queryWrapper.eq(Warehouse::getGoodsType,goods.getGiveGoodsType());
        queryWrapper.eq(Warehouse::getGiveStatus, GiveStatusEnum.GIVE.name());
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<WarehouseDTO> queryByParams(WareHouseSearchParams warehouseSearchParams) {
        List<Warehouse>  warehouseList =this.baseMapper.selectList(warehouseSearchParams.queryWrapper());
        List<WarehouseDTO> warehouseDTOList = new ArrayList<WarehouseDTO>();

        for (Warehouse wareHouse: warehouseList) {
            WarehouseDTO warehouseDTO = new WarehouseDTO(wareHouse);
            String  goods_id = wareHouse.getGoodsId();
            String sku_id = wareHouse.getSkuId();
            if(null != goods_id &&!goods_id.equals("") ){
                BoxGoods  boxGoods =boxGoodsService.getById(goods_id);
                if(null != boxGoods) {
                    warehouseDTO.setPrice(boxGoods.getPrice());
                    warehouseDTO.setGoodsName(boxGoods.getGoodsName());
                    warehouseDTO.setSinewyBean(boxGoods.getSinewyBean());
                    warehouseDTO.setSmall(boxGoods.getSmall());
                }

            }else if(null != sku_id &&!sku_id.equals("") ){

                GoodsSku goodsSku =  goodsSkuService.getById(sku_id);
                if(null != goodsSku) {
                    warehouseDTO.setPrice(goodsSku.getPrice());
                    warehouseDTO.setGoodsName(goodsSku.getGoodsName());
                    warehouseDTO.setSinewyBean(goodsSku.getSinewyBean());
                    warehouseDTO.setSmall(goodsSku.getSmall());
                }

            }
            warehouseDTOList.add(warehouseDTO);
        }
        return warehouseDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReplaceOrder(String goodsId , List<WarehouseDTO> warehouseDTOs) {
        GoodsSku goodsSku =checkExist(goodsId);
        String sn= "e" + SnowFlake.getId();
        Double deductionAmount =0.00;
        for (WarehouseDTO warehouseDTO : warehouseDTOs) {
            ReplaceDetail replaceDetail = new ReplaceDetail();
            replaceDetail.setGoodsId(warehouseDTO.getGoodsId());
            replaceDetail.setSkuId(warehouseDTO.getSkuId());
            replaceDetail.setPrice(warehouseDTO.getPrice());
            replaceDetail.setSn(sn);
            deductionAmount=deductionAmount+warehouseDTO.getPrice();
            replaceDetail.setOrderStatus("UNPAID");
            replaceDetailService.save(replaceDetail);

            //更改仓库动态
             Warehouse warehouse = new Warehouse(warehouseDTO);
             warehouse.setSubstitutionFlag("1");//不可置换
             warehouse.setExchangeFlag("1");//不可兑换
             warehouse.setPickUpGoodsFlag("1");//不可提货
             this.updateById(warehouse);
        }
        Double goodsPrice =goodsSku.getPrice();
        ReplaceOrder replaceOrder = new  ReplaceOrder() ;
        replaceOrder.setGoodsId(goodsSku.getGoodsId());
        replaceOrder.setPrice(goodsPrice);
        replaceOrder.setSn(sn);
        replaceOrder.setDeduction(deductionAmount);
        replaceOrder.setDisparity(goodsPrice-deductionAmount);

        Double actual_amount =  0.00 ;
        if(deductionAmount+50<goodsPrice){

            actual_amount =goodsPrice -deductionAmount ;
        }
        replaceOrder.setActualAmount(actual_amount);
        replaceOrder.setOrderStatus("UNPAID");
        replaceOrderService.save(replaceOrder);


    }

    /**
     * 判断商品是否存在
     *
     * @param goodsId 商品id
     * @return 商品信息
     */
    private GoodsSku checkExist(String goodsId) {
        GoodsSku goodsSku = goodsSkuService.getById(goodsId);
        if (goodsSku == null) {
            log.error("商品ID为" + goodsId + "的商品不存在");
            throw new ServiceException(ResultCode.GOODS_NOT_EXIST);
        }
        return goodsSku;
    }

    /**
     * 用户置换记录
     * @return
     */
    public List<ReplaceOrder> getReplaceOrderList(){

        List<ReplaceOrder>  replaceOrderDTOList = new ArrayList<ReplaceOrder>();
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());

        QueryWrapper<ReplaceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", currentUser.getId());
        List<ReplaceOrder>  replaceOrders =this.replaceOrderService.list(queryWrapper);
        for (ReplaceOrder replaceOrder : replaceOrders) {
            QueryWrapper<ReplaceDetail> queryWrapperDetail = new QueryWrapper<>();
            queryWrapperDetail.eq("sn", replaceOrder.getSn());
            List<ReplaceDetail>  replaceDetailList = this.replaceDetailService.list(queryWrapperDetail);
            List<ReplaceDetailVO>  replaceDetailVOList =new ArrayList<ReplaceDetailVO>();
            for (ReplaceDetail replaceDetail : replaceDetailList) {
                String  goods_id = replaceDetail.getGoodsId();
                String sku_id = replaceDetail.getSkuId();
                ReplaceDetailVO  replaceDetailVO =new ReplaceDetailVO();
                BeanUtil.copyProperties(replaceDetail, replaceDetailVO);
                if(null != goods_id &&!goods_id.equals("") ){
                    BoxGoods  boxGoods =boxGoodsService.getById(goods_id);
                    if(null != boxGoods) {
                        replaceDetailVO.setPrice(boxGoods.getPrice());
                        replaceDetailVO.setGoodsName(boxGoods.getGoodsName());
                        replaceDetailVO.setSmall(boxGoods.getSmall());
                    }
                }else if(null != sku_id &&!sku_id.equals("") ){
                    GoodsSku goodsSku =  goodsSkuService.getById(sku_id);
                    if(null != goodsSku) {
                        replaceDetailVO.setPrice(goodsSku.getPrice());
                        replaceDetailVO.setGoodsName(goodsSku.getGoodsName());
                        replaceDetailVO.setSmall(goodsSku.getSmall());
                    }

                }
                replaceDetailVOList.add(replaceDetailVO);
            }
            replaceOrder.setReplaceDetailList(replaceDetailVOList);
        }
        return replaceOrderDTOList;
    }
}
