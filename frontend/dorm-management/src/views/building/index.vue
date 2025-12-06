<script setup>
// 导包
import { onMounted, ref, reactive } from 'vue';
import { queryAllApi, insertBuildingApi, selectById, updateBuildingApi, startAndStop, deleteByIdApi } from '@/api/building';
import { ElMessage, ElMessageBox } from 'element-plus';

// 性别类型
const genders = ref([{ name: '男', value: 0 }, { name: '女', value: 1 }]);

// 楼栋数据
const buildingData = ref([]);

// 查询楼栋
const queryBuilding = async () => {
  try {
    const result = await queryAllApi();
    if (result.code === 1) {
      buildingData.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('网络错误', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
};

onMounted(() => {
  queryBuilding();
});

// 自定义宿舍相关
const isCustomMode = ref(false);
const floorConfigs = ref([]);

// 监听楼层数变化，用 roomsPerFloor 初始化配置
const handleFloorNumChange = (val) => {
  if (!val || val <= 0) {
    floorConfigs.value = [];
    return;
  }
  const defaultRooms = buildingForm.value.roomsPerFloor || 30;
  floorConfigs.value = Array.from({ length: val }, (_, i) => ({
    floorNumber: i + 1,
    roomCount: defaultRooms
  }));
};

// 对话框控制
const dialogFormVisible = ref(false);
const title = ref('');
const buildingId = ref(null);
const buildingFormRef = ref();

// 表单数据
const buildingForm = ref({
  name: '',
  code: '',
  genderType: null,
  description: '',
  floorNum: null,
  roomsPerFloor: 30
});

// 表单校验规则 —— 关键：roomsPerFloor 不再 required！
const rules = reactive({
  name: [
    { required: true, message: '请输入楼栋名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入楼栋编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{2,20}$/, message: '编码只能包含字母和数字，2-20位', trigger: 'blur' }
  ],
  genderType: [
    { required: true, message: '请选择性别类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入楼栋描述', trigger: 'blur' },
    { max: 200, message: '描述不能超过200字', trigger: 'blur' }
  ],
  floorNum: [
    { required: true, message: '请输入楼层数', trigger: 'blur' },
    { type: 'number', min: 1, max: 10, message: '楼层数必须为1-10之间的整数', trigger: 'blur' }
  ],
  // ✅ 关键修改：移除 required，只保留数值范围
  roomsPerFloor: [
    { type: 'number', min: 1, max: 50, message: '每层宿舍数必须为1-50之间的整数', trigger: 'blur' }
  ]
});

// 点击新增
const addBuilding = () => {
  dialogFormVisible.value = true;
  title.value = "新增楼栋";
  buildingFormRef.value?.resetFields();
  isCustomMode.value = false;
  floorConfigs.value = [];
};

// 提交
const submit = async () => {
  try {
    // 先整体校验（此时 roomsPerFloor 可为空）
    await buildingFormRef.value.validate();

    // 自定义模式额外校验
    if (title.value === "新增楼栋" && isCustomMode.value) {
      const invalid = floorConfigs.value.some(cfg => cfg.roomCount == null || cfg.roomCount < 0 || cfg.roomCount > 50);
      if (invalid) {
        ElMessage.error('请填写所有楼层的有效宿舍数量（0-50）');
        return;
      }
    }

    if (title.value === "新增楼栋") {
      // ✅ 构造 payload：自定义模式时删除 roomsPerFloor
      const payload = { ...buildingForm.value };
      if (isCustomMode.value) {
        payload.floorConfigs = floorConfigs.value;
        delete payload.roomsPerFloor; // 👈 关键：不让它传过去
      }

      const result = await insertBuildingApi(payload);
      if (result.code === 1) {
        dialogFormVisible.value = false;
        ElMessage.success(result.data);
        queryBuilding();
      } else {
        ElMessage.error(result.msg);
      }
    } else if (title.value === '修改楼栋') {
      const result = await updateBuildingApi(buildingId.value, buildingForm.value);
      if (result.code === 1) {
        dialogFormVisible.value = false;
        ElMessage.success(result.data);
        queryBuilding();
      } else {
        ElMessage.error(result.msg);
      }
    }
  } catch (error) {
    ElMessage.error('请检查表单填写是否正确！');
  }
};

// 修改
const updateBuilding = async (id) => {
  title.value = '修改楼栋';
  dialogFormVisible.value = true;
  try {
    const result = await selectById(id);
    if (result.code === 1) {
      buildingForm.value = result.data;
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    console.log('网络错误', error);
    ElMessage.error('网络错误，请检查网络后重试');
  }
  buildingId.value = id;
};

// 启用/停用
const changeStatus = async (id, targetStatus) => {
  const action = targetStatus === 0 ? '停用' : '启用';
  const confirmMessage = `你确定要${action}该楼栋吗？`;
  const confirmType = targetStatus === 0 ? 'warning' : 'info';

  try {
    await ElMessageBox.confirm(confirmMessage, '操作确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: confirmType
    });

    const result = await startAndStop(id, targetStatus);
    if (result.code === 1) {
      ElMessage.success(`${action}楼栋成功！`);
      queryBuilding();
    } else {
      ElMessage.error(`${action}失败：${result.msg || '请稍后重试'}`);
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作异常，请稍后重试');
      console.error(`[${action}] 楼栋时发生错误:`, error);
    }
  }
};

// 删除
const deleteBuilding = (id) => {
  ElMessageBox.confirm('此操作将永久删除该楼栋，是否继续？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const result = await deleteByIdApi(id);
      if (result.code === 1) {
        ElMessage.success(result.data || '删除楼栋成功');
        queryBuilding();
      } else {
        ElMessage.error(result.msg || '删除楼栋失败');
      }
    } catch (error) {
      console.error('删除楼栋失败：', error);
      ElMessage.error('网络错误，请稍后重试');
    }
  }).catch(() => { });
};
</script>

<template>
  <h3 style="margin-top: 0px;">楼栋管理</h3>
  <div style="text-align: right;">
    <el-button type="primary" size="large" @click="addBuilding()">新增楼栋 + </el-button>
  </div>

  <!-- 新增/修改对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="title" width="600px" center>
    <el-form :model="buildingForm" style="width: 80%; margin: 0 auto;" :rules="rules" ref="buildingFormRef"
      label-width="100px">
      <el-form-item label="楼栋名称" prop="name">
        <el-input v-model="buildingForm.name" style="width: 100%;" size="large" placeholder="请输入楼栋名称" />
      </el-form-item>

      <el-form-item label="楼栋编码" prop="code">
        <el-input v-model="buildingForm.code" style="width: 100%;" size="large" placeholder="请输入楼栋编码" />
      </el-form-item>

      <el-form-item label="性别类型" prop="genderType">
        <el-select v-model="buildingForm.genderType" style="width: 100%;" size="large" placeholder="请选择性别类型">
          <el-option v-for="gender in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
        </el-select>
      </el-form-item>

      <el-form-item label="楼栋描述" prop="description">
        <el-input v-model="buildingForm.description" type="textarea" :rows="4" style="width: 100%;" size="large"
          placeholder="请输入楼栋描述" />
      </el-form-item>

      <!-- 楼层数 -->
      <el-form-item v-if="title === '新增楼栋'" label="楼层数" prop="floorNum">
        <el-input-number v-model="buildingForm.floorNum" :min="1" :max="10" @change="handleFloorNumChange"
          style="width: 60%;" size="large" placeholder="请输入楼层数" />
      </el-form-item>

      <!-- 统一宿舍数（仅简单模式显示） -->
      <el-form-item v-if="title === '新增楼栋' && !isCustomMode" label="每层宿舍数" prop="roomsPerFloor">
        <el-input-number v-model="buildingForm.roomsPerFloor" :min="1" :max="50" style="width: 60%;" size="large"
          placeholder="请输入宿舍数量" />
      </el-form-item>

      <!-- 自定义开关 -->
      <el-form-item v-if="title === '新增楼栋'" label="宿舍配置">
        <el-switch v-model="isCustomMode" :active-value="true" :inactive-value="false" active-text="自定义每层宿舍数"
          inactive-text="统一设置" />
      </el-form-item>

      <!-- 自定义模式表格（美化版） -->
      <el-form-item v-if="title === '新增楼栋' && isCustomMode" label="">
        <el-table :data="floorConfigs" style="width: 100%" border size="default">
          <el-table-column label="楼层" width="120" align="center">
            <template #default="scope">
              {{ scope.row.floorNumber }} 楼
            </template>
          </el-table-column>
          <el-table-column label="宿舍数量" align="center">
            <template #default="scope">
              <el-input-number v-model="scope.row.roomCount" :min="0" :max="50" controls-position="right" size="large"
                style="width: 100%;" />
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 8px; color: #999; font-size: 12px; text-align: center;">
          提示：宿舍数量可为 0（表示该层无宿舍）
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="submit()">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <!-- 楼栋列表 -->
  <div>
    <el-table :data="buildingData" stripe style="width: 100%">
      <el-table-column prop="name" label="楼栋名称" width="200" align="center" />
      <el-table-column prop="code" label="楼栋编码" width="200" align="center" />
      <el-table-column prop="status" label="状态" width="200" align="center">
        <template #default="scope">
          <span class="status-dot" :class="scope.row.status === 1 ? 'status-active' : 'status-inactive'"></span>
          {{ scope.row.status === 1 ? '启用' : '停用' }}
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" width="220" align="center" />
      <el-table-column prop="updateTime" label="最后修改时间" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="default" @click="updateBuilding(scope.row.id)"
            :disabled="scope.row.status === 0">
            修改
          </el-button>
          <el-button v-if="scope.row.status === 1" type="warning" size="default" @click="changeStatus(scope.row.id, 0)">
            停用
          </el-button>
          <el-button v-else-if="scope.row.status === 0" type="success" size="default"
            @click="changeStatus(scope.row.id, 1)">
            启用
          </el-button>
          <!-- <el-button type="info" size="default" @click="">查看详情</el-button> -->
          <el-button type="danger" size="default" @click="deleteBuilding(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
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