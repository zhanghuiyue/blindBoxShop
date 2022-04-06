package cn.lili.modules.blindBox.enums;

/**
 * 状态枚举
 */
public enum StatusEnum {

    /**
     * 有效状态
     */
    VALID("有效状态"),
    /**
     * 无效状态
     */
    UNVALID("无效状态");

    private final String description;

    StatusEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
