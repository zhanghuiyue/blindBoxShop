package cn.lili.controller.order;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.OperationalJudgment;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dos.Order;
import cn.lili.modules.order.order.entity.dto.BoxOrderSearchParams;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.enums.OrderStatusEnum;
import cn.lili.modules.order.order.entity.vo.BoxOrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.OrderDetailVO;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.service.BlindBoxOrderService;
import cn.lili.modules.order.order.service.OrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 买家端,订单接口
 *
 * @author Chopper
 * @since 2020/11/16 10:08 下午
 */
@RestController
@Api(tags = "买家端,订单接口")
@RequestMapping("/buyer/blindOrder/order")
public class BlindBoxOrderController {

    /**
     * 盲盒订单
     */
    @Autowired
    private BlindBoxOrderService blindOrderService;

    @ApiOperation(value = "按条件查询会员订单列表")
    @GetMapping
    public List<BoxOrderSimpleVO> queryMineOrder(BoxOrderSearchParams boxOrderSearchParams) {

        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        boxOrderSearchParams.setMemberId(currentUser.getId());
        return blindOrderService.queryByParams(boxOrderSearchParams);
    }


    @ApiOperation(value = "订单明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, paramType = "path")
    })
    @GetMapping(value = "/{orderSn}")
    public ResultMessage<BoxOrderSimpleVO> detail(@NotNull(message = "订单编号不能为空") @PathVariable("orderSn") String orderSn) {
        BoxOrderSimpleVO boxOrderSimpleVO = blindOrderService.queryDetailBySn(orderSn);

        return ResultUtil.data(boxOrderSimpleVO);
    }


    @PreventDuplicateSubmissions
    @ApiOperation(value = "删除订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, dataType = "String", paramType = "path")
    })
    @DeleteMapping(value = "/{orderSn}")
    public ResultMessage<Object> deleteOrder(@PathVariable String orderSn) {
        OperationalJudgment.judgment(blindOrderService.getBySn(orderSn));
        blindOrderService.deleteOrderBySn(orderSn);
        return ResultUtil.success();
    }

}
