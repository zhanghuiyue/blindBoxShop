package cn.lili.controller.blindBox;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BoxSeckill;
import cn.lili.modules.blindBox.entity.dos.BoxSeckillApply;
import cn.lili.modules.blindBox.entity.dto.search.SeckillBoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillApplyVO;
import cn.lili.modules.blindBox.entity.vo.BoxSeckillVO;
import cn.lili.modules.blindBox.service.BoxSeckillApplyService;
import cn.lili.modules.blindBox.service.BoxSeckillService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 管理端,盲盒秒杀活动接口
 *
 * @author lilei
 * @since 2022/03/22
 **/
@RestController
@Api(tags = "管理端,盲盒秒杀活动接口")
@RequestMapping("/manager/blind-box/recommendBanner/seckill")
public class BoxSeckillManagerController {
    @Autowired
    private BoxSeckillService boxSeckillService;
    @Autowired
    private BoxSeckillApplyService boxSeckillApplyService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取秒杀活动列表")
    public ResultMessage<IPage<BoxSeckill>> getBoxSeckillPage(SeckillBoxSearchParams queryParam) {

        IPage<BoxSeckill> seckillPage = boxSeckillService.queryByParams(queryParam);
        return ResultUtil.data(seckillPage);
    }

    @PostMapping
    @ApiOperation(value = "添加秒杀活动")
    public ResultMessage<Object> add(@RequestBody BoxSeckill boxSeckill) {

        boxSeckillService.saveBoxSeckill(boxSeckill);
        return ResultUtil.data(boxSeckill);
    }

    @ApiOperation(value = "修改秒杀活动")
    @PutMapping()
    public ResultMessage<BoxSeckill> updateSeckill(@RequestBody BoxSeckill boxSeckill) {
        boxSeckillService.updateBoxSeckill(boxSeckill);
        return ResultUtil.data(boxSeckill);
    }

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/{id}")
    public ResultMessage<BoxSeckill> get(@PathVariable String id) {
        BoxSeckill boxSeckill = boxSeckillService.getById(id);
        return ResultUtil.data(boxSeckill);
    }

    @ApiOperation(value = "删除一个秒杀活动")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public ResultMessage<Object> deleteBoxSeckill(@PathVariable String id) {
        boxSeckillService.deleteBoxSeckill(Collections.singletonList(id));
        return ResultUtil.success();
    }

    @ApiOperation(value = "操作秒杀活动状态")
    @ApiImplicitParam(name = "id", value = "秒杀活动ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/status/{id}")
    public ResultMessage<Object> updateSeckillStatus(@PathVariable String id, Long startTime, Long endTime) {
        boxSeckillService.updateStatus(Collections.singletonList(id), startTime, endTime);
        return ResultUtil.success();
    }

    @GetMapping("/apply/{seckillApplyId}")
    @ApiOperation(value = "获取秒杀活动申请")
    public ResultMessage<BoxSeckillApply> getSeckillApply(@PathVariable String seckillApplyId) {

        BoxSeckillApply seckillApply = OperationalJudgment.judgment(boxSeckillApplyService.getById(seckillApplyId));
        return ResultUtil.data(seckillApply);
    }

    @PostMapping(path = "/apply/{boxSeckillId}", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加秒杀活动申请")
    public ResultMessage<String> addBoxSeckillApply(@PathVariable String boxSeckillId, @RequestBody List<BoxSeckillApplyVO> applyVos) {

        boxSeckillApplyService.addBoxSeckillApply(boxSeckillId, applyVos);
        return ResultUtil.success();
    }


    @GetMapping("/applyList/{seckillId}")
    @ApiOperation(value = "获取秒杀活动申请列表")
    public ResultMessage<IPage<BoxSeckillApply>> getSeckillApplyPage(@PathVariable String seckillId, PageVO pageVo) {

        SeckillBoxSearchParams   seckillBoxSearchParams = new SeckillBoxSearchParams();
        System.out.println("盲盒秒杀申请22:"+pageVo.toString());
        System.out.println("盲盒秒杀申请111:"+seckillBoxSearchParams.toString());
        seckillBoxSearchParams.setSeckill_id(seckillId);
        System.out.println("盲盒秒杀申请:"+seckillBoxSearchParams.toString());
        IPage<BoxSeckillApply> boxSeckillPage = boxSeckillApplyService.getBoxSeckillApplyPage(seckillBoxSearchParams, pageVo);
        return ResultUtil.data(boxSeckillPage);
    }


    @DeleteMapping("/apply/{id}")
    @ApiOperation(value = "删除秒杀活动商品")
    public ResultMessage<String> deleteSeckillApply( @PathVariable String id) {
        boxSeckillApplyService.removeById(id);
        return ResultUtil.success();

    }


}
