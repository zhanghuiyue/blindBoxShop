package cn.lili.controller.blindBox;


import cn.lili.common.enums.ResultUtil;

import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dto.BoxGoodsOperationDTO;
import cn.lili.modules.blindBox.service.BoxGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * 管理端,盲盒商品管理接口
 *
 * @author pikachu
 * @since 2020-02-23 15:18:56
 */
@RestController
@Api(tags = "管理端,盲盒商品管理接口")
@RequestMapping("/manager/blind-box/goods")
public class BoxGoodsController {
    /**
     * 商品
     */
    @Autowired
    private BoxGoodsService boxGoodsService;



    @ApiOperation(value = "新增盲盒商品")
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResultMessage<BoxGoodsOperationDTO> save(@Valid @RequestBody BoxGoodsOperationDTO boxGoodsOperationDTO) {

        boxGoodsService.addBoxGoods(boxGoodsOperationDTO);
        return ResultUtil.success();
    }


}
