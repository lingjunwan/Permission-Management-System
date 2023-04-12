import request from "@/utils/request";

/*
Menu management related API request functions
*/
const api_name = "/admin/system/sysMenu";

export default {
  /*
  Get permission (menu/function) list
  */
  findNodes() {
    return request({
      url: `${api_name}/findNodes`,
      method: "get",
    });
  },

  /*
  Delete
  */
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: "delete",
    });
  },

  /*
  Save
  */
  save(sysMenu) {
    return request({
      url: `${api_name}/save`,
      method: "post",
      data: sysMenu,
    });
  },

  /*
  Update
  */
  updateById(sysMenu) {
    return request({
      url: `${api_name}/update`,
      method: "post",
      data: sysMenu,
    });
  },

  // Update Status
  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: "get",
    });
  },

  /*
  View the list of permissions for a role
  */
  toAssign(roleId) {
    return request({
      url: `${api_name}/toAssign/${roleId}`,
      method: "get",
    });
  },

  /*
  Delegate authority to a role
  */
  doAssign(assginMenuVo) {
    return request({
      url: `${api_name}/doAssign`,
      method: "post",
      data: assginMenuVo,
    });
  },
};
