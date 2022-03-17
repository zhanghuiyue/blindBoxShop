package cn.lili.controller.blindBox;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.blindBox.entity.dos.BlindBoxCategory;
import cn.lili.modules.blindBox.entity.dto.BlindBoxCategoryDTO;
import cn.lili.modules.blindBox.entity.vo.BlindBoxCategoryVO;
import cn.lili.modules.blindBox.entity.vo.BlindBoxPriceVO;
import cn.lili.modules.blindBox.service.BlindBoxPriceService;
import cn.lili.modules.blindBox.service.BlindBoxService;
import cn.lili.modules.distribution.entity.dos.DistributionOrder;
import cn.lili.modules.distribution.entity.vos.DistributionOrderSearchParams;
import cn.lili.modules.distribution.service.DistributionService;
import cn.lili.modules.promotion.entity.vos.CouponVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 盲盒相关的控制层
 */
@RestController
@Api(tags = "盲盒查询接口")
@RequestMapping("/blindBox")
public class BlindBoxController {

    @Autowired
    private BlindBoxService blindBoxService;
    @Autowired
    private BlindBoxPriceService blindBoxPriceService;

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

    @ApiOperation(value = "盲盒价格列表查询")
    @GetMapping("/price/{categoryId}")
    public ResultMessage<BlindBoxPriceVO> queryBlindBoxPriceList(@NotNull(message = "盲盒类型不能为空") @PathVariable String categoryId) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        return ResultUtil.data(blindBoxPriceService.queryPriceByCategory(currentUser.getId(),categoryId));
    }
}
