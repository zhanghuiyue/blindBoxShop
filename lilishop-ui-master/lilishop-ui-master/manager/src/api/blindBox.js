// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, deleteRequest} from '@/libs/axios';

//  获取banner分页列表
export const getBlindBoxBannerPage = (params) => {
    return getRequest('/blind-box/recommendBanner/getByPage', params)
}
// 批量banner
export const delBanner = (ids) =>{
  return deleteRequest(`/blind-box/recommendBanner/delByIds/${ids}`)
}
//  添加
export const addBanner = (params) => {
    return postRequest('/blind-box/recommendBanner', params)
}
// 修改banner设置
export const updateBanner = (params) => {
    return putRequest(`/blind-box/recommendBanner/${params.id}`, params)
}
// 禁用banner
export const disableBanner = (id, params) => {
    return putRequest(`/blind-box/recommendBanner/disable/${id}`, params)
}

//获取所有可用banner
export const getBannerListData = () => {
    return getRequest('/blind-box/recommendBanner/all')
}

// 获取盲盒分页列表
export const getBoxListData = (params) => {
  return getRequest('/blind-box/getByPage', params)
}


// 获取盲盒列表
export const getBoxList = (params) => {
  return getRequest('/blind-box/list', params)
}

export function createGoods(params) {
  return postRequest("/blind-box/goods/create", params, {
    "Content-Type": "application/json"
  });
}

//  获取盲盒分页列表
export const getBlindBoxPage = (params) => {
  return getRequest('/blind-box/getByPage', params)
}

// 删除
export const delBlindBox = (id) =>{
  return deleteRequest(`/blind-box/delete/${id}`)
}

//  添加
export const addBlindBox = (params) => {
  return postRequest('/blind-box/add', params)
}
// 修改品牌设置
export const updateBlindBox = (params) => {
  return putRequest(`/blind-box/update/${params.id}`, params)
}
// 禁用品牌
export const disableBlindBox = (id, params) => {
  return putRequest(`/blind-box/disable/${id}`, params)
}
//  获取盲盒分类列表
export const getBlindBoxCategoryList = (params) => {
  return getRequest('/blind-box/category/list', params)
}
