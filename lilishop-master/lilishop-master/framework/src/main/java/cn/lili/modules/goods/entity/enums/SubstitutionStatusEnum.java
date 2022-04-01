package cn.lili.modules.goods.entity.enums;
/**
 * 置换状态类型
 */
public enum SubstitutionStatusEnum {

    /**
     * "置换"
     */
    SUBSTITUTION("置换"),
    /**
     * "未置换"
     */
    UNSUBSTITUTION("未置换");

    private final String description;

    SubstitutionStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
