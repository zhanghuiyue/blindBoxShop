package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dos.Price;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxPriceDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultMessage<IPage<Price>> getBoxByPage(PageVO pageVO) {
        return ResultUtil.data(blindBoxPriceService.queryPriceByPage(pageVO));
    }

    @ApiOperation(value = "添加盲盒")
    @PostMapping(value = "/add")
    public ResultMessage add(@RequestBody List<BlindBoxPriceDTO> blindBoxPriceDTO) {
        blindBoxPriceService.batchAdd(blindBoxPriceDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "更新盲盒")
    @PutMapping(value = "/update")
    public ResultMessage update(@RequestBody BlindBoxPriceDTO blindBoxPriceDTO) {
        blindBoxPriceService.update(blindBoxPriceDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "删除盲盒价格")
    @PutMapping(value = "/delete/{id}")
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
}
