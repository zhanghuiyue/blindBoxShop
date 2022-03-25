<template>
  <div class="seckill">
    <Card>
      <Row>
        <Form
          ref="searchForm"
          :model="searchForm"
          inline
          :label-width="70"
          class="search-form"
        >
          <Form-item label="活动名称" prop="promotionName">
            <Input
              type="text"
              v-model="searchForm.promotionName"
              placeholder="请输入活动名称"
              clearable
              style="width: 200px"
            />
          </Form-item>
          <Form-item label="活动状态" prop="promotionStatus">
            <Select
              v-model="searchForm.promotionStatus"
              placeholder="请选择"
              clearable
              style="width: 200px"
            >
              <Option value="NEW">未开始</Option>
              <Option value="START">已开始/上架</Option>
              <Option value="END">已结束/下架</Option>
              <Option value="CLOSE">紧急关闭/作废</Option>
            </Select>
          </Form-item>
          <Form-item label="活动时间">
            <DatePicker
              v-model="selectDate"
              type="daterange"
              clearable
              placeholder="选择起始时间"
              style="width: 200px"
            >
            </DatePicker>
          </Form-item>

          <Button
            @click="handleSearch"
            type="primary"
            icon="ios-search"
            class="search-btn"
            >搜索</Button
          >
        </Form>
      </Row>
      <Row class="operation padding-row">
        <Button @click="add" type="primary">添加秒杀</Button>
      </Row>

      <Tabs value="list" class="mt_10" >
        <TabPane label="秒杀活动列表" name="list">
          <Table
            :loading="loading"
            border
            :columns="columns"
            :data="data"
            ref="table"
            class="mt_10"
          >
            <template slot-scope="{ row }" slot="action">
              <Button
                type="info"
                size="small"
                class="mr_5"
                v-if="row.promotionStatus == 'NEW'"
                @click="edit(row)"
                >编辑</Button
              >

              <Button type="info" size="small" class="mr_5" v-else @click="manage(row)"
                >查看</Button
              >

              <Button
                type="success"
                size="small"
                class="mr_5"
                v-if="row.promotionStatus == 'NEW'"
                @click="manage(row)"
                >管理</Button
              >

              <Button
                type="error"
                size="small"
                v-if="row.promotionStatus == 'START' || row.promotionStatus == 'NEW'"
                class="mr_5"
                @click="off(row)"
                >下架</Button
              >
              &nbsp;
              <Button
                type="error"
                size="small"
                v-if="row.promotionStatus == 'CLOSE'"
                ghost
                @click="expire(row)"
                >删除</Button
              >
            </template>
          </Table>

          <Row type="flex" justify="end" class="mt_10">
            <Page
              style="margin: 20px 0"
              :current="searchForm.pageNumber"
              :total="total"
              :page-size="searchForm.pageSize"
              @on-change="changePage"
              @on-page-size-change="changePageSize"
              :page-size-opts="[10, 20, 50]"
              size="small"
              show-total
              show-elevator
              show-sizer
            ></Page>
          </Row>
        </TabPane>

      </Tabs>
    </Card>
  </div>
</template>

<script>
import { getBoxSeckillList, delBoxSeckill, updateBoxSeckillStatus } from "@/api/blindBoxSeckill";
import addBoxSeckill from "@/views/blind-box/box-recommend/seckill/seckill-add";
import { boxSeckillStatusRender } from "@/utils/boxSeckill";

export default {
  name: "seckill",
  components: {
    addBoxSeckill,
  },
  data() {
    return {
      selectDate: [],
      loading: true, // 表单加载状态
      searchForm: {
        // 搜索框初始化对象
        pageNumber: 1, // 当前页数
        pageSize: 10, // 页面大小
        sort: "startTime",
        order: "desc", // 默认排序方式
      },
      setupFlag: false, //默认不请求设置
      columns: [
        // 表单
        {
          title: "活动名称",
          key: "promotionName",
          minWidth: 130,
          tooltip: true,
        },

        {
          title: "开始时间",
          key: "startTime",
          width: 180,
        },
        {
          title: "结束时间",
          key: "endTime",
          width: 180,
        },

        {
          title: "活动状态",
          key: "promotionStatus",
          width: 100,
          render: (h, params) => {
            return boxSeckillStatusRender(h, params);
          },
        },

        {
          title: "操作",
          slot: "action",
          align: "center",
          width: 250,
        },
      ],
      data: [], // 表单数据
      total: 0, // 表单数据总数
    };
  },
  methods: {

    // 初始化信息
    init() {
      this.getDataList();
    },

    // 分页 改变页码
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getDataList();
    },
    // 分页 改变页数
    changePageSize(v) {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = v;
      this.getDataList();
    },
    // 搜索
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.getDataList();
    },
    edit(v) {
      // 编辑
      this.$router.push({ name: "box-seckill-add", query: { id: v.id } });
    },
    add() {
      this.$router.push({ name: "box-seckill-add" });
    },

    manage(v) {
      // 管理
      this.$router.push({ name: "box-seckill-goods", query: { id: v.id } });
    },

    off(v) {
      // 下架
      this.$Modal.confirm({
        title: "提示",
        content: "您确定要下架该活动吗？",
        onOk: () => {
          updateBoxSeckillStatus(v.id).then((res) => {
            if (res.success) {
              this.$Message.success("下架成功");
              this.getDataList();
            }
          });
        },
      });
    },
    expire(v) {
      // 作废
      this.$Modal.confirm({
        title: "提示",
        content: "您确定要作废该活动吗？",
        onOk: () => {
          delBoxSeckill(v.id).then((res) => {
            if (res.success) {
              this.$Message.success("作废成功");
              this.getDataList();
            }
          });
        },
      });
    },
    // 获取数据集合
    getDataList() {
      this.loading = true;
      if (this.selectDate && this.selectDate[0] && this.selectDate[1]) {
        this.searchForm.startTime = this.selectDate[0].getTime();
        this.searchForm.endTime = this.selectDate[1].getTime();
      } else {
        this.searchForm.startTime = null;
        this.searchForm.endTime = null;
      }
      // 带多条件搜索参数获取表单数据
      getBoxSeckillList(this.searchForm).then((res) => {

        this.loading = false;
        if (res.success) {
          this.data = res.result.records;
          this.total = res.result.total;
        }
      });
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style lang="scss">
.mr_5 {
  margin: 0 5px;
}
</style>
