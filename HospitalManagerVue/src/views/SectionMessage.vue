<template>
    <div>
        <!-- 卡片 -->
        <el-card>
            <!-- 面包屑 -->
            <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/orderOperate' }"
                    >department_select</el-breadcrumb-item
                >
                <el-breadcrumb-item>date_select</el-breadcrumb-item>
                <el-breadcrumb-item>regist</el-breadcrumb-item>
            </el-breadcrumb>

            <!-- 两边布局 -->
            <div class="head">
                <div>
                    <i
                        class="iconfont icon-r-user1"
                        style="margin: 5px; font-size: 26px"
                    >
                        {{ sectionOpt }}doctor_list</i
                    >
                </div>

                <!-- 选择挂号时间 -->
                <div>
                    <i class="iconfont icon-r-paper" style="font-size: 22px">
                        regist_date：</i
                    >
                    <ul class="dateUl">
                        <li v-for="monthDay in monthDays" :key="monthDay">
                            <el-button
                                icon="iconfont icon-r-paper"
                                @click="dateClick(monthDay)"
                            >
                                {{ monthDay }}</el-button
                            >
                        </li>
                    </ul>
                </div>
            </div>

            <!-- 表格 -->
            <el-table :data="sectionData" stripe style="width: 100%" border>
                <el-table-column
                    type="index"
                    label="NO"
                    width="60"
                ></el-table-column>
                <el-table-column prop="dId" label="NO" width="80">
                </el-table-column>
                <el-table-column prop="dName" label="name" width="80">
                </el-table-column>
                <el-table-column prop="dGender" label="gender" width="60">
                </el-table-column>
                <el-table-column prop="dPost" label="position" width="100">
                </el-table-column>
                <el-table-column
                    prop="dSection"
                    label="department"
                    width="100"
                ></el-table-column>
                <el-table-column prop="dIntroduction" label="brief">
                </el-table-column>
                <el-table-column prop="dPrice" label="fee/Yuan" width="80">
                </el-table-column>
                <el-table-column prop="dAvgStar" label="rate/5star" width="80">
                </el-table-column>
                <el-table-column label="Operation" width="140" v-if="clickTag">
                    <template slot-scope="scope">
                        <el-button
                            class="iconfont icon-r-paper"
                            style="font-size: 14px"
                            type="warning"
                            @click="openClick(scope.row.dId, scope.row.dName)"
                        >
                            regist</el-button
                        >
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <!-- 挂号对话框 -->
        <el-dialog title="regist_info" :visible.sync="orderFormVisible">
            <el-form :model="orderForm" ref="orderForm" :rules="orderRules">
                <el-form-item
                    label="regist_date"
                    label-width="100px"
                    prop="oTime"
                >
                    <el-select
                        v-model="orderForm.oTime"
                        placeholder="select"
                        no-data-text="try_another_day"
                    >
                        <el-option
                            v-for="time in times"
                            :key="time"
                            :label="time"
                            :value="time"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="regist_date" label-width="100px">
                    <el-input
                        v-model="orderForm.orderDate"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="NO" label-width="100px">
                    <el-input
                        v-model="orderForm.dId"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="doctor_name" label-width="100px">
                    <el-input
                        v-model="orderForm.dName"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="patient_name" label-width="100px">
                    <el-input
                        v-model="orderForm.pName"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="patient_ID" label-width="100px">
                    <el-input
                        v-model="orderForm.pCard"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button
                    @click="orderFormVisible = false"
                    style="font-size: 18px"
                    ><i
                        class="iconfont icon-r-left"
                        style="font-size: 20px"
                    ></i>
                    No</el-button
                >
                <el-button
                    type="primary"
                    @click="orderSuccess('orderForm')"
                    style="font-size: 18px"
                    ><i class="iconfont icon-r-yes" style="font-size: 20px"></i>
                    Yes</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import request from "@/utils/request.js";
import { getToken } from "@/utils/storage.js";
export default {
    name: "sectionMessage",
    data() {
        return {
            sectionOpt: this.$route.query.sectionOpt,
            sectionData: [],
            monthDays: [],
            clickTag: false,
            orderFormVisible: false,
            orderForm: { orderDate: "" },
            times: [],
            orderRules: {
                oTime: [
                    { required: true, message: "date_select", trigger: "blur" },
                ],
            },
            //挂号日期
            orderDate: "",
            //拼接时间和日期成为oId
            idTime: "",
        };
    },
    methods: {
        //打开挂号对话框触发,获取挂号时间段已剩余票数
        requestTime(id) {
            this.idTime = id + this.orderDate;
            request
                .get("order/findOrderTime", {
                    params: {
                        arId: this.idTime,
                    },
                })
                .then((res) => {
                    const date = new Date(this.orderDate);
                    const today = new Date();
                    const isToday =
                        date.getFullYear() === today.getFullYear() &&
                        date.getMonth() === today.getMonth() &&
                        date.getDate() === today.getDate();
                    var array = [];
                    if (!this.isTimeAfterTarget("09:30") || !isToday) {
                        array.push(
                            "08:30-09:30  " + "   res " + res.data.data.eTOn
                        );
                    }
                    if (!this.isTimeAfterTarget("10:30") || !isToday) {
                        array.push(
                            "09:30-10:30  " + "   res " + res.data.data.nTOt
                        );
                    }
                    if (!this.isTimeAfterTarget("11:30") || !isToday) {
                        array.push(
                            "10:30-11:30  " + "   res " + res.data.data.tTOe
                        );
                    }
                    if (!this.isTimeAfterTarget("15:30") || !isToday) {
                        array.push(
                            "14:30-15:30  " + "   res " + res.data.data.fTOf
                        );
                    }
                    if (!this.isTimeAfterTarget("16:30") || !isToday) {
                        array.push(
                            "15:30-16:30  " + "   res " + res.data.data.fTOs
                        );
                    }
                    if (!this.isTimeAfterTarget("17:30") || !isToday) {
                        array.push(
                            "16:30-17:30  " + "   res " + res.data.data.sTOs
                        );
                    }
                    this.times = array;
                });
        },
        isTimeAfterTarget(timeString) {
            // 判断时间是否超过timeString(入参格式如：09:30)
            const currentTime = new Date();

            // 解析传入的目标时间字符串，获取小时和分钟
            const [targetHour, targetMinute] = timeString.split(":");

            // 设置要比较的时间
            const targetTime = new Date();
            targetTime.setHours(targetHour);
            targetTime.setMinutes(targetMinute);
            targetTime.setSeconds(0);

            // 比较当前时间是否超过了目标时间
            return currentTime > targetTime;
        },
        //挂号点击确认
        orderSuccess(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("patient/addOrder", {
                            params: {
                                pId: this.orderForm.pId,
                                dId: this.orderForm.dId,
                                oStart:
                                    this.orderForm.orderDate +
                                    " " +
                                    this.orderForm.oTime,
                                arId: this.idTime,
                            },
                        })
                        .then((res) => {
                            if (res.data.status != 200)
                                return this.$message.error(
                                    "None"
                                );
                            this.orderFormVisible = false;
                            this.$message.success("success");
                            this.orderForm.oTime = "";
                        })
                        .catch((e) => {
                            if (e.response.data == undefined) {
                                this.$message.error(e);
                            } else {
                                this.$message.error(e.response.data);
                            }
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        //打开挂号对话框
        openClick(id, name) {
            this.orderForm.dId = id;
            this.orderForm.dName = name;
            this.orderFormVisible = true;
            //请求挂号时间段
            this.requestTime(id);
        },
        //选择日期触发时间
        dateClick(date) {
            //获取挂号年月日
            const nowDate = new Date();
            let year = nowDate.getFullYear();
            this.orderForm.orderDate = year + "-" + date;
            let dateYear = year + "-" + date;
            this.orderDate = dateYear;
            request
                .get("/arrange/findByTime", {
                    params: {
                        arTime: dateYear,
                        dSection: this.sectionOpt,
                    },
                })
                .then((res) => {
                    //this.sectionData.dId = res.data.data.doctors.dId;
                    /**
                     * 重点！！！把数组中的对象取出来用map
                     */
                    this.sectionData = res.data.data.map((item) => item.doctor);
                    this.clickTag = true;
                    console.log(res.data.data.map((item) => item.doctor));
                    //console.log(res.data.data[0].doctor);
                });
        },
        //获取当天及后7天的日期星期
        nowDay(num) {
            var nowDate = new Date();
            var currentHour = nowDate.getHours();
            var currentMinute = nowDate.getMinutes();

            // 判断当前时间是否已经过了17:30
            if (
                currentHour > 17 ||
                (currentHour === 17 && currentMinute > 30)
            ) {
                num++; // 次日
            }

            nowDate.setDate(nowDate.getDate() + num);
            var month = nowDate.getMonth() + 1;
            var date = nowDate.getDate();
            if (date < 10) {
                date = "0" + date;
            }
            if (month < 10) {
                month = "0" + month;
            }
            var time = month + "-" + date;
            this.monthDays.push(time);
        },
        //请求部门医生信息
        requestSection() {
            request
                .get("patient/findDoctorBySection", {
                    params: {
                        dSection: this.$route.query.sectionOpt,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("请求数据失败");
                    this.sectionData = res.data.data.doctors;
                    console.log(res.data.data.doctors);
                });
        },
    },
    created() {
        //获取当天的后7天
        for (var i = 0; i < 7; i++) {
            this.nowDay(i);
        }
        //按科室请求医生信息
        this.requestSection();
        // 解码token
        this.orderForm.pName = this.tokenDecode(getToken()).pName;
        this.orderForm.pCard = this.tokenDecode(getToken()).pCard;
        this.orderForm.pId = this.tokenDecode(getToken()).pId;
        //console.log(this.orderForm.pId)
    },
};
</script>
<style scoped lang="scss">
.dateUl li {
    display: inline;
    //margin: 5px;
    padding: 1px;
}
.dateUl {
    margin: 10px;
}
.el-breadcrumb {
    margin: 8px;
}
.head {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.el-form {
    margin-top: 0;
}
</style>