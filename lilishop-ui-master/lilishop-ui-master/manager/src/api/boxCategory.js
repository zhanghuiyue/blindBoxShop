// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, deleteRequest} from '@/libs/axios';

//  获取categary分页列表
export const getBlindBoxCategoryPage = (params) => {
  return getRequest('/blind-box/category/getByPage', params)
}

// 删除
export const delBlindBoxCategory = (id) =>{
  return deleteRequest(`/blind-box/category/delete/${id}`)
}
//  添加
export const addBlindBoxCategory = (params) => {
  return postRequest('/blind-box/category/add', params)
}
// 修改品牌设置
export const updateBlindBoxCategory = (params) => {
  return putRequest(`/blind-box/category/update/${params.id}`, params)
}
// 禁用品牌
export const disableBlindBoxCategory = (id, params) => {
  return putRequest(`/blind-box/category/disable/${id}`, params)
}
