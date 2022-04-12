package cn.lili.modules.order.order.serviceimpl;

import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.order.order.entity.dos.SubstitutionOrder;
import cn.lili.modules.order.order.entity.dto.SubstitutionGoodsDTO;
import cn.lili.modules.order.order.entity.dto.SubstitutionOrderDTO;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSearchParams;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSimpleVO;
import cn.lili.modules.order.order.mapper.SubstitutionOrderMapper;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.SubstitutionOrderService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubstitutionOrderServiceImpl extends ServiceImpl<SubstitutionOrderMapper, SubstitutionOrder> implements SubstitutionOrderService {

    @Autowired
    private BlindBoxGoodsService blindBoxGoodsService;
    @Override
    public IPage<SubstitutionOrderDTO> querySubstitutionOrder(SubstitutionOrderSearchParams substitutionOrderSearchParams) {
        QueryWrapper queryWrapper = substitutionOrderSearchParams.queryWrapper();
        queryWrapper.groupBy("o.id");
        queryWrapper.orderByDesc("o.id");
        IPage<SubstitutionOrderDTO> substitutionOrderDTOIPage = this.baseMapper.queryExportOrder(PageUtil.initPage(substitutionOrderSearchParams),queryWrapper);
        if(CollectionUtils.isNotEmpty(substitutionOrderDTOIPage.getRecords())){
            List<String> substitutionIdList = new ArrayList<>();
            for (SubstitutionOrderDTO substitutionOrder :substitutionOrderDTOIPage.getRecords()) {
                String goodIdPath = substitutionOrder.getGoodsIdPath();
                String [] goodIdPaths = goodIdPath.split(",");
                for (String goodId:goodIdPaths) {
                    substitutionIdList.add(goodId);
                }
            }
            List<BlindBoxGoods> blindBoxGoods = blindBoxGoodsService.batchQueryById(substitutionIdList);
            Map<String,BlindBoxGoods> blindBoxGoodsMap = new HashMap<>();
            for (BlindBoxGoods boxGoods:blindBoxGoods) {
                blindBoxGoodsMap.put(boxGoods.getId(),boxGoods);
            }
            List<SubstitutionOrderDTO> substitutionOrderDTOList = new ArrayList<>();
            for (SubstitutionOrderDTO substitutionOrder:substitutionOrderDTOIPage.getRecords()) {
                List<SubstitutionGoodsDTO> substitutionGoodsDTOList = new ArrayList<>();
                String [] goodIdPaths = substitutionOrder.getGoodsIdPath().split(",");
                for (String goodId:goodIdPaths) {
                    SubstitutionGoodsDTO substitutionGoodsDTO = new SubstitutionGoodsDTO();
                    BlindBoxGoods boxGoods = blindBoxGoodsMap.get(goodId);
                    if(boxGoods!=null) {
                        BeanUtil.copyProperties(boxGoods, substitutionGoodsDTO);
                    }
                    substitutionGoodsDTOList.add(substitutionGoodsDTO);
                }
                substitutionOrder.setSubstitutionGoodsDTOList(substitutionGoodsDTOList);
                substitutionOrderDTOList.add(substitutionOrder);
            }
        }

        return substitutionOrderDTOIPage;
    }
}
