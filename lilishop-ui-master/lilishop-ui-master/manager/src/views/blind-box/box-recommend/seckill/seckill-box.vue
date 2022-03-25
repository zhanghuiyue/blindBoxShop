<template>
  <div class="seckill-goods">
    <Card>
      <Table border :columns="columns" :data="data">
        <template slot-scope="{ row }" slot="endTime">
          {{ unixDate(row.endTime) }}
        </template>
        <template slot-scope="{ row }" slot="hours">
          <Tag v-for="item in unixHours(row.hours)" :key="item">{{ item }}</Tag>
        </template>
      </Table>

      <Row class="operation">
        <template v-if="promotionStatus == 'NEW'">
          <Button type="primary" @click="openBoxListDilog">选择盲盒</Button>
          <!-- <Button @click="delAll">批量删除</Button> -->
        </template>
      </Row>
      <Row class="operation">
        <Tabs type="card" v-model="tabCurrent">
          <TabPane
            v-for="(tab, tabIndex) in boxList"
            :key="tabIndex"
            :label="tab.hour"
            :name="tabIndex + ''"
          >
            <Table
              :loading="loading"
              border
              :columns="boxColumns"
              v-if="tabIndex == tabCurrent"
              :data="tab.list"
              :ref="'table' + tabIndex"
              @on-selection-change="changeSelect"
            >
              <template slot-scope="{ row }" slot="originalPrice">
                <div>{{ row.originalPrice | unitPrice("￥") }}</div>
              </template>

              <template slot-scope="{ row, index }" slot="quantity">
                <InputNumber
                  :min="0"
                  v-model="row.quantity"
                  :disabled="row.promotionApplyStatus == 'PASS'"
                  @input="boxList[tabIndex].list[index].quantity = row.quantity"
                />
              </template>

              <template slot-scope="{ row, index }" slot="price">
                <InputNumber
                  :min="0"
                  style="width: 100%"
                  v-model="row.price"
                  :disabled="row.promotionApplyStatus == 'PASS'"
                  @input="boxList[tabIndex].list[index].price = row.price"
                />
              </template>

              <template slot-scope="{ row }" slot="promotionApplyStatus">
                <Badge
                  status="success"
                  v-if="row.promotionApplyStatus == 'PASS'"
                  :text="promotionApplyStatus(row.promotionApplyStatus)"
                />
                <Badge
                  status="blue"
                  v-if="row.promotionApplyStatus == 'APPLY'"
                  :text="promotionApplyStatus(row.promotionApplyStatus)"
                />
                <Badge
                  status="error"
                  v-if="row.promotionApplyStatus == 'REFUSE'"
                  :text="promotionApplyStatus(row.promotionApplyStatus)"
                />
                <span
                  v-if="row.promotionApplyStatus == 'REFUSE'"
                  @click="showReason(row.failReason)"
                  class="reason"
                  >（拒绝原因）</span
                >
                <Badge
                  status="error"
                  v-if="row.promotionApplyStatus == ''"
                  :text="promotionApplyStatus(row.promotionApplyStatus)"
                />
              </template>

              <template slot-scope="{ row }" slot="QRCode">
                <img
                  v-if="row.QRCode"
                  :src="row.QRCode || '../../../assets/lili.png'"
                  width="50px"
                  height="50px"
                  alt=""
                />
              </template>
              <template slot-scope="{ row, index }" slot="action">
                <Button
                  type="error"
                  v-if="promotionStatus === 'NEW'"
                  size="small"
                  ghost
                  @click="delGoods(index, row)"
                  >删除
                </Button>
              </template>
            </Table>
          </TabPane>
        </Tabs>
      </Row>

      <Row class="operation">
        <Button @click="closeCurrentPage">返回</Button>
        <Button
          type="primary"
          :loading="submitLoading"
          v-if="promotionStatus === 'NEW'"
          @click="save"
          >提交
        </Button>
      </Row>
    </Card>

    <box-select ref="boxSelect" @selectedBoxData="selectedBoxData"></box-select>
  </div>
</template>
<script>
import {
  seckillBoxDetail,
  seckillBoxList,
  setSeckillBox,
  delSeckillBox,
} from "@/api/blindBoxSeckill.js";
import boxSelect from "@/views/lili-dialog";

export default {
  components: {
    boxSelect,
  },
  watch: {
    tabCurrent(val) {
      this.tabIndex = val;
    },
  },
  data() {
    return {
      tabCurrent: 0,
      promotionStatus: "", // 活动状态
      loading: false, // 表单加载状态
      searchForm: {
        // 搜索框初始化对象
        pageNumber: 0, // 当前页数
        pageSize: 1000, // 页面大小
      },
      tabIndex: 0, // 选择商品的下标
      submitLoading: false, // 添加或编辑提交状态
      selectList: [], // 多选数据
      selectCount: 0, // 多选计数
      data: [{}], // 表单数据
      columns: [
        {
          title: "活动名称",
          key: "promotionName",
          minWidth: 120,
        },
        {
          title: "活动开始时间",
          key: "startTime",
        },
        {
          title: "活动结束时间",
          slot: "endTime",
        },
        {
          title: "时间场次",
          slot: "hours",
        },
      ],
      boxColumns: [

        {
          title: "商品名称",
          key: "name",
          minWidth: 120,
        },
        {
          title: "商品原始价格",
          slot: "originalPrice",
          minWidth: 50,
        },
        {
          title: "库存",
          slot: "quantity",
          minWidth: 40,
        },
        {
          title: "活动价格",
          slot: "price",
          minWidth: 50,
        },
        {
          title: "状态",
          slot: "promotionApplyStatus",
          minWidth: 30,
        },
        {
          title: "操作",
          slot: "action",
          minWidth: 50,
        },
      ],
      boxList: [], // 盲盒列表
      defaultBoxList: [], //默认查询秒杀的盲盒
    };
  },
  methods: {
    // 关闭当前页面
    closeCurrentPage() {
      this.$store.commit("removeTag", "box-seckill-goods");
      localStorage.storeOpenedList = JSON.stringify(
        this.$store.state.app.storeOpenedList
      );
      this.$router.go(-1);
    },
    // 提交秒杀商品
    save() {

      let list = JSON.parse(JSON.stringify(this.boxList));
      let params = {
        seckillId: this.$route.query.id,
        applyVos: [],
      };

      list.forEach((e, index) => {
        e.list.forEach((i) => {
          // if(e.id) delete e.id
          params.applyVos.push(i);
        });
      });
      this.submitLoading = true;

      console.log(list);

      setSeckillBox(params).then((res) => {
        console.log("提交盲盒："+JSON.stringify(params));
        this.submitLoading = false;
        if (res && res.success) {
          this.$Message.success("提交活动盲盒成功");
          this.closeCurrentPage();
        }
      });
    },
    // 初始化数据
    init() {
      this.getSeckillMsg();
    },
    // 清除选中状态
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    // 获取选择数据
    changeSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },

    getDataList() {
      // 获取商品详情
      this.loading = true;
      this.searchForm.seckillId = this.$route.query.id;
      // 处理过的时间 为‘1:00’
      let hours = this.unixHours(this.data[0].hours);
      hours.forEach((e) => {
        this.boxList.push({
          hour: e,
          list: [],
        });
      });
      seckillBoxList(this.searchForm).then((res) => {
        console.log("盲盒列表："+JSON.stringify(res));
        this.loading = false;
        if (res.success && res.result) {
          let data = res.result.records;
          // 未处理时间 为'1'
          let noFilterhours = this.data[0].hours.split(",");
          if (data.length) {
            noFilterhours.forEach((e, index) => {
              data.forEach((i) => {
                if (i.timeLine == e) {
                  this.boxList[index].list.push(i);
                }
              });
            });

            this.defaultBoxList = this.boxList[this.tabIndex].list;
          }
        }
      });
    },

    getSeckillMsg() {
      // 获取活动详情
      seckillBoxDetail(this.$route.query.id).then((res) => {


        if (res.success && res.result) {
          this.data = [];
          this.data.push(res.result);
          this.promotionStatus = res.result.promotionStatus;
          this.getDataList();
        }
      });
    },
    delGoods(index, row) {

      console.log("删除参数："+JSON.stringify(row));
      // 删除商品
      if (row.promotionApplyStatus === "PASS") {
        const params = {
          id: row.id
        };
        delSeckillBox(params).then((res) => {
          if (res.success) {
            this.boxList[this.tabIndex].list.splice(index, 1);
            this.$Message.success("删除成功！");
          }
        });
      } else {
        this.boxList[this.tabIndex].list.splice(index, 1);
        this.$Message.success("删除成功！");
      }
    },
    selectedBoxData(callback) {
      console.log("商品信息："+JSON.stringify(callback));
      let way = [];
      let data = JSON.parse(JSON.stringify(callback));

      data.forEach((e) => {
        way.push({
          name: e.name,
          originalPrice: e.price,
          price: e.price||e.price-30 ,
          promotionApplyStatus: e.promotionApplyStatus || "",
          quantity: e.quantity||10,
          seckillId: this.$route.query.id,
          boxId: e.id,
          timeLine: this.data[0].hours.split(",")[this.tabIndex],
        });
      });

      this.$set(this.boxList[this.tabIndex], "list", [
        ...way,
        // ...this.defaultGoodsList,
      ]);
      // this.$nextTick(() => {
      //   this.$forceUpdate();
      // });
    },
    openBoxListDilog() {
      // 显示商品选择器
      this.$refs.boxSelect.open("box");
     /* console.log("点击产品选择器"+JSON.stringify(this.$refs.boxSelect));*/
      let data = this.boxList[this.tabIndex].list;
      data.forEach((e) => {
        console.log("回调信息："+JSON.stringify(e));
        e.box_id = e.boxId;
      });
      console.log("信息："+JSON.stringify(data));
      this.$refs.boxSelect.boxData = data;

    },
    unixDate(time) {
      // 处理报名截止时间
      return this.$options.filters.unixToDate(new Date(time) / 1000);
    },
    unixHours(item) {
      if (item) {
        // 处理小时场次
        let hourArr = item.split(",");
        for (let i = 0; i < hourArr.length; i++) {
          hourArr[i] += ":00";
        }
        return hourArr;
      }
      return [];
    },
    // 格式化申请状态
    promotionApplyStatus(key) {
      switch (key) {
        case "APPLY":
          return "申请";
        case "PASS":
          return "通过";
        case "REFUSE":
          return "拒绝";
        default:
          return "未申请";
      }
    },
    // 展示审核拒绝原因
    showReason(reason) {
      this.$Modal.info({
        title: "拒绝原因",
        content: reason,
      });
    },
  },
  mounted() {
    this.init();
  },
  // 如果是从详情页返回列表页，修改列表页keepAlive为true，确保不刷新页面
  beforeRouteLeave(to, from, next) {
    if (to.name === "seckill") {
      to.meta.keepAlive = true;
    }
    next();
  },
};
</script>
<style lang="scss" scoped>
.operation {
  margin: 10px 0;
}

.reason {
  cursor: pointer;
  color: #2d8cf0;
  font-size: 12px;
}
</style>
