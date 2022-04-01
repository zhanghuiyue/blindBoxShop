package cn.lili.modules.goods.entity.enums;

/**
 * 商品类型
 *
 * @author Bulbasaur
 * @since 2021/5/28 4:23 下午
 */
public enum GiveStatusEnum {

    /**
     * "未兑换"
     */
    GIVE("赠送"),
    /**
     * "取消赠送"
     */
    UNGIVE("取消赠送"),
    /**
     * "自动取消赠送"
     */
    AUTOUNGIVE("自动取消赠送");

    private final String description;

    GiveStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
