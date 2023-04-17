<template>
  <div class="app-container">
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="Keyword">
              <el-input
                style="width: 95%"
                v-model="searchObj.keyword"
                placeholder="User Name / Name / Phone Number"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="Time">
              <el-date-picker
                v-model="createTimes"
                type="datetimerange"
                range-separator="To"
                start-placeholder="Start time"
                end-placeholder="End time"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px; width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display: flex">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="fetchData()"
            >Search</el-button
          >
          <el-button icon="el-icon-refresh" size="mini" @click="resetData"
            >Reset</el-button
          >
        </el-row>
      </el-form>
    </div>

    <!-- Toolbar -->
    <div class="tools-div">
      <el-button
        type="success"
        icon="el-icon-plus"
        size="mini"
        @click="add"
        :disabled="$hasBP('bnt.sysUser.add') === false"
        >Add</el-button
      >
    </div>

    <!-- List -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column label="No." width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="Username" width="180" />
      <el-table-column prop="name" label="Name" width="110" />
      <el-table-column prop="phone" label="Phone" />
      <el-table-column label="Status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status === 1"
            @change="switchStatus(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time" />

      <el-table-column label="Operation" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="edit(scope.row.id)"
            :disabled="$hasBP('bnt.sysUser.update') === false"
            title="Edit"
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="removeDataById(scope.row.id)"
            :disabled="$hasBP('bnt.sysUser.remove') === false"
            title="Delete"
          />
          <el-button
            type="warning"
            icon="el-icon-baseball"
            size="mini"
            @click="showAssignRole(scope.row)"
            :disabled="$hasBP('bnt.sysUser.assignRole') === false"
            title="Assign Roles"
          />
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />

    <el-dialog title="Assign Roles" :visible.sync="dialogRoleVisible">
      <el-form label-width="80px">
        <el-form-item label="username">
          <el-input disabled :value="sysUser.username"></el-input>
        </el-form-item>

        <el-form-item label="Role List">
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >Select All</el-checkbox
          >
          <div style="margin: 15px 0"></div>
          <el-checkbox-group
            v-model="userRoleIds"
            @change="handleCheckedChange"
          >
            <el-checkbox
              v-for="role in allRoles"
              :key="role.id"
              :label="role.id"
              >{{ role.roleName }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignRole" size="small"
          >Save</el-button
        >
        <el-button @click="dialogRoleVisible = false" size="small"
          >Cancel</el-button
        >
      </div>
    </el-dialog>

    <el-dialog title="Add / Edit" :visible.sync="dialogVisible" width="40%">
      <el-form
        ref="dataForm"
        :model="sysUser"
        label-width="100px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="sysUser.username" />
        </el-form-item>
        <el-form-item v-if="!sysUser.id" label="Password" prop="password">
          <el-input v-model="sysUser.password" type="password" />
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="sysUser.name" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="sysUser.phone" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button
          @click="dialogVisible = false"
          size="small"
          icon="el-icon-refresh-right"
          >Cancel</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-check"
          @click="saveOrUpdate()"
          size="small"
          >Confirm</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import api from "@/api/system/user";
import roleApi from "@/api/system/role";
export default {
  data() {
    return {
      listLoading: false, // Whether the data is loading or not
      list: null, // banner list
      total: 0, // Total number of records in the database
      page: 1, // Default Page Code
      limit: 3, // Number of records per page
      searchObj: {}, // Query Form Object

      createTimes: [],

      dialogVisible: false,
      sysUser: {},

      dialogRoleVisible: false,
      allRoles: [], // List of all roles
      userRoleIds: [], // List of user's role IDs
      isIndeterminate: false, // Whether it is indeterminate
      checkAll: false, // Whether to select all
    };
  },

  // Life cycle function: memory ready, page not yet rendered
  created() {
    console.log("list created......");
    this.fetchData();
  },

  methods: {
    // Show Assigned Role
    showAssignRole(row) {
      this.sysUser = row;
      this.dialogRoleVisible = true;
      roleApi.getRolesByUserId(row.id).then((response) => {
        this.allRoles = response.data.allRoles;
        console.log(this.allRoles);
        this.userRoleIds = response.data.userRoleIds;
        console.log(this.userRoleIds);
        this.checkAll = this.userRoleIds.length === this.allRoles.length;
        this.isIndeterminate =
          this.userRoleIds.length > 0 &&
          this.userRoleIds.length < this.allRoles.length;
      });
    },

    /*
    Check all listeners whose status has changed
    */
    handleCheckAllChange(value) {
      // value-- Current ticking status:true/false
      // If all are currently selected, userRoleIds is an array of all role ids, otherwise it is an empty array
      this.userRoleIds = value ? this.allRoles.map((item) => item.id) : [];
      // If the current selection is neither all nor none, specify false
      this.isIndeterminate = false;
    },

    /*
    Listening for changes to the selected items in the role list
    */
    handleCheckedChange(value) {
      const { userRoleIds, allRoles } = this;
      this.checkAll =
        userRoleIds.length === allRoles.length && allRoles.length > 0;
      this.isIndeterminate =
        userRoleIds.length > 0 && userRoleIds.length < allRoles.length;
    },

    // Assign roles
    assignRole() {
      let assginRoleVo = {
        userId: this.sysUser.id,
        roleIdList: this.userRoleIds,
      };
      roleApi.assignRoles(assginRoleVo).then((response) => {
        this.$message.success(response.message || "Role assigned successfully");
        this.dialogRoleVisible = false;
        this.fetchData(this.page);
      });
    },

    // Update User Status
    switchStatus(row) {
      row.status = row.status === 1 ? 0 : 1;
      api.updateStatus(row.id, row.status).then((response) => {
        if (response.code) {
          this.$message.success(response.message || "Operated successfully");
          this.fetchData();
        }
      });
    },
    // Delete User by ID
    removeDataById(id) {
      this.$confirm(
        "This operation will permanently delete the user, do you want to continue?",
        "Tip",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      ).then(() => {
        // promise
        // Call method delete
        api.removeById(id).then((response) => {
          //Tip Message
          this.$message({
            type: "success",
            message: "Deleted successfully!",
          });
          //Refresh
          this.fetchData();
        });
      });
    },
    // Query by id for data return
    edit(id) {
      // pop-up box
      this.dialogVisible = true;
      // Calling interface queries
      api.getUserId(id).then((response) => {
        this.sysUser = response.data;
      });
    },
    // SaveOrUpdate
    saveOrUpdate() {
      if (!this.sysUser.id) {
        this.save();
      } else {
        this.update();
      }
    },
    save() {
      api.save(this.sysUser).then((response) => {
        // Tip Message
        this.$message.success("Operated successfully");
        // Close pop-up box
        this.dialogVisible = false;
        // Close Page
        this.fetchData();
      });
    },
    update() {
      api.update(this.sysUser).then((response) => {
        this.$message.success("Operated successfully");
        this.dialogVisible = false;
        this.fetchData();
      });
    },
    //Method to add a pop-up box
    add() {
      this.dialogVisible = true;
      this.sysUser = {};
    },

    // Reset query form
    resetData() {
      console.log("Reset query form");
      this.searchObj = {};
      this.createTimes = [];
      this.fetchData();
    },

    // Load banner list data
    fetchData(page = 1) {
      this.page = page;
      if (this.createTimes && this.createTimes.length == 2) {
        this.searchObj.createTimeBegin = this.createTimes[0];
        this.searchObj.createTimeEnd = this.createTimes[1];
      }
      api
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          //this.list = response.data.list
          this.list = response.data.records;
          this.total = response.data.total;
        });
    },
  },
};
</script>
