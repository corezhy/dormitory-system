<script setup>
//导包
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryIsEnabledApi as queryBuildingAllApi } from '@/api/building'
import { selectAll as queryMajorAllApi } from '@/api/major'
import { conditionQueryFloorApi, insertFloorApi, selectByIdApi, updateByIdApi, deleteByIdApi } from '@/api/floor'

//性别类型数据
const genders = ref([{ name: '男', value: 0 }, { name: '女', value: 1 }])

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
  //查询楼层
  queryFloor();
})

//搜索栏动态数据绑定
const searchForm = ref({
  pageNum: 1,
  pageSize: 10,
  buildingId: null,
  majorCode: null,
  gender: null
})

//总记录数
const total = ref(0)

//查询结果
const floorQueryResult = ref([]);

//查询楼层方法
const queryFloor = async () => {
  try {
    const result = await conditionQueryFloorApi(searchForm.value);
    if (result.code === 1) {
      //查询结果赋值
      floorQueryResult.value = result.data.records;
      //total赋值
      total.value = result.data.total;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('网络错误', error)
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//清空按钮触发
const clearMethod = () => {
  //搜索表单置空
  searchForm.value = {
    buildingId: null,
    majorCode: null,
    gender: null
  }
  //重新查询
  queryFloor();
}

//对话框是否显示
const dialogFormVisible = ref(false)

//对话框标题
const title = ref('')

//对话框数据
const floorDialogData = ref({
  buildingId: null,
  majorCode: '',
  totalRooms: null,
  floorNumber: null //当前楼层号
})

//点击新增楼层触发
const addFloor = () => {
  floorDialogData.value = {
    buildingId: null,
    majorCode: '',
    totalRooms: null
  };
  //打开对话框
  dialogFormVisible.value = true;
  //修改标题
  title.value = '新增楼层';
  //重置表单校验规则
  floorFormRef.value?.resetFields();
}

// 表单 ref（用于触发校验）
const floorFormRef = ref();

// 表单校验规则
const rules = reactive({
  buildingId: [
    { required: true, message: '请选择楼栋名称', trigger: 'change' }
  ],
  majorCode: [
    { required: true, message: '请选择专业名称', trigger: 'change' }
  ],
  totalRooms: [
    { required: true, message: '请输入宿舍数量', trigger: 'blur' },
    { type: 'number', min: 1, max: 200, message: '宿舍数量应在 1~200 之间', trigger: 'blur' }
  ]
});

//定义表示要修改的楼层Id
const floorId = ref(null);

//点击对话框确定按钮触发
const handleFloor = async () => {
  //表单校验
  // ✅ 使用 await 等待校验完成，并捕获异常
  try {
    await floorFormRef.value.validate();
    // 只有校验通过才会走到这里
  } catch (error) {
    // 校验失败，自动提示错误信息
    ElMessage.error('请检查表单填写是否正确！');
    return; // ✅ 阻止后续提交
  }

  //如果majorCode是空字符串，转为null
  if (floorDialogData.value.majorCode === '00000') {
    floorDialogData.value.majorCode = null;
  }

  if (title.value === '新增楼层') {
    try {
      const result = await insertFloorApi(floorDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //重新查询数据
        queryFloor();
      } else {
        ElMessage.error(result.msg)
      }
    } catch (error) {
      console.log('新增楼层失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

  if (title.value === '修改楼层') {
    try {
      const result = await updateByIdApi(floorId.value, floorDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //重新查询
        queryFloor();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('修改楼层失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

}

//点击编辑按钮触发
const editFloor = async (id) => {
  //打开对话框
  dialogFormVisible.value = true;
  //标题
  title.value = '修改楼层';
  //查询回显
  try {
    const result = await selectByIdApi(id);
    if (result.code === 1) {
      //赋值
      floorDialogData.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询回显楼层信息失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
  //为修改楼层Id赋值
  floorId.value = id;
}

//点击删除按钮时触发
const deleteFloor = async (id) => {
  //弹出警告框
  ElMessageBox.confirm('此操作将永久删除该楼层，是否继续？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    //删除楼层
    try {
      const result = await deleteByIdApi(id);
      if(result.code === 1) {
        //提示
        ElMessage.success(result.data);
        //重新查询数据
        queryFloor();
      }else {
        ElMessage.error(result.msg)
      }
    } catch (error) {
      ElMessage.error('网络错误，请稍后重试');
      console.error('删除部门失败：', error);
    }
  }).catch(() => {
    //用户点击取消时会进入这里，通常无处理
  })
}

</script>

<template>
  <!-- 标题 -->
  <h3 style="margin-top: 0px;">楼层管理</h3>
  <!-- 搜索栏 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px;">
    楼栋名称:
    <el-select v-model="searchForm.buildingId" size="large" placeholder="请选择楼栋名称" style="width: 240px"
      @change="queryFloor()">
      <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>

    专业名称:
    <el-select v-model="searchForm.majorCode" size="large" placeholder="请选择专业名称" style="width: 240px"
      @change="queryFloor()">
      <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
    </el-select>

    宿舍类型:
    <el-select v-model="searchForm.gender" size="large" placeholder="请选择宿舍类型" style="width: 240px"
      @change="queryFloor()">
      <el-option v-for="item in genders" :key="item.value" :label="item.name" :value="item.value" />
    </el-select>
    <el-button type="primary" size="large" @click="clearMethod()">清空</el-button>
  </div>
  <!-- 新增楼层和批量删除楼层按钮 -->
  <div style="text-align: right; margin-bottom: 30px;">
    <el-button size="large" type="primary" @click="addFloor()">新增楼层+</el-button>
  </div>
  <!-- 表格 -->
  <el-table :data="floorQueryResult" stripe style="width: 100%">
    <el-table-column prop="buildingName" label="楼栋名称" width="200" align="center" />
    <el-table-column prop="floorNumber" label="楼层号" width="180" align="center" />
    <el-table-column prop="majorName" label="专业名称" width="200" align="center">
      <template #default="scope">
        {{ scope.row.majorName === null ? '不限' : scope.row.majorName }}
      </template>
    </el-table-column>
    <el-table-column prop="genderType" label="宿舍类型" width="200" align="center">
      <template #default="scope">
        {{ scope.row.genderType === 0 ? '男生宿舍' : '女生宿舍' }}
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="最后修改时间" align="center" />
    <el-table-column label="操作" width="200" align="center">
      <template #default="scope">
        <el-button size="default" type="primary" @click="editFloor(scope.row.id)">编辑</el-button>
        <el-button size="default" type="danger" @click="deleteFloor(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页 -->
  <div class="demo-pagination-block" style="margin-top: 20px;">
    <el-pagination v-model:current-page="searchForm.pageNum" v-model:page-size="searchForm.pageSize"
      :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
      @size-change="queryFloor()" @current-change="queryFloor()" />
  </div>
  <!-- 新增修改楼层对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="600px" center>
    <el-form :model="floorDialogData" style="width: 80%; margin: 0 auto;" :rules="rules" ref="floorFormRef"
      label-width="100px">
      <!-- 第一行 -->
      <el-form-item label="楼栋名称" prop="buildingId">
        <el-select v-model="floorDialogData.buildingId" placeholder="请选择楼栋名称" style="width: 100%" size="large"
          :disabled="title === '修改楼层'">
          <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <!-- 第二行 -->
      <el-form-item label="专业名称" prop="majorCode">
        <el-select v-model="floorDialogData.majorCode" placeholder="请选择专业名称" style="width: 100%" size="large">
          <el-option label="不限" value="00000" /> <!-- 将不限默认值改为00000，新增或修改时判断，如果等于00000就改为null -->
          <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
        </el-select>
      </el-form-item>
      <!-- 第三行 -->
      <el-form-item label="宿舍数量" prop="totalRooms">
        <el-input-number v-model="floorDialogData.totalRooms" :min="1" :max="200" style="width: 70%;"
          placeholder="请输入该层宿舍数量" size="large" :disabled="title === '修改楼层'" />
      </el-form-item>
      <!-- 修改时才有，展示当前楼层号 -->
      <el-form-item v-if="title === '修改楼层'" label="当前楼层">
        <el-input v-model="floorDialogData.floorNumber" suffix-icon="House" size="large" readonly style="width: 70%">
          <template #suffix>
            楼
          </template>
        </el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleFloor()">确定</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped></style>