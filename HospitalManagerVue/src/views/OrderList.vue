<template>
    <!-- 卡片 -->
    <el-card>
        <!-- 搜索栏 -->
        <el-row type="flex">
            <el-col :span="6">
                <el-input v-model="query" placeholder="Search_by_id">
                    <el-button
                        slot="append"
                        style="font-size: 18px;"
                        @click="requestOrders"
                    ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> search</el-button>
                </el-input>
            </el-col>
        </el-row>
        <!-- 表格 -->
        <el-table :data="orderData" stripe style="width: 100%" border>
            <el-table-column
                prop="oId"
                label="NO"
                width="80px"
            ></el-table-column>
            <el-table-column
                prop="pId"
                label="patient_id"
                width="80px"
            ></el-table-column>

            <el-table-column prop="dId" label="doctor_id" width="100px">
            </el-table-column>

            <el-table-column
                prop="oStart"
                label="regist_time"
                width="180px"
            ></el-table-column>
            <el-table-column
                prop="oEnd"
                label="End_time"
                width="180px"
            ></el-table-column>
            <el-table-column
                prop="oRecord"
                label="reason"
                width="400px"
            ></el-table-column>
            <el-table-column
                prop="oDrug"
                label="drug"
                width="180px"
            ></el-table-column>
            <el-table-column
                prop="oCheck"
                label="check_item"
                width="180px"
            ></el-table-column>
            <el-table-column
                prop="oTotalPrice"
                label="fee/Yuan"
                width="80px"
            ></el-table-column>
            <el-table-column prop="oPriceState" label="payment_state" width="100px">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.oPriceState === 1"
                        >payed</el-tag
                    >
                    <!-- <el-tag type="danger" v-if="scope.row.oPriceState === 0 && scope.row.oState === 1">unpay</el-tag> -->
                    <el-button
                        type="danger"
                        size="mini"
                        v-if="
                            scope.row.oPriceState === 0 &&
                            scope.row.oState === 1
                        "
                        @click="priceClick(scope.row.oId)"
                        >PAY</el-button
                    >
                </template>
            </el-table-column>
            <el-table-column prop="oState" label="Regist_state" width="100px">
                <template slot-scope="scope">
                    <el-tag
                        type="success"
                        v-if="
                            scope.row.oState === 1 &&
                            scope.row.oPriceState === 1
                        "
                        >done</el-tag
                    >
                    <el-tag
                        type="danger"
                        v-if="scope.row.oState === 0 && scope.row.oState === 0"
                        >undone</el-tag
                    >
                </template>
            </el-table-column>
            <el-table-column label="Operation" width="140" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px"
                        type="danger"
                        @click="deleteDialog(scope.row.oId)"
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
    name: "OrderList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            orderData: [],
            total: 3,
        };
    },
    methods: {
        //删除挂号操作
        deleteOrder(id) {
            request
                .get("admin/deleteOrder", {
                    params: {
                        oId: id,
                    },
                })
                .then((res) => {
                    this.requestOrders();
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
                    this.deleteOrder(id);
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
            this.requestOrders();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestOrders();
        },
        // 加载订单列表
        requestOrders() {
            request
                .get("admin/findAllOrders", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.orderData = res.data.data.orders;
                    this.total = res.data.data.total;
                    toLoad()
                });
        },
    },
    created() {
        this.requestOrders();
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