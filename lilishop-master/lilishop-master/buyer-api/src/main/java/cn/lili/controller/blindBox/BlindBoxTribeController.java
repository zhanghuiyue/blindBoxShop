package cn.lili.controller.blindBox;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dos.Tribe;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.TribePageDTO;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.blindBox.service.BlindBoxService;
import cn.lili.modules.blindBox.service.TribeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 盲盒部落相关的控制层
 */
@RestController
@Api(tags = "盲盒部落相关接口")
@RequestMapping("/buyer/blindBox/tribe")
public class BlindBoxTribeController {

    @Autowired
    private TribeService tribeService;

    @ApiOperation(value = "盲盒部落列表查询")
    @PostMapping("/list")
    public ResultMessage<IPage<Tribe>> queryBlindBoxTribeList(@RequestBody TribePageDTO pageDTO) {
        IPage<Tribe> tribeIPage = tribeService.getTribesByPage(pageDTO);
        return ResultUtil.data(tribeIPage);
    }



}