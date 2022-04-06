package cn.lili.modules.blindBox.serviceimpl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.blindBox.entity.dos.BoxSeckillApply;
import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillApplyVO;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillBoxVO;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillTimelineVO;
import cn.lili.modules.blindBox.enums.PromotionsBoxApplyStatusEnum;
import cn.lili.modules.blindBox.mapper.BoxSeckillApplyMapper;
import cn.lili.modules.blindBox.service.BlindBoxService;
import cn.lili.modules.blindBox.service.BoxSeckillApplyService;
import cn.lili.modules.blindBox.service.BoxSeckillService;
import cn.lili.modules.promotion.entity.enums.PromotionsApplyStatusEnum;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private BlindBoxService blindBoxService;


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

    @Override
    public List<BoxSeckillTimelineVO> getBoxSeckillTimeline() {
        //秒杀活动缓存key
        return getBoxSeckillTimelineInfo();
    }

    /**
     * 获取秒杀活动信息
     *
     * @return 秒杀活动信息
     */
    private List<BoxSeckillTimelineVO> getBoxSeckillTimelineInfo() {
        List<BoxSeckillTimelineVO> timelineList = new ArrayList<>();
        LambdaQueryWrapper<BoxSeckill> queryWrapper = new LambdaQueryWrapper<>();
        //查询当天时间段内的秒杀活动活动
        Date now = new Date();
        queryWrapper.between(BoxSeckill::getStartTime, DateUtil.beginOfDay(now), DateUtil.endOfDay(now));
        /*queryWrapper.ge(BoxSeckill::getEndTime, DateUtil.endOfDay(now));*/
        List<BoxSeckill> seckillList = this.boxSeckillService.list(queryWrapper);
        for (BoxSeckill boxSeckill : seckillList) {
            //读取系统时间的时刻
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            String[] split = boxSeckill.getHours().split(",");
            int[] hoursSored = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
            Arrays.sort(hoursSored);
            for (int i = 0; i < hoursSored.length; i++) {
                BoxSeckillTimelineVO tempTimeline = new BoxSeckillTimelineVO();
                boolean hoursSoredHour = (hoursSored[i] >= hour || ((i + 1) < hoursSored.length && hoursSored[i + 1] > hour));
                if (hoursSoredHour) {
                    SimpleDateFormat format = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
                    String date = format.format(new Date());
                    //当前时间的秒数
                    long currentTime = DateUtil.currentSeconds();
                    //秒杀活动的时刻
                    long timeLine = cn.lili.common.utils.DateUtil.getDateline(date + " " + hoursSored[i], "yyyy-MM-dd HH");

                    Long distanceTime = timeLine - currentTime < 0 ? 0 : timeLine - currentTime;
                    tempTimeline.setDistanceStartTime(distanceTime);
                    tempTimeline.setStartTime(timeLine);
                    tempTimeline.setTimeLine(hoursSored[i]);
                    tempTimeline.setSeckillBoxList(wrapperSeckillBox(hoursSored[i], boxSeckill.getId()));
                    timelineList.add(tempTimeline);
                }
            }
        }
        return timelineList;
    }

    /**
     * 组装当时间秒杀活动的盲盒数据
     * w
     *
     * @param startTimeline 秒杀活动开始时刻
     * @return 当时间秒杀活动的商品数据
     */
    private List<BoxSeckillBoxVO> wrapperSeckillBox(Integer startTimeline, String seckillId) {
        List<BoxSeckillBoxVO> seckillBoxVoS = new ArrayList<>();
        List<BoxSeckillApply> seckillApplyList = this.list(new LambdaQueryWrapper<BoxSeckillApply>().eq(BoxSeckillApply::getSeckillId, seckillId));
        if (!seckillApplyList.isEmpty()) {
            List<BoxSeckillApply> collect = seckillApplyList.stream().filter(i -> i.getTimeLine().equals(startTimeline) && i.getPromotionApplyStatus().equals(PromotionsApplyStatusEnum.PASS.name())).collect(Collectors.toList());
            for (BoxSeckillApply seckillApply : collect) {
                BlindBox blindBox = blindBoxService.getById(seckillApply.getBoxId());
                if (blindBox != null) {
                    BoxSeckillBoxVO boxSeckillBoxVO = new BoxSeckillBoxVO();
                    BeanUtil.copyProperties(seckillApply, boxSeckillBoxVO);
                    boxSeckillBoxVO.setImage(blindBox.getImage());
                    boxSeckillBoxVO.setBoxId(blindBox.getId());
                    boxSeckillBoxVO.setName(blindBox.getName());
                    seckillBoxVoS.add(boxSeckillBoxVO);
                }
            }
        }
        return seckillBoxVoS;
    }

    @Override
    public List<BoxSeckillBoxVO> getSeckillBox(Integer timeline) {
        List<BoxSeckillBoxVO> boxSeckillBoxVOs = new ArrayList<>();
        //获取
        List<BoxSeckillTimelineVO> seckillTimelineToCache =getBoxSeckillTimelineInfo();
        Optional<BoxSeckillTimelineVO> first = seckillTimelineToCache.stream().filter(i -> i.getTimeLine().equals(timeline)).findFirst();
        if (first.isPresent()) {
            boxSeckillBoxVOs = first.get().getSeckillBoxList();
        }
        return boxSeckillBoxVOs;
    }

    @Override
   public  Boolean deleteBySeckillId(String id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.in("seckill_id", id);
        this.remove(queryWrapper);
        return  true ;
    }
}