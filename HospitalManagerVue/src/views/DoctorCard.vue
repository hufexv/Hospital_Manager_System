<template>
    <div style="width: 100%;margin-top: -10px">
        <el-card shadow="hover">
            <table>
                <tr>
                    <td style="">name：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dName"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>account：</td>
                    <td>
                        <el-input disabled v-model="doctorData.dId"></el-input>
                    </td>
                </tr>
                <tr>
                    <td>gender：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dGender"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>phone_num：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dPhone"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>ID：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dCard"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>Email：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dEmail"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>职位：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dPost"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>department：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dSection"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>Regist_price：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dPrice"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>rate：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dAvgStar"
                        ></el-input>
                    </td>
                </tr>
                <tr>
                    <td>brief：</td>
                    <td>
                        <el-input
                            disabled
                            v-model="doctorData.dIntroduction"
                            type="textarea"
                            :rows="4"
                        ></el-input>
                    </td>
                </tr>
            </table>
        </el-card>
    </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken } from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
    name: "DoctorCard",
    data() {
        return {
            userId: "",
            doctorData: {},
        };
    },
    methods: {
        //请求医生信息
        requestDoctor() {
            request
                .get("admin/findDoctor", {
                    params: {
                        dId: this.userId,
                    },
                })
                .then((res) => {
                    if (res.data.status != 200)
                        return this.$message.error("Fail");
                    this.doctorData = res.data.data;
                });
        },
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
    },
    created() {
        //解码token信息
        this.userId = this.tokenDecode(getToken()).dId;
        this.requestDoctor();
        console.log(this.userId);
    },
};
</script>
<style lang="scss" scope>
td, th {
  white-space: nowrap;
  padding: 10px;
}
</style>