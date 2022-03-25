package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.blindBox.entity.dos.BoxSeckillApply;
import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillApplyVO;
import cn.lili.modules.blindBox.enums.PromotionsBoxApplyStatusEnum;
import cn.lili.modules.blindBox.mapper.BoxSeckillApplyMapper;
import cn.lili.modules.blindBox.service.BoxSeckillApplyService;
import cn.lili.modules.blindBox.service.BoxSeckillService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 秒杀申请业务层实现
 *
 * @author Chopper
 * @since 2020/8/21
 */
@Service
@Slf4j
public class BoxSeckillApplyServiceImpl extends ServiceImpl<BoxSeckillApplyMapper,BoxSeckillApply> implements BoxSeckillApplyService {

    /**
     * 秒杀
     */
    @Autowired
    private BoxSeckillService boxSeckillService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBoxSeckillApply(String seckillId, List<BoxSeckillApplyVO> seckillApplyList) {
        BoxSeckill boxSeckill = this.boxSeckillService.getById(seckillId);
        if (boxSeckill == null) {
            throw new ServiceException(ResultCode.SECKILL_BOX_NOT_EXIST_ERROR);
        }
        if (seckillApplyList == null || seckillApplyList.isEmpty()) {
            return;
        }
        //检查秒杀活动申请是否合法
        checkSeckillApplyList(boxSeckill.getHours(), seckillApplyList);
        //获取已参与活动的秒杀活动活动申请列表
        List<String> boxIds = seckillApplyList.stream().map(BoxSeckillApply::getBoxId).collect(Collectors.toList());
        List<BoxSeckillApply> boxSeckillApplyList = new ArrayList<>();

        for (BoxSeckillApplyVO boxSeckillApply : seckillApplyList) {

            if (null != boxSeckillApply.getBoxId() && boxSeckillApply.getBoxId().length() < 15) {
                //设置秒杀申请默认内容
                boxSeckillApply.setOriginalPrice(boxSeckillApply.getOriginalPrice());
                boxSeckillApply.setPromotionApplyStatus(PromotionsBoxApplyStatusEnum.PASS.name());
                boxSeckillApply.setSalesNum(0);
                boxSeckillApplyList.add(boxSeckillApply);
            }

        }

        this.remove(new LambdaQueryWrapper<BoxSeckillApply>().eq(BoxSeckillApply::getSeckillId, seckillId).in(BoxSeckillApply::getBoxId, boxIds));
        this.saveBatch(boxSeckillApplyList);


    }

    /**
     * 检查秒杀活动申请列表参数信息
     *
     * @param hours            秒杀活动时间段
     * @param boxSeckillApplyList 秒杀活动申请列表
     */
    private void checkSeckillApplyList(String hours, List<BoxSeckillApplyVO> boxSeckillApplyList) {
        List<String> existBox = new ArrayList<>();
        for (BoxSeckillApplyVO seckillApply : boxSeckillApplyList) {

            if (seckillApply.getPrice() > seckillApply.getOriginalPrice()) {
                throw new ServiceException(ResultCode.SECKILL_BOX_PRICE_ERROR);
            }
            //检查秒杀活动申请的时刻，是否存在在秒杀活动的时间段内
            String[] rangeHours = hours.split(",");
            boolean containsSame = Arrays.stream(rangeHours).anyMatch(i -> i.equals(seckillApply.getTimeLine().toString()));
            if (!containsSame) {
                throw new ServiceException(ResultCode.SECKILL_BOX_TIME_ERROR);
            }
            //检查商品是否参加多个时间段的活动
            if (existBox.contains(seckillApply.getBoxId())) {
                throw new ServiceException(seckillApply.getName() + "该盲盒不能同时参加多个时间段的活动");
            } else {
                existBox.add(seckillApply.getBoxId());
            }

        }
    }


    @Override
    public IPage<BoxSeckillApply> getBoxSeckillApplyPage(SeckillBoxSearchParams queryParam, PageVO pageVo) {
        IPage<BoxSeckillApply> seckillApplyPage = this.page(PageUtil.initPage(pageVo), queryParam.queryWrapper());
        return seckillApplyPage;
    }


}