package cn.lili.modules.blindBox.enums;

/**
 * 促销活动申请状态枚举
 *
 * @author Chopper
 * @since 2020-03-19 9:36 上午
 */
public enum PromotionsBoxApplyStatusEnum {

    /**
     * 枚举
     */
    APPLY("申请"), PASS("通过"), REFUSE("拒绝");

    private final String description;

    PromotionsBoxApplyStatusEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
