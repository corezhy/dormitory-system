<script setup>
//导包
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { selectAll as queryMajorAllApi } from '@/api/major'
import { conditionQueryClassAPi, insertClassApi, getByIdApi, editClassApi, deleteClassApi } from '@/api/class'

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

//搜索栏数据绑定
const queryClassData = ref({
  pageNum: 1,
  pageSize: 10,
  name: '',
  majorCode: '',
  grade: null
})

//表格数据
const classQueryResult = ref([]);

//分页总记录数
const total = ref(null);

//条件分页查询班级
const queryClass = async () => {
  try {
    const result = await conditionQueryClassAPi(queryClassData.value);
    if (result.code === 1) {
      //赋值表格数据
      classQueryResult.value = result.data.records;
      //赋值总记录数
      total.value = result.data.total;
    } else {
      ElMessage.error('查询班级失败');
    }
  } catch (error) {
    console.log('查询班级失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//点击清空按钮触发
const clearMethod = () => {
  //清空查询参数
  queryClassData.value = {
    pageNum: 1,
    pageSize: 10,
    name: '',
    majorCode: '',
    grade: null
  }
  //查询
  queryClass()
}

//钩子函数
onMounted(() => {
  //查询专业名称
  queryMajor();
  //查询班级
  queryClass();
})

//对话框是否显示
const dialogFormVisible = ref(false);

//对话框标题
const title = ref('');

//对话框数据绑定
const classDialogData = ref({
  code: '',
  name: '',
  majorCode: '',
  grade: null,
  counselor: ''
})

//动态计算年份范围
const currentYear = new Date().getFullYear();
const minGradeYear = currentYear - 20; //2005
const maxGradeYear = currentYear + 10; //2035

//校验函数
const validateGrade = (rule, value, callback) => {
  if (value === null || value === undefined) {
    callback(new Error('请输入入学年份'));
  } else if (value < minGradeYear || value > maxGradeYear) {
    callback(new Error(`入学年份应在${minGradeYear}至${maxGradeYear}之间`));
  } else {
    callback();
  }
}

//对话框表单校验规则
const classFormRef = ref();
const rules = reactive({
  code: [
    { required: true, message: '请输入班级编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  majorCode: [
    { required: true, message: '请选择专业名称', trigger: 'change' }
  ],
  grade: [
    { required: true, validator: validateGrade, trigger: 'blur' }
  ],
  counselor: [
    {
      required: true,
      message: '请输入辅导员姓名',
      trigger: 'blur'
    },
    {
      pattern: /^[\u4e00-\u9fa5a-zA-Z\s]{2,10}$/,
      message: '辅导员姓名应为 2-10 个中文或英文字符',
      trigger: 'blur'
    }
  ]
})

//点击新增班级触发
const addClass = () => {
  //打开对话框
  dialogFormVisible.value = true;
  //修改标题
  title.value = '新增班级';
  //清空对话框数据
  classDialogData.value = {
    code: '',
    name: '',
    majorCode: '',
    grade: null,
    counselor: ''
  }
  //清空表单校验
  classFormRef.value?.resetFields();
}

//点击对话框确定按钮触发
const submit = async () => {
  //表单校验
  // ✅ 使用 await 等待校验完成，并捕获异常
  try {
    await classFormRef.value.validate();
    // 只有校验通过才会走到这里
  } catch (error) {
    // 校验失败，自动提示错误信息
    ElMessage.error('请检查表单填写是否正确！');
    return; // ✅ 阻止后续提交
  }
  //新增班级
  if (title.value === '新增班级') {
    try {
      const result = await insertClassApi(classDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //查询
        queryClass();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('新增班级失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }
  //修改班级
  if (title.value === '修改班级') {
    try {
      const result = await editClassApi(classId.value, classDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //查询
        queryClass();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('修改班级失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

}

//定义需要修改的id字段
const classId = ref(null);

//点击修改按钮触发
const editClass = async (id) => {
  //打开对话框
  dialogFormVisible.value = true;
  //标题
  title.value = '修改班级';
  //查询回显数据
  try {
    const result = await getByIdApi(id);
    if (result.code === 1) {
      //对话框数据赋值
      classDialogData.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询回显班级失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
  //清空表单校验
  classFormRef.value?.resetFields();
  //为classId赋值
  classId.value = id;
}

//点击删除按钮触发
const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该班级吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    const result = await deleteClassApi([id]); //id参数如果和批量删除共用一个APi，后端是需要集合，可以写[id]
    if (result.code) {
      ElMessage.success(result.data);
      //查询
      queryClass();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}

//记录勾选的员工的id
const selectedIds = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
}

//点击批量删除按钮触发
const deleteByIds = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该班级吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    if (selectedIds.value && selectedIds.value.length > 0) {
      const result = await deleteClassApi(selectedIds.value);
      if (result.code) {
        ElMessage.success(result.data);
        queryClass();
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
  <h3 style="margin-top: 0px;">班级管理</h3>
  <!-- 搜索栏 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px;">
    班级名称:
    <el-input v-model="queryClassData.name" size="large" style="width: 240px" placeholder="请输入班级名称" clearable
      @clear="queryClass()" />

    专业名称:
    <el-select v-model="queryClassData.majorCode" size="large" placeholder="请选择专业名称" style="width: 240px"
      @change="queryClass()">
      <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
    </el-select>

    入学年级:
    <el-input v-model="queryClassData.grade" size="large" style="width: 240px" placeholder="请输入入学年级" clearable
      @clear="queryClass()" />
    <el-button style="margin-left: 20px;" type="success" size="large" @click="queryClass()">查询</el-button>
    <el-button type="primary" size="large" @click="clearMethod()">清空</el-button>
  </div>
  <!-- 新增班级和批量删除班级按钮 -->
  <div style="margin-bottom: 20px;">
    <el-button size="large" type="primary" @click="addClass()">新增班级+</el-button>
    <el-button size="large" type="danger" @click="deleteByIds()">批量删除-</el-button>
  </div>
  <!-- 表格 -->
  <el-table :data="classQueryResult" stripe style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" align="center" />
    <el-table-column prop="code" label="班级编码" width="180" align="center" />
    <el-table-column prop="name" label="班级名称" width="200" align="center" />
    <el-table-column prop="majorName" label="专业名称" width="200" align="center" />
    <el-table-column prop="grade" label="入学年级" width="200" align="center" />
    <el-table-column prop="counselor" label="辅导员姓名" width="200" align="center" />
    <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center" />
    <el-table-column label="操作" width="200" align="center">
      <template #default="scope">
        <el-button size="default" type="primary" @click="editClass(scope.row.id)">编辑</el-button>
        <el-button size="default" type="danger" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 分页 -->
  <div class="demo-pagination-block" style="margin-top: 20px;">
    <el-pagination v-model:current-page="queryClassData.pageNum" v-model:page-size="queryClassData.pageSize"
      :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
      @size-change="queryClass()" @current-change="queryClass()" />
  </div>
  <!-- 新增修改班级对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="600px" center>
    <el-form :model="classDialogData" style="width: 80%; margin: 0 auto;" :rules="rules" ref="classFormRef"
      label-width="100px">
      <!-- 第一行 -->
      <el-form-item label="班级编码" prop="code">
        <el-input v-model="classDialogData.code" placeholder="请输入班级编码" style="width: 100%" size="large" />
      </el-form-item>
      <!-- 第二行 -->
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="classDialogData.name" placeholder="请输入班级名称" style="width: 100%" size="large" />
      </el-form-item>
      <!-- 第三行 -->
      <el-form-item label="专业名称" prop="majorCode">
        <el-select v-model="classDialogData.majorCode" size="large" placeholder="请选择专业名称" style="width: 240px">
          <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <!-- 第四行 -->
      <el-form-item label="入学年份" prop="grade">
        <el-input-number v-model="classDialogData.grade" :min="minGradeYear" :max="maxGradeYear" style="width: 70%;"
          placeholder="请输入入学年份" size="large" />
      </el-form-item>
      <!-- 第五行 -->
      <el-form-item label="辅导员姓名" prop="counselor">
        <el-input v-model="classDialogData.counselor" placeholder="请输入辅导员姓名" style="width: 100%" size="large" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submit()">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped></style>