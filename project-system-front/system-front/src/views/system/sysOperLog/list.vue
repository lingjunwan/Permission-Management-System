<template>
  <div id="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%; margin-top: 10px"
    >
      <el-table-column label="Index" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="title" label="Title" width="180" />
      <el-table-column prop="operName" label="Name" width="150" />
      <el-table-column prop="businessType" label="Business Type" width="150" />
      <el-table-column
        prop="requestMethod"
        label="Request Method"
        width="150"
      />
      <el-table-column prop="operUrl" label="Router" width="250" />
      <el-table-column prop="operIp" label="Ip Address" width="130" />
      <el-table-column prop="createTime" label="create Time" width="160" />
      <el-table-column prop="operatorType" label="Operator Type" width="130" />
      <el-table-column prop="jsonResult" label="Result" />
    </el-table>
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />
  </div>
</template>
<script>
import api1 from "@/api/system/operLog.js";

export default {
  // 定义数据
  data() {
    return {
      listLoading: false, //loading icon
      list: [], //role list
      total: 0,
      page: 1,
      limit: 8,
      searchObj: {},

      createTimes: [],
    };
  },

  //当页面加载时获取数据
  created() {
    this.fetchData();
  },

  methods: {
    //调用api层获取数据库中的数据
    fetchData(pageNum = 1) {
      //give pageNum
      this.page = pageNum;

      //ajax
      api1
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.listLoading = false;

          //每页数据列表
          this.list = response.data.records;
          this.operLog = response.data;
          console.log(this.list);
          this.createTimes = response.data.records.updateTime;
          console.log("时间");
          console.log(this.createTimes);
          //总记录数
          this.total = response.data.total;
        });
    },
  },
};
</script>
