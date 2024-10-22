<template>
    <!-- 卡片 -->
    <el-card>
        <!-- 搜索栏及增加医生 -->
        <el-row type="flex">
            <el-col :span="8">
                <el-input v-model="query" placeholder="Search_by_name">
                    <el-button
                        slot="append"
                        style="font-size: 18px;"
                        @click="requestDoctors"
                    ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> Sreach</el-button>
                </el-input>
            </el-col>
            <el-col :span="4"></el-col>
            <el-col :span="4">
                <el-button type="primary" @click="addFormVisible = true"
                        style="font-size: 18px;"
                    >
                    <i class="iconfont icon-r-add" style="font-size: 22px;"></i> 
                    increase</el-button
                >
            </el-col>
            <el-col :span="4"></el-col>
            <!-- 导入数据 -->
            <!-- <el-col :span="2">
                <el-upload
                    class="upload-demo"
                    action="doctor/uploadExcel"
                    accept=".xlsx,.xls "
                    :limit="1"
                    :show-file-list="false"
                    :on-progress="handleProgress"
                    :on-exceed="handleExceed"
                    :on-success="handleSuccess"
                    :on-error="handleError"
                    :file-list="fileList"
                >
                    <el-button size="small" type="success">export</el-button>
                </el-upload>
            </el-col>

            <el-col :span="2">
                <el-button size="small" type="success" @click="exportDoctors"
                    >export</el-button
                ></el-col
            > -->
        </el-row>
        <!-- 表格 -->
        <el-table :data="doctorData" stripe style="width: 100%" border>
            <el-table-column prop="dId" label="account" width="100">
            </el-table-column>
            <el-table-column prop="dName" label="name" width="80">
            </el-table-column>
            <el-table-column prop="dGender" label="gender" width="60">
            </el-table-column>
            <el-table-column prop="dPost" label="position" width="100">
            </el-table-column>
            <el-table-column prop="dSection" label="department：" width="100">
            </el-table-column>
            <el-table-column prop="dCard" label="ID">
            </el-table-column>
            <el-table-column prop="dPhone" label="phone_num">
            </el-table-column>
            <el-table-column prop="dEmail" label="Email" width="170">
            </el-table-column>
            <el-table-column prop="dAvgStar" label="rate/5" width="80">
            </el-table-column>
            <el-table-column prop="dPrice" label="fee/Yuan" width="100">
            </el-table-column>
            <el-table-column prop="dState" label="working" width="80">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.dState === 1"
                        >Yes</el-tag
                    >
                    <el-tag type="danger" v-else>No</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="Operation" width="240" fixed="right">
                <template slot-scope="scope">
                    <el-button
                        style="font-size: 18px;"
                        type="success"
                        @click="modifyDialog(scope.row.dId)"
                    ><i class="iconfont icon-r-edit" style="font-size: 22px;"></i> edit</el-button>
                    <el-button
                        style="font-size: 18px;"
                        type="danger"
                        @click="deleteDialog(scope.row.dId)"
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
        <!-- 增加医生对话框 -->
        <el-dialog title="increase_doctor" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="account" label-width="80px" prop="dId">
                    <el-input
                        v-model.number="addForm.dId"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="password" label-width="80px">
                    <el-input
                        v-model="addForm.dPassword"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="name" label-width="80px" prop="dName">
                    <el-input
                        v-model="addForm.dName"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="gender" label-width="80px">
                    <el-radio v-model="addForm.dGender" label="male">male</el-radio>
                    <el-radio v-model="addForm.dGender" label="female">female</el-radio>
                </el-form-item>
                <el-form-item label="position" label-width="80px" prop="dPost">
                    <el-select v-model="addForm.dPost" placeholder="select_posi">
                        <el-option
                            v-for="post in posts"
                            :key="post"
                            :label="post"
                            :value="post"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="department" label-width="80px" prop="dSection">
                    <el-select
                        v-model="addForm.dSection"
                        filterable
                        placeholder="select_depar"
                    >
                        <el-option
                            v-for="section in sections"
                            :key="section"
                            :label="section"
                            :value="section"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="ID" label-width="80px" prop="dCard">
                    <el-input
                        v-model="addForm.dCard"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="phone_num" label-width="80px" prop="dPhone">
                    <el-input
                        v-model="addForm.dPhone"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="Email" label-width="80px" prop="dEmail">
                    <el-input
                        v-model="addForm.dEmail"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="Regist_price" label-width="80px" prop="dPrice">
                    <el-input
                        v-model="addForm.dPrice"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="brief"
                    label-width="80px"
                    prop="dIntroduction"
                >
                    <el-input
                        type="textarea"
                        :rows="5"
                        placeholder="content"
                        v-model="addForm.dIntroduction"
                    >
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="addDoctor('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button
                >
            </div>
        </el-dialog>

        <!-- 修改医生对话框 -->
        <el-dialog title="update_doctor_info" :visible.sync="modifyFormVisible">
            <el-form :model="modifyForm" :rules="rules" ref="ruleForm">
                <el-form-item label="account" label-width="80px" prop="dId">
                    <el-input
                        v-model.number="modifyForm.dId"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>

                <el-form-item label="name" label-width="80px" prop="dName">
                    <el-input
                        v-model="modifyForm.dName"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="gender" label-width="80px">
                    <el-radio v-model="modifyForm.dGender" label="male"
                        >male</el-radio
                    >
                    <el-radio v-model="modifyForm.dGender" label="female"
                        >female</el-radio
                    >
                </el-form-item>
                <el-form-item label="position" label-width="80px" prop="dPost">
                    <el-select
                        v-model="modifyForm.dPost"
                        placeholder="select_posi"
                    >
                        <el-option
                            v-for="post in posts"
                            :key="post"
                            :label="post"
                            :value="post"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="department" label-width="80px" prop="dSection">
                    <el-select
                        v-model="modifyForm.dSection"
                        filterable
                        placeholder="select_depar"
                    >
                        <el-option
                            v-for="section in sections"
                            :key="section"
                            :label="section"
                            :value="section"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="ID" label-width="80px" prop="dCard">
                    <el-input
                        v-model="modifyForm.dCard"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="phone_num" label-width="80px" prop="dPhone">
                    <el-input
                        v-model="modifyForm.dPhone"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="Email" label-width="80px" prop="dEmail">
                    <el-input
                        v-model="modifyForm.dEmail"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item label="Regist_price" label-width="80px" prop="dPrice">
                    <el-input
                        v-model="modifyForm.dPrice"
                        autocomplete="off"
                    ></el-input>
                </el-form-item>
                <el-form-item
                    label="brief"
                    label-width="80px"
                    prop="dIntroduction"
                >
                    <el-input
                        type="textarea"
                        :rows="5"
                        placeholder="content"
                        v-model="modifyForm.dIntroduction"
                    >
                    </el-input>
                </el-form-item>
                <el-form-item label="state" label-width="80px" prop="dState">
                    <el-input
                        v-model="modifyForm.dState"
                        autocomplete="off"
                        disabled
                    ></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="modifyFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> No</el-button>
                <el-button type="primary" @click="modifyDoctor('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> Yes</el-button
                >
            </div>
        </el-dialog>
    </el-card>
</template>
<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
    name: "DoctorList",
    data() {
        var validateMoblie = (rule, value, callback) => {
            if (value === undefined) {
                callback(new Error("Phone Number please"));
            } else {
                let reg =
                    /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
                if (!reg.test(value)) {
                    callback(new Error("illegal number"));
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
        return {
            //文件上传
            fileList: [],
            pageNumber: 1,
            size: 8,
            query: "",
            doctorData: [],
            total: 3,
            addFormVisible: false,
            addForm: {
                dPassword: 123456,
                dGender: "male",
            },
            posts: ["主任医师", "副主任医师", "主治医生"],
            sections: [
                "神经内科",
                "内分泌科",
                "呼吸与危重症医学科",
                "消化内科",
                "心血管内科",
                "发热门诊",
                "手足外科",
                "普通外科",
                "肛肠外科",
                "神经外科",
                "骨科",
                "烧伤整形外科",
                "妇科",
                "产科",
                "儿科",
                "耳鼻咽喉科",
                "眼科",
                "中医科",
                "急诊科",
                "皮肤病科",
                "口腔科",
            ],
            rules: {
                dId: [
                    { required: true, message: "account please", trigger: "blur" },
                    {
                        type: "number",
                        message: "number please",
                        trigger: "blur",
                    },
                ],
                dName: [
                    { required: true, message: "name please", trigger: "blur" },
                    {
                        min: 2,
                        max: 5,
                        message: "2-5char",
                        trigger: "blur",
                    },
                ],
                dPost: [
                    { required: true, message: "position please", trigger: "blur" },
                ],
                dSection: [
                    {
                        required: true,
                        message: "department",
                        trigger: "blur",
                    },
                ],
                dEmail: [
                    { required: true, message: "Email", trigger: "blur" },
                    {
                        type: "email",
                        message: "wrong email",
                        trigger: ["blur", "change"],
                    },
                ],
                dPrice: [
                    {
                        required: true,
                        message: "Regist_price",
                        trigger: "blur",
                    },
                ],
                dPhone: [{ validator: validateMoblie }],
                dCard: [{ validator: validateCard }],
                dIntroduction: [
                    {
                        required: true,
                        message: "brief",
                        trigger: "blur",
                    },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        //导出医生信息
        exportDoctors() {
            window.location.href = "http://localhost:9281/doctor/downloadExcel";
        },
        //文件上传
        handleProgress() {
            this.$message.warning("文件正在解析中！");
        },
        // 文件上传成功时的钩子
        handleSuccess() {
            this.$message.success("数据导入成功！");
            this.requestDoctors();
        },
        handleError() {
            //this.$message.error("数据导入失败！");
            this.$message.success("数据导入成功！");
            this.requestDoctors();
        },
        handleExceed() {
            this.$message.warning("当前限制选择 1 个文件");
        },
        //点击修改医生信息
        modifyDoctor(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("admin/modifyDoctor", {
                            params: {
                                dId: this.modifyForm.dId,
                                dGender: this.modifyForm.dGender,
                                dName: this.modifyForm.dName,
                                dPost: this.modifyForm.dPost,
                                dSection: this.modifyForm.dSection,
                                dPhone: this.modifyForm.dPhone,
                                dEmail: this.modifyForm.dEmail,
                                dCard: this.modifyForm.dCard,
                                dPrice: this.modifyForm.dPrice,
                                dIntroduction: this.modifyForm.dIntroduction,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("Fail！");
                            this.modifyFormVisible = false;
                            this.requestDoctors();
                            this.$message.success("success！");
                            console.log(res);
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //打开修改对话框
        modifyDialog(id) {
            request
                .get("admin/findDoctor", {
                    params: {
                        dId: id,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        this.$message.error("Fail");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                    console.log(res);
                });
        },
        //删除医生操作
        deleteDoctor(id) {
            request
                .get("admin/deleteDoctor", {
                    params: {
                        dId: id,
                    },
                })
                .then((res) => {
                    this.requestDoctors();
                    console.log(res);
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("delete_doctor_info?", "Tip", {
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                type: "warning",
            })
                .then(() => {
                    this.deleteDoctor(id);
                    this.$message({
                        type: "success",
                        message: "success",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "canceled",
                    });
                });
        },
        // 增加医生
        addDoctor(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("admin/addDoctor", {
                            params: {
                                dId: this.addForm.dId,
                                dGender: this.addForm.dGender,
                                dPassword: this.addForm.dPassword,
                                dName: this.addForm.dName,
                                dPost: this.addForm.dPost,
                                dSection: this.addForm.dSection,
                                dPhone: this.addForm.dPhone,
                                dEmail: this.addForm.dEmail,
                                dCard: this.addForm.dCard,
                                dPrice: this.addForm.dPrice,
                                dIntroduction: this.addForm.dIntroduction,
                                doctor: this.addForm,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "illegal account"
                                );
                            this.addFormVisible = false;
                            this.requestDoctors();
                            this.$message.success("success");
                            console.log(res);
                        });
                } else {
                    console.log("error submit!!");
                    return false;
                }
            });
        },
        //页面大小改变时触发
        handleSizeChange(size) {
            this.size = size;
            this.requestDoctors();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestDoctors();
        },
        // 加载医生列表
        requestDoctors() {
            request
                .get("admin/findAllDoctors", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.doctorData = res.data.data.doctors;

                    this.total = res.data.data.total;
                    toLoad()
                });
        },
    },
    created() {
        this.requestDoctors();
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