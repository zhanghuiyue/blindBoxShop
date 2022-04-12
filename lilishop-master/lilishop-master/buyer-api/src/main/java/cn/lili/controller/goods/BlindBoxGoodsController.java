package cn.lili.controller.goods;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.vos.CategoryVO;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.goods.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Api(tags = "买家端,盲盒商品接口")
@RequestMapping("/buyer/blindbox/goods")
public class BlindBoxGoodsController {

    /**
     * 盲盒商品
     */
    @Autowired
    private BlindBoxGoodsService blindBoxGoodsService;

    @ApiOperation(value = "获取商品详情")
    @ApiImplicitParam(name = "goodsId", value = "商品编号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/detail")
    public ResultMessage<BlindBoxGoods> queryGoodsDetail(@NotNull(message = "商品编号不能为空") @RequestParam String goodsId) {
        return ResultUtil.data(blindBoxGoodsService.queryProductDetails(goodsId));
    }

    @ApiOperation(value = "获取商品列表")
    @ApiImplicitParam(name = "blindBoxId", value = "商品编号", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/list")
    public ResultMessage<List<BlindBoxGoods>> list(@NotNull(message = "盲盒编号不能为空") @RequestParam String blindBoxId) {
        return ResultUtil.data(blindBoxGoodsService.queryList(blindBoxId));
    }


}
