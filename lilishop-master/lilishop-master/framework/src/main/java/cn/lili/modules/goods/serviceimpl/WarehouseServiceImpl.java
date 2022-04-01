package cn.lili.modules.goods.serviceimpl;

import cn.lili.common.utils.StringUtils;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.Warehouse;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.mapper.BlindBoxGoodsMapper;
import cn.lili.modules.goods.mapper.WarehouseMapper;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.goods.service.WarehouseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 盲盒仓库业务层实现
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

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
}
