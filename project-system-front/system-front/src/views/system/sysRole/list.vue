<template>
  <div class="app-container">
    Role List
    <!--search-div-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="RoleName">
              <el-input
                style="width: 100%"
                v-model="searchObj.roleName"
                placeholder="Role Name"
              ></el-input>
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

    <!-- tools-div -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add"
        >Create</el-button
      >
      <el-button class="btn-add" size="mini" @click="batchRemove()"
        >Batch Remove</el-button
      >
    </div>

    <!-- TABLE -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%; margin-top: 10px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" />
      <el-table-column label="No." width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="Role Name" />
      <el-table-column prop="roleCode" label="Role Code" />
      <el-table-column prop="createTime" label="Create Time" width="160" />
      <el-table-column label="Operation" width="200" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="edit(scope.row.id)"
            title="Edit"
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="removeDataById(scope.row.id)"
            title="Delete"
          />
          <el-button
            type="warning"
            icon="el-icon-baseball"
            size="mini"
            @click="showAssignAuth(scope.row)"
            title="Authority assignment"
          />
        </template>
      </el-table-column>
    </el-table>
    <!-- Pagination component -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />

    <el-dialog title="Create/Modify" :visible.sync="dialogVisible" width="40%">
      <el-form
        ref="dataForm"
        :model="sysRole"
        label-width="150px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="Role Name">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="Role Code">
          <el-input v-model="sysRole.roleCode" />
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
//  Introduce the js file that defines the interface
import api from "@/api/system/role";
export default {
  // Define the initial value
  data() {
    return {
      listLoading: true, //  Whether the data is loading or not
      list: [], //  Role List
      total: 0, //  Total number of records
      page: 1, //  Current page number
      limit: 3, //  Number of records displayed per page
      searchObj: {}, //  Conditional query wrapper object

      dialogVisible: false, // Pop-up-Box
      sysRole: {}, // Wrapping Add Form Data
      selectValue: [], //Wrapped array of selected checkbox content
    };
  },
  //  Fetching data before page rendering
  created() {
    // Calling the list method
    this.fetchData();
  },

  //  Define Method
  methods: {
    // Jump to assign menu authority
    showAssignAuth(row) {
      this.$router.push(
        "/system/assignAuth?id=" + row.id + "&roleName=" + row.roleName
      );
    },

    // Called when a multi-select option changes
    handleSelectionChange(selection) {
      this.selectValue = selection;
      //console.log(this.selectValue);
    },

    // Batch Remove
    batchRemove() {
      if (this.selectValue.length === 0) {
        this.$message.warning("Please select the records to be deleted!");
        return;
      }
      this.$confirm(
        "This operation will permanently delete the role, do you want to continue?",
        "Tip",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      ).then(() => {
        var idList = [];
        //Get the ids corresponding to multiple checkboxes, wrapped in an array
        for (var i = 0; i < this.selectValue.length; i++) {
          var obj = this.selectValue[i];
          //value of the id
          var id = obj.id;
          //The array named idList is usually used for batch operations, where the ids of the data to be operated on are stored and then operated on at once.
          idList.push(id);
        }
        // Call method delete
        api.batchRemove(idList).then((response) => {
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

    // Edit - Data playback
    edit(id) {
      this.dialogVisible = true;
      api.getRoleId(id).then((response) => {
        this.sysRole = response.data;
      });
    },

    // Click Confirm to add or update
    saveOrUpdate() {
      if (!this.sysRole.id) {
        this.saveRole();
      } else {
        this.updateRole();
      }
    },

    // Method to add
    saveRole() {
      api.saveRole(this.sysRole).then((response) => {
        // hint
        this.$message({
          type: "success",
          message: "Added successfully!",
        });
        // close Pop-up-Box
        this.dialogVisible = false;
        // Refresh the page
        this.fetchData();
      });
    },

    // Method to update
    updateRole() {
      api.update(this.sysRole).then((response) => {
        this.$message({
          type: "success",
          message: "Updated successfully!",
        });
        // close Pop-up-Box
        this.dialogVisible = false;
        // Refresh the page
        this.fetchData();
      });
    },

    // Click Create and a pop-up box appears
    add() {
      this.dialogVisible = true;
      this.sysRole = {};
    },

    // Delete
    removeDataById(id) {
      this.$confirm(
        "This operation will permanently delete the role, do you want to continue?",
        "Tip",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      ).then(() => {
        // promise
        // Call method delete
        api.removeId(id).then((response) => {
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

    // Reset
    resetData() {
      console.log("Reset query form");
      // Clear the form
      this.searchObj = {};
      // Query all data
      this.fetchData();
    },

    // Conditional pagination query list
    fetchData(pageNum = 1) {
      // Assignment of page number
      this.page = pageNum;
      // ajax
      api
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.listLoading = false;
          // console.log(response);
          // List of data per page
          this.list = response.data.records;
          // Total number of records
          this.total = response.data.total;
        });
    },
  },
};
</script>
