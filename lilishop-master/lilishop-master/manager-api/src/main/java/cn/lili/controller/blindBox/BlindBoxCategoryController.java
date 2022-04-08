package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBox;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.dto.BlindBoxDTO;
import cn.lili.modules.blindBox.entity.dto.search.BoxCategorySearchParams;
import cn.lili.modules.blindBox.entity.dto.search.BoxSearchParams;
import cn.lili.modules.blindBox.entity.vo.BlindBoxVO;
import cn.lili.modules.blindBox.service.BlindBoxCategoryService;
import cn.lili.modules.blindBox.service.BlindBoxService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 盲盒相关的控制层
 */
@RestController
@Api(tags = "盲盒管理")
@RequestMapping("/manager/blind-box/category")
public class BlindBoxCategoryController {

    @Autowired
    private BlindBoxCategoryService blindBoxCategoryService;



    @ApiOperation(value = "分页获取盲盒分类列表")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<BlindBoxCategory>> getBoxByPage(BoxCategorySearchParams boxSearchParams) {
        return ResultUtil.data(blindBoxCategoryService.getBlindBoxCategoryByPage(boxSearchParams));
    }

    @ApiOperation(value = "获取盲盒分类列表")
    @GetMapping(value = "/list")
    public ResultMessage<List<BlindBoxCategory>> getBoxCategoryList() {
        return ResultUtil.data(blindBoxCategoryService.getBlindBoxCategoryList());
    }

    @ApiOperation(value = "添加盲盒分类")
    @PostMapping(value = "/add")
    public ResultMessage<BlindBoxCategoryDTO> add(@Valid BlindBoxCategoryDTO blindBoxCategoryDTO) {
        if(blindBoxCategoryService.addBlindBoxCategory(blindBoxCategoryDTO)){
            return ResultUtil.data(blindBoxCategoryDTO);
        }else {
            throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_SAVE_ERROR);
        }
    }

    @ApiOperation(value = "更新盲盒分类")
    @PutMapping(value = "/update/{id}")
    public ResultMessage update (@PathVariable String id,@Valid BlindBoxCategoryDTO blindBoxCategoryDTO) {
        blindBoxCategoryDTO.setId(id);
        if (blindBoxCategoryService.updateBlindBoxCategory(blindBoxCategoryDTO)) {
            return ResultUtil.data(blindBoxCategoryDTO);
        }
        throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_UPDATE_ERROR);
    }

    @ApiOperation(value = "删除盲盒分类")
    @DeleteMapping(value = "/delete/{id}")
    public ResultMessage delete (@PathVariable String id) {
        blindBoxCategoryService.deleteBlindBoxCategory(id);
        return ResultUtil.success();
    }

    @PutMapping(value = "/disable/{id}")
    public ResultMessage<Object> disable(@PathVariable String id, @RequestParam Boolean disable) {
        if (blindBoxCategoryService.blindBoxCategoryDisable(id, disable)) {
            return ResultUtil.success();
        }
        throw new ServiceException(ResultCode.BLIND_BOX_CATEGORY_DISABLE_ERROR);
    }

}