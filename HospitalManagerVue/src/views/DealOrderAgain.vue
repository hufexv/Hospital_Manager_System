<template>
<div>
  <el-card>
    <el-row>
      <el-col :span="7">
          Register：<el-input disabled v-model="oId" class="orderInput"></el-input>
      </el-col>
      <el-col :span="7">
          Patient_account：<el-input disabled v-model="pId" class="orderInput"></el-input></el-col>
      <el-col :span="7">
          Patient_name：<el-input disabled v-model="pName" class="orderInput"></el-input>
      </el-col>
        <el-col :span="3">
          <el-button type="success" 
                  style="font-size: 18px;"
                  icon="iconfont icon-r-paper" @click="submitClick"> 提交</el-button>
      </el-col>
    </el-row>
    <el-row>
          <el-col :span="7">
          Patient_gender：<el-input disabled v-model="pGender" class="orderInput"></el-input>
      </el-col>
      <el-col :span="7">
          Contact：<el-input disabled v-model="pPhone" class="orderInput"></el-input></el-col>
      <el-col :span="7">
          Doctor_name：：<el-input disabled v-model="dName" class="orderInput"></el-input>
      </el-col>
              <el-col :span="3">
          <el-button type="success" 
                  style="font-size: 18px;" @click="openAdvice">
                  <i class="iconfont icon-r-add" style="font-size: 20px;"></i> 
                  diagnose</el-button>
      </el-col>
    </el-row>
      <!-- 药物表格 -->
      <el-row>
        <el-col :span="12">
          <el-input v-model="queryDrug" placeholder="Search_by_name" class="drugInput">
            <el-button
            slot="append"
            icon="el-icon-search"
            @click="requestDrug"
          ></el-button>
          </el-input>
      <el-table :data="drugData" stripe border>
        <el-table-column label="NO" prop="drId"></el-table-column>
        <el-table-column label="Name" prop="drName"></el-table-column>
        <el-table-column label="Number" prop="drNumber"></el-table-column>
        <el-table-column label="Unit" prop="drUnit"></el-table-column>
        <el-table-column label="Price" prop="drPrice"></el-table-column>
        <el-table-column label="Operation" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button type="success" 
                  style="font-size: 14px;" @click="addDrug(scope.row.drId)">
                  <i class="iconfont icon-r-add" style="font-size: 16px;"></i> 
                  increase</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
  background
  layout="total, sizes, prev, pager, next"
  :total="total"
  :page-size="size"
  :page-sizes="[1, 2, 4, 8, 16]"
  >
  
</el-pagination>
<el-row></el-row>
      </el-col>
      <!-- 右边已选择的药物 -->
      <el-col :span="12" class="drugRigth">
        <el-table stripe border :data="drugBuyData" class="rigthTable">
          <el-table-column label="NO" prop="drId"></el-table-column>
          <el-table-column label="Name" prop="drName"></el-table-column>
           <el-table-column label="Price" prop="drPrice"></el-table-column>
            <el-table-column label="Number" prop="drNum"></el-table-column>
             <el-table-column label="ps" prop="drSum"></el-table-column>
             <el-table-column label="Operation" width="120" fixed="right">
               <template slot-scope="scope">
                 <el-button type="danger" 
                  style="font-size: 14px;"
                  icon="iconfont icon-r-delete" @click="deleteDrug(scope.row.drId)"> delete</el-button>
               </template>
             </el-table-column>
        </el-table>
        <el-tag>total：{{drugTotalPrice}}Yuan</el-tag>

      </el-col>
      
</el-row>
<!-- 检查项目编写 -->
<el-row>
      <el-col :span="12">
        <el-input v-model="queryCheck" placeholder="Search_by_name" class="drugInput">
            <el-button
            slot="append"
            icon="el-icon-search"
            @click="requestCheck"
          ></el-button>
          </el-input>
          <el-table stripe border :data="checkData">
            <el-table-column label="NO" prop="chId"></el-table-column>
             <el-table-column label="Proj" prop="chName"></el-table-column>
              <el-table-column label="Price" prop="chPrice"></el-table-column>
              <el-table-column label="Operation" width="120" fixed="right">
                <template slot-scope="scope">
                  <el-button type="success" 
                  style="font-size: 14px;" @click="addCheck(scope.row.chId)">
                  <i class="iconfont icon-r-add" style="font-size: 16px;"></i> 
                  increase</el-button>
                </template>
              </el-table-column>
          </el-table>
                <!-- 分页 -->
      <el-pagination
      @size-change="checkSizeChange"
      @current-change="checkCurrentChange"
  background
  layout="total, prev, pager, next"
  :total="checkTotal"
  :page-size="checkSize"
  >
      </el-pagination>
      </el-col>
      <!-- 右边已选择的检查 -->
      <el-col :span="12" class="drugRigth">
        <el-table stripe border class="rigthTable" :data="checkBuyData">
          <el-table-column label="NO" prop="chId"></el-table-column>
             <el-table-column label="Proj" prop="chName"></el-table-column>
              <el-table-column label="Price" prop="chPrice"></el-table-column>
              <el-table-column label="Operation" width="120" fixed="right">
                <template slot-scope="scope">
                  <el-button type="danger" 
                  style="font-size: 14px;"
                  icon="iconfont icon-r-delete" @click="deleteCheck(scope.row.chId)"> delete</el-button>
                </template>
              </el-table-column>
              </el-table>
              <el-tag>total：{{checkTotalPrice}}Yuan</el-tag>
      </el-col>
    </el-row>

  </el-card>


<!-- 病因编写对话框 -->
 <el-dialog title="diagnose" :visible.sync="adviceFormVisible">
   <el-input
  type="textarea"
  :rows="8"
  placeholder="content"
  v-model="advice">
</el-input>
<div slot="footer" class="dialog-footer">
        <el-button @click="adviceFormVisible = false" style="font-size: 18px;"><i class="iconfont icon-r-left" style="font-size: 20px;"></i> cancel</el-button>
        <el-button type="primary" @click="holdAdvice"
          >save</el-button
        >
      </div>
 </el-dialog>

  </div>
</template>
<script>
import jwtDecode from "jwt-decode";
import { getToken} from "@/utils/storage.js";
import request from "@/utils/request.js";
export default {
  name: "dealOrderAgain",
  data() {
      return {
          oId: 1,
          pId: 1,
          dId: 0,
          pName: "aa",
          pGender: "as",
          pPhone: "asd",
          dName: "",
          drugData:[],
          size: 4,
          pageNumber: 1,
          total: 3,
          queryDrug: "",
          drugBuyData: [],
          drugTotalPrice:0,
          //病因
          advice: "",
          //项目表格数据
          checkData:[],
          queryCheck:"",
          checkTotal:3,
          checkSize:4,
          checkPageNumber:1,
          checkTotalPrice:0,
          checkBuyData:[],
          // 病因编写对话框显示参数
          adviceFormVisible: false,

      }
  },
  methods: {
    //根据id减少药物数量
    reduceDrugNumber(drId,usedNumber){
      request.get("drug/reduceDrugNumber", {
        params: {
          drId: drId,
          usedNumber: usedNumber,
        }
      })
      .then(res => {
       if(res.data.status !== 200){
         this.$message.error("Lack_durg！！");
         }
      })
    },
    //点击提交按钮
    submitClick(){
      
      for(let i=0;i<this.drugBuyData.length;i++){
         this.reduceDrugNumber(this.drugBuyData[i].drId, this.drugBuyData[i].drNum);
      }; 
      let data = { 
          oId: this.dataPackage().oId,
        //   pId: this.pId,
        //   dId: this.dId,
          oAdvice: this.dataPackage().oAdvice,
          oDrug: this.dataPackage().oDrug,
          oCheck: this.dataPackage().oCheck,
          oTotalPrice: this.dataPackage().oTotalPrice }
      request.post("order/updateOrderByAdd",data)
      .then(res => {
          console.log(res)
              if(res.data.status !== 200){
         this.$message.error("Fail");
         return;
              }
              
         //this.$message.success("success！login and pay the fee！");
                  request.get("order/findTotalPrice", {
           params: {
             oId: data.oId
           }
         })
         .then(res => {
           if(res.data.status === 200)
           this.$message.success("success！login and pay the fee！");
           
         });



         this.$router.push("/orderToday");
         console.log(res.data)
      })
      .catch(err => {
        console.error(err); 
      })

    },
    //封装数据
    dataPackage(){
      let oDrug = " // ";
      let oCheck = " // ";
      let oTotalPrice = 0;
      let oId = this.oId;
      let oAdvice = this.advice;
      for(let i=0;i<this.drugBuyData.length;i++){
        oDrug += this.drugBuyData[i].drName+"*"+this.drugBuyData[i].drPrice+"(Yuan)*"+this.drugBuyData[i].drNum+" ";
      };
       for(let i=0;i<this.checkBuyData.length;i++){
        oCheck += this.checkBuyData[i].chName+"*"+this.checkBuyData[i].chPrice+"(Yuan) ";
      }
      oCheck += "check_price"+this.checkTotalPrice+"Yuan";
      oDrug += "drug_price"+this.drugTotalPrice+"Yuan";
      oTotalPrice = this.checkTotalPrice+this.drugTotalPrice;
      return {oId,oAdvice, oDrug, oCheck, oTotalPrice};

    },
    //点击病因保存按钮
    holdAdvice(){
      this.adviceFormVisible = false;
      this.$message.success("success！");
    },
    //打开病因编写对话框
    openAdvice(){
      this.adviceFormVisible = true;

    },
      //检查列表点击移除按钮
    deleteCheck(chId){
      for(let i=0;i<this.checkBuyData.length;i++){
        if(this.checkBuyData[i].chId === chId){
          this.checkTotalPrice -= this.checkBuyData[i].chPrice;//药物价格总计
          this.checkBuyData.splice(i,1) ;//！！！！！！删除数组中下标为i的元素
        }
      }
    },
      //检查列表点击增加按钮
    addCheck(chId){
      request.get("check/findCheck", {
        params: {
          chId: chId
        }
      })
      .then(res => {
        if(res.data.status != 200)
        this.$message.error("Fail");
        //后端传过来的是对象，表格绑定的数组
           this.checkBuyData.push(
          {
            chId: res.data.data.chId,
            chPrice: res.data.data.chPrice,
            chName:res.data.data.chName,
          }
          )
           this.checkTotalPrice += res.data.data.chPrice;//药物价格总计
        console.log(res.data.data);
      })

    },
        //药物页面大小切换时触发
    checkSizeChange(size){
      this.checkSize = size;
      this.requestCheck();

    },
    //检查页数切换时触发
    checkCurrentChange(num){
      this.checkPageNumber = num;
      this.requestCheck();
    },
    //请求检查项目
    requestCheck(){
      request.get("check/findAllChecks", {
        params: {
          size: this.checkSize,
          pageNumber: this.checkPageNumber,
          query: this.queryCheck
        }
      })
      .then(res => {
        if(res.data.status != 200)
              this.$message.error("Fail");
              this.checkData = res.data.data.checks;
              this.checkTotal = res.data.data.total;
        console.log(res);
      })
    },
    /**
     * 此处逻辑较复杂，容易出现错误
     */
    //药物列表点击移除按钮
    deleteDrug(drId){
      for(let i=0;i<this.drugBuyData.length;i++){
        if(this.drugBuyData[i].drId === drId){
          for(let j=0;j<this.drugData.length;j++){
            if(this.drugData[j].drId === drId)
            this.drugData[j].drNumber +=1;
          }
          this.drugBuyData[i].drNum-=1;
          this.drugBuyData[i].drSum = this.drugBuyData[i].drPrice * this.drugBuyData[i].drNum;
          this.drugTotalPrice -= this.drugBuyData[i].drPrice;//药物价格总计
          if(this.drugBuyData[i].drNum === 0)
          this.drugBuyData.splice(i,1) ;//！！！！！！删除数组中下标为i的元素
        }
      }
    },
    //药物列表点击增加按钮
    addDrug(drId){
      request.get("drug/findDrug", {
        params: {
          drId: drId
        }
      })
      .then(res => {
        if(res.data.status != 200)
        this.$message.error("Fail");
        //后端传过来的是对象，表格绑定的数组
        for(let i=0;i<this.drugBuyData.length;i++){
          if(this.drugBuyData[i].drId === res.data.data.drId){
          for(let j=0;j<this.drugData.length;j++){
            if(this.drugData[j].drId === res.data.data.drId && this.drugData[j].drNumber > 0){
            this.drugData[j].drNumber -=1;
            this.drugBuyData[i].drNum+=1;
          this.drugBuyData[i].drSum = this.drugBuyData[i].drPrice * this.drugBuyData[i].drNum;
          this.drugTotalPrice += this.drugBuyData[i].drPrice;//药物价格总计
          //return;
            }
          }
          return;
          }
        }
        for(let j=0;j<this.drugData.length;j++){
            if(this.drugData[j].drId === res.data.data.drId && this.drugData[j].drNumber <= 0)
            return;
          }
           this.drugBuyData.push(
          {
            drId: res.data.data.drId,
            drPrice: res.data.data.drPrice,
            drName:res.data.data.drName,
            drNum: 1,
            drSum: res.data.data.drPrice,
          }
          )
          for(let j=0;j<this.drugData.length;j++){
            if(this.drugData[j].drId === res.data.data.drId )
            this.drugData[j].drNumber -=1;
          }
           this.drugTotalPrice += res.data.data.drPrice;//药物价格总计
         

        console.log(res.data.data);
      })

    },
    //药物页面大小切换时触发
    handleSizeChange(size){
      this.size = size;
      this.requestDrug();

    },
    //药物页数切换时触发
    handleCurrentChange(num){
      this.pageNumber = num;
      this.requestDrug();
    },

    //获取药物列表
    requestDrug(){
      request.get("drug/findAllDrugs", {
        params: {
          size: this.size,
          pageNumber: this.pageNumber,
          query: this.queryDrug
        }
      })
      .then(res => {
         if(res.data.status != 200)
              this.$message.error("Fail");
              this.drugData = res.data.data.drugs;
              this.total = res.data.data.total;
  

        console.log(res);
      })
    },
      //获取患者信息
      requestPatient(){
          request.get("doctor/findPatientById", {
              params: {
                  pId: this.pId
              }
          })
          .then(res => {
            console.log(res);
              if(res.data.status != 200)
              this.$message.error("Fail");
              this.pName = res.data.data.pName;
              this.pGender = res.data.data.pGender;
              this.pPhone = res.data.data.pPhone;
              
          })

      },
    //token解码
    tokenDecode(token){
      if (token !== null)
      return jwtDecode(token);
    },
  },
  created() {
    this.dName = this.tokenDecode(getToken()).dName;
    this.dId = this.tokenDecode(getToken()).dId;
    this.oId = this.$route.query.oId;
    this.pId = this.$route.query.pId;
    this.requestPatient();
    this.requestDrug();
    this.requestCheck();
  },
};
</script>
<style lang="scss">

// 让文字居中
.drugRigth{
  text-align: center;
}
.el-tag{
  margin: 8px;
}
.rigthTable{
  margin-top: 56px;
  margin-left: 8px;
}
.drugInput{
  margin-top: 8px;
  margin-bottom: 8px;
}
.el-row{
    margin: 5px;
}
.orderInput{
    width: 240px;
}
.el-pagination{
  margin: 8px;
}
</style>