<template>
    <div>
        <!-- <div class="indexImage">
        <img src="@/assets/hospital.jpeg" class="layoutImage"/>
        <span>register_num：{{orderPeople}}</span>
      </div> -->
        <div class="indexPeople">
            <div class="userImage">
                <i class="iconfont icon-r-user2" style="font-size: 132px"></i>
            </div>

            <div class="userFont">
                <div class="spanCure">
                    <span>treatment_overview</span>
                </div>
                <div class="spanPeople">
                    <span>register_num：{{ orderPeople }}</span>
                </div>
            </div>
        </div>
        <div class="indexPeople">
            <div class="userImage">
                <i class="iconfont icon-r-home" style="font-size: 132px"></i>
            </div>

            <div class="userFont">
                <div class="spanCure">
                    <span>hospitaliz_overview</span>
                </div>
                <div class="spanPeople">
                    <span>hospitaliz_number：{{ bedPeople }}</span>
                </div>
            </div>
        </div>

        <el-row>
            <el-col :span="24">
                <img src="@/assets/back.jpg" style="width: 941px;margin-left: 25px;">
            </el-col>
        </el-row>
    </div>
</template>
<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
    name: "PatientLayout",
    data() {
        return {
            orderPeople: 1,
            bedPeople: 1,
        };
    },
    methods: {
        requestPeople() {
            request
                .get("order/orderPeople")
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("Fail");
                    this.orderPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
        requestBed() {
            request
                .get("bed/bedPeople")
                .then((res) => {
                    toLoad()
                    if (res.data.status !== 200)
                        return this.$message.error("Fail");
                    this.bedPeople = res.data.data;
                })
                .catch((err) => {
                    console.error(err);
                });
        },
    },
    created() {
        this.requestPeople();
        this.requestBed();
    },
};
</script>
<style lang="scss" scoped>
.userFont {
    height: 150px;
    width: 250px;
    float: right;
    color: white;
    .spanCure {
        font-size: 15px;
        margin-top: 60px;
        margin-bottom: 15px;
    }
    .spanPeople {
        font-size: 18px;
    }
}

.userImage {
    height: 150px;
    width: 150px;
    font-size: 130px;
    color: white;
    position: relative;
    left: 40px;
    top: 10px;
    float: left;
}
.indexPeople {
    height: 200px;
    width: 440px;
    background: #58b9ae;
    float: left;
    margin: 30px;
}
</style>