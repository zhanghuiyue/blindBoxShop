package cn.lili.modules.blindBox.serviceimpl;

import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCouponDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import cn.lili.modules.blindBox.mapper.PriceMapper;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.goods.entity.vos.CategoryVO;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.service.MemberCouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class BlindBoxPriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements BlindBoxPriceService {
    @Autowired
    private MemberCouponService memberCouponService;
    @Override
    public BlindBoxPriceVO queryPriceByCategory(String memberId, String categoryId) {
        BlindBoxPriceVO blindBoxPriceVO = new BlindBoxPriceVO();
        List<BlindBoxPriceDTO> blindBoxPriceDTOList = new ArrayList<>();
        List<BlindBoxCouponDTO> canUseCouponList = new ArrayList<>();
        List<BlindBoxCouponDTO> unUseCouponList = new ArrayList<>();
        List<Price> priceList = this.baseMapper.queryPriceList(categoryId);
        List<MemberCoupon> canUseCoupons = null;
        List<MemberCoupon> unUsedCoupons = null;
        for (Price price:priceList) {
            BlindBoxPriceDTO blindBoxPriceDTO = new BlindBoxPriceDTO();
            if(price.getNum()==0){
                canUseCoupons = memberCouponService.getBlidBoxCanUseCoupon(memberId,price.getPrice());
                if(!CollectionUtils.isEmpty(canUseCoupons)){
                    canUseCoupons.sort((MemberCoupon o1, MemberCoupon o2) -> {
                        if (o1.getPrice()> o2.getPrice()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    });
                    blindBoxPriceDTO.setDiscount(canUseCoupons.get(0).getPrice());
                    unUsedCoupons = memberCouponService.getBlidBoxUnUseCoupon(memberId,price.getPrice());
                }
            }else {
                blindBoxPriceDTO.setDiscount(price.getPrice()-price.getOriginalPrice());
            }
            BeanUtil.copyProperties(price, blindBoxPriceDTO);
            blindBoxPriceDTOList.add(blindBoxPriceDTO);
        }
        blindBoxPriceVO.blindBoxPriceDTOList=blindBoxPriceDTOList;
        if(!CollectionUtils.isEmpty(canUseCoupons)) {
            for (MemberCoupon useMemberCoupon : canUseCoupons) {
                BlindBoxCouponDTO blindBoxCouponDTO = new BlindBoxCouponDTO();
                BeanUtil.copyProperties(useMemberCoupon, blindBoxCouponDTO);
                canUseCouponList.add(blindBoxCouponDTO);
            }
        }
        if(!CollectionUtils.isEmpty(unUsedCoupons)) {
            for (MemberCoupon useMemberCoupon : unUsedCoupons) {
                BlindBoxCouponDTO blindBoxCouponDTO = new BlindBoxCouponDTO();
                BeanUtil.copyProperties(useMemberCoupon, blindBoxCouponDTO);
                canUseCouponList.add(blindBoxCouponDTO);
            }
        }
        blindBoxPriceVO.canUseCouponList = canUseCouponList;
        blindBoxPriceVO.unUseCouponList = unUseCouponList;
        return blindBoxPriceVO;
    }
}
