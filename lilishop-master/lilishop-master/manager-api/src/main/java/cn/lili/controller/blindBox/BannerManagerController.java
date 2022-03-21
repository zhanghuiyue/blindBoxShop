package cn.lili.controller.blindBox;


import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.dto.BannerPageDTO;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import cn.lili.modules.blindBox.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 管理端,品牌接口
 *
 * @author lilei
 * @since 2022-03-21 15:18:56
 */
@RestController
@Api(tags = "盲盒端,推荐banner接口")
@RequestMapping("/manager/blind-box/recommendBanner")
public class BannerManagerController {

    /**
     * 推荐banner
     */
    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "品牌ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<Banner> get(@NotNull @PathVariable String id) {
        return ResultUtil.data(bannerService.getById(id));
    }

    @GetMapping(value = "/all")
    @ApiOperation(value = "获取所有可用推荐banner")
    public List<Banner> getAll() {
        List<Banner> list = bannerService.list(new QueryWrapper<Banner>().eq("delete_flag", 0));
        return list;
    }

    @ApiOperation(value = "分页获取")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<Banner>> getByPage(BannerPageDTO page) {
        return ResultUtil.data(bannerService.getBannersByPage(page));
    }

    @ApiOperation(value = "新增推荐banner")
    @PostMapping
    public ResultMessage<BannerVO> save(@Valid BannerVO banner) {
        if (bannerService.addBanner(banner)) {
            return ResultUtil.data(banner);
        }
        throw new ServiceException(ResultCode.BANNER_SAVE_ERROR);
    }

    @ApiOperation(value = "更新数据")
    @ApiImplicitParam(name = "id", value = "品牌ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/{id}")
    public ResultMessage<BannerVO> update(@PathVariable String id, @Valid BannerVO banner) {
        banner.setId(id);
        if (bannerService.updateBanner(banner)) {
            return ResultUtil.data(banner);
        }
        throw new ServiceException(ResultCode. BANNER_UPDATE_ERROR);
    }

    @ApiOperation(value = "后台禁用品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brandId", value = "品牌ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "disable", value = "是否可用", required = true, dataType = "boolean", paramType = "query")
    })
    @PutMapping(value = "/disable/{brandId}")
    public ResultMessage<Object> disable(@PathVariable String brandId, @RequestParam Boolean disable) {
        if (bannerService.brandDisable(brandId, disable)) {
            return ResultUtil.success();
        }
        throw new ServiceException(ResultCode.BANNER_DISABLE_ERROR);
    }

    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "ids", value = "品牌ID", required = true, dataType = "String", allowMultiple = true, paramType = "path")
    @DeleteMapping(value = "/delByIds/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        bannerService.deleteBanners(ids);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


}
