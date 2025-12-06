<script setup>
//导包
import { ref, onMounted, reactive, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryIsEnabledApi as queryBuildingAllApi } from '@/api/building'
import { getFloorNumberByBuildingIdApi as queryFloorNumber } from '@/api/floor'
import { selectAll as queryMajorAllApi } from '@/api/major'
import { conditionPageQueryStuApi, addStuApi, getStuByIdApi, editStuApi, disciplineApi, deleteStuByIdApi } from '@/api/stu'

//性别列表数据
const genders = ref([{ name: '男', value: 0 }, { name: '女', value: 1 }])

//状态列表数据
const statuses = ref([{ name: '在住', value: 1 }, { name: '已退宿', value: 0 }])

//搜索栏数据
const stuQueryParam = ref({
  pageNum: 1,
  pageSize: 10,
  studentNo: '',
  name: '',
  gender: null,
  phone: '',
  status: null,
  buildingId: null,
  floorId: null,
  majorCode: '',
  className: '',
  fullCode: ''
})

//定义变量存储学生列表数据
const stuTableData = ref([]);

// 监听 buildingId 变化，自动加载楼层
watch(
  () => stuQueryParam.value.buildingId,
  async (newBuildingId) => {
    if (newBuildingId !== null) {
      await queryFloor();
    } else {
      // 如果清空楼栋，也清空楼层选项
      searchFloorOptions.value = [];
      stuQueryParam.value.floorId = null;
    }
  }
);

//条件分页查询学生
const queryStu = async () => {
  try {
    const result = await conditionPageQueryStuApi(stuQueryParam.value);
    if (result.code === 1) {
      stuTableData.value = result.data.records;
      total.value = result.data.total;
    } else {
      ElMessage.error('查询学生信息失败');
    }
  } catch (error) {
    console.log('查询学生信息失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//存储楼栋数据
const buildingOptions = ref([])
//查询楼栋名称
const queryBuilding = async () => {
  try {
    const result = await queryBuildingAllApi();
    if (result.code === 1) {
      //赋值
      buildingOptions.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询楼栋失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//存储楼栋下楼层数据(搜索栏)
const searchFloorOptions = ref([])

//查询楼栋下楼层
const queryFloor = async () => {
  try {
    const result = await queryFloorNumber(stuQueryParam.value.buildingId);
    if (result.code === 1) {
      //赋值
      searchFloorOptions.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询楼层失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//存储专业数据
const majorOptions = ref([])

//查询专业名称
const queryMajor = async () => {
  try {
    const result = await queryMajorAllApi();
    if (result.code === 1) {
      //赋值
      majorOptions.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询专业失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//钩子函数
onMounted(() => {
  //查询楼栋
  queryBuilding();
  //查询专业
  queryMajor();
  //查询学生
  queryStu();
})

//分页总记录数
const total = ref(null);

//点击清空按钮触发
const clearMethod = () => {
  //清空全部搜索字段
  stuQueryParam.value = {
    pageNum: 1,
    pageSize: 10,
    studentNo: '',
    name: '',
    gender: null,
    phone: '',
    status: null,
    buildingId: null,
    floorId: null,
    majorCode: '',
    className: '',
    fullCode: ''
  }
  //查询
  queryStu();
}

//对话框是否启用
const dialogFormVisible = ref(false);

//对话框标题
const title = ref('');

//对话框数据绑定
const stuDialogData = ref({
  studentNo: '',
  name: '',
  gender: null,
  majorCode: '',
  className: '',
  phone: '',
  buildingId: null,
  floorId: null,
  fullCode: '',
  bedNo: null,
  status: null
})

// 对话框中的楼层选项
const dialogFloorOptions = ref([]);

// 根据对话框中选中的 buildingId 查询对应楼层（用于新增/编辑）
const queryDialogFloor = async () => {
  const buildingId = stuDialogData.value.buildingId;
  if (buildingId === null || buildingId === undefined) {
    dialogFloorOptions.value = [];
    stuDialogData.value.floorId = null;
    return;
  }

  try {
    const result = await queryFloorNumber(buildingId);
    if (result.code === 1) {
      dialogFloorOptions.value = result.data;
      // 可选：如果之前选的 floorId 不在新列表里，清空它
      const validFloorIds = result.data.map(f => f.id);
      if (!validFloorIds.includes(stuDialogData.value.floorId)) {
        stuDialogData.value.floorId = null;
      }
    } else {
      ElMessage.error(result.msg);
      dialogFloorOptions.value = [];
      stuDialogData.value.floorId = null;
    }
  } catch (error) {
    console.error('查询对话框楼层失败', error);
    ElMessage.error('网络错误，请稍后重试');
    dialogFloorOptions.value = [];
    stuDialogData.value.floorId = null;
  }
};

//表单校验规则
const stuFormRef = ref();
const rules = reactive({
  studentNo: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    {
      pattern: /^[a-zA-Z0-9]{1,20}$/,
      message: '学号只能包含字母和数字，长度1-20位',
      trigger: 'blur'
    }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    {
      min: 1,
      max: 50,
      message: '姓名长度为1-50个字符',
      trigger: 'blur'
    }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  majorCode: [
    { required: true, message: '请选择专业名称', trigger: 'change' }
  ],
  className: [
    { required: true, message: '请输入班级名称', trigger: 'blur' },
    {
      max: 50,
      message: '班级名称不能超过50个字符',
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入正确的中国大陆手机号',
      trigger: 'blur'
    }
  ],
  buildingId: [
    { required: true, message: '请选择楼栋名称', trigger: 'change' }
  ],
  floorId: [
    { required: true, message: '请选择楼层号', trigger: 'change' }
  ],
  fullCode: [
    { required: true, message: '请输入房间号', trigger: 'blur' },
    {
      max: 30,
      message: '房间号不能超过30个字符',
      trigger: 'blur'
    }
  ],
  bedNo: [
    { required: true, message: '请输入床位号', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value);
        if (!value || isNaN(num) || !Number.isInteger(num) || num <= 0) {
          callback(new Error('床位号必须是大于0的整数'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
  // status 不校验，因为新增时不显示，且允许为空
});

//点击新增学生按钮触发
const addStu = () => {
  //打开对话框
  dialogFormVisible.value = true;
  //修改标题
  title.value = '新增学生';
  //清空表单数据
  stuDialogData.value = {
    studentNo: '',
    name: '',
    gender: null,
    majorCode: '',
    className: '',
    phone: '',
    buildingId: null,
    floorId: null,
    fullCode: '',
    bedNo: null,
    status: null
  }
  //清空表单校验规则
  stuFormRef.value?.resetFields();
}

//点击对话框确定按钮触发
const confirm = async () => {
  //表单校验
  // ✅ 使用 await 等待校验完成，并捕获异常
  try {
    await stuFormRef.value.validate();
    // 只有校验通过才会走到这里
  } catch (error) {
    // 校验失败，自动提示错误信息
    ElMessage.error('请检查表单填写是否正确！');
    return; // ✅ 阻止后续提交
  }

  //新增学生
  if (title.value === '新增学生') {
    try {
      const result = await addStuApi(stuDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //查询
        queryStu();
      } else {
        //提示
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('新增学生失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

  //修改学生
  if (title.value === '修改学生') {
    try {
      const result = await editStuApi(studentId.value, stuDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //查询
        queryStu();
      } else {
        //提示
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('修改学生失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }
}

//定义变量记录查询回显时学生id（用于修改）
const studentId = ref(null);

//点击编辑按钮触发
const editStu = async (id) => {
  //打开对话框
  dialogFormVisible.value = true;
  //标题
  title.value = '修改学生';
  //查询回显
  try {
    const result = await getStuByIdApi(id);
    if (result.code === 1) {
      const student = result.data;

      // 第一步：先设置 buildingId（触发楼层加载）
      stuDialogData.value.buildingId = student.buildingId;

      // 第二步：主动加载该楼栋的楼层选项（关键！）
      await queryDialogFloor(); // 等待 dialogFloorOptions 被填充

      // 第三步：再赋值 floorId（此时选项已存在，能正确匹配）
      stuDialogData.value = result.data;

    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询回显学生信息失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
  //清空表单校验
  stuFormRef.value?.resetFields();
  //为修改时学生id赋值
  studentId.value = id;
}

// 违纪扣分表单引用
const disciplineFormRef = ref();

// 违纪扣分校验规则
const disciplineRules = reactive({
  violationScore: [
    { required: true, message: '请输入扣分数', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const num = Number(value);
        if (!value || isNaN(num) || !Number.isInteger(num) || num <= 0) {
          callback(new Error('扣分数必须是大于0的整数'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
});

//违纪扣分对话框是否启用
const dialogDisciplineFormVisible = ref(false);

//违纪扣分数据
const disciplineForm = ref({
  violationScore: null
});

//定义变量记录违纪扣分学生Id
const violationStuId = ref(null);

//点击违纪按钮触发
const violation = (id) => {
  //打开对话框
  dialogDisciplineFormVisible.value = true;
  //清空对话框数据
  disciplineForm.value = {
    violationScore: null
  }
  //重置表单校验
  disciplineFormRef.value?.resetFields();
  //赋值扣分学生Id
  violationStuId.value = id;
}

//点击违纪扣分确定按钮触发
const submitDiscipline = async () => {
  try {
    const result = await disciplineApi(violationStuId.value, disciplineForm.value.violationScore);
    if (result.code === 1) {
      //关闭对话框
      dialogDisciplineFormVisible.value = false;
      //提示
      ElMessage.success(result.data);
      //查询
      queryStu();
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('违纪扣分失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//点击删除按钮触发
const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该学生吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    const result = await deleteStuByIdApi([id]); //id参数如果和批量删除共用一个APi，后端是需要集合，可以写[id]
    if (result.code) {
      ElMessage.success(result.data);
      queryStu();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}

//记录勾选学生的id
const selectedIds = ref([]);

//复选框勾选发生变化时触发 -selection: 当前选中的记录（数组）
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
}

//点击批量删除按钮触发
const deleteBatchByIds = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该学生吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    if (selectedIds.value && selectedIds.value.length > 0) {
      const result = await deleteStuByIdApi(selectedIds.value);
      if (result.code) {
        ElMessage.success(result.data);
        queryStu();
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.info('您没有选择任何要删除的数据');
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}
</script>

<template>
  <!-- 标题 -->
  <h3 style="margin-top: 0px;">学生管理</h3>
  <!-- 搜索栏 -->
  <!-- 第一行 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 16px; flex-wrap: wrap;">
    <span style="width: 80px; text-align: right; white-space: nowrap;">学号:</span>
    <el-input v-model="stuQueryParam.studentNo" size="large" style="width: 240px" placeholder="请输入学号" clearable
      @clear="queryStu()" />

    <span style="width: 80px; text-align: right; white-space: nowrap;">姓名:</span>
    <el-input v-model="stuQueryParam.name" size="large" style="width: 240px" placeholder="请输入姓名" clearable
      @clear="queryStu()" />

    <span style="width: 80px; text-align: right; white-space: nowrap;">性别:</span>
    <el-select v-model="stuQueryParam.gender" size="large" placeholder="请选择性别" style="width: 240px"
      @change="queryStu()">
      <el-option v-for="item in genders" :key="item.value" :label="item.name" :value="item.value" />
    </el-select>

    <span style="width: 80px; text-align: right; white-space: nowrap;">手机号:</span>
    <el-input v-model="stuQueryParam.phone" size="large" style="width: 240px" placeholder="请输入手机号" clearable
      @clear="queryStu()" />
  </div>

  <!-- 第二行 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 16px; flex-wrap: wrap;">
    <span style="width: 80px; text-align: right; white-space: nowrap;">状态:</span>
    <el-select v-model="stuQueryParam.status" size="large" placeholder="请选择状态" style="width: 240px"
      @change="queryStu()">
      <el-option v-for="item in statuses" :key="item.value" :label="item.name" :value="item.value" />
    </el-select>

    <span style="width: 80px; text-align: right; white-space: nowrap;">楼栋名称:</span>
    <el-select v-model="stuQueryParam.buildingId" size="large" placeholder="请选择楼栋名称" style="width: 240px"
      @change="queryStu()">
      <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>

    <span style="width: 80px; text-align: right; white-space: nowrap;">楼层号:</span>
    <el-select v-model="stuQueryParam.floorId" size="large" placeholder="请选择楼层号" style="width: 240px"
      @change="queryStu()">
      <el-option v-for="item in searchFloorOptions" :key="item.id" :label="item.floorNumber" :value="item.id" />
    </el-select>

    <span style="width: 80px; text-align: right; white-space: nowrap;">专业名称:</span>
    <el-select v-model="stuQueryParam.majorCode" size="large" placeholder="请选择专业名称" style="width: 240px"
      @change="queryStu()">
      <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
    </el-select>
  </div>

  <!-- 第三行 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px; flex-wrap: wrap;">
    <span style="width: 80px; text-align: right; white-space: nowrap;">班级名称:</span>
    <el-input v-model="stuQueryParam.className" size="large" style="width: 240px" placeholder="请输入班级名称" clearable
      @clear="queryStu()" />

    <span style="width: 80px; text-align: right; white-space: nowrap;">房间号:</span>
    <el-input v-model="stuQueryParam.fullCode" size="large" style="width: 240px" placeholder="请输入房间号" clearable
      @clear="queryStu()" />

    <div style="margin-left: 500px;">
      <el-button type="success" size="large" @click="queryStu()">查询</el-button>
      <el-button type="primary" size="large" @click="clearMethod()">清空</el-button>
    </div>
  </div>
  <!-- 新增学生和批量删除学生按钮 -->
  <div style="margin-bottom: 20px;">
    <el-button size="large" type="primary" @click="addStu()">新增学生+</el-button>
    <el-button size="large" type="danger" @click="deleteBatchByIds()">批量删除-</el-button>
  </div>
  <!-- 表格展示区域 -->
  <div>
    <el-table :data="stuTableData" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="40" />
      <el-table-column prop="studentNo" label="学号" width="140" align="center" show-overflow-tooltip />
      <el-table-column prop="studentName" label="姓名" width="80" align="center" show-overflow-tooltip />
      <el-table-column prop="gender" label="性别" width="70" align="center">
        <template #default="scope">
          {{ scope.row.gender === 0 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="120" align="center" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="84" align="center">
        <template #default="scope">
          <span class="status-dot" :class="scope.row.status === 1 ? 'status-active' : 'status-inactive'"></span>
          {{ scope.row.status === 1 ? '在住' : '已退宿' }}
        </template>
      </el-table-column>
      <el-table-column prop="majorName" label="专业名称" width="110" align="center" show-overflow-tooltip />
      <el-table-column prop="className" label="班级名称" width="120" align="center" show-overflow-tooltip />
      <el-table-column prop="buildingName" label="楼栋名称" width="80" align="center" show-overflow-tooltip />
      <el-table-column prop="floorNumber" label="楼层号" width="90" align="center" />
      <el-table-column prop="fullCode" label="房间号" width="100" align="center" show-overflow-tooltip />
      <el-table-column prop="bedNo" label="床位号" width="70" align="center" show-overflow-tooltip />
      <el-table-column prop="violationScore" label="违纪扣分" width="90" align="center" show-overflow-tooltip />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="default" type="warning" @click="violation(scope.row.id)">违纪</el-button>
          <el-button size="default" type="primary" @click="editStu(scope.row.id)">编辑</el-button>
          <el-button size="default" type="danger" @click="deleteById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="demo-pagination-block" style="margin-top: 20px;">
      <el-pagination v-model:current-page="stuQueryParam.pageNum" v-model:page-size="stuQueryParam.pageSize"
        :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="queryStu()" @current-change="queryStu()" />
    </div>
  </div>
  <!-- 新增修改学生对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="800px">
    <el-form :model="stuDialogData" label-width="85px" size="large" :rules="rules" ref="stuFormRef">
      <!-- 第一行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="学号:" prop="studentNo">
            <el-input v-model="stuDialogData.studentNo" placeholder="请输入学号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名:" prop="name">
            <el-input v-model="stuDialogData.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="性别:" prop="gender">
            <el-select v-model="stuDialogData.gender" placeholder="请选择性别" style="width: 100%;">
              <el-option v-for="gender in genders" :key="gender.value" :label="gender.name"
                :value="gender.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="专业名称:" prop="majorCode">
            <el-select v-model="stuDialogData.majorCode" placeholder="请选择专业名称" style="width: 100%;">
              <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第三行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="班级名称:" prop="className">
            <el-input v-model="stuDialogData.className" placeholder="请输入班级名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号:" prop="phone">
            <el-input v-model="stuDialogData.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第四行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="楼栋名称:" prop="buildingId">
            <el-select v-model="stuDialogData.buildingId" size="large" placeholder="请选择楼栋名称" style="width: 100%;"
              @change="queryDialogFloor()">
              <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="楼层号:" prop="floorId">
            <el-select v-model="stuDialogData.floorId" size="large" placeholder="请选择楼层号" style="width: 100%;">
              <el-option v-for="item in dialogFloorOptions" :key="item.id" :label="item.floorNumber" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第五行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="房间号" prop="fullCode">
            <el-input v-model="stuDialogData.fullCode" placeholder="请输入房间号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="床位号:" prop="bedNo">
            <el-input v-model="stuDialogData.bedNo" placeholder="请输入床位号" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第六行 -->
      <el-row gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status" v-if="title === '修改学生'">
            <el-select v-model="stuDialogData.status" placeholder="请选择状态" style="width: 240px">
              <el-option v-for="item in statuses" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="confirm()">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 违纪扣分对话框 -->
  <!-- 违纪扣分对话框 -->
  <el-dialog v-model="dialogDisciplineFormVisible" title="违纪扣分" width="500px">
    <el-form :model="disciplineForm" :rules="disciplineRules" ref="disciplineFormRef" label-position="left"
      label-width="100px">
      <el-form-item label="扣分数:" prop="violationScore">
        <el-input-number v-model="disciplineForm.violationScore" :min="1" style="width: 80%;" placeholder="请输入大于0的整数"
          size="large" align="center" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" align="center">
        <el-button @click="dialogDisciplineFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDiscipline()">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
:deep(.status-dot) {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 6px;
  vertical-align: middle;
}

:deep(.status-active) {
  background-color: #67c23a;
  /* Element Plus success green */
}

:deep(.status-inactive) {
  background-color: #f56c6c;
  /* Element Plus error red */
}
</style>