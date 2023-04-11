import request from "@/utils/request";
const api_name = "/admin/system/sysRole";

export default {
  // Get a paginated list of roles (with search)
  getPageList(page, limit, searchObj) {
    return request({
      // Interface Path
      // url: `/admin/system/sysRole` + page + "/" + limit,
      url: `${api_name}/${page}/${limit}`,
      method: "get", // Submission Method
      params: searchObj,
    });
  },
  //DELETE
  removeId(id) {
    return request({
      url: `${api_name}/remove/${id}`, // Interface Path
      method: "delete", // Submission Method
    });
  },
  // Add
  saveRole(role) {
    return request({
      url: `${api_name}/save`, // Interface Path
      method: "post", // Submission Method
      data: role, // Passing json format
    });
  },
  //Modify
  getRoleId(id) {
    return request({
      url: `${api_name}/findRoleById/${id}`, // Interface Path
      method: "post", // Submission Method
    });
  },
  // Update
  update(role) {
    return request({
      url: `${api_name}/update`, // Interface Path
      method: "put", // Submission Method
      data: role, // Passing json format
    });
  },
  // Batch Remove
  batchRemove(idList) {
    return request({
      url: `${api_name}/batchRemove`, // Interface Path
      method: "delete", // Submission Method
      data: idList, // Passing json format
    });
  },
  // Query a user's assigned role based on user id
  getRolesByUserId(userId) {
    return request({
      url: `${api_name}/toAssign/${userId}`,
      method: "get",
    });
  },

  // Assigning roles
  assignRoles(assginRoleVo) {
    return request({
      url: `${api_name}/doAssign`,
      method: "post",
      data: assginRoleVo,
    });
  },
};
