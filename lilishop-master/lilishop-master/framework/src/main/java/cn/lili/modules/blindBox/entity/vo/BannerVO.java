package cn.lili.modules.blindBox.entity.vo;

import cn.lili.modules.blindBox.entity.dos.Banner;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;

/**
 * 品牌VO
 *
 * @author lilei
 * @since 2022-03-21 15:24:13
 */
@Data
public class BannerVO extends Banner {

    private static final long serialVersionUID = 3829199991161122317L;


        public BannerVO(Banner banner) {
        BeanUtil.copyProperties(banner, this);
    }
}
