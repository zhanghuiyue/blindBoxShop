package cn.lili.controller.order;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.order.order.entity.dto.OrderSearchParams;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSearchParams;
import cn.lili.modules.order.order.entity.vo.SubstitutionOrderSimpleVO;
import cn.lili.modules.order.order.service.SubstitutionOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Api(tags = "置换商品订单接口")
@RequestMapping("/substitution/order")
public class SubstitutionOrderController {

    @Autowired
    private SubstitutionOrderService substitutionOrderService;

    @ApiOperation(value = "查询置换商品订单列表及详情")
    @PostMapping("/query")
    public ResultMessage<SubstitutionOrderSimpleVO> querySubstitutionOrder(@RequestBody SubstitutionOrderSearchParams substitutionOrderSearchParams) {
        return ResultUtil.data(substitutionOrderService.querySubstitutionOrder(substitutionOrderSearchParams));
    }
}
