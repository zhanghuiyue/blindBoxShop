package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.entity.dto.TribePageDTO;
import cn.lili.modules.blindBox.service.TribeService;
import cn.lili.modules.goods.service.GoodsGiveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 盲盒部落相关的控制层
 */
@RestController
@Api(tags = "盲盒部落相关接口")
@RequestMapping("/buyer/blindBox/tribe")
public class BlindBoxTribeController {

    @Autowired
    private TribeService tribeService;

    @Autowired
    private GoodsGiveService goodsGiveService;

    @ApiOperation(value = "盲盒部落列表查询")
    @PostMapping("/list")
    public ResultMessage<IPage<Tribe>> queryBlindBoxTribeList(@RequestBody TribePageDTO pageDTO) {
        IPage<Tribe> tribeIPage = tribeService.getTribesByPage(pageDTO);
        return ResultUtil.data(tribeIPage);
    }

}