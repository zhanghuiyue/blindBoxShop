package cn.lili.modules.goods.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.blindBox.entity.dos.BoxGoods;
import cn.lili.modules.blindBox.service.BoxGoodsService;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.goods.entity.dos.ReplaceDetail;
import cn.lili.modules.goods.entity.dos.Warehouse;
import cn.lili.modules.goods.entity.dto.WarehouseDTO;
import cn.lili.modules.goods.entity.dto.search.WareHouseSearchParams;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.mapper.ReplaceDetailMapper;
import cn.lili.modules.goods.mapper.WarehouseMapper;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.goods.service.ReplaceDetailService;
import cn.lili.modules.goods.service.WarehouseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class ReplaceDetailServiceImpl extends ServiceImpl<ReplaceDetailMapper, ReplaceDetail> implements ReplaceDetailService {



}
