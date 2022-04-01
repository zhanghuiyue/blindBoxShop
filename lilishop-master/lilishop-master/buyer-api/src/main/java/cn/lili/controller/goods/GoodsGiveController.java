package cn.lili.controller.goods;

import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.goods.entity.dos.BlindBoxGoods;
import cn.lili.modules.goods.entity.dto.GiveGoodsDTO;
import cn.lili.modules.goods.entity.vos.GiveGoodsVO;
import cn.lili.modules.goods.service.BlindBoxGoodsService;
import cn.lili.modules.goods.service.GoodsGiveService;
import cn.lili.modules.system.entity.dos.Setting;
import cn.lili.modules.system.entity.dto.BindBoxOrderSetting;
import cn.lili.modules.system.entity.dto.GoodsGiveSetting;
import cn.lili.modules.system.entity.enums.SettingEnum;
import cn.lili.modules.system.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "买家端,赠送商品接口")
@RequestMapping("/buyer/goods/give")
public class GoodsGiveController {

    @Autowired
    private GoodsGiveService goodsGiveService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "赠送商品取消时间查询")
    @GetMapping(value = "/cancel/time")
    public ResultMessage<Object> queryCancelTime() {
        Map map = new HashMap();
        Setting setting = settingService.get(SettingEnum.GIVE_SETTING.name());
        GoodsGiveSetting goodsGiveSetting = JSONUtil.toBean(setting.getSettingValue(), GoodsGiveSetting.class);
        if (goodsGiveSetting != null && goodsGiveSetting.getAutoCancel() != null) {
            map.put("autoCancel",goodsGiveSetting.getAutoCancel());
        }else {
            map.put("autoCancel",24);
        }
        return ResultUtil.data(map);
    }

    @ApiOperation(value = "赠送")
    @GetMapping(value = "/give")
    public ResultMessage<Object> give(GiveGoodsDTO giveGoodsDTO) {
        String id = goodsGiveService.give(giveGoodsDTO);
        Map map = new HashMap();
        map.put("id",id);
        return ResultUtil.data(map);
    }

    @ApiOperation(value = "赠送商品查询")
    @GetMapping(value = "/give/goods/query")
    public ResultMessage<GiveGoodsVO> giveGoodsQuery(@RequestParam String  id) {
        return ResultUtil.data(goodsGiveService.giveGoodsQuery(id));
    }

    @ApiOperation(value = "赠送商品兑换")
    @GetMapping(value = "/give/goods/exchange")
    public ResultMessage<Object> giveGoodsExchange(@RequestParam String  giveCode) {
        goodsGiveService.giveGoodsExchange(giveCode);
        return ResultUtil.success();
    }
}
