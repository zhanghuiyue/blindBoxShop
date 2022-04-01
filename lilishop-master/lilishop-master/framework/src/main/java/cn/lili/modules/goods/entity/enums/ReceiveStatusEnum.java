package cn.lili.modules.goods.entity.enums;

/**
 * 领取状态
 */
public enum  ReceiveStatusEnum {
    /**
     * "领取"
     */
    RECEIVE("领取"),
    /**
     * "未领取"
     */
    UNRECEIVE("未领取");

    private final String description;

    ReceiveStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
