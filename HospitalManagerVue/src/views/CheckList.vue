<template>
    <div>
        <!-- 卡片 -->
        <el-card>
            <!-- 搜索栏及增加检查 -->
            <el-row type="flex">
                <el-col :span="6">
                    <el-input v-model="query" placeholder="Search_by_name">
                        <el-button
                            slot="append"
                            style="font-size: 18px;"
                            @click="requestChecks"
                        ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> search</el-button>
                    </el-input>
                </el-col>
                <el-col :span="6"></el-col>
                <el-col :span="6">
                    <el-button
                        type="primary"
                        style="font-size: 18px"
                        @click="addFormVisible = true"
                    >
                    <i class="iconfont icon-r-add" style="font-size: 22px;"></i> 
                        Increase_item</el-button
                    >
                </el-col>
            </el-row>
            <!-- 表格 -->
            <el-table :data="checkData" stripe style="width: 100%" border>
                <el-table-column label="NO" prop="chId"></el-table-column>
                <el-table-column label="Proj" prop="chName"></el-table-column>
                <el-table-column
                    label="Price/Yuan"
                    prop="chPrice"
                ></el-table-column>
                <el-table-column label="Operation" width="250" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            style="font-size: 18px"
                            type="success"
                            @click="modifyDialog(scope.row.chId)"
                        ><i class="iconfont icon-r-edit" style="font-size: 22px;"></i> edit</el-button>
                        <el-button
                            style="font-size: 18px"
                            type="danger"
                            @click="deleteDialog(scope.row.chId)"
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

        <!-- 增加检查项目对话框 -->
        <el-dialog title="Increase_item" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="NO" prop="chId" label-width="80px">
                    <el-input v-model.number="addForm.chId"></el-input>
                </el-form-item>
                <el-form-item label="Name" prop="chName" label-width="80px">
                    <el-input v-model="addForm.chName"></el-input>
                </el-form-item>
                <el-form-item label="Price" prop="chPrice" label-width="80px">
                    <el-input v-model="addForm.chPrice"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="addCheck('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button
                >
            </div>
        </el-dialog>

        <!-- 修改检查项目药物对话框 -->
        <el-dialog title="Update_check_item" :visible.sync="modifyFormVisible">
            <el-form :model="modifyForm" :rules="rules" ref="ruleForm">
                <el-form-item label="NO" prop="chId" label-width="80px">
                    <el-input
                        v-model.number="modifyForm.chId"
                        disabled
                    ></el-input>
                </el-form-item>
                <el-form-item label="Name" prop="chName" label-width="80px">
                    <el-input v-model="modifyForm.chName"></el-input>
                </el-form-item>
                <el-form-item label="Price" prop="chPrice" label-width="80px">
                    <el-input v-model="modifyForm.chPrice"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="modifyFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="modifyCheck('ruleForm')"
                    style="font-size: 18px;"><i class="iconfont icon-r-yes" style="font-size: 20px;"></i> 确 定</el-button
                >
            </div>
        </el-dialog>
    </div>
</template>
<script>
import request from "@/utils/request.js";
import { toLoad } from "@/utils/initialize.js";
export default {
    name: "CheckList",
    data() {
        return {
            pageNumber: 1,
            size: 8,
            query: "",
            checkData: [],
            total: 3,
            addFormVisible: false,
            addForm: {},
            rules: {
                chId: [
                    { required: true, message: "No:", trigger: "blur" },
                    {
                        type: "number",
                        message: "Numbers_please",
                        trigger: "blur",
                    },
                ],
                chName: [
                    { required: true, message: "name", trigger: "blur" },
                    {
                        min: 1,
                        max: 50,
                        message: "1-50char",
                        trigger: "blur",
                    },
                ],
                chPrice: [
                    { required: true, message: "price", trigger: "blur" },
                ],
            },
            modifyFormVisible: false,
            modifyForm: {},
        };
    },
    methods: {
        //点击修改药物信息
        modifyCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("check/modifyCheck", {
                            params: {
                                chId: this.modifyForm.chId,
                                chName: this.modifyForm.chName,
                                chPrice: this.modifyForm.chPrice,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("Fail to update");
                            this.modifyFormVisible = false;
                            this.requestChecks();
                            this.$message.success("success");
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
                .get("check/findCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    if (res.data.status !== 200)
                        return this.$message.error("Fail to ask data");
                    this.modifyForm = res.data.data;
                    this.modifyFormVisible = true;
                    console.log(res);
                });
        },
        //删除检查操作
        deleteCheck(id) {
            request
                .get("check/deleteCheck", {
                    params: {
                        chId: id,
                    },
                })
                .then((res) => {
                    this.requestChecks();
                    console.log(res);
                });
        },
        //删除对话框
        deleteDialog(id) {
            this.$confirm("delete_check_item_info?", "Tip", {
                confirmButtonText: "Yesy",
                cancelButtonText: "No",
                type: "warning",
            })
                .then(() => {
                    this.deleteCheck(id);
                    this.$message({
                        type: "success",
                        message: "success",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "Canceled",
                    });
                });
        },
        //点击增加确认按钮
        addCheck(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("check/addCheck", {
                            params: {
                                chId: this.addForm.chId,
                                chName: this.addForm.chName,
                                chPrice: this.addForm.chPrice,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error(
                                    "Occupied"
                                );
                            this.addFormVisible = false;
                            this.requestChecks();
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
            this.requestChecks();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestChecks();
        },
        // 加载检查列表
        requestChecks() {
            request
                .get("check/findAllChecks", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.checkData = res.data.data.checks;
                    this.total = res.data.data.total;
                    toLoad();
                });
        },
    },
    created() {
        this.requestChecks();
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