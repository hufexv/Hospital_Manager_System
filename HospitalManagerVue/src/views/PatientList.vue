<template>
    <!-- 卡片 -->
    <el-card>
        <!-- 搜索栏 -->
        <el-row type="flex">
            <el-col :span="6">
                <el-input v-model="query" placeholder="Search_by_name">
                    <el-button
                        slot="append"
                        style="font-size: 18px;"
                        @click="requestPatients"
                    ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> search</el-button>
                </el-input>
            </el-col>
        </el-row>
        <!-- 表格 -->
        <el-table :data="patientData" stripe style="width: 100%" border>
            <el-table-column prop="pId" label="account" width="100">
            </el-table-column>
            <el-table-column prop="pName" label="name" width="80">
            </el-table-column>
            <el-table-column prop="pGender" label="gender" width="60">
            </el-table-column>
            <el-table-column prop="pAge" label="age" width="180">
            </el-table-column>
            <el-table-column prop="pCard" label="ID"> </el-table-column>
            <el-table-column prop="pPhone" label="phone"> </el-table-column>
            <el-table-column prop="pEmail" label="Email" width="170">
            </el-table-column>
            <el-table-column prop="pState" label="state" width="80">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.pState === 1"
                        >normal</el-tag
                    >
                    <el-tag type="danger" v-else>deleted</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="Operation" width="160" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px"
                        type="danger"
                        @click="deleteDialog(scope.row.pId)"
                    ><i class="iconfont icon-r-delete" style="font-size: 22px;"></i> delete</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :current-page="pageNumber"
            :page-size="size"
            :page-sizes="[1, 2, 4, 8, 16]"
            :total="total"
        >
        </el-pagination>
    </el-card>
</template>
<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
    name: "PatientList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            patientData: [],
            total: 3,
        };
    },
    methods: {
        //删除病人操作
        deletePatient(id) {
            request
                .get("admin/deletePatient", {
                    params: {
                        pId: id,
                    },
                })
                .then((res) => {
                    this.requestPatients();
                    console.log(res);
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("delete?", "Tip", {
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                type: "warning",
            })
                .then(() => {
                    this.deletePatient(id);
                    this.$message({
                        type: "success",
                        message: "success!",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "canceled",
                    });
                });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestPatients();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestPatients();
        },
        // 加载患者列表
        requestPatients() {
            request
                .get("admin/findAllPatients", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.patientData = res.data.data.patients;

                    this.total = res.data.data.total;
                    toLoad()
                });
        },
    },
    created() {
        this.requestPatients();
    },
};
</script>
<style scoped lang="scss">
.el-table {
    margin-top: 20px;
    margin-bottom: 20px;
}
.el-form {
    margin-top: 0;
}
</style>