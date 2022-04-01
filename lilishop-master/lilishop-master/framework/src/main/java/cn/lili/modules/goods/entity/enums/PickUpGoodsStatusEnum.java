package cn.lili.modules.goods.entity.enums;

/**
 * 提货状态类型
 */
public enum PickUpGoodsStatusEnum {

    /**
     * "提货"
     */
    PICKUPGOODS("提货"),
    /**
     * "未提货"
     */
    UNPICKUPGOODS("未提货");

    private final String description;

    PickUpGoodsStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
