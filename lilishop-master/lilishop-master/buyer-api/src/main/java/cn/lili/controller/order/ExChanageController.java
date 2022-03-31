package cn.lili.controller.order;

import cn.lili.common.aop.annotation.PreventDuplicateSubmissions;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.vo.ExChangeParams;
import cn.lili.modules.order.order.service.ExChangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 买家端，购物车接口
 *
 * @author Chopper
 * @since 2020/11/16 10:04 下午
 */
@Slf4j
@RestController
@Api(tags = "买家端，商品兑换接口")
@RequestMapping("/buyer/order/exChanage")
public class ExChanageController {

    /**
     * 商品兑换
     */
    @Autowired
    private ExChangeService exChangeService;


    @ApiOperation(value = "向兑换池中添加一个产品")
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuId", value = "产品ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "此产品的购买数量", required = true, dataType = "int", paramType = "query")
    })
    public ResultMessage<Object> add(@NotNull(message = "产品id不能为空") String skuId,
                                     @NotNull(message = "购买数量不能为空") @Min(value = 1, message = "加入购物池数量必须大于0") Integer num) {
        try {
            //读取选中的列表
            exChangeService.add(skuId, num);
            return ResultUtil.success();
        } catch (ServiceException se) {
            log.info(se.getMsg(), se);
            throw se;
        } catch (Exception e) {
            log.error(ResultCode.EXCHANGE_ERROR.message(), e);
            throw new ServiceException(ResultCode.EXCHANGE_ERROR);
        }
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "创建交易")
    @PostMapping(value = "/create/tradeExchangeOrder", consumes = "application/json", produces = "application/json")
    public ResultMessage<Object> crateExChangeTrade(@RequestBody ExChangeParams exChangeParams) {
        try {
            //创建交易

            return ResultUtil.data(this.exChangeService.createExChangeTrade(exChangeParams));

        } catch (ServiceException se) {
            log.info(se.getMsg(), se);
            throw se;
        } catch (Exception e) {
            log.error(ResultCode.ORDER_ERROR.message(), e);
            throw e;
        }
    }



}
