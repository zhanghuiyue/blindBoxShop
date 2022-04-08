// 统一请求路径前缀在libs/axios.js中修改
import { getRequest, postRequest, putRequest, deleteRequest} from '@/libs/axios';

//  获取categary分页列表
export const getBlindBoxPricePage = (params) => {
  return getRequest('/blind-box/price/getByPage', params)
}

// 删除
export const delBlindBoxPrice = (id) =>{
  return deleteRequest(`/blind-box/price/delete/${id}`)
}
//  添加
export const addBlindBoxPrice = (params) => {
  return postRequest('/blind-box/price/add', params)
}
// 修改品牌设置
export const updateBlindBoxPrice = (params) => {
  return putRequest(`/blind-box/price/update/${params.id}`, params)
}
// 禁用品牌
export const disableBlindBoxPrice = (id, params) => {
  return putRequest(`/blind-box/price/disable/${id}`, params)
}
