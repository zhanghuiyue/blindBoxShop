package cn.lili.modules.order.order.mapper;

import cn.lili.modules.goods.entity.vos.CommodityVO;
import cn.lili.modules.order.order.entity.dos.BlindBoxOrder;
import cn.lili.modules.order.order.entity.vo.BoxOrderSimpleVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlindBoxOrderMapper extends BaseMapper<BlindBoxOrder> {


    /**
     * 获取商品订单列表
     *
     * @param queryWrapper 查询条件
     * @return
     */
    @Select("SELECT o.sn , o.create_time , o.flow_price , o.goods_price , o.discount_price  , gs.image  , o.order_status  , o.goods_num as num ,o. payment_method , o.payment_time  ,gs.goods_name as name  FROM li_order o LEFT JOIN li_order_item  gs ON o.sn = gs.order_sn ${ew.customSqlSegment}")
    List<BoxOrderSimpleVO> getGoodsOrderSimple(@Param(Constants.WRAPPER) Wrapper<BoxOrderSimpleVO> queryWrapper);


    /**
     * 获取盲盒订单列表
     *
     * @param queryWrapper 查询条件
     * @return
     */
    @Select("SELECT o.sn , o.create_time , o.flow_price , o.goods_price , o.discount_price  , gs.image  , o.order_status  , o.goods_num as num ,o. payment_method , o.payment_time  ,gs. name  FROM li_blind_box_order o LEFT JOIN li_blind_box_category  gs ON o.blind_box_category = gs.id ${ew.customSqlSegment}")
    List<BoxOrderSimpleVO> getBoxOrderSimple(@Param(Constants.WRAPPER) Wrapper<BoxOrderSimpleVO> queryWrapper);

    /**
     * 获取盲盒订单详情
     *
     * @param sn 查询条件
     * @return
     */
    @Select("SELECT o.sn , o.create_time , o.flow_price , o.goods_price , o.discount_price  , gs.image  , o.order_status  , o.goods_num as num ,o. payment_method , o.payment_time  ,gs. name  FROM li_blind_box_order o LEFT JOIN li_blind_box_category  gs ON o.blind_box_category = gs.id where  o.sn = #{sn} ")
    BoxOrderSimpleVO  getBoxOrderDetail(String sn) ;



    /**
     * 获取商品订单详情
     *
     * @param sn 查询条件
     * @return
     */
    @Select("SELECT o.sn , o.create_time , o.flow_price , o.goods_price , o.discount_price  , gs.image  , o.order_status  , o.goods_num as num ,o. payment_method , o.payment_time  ,gs.goods_name as name  FROM li_order o LEFT JOIN li_order_item  gs ON o.sn = gs.order_sn where  o.sn = #{sn} ")
   BoxOrderSimpleVO getGoodsOrderDetail(String sn);

}
