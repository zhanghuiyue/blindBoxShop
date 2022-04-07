<template>
  <div>
    <div class="content-goods-publish">
      <Form
        ref="baseInfoForm"
        :model="baseInfoForm"
        :label-width="120"
        :rules="baseInfoFormRule"
      >
        <div class="base-info-item">
          <h4>基本信息</h4>
          <div class="form-item-view">
            <FormItem label="所属盲盒种类">
              <span class="goods-category-name" style="color:red">{{
                this.baseInfoForm.boxName
              }}</span>

            </FormItem>

            <FormItem label="商品名称" prop="goodsName">
              <Input
                type="text"
                v-model="baseInfoForm.goodsName"
                placeholder="商品名称"
                clearable
                style="width: 260px"
              />
            </FormItem>

            <FormItem label="商品品牌">
              <span class="goods-category-name" style="color:red">{{
                this.baseInfoForm.brandName
              }}</span>

            </FormItem>


            <FormItem label="商品价格" prop="price">
              <Input
                type="text"
                v-model="baseInfoForm.price"
                placeholder="商品价格"
                clearable
                style="width: 260px"
              />
            </FormItem>

            <FormItem label="抽中概率" prop="probability">
              <Input
                type="text"
                v-model="baseInfoForm.probability"
                placeholder="抽中概率"
                clearable
                style="width: 260px"
              />
            </FormItem>


            <FormItem  label="元气豆"   prop="sinewyBean">
              <Input
                type="text"
                v-model="baseInfoForm.sinewyBean"
                placeholder="元气豆"
                clearable
                style="width: 260px"
              />
            </FormItem>

            <FormItem  label="库存"   prop="quantity">
              <Input
                type="text"
                v-model="baseInfoForm.quantity"
                placeholder="库存"
                clearable
                style="width: 260px"
              />
            </FormItem>

            <FormItem
              class="form-item-view-el"
              label="计量单位"
              prop="goodsUnit"
            >
              <Select v-model="baseInfoForm.goodsUnit" style="width: 100px">
                <Scroll :on-reach-bottom="handleReachBottom" >
                  <Option
                    v-for="(item, index) in goodsUnitList"
                    :key="index"
                    :value="item"
                  >{{ item }}
                  </Option>
                </Scroll>
              </Select>
            </FormItem>

            <FormItem
              class="form-item-view-el required"
              label="商品图片"
              prop="goodsGalleryFiles"
            >
              <div style="display: flex; flex-wrap: flex-start">
                <vuedraggable
                  :list="baseInfoForm.goodsGalleryFiles"
                  :animation="200"
                >
                  <div
                    class="demo-upload-list"
                    v-for="(item, __index) in baseInfoForm.goodsGalleryFiles"
                    :key="__index"
                  >
                    <template>
                      <img :src="item.url" />
                      <div class="demo-upload-list-cover">
                        <div>
                          <Icon
                            type="md-search"
                            size="30"
                            @click.native="handleViewGoodsPicture(item.url)"
                          ></Icon>
                          <Icon
                            type="md-trash"
                            size="30"
                            @click.native="handleRemoveGoodsPicture(item)"
                          ></Icon>
                        </div>
                      </div>
                    </template>
                  </div>
                </vuedraggable>

                <Upload
                  ref="upload"
                  :show-upload-list="false"
                  :on-success="handleSuccessGoodsPicture"
                  :format="['jpg', 'jpeg', 'png']"
                  :on-format-error="handleFormatError"
                  :on-exceeded-size="handleMaxSize"
                  :max-size="1024"
                  :before-upload="handleBeforeUploadGoodsPicture"
                  multiple
                  type="drag"
                  :action="uploadFileUrl"
                  :headers="{ ...accessToken }"
                  style="margin-left: 10px"
                >
                  <div style="width: 148px; height: 148px; line-height: 148px">
                    <Icon type="md-add" size="20"></Icon>
                  </div>
                </Upload>
              </div>
              <Modal title="View Image" v-model="goodsPictureVisible">
                <img
                  :src="previewGoodsPicture"
                  v-if="goodsPictureVisible"
                  style="width: 100%"
                />
              </Modal>
            </FormItem>
          </div>

          <div class="form-item-view">

            <FormItem
              class="form-item-view-el"
              label="商品描述"
              prop="skuList"
            >
              <editor
                eid="intro"
                v-model="baseInfoForm.intro"
              ></editor>
            </FormItem>
          </div>

        </div>
      </Form>
    </div>
    <!-- 底部按钮 -->
    <div class="footer">
      <ButtonGroup>
        <Button
          type="primary"
          @click="pre"
          v-if="!$route.query.id && !$route.query.draftId"
        >上一步
        </Button>
        <Button type="primary" @click="save" :loading="submitLoading">
          {{ this.$route.query.id ? "保存" : "保存商品" }}
        </Button>
      </ButtonGroup>
    </div>
  </div>
</template>
<script>
  import * as API_GOODS from "@/api/goods";
  import * as API_BOX from "@/api/blindBox";
  import vuedraggable from "vuedraggable";
  import editor from "@/views/my-components/lili/editor";
  import { uploadFile } from "@/libs/axios";
  import { regular } from "@/utils";

  export default {
    components: {
      editor,
      vuedraggable,
    },
    props: {
      firstData: {
        default: {},
        type: Object,
      },
    },
    data() {
      // 表单验证项，商品价格
      const checkPrice = (rule, value, callback) => {
        if (!value && value !== 0) {
          return callback(new Error("商品价格不能为空"));
        }
        setTimeout(() => {
          if (!regular.money.test(value)) {
            callback(new Error("请输入正整数或者两位小数"));
          } else if (parseFloat(value) > 99999999) {
            callback(new Error("商品价格设置超过上限值"));
          } else {
            callback();
          }
        }, 1000);
      };

      // 表单验证项，元气豆
      const checkPsinewyBean = (rule, value, callback) => {
        if (!value && value !== 0) {
          return callback(new Error("元气豆不能为空"));
        }
        setTimeout(() => {
          if (!regular.money.test(value)) {
            callback(new Error("请输入正整数或者两位小数"));
          } else if (parseFloat(value) > 999999999) {
            callback(new Error("元气豆设置超过上限值"));
          } else {
            callback();
          }
        }, 1000);
      };

      // 表单验证项，抽奖概率
      const checkProbability = (rule, value, callback) => {
        if (!value && value !== 0) {
          return callback(new Error("抽奖概率不能为空"));
        }
        setTimeout(() => {

          if (parseFloat(value) > 1) {
            callback(new Error("抽奖概率设置超过上限值"));
          } else {
            callback();
          }
        }, 1000);
      };

      // 表单验证项，库存
      const checkQuantity = (rule, value, callback) => {
        if (!value && value !== 0) {
          return callback(new Error("商品库存不能为空"));
        }
        setTimeout(() => {
          if (!regular.Integer.test(value)) {
            callback(new Error("请输入正整数"));
          } else if (parseFloat(value) > 9999) {
            callback(new Error("商品库存设置超过上限值"));
          } else {
            callback();
          }
        }, 1000);
      };


      return {
        regular,
        total:0,
        accessToken: "", //令牌token
        goodsParams: "",

        //提交状态
        submitLoading: false,
        //上传图片路径
        uploadFileUrl: uploadFile,
        // 预览图片路径
        previewPicture: "",
        //商品图片
        previewGoodsPicture: "",
        //展示图片层
        visible: false,
        //展示商品图片
        goodsPictureVisible: false,

        /** 发布商品基本参数 */
        baseInfoForm: {
          salesModel: "RETAIL",
          /** 商品相册列表 */
          goodsGalleryFiles: [],
          /** 是否立即发布 true 立即发布 false 放入仓库 */
          release: 1,

          /** 计量单位 **/
          goodsUnit: "",

          /** 商品详情 **/
          intro: "",
          updateSku: true,

          /** 商品类型 **/
          goodsType: "",


          /**商品所属盲盒ID*/
          blindBoxId:"",

          /**抽中概率*/
          probability:'' ,
          boxName:"",
          brandName:"",
          brandId: 0,
        },
        /** 表格头 */
        skuTableColumn: [],

        /** 固定列校验提示内容 */
        validatatxt: "请输入0~99999999之间的数字值",
        //参数panel展示
        params_panel: [],
        /** 存储未通过校验的单元格位置  */
        validateError: [],
        baseInfoFormRule: {
          blindBoxCategory: [{ required: true, message: "请选择商品品牌" }],
          goodsName: [regular.REQUIRED, regular.WHITE_SPACE, regular.VARCHAR60],
          price: [regular.REQUIRED, { validator: checkPrice }],
          sinewyBean: [regular.REQUIRED,{ validator: checkPrice }],
          goodsUnit: [{ required: true, message: "请选择计量单位" }],
          brandId: [{ required: true, message: "请选择商品品牌" }],
          name: [regular.REQUIRED, regular.VARCHAR5],
          value: [regular.REQUIRED, regular.VARCHAR60],
          probability:[regular.REQUIRED,{ validator: checkProbability }],
          quantity:[regular.REQUIRED, { validator: checkQuantity }],
        },
        params:{
          pageNumber:1,
          pageSize:10
        },

        /** 商品单位列表 */
        goodsUnitList: [],

      };
    },
    methods: {

      pre() {
        // 上一步
        this.$parent.activestep--;
      },
      // 预览图片
      handleView(url) {
        this.previewPicture = url;
        this.visible = true;
      },
      // 移除已选图片
      handleRemove(item, index) {
        this.selectedSku.images = this.selectedSku.images.filter(
          (i) => i.url !== item.url
        );
        if (this.selectedSku.images.length > 0 && index === 0) {
          this.previewPicture = this.selectedSku.images[0].url;
        } else if (this.selectedSku.images.length < 0) {
          this.previewPicture = "";
        }
      },
      // 查看商品大图
      handleViewGoodsPicture(url) {
        this.previewGoodsPicture = url;
        this.goodsPictureVisible = true;
      },
      // 移除商品图片
      handleRemoveGoodsPicture(file) {
        this.baseInfoForm.goodsGalleryFiles =
          this.baseInfoForm.goodsGalleryFiles.filter((i) => i.url !== file.url);
      },

      // 商品图片上传成功
      handleSuccessGoodsPicture(res, file) {
        console.log(res);
        if (file.response) {
          file.url = file.response.result;
          this.baseInfoForm.goodsGalleryFiles.push(file);
        }
      },
      // 图片格式不正确
      handleFormatError(file) {
        this.$Notice.warning({
          title: "文件格式不正确",
          desc: "文件 " + file.name + " 的格式不正确",
        });
      },
      // 图片大小不正确
      handleMaxSize(file) {
        this.$Notice.warning({
          title: "超过文件大小限制",
          desc: "图片大小不能超过1MB",
        });
      },
      // 图片上传前钩子
      handleBeforeUploadGoodsPicture(file) {
        const check = this.baseInfoForm.goodsGalleryFiles.length < 5;
        if (!check) {
          this.$Notice.warning({
            title: "图片数量不能大于五张",
          });
          return false;
        }
      },

      // 页面触底
      handleReachBottom(){
        setTimeout(() => {
          if (this.params.pageNumber * this.params.pageSize <= this.total) {
            this.params.pageNumber++;
            this.GET_GoodsUnit();
          }
        }, 1000);
      },
      // 获取商品单位
      GET_GoodsUnit() {

        API_GOODS.getGoodsUnitList(this.params).then((res) => {
          if (res.success) {
            this.goodsUnitList.push(...res.result.records.map((i) => i.name));
            this.total = res.result.total;
          }
        });
      },

      // 编辑时获取商品信息
      async GET_GoodData(id, draftId) {
        let response = {};
        if (draftId) {
          response = await API_GOODS.getDraftGoodsDetail(draftId);
        } else {
          response = await API_GOODS.getGoods(id);
          this.goodsId = response.result.id;
        }


        this.baseInfoForm = { ...this.baseInfoForm, ...response.result };
        this.categoryId = response.result.categoryPath.split(",")[2];

        if (
          response.result.goodsGalleryList &&
          response.result.goodsGalleryList.length > 0
        ) {
          this.baseInfoForm.goodsGalleryFiles =
            response.result.goodsGalleryList.map((i) => {
              let files = { url: i };
              return files;
            });
        }

        this.Get_SkuInfoByCategory(this.categoryId);

        this.renderGoodsDetailSku(response.result.skuList);

        /** 查询商品参数 */
        this.GET_GoodsParams();
        /** 查询店铺商品分类 */
        this.GET_ShopGoodsLabel();
        this.GET_GoodsUnit();
      },

      /**  添加商品 **/
      save() {
        this.submitLoading = true;
        this.$refs["baseInfoForm"].validate((valid) => {
          if (valid) {
            let submit = JSON.parse(JSON.stringify(this.baseInfoForm));
            if (
              submit.goodsGalleryFiles &&
              submit.goodsGalleryFiles.length <= 0
            ) {
              this.submitLoading = false;
              this.$Message.error("请上传商品图片");
              return;
            }

            let flag = false;
            let paramValue = "";

            if (flag) {
              this.$Message.error(paramValue + " 参数值不能为空");
              this.submitLoading = false;
              return;
            }

            if (submit.goodsGalleryFiles.length > 0) {
              submit.goodsGalleryList = submit.goodsGalleryFiles.map(
                (i) => i.url
              );
            }
            /** 参数校验 **/
            /* Object.keys(submit.goodsParamsList).forEach((item) => {
            });*/
            submit.release ? (submit.release = true) : (submit.release = false);
           console.log("提交的参数："+JSON.stringify(submit)) ;

           if (this.goodsId) {
              API_GOODS.editGoods(this.goodsId, submit).then((res) => {
                if (res.success) {
                  this.submitLoading = false;
                  this.$router.go(-1);
                } else {
                  this.submitLoading = false;
                }
              });
            } else {
              API_BOX.createGoods(submit).then((res) => {
                //console.log("添加商品回调："+JSON.stringify(res));

                if (res.success) {
                  this.submitLoading = false;
                  this.$parent.activestep = 2;
                  window.scrollTo(0, 0);
                } else {
                  this.submitLoading = false;
                }
              });
            }
          } else {
            this.submitLoading = false;

            this.$Message.error("还有必填项未做处理，请检查表单");
          }
        });
      },

    },
    mounted() {
      this.accessToken = {
        accessToken: this.getStore("accessToken"),
      };

      if (this.$route.query.id || this.$route.query.draftId) {
        // 编辑商品、模板
        this.GET_GoodData(this.$route.query.id, this.$route.query.draftId);
      }else {
        this.baseInfoForm.boxName = this.firstData.blindBox.name ;
        this.baseInfoForm.brandName = this.firstData.brand.name;
        this.baseInfoForm.goodsType = this.firstData.goodsType;
        this.baseInfoForm.blindBoxId = this.firstData.blindBox.id ;
        this.baseInfoForm.name = this.firstData.brand.name ;
        this.baseInfoForm.brandIntro = this.firstData.brand.logo ;
        // 获取商品单位
        this.GET_GoodsUnit();
      }

    },
  };
</script>
<style lang="scss" scoped>
  @import "./addGoods.scss";
</style>

<style>
  .ivu-select .ivu-select-dropdown{
    overflow: hidden !important;
  }
</style>
