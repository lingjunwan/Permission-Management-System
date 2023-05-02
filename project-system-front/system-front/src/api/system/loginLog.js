import request from '@/utils/request'

/*
api request link
*/
const api_name = '/admin/system/sysLoginLog/'

export default {
    getPageList(page, limit, searchObj) {
        return request({
            //interface
            url: `${api_name}/${page}/${limit}`,
            method: 'get',
            params: searchObj
        })
    }
}