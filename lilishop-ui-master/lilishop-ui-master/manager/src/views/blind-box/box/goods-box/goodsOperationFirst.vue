<template>
  <div>
    <!-- 选择商品类型 -->
    <Modal v-model="selectGoodsType" width="550" :closable="false">
      <div class="goods-type-list" >
        <div
          class="goods-type-item"
          :class="{ 'active-goods-type': item.check }"
          @click="handleClickGoodsType(item)"
          v-for="(item, index) in goodsTypeWay"
          :key="index"
        >
          <img :src="item.img" />
          <div>
            <h2>{{ item.title }}</h2>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>

    </Modal>
    <!-- 商品分类 -->
    <div class="content-goods-publish">

      <div class="goods-category">

        <ul v-if="blindBoxList.length > 0">
          <h2 style="margin-left:55px;">请选择盲盒种类</h2>
          <li
            v-for="(item, index) in blindBoxList"
            :class="{ activeClass: blindBox.id === item.id }"
            @click="handleSelectBlindBox(item)"
            :key="index"
          >
            <span>{{ item.name }}</span>
          </li>
        </ul>
        <ul v-if="brandList.length > 0">
          <h2 style="margin-left:55px;">请选择商品品牌</h2>
          <li
            v-for="(item, index) in brandList"
            :class="{ activeClass: brand.id === item.id }"
            @click="handleSelectBrand(item)"
            :key="index"
          >
            <span>{{ item.name }}</span>
          </li>
        </ul>

      </div>
      <p class="current-goods-category">
        您当前选择的盲盒类别是：
        <span>{{ blindBox.name }}</span>
      </p>
      <p class="current-goods-category">
        您当前选择的品牌是：
        <span>{{brand.name }}</span>
      </p>

    </div>
    <!-- 底部按钮 -->
    <div class="footer">
      <ButtonGroup>
        <Button type="primary" @click="selectGoodsType = true">商品类型</Button>
        <Button type="primary" @click="next">下一步</Button>
      </ButtonGroup>
    </div>
  </div>
</template>
<script>
import * as API_BOXS from "@/api/blindBox";
import * as API_GOODS from "@/api/goods";
export default {
  data() {
    return {
      selectedTemplate: {}, // 已选模板
      selectGoodsType: false, // 展示选择商品分类modal
      goodsTypeWay: [
        {
          title: "实物商品",
          img: require("@/assets/goodsType1.png"),
          desc: "零售批发，物流配送",
          type: "PHYSICAL_GOODS",
          check: false,
        },
        {
          title: "虚拟商品",
          img: require("@/assets/goodsType2.png"),
          desc: "虚拟核验，无需物流",
          type: "VIRTUAL_GOODS",
          check: false,
        },
        {
          title: "电子卡券",
          img: require("@/assets/goodsTypeTpl.png"),
          desc: "可消费，可提现，可赠送",
          type: "E_COUPON",
          check: false,
        },
      ],

      // 商品类型
      goodsType: "",

      //选中的盲盒
      blindBox:"",
      //盲盒列表
      blindBoxList:[],

      //选中的品牌
      brand:"",
      //品牌列表
      brandList:[],

    };
  },
  methods: {
    // 点击商品类型
    handleClickGoodsType(val) {
      this.goodsTypeWay.map((item) => {
        return (item.check = false);
      });

      val.check = !val.check;
      if (!val.type) {
        this.GET_GoodsTemplate();
        this.showGoodsTemplates = true;
      } else {
        this.goodsType = val.type;
        this.selectedTemplate = {};
      }
    },
    // 点击商品模板
    handleClickGoodsTemplate(val) {
      this.selectedTemplate = val;
      this.selectGoodsType = false;
      this.$emit("change", { tempId: val.id });
    },
    // 获取商品模板
    GET_GoodsTemplate() {
      let searchParams = {
        saveType: "TEMPLATE",
        sort: "create_time",
        order: "desc",
      };
      API_GOODS.getDraftGoodsListData(searchParams).then((res) => {
        if (res.success) {
          this.goodsTemplates = res.result.records;
        }
      });
    },
    /** 选择盲盒种类 */
    handleSelectBlindBox(row) {

      this.blindBox = row ;
    },

    handleSelectBrand(row) {

      this.brand = row ;

    },


    /** 查询盲盒列表*/
    GET_BlindBoxList() {

      API_BOXS.getBoxList().then((res) => {

        if (res.success && res.result) {
          this.blindBoxList = res.result.blindBoxDTOList;
        }
      });
    },
    /** 查询品牌列表*/
    GET_BrandBoxList(row) {

      API_GOODS.getBrandListData().then((res) => {
          this.brandList = res;

      });
    },



    // 下一步
    next() {
      window.scrollTo(0, 0);
      if (!this.goodsType) {
        this.$Message.error("请选择商品类型");
        return;
      }
      if (!this.blindBox.id) {
        this.$Message.error("请选择盲盒种类");
        return;
      } else if (!this.brand.name) {
        this.$Message.error("请选择商品品牌");
        return;
      } else {
          this.$emit("change", { brand: this.brand, goodsType: this.goodsType ,blindBox:this.blindBox });
      }
    },
  },
  mounted() {
    this.GET_BlindBoxList();
    this.GET_BrandBoxList();
  },
};
</script>
<style lang="scss" scoped>
@import "./addGoods.scss";
</style>
