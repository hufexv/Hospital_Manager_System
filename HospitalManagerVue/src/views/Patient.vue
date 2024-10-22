<template>
    <el-container>
        <!-- 头部 -->
        <el-header>
            <div class="words">
                <span
                    @click="menuClick('patientLayout')"
                    style="font-size: 22px"
                >
                    <i class="iconfont icon-r-love" style="font-size: 26px">
                        Hospital_Management_System</i
                    >
                </span>
            </div>
            <div class="words">
                <span
                    >Welcome，<b>{{ userName }}</b
                    >&nbsp;</span
                >
                <span
                    ><el-button type="danger" size="mini" @click="logout"
                        >logout</el-button
                    ></span
                >
            </div>
        </el-header>
        <el-container>
            <!-- 侧边栏 -->
            <el-aside width="200px">
                <!-- 导航菜单 -->
                <el-menu
                    background-color="white"
                    text-color="grey"
                    active-text-color="black"
                    :default-active="activePath"
                    style="font-size: 22px"
                >
                    <el-menu-item
                        index="patientLayout"
                        @click="menuClick('patientLayout')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-home" style="font-size: 26px">
                        </i>
                        home_page\
                    </el-menu-item>
                    <el-menu-item
                        index="orderOperate"
                        @click="menuClick('orderOperate')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-edit" style="font-size: 22px">
                        </i>
                        regist
                    </el-menu-item>
                    <el-menu-item
                        index="myOrder"
                        @click="menuClick('myOrder')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-list" style="font-size: 22px">
                        </i>
                        My_regist
                    </el-menu-item>
                    <el-menu-item
                        index="myBed"
                        @click="menuClick('myBed')"
                        style="font-size: 20px"
                    >
                        <i class="iconfont icon-r-home" style="font-size: 22px">
                        </i>
                        hospitaliz_info
                    </el-menu-item>
                    <el-menu-item
                        index="patientCard"
                        style="font-size: 20px"
                        @click="menuClick('patientCard')"
                    >
                        <i
                            class="iconfont icon-r-user2"
                            style="font-size: 22px"
                        ></i>
                        My_info
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-main>
                <!-- 子路由入口 -->
                <router-view></router-view>
            </el-main>
        </el-container>
    </el-container>
</template>
<script>
import jwtDecode from "jwt-decode";
import { toLoad } from "@/utils/initialize.js";
import {
    getToken,
    clearToken,
    getActivePath,
    setActivePath,
} from "@/utils/storage.js";
export default {
    name: "Patient",
    data() {
        return {
            userName: "",
            activePath: "",
        };
    },
    methods: {
        //token解码
        tokenDecode(token) {
            if (token !== null) return jwtDecode(token);
        },
        //导航栏点击事件
        menuClick(path) {
            this.activePath = path;
            setActivePath(path);
            if (this.$route.path !== "/" + path) this.$router.push(path);
            console.log(path);
        },
        //退出登录
        logout() {
            this.$confirm("logout?", "Tip", {
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                type: "warning",
            })
                .then(() => {
                    clearToken();
                    this.$message({
                        type: "success",
                        message: "success!",
                    });
                    this.$router.push("login");
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "canceled",
                    });
                });
        },
    },
    mounted() {
        toLoad();
    },
    created() {
        //  获取激活路径
        this.activePath = getActivePath();
        // 解码token
        this.userName = this.tokenDecode(getToken()).pName;
    },
};
</script>
<style scoped lang="scss">
.title {
    cursor: pointer;
}
.el-header {
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid lightgrey;
    .words {
        text-align: center;
        span {
            color: black;
        }
    }
}
.el-container {
    height: 100%;
}
.el-aside {
    background-color: white;
    border-right: 1px solid lightgrey;
}
.el-menu {
    border: 0;
}
</style>
