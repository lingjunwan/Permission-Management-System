<template>
  <div class="app-container">
    <div style="padding: 20px 20px 0 20px">
      Role Authorization：{{ $route.query.roleName }}
    </div>
    <el-tree
      style="margin: 20px 0"
      ref="tree"
      :data="sysMenuList"
      node-key="id"
      show-checkbox
      default-expand-all
      :props="defaultProps"
    />
    <div style="padding: 20px 20px">
      <el-button
        :loading="loading"
        type="primary"
        icon="el-icon-check"
        size="mini"
        @click="save"
        >Save</el-button
      >
      <el-button
        @click="$router.push('/system/sysRole')"
        size="mini"
        icon="el-icon-refresh-right"
        >Back</el-button
      >
    </div>
  </div>
</template>
<script>
import api from "@/api/system/menu";
export default {
  name: "roleAuth",

  data() {
    return {
      loading: false, // Identifies whether the request is being saved, preventing duplicate submissions
      sysMenuList: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },

  created() {
    this.fetchData();
  },

  methods: {
    /*
      Initialization
      */
    fetchData() {
      const roleId = this.$route.query.id;
      api.toAssign(roleId).then((result) => {
        const sysMenuList = result.data;
        this.sysMenuList = sysMenuList;
        const checkedIds = this.getCheckedIds(sysMenuList);
        console.log("getPermissions() checkedIds", checkedIds);
        this.$refs.tree.setCheckedKeys(checkedIds);
      });
    },

    /*
      Get the list of all selected ids
      */
    getCheckedIds(auths, initArr = []) {
      return auths.reduce((pre, item) => {
        if (item.select && item.children.length === 0) {
          pre.push(item.id);
        } else if (item.children) {
          this.getCheckedIds(item.children, initArr);
        }
        return pre;
      }, initArr);
    },

    /*
      Save permission list
      */
    save() {
      //Get current child node
      //const checkedNodes = this.$refs.tree.getCheckedNodes()
      //Get the current child node and parent node
      const allCheckedNodes = this.$refs.tree.getCheckedNodes(false, true);
      let idList = allCheckedNodes.map((node) => node.id);
      console.log(idList);
      let assginMenuVo = {
        roleId: this.$route.query.id,
        menuIdList: idList,
      };
      this.loading = true;
      api.doAssign(assginMenuVo).then((result) => {
        this.loading = false;
        this.$message.success(
          result.$message || "Assign authority successfully"
        );
        this.$router.push("/system/sysRole");
      });
    },
  },
};
</script>
