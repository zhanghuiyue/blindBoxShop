package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 买家端,盲盒接口
 *
 * @author lilei
 * @since 2022/03/22 10:06 下午
 */
@Slf4j
@Api(tags = "买家端,盲盒推荐管理")
@RestController
@RequestMapping("/buyer/blindBox/recommend")
public class BlindBoxRecommendController {

    /**
     * 推荐 banner
     */
    @Autowired
    private BannerService bannerService;

    /**
     * 盲盒限时抢购
     *@return
     */
  /*  @Autowired
    private BoxSeckillService boxSeckillService ;*/


    @ApiOperation(value = "获取推荐banner列表")
    @GetMapping(value = "/banner/list")
    public ResultMessage<List<Banner>> getBannersList() {
        return ResultUtil.data(bannerService.getBannersList() );
    }


   /* @ApiOperation(value = "获取当天盲盒秒杀信息")
    @GetMapping
    public ResultMessage<List<BoxSeckillTimelineVO>> getBoxSeckillTime() {
        return ResultUtil.data(boxSeckillService.getBoxSeckillTimeline());
    }*/

  /*  @ApiOperation(value = "获取某个时刻的限时抢购商品信息")
    @GetMapping("/seckill/{timeline}")
    public ResultMessage<List<BoxSeckillVO>> getSeckillBox(@PathVariable Integer timeline) {
        return ResultUtil.data(boxSeckillService.getSeckillBoxList(timeline));
    }

    @ApiOperation(value = "获取当前抢购的盲盒信息")
    @GetMapping("/seckill/cuurentSeckill")
    public ResultMessage<List<BoxSeckillVO>> getCuurentSeckillBox(@PathVariable Integer timeline) {
        return ResultUtil.data(boxSeckillService.getSeckillBoxList(timeline));
    }
*/
    /**
     *
     * 当天盲盒抢购时间列表
     *
     */
  /*  @ApiOperation(value = "当天盲盒抢购时间列表")
    @GetMapping("/seckill/timeInterval")
    public ResultMessage<List<BoxSeckillVO>> getCuurentSeckillBox(@PathVariable Integer timeline) {
        return ResultUtil.data(boxSeckillService.getSeckillBoxList(timeline));
    }*/

}
