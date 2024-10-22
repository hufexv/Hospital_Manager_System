<template>
    <div class="login-index" :style="backgroundDiv">
        <div class="mid-index">
            <i
                style="
                    top: 40px;
                    font-size: 28px;
                    left: 20px;
                    position: absolute;
                "
                class="iconfont icon-r-love"
            >
                Hospital_Management_System
            </i>
            <el-form
                :model="loginForm"
                :rules="loginRules"
                ref="ruleForm"
                class="loginForm"
            >
                <el-form-item prop="id">
                    <!--必须绑定v-model输入框才能输入字符---->
                    <el-input v-model="loginForm.id">
                        <i
                            slot="prefix"
                            class="iconfont icon-r-user1"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="loginForm.password" show-password>
                        <i
                            slot="prefix"
                            class="iconfont icon-r-lock"
                            style="font-size: 22px"
                        ></i>
                    </el-input>
                </el-form-item>

                <!-- 角色单选框 -->
                <el-form-item class="role">
                    <el-radio-group v-model="role" size="small">
                        <el-radio label="Patient"></el-radio>
                        <el-radio label="Doctor"></el-radio>
                        <el-radio label="Admin"></el-radio>
                    </el-radio-group>
                </el-form-item>

                <el-form-item class="btns">
                    <el-button
                        type="primary"
                        style="font-size: 18px"
                        @click="submitLoginForm('ruleForm')"
                    >
                        <i
                            class="iconfont icon-r-yes"
                            style="font-size: 20px"
                        ></i>
                        Login</el-button
                    >
                    <el-button
                        type="info"
                        style="font-size: 18px"
                        @click="registerFormVisible = true"
                    >
                        <i
                            class="iconfont icon-r-add"
                            style="font-size: 20px"
                        ></i>
                        sign_in</el-button
                    >
                </el-form-item>
            </el-form>
        </div>

        <!-- 注册对话框 -->
        <el-dialog title="sign_in" :visible.sync="registerFormVisible">
            <el-form
                class="findPassword"
                :model="registerForm"
                :rules="registerRules"
                ref="registerForm"
            >
                <el-form-item label="Account" label-width="80px" prop="pId">
                    <el-input v-model.number="registerForm.pId"></el-input>
                </el-form-item>
                <el-form-item label="Gender" label-width="80px">
                    <el-radio v-model="registerForm.pGender" label="male"
                        >male</el-radio
                    >
                    <el-radio v-model="registerForm.pGender" label="female"
                        >female</el-radio
                    >
                </el-form-item>
                <el-form-item label="password" label-width="80px" prop="pPassword">
                    <el-input v-model="registerForm.pPassword"></el-input>
                </el-form-item>
                <el-form-item label="name" label-width="80px" prop="pName">
                    <el-input v-model="registerForm.pName"></el-input>
                </el-form-item>
                <el-form-item
                    label="brithday"
                    label-width="80px"
                    prop="pBirthday"
                >
                    <el-date-picker
                        v-model="registerForm.pBirthday"
                        type="date"
                        placeholder="date_select"
                        value-format="yyyy-MM-dd"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="phone" label-width="80px" prop="pPhone">
                    <el-input v-model="registerForm.pPhone"></el-input>
                </el-form-item>
                <el-form-item label="Email" label-width="80px" prop="pEmail">
                    <el-input v-model="registerForm.pEmail"></el-input>
                </el-form-item>
                <el-form-item label="ID" label-width="80px" prop="pCard">
                    <el-input v-model="registerForm.pCard"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button
                    @click="registerFormVisible = false"
                    style="font-size: 18px"
                    ><i
                        class="iconfont icon-r-left"
                        style="font-size: 20px"
                    ></i>
                    cancel</el-button
                >
                <el-button
                    type="primary"
                    @click="registerClick('registerForm')"
                    style="font-size: 18px"
                    ><i class="iconfont icon-r-yes" style="font-size: 20px"></i>
                    Yes</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>

<script>
import request from "@/utils/request.js";
import { setToken } from "@/utils/storage.js";
import { toLoad } from "@/utils/initialize.js";

export default {
    name: "Login",
    data() {
        var validateMoblie = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("phone please"));
            } else {
                let reg =
                    /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                if (!reg.test(value)) {
                    callback(new Error("illegal phone"));
                }
                callback();
            }
        };
        var validateCard = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("ID please"));
            } else {
                let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
                if (!reg.test(value)) {
                    callback(new Error("illegal ID"));
                }
                callback();
            }
        };
        var validatePass = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("password"));
            } else {
                if (this.findForm.checkPassword !== "") {
                    this.$refs.findForm.validateField("checkPassword");
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("password check"));
            } else if (value !== this.findForm.newPassword) {
                callback(new Error("wrong"));
            } else {
                callback();
            }
        };
        return {
            //背景图片
            backgroundDiv: {
                backgroundImage:
                    "url(" + require("../assets/doctor.jpeg") + ")",
                backgroundRepeat: "no-repeat",
                backgroundSize: "100% 100%",
            },
            loginForm: {
                id: "",
                password: "",
            },
            loginRules: {
                id: [
                    {
                        required: true,
                        message: "account",
                        trigger: "blur",
                    },
                    {
                        min: 1,
                        max: 50,
                        message: "1-50char",
                        trigger: "blur",
                    },
                ],
                password: [
                    { required: true, message: "password", trigger: "blur" },
                ],
            },
            role: "patient",
            findRole: "patient",
            //找回密码
            findFormVisible: false,
            findForm: {
                code: "",
                newPassword: "",
                checkPassword: "",
                pEmail: "",
            },

            findRules: {
                pEmail: [
                    {
                        required: true,
                        message: "Email",
                        trigger: "blur",
                    },
                    {
                        type: "email",
                        message: "wrong email",
                        trigger: ["blur", "change"],
                    },
                ],
                code: [
                    {
                        required: true,
                        message: "verify code",
                        trigger: "blur",
                    },
                ],
                newPassword: [{ validator: validatePass, trigger: "blur" }],
                checkPassword: [{ validator: validatePass2, trigger: "blur" }],
            },
            totalTime: 60,
            content: "send verify code",
            canClick: true,
            //注册
            registerFormVisible: false,
            registerForm: {
                pGender: "male",
            },
            registerRules: {
                pId: [
                    { required: true, message: "account", trigger: "blur" },
                    {
                        type: "number",
                        message: "number",
                        trigger: "blur",
                    },
                ],
                pPassword: [
                    { required: true, message: "password", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "1-50char",
                        trigger: "blur",
                    },
                ],
                pName: [
                    { required: true, message: "name", trigger: "blur" },
                    {
                        min: 1,
                        max: 8,
                        message: "1-8char",
                        trigger: "blur",
                    },
                ],
                pEmail: [
                    { required: true, message: "Email", trigger: "blur" },
                    {
                        type: "email",
                        message: "wrong",
                        trigger: ["blur", "change"],
                    },
                ],
                pPhone: [{ validator: validateMoblie }],
                pCard: [{ validator: validateCard }],
                pBirthday: [
                    {
                        required: true,
                        message: "brithday",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    mounted() {
        toLoad()
    },
    methods: {
        //点击注册确认按钮
        registerClick(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("patient/addPatient", {
                            params: {
                                pId: this.registerForm.pId,
                                pName: this.registerForm.pName,
                                pPassword: this.registerForm.pPassword,
                                pGender: this.registerForm.pGender,
                                pEmail: this.registerForm.pEmail,
                                pPhone: this.registerForm.pPhone,
                                pCard: this.registerForm.pCard,
                                pBirthday: this.registerForm.pBirthday,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "Occupied！"
                                );
                            this.registerFormVisible = false;
                            this.$message.success("success！");
                            console.log(res);
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        // 点击找回密码确认按钮
        findPassword(findForm) {
            this.$refs[findForm].validate((valid) => {
                if (valid) {
                    //如果是选中患者
                    if (this.findRole === "patient") {
                        request
                            .get("patient/findPassword", {
                                params: {
                                    pEmail: this.findForm.pEmail,
                                    pPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "wrong！！！"
                                    );
                                this.$message.success("success");
                                this.findFormVisible = false;
                            });
                    }
                    //如果是选中管理员
                    if (this.findRole === "Admin") {
                        request
                            .get("admin/findPassword", {
                                params: {
                                    aEmail: this.findForm.pEmail,
                                    aPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "wrong！！！"
                                    );
                                this.$message.success("success");
                                this.findFormVisible = false;
                            });
                    }
                    //如果是选中患者
                    if (this.findRole === "Doctor") {
                        request
                            .get("doctor/findPassword", {
                                params: {
                                    dEmail: this.findForm.pEmail,
                                    dPassword: this.findForm.newPassword,
                                    code: this.findForm.code,
                                },
                            })
                            .then((res) => {
                                if (res.data.status !== 200)
                                    return this.$message.error(
                                        "wrong"
                                    );
                                this.$message.success("success");
                                this.findFormVisible = false;
                            });
                    }
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //点击发送验证码按钮
        sendEmail() {
            //倒计时
            if (!this.canClick) return; //改动的是这两行代码
            this.canClick = false;
            this.content = this.totalTime + "s resend";
            let clock = window.setInterval(() => {
                this.totalTime--;
                this.content = this.totalTime + "s resend";
                if (this.totalTime < 0) {
                    window.clearInterval(clock);
                    this.content = "resend";
                    this.totalTime = 10;
                    this.canClick = true; //这里重新开启
                }
            }, 1000);

            //如果是选中患者
            if (this.findRole === "Patient") {
                request
                    .get("patient/sendEmail", {
                        params: {
                            pEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "regist_first！"
                            );
                        this.$message.success("success");
                    });
            }
            //如果是选中管理员
            if (this.findRole === "Admin") {
                request
                    .get("admin/sendEmail", {
                        params: {
                            aEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "regist_first！"
                            );
                        this.$message.success("success！");
                    });
            }
            //如果是选中医生
            if (this.findRole === "Doctor") {
                request
                    .get("doctor/sendEmail", {
                        params: {
                            dEmail: this.findForm.pEmail,
                        },
                    })
                    .then((res) => {
                        console.log(this.findForm.pEmail);
                        console.log(res);
                        if (res.data.status !== 200)
                            return this.$message.error(
                                "regist_first！"
                            );
                        this.$message.success("success");
                    });
            }
        },
        //提交表单
        submitLoginForm(formName) {
            if (!/^\d+$/.test(this.loginForm.id)) {
                this.$message.error("wrong name");
                return;
            }
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if (this.role === "Admin") {
                        var params = new URLSearchParams();
                        params.append("aId", this.loginForm.id);
                        params.append("aPassword", this.loginForm.password);

                        request
                            .post("admin/login", params)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "wrong account or password"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/adminLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "Doctor") {
                        var params1 = new URLSearchParams();
                        params1.append("dId", this.loginForm.id);
                        params1.append("dPassword", this.loginForm.password);

                        request
                            .post("doctor/login", params1)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "wrong account or password"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/doctorLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                    if (this.role === "Patient") {
                        var params2 = new URLSearchParams();
                        params2.append("pId", this.loginForm.id);
                        params2.append("pPassword", this.loginForm.password);

                        request
                            .post("patient/login", params2)
                            .then((res) => {
                                console.log(res);
                                if (res.data.status != 200)
                                    return this.$message.error(
                                        "wrong account or password"
                                    );
                                setToken(res.data.data.token);
                                this.$router.push("/patientLayout");
                            })
                            .catch((e) => {
                                console.log(e);
                                if (
                                    e.response == undefined ||
                                    e.response.data == undefined
                                ) {
                                    this.$message({
                                        showClose: true,
                                        message: e,
                                        type: "error",
                                        duration: 5000,
                                    });
                                } else {
                                    this.$message({
                                        showClose: true,
                                        message: e.response.data,
                                        type: "error",
                                        duration: 5000,
                                    });
                                }
                            });
                    }
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
    }
};
</script>

<style lang="scss">
.codeInput {
    width: 70%;
    margin-right: 10px;
}
.findPassword {
    margin-top: 0px;
}
.login-index {
    background: #2b4b6b;
    height: 100%;
    position: relative;
}
.mid-index {
    opacity: 0.9;
    width: 450px;
    height: 390px;
    background: white;
    //绝对定位，相对于最左上角来说
    position: absolute;
    left: 70%;
    top: 50%;
    transform: translate(-50%, -50%);
}
.logo-index {
    background: white;
    height: 130px;
    width: 130px;
    border-radius: 50%;
    padding: 10px;
    //子绝父相,使一个div悬挂在另一个div上中间
    position: absolute;
    left: 50%;
    top: 0;
    transform: translate(-50%, -50%);

    border: 1px solid #eee;
    box-shadow: 0px 0px 10px #ddd;

    img {
        height: 100%;
        width: 100%;
        border-radius: 50%;
        background: #eeeeee;
    }
}

.loginForm {
    margin-top: 120px;
}
.el-form-item {
    margin-left: 20px;
    margin-right: 20px;
}
//角色单选
.role {
    margin-left: 90px;
    margin-right: 90px;
}
//按钮
.btns {
    display: flex;
    justify-content: flex-end;
    height: 25px;
}
</style>
