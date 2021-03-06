package cn.lili.controller.blindBox;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.blindBox.service.BlindBoxCategoryService;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.blindBox.service.BlindBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 盲盒相关的控制层
 */
@RestController
@Api(tags = "盲盒查询接口")
@RequestMapping("/buyer/blindBox")
public class BlindBoxController {

    @Autowired
    private BlindBoxService blindBoxService;
    @Autowired
    private BlindBoxPriceService blindBoxPriceService;
    @Autowired
    private BlindBoxCategoryService blindBoxCategoryService;
    @ApiOperation(value = "盲盒列表查询")
    @GetMapping("/list")
    public ResultMessage<BlindBoxVO> queryBlindBoxCategoryList() {
        List<BlindBox> blindBoxList =blindBoxService.queryBlindBoxCategoryList();
        BlindBoxVO blindBoxVO = new BlindBoxVO();
        List<BlindBoxDTO> blindBoxDTOList = new ArrayList<>();
        for (BlindBox blindBox : blindBoxList){
            BlindBoxDTO blindBoxDTO = new BlindBoxDTO();
            BeanUtil.copyProperties(blindBox, blindBoxDTO);
            blindBoxDTOList.add(blindBoxDTO);
        }
        blindBoxVO.blindBoxDTOList = blindBoxDTOList;
        return ResultUtil.data(blindBoxVO);
    }

    @ApiOperation(value = "盲盒价格列表查询")
    @GetMapping("/price/{categoryId}")
    public ResultMessage<BlindBoxPriceVO> queryBlindBoxPriceList(@NotNull(message = "盲盒类型不能为空") @PathVariable String categoryId) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        return ResultUtil.data(blindBoxPriceService.queryPriceByCategory(currentUser.getId(),categoryId));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "创建订单")
    @PostMapping(value = "/create/order", consumes = "application/json", produces = "application/json")
    public ResultMessage<Object> createOrder(@RequestBody OrderParam orderParam) {
        return ResultUtil.data(blindBoxService.createOrder(orderParam));
    }

    @ApiOperation(value = "盲盒抽取")
    @PostMapping(value = "/extract", consumes = "application/json", produces = "application/json")
    public ResultMessage<BlindBoxGoodsVO> blindBoxExtract(@RequestBody ExtractParam extractParam) {
        return ResultUtil.data(blindBoxService.blindBoxExtract(extractParam));
    }


    @ApiOperation(value = "搜索页盲盒列表查询")
    @PostMapping(value = "/search/list", consumes = "application/json", produces = "application/json")
    public ResultMessage<BlindBoxVO> queryBlindBoxList(@RequestBody BlindBoxCategorySearchParam blindBoxCategorySearchParam) {
        return ResultUtil.data(blindBoxService.queryBlindBoxList(blindBoxCategorySearchParam));
    }

    @ApiOperation(value = "搜索页盲盒类型列表查询")
    @GetMapping("/category/list")
    public ResultMessage<List<BlindBoxCategory>> queryBlindBoxCategory() {
        return ResultUtil.data(blindBoxCategoryService.getBlindBoxCategoryList());

    }

}