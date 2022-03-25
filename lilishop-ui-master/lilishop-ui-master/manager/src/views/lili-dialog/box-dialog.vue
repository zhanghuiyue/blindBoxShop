<template>
  <div class="wrapper">
    <div class="wap-content">
      <div class="query-wrapper">
        <div class="query-item">
          <div>搜索范围</div>
          <Input placeholder="商品名称" @on-clear="boxData=[]; boxParams.name=''; boxParams.pageNumber = 1; getQueryBoxList()" @on-enter="()=>{boxData=[];boxParams.pageNumber =1; getQueryBoxList();}" icon="ios-search" clearable
            style="width: 150px" v-model="boxParams.name" />
        </div>

        <div class="query-item">
          <Button type="primary" @click="boxData=[]; getQueryBoxList();" icon="ios-search">搜索</Button>
        </div>
      </div>
      <div style="positon:retavle;">
        <Scroll class="wap-content-list" :on-reach-bottom="handleReachBottom" :distance-to-edge="[3,3]">

          <div class="wap-content-item" :class="{ active: item.selected }" @click="checkedBox(item, index)" v-for="(item, index) in boxData" :key="index">
            <div>
              <img :src="item.image" alt="" />
            </div>
            <div class="wap-content-desc">
              <div class="wap-content-desc-title">{{ item.name }}</div>
              <div class="wap-sku">个</div>
              <div class="wap-content-desc-bottom">
                <div>￥{{ item.price | unitPrice }}</div>
              </div>
            </div>
          </div>
          <Spin size="large" fix v-if="loading"></Spin>

          <div v-if="empty" class="empty">暂无商品信息</div>
        </Scroll>

      </div>
    </div>
  </div>
</template>
<script>
import * as API_Boxs from "@/api/blindBox";
export default {
  data() {
    return {
      type: "multiple", //单选或者多选 single  multiple
      total: 0,  // 商品总数
      boxParams: { // 商品请求参数
        pageNumber: 1,
        pageSize: 16,
        order: "desc",
        name: "",
        authFlag: "PASS",
      },
      boxData: [], // 盲盒数据
      empty: false, // 空数据
      loading: false, // 加载状态
    };
  },
  props: {
    selectedWay: {
      type: Array,
      default: function(){
        return new Array()
      }
    }
  },
  watch: {

    selectedWay: {
      handler() {
        this.$emit("selected", this.selectedWay);
      },
      deep: true,
      immediate: true
    },

  },
  mounted() {
    console.log("进来的值:"+JSON.stringify(this.selectedWay));
    this.init();
  },
  methods: {
    // 触底加载更多方法
    handleReachBottom() {
      setTimeout(() => {
        if (
          this.boxParams.pageNumber * this.boxParams.pageSize <=
          this.total
        ) {
          this.boxParams.pageNumber++;
          this.getQueryBoxList();
        }
      }, 1500);
    },
    // 获取商品列表
    getQueryBoxList() {
      API_Boxs.getBoxListData(this.boxParams).then((res) => {
        this.initBox(res);
      });
    },
    // 获取列表方法
    initBox(res) {

      if (res.result.records.length !=0) {
        res.result.records.forEach((item) => {

          item.selected = false;
          item.___type = "box"; //设置为goods让pc wap知道标识
          this.selectedWay.forEach(e => {
            if (e.boxId && e.boxId === item.id) {
              item.selected = true
            }
          })
        });
        /**
         * 解决数据请求中，滚动栏会一直上下跳动
         */
        this.total = res.result.total;
        this.boxData.push(...res.result.records);

      } else {
        this.empty = true;
      }
    },

    // 查询盲盒
    init() {
      API_Boxs.getBoxListData(this.boxParams).then((res) => {
        // 盲盒
        this.initBox(res);
      });

    },

    /**
     * 点击商品
     */
    checkedBox(val, index) {

      // 如果单选的话
      if (this.type != "multiple") {

        this.boxData.forEach((item) => {
          item.selected = false;
        });
        this.selectedWay = [];
        val.selected = true;
        this.selectedWay.push(val);

        return false;
      }

      if (val.selected == false) {
        val.selected = true;
        this.selectedWay.push(val);
      } else {
        val.selected = false;
        for (let i = 0; i<this.selectedWay.length; i++ ) {
          if (this.selectedWay[i].id===val.id) {
            this.selectedWay.splice(i,1)
            break;
          }
        }
      }
    },
  },
};
</script>
<style scoped lang="scss">
@import "./style.scss";
.wap-content {
  width: 100%;
}
.empty {
  text-align: center;
  padding: 8px 0;
  width: 100%;
}
.wap-content {
  flex: 1;
  padding: 0;
}
.wap-content-list {
  position: relative;
}
.wap-content-item {
  width: 210px;
  margin: 10px 7px;
  padding: 6px 0;
}
// .wap-content-item{

// }
.active {
  background: url("../../assets/selected.png") no-repeat;
  background-position: right;
  background-size: 10%;
}
</style>
