package cn.lili.modules.blindBox.serviceimpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCouponDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import cn.lili.modules.blindBox.entity.dto.search.PriceSearchParams;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import cn.lili.modules.blindBox.mapper.PriceMapper;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.promotion.entity.dos.MemberCoupon;
import cn.lili.modules.promotion.service.MemberCouponService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 盲盒价格业务层
 */
@Service
public class BlindBoxPriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements BlindBoxPriceService {

    @Autowired
    private MemberCouponService memberCouponService;

    /**
     * 查询盲盒价格
     * @param memberId 会员编号
     * @param categoryId 分类编号
     * @return BlindBoxPriceVO
     */
    @Override
    public BlindBoxPriceVO queryPriceByCategory(String memberId, String categoryId) {
        BlindBoxPriceVO blindBoxPriceVO = new BlindBoxPriceVO();
        List<BlindBoxPriceDTO> blindBoxPriceDTOList = new ArrayList<>();
        List<BlindBoxCouponDTO> canUseCouponList = new ArrayList<>();
        List<BlindBoxCouponDTO> unUseCouponList = new ArrayList<>();
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Price::getBlindBoxId,categoryId);
        List<Price> priceList = this.baseMapper.selectList(queryWrapper);
        List<MemberCoupon> canUseCoupons = null;
        List<MemberCoupon> unUsedCoupons = null;
        for (Price price:priceList) {
            BlindBoxPriceDTO blindBoxPriceDTO = new BlindBoxPriceDTO();
            if(price.getNum()==1){
                canUseCoupons = memberCouponService.getBlidBoxCanUseCoupon(memberId,price.getPrice());
                if(!CollectionUtils.isEmpty(canUseCoupons)){
                                     canUseCoupons.sort((MemberCoupon o1, MemberCoupon o2) -> {
                        if (o1.getPrice()> o2.getPrice()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    });
                    blindBoxPriceDTO.setDiscount(canUseCoupons.get(0).getPrice()<0?0.00:canUseCoupons.get(0).getPrice());
                    unUsedCoupons = memberCouponService.getBlidBoxUnUseCoupon(memberId,price.getPrice());
                }
            }else {
                blindBoxPriceDTO.setDiscount(price.getOriginalPrice()-price.getPrice());
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
                unUseCouponList.add(blindBoxCouponDTO);
            }
        }
        blindBoxPriceVO.canUseCouponList = canUseCouponList;
        blindBoxPriceVO.unUseCouponList = unUseCouponList;
        return blindBoxPriceVO;
    }

    @Override
    public IPage<Price> queryPriceByPage(PriceSearchParams priceSearchParams) {
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(priceSearchParams.getName())) {
            queryWrapper.eq(Price::getName, priceSearchParams.getName());
        }
        if(StringUtils.isNotBlank(priceSearchParams.getBlindBoxId())) {
            queryWrapper.eq(Price::getBlindBoxId, priceSearchParams.getBlindBoxId());
        }
        return this.page(PageUtil.initPage(priceSearchParams),queryWrapper);
    }

    @Override
    public void batchAdd(List<BlindBoxPriceDTO> blindBoxPriceDTOs) {
        List<Price> prices = new ArrayList<>();
        for (BlindBoxPriceDTO blindBoxPriceDTO:blindBoxPriceDTOs) {
            Price price = new Price();
            BeanUtil.copyProperties(blindBoxPriceDTO,price);
            prices.add(price);
        }
        this.saveBatch(prices);
    }

    @Override
    public boolean update(BlindBoxPriceDTO blindBoxPriceDTO) {
        Price price = new Price();
        BeanUtil.copyProperties(blindBoxPriceDTO,price);
        this.checkExist(blindBoxPriceDTO.getId());
        if (getOne(new LambdaQueryWrapper<Price>().eq(Price::getName, blindBoxPriceDTO.getName()).ne(Price::getId, blindBoxPriceDTO.getId())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_PRICE_NAME_EXIST_ERROR);
        }
        return this.updateById(price);
    }

    /**
     * 校验是否存在
     *
     * @param id 分类ID
     * @return
     */
    private Price checkExist(String id) {
        Price price = getById(id);
        if (price == null) {
            log.error("价格ID为" + id + "的价格不存在");
            throw new ServiceException(ResultCode.BLIND_BOX_PRICE_NOT_EXIST);
        }
        return price;
    }

    @Override
    public void deleteByCategoryId(String categoryId) {
        LambdaQueryWrapper<Price> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Price::getBlindBoxId,categoryId);
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public boolean deleteById(String id) {
        return this.removeById(id);
    }

    @Override
     public List<String> batchQuery(List<String> categoryIds) {
        List<Price> prices = new LambdaQueryChainWrapper<Price>(this.baseMapper).ge(Price::getBlindBoxId,categoryIds).list();
        List<String> ids = new ArrayList<>();
        for (Price price:prices) {
            ids.add(price.getId());
        }
        return ids;
    }

    @Override
    public void batchDelete(List<String> ids) {
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public boolean add(BlindBoxPriceDTO blindBoxPriceDTO) {
        if (getOne(new LambdaQueryWrapper<Price>().eq(Price::getName, blindBoxPriceDTO.getName()).eq(Price::getBlindBoxId, blindBoxPriceDTO.getBlindBoxId())) != null) {
            throw new ServiceException(ResultCode.BLIND_BOX_PRICE_NAME_EXIST_ERROR);
        }
        Price price = new Price();
        BeanUtil.copyProperties(blindBoxPriceDTO,price);
        return this.save(price);
    }
    @Override
    public boolean blindBoxPriceDisable(String id, boolean disable) {
        Price price = this.checkExist(id);
        price.setDeleteFlag(disable);
        return updateById(price);
    }

}
