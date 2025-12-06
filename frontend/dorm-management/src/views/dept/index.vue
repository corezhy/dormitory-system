<script setup>
import { ref, onMounted, reactive } from 'vue';
import { queryAll, addDeptApi, updateDeptApi, queryByIdApi, deleteDeptApi } from '@/api/dept';
import { ElMessage, ElMessageBox } from 'element-plus';

//钩子函数
onMounted(() => {
    searchDept();
})

//查询部门
const searchDept = async () => {
    try {
        const result = await queryAll()
        if (result.code !== 1) {
            ElMessage.error(result.msg || '操作失败');
            return; //提前退出，避免继续执行
        }
        //只有业务成功时，才更新数据
        deptData.value = result.data;
    } catch (error) {
        console.error('查询部门时发生网络错误：', error)
        ElMessage.error('网络连接失败，请检查你的网络或稍后重试')
    }

}
//部门数据
const deptData = ref([])

//新增部门对话框
const dialogFormVisible = ref(false)

//新增部门输入数据
const deptInfo = ref({
    deptCode: '',
    deptName: ''
})

//对话框标题
const title = ref('')

//用户点击了新增或修改部门的确定按钮
const handleDept = async () => {
    //表单校验
    // ✅ 使用 await 等待校验完成，并捕获异常
    try {
        await deptFormRef.value.validate();
        // 只有校验通过才会走到这里
    } catch (error) {
        // 校验失败，自动提示错误信息
        ElMessage.error('请检查表单填写是否正确！');
        return; // ✅ 阻止后续提交
    }

    if (title.value === "新增部门") {
        //新增部门逻辑
        try {
            const result = await addDeptApi(deptInfo.value);
            if (result.code != 1) {
                ElMessage.error(result.msg);
                return;
            }
            ElMessage.success(result.data);
            //关闭对话框
            dialogFormVisible.value = false;
            //重新查询最新数据
            searchDept();
        } catch (error) {
            ElMessage.error('网络错误')
        }
    }
    if (title.value === "修改部门") {
        //修改部门逻辑
        try {
            const result = await updateDeptApi(deptId.value, deptInfo.value);
            if (result.code != 1) {
                ElMessage.error(result.msg);
                return;
            }
            ElMessage.success(result.data);
            //关闭对话框
            dialogFormVisible.value = false;
            //重新查询最新数据
            searchDept();
        } catch (error) {
            ElMessage.error('网络错误');
        }
    }
}

//用户点击了新增部门
const addDept = () => {
    //将标题改为新增部门
    title.value = '新增部门';
    //打开对话框
    dialogFormVisible.value = true;
    // 重置表单校验状态以及数据
    deptFormRef.value?.resetFields();
}

//定义变量接收用户修改部门的id
const deptId = ref(null)

//用户点击了修改按钮
const editDept = async (id) => {
    //将标题改为修改
    title.value = '修改部门';
    //打开对话框
    dialogFormVisible.value = true;
    // 重置表单校验状态
    deptFormRef.value?.resetFields();
    //查询回显
    try {
        const result = await queryByIdApi(id);
        if (result.code != 1) {
            ElMessage.error(result.msg);
            return;
        }
        deptInfo.value = { deptCode: result.data.deptCode, deptName: result.data.deptName }
    } catch (error) {
        ElMessage.error('请求失败，请检查网络')
    }
    //将id赋值给deptId
    deptId.value = id;
}

//表单校验
const deptFormRef = ref()
const rules = reactive({ //表单校验规则
    deptCode: [
        { required: true, message: '请输入部门编码', trigger: 'blur' },
        { min: 2, max: 10, message: '部门编码的长度在2-10之间', trigger: 'blur' }
    ],
    deptName: [
        { required: true, message: '请输入部门名称', trigger: 'blur' },
        { min: 2, max: 10, message: '部门名称的长度在2-10之间', trigger: 'blur' }
    ]
})

//删除部门按钮触发
const deleteDept = (id) => {
    console.log(`Delete item with ID ${id}`)
    //删除部门时，需要弹出一个确认框，如果是确认，则删除部门
    ElMessageBox.confirm('此操作将永久删除该部门，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        //删除部门
        try {
            const result = await deleteDeptApi(id);
            if (result.code === 1) {
                ElMessage.success(result.data || '删除部门成功')
                //查询
                searchDept();
            } else {
                ElMessage.error(result.msg || '删除部门失败');
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
    <h3 style="margin-top: 0px;">部门管理</h3>
    <!-- 新增部门按钮 -->
    <div style="text-align: right;">
        <el-button type="primary" size="large" @click="addDept()">新增部门+</el-button>
    </div>
    <!-- 新增部门对话框 -->
    <el-dialog v-model="dialogFormVisible" :title="title" width="500px" center>
        <el-form :model="deptInfo" style="width: 80%; margin: 0 auto;" :inline="false" :rules="rules" ref="deptFormRef">
            <el-form-item label="部门编码" style="width: 100%; display: flex; justify-content: center;" prop="deptCode">
                <el-input v-model="deptInfo.deptCode" style="width: 100%" placeholder="请输入部门编码" size="large" />
            </el-form-item>

            <el-form-item label="部门名称" style="width: 100%; display: flex; justify-content: center;" prop="deptName">
                <el-input v-model="deptInfo.deptName" style="width: 100%" placeholder="请输入部门名称" size="large" />
            </el-form-item>
        </el-form>

        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="handleDept">确定</el-button>
            </div>
        </template>
    </el-dialog>
    <!-- 表格展示区域 -->
    <div>
        <el-table :data="deptData" style="width: 100%" stripe>
            <el-table-column label="序号" width="300" align="center">
                <template #default="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="deptName" label="部门名称" width="300" align="center" />
            <el-table-column prop="updateTime" label="最后修改时间" width="300" align="center" />
            <el-table-column label="操作" align="center">
                <template #default="scope">
                    <el-button type="primary" size="default" @click="editDept(scope.row.id)">修改</el-button>
                    <el-button type="danger" size="default" @click="deleteDept(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<style scoped></style>