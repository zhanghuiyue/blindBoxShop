// 统一请求路径前缀在libs/axios.js中修改
import {
  getRequest,
  postRequest,
  putRequest,
  deleteRequest
} from "@/libs/axios";

// 获取盲盒秒杀活动申请列表

export const getBoxSeckill = params => {
  return getRequest(`/blind-box/recommendBanner/seckill/apply`, params);
};


// 获取秒杀活动数据
export const getBoxSeckillList = params => {
  return getRequest("/blind-box/recommendBanner/seckill/list", params);
};

// 获取秒杀活动审核列表
export const seckillBoxList = params => {
  return getRequest("/blind-box/recommendBanner/seckill/apply", params);
};

// 获取秒杀活动详情数据
export const seckillBoxDetail = (id, params) => {
  return getRequest(`/blind-box/recommendBanner/seckill/${id}`, params);
};

// 删除秒杀活动
export const delBoxSeckill = id => {
  return deleteRequest(`/blind-box/recommendBanner/seckill/${id}`);

};

// 保存秒杀活动
export const saveBoxSeckill = params => {
  return postRequest("/blind-box/recommendBanner/seckill", params,{
    "Content-type": "application/json"
  });
};


// 修改秒杀活动
export const updateBoxSeckill = params => {
  return putRequest("/blind-box/recommendBanner/seckill", params, {
    "Content-type": "application/json"
  });
};

// 关闭秒杀活动
export const updateBoxSeckillStatus = (id, params) => {
  return putRequest(`/blind-box/recommendBanner/seckill/status/${id}`, params);
};

// 审核秒杀活动
export const auditApplyBoxSeckill = params => {
  return putRequest(`/blind-box/recommendBanner/seckill/apply/audit/${params.ids}`, params);
};

// 添加限时抢购盲盒
export const setSeckillBox = (params) => {
  return postRequest(`/blind-box/recommendBanner/seckill/apply/${params.seckillId}`,params.applyVos,{'Content-type': 'application/json'})
}

// 删除秒杀盲盒
export const delSeckillBox = params => {
  return deleteRequest(`/blind-box/recommendBanner/seckill/apply/${params.id}`);
};

