package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import cn.lili.modules.blindBox.entity.dto.search.PriceSearchParams;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 盲盒相关的控制层
 */
@RestController
@Api(tags = "盲盒价格管理")
@RequestMapping("/manager/blind-box/price")
public class BlindBoxPriceController {

    @Autowired
    private BlindBoxPriceService blindBoxPriceService;

    @ApiOperation(value = "分页获取盲盒列表")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<Price>> getBoxByPage(PriceSearchParams priceSearchParams) {
        return ResultUtil.data(blindBoxPriceService.queryPriceByPage(priceSearchParams));
    }

    @ApiOperation(value = "添加盲盒")
    @PostMapping(value = "/add")
    public ResultMessage<BlindBoxPriceDTO> add(@Valid BlindBoxPriceDTO blindBoxPriceDTO) {
        if(blindBoxPriceService.add(blindBoxPriceDTO)){
            return ResultUtil.data(blindBoxPriceDTO);
        }else {
            throw new ServiceException(ResultCode.BLIND_BOX_PRICE_SAVE_ERROR);
        }
    }

    @ApiOperation(value = "更新盲盒")
    @PutMapping(value = "/update/{id}")
    public ResultMessage update(@PathVariable String id,@Valid BlindBoxPriceDTO blindBoxPriceDTO) {
        blindBoxPriceDTO.setId(id);
        if (blindBoxPriceService.update(blindBoxPriceDTO)) {
            return ResultUtil.data(blindBoxPriceDTO);
        }
        throw new ServiceException(ResultCode.BLIND_BOX_PRICE_UPDATE_ERROR);
    }

    @ApiOperation(value = "删除盲盒价格")
    @DeleteMapping(value = "/delete/{id}")
    public ResultMessage delete(@PathVariable String id) {
        blindBoxPriceService.deleteById(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "批量删除盲盒价格")
    @PutMapping(value = "/batch/delete/{ids}")
    public ResultMessage batchDelete(@PathVariable List<String> ids) {
        blindBoxPriceService.batchDelete(ids);
        return ResultUtil.success();
    }

    @PutMapping(value = "/disable/{id}")
    public ResultMessage<Object> disable(@PathVariable String id, @RequestParam Boolean disable) {
        if (blindBoxPriceService.blindBoxPriceDisable(id, disable)) {
            return ResultUtil.success();
        }
        throw new ServiceException(ResultCode.BLIND_BOX_PRICE_DISABLE_ERROR);
    }
}
