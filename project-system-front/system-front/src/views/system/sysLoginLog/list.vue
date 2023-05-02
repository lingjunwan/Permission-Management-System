<template>
    <div id="app-container">
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%;margin-top: 10px;">

        <el-table-column label="Index" width="70" align="center">
            <template slot-scope="scope">
                {{ (page - 1) * limit + scope.$index + 1 }}
            </template>
        </el-table-column>

        <el-table-column prop="username" label="User Name" width="180" />
        <el-table-column prop="ipaddr" label="ipaddr" width="110" />
        <el-table-column prop="msg" label="Message" />
        <el-table-column prop="createTime" label="Access Time" />

    
    </el-table>

    <el-pagination :current-page="page" :total="total" :page-size="limit" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="fetchData" />
    </div>
</template>
<script>
import api1 from '@/api/system/loginLog.js'



export default {
    // 定义数据
    data() {
        return {
            listLoading: false,//loading icon
            list: [], //role list
            total: 0,
            page: 1,
            limit: 8,
            searchObj: {},

            createTimes: [],

            


        


        }
    },

    //当页面加载时获取数据
    created() {
        this.fetchData()
    },

    methods: {
        //调用api层获取数据库中的数据
        fetchData(pageNum = 1) {
            //give pageNum
            this.page = pageNum

            //ajax
            api1.getPageList(this.page, this.limit, this.searchObj)
                .then(response => {
                    this.listLoading = false
                    
                    //每页数据列表
                    this.list = response.data.records
                    
                    //总记录数
                    this.total = response.data.total
                })


        }

    }
}

</script>
