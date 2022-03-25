<template>
  <div>
    <Card>
      <Form ref="form" :model="form" :label-width="120" :rules="formRule">
        <div class="base-info-item">
          <h4>基本信息</h4>
          <div class="form-item-view">
            <FormItem label="活动名称" prop="promotionName">
              <Input
                type="text"
                v-model="form.promotionName"
                placeholder="请填写活动名称"
                clearable
                style="width: 260px"
              />
            </FormItem>

            <FormItem label="活动开始时间" prop="startTime">
              <DatePicker
                type="datetime"
                v-model="form.startTime"
                format="yyyy-MM-dd"
                placeholder="请选择"
                clearable
                style="width: 200px"
              >
              </DatePicker>
            </FormItem>

            <FormItem label="抢购时间段" prop="seckillPeriod">
              <Tag
                v-for="item in form.seckillPeriod"
                :key="item"
                :name="item"
                closable
                style="marrgin-left: 10px"
                @on-close="removePeriodTime"
                >{{ item >= 10 ? item : "0" + item }}:00</Tag
              >
              <InputNumber
                :max="23"
                :min="0"
                v-model="periodTime"
                v-show="showAddPeriod"
                @on-blur="addPeriodTime"
              ></InputNumber>
              <Button type="default" @click="addPeriod">添加时间段</Button>
            </FormItem>

          </div>
          <div class="foot-btn">
            <Button @click="closeCurrentPage" style="margin-right: 5px">返回</Button>
            <Button type="primary" :loading="submitLoading" @click="handleSubmit"
              >提交</Button
            >
          </div>
        </div>
      </Form>
    </Card>
  </div>
</template>

<script>
import { updateBoxSeckill, seckillBoxDetail,saveBoxSeckill } from "@/api/blindBoxSeckill";

export default {
  name: "addBoxSeckill",
  data() {
    return {
      form: {
        /** 活动名称 */
        promotionName: "",
        /** 活动开始时间 */
        startTime: "",
        /** 活动结束时间 */
        endTime: "",
        /** 抢购时间段 */
        seckillPeriod: [],

        promotionStatus: "NEW",
      },
      id: this.$route.query.id, // 活动id
      periodTime: null, // 抢购时间段
      showAddPeriod: false, // input显隐
      submitLoading: false, // 添加或编辑提交状态
      modalType: 0, // 添加或编辑标识
      formRule: {
        promotionName: [{ required: true, message: "请填写活动名称" }],
        seckillPeriod: [{ required: true, message: "请填写抢购时间段" }],
        startTime: [{ required: true, message: "请填写活动开始时间" }],

      },
    };
  },
  mounted() {
    // 如果id不为空则查询信息
    if (this.id) {
      this.getData();
      this.modalType =1 ;
    }
  },
  methods: {
    // 关闭当前页面
    closeCurrentPage() {
      this.$store.commit("removeTag", "manager-seckill-add");
      localStorage.pageOpenedList = JSON.stringify(this.$store.state.app.pageOpenedList);
      this.$router.go(-1);
    },
    // 获取详情数据
    getData() {
      console.log("获取数据详情");
      seckillBoxDetail(this.id).then((res) => {
        console.log("秒杀活动详情:"+JSON.stringify(res));
        if (res.success) {
          let data = res.result;
          data.seckillPeriod = res.result.hours.split(",");
          this.form = data;
        }
      });
    },
    addPeriod() {
      // 添加时间段显示input
      this.addPeriodTime();
      this.showAddPeriod = true;
    },
    addPeriodTime() {
      // 添加秒杀时间段
      this.showAddPeriod = false;
      if (
        this.periodTime !== null &&
        !this.form.seckillPeriod.includes(this.periodTime)
      ) {
        this.form.seckillPeriod.push(this.periodTime);
      }
    },
    removePeriodTime(event, name) {
      // 移除秒杀时间段
      this.form.seckillPeriod = this.form.seckillPeriod.filter((i) => i !== name);
    },
    // // 申请截止时间格式化
    // applyTimeChange (time) {
    //   console.log(time);
    //
    // },
    // // 开始时间格式化
    // startTimeChange (time) {
    //   this.form.startTime = time
    // },
    /** 添加秒杀活动 */
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true;
          this.form.hours = this.form.seckillPeriod.toString();

          let params = this.form;
          params.startTime = this.$options.filters.unixToDate(this.form.startTime / 1000);
          let startTime = this.form.startTime;
          params.endTime = startTime.replace("00:00:00","23:59:59");
          if(this.modalType ===0){
            delete params.id;
            saveBoxSeckill(params).then((res) => {
              this.submitLoading = false;
              if (res && res.success) {
                this.$Message.success("添加成功");
                this.closeCurrentPage();
              }
            });

          }else{
            console.log("编辑操作！");
            delete this.form.createTime;
            delete this.form.updateTime;
            delete this.form.seckillApplyList;
            // 编辑
            updateBoxSeckill(params).then((res) => {
              this.submitLoading = false;
              if (res && res.success) {
                this.$Message.success("编辑成功");
                this.closeCurrentPage();
              }
            });



          }

        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
h4 {
  margin-bottom: 10px;
  padding: 0 10px;
  border: 1px solid #ddd;
  background-color: #f8f8f8;
  font-weight: bold;
  color: #333;
  font-size: 14px;
  line-height: 40px;
  text-align: left;
}
.ivu-form-item {
  margin-bottom: 30px;
}
</style>
