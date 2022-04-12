package cn.lili.modules.order.order.mapper;

import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.dos.SubstitutionOrder;
import cn.lili.modules.order.order.entity.dto.OrderExportDTO;
import cn.lili.modules.order.order.entity.dto.SubstitutionOrderDTO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSearchParams;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SubstitutionOrderMapper extends BaseMapper<SubstitutionOrder> {


    /**
     * 查询导出订单DTO列表
     *
     * @param queryWrapper 查询条件
     * @return 导出订单DTO列表
     */
    @Select("SELECT o.sn,o.create_time,o.order_status,o.goods_id_path,o.pay_amount,o.complete_time,GROUP_CONCAT(b.goods_name) as goods_name ,GROUP_CONCAT(c.simple_specs) as specs" +
            " ,GROUP_CONCAT(c.price) as price ,GROUP_CONCAT(c.small) as small FROM li_substitution_order o LEFT JOIN  li_goods b " +
            "ON o.buy_goods_id=b.id  LEFT JOIN  li_goods_sku c on o.sku_id = c.id ${ew.customSqlSegment}")
    IPage<SubstitutionOrderDTO> queryExportOrder(IPage<SubstitutionOrderSearchParams> page,@Param(Constants.WRAPPER) Wrapper<SubstitutionOrderSearchParams> queryWrapper);
}
