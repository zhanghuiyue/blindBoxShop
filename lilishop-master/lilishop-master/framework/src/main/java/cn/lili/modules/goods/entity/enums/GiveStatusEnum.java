package cn.lili.modules.goods.entity.enums;

/**
 * 赠送类型
 *
 * @author Bulbasaur
 * @since 2021/5/28 4:23 下午
 */
public enum GiveStatusEnum {

    /**
     * "赠送，未领取"
     */
    GIVE("赠送"),
    /**
     * "未赠送"
     */
    UNGIVE("未赠送"),
    /**
     * "取消赠送"
     */
    CANCELGIVE("取消赠送"),
    /**
     * "自动取消赠送"
     */
    AUTOUNGIVE("自动取消赠送"),
    /**
     * "已赠送，已领取"
     */
    GIVEED("已赠送");

    private final String description;

    GiveStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
