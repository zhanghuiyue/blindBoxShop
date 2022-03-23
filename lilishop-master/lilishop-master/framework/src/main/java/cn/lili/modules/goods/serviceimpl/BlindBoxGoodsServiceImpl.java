package cn.lili.modules.goods.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.mapper.BlindBoxGoodsMapper;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlindBoxGoodsServiceImpl extends ServiceImpl<BlindBoxGoodsMapper, BlindBoxGoods> implements BlindBoxGoodsService {

    @Override
    public List<BlindBoxGoods> queryList(String categoryId) {
        QueryWrapper<BlindBoxGoods> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(categoryId)) {
            queryWrapper.eq("blind_box_category",categoryId);
        }
        queryWrapper.eq("auth_flag", "PASS");
        queryWrapper.gt("quantity","0");
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<BlindBoxGoods> batchQueryById(List<String> goodsId) {

        return this.baseMapper.selectBatchIds(goodsId);
    }
}
