import request from "@/utils/request";

const api_name = "/admin/system/sysUser";

export default {
  getPageList(page, limit, searchObj) {
    return request({
      // Interface Path
      // url: `/admin/system/sysRole` + page + "/" + limit,
      url: `${api_name}/${page}/${limit}`,
      method: "get", // Submission Method
      params: searchObj,
    });
  },

  // Add
  save(user) {
    return request({
      url: `${api_name}/save`,
      method: "post",
      data: user,
    });
  },

  // getById
  getUserId(id) {
    return request({
      url: `${api_name}/getUser/${id}`,
      method: "get",
    });
  },
  // update
  update(user) {
    return request({
      url: `${api_name}/update`,
      method: "post",
      data: user,
    });
  },
  // remove
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: "delete",
    });
  },
  // Update Status
  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: "get",
    });
  },
};
