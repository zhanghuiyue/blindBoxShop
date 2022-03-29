package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.*;
import cn.lili.modules.blindBox.service.BlindBoxService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 盲盒相关的控制层
 */
@RestController
@Api(tags = "盲盒管理")
@RequestMapping("/manager/blind-box/")
public class BlindBoxController {

    @Autowired
    private BlindBoxService blindBoxService;


   @ApiOperation(value = "盲盒列表查询")
    @GetMapping("/list")
    public ResultMessage<BlindBoxCategoryVO> queryBlindBoxCategoryList() {
        List<BlindBoxCategory>  blindBoxCategoryList =blindBoxService.queryBlindBoxCategoryList();
        BlindBoxCategoryVO blindBoxCategoryVO = new BlindBoxCategoryVO();
        List<BlindBoxCategoryDTO> blindBoxCategoryDTOList = new ArrayList<>();
        for (BlindBoxCategory blindBoxCategory:blindBoxCategoryList){
            BlindBoxCategoryDTO blindBoxCategoryDTO = new BlindBoxCategoryDTO();
            BeanUtil.copyProperties(blindBoxCategory, blindBoxCategoryDTO);
            blindBoxCategoryDTOList.add(blindBoxCategoryDTO);
        }
        blindBoxCategoryVO.blindBoxCategoryDTOList=blindBoxCategoryDTOList;
        return ResultUtil.data(blindBoxCategoryVO);
    }


    @ApiOperation(value = "分页获取盲盒列表")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<BlindBoxCategory>> getBoxByPage(BoxSearchParams boxSearchParams) {
        return ResultUtil.data(blindBoxService.getBlindBoxCategoryByPage(boxSearchParams));
    }

    @ApiOperation(value = "添加盲盒")
    @PostMapping(value = "/add")
    public ResultMessage add(@RequestBody BlindBoxCategoryDTO blindBoxCategoryDTO) {
        blindBoxService.addBlindBox(blindBoxCategoryDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "更新盲盒")
    @PutMapping(value = "/update")
    public ResultMessage update (@RequestBody BlindBoxCategoryDTO blindBoxCategoryDTO) {
        blindBoxService.updateBlindBox(blindBoxCategoryDTO);
        return ResultUtil.success();
    }

    @ApiOperation(value = "删除盲盒")
    @PutMapping(value = "/delete/{id}")
    public ResultMessage delete (@PathVariable String id) {
        blindBoxService.deleteBlindBox(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "批量删除盲盒")
    @PutMapping(value = "/batch/delete/{ids}")
    public ResultMessage batchDelete (@PathVariable List<String> ids) {
        blindBoxService.batchDeleteBlindBox(ids);
        return ResultUtil.success();
    }
}