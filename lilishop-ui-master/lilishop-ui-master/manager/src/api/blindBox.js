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
export const getBannerListData = (params) => {
    return getRequest('/blind-box/recommendBanner/all', params)
}

