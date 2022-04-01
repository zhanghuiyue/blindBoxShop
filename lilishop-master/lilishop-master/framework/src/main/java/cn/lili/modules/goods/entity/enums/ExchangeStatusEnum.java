package cn.lili.modules.goods.entity.enums;

/**
 * 商品类型
 *
 * @author Bulbasaur
 * @since 2021/5/28 4:23 下午
 */
public enum ExchangeStatusEnum {

    /**
     * "未兑换"
     */
    UNEXCHANGE("未兑换"),
    /**
     * "兑换"
     */
    EXCHANGE("兑换");


    private final String description;

    ExchangeStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

}
