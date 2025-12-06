<script setup>
//导包
import { onMounted, ref, reactive } from 'vue';
import { queryAllApi, insertMajorApi, queryMajorById, updateMajorApi, startOrStopAPi, deleteBatchApi } from '@/api/major';
import { ElMessage, ElMessageBox } from 'element-plus';

//状态
const isEnabledData = ref([{ name: '启用', value: 1 }, { name: '停用', value: 0 }])

//表格数据
const majorData = ref([])

//条件查询专业
const queryAll = async () => {
  try {
    const result = await queryAllApi(majorQueryParam.value);
    if (result.code === 1) {
      //赋值
      majorData.value = result.data.records;
      //总记录数
      total.value = result.data.total;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('条件查询专业失败: ', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//钩子函数
onMounted(() => {
  //查询全部专业
  queryAll()
})

//搜索框数据
const majorQueryParam = ref({
  pageNum: 1,
  pageSize: 10,
  name: '',
  department: '',
  isEnabled: null
})

//分页总记录数
const total = ref(null)

//用户点击清空按钮触发
const clearMethod = () => {
  //清空表单数据
  majorQueryParam.value = {
    pageNum: 1,
    pageSize: 10,
    name: '',
    department: '',
    isEnabled: null
  }
  //重新查询
  queryAll();
}

//对话框
const dialogFormVisible = ref(false);

const title = ref('');

//对话框表单数据
const majorForm = ref({
  code: '',
  name: '',
  department: '',
  isEnabled: null
})

// 获取表单 ref（用于手动校验）
const majorFormRef = ref();

//表单校验规则
const rules = reactive({
  code: [
    { required: true, message: '请输入专业代码', trigger: 'blur' },
    {
      pattern: /^\d{6}$/,
      message: '专业代码必须为6位数字',
      trigger: 'blur'
    }
  ],
  name: [
    { required: true, message: '请输入专业名称', trigger: 'blur' },
    { min: 2, max: 20, message: '专业名称长度在2~20个字符', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请输入所属院系', trigger: 'blur' },
    { min: 2, max: 30, message: '院系名称长度在2~30个字符', trigger: 'blur' }
  ],
  isEnabled: [
    { required: true, message: '请选择启用状态', trigger: 'change' }
  ]
});

//点击新增专业触发
const addMajor = () => {
  //标题
  title.value = '新增专业';
  //显示对话框
  dialogFormVisible.value = true;
  // 重置表单校验状态（不仅可以重置表单校验规则，还可以清空表单数据）
  majorFormRef.value?.resetFields();
}

//点击对话框确定按钮触发
const submit = async () => {
  //表单校验
  // ✅ 使用 await 等待校验完成，并捕获异常
  try {
    await majorFormRef.value.validate();
    // 只有校验通过才会走到这里
  } catch (error) {
    // 校验失败，自动提示错误信息
    ElMessage.error('请检查表单填写是否正确！');
    return; // ✅ 阻止后续提交
  }
  //新增专业
  if (title.value === '新增专业') {
    try {
      const result = await insertMajorApi(majorForm.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //重新查询
        queryAll();
      } else {
        ElMessage.error(result.msg)
      }
    } catch (error) {
      console.log('新增专业失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }
  //修改专业
  if (title.value === '修改专业') {
    try {
      const result = await updateMajorApi(majorForm.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //查询
        queryAll();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('修改专业失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

}

//点击修改按钮触发
const updateMajor = async (code) => {
  //标题
  title.value = '修改专业';
  //打开对话框
  dialogFormVisible.value = true;
  // 重置表单校验状态（不仅可以重置表单校验规则，还可以清空表单数据）
  majorFormRef.value?.resetFields();
  //查询回显数据
  try {
    const result = await queryMajorById(code);
    if (result.code === 1) {
      //赋值
      majorForm.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询回显失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//启用停用按钮
const changeStatus = async (code, status) => {
  // 1. 根据状态生成操作名称和提示信息
  const action = status === 0 ? '停用' : '启用';
  const confirmMessage = `你确定要${action}该专业吗？`;
  const confirmType = status === 0 ? 'warning' : 'info'; // 停用用警告色，启用用普通色

  try {
    // 2. 弹出确认框（无论启用还是停用）
    await ElMessageBox.confirm(confirmMessage, '操作确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: confirmType
    });

    // 3. 调用接口
    const result = await startOrStopAPi(code, status);

    // 4. 处理结果
    if (result.code === 1) {
      ElMessage.success(`${action}专业成功！`);
      queryAll(); // 刷新列表
    } else {
      ElMessage.error(`${action}失败：${result.msg || '请稍后重试'}`);
    }
  } catch (error) {
    // 5. 错误处理：仅当不是“用户取消”时才提示错误
    if (error !== 'cancel') {
      ElMessage.error('操作异常，请稍后重试');
      console.error(`[${action}] 专业时发生错误:`, error);
    }
    // 如果是用户点击“取消”，静默退出，不打扰用户
  }
}

//单个删除按钮
const deleteMajor = (code) => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该专业吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    const result = await deleteBatchApi([code]); //id参数如果和批量删除共用一个APi，后端是需要集合，可以写[id]
    if (result.code) {
      ElMessage.success(result.data);
      queryAll();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}

//批量删除专业
//记录勾选的专业的code
const selectedCodes = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
const handleSelectionChange = (selection) => {
  selectedCodes.value = selection.map(item => item.code);
}

//批量删除
const deleteBatch = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该专业吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    if (selectedCodes.value && selectedCodes.value.length > 0) {
      const result = await deleteBatchApi(selectedCodes.value);
      if (result.code) {
        ElMessage.success(result.data);
        queryAll();
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
  <h3 style="margin-top: 0px;">专业管理</h3>
  <!-- 搜索栏 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px;">
    专业名称:
    <el-input v-model="majorQueryParam.name" size="large" style="width: 280px" placeholder="请输入专业名称" clearable
      @clear="queryAll" />

    所属院系:
    <el-input v-model="majorQueryParam.department" size="large" style="width: 280px" placeholder="请输入所属院系" clearable
      @clear="queryAll" />

    状态:
    <el-select v-model="majorQueryParam.isEnabled" size="large" placeholder="请选择状态" style="width: 280px"
      @change="queryAll">
      <el-option v-for="item in isEnabledData" :key="item.value" :label="item.name" :value="item.value" />
    </el-select>

    <el-button type="success" size="large" @click="queryAll" style="margin-left: 140px">查询</el-button>
    <el-button type="primary" size="large" @click="clearMethod" style="margin-left: 20px">清空</el-button>
  </div>

  <!-- 新增专业按钮 -->
  <div>
    <el-button type="primary" size="large" @click="addMajor()">新增专业 + </el-button>
    <el-button type="danger" size="large" @click="deleteBatch()">批量删除 + </el-button>
  </div>

  <!-- 表格展示区域 -->
  <div style="margin-top: 20px;">
    <el-table :data="majorData" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" />
      <el-table-column prop="code" label="专业编码" width="200" align="center" />
      <el-table-column prop="name" label="专业名称" width="220" align="center" />
      <el-table-column prop="department" label="所属院系" width="220" align="center" />
      <el-table-column prop="isEnabled" label="状态" width="200" align="center">
        <template #default="scope">
          <span class="status-dot" :class="scope.row.isEnabled === 1 ? 'status-active' : 'status-inactive'"></span>
          {{ scope.row.isEnabled === 1 ? '启用' : '停用' }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="最后修改时间" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="default" @click="updateMajor(scope.row.code)"
            :disabled="scope.row.isEnabled === 0">修改</el-button>
          <el-button v-if="scope.row.isEnabled === 1" type="warning" size="default"
            @click="changeStatus(scope.row.code, 0)">停用</el-button><!-- 传0是停用 -->
          <el-button v-else-if="scope.row.isEnabled === 0" type="success" size="default"
            @click="changeStatus(scope.row.code, 1)">启用</el-button><!-- 传1是启用 -->
          <!-- <el-button type="info" size="default" @click="">查看详情</el-button> -->
          <el-button type="danger" size="default" @click="deleteMajor(scope.row.code)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!-- 分页 -->
  <div class="demo-pagination-block" style="margin-top: 20px;">
    <el-pagination v-model:current-page="majorQueryParam.pageNum" v-model:page-size="majorQueryParam.pageSize"
      :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
      @size-change="queryAll" @current-change="queryAll" />
  </div>
  <!-- 新增专业对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="600px" center>
    <el-form :model="majorForm" style="width: 80%; margin: 0 auto;" :rules="rules" ref="majorFormRef"
      label-width="100px">
      <!-- 第一行 -->
      <el-form-item label="专业代码" prop="code">
        <el-input v-model="majorForm.code" style="width: 100%;" size="large" :disabled="title === '修改专业'"
          placeholder="请输入专业代码" />
      </el-form-item>
      <!-- 第二行 -->
      <el-form-item label="专业名称" prop="name">
        <el-input v-model="majorForm.name" style="width: 100%;" size="large" placeholder="请输入专业名称" />
      </el-form-item>
      <!-- 第三行 -->
      <el-form-item label="所属院系" prop="department">
        <el-input v-model="majorForm.department" style="width: 100%;" size="large" placeholder="请输入所属院系" />
      </el-form-item>
      <!-- 第四行 -->
      <el-form-item label="是否启用" prop="isEnabled">
        <el-select v-model="majorForm.isEnabled" style="width: 100%;" size="large" placeholder="请选择是否启用">
          <el-option v-for="isEnabled in isEnabledData" :key="isEnabled.value" :label="isEnabled.name"
            :value="isEnabled.value" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submit()">
          确定
        </el-button>
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
  background-color: #67c23a; /* Element Plus success green */
}

:deep(.status-inactive) {
  background-color: #f56c6c; /* Element Plus error red */
}
</style>