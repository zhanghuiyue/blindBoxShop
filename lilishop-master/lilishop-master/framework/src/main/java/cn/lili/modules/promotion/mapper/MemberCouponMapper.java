package cn.lili.modules.promotion.mapper;

import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 会员优惠券数据处理层
 *
 * @author Chopper
 * @since 2020/8/21
 */
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {
    @Select("SELECT coupon_name,coupon_id,start_time,end_time,price,consume_threshold,get_type FROM li_member_coupon  WHERE member_id= #{memberId} and consume_threshold<#{totalPrice} and end_time>#{nowDate} and delete_flag =#{deleteFlag} and member_coupon_status = #{couponStatus}")
    List<MemberCoupon> queryBlidBoxCanUseCoupon(@Param("memberId")String memberId, @Param("totalPrice") Double totalPrice,@Param("nowDate") Date nowDate,@Param("deleteFlag")Boolean  deleteFlag,@Param("couponStatus")String  couponStatus);

    @Select("SELECT coupon_name,coupon_id,start_time,end_time,price,consume_threshold,get_type FROM li_member_coupon  WHERE member_id= #{memberId} or consume_threshold>#{totalPrice} or end_time<#{nowDate} and delete_flag =#{deleteFlag} and member_coupon_status != #{couponStatus}")
    List<MemberCoupon> queryBlidBoxUnUseCoupon(@Param("memberId")String memberId,@Param("totalPrice") Double totalPrice,@Param("nowDate") Date nowDate,@Param("deleteFlag")Boolean  deleteFlag,@Param("couponStatus")String  couponStatus);
}