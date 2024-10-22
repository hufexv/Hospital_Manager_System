<template>
    <div>
        <!-- 卡片 -->
        <el-card>
            <!-- 搜索栏及增加检查 -->
            <el-row type="flex">
                <el-col :span="6">
                    <el-input v-model="query" placeholder="Search_by_id">
                        <el-button
                            slot="append"
                            @click="requestBeds"
                            style="font-size: 18px"
                        ><i class="iconfont icon-r-find" style="font-size: 22px;"></i> Search</el-button>
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
                        Increase_bed</el-button
                    >
                </el-col>
            </el-row>
            <!-- 表格 -->
            <el-table :data="bedData" stripe style="width: 100%" border>
                <el-table-column label="Bed_num" prop="bId"></el-table-column>
                <el-table-column label="Patient_id" prop="pId"></el-table-column>
                <el-table-column label="Doctor_id" prop="dId"></el-table-column>
                <el-table-column
                    label="Start_time"
                    prop="bStart"
                ></el-table-column>
                <el-table-column
                    label="Reason"
                    prop="bReason"
                ></el-table-column>
                <el-table-column label="State" prop="bState">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.bState === 1" type="danger"
                            >Occupied</el-tag
                        >
                        <el-tag v-if="scope.row.bState === 0" type="success"
                            >None</el-tag
                        >
                    </template>
                </el-table-column>

                <el-table-column label="Operation" width="240" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                            style="font-size: 18px"
                            type="success"
                            @click="deleteDialog(scope.row.bId)"
                        ><i class="iconfont icon-r-edit" style="font-size: 22px;"></i> edit</el-button>
                        <el-button
                            style="font-size: 18px"
                            type="danger"
                            @click="emptyDialog(scope.row.bId)"
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

        <!-- 增加床位对话框 -->
        <el-dialog title="Increase_bed" :visible.sync="addFormVisible">
            <el-form :model="addForm" :rules="rules" ref="ruleForm">
                <el-form-item label="Bed_num" prop="bId" label-width="80px">
                    <el-input v-model.number="addForm.bId"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> 取 消</el-button>
                <el-button type="primary" @click="addBed('ruleForm')"
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
            bedData: [],
            total: 3,
            addFormVisible: false,
            addForm: {},
            rules: {
                bId: [
                    { required: true, message: "Bed_num", trigger: "blur" },
                    {
                        type: "number",
                        message: "Numbers_please",
                        trigger: "blur",
                    },
                ],
            },
        };
    },
    methods: {
        //清空床位操作
        emptyBed(id) {
            request
                .get("bed/emptyBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
                    console.log(res);
                });
        },
        //清空对话框
        emptyDialog(id) {
            this.$confirm("EmptyBed?", "Tip", {
                confirmButtonText: "Yes",
                cancelButtonText: "No",
                type: "warning",
            })
                .then(() => {
                    this.emptyBed(id);
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

        //删除床位操作
        deleteBed(id) {
            request
                .get("bed/deleteBed", {
                    params: {
                        bId: id,
                    },
                })
                .then((res) => {
                    this.requestBeds();
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
                    this.deleteBed(id);
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
        addBed(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    request
                        .get("bed/addBed", {
                            params: {
                                bId: this.addForm.bId,
                                pId: -1,
                                dId: -1,
                            },
                        })
                        .then((res) => {
                            if (res.data.status !== 200)
                                return this.$message.error("Occupied");
                            this.addFormVisible = false;
                            this.requestBeds();
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
            this.requestBeds();
        },
        //   页码改变时触发
        handleCurrentChange(num) {
            console.log(num);
            this.pageNumber = num;
            this.requestBeds();
        },
        // 加载检查列表
        requestBeds() {
            request
                .get("bed/findAllBeds", {
                    params: {
                        pageNumber: this.pageNumber,
                        size: this.size,
                        query: this.query,
                    },
                })
                .then((res) => {
                    this.bedData = res.data.data.beds;
                    this.total = res.data.data.total;
                    toLoad()
                });
        },
    },
    created() {
        this.requestBeds();
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