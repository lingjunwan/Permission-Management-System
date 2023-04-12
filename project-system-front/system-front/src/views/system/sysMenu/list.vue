<template>
  <div class="app-container">
    <!-- Toolbar -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add()"
        >Create</el-button
      >
    </div>
    <el-table
      :data="sysMenuList"
      style="width: 100%; margin-bottom: 20px; margin-top: 10px"
      row-key="id"
      border
      :default-expand-all="false"
      :tree-props="{ children: 'children' }"
    >
      <el-table-column prop="name" label="Name of menu" width="160" />
      <el-table-column label="Icon">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="perms" label="Permission Mark" width="160" />
      <el-table-column prop="path" label="Routing Path" width="120" />
      <el-table-column prop="component" label="Component Path" width="160" />
      <el-table-column prop="sortValue" label="Sort" width="60" />
      <el-table-column label="Status" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status === 1"
            @change="switchStatus(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Create Time" width="160" />
      <el-table-column
        label="Operation"
        width="180"
        align="center"
        fixed="right"
      >
        <template slot-scope="scope">
          <el-button
            type="success"
            v-if="scope.row.type !== 2"
            icon="el-icon-plus"
            size="mini"
            @click="add(scope.row)"
            title="Add subordinate nodes"
          />
          <el-button
            type="primary"
            icon="el-icon-edit"
            size="mini"
            @click="edit(scope.row)"
            title="Edit"
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            @click="removeDataById(scope.row.id)"
            title="Delete"
            :disabled="scope.row.children.length > 0"
          />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
      <el-form
        ref="dataForm"
        :model="sysMenu"
        label-width="150px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="Superior Depart" v-if="sysMenu.id === ''">
          <el-input v-model="sysMenu.parentName" disabled="true" />
        </el-form-item>
        <el-form-item label="Menu Type" prop="type">
          <el-radio-group v-model="sysMenu.type" :disabled="typeDisabled">
            <el-radio :label="0" :disabled="type0Disabled">Catalog</el-radio>
            <el-radio :label="1" :disabled="type1Disabled">Menu</el-radio>
            <el-radio :label="2" :disabled="type2Disabled">Button</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Menu Name" prop="name">
          <el-input v-model="sysMenu.name" />
        </el-form-item>
        <el-form-item label="Icon" prop="icon" v-if="sysMenu.type !== 2">
          <el-select v-model="sysMenu.icon" clearable>
            <el-option
              v-for="item in iconList"
              :key="item.class"
              :label="item.class"
              :value="item.class"
            >
              <span style="float: left">
                <i :class="item.class"></i>
                <!-- If the icon is displayed dynamically, add a judgment here -->
              </span>
              <span style="padding-left: 6px">{{ item.class }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Sort">
          <el-input-number
            v-model="sysMenu.sortValue"
            controls-position="right"
            :min="0"
          />
        </el-form-item>
        <el-form-item prop="path">
          <span slot="label">
            <el-tooltip
              content="The routing path to access, e.g. `sysUser`"
              placement="top"
            >
              <i class="el-icon-question"></i>
            </el-tooltip>
            Routing Path
          </span>
          <el-input
            v-model="sysMenu.path"
            placeholder="Please enter the routing path"
          />
        </el-form-item>
        <el-form-item prop="component" v-if="sysMenu.type !== 0">
          <span slot="label">
            <el-tooltip
              content="Accessed component path, e.g. `system/user/index`, by default in the `views` directory"
              placement="top"
            >
              <i class="el-icon-question"></i>
            </el-tooltip>
            Component Path
          </span>
          <el-input
            v-model="sysMenu.component"
            placeholder="Please enter the component path"
          />
        </el-form-item>
        <el-form-item v-if="sysMenu.type === 2">
          <el-input
            v-model="sysMenu.perms"
            placeholder="Please enter the permission mark"
            maxlength="100"
          />
          <span slot="label">
            <el-tooltip
              content="The permission characters defined in the controller, such as:@PreAuthorize(hasAuthority('bnt.sysRole.list'))"
              placement="top"
            >
              <i class="el-icon-question"></i>
            </el-tooltip>
            Permission characters
          </span>
        </el-form-item>
        <el-form-item label="Status" prop="type">
          <el-radio-group v-model="sysMenu.status">
            <el-radio :label="1">Normal</el-radio>
            <el-radio :label="0">Disabled</el-radio>
          </el-radio-group>
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
import api from "@/api/system/menu";
const defaultForm = {
  id: "",
  parentId: "",
  name: "",
  type: 0,
  path: "",
  component: "",
  perms: "",
  icon: "",
  sortValue: 1,
  status: 1,
};
export default {
  // Define Data
  data() {
    return {
      sysMenuList: [],
      expandKeys: [], // Items that need to be expanded automatically

      typeDisabled: false,
      type0Disabled: false,
      type1Disabled: false,
      type2Disabled: false,
      dialogTitle: "",

      dialogVisible: false,
      sysMenu: defaultForm,
      saveBtnDisabled: false,

      iconList: [
        {
          class: "el-icon-s-tools",
        },
        {
          class: "el-icon-s-custom",
        },
        {
          class: "el-icon-setting",
        },
        {
          class: "el-icon-user-solid",
        },
        {
          class: "el-icon-s-help",
        },
        {
          class: "el-icon-phone",
        },
        {
          class: "el-icon-s-unfold",
        },
        {
          class: "el-icon-s-operation",
        },
        {
          class: "el-icon-more-outline",
        },
        {
          class: "el-icon-s-check",
        },
        {
          class: "el-icon-tickets",
        },
        {
          class: "el-icon-s-goods",
        },
        {
          class: "el-icon-document-remove",
        },
        {
          class: "el-icon-warning",
        },
        {
          class: "el-icon-warning-outline",
        },
        {
          class: "el-icon-question",
        },
        {
          class: "el-icon-info",
        },
      ],
    };
  },

  // Fetching data when the page loads
  created() {
    this.fetchData();
  },

  methods: {
    // Update Menu Status
    switchStatus(row) {
      row.status = row.status === 1 ? 0 : 1;
      api.updateStatus(row.id, row.status).then((response) => {
        if (response.code) {
          this.$message.success(response.message || "Operated successfully");
          this.fetchData();
        }
      });
    },

    //C all the api layer to get the data in the database
    fetchData() {
      console.log("Load List");
      api.findNodes().then((response) => {
        this.sysMenuList = response.data;
        console.log(this.sysMenuList);
      });
    },

    // Delete data based on id
    removeDataById(id) {
      // debugger
      this.$confirm(
        "This operation will permanently delete the record, do you want to continue?",
        "Tip",
        {
          confirmButtonText: "Confirm",
          cancelButtonText: "Cancel",
          type: "warning",
        }
      )
        .then(() => {
          // promise
          // Click OK to call ajax remotely
          return api.removeById(id);
        })
        .then((response) => {
          this.fetchData();
          this.$message({
            type: "success",
            message: response.message,
          });
        })
        .catch(() => {
          this.$message.info("Cancel Delete");
        });
    },

    //弹出添加或更新的表单
    add(row) {
      debugger;
      this.typeDisabled = false;
      this.dialogTitle = "Add Subordinate Node";
      this.dialogVisible = true;

      this.sysMenu = Object.assign({}, defaultForm);
      this.sysMenu.id = "";
      if (row) {
        this.sysMenu.parentId = row.id;
        this.sysMenu.parentName = row.name;
        //this.sysMenu.component = 'ParentView'
        if (row.type === 0) {
          this.sysMenu.type = 1;
          this.typeDisabled = false;
          this.type0Disabled = false;
          this.type1Disabled = false;
          this.type2Disabled = true;
        } else if (row.type === 1) {
          this.sysMenu.type = 2;
          this.typeDisabled = true;
        }
      } else {
        this.dialogTitle = "Add directory node";
        this.sysMenu.type = 0;
        this.sysMenu.component = "Layout";
        this.sysMenu.parentId = 0;
        this.typeDisabled = true;
      }
    },

    // Edit
    edit(row) {
      debugger;
      this.dialogTitle = "Edit Menu";
      this.dialogVisible = true;

      this.sysMenu = Object.assign({}, row);
      this.typeDisabled = true;
    },

    // Add or update
    saveOrUpdate() {
      if (this.sysMenu.type === 0 && this.sysMenu.parentId !== 0) {
        this.sysMenu.component = "ParentView";
      }
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.saveBtnDisabled = true; // Preventing duplicate form submissions
          if (!this.sysMenu.id) {
            this.save();
          } else {
            this.update();
          }
        }
      });
    },

    // Save
    save() {
      api.save(this.sysMenu).then((response) => {
        this.$message.success(response.message || "操作成功");
        this.dialogVisible = false;
        this.fetchData(this.page);
      });
    },

    // Update
    update() {
      api.updateById(this.sysMenu).then((response) => {
        this.$message.success(response.message || "操作成功");
        this.dialogVisible = false;
        this.fetchData();
      });
    },
  },
};
</script>
