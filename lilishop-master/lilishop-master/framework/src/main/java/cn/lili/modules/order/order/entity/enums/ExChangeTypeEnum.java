package cn.lili.modules.order.order.entity.enums;

/**
 * 购买类型
 *
 * @author Chopper
 * @since 2020-03-25 2:30 下午
 */
public enum ExChangeTypeEnum {


    /**
     * 立即购买
     */
    BUY_NOW;


    public String getPrefix() {
        return "{" + this.name() + "}_";
    }

}
