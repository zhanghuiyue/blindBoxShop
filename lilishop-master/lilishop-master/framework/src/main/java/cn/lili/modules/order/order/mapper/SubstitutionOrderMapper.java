package cn.lili.modules.order.order.mapper;

import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.SubstitutionOrder;
import cn.lili.modules.order.order.entity.dto.SubstitutionOrderDTO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SubstitutionOrderMapper extends BaseMapper<SubstitutionOrder> {

    /**
     * 查询所有订单
     * @return
     */
    @Select("SELECT a.sn，a.create_time, a.order_status,a.pay_amount,a.complete_time,a.goods_id_path ,b.goods_name,b.price,b.small,c.specs FROM li_substitution_order a left join li_goods b on a.buy_goods_id = b.id left join li_goods_sku c on a.sku_id = b.id ${ew.customSqlSegment}")
    IPage<SubstitutionOrderDTO> queryAllOrder(IPage<SubstitutionOrderDTO> page, @Param(Constants.WRAPPER) Wrapper<SubstitutionOrderDTO> queryWrapper);



}
