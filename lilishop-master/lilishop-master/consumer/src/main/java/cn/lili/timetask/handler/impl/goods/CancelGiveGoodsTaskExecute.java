package cn.lili.timetask.handler.impl.goods;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.modules.goods.entity.dos.GiveGoods;
import cn.lili.modules.goods.entity.dos.Warehouse;
import cn.lili.modules.goods.entity.enums.GiveStatusEnum;
import cn.lili.modules.goods.service.GoodsGiveService;
import cn.lili.modules.goods.service.WarehouseService;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.entity.enums.MemberCouponStatusEnum;
import cn.lili.modules.promotion.service.MemberCouponService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.BindBoxOrderSetting;
import cn.lili.modules.system.entity.dto.GoodsGiveSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import cn.lili.timetask.handler.EveryMinuteExecute;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单自动取消（每分钟执行）
 *
 * @author paulG
 * @since 2021/3/11
 **/
@Slf4j
@Component
public class CancelGiveGoodsTaskExecute implements EveryMinuteExecute {
    /**
     * 订单
     */
    @Autowired
    private GoodsGiveService goodsGiveService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @Autowired
    private WarehouseService warehouseService;

    @Override
    public void execute() {
        Setting setting = settingService.get(SettingEnum.GIVE_SETTING.name());
        GoodsGiveSetting goodsGiveSetting = JSONUtil.toBean(setting.getSettingValue(), GoodsGiveSetting.class);
        if (goodsGiveSetting != null && goodsGiveSetting.getAutoCancel() != null) {
            //订单自动取消时间 = 当前时间 - 自动取消时间分钟数
            DateTime cancelTime = DateUtil.offsetHour(DateUtil.date(), -goodsGiveSetting.getAutoCancel());
            LambdaQueryWrapper<GiveGoods> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GiveGoods::getGiveStatus, GiveStatusEnum.GIVE.name());
            //订单创建时间 <= 订单自动取消时间
            queryWrapper.le(GiveGoods::getStartTime, cancelTime);
            List<GiveGoods> list = goodsGiveService.list(queryWrapper);
            List<String> cancelSnList = list.stream().map(GiveGoods::getGiveCode).collect(Collectors.toList());
            for (String sn : cancelSnList) {
                goodsGiveService.systemCancel(sn, "超时未领取自动取消");
            }
            List<Warehouse> warehouseList = new ArrayList<>();
            for (GiveGoods giveGoods:list) {
                Warehouse warehouse = warehouseService.queryWarehouse(giveGoods);
                warehouse.setGiveStatus(GiveStatusEnum.AUTOUNGIVE.name());
                warehouseList.add(warehouse);
            }
            warehouseService.updateBatchById(warehouseList);
        }
    }
}
