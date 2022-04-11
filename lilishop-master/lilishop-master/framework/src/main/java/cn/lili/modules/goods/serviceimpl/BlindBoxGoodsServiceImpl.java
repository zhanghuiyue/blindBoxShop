package cn.lili.modules.goods.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.mapper.BlindBoxGoodsMapper;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxGoodsServiceImpl extends ServiceImpl<BlindBoxGoodsMapper, BlindBoxGoods> implements BlindBoxGoodsService {

    /**
     * 查询盲盒商品列表
     * @param blindBoxId
     * @return
     */
    @Override
    public List<BlindBoxGoods> queryList(String blindBoxId) {
        LambdaQueryWrapper<BlindBoxGoods> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(blindBoxId)) {
            queryWrapper.eq(BlindBoxGoods::getBlindBoxId, blindBoxId);
        }
        queryWrapper.eq(BlindBoxGoods::getAuthFlag,"PASS");
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 批量查询盲盒商品
     * @param goodsId
     * @return List<BlindBoxGoods>
     */
    @Override
    public List<BlindBoxGoods> batchQueryById(List<String> goodsId) {
        return this.baseMapper.selectBatchIds(goodsId);
    }

    /**
     * 查询商品详情
     * @param goodsId 商品编号
     * @return BlindBoxGoods
     */
    @Override
    public BlindBoxGoods queryProductDetails(String goodsId) {
        return this.baseMapper.selectById(goodsId);
    }
}
