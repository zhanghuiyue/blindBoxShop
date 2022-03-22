package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.Banner;
import cn.lili.modules.blindBox.entity.vo.BannerVO;
import cn.lili.modules.blindBox.service.BannerService;
import cn.lili.modules.goods.entity.dos.Goods;
import cn.lili.modules.goods.entity.dto.GoodsSearchParams;
import cn.lili.modules.goods.entity.vos.GoodsVO;
import cn.lili.modules.goods.service.GoodsService;
import cn.lili.modules.goods.service.GoodsSkuService;
import cn.lili.modules.search.entity.dos.EsGoodsIndex;
import cn.lili.modules.search.entity.dos.EsGoodsRelatedInfo;
import cn.lili.modules.search.entity.dto.EsGoodsSearchDTO;
import cn.lili.modules.search.service.EsGoodsSearchService;
import cn.lili.modules.statistics.aop.PageViewPoint;
import cn.lili.modules.statistics.aop.enums.PageViewEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 买家端,盲盒接口
 *
 * @author lilei
 * @since 2022/03/22 10:06 下午
 */
@Slf4j
@Api(tags = "买家端,盲盒推荐管理")
@RestController
@RequestMapping("/buyer/blindBox/recommend")
public class BlindBoxRecommendController {

    /**
     * 推荐 banner
     */
    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "获取推荐banner列表")
    @GetMapping(value = "/banner/list")
    public ResultMessage<List<Banner>> getBannersList() {
        return ResultUtil.data(bannerService.getBannersList() );
    }


}
