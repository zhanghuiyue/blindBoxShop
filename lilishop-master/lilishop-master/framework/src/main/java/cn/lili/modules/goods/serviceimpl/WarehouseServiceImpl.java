package cn.lili.modules.goods.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.SnowFlake;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.blindBox.service.BoxGoodsService;
import cn.lili.modules.goods.entity.dos.*;
import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.modules.goods.entity.dto.search.WareHouseSearchParams;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.mapper.WarehouseMapper;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.goods.service.ReplaceDetailService;
import cn.lili.modules.goods.service.ReplaceOrderService;
import cn.lili.modules.goods.service.WarehouseService;
import cn.lili.modules.payment.kit.plugin.wechat.model.GoodsDetail;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            System.out.println("22商品信息："+wareHouse.getId());
            System.out.println("商品信息："+goods_id +"2323Sku_id："+sku_id);
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


}
