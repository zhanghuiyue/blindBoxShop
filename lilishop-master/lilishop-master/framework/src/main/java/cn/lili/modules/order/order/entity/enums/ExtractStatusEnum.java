package cn.lili.modules.order.order.entity.enums;

public enum ExtractStatusEnum {
   UNEXTRACT("0","未抽取"),
   EXTRACT("1","已抽取");
   /**
    * 抽取状态
    */
   private final String state;
   /**
    * 抽取状态描述
    */
   private final String desc;

   public String getState() {
      return state;
   }

   public String getDesc() {
      return desc;
   }

   ExtractStatusEnum(String state, String desc) {
      this.state = state;
      this.desc = desc;
   }
}
