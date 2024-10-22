<template>
    <div>
        <el-card>
            <el-table :data="orderData" stripe style="width: 100%" border>
                <el-table-column
                    prop="oId"
                    label="regist"
                    width="75px"
                ></el-table-column>
                <el-table-column
                    prop="pId"
                    label="user_id"
                    width="75px"
                ></el-table-column>
                <el-table-column
                    prop="pName"
                    label="name"
                    width="75px"
                ></el-table-column>
                <el-table-column
                    prop="dId"
                    label="doctor_id"
                    width="75px"
                ></el-table-column>
                <el-table-column
                    prop="dName"
                    label="doctor_name"
                    width="75px"
                ></el-table-column>

                <el-table-column
                    prop="oStart"
                    label="regist_time"
                    width="195px"
                ></el-table-column>
                <el-table-column
                    prop="oEnd"
                    label="End_time"
                    width="185px"
                ></el-table-column>
                <el-table-column
                    prop="oTotalPrice"
                    label="fee/YUan"
                    width="80px"
                ></el-table-column>
                <el-table-column
                    prop="oPriceState"
                    label="payment_state"
                    width="150"
                >
                    <template slot-scope="scope">
                        <el-tag
                            type="success"
                            v-if="scope.row.oPriceState === 1"
                            >payed</el-tag
                        >
                        <!-- <el-tag type="danger" v-if="scope.row.oPriceState === 0 && scope.row.oState === 1">unpay</el-tag> -->
                        <el-button
                            type="warning"
                            icon="iconfont icon-r-mark1"
                            style="font-size: 14px"
                            v-if="
                                scope.row.oPriceState === 0 &&
                                scope.row.oState === 1
                            "
                            @click="priceClick(scope.row.oId, scope.row.dId)"
                        >
                            点击缴费</el-button
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
                            v-if="
                                scope.row.oState === 0 && scope.row.oState === 0
                            "
                            >undone</el-tag
                        >
                    </template>
                </el-table-column>
                <el-table-column label="report">
                    <template slot-scope="scope">
                        <el-button
                            type="success"
                            icon="iconfont icon-r-find"
                            style="font-size: 14px"
                            @click="seeReport(scope.row.oId)"
                            v-if="
                                scope.row.oState === 1 &&
                                scope.row.oPriceState === 1
                            "
                            > check</el-button
                        >
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <!-- 评价对话框 -->
        <el-dialog title="comment" :visible.sync="starVisible">
            <div>
                <h3>
                    remark：{{ dId }}&nbsp;doctor：{{ dName }}&nbsp;comment
                </h3>
            </div>
            <div>
                <el-rate v-model="star" show-text> </el-rate>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="starVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> No</el-button>
                <el-button type="primary" @click="starClick"style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> Yes</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
export default {
    name: "MyOrder",
    data() {
        return {
            userId: 1,
            orderData: [],
            star: 5,
            starVisible: false,
            dId: 1,
            dName: "",
        };
    },
    methods: {
        //评价点击确认
        starClick() {
            console.log(this.star);
            console.log(this.dId);
            request
                .get("doctor/updateStar", {
                    params: {
                        dId: this.dId,
                        dStar: this.star,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("Fail");
                    this.$message.success("Thanks for remark");
                    this.starVisible = false;
                });
        },
        //查看报告单
        seeReport(id) {
            window.location.href =
                "http://localhost:9281/patient/pdf?oId=" + id;
        },
        //点击缴费按钮
        priceClick(oId, dId) {
            request
                .get("order/updatePrice", {
                    params: {
                        oId: oId,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200) {
                        this.$message.error("Fail");
                        return;
                    }
                    this.$message.success("NO " + oId + " pay success！");
                    request
                        .get("admin/findDoctor", {
                            params: {
                                dId: dId,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("Fail");
                            this.dId = res.data.data.dId;
                            this.dName = res.data.data.dName;
                        });
                    this.starVisible = true;
                    this.requestOrder();
                });
        },
        //请求挂号信息
        requestOrder() {
            request
                .get("patient/findOrderByPid", {
                    params: {
                        pId: this.userId,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        this.$message.error("Fail");
                    this.orderData = res.data.data;
                    //this.orderData.dSection = res.data.data.map(item => item.doctor.dSection);
                    //console.log(res.data.data.map(item => item.doctor.dSection));
                    console.log(this.orderData.oId);
                    console.log(this.orderData.pName);
                    console.log(res);
                });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        // 解码token
        //this.orderData.pName = this.tokenDecode(getToken()).pName;
        //this.orderData.pCard = this.tokenDecode(getToken()).pCard;
        this.userId = this.tokenDecode(getToken()).pId;
        console.log(this.orderData.pName);
        //this.orderData.pName = "dasda"
        this.requestOrder();
    },
};
</script>
<style lang="scss" scoped>
.el-dialog div {
    text-align: center;
    margin-bottom: 8px;
}
</style>