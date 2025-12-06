<script setup>
//导包
import { ref, onMounted, reactive, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { selectAll as queryMajorAllApi } from '@/api/major'
import { queryIsEnabledApi as queryBuildingAllApi } from '@/api/building'
import { getFloorNumberByBuildingIdApi as queryFloorNumber } from '@/api/floor'
import { conditionQueryDormRoomApi, insertDormApi, getDormRoomByIdApi, updateByIdApi, startOrStopApi, deleteBatchApi } from '@/api/dorm'

//搜索栏动态数据绑定
const searchForm = ref({
  pageNum: 1,
  pageSize: 10,
  buildingId: null,
  majorCode: null,
  floorId: null
})

//存储楼栋下楼层数据(搜索栏)
const searchFloorOptions = ref([])

//查询楼栋下楼层
const queryFloor = async () => {
  try {
    const result = await queryFloorNumber(searchForm.value.buildingId);
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
  //查询宿舍
  queryDorm();
})

//清空按钮触发
const clearMethod = () => {
  //搜索表单置空
  searchForm.value = {
    pageNum: 1,
    pageSize: 10,
    buildingId: null,
    majorCode: null,
    floorNumber: null
  }
  //重新查询
  queryDorm();
}

//侦听楼栋id有值后查询楼层
watch(
  () => searchForm.value.buildingId,
  async (newBuildingId) => {
    if (newBuildingId !== null) {
      await queryFloor(); // 👈 唯一的入口
    } else {
      searchFloorOptions.value = [];
      searchForm.value.floorId = null;
    }
  }
);

//选中下拉框触发，查询宿舍
const queryDorm = async () => {
  try {
    const result = await conditionQueryDormRoomApi(searchForm.value);
    if (result.code === 1) {
      //赋值
      dormTableData.value = result.data.records;
      total.value = result.data.total;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('查询宿舍失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
}

//表格数据展示
const dormTableData = ref([]);

//分页总记录数
const total = ref(null);

//对话框是否显示
const dialogFormVisible = ref(false)

//对话框标题
const title = ref('')

//对话框数据
const dormDialogData = ref({
  buildingId: null,
  floorId: null,
  capacity: null, //总床位数
  fullCode: '' //房间号
})

//点击新增宿舍触发
const addDorm = () => {
  //打开对话框
  dialogFormVisible.value = true;
  //标题
  title.value = '新增宿舍';
  //清空数据并重置表单校验
  dormDialogData.value = {
    buildingId: null,
    floorId: null,
    capacity: null,
    fullCode: ''
  }
  //清空楼层选项列表
  dialogFloorOptions.value = [];
  dormFormRef.value?.resetFields();
}

//存储楼栋下楼层数据(对话框)
const dialogFloorOptions = ref([])

// 对话框中楼栋变化时，查询对应楼层数
const handleDialogBuildingChange = async () => {
  dormDialogData.value.floorId = null; // 清空已选楼层
  if (dormDialogData.value.buildingId) {
    try {
      const result = await queryFloorNumber(dormDialogData.value.buildingId);
      dialogFloorOptions.value = result.code === 1 ? result.data : [];
    } catch (error) {
      ElMessage.error('加载楼层失败');
      dialogFloorOptions.value = [];
    }
  } else {
    dialogFloorOptions.value = []; // 没选楼栋，清空
  }
}

// 表单引用
const dormFormRef = ref()

// 表单校验规则
const rules = reactive({
  buildingId: [
    { required: true, message: '请选择楼栋名称', trigger: 'change' }
  ],
  floorId: [
    { required: true, message: '请选择楼层号', trigger: 'change' }
  ],
  capacity: [
    { required: true, message: '请输入总床位数', trigger: 'blur' },
    { type: 'number', min: 1, max: 12, message: '床位数必须为1-12之间的整数', trigger: 'blur' }
  ]
})

//点击对话框确定按钮触发
const submitDormForm = async () => {
  //表单校验
  // ✅ 使用 await 等待校验完成，并捕获异常
  try {
    await dormFormRef.value.validate();
    // 只有校验通过才会走到这里
  } catch (error) {
    // 校验失败，自动提示错误信息
    ElMessage.error('请检查表单填写是否正确！');
    return; // ✅ 阻止后续提交
  }
  //新增宿舍
  if (title.value === '新增宿舍') {
    try {
      const result = await insertDormApi(dormDialogData.value);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //重新查询
        queryDorm();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('新增宿舍失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

  //修改宿舍
  if (title.value === '修改宿舍') {
    try {
      //修改时只传id和修改的宿舍总床铺数
      const updateData = {
        capacity: dormDialogData.value.capacity
      }
      const result = await updateByIdApi(dormId.value, updateData);
      if (result.code === 1) {
        //关闭对话框
        dialogFormVisible.value = false;
        //提示
        ElMessage.success(result.data);
        //重新查询
        queryDorm();
      } else {
        ElMessage.error(result.msg);
      }
    } catch (error) {
      console.log('修改宿舍失败', error);
      ElMessage.error('网络错误，请检查网络后重试');
    }
  }

}

//定义宿舍Id用于修改宿舍
const dormId = ref(null);

//点击编辑按钮触发
const editDorm = async (id) => {
  dialogFormVisible.value = true;
  title.value = '修改宿舍';

  try {
    // 第一步：确保楼栋列表已加载（通常 onMounted 已加载，但保险起见）
    if (buildingOptions.value.length === 0) {
      await queryBuilding(); // 重新加载楼栋
    }

    // 第二步：查询宿舍详情
    const result = await getDormRoomByIdApi(id);
    if (result.code !== 1) {
      ElMessage.error(result.msg);
      return;
    }

    const dorm = result.data;

    // 第三步：根据 buildingId 加载对应楼层
    if (dorm.buildingId) {
      const floorResult = await queryFloorNumber(dorm.buildingId);
      dialogFloorOptions.value = floorResult.code === 1 ? floorResult.data : [];
    }

    // 第四步：最后再赋值回显数据！
    dormDialogData.value = {
      buildingId: dorm.buildingId,
      floorId: dorm.floorId,
      capacity: dorm.capacity,
      fullCode: dorm.fullCode
    };

    dormFormRef.value?.resetFields(); // 清除校验状态

  } catch (error) {
    console.log('编辑宿舍失败', error);
    ElMessage.error('网络错误，请检查网络后重试');
    dialogFormVisible.value = false; // 失败则关闭对话框
  }

  //查询回显的Id就是用户要修改的宿舍Id，赋值
  dormId.value = id;
};

//点击启用停用按钮触发
const startOrStopDorm = async (id, status) => {
  // 1. 根据状态生成操作名称和提示信息
  const action = status === 0 ? '停用' : '启用';
  const confirmMessage = `你确定要${action}该宿舍吗？`;
  const confirmType = status === 0 ? 'warning' : 'info'; // 停用用警告色，启用用普通色

  try {
    // 2. 弹出确认框（无论启用还是停用）
    await ElMessageBox.confirm(confirmMessage, '操作确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: confirmType
    });

    // 3. 调用接口
    const result = await startOrStopApi(id, status);

    // 4. 处理结果
    if (result.code === 1) {
      ElMessage.success(`${action}宿舍成功！`);
      queryDorm(); // 刷新列表
    } else {
      ElMessage.error(`${action}失败：${result.msg || '请稍后重试'}`);
    }
  } catch (error) {
    // 5. 错误处理：仅当不是“用户取消”时才提示错误
    if (error !== 'cancel') {
      ElMessage.error('操作异常，请稍后重试');
      console.error(`[${action}] 宿舍时发生错误:`, error);
    }
    // 如果是用户点击“取消”，静默退出，不打扰用户
  }
}

//批量删除宿舍
//记录勾选的宿舍id
const selectedIds = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
}

//点击删除按钮触发
const deleteById = (id) => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该宿舍吗？', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    const result = await deleteBatchApi([id]);
    if (result.code === 1) {
      ElMessage.success(result.data);
      queryDorm();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}

//点击批量删除按钮触发
const deleteByIds = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该宿舍吗？', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => { //确认
    if (selectedIds.value && selectedIds.value.length > 0) {
      const result = await deleteBatchApi(selectedIds.value);
      if (result.code === 1) {
        ElMessage.success(result.data);
        queryDorm();
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
  <h3 style="margin-top: 0px;">宿舍管理</h3>
  <!-- 搜索栏 -->
  <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px;">
    楼栋名称:
    <el-select v-model="searchForm.buildingId" size="large" placeholder="请选择楼栋名称" style="width: 240px"
      @change="queryDorm()">
      <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>

    专业名称:
    <el-select v-model="searchForm.majorCode" size="large" placeholder="请选择专业名称" style="width: 240px"
      @change="queryDorm()">
      <el-option v-for="item in majorOptions" :key="item.code" :label="item.name" :value="item.code" />
    </el-select>

    楼层号:
    <el-select v-model="searchForm.floorId" size="large" placeholder="请选择楼层号" style="width: 240px"
      @change="queryDorm()">
      <el-option v-for="item in searchFloorOptions" :key="item.id" :label="item.floorNumber" :value="item.id" />
    </el-select>
    <el-button type="primary" size="large" @click="clearMethod()">清空</el-button>
  </div>
  <!-- 新增宿舍和批量删除宿舍按钮 -->
  <div style="margin-bottom: 20px;">
    <el-button size="large" type="primary" @click="addDorm()">新增宿舍+</el-button>
    <el-button size="large" type="danger" @click="deleteByIds()">批量删除-</el-button>
  </div>
  <!-- 表格 -->
  <el-table :data="dormTableData" stripe style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" align="center" width="50" />
    <el-table-column prop="buildingName" label="楼栋名称" width="160" align="center" />
    <el-table-column prop="floorNumber" label="楼层号" width="130" align="center" />
    <el-table-column prop="fullCode" label="房间号" width="160" align="center" />
    <el-table-column prop="majorName" label="专业名称" width="220" align="center">
      <template #default="scope">
        {{ scope.row.majorName === null ? '不限' : scope.row.majorName }}
      </template>
    </el-table-column>
    <el-table-column prop="capacity" label="总床位数" width="140" align="center" />
    <el-table-column prop="availableBeds" label="空闲床铺数" width="140" align="center" />
    <el-table-column prop="status" label="状态" width="160" align="center">
      <template #default="scope">
        <span class="status-dot" :class="scope.row.status === 1 ? 'status-active' : 'status-inactive'"></span>
        {{ scope.row.status === 1 ? '正常' : '停用/维修' }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="default" type="primary" @click="editDorm(scope.row.id)">编辑</el-button>
        <el-button v-if="scope.row.status === 1" type="warning" size="default"
          @click="startOrStopDorm(scope.row.id, 0)">停用</el-button><!-- 传0是停用 -->
        <el-button v-else-if="scope.row.status === 0" type="success" size="default"
          @click="startOrStopDorm(scope.row.id, 1)">启用</el-button><!-- 传1是启用 -->
        <!-- <el-button type="info" size="default" @click="">查看详情</el-button> -->
        <el-button size="default" type="danger" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页 -->
  <div class="demo-pagination-block" style="margin-top: 20px;">
    <el-pagination v-model:current-page="searchForm.pageNum" v-model:page-size="searchForm.pageSize"
      :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
      @size-change="queryDorm()" @current-change="queryDorm()" />
  </div>
  <!-- 新增修改宿舍对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="600px" center>
    <el-form :model="dormDialogData" style="width: 80%; margin: 0 auto;" :rules="rules" ref="dormFormRef"
      label-width="100px">
      <!-- 第一行 -->
      <el-form-item label="楼栋名称" prop="buildingId">
        <el-select v-model="dormDialogData.buildingId" placeholder="请选择楼栋名称" style="width: 100%" size="large"
          :disabled="title === '修改宿舍'" @change="handleDialogBuildingChange()">
          <el-option v-for="item in buildingOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <!-- 第二行 -->
      <el-form-item label="楼层号" prop="floorId">
        <el-select v-model="dormDialogData.floorId" placeholder="请选择楼层号" style="width: 100%" size="large"
          :disabled="title === '修改宿舍'">
          <el-option v-for="item in dialogFloorOptions" :key="item.id" :label="item.floorNumber" :value="item.id" />
        </el-select>
      </el-form-item>
      <!-- 第三行（房间号，仅修改时展示） -->
      <el-form-item label="房间号" v-if="title === '修改宿舍'">
        <el-input v-model="dormDialogData.fullCode" style="width: 100%" size="large" disabled />
      </el-form-item>
      <!-- 第四行 -->
      <el-form-item label="总床位数" prop="capacity">
        <el-input-number v-model="dormDialogData.capacity" :min="1" :max="12" style="width: 70%;"
          placeholder="请输入宿舍总床位数" size="large" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDormForm()">确定</el-button>
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