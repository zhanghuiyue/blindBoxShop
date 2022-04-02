package cn.lili.modules.blindBox.enums;

public enum BlindBoxTypeEnum {

    /**
     * 免费盲盒
     */
    FREE("免费盲盒"),
    /**
     * 收费盲盒
     */
    CHARGE("收费盲盒");

    private final String description;

    BlindBoxTypeEnum(String str) {
        this.description = str;
    }

    public String description() {
        return description;
    }
}
