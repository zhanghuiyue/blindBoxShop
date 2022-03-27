package cn.lili.modules.promotion.mapper;

import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * 会员优惠券数据处理层
 *
 * @author Chopper
 * @since 2020/8/21
 */
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {

}