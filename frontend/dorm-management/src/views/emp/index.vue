<script setup>
import { ref, onMounted, reactive } from 'vue'
import { query, addEmpAPi, queryEmpByIdApi, updateEmpByIdApi, deleteEmpByIdsApi } from '@/api/emp'
import { queryAll as queryDeptAll } from '@/api/dept'
import { ElMessage, ElMessageBox } from 'element-plus'

//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 0 }, { name: '未知', value: 2 }])

// 查询条件（统一管理）
const empQueryParam = ref({
    pageNum: 1,
    pageSize: 10,
    name: '',
    deptId: null,
    data: []  // 时间范围
})

// 查询结果
const empQueryResult = ref([])
const total = ref(0)

// 将 Date 对象转为 'yyyy-MM-dd' 字符串
const formatDate = (date) => date ? new Date(date).toISOString().split('T')[0] : null;

// 调用 API 查询
const queryMethod = async () => {
    // 构造请求参数
    const params = {
        pageNum: empQueryParam.value.pageNum,
        pageSize: empQueryParam.value.pageSize,
        name: empQueryParam.value.name || null,
        deptId: empQueryParam.value.deptId || null,
        begin: formatDate(empQueryParam.value.data?.[0]),
        end: formatDate(empQueryParam.value.data?.[1])
    }

    try {
        const result = await query(params)
        console.log('后端返回:', result.data)

        empQueryResult.value = result.data.records || []
        total.value = result.data.total || 0
    } catch (error) {
        console.error('查询失败:', error)
    }
}

// 清空查询条件
const clearMethod = () => {
    empQueryParam.value = {
        pageNum: 1,
        pageSize: 10,
        name: '',
        deptId: null,
        data: []
    }
    queryMethod()
}

//部门下拉框查询
const queryDept = async () => {
    const result = await queryDeptAll();
    if (result.code === 1) {
        deptOptions.value = result.data;
    } else {
        ElMessage.error(result.msg);
    }
}

// 钩子函数
onMounted(() => {
    //初始化查询员工列表
    queryMethod()
    //初始化查询部门数据
    queryDept();
    //获取token
    getToken();
})

//新增修改员工对话框
const title = ref('');
const dialogFormVisible = ref(false)

const employee = ref({
    name: '',
    gender: null,
    phone: '',
    idCard: '',
    entryDate: null,
    position: '',
    deptId: null,
    email: '',
    avatar: '',
    username: '',
    password: ''
})

//重置表单（不仅可以重置表单校验规则，还可以清空表单数据）
const clearEmpFormData = () => {
    empFormRef.value?.resetFields();
}

//用户点击新增按钮触发
const handleAdd = () => {
    //将标题修改为新增员工
    title.value = '新增员工';
    //弹对话框
    dialogFormVisible.value = true;
    //清空对话框
    clearEmpFormData();
}



//部门下拉框数据
const deptOptions = ref([])

//编辑按钮触发
const editEmp = async (id) => {
    //将标题改为修改员工
    title.value = '修改员工';
    //显示对话框
    dialogFormVisible.value = true;
    //重置表单校验状态
    clearEmpFormData();
    //调用Api查询回显数据
    try {
        const result = await queryEmpByIdApi(id);
        if (result.code === 1) {
            //回显数据
            employee.value = result.data;
        } else {
            ElMessage.error(result.msg);
        }
    } catch (error) {
        console.log('网络错误', error)
        ElMessage.error('网络错误，请检查网络后重试')
    }
    //给id赋值
    empId.value = id;
}

//删除按钮触发
const deleteEmp = (id) => {
    //弹出确认框
    ElMessageBox.confirm('您确认删除该员工吗？', '提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => { //确认 
        try {
            const result = await deleteEmpByIdsApi([id]); //传数组
            if (result.code === 1) {
                ElMessage.success(result.data);
                //重新查询数据
                queryMethod();
            } else {
                ElMessage.error(result.msg);
            }
        } catch (error) {
            console.log('网络问题', error)
            ElMessage.error('网络问题，请检查网络');
        }
    }
    ).catch(
        //用户点击取消，通常不处理
    )
}

//文件上传
// 图片上传成功后触发
const handleAvatarSuccess = (response, uploadFile) => {
    employee.value.avatar = response.data
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        ElMessage.error('只支持上传图片')
        return false
    } else if (rawFile.size / 1024 / 1024 > 10) {
        ElMessage.error('只能上传10M以内图片')
        return false
    }
    return true
}

//解决图片上传401问题（没有携带token）
//声明token
const token = ref('')

//获取token
const getToken = () => {
    const loginUser = JSON.parse(localStorage.getItem('loginUser'));
    if (loginUser && loginUser.token) {
        token.value = loginUser.token;
    }
}

//表单校验
const empFormRef = ref()
const rules = reactive({
    name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
        { min: 2, max: 10, message: '姓名长度在2~10个字符', trigger: 'blur' }
    ],
    gender: [
        { required: true, message: '请选择性别', trigger: 'change' }
    ],
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
    ],
    idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}[\dXx]$)/, message: '身份证号格式不正确', trigger: 'blur' }
    ],
    entryDate: [
        { required: true, message: '请选择入职日期', trigger: 'change' }
    ],
    position: [
        { required: true, message: '请输入职位', trigger: 'blur' },
        { min: 2, max: 20, message: '职位名称长度在2~20个字符', trigger: 'blur' }
    ],
    deptId: [
        { required: true, message: '请选择所属部门', trigger: 'change' }
    ],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式不正确', trigger: 'blur' }
    ],
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在3~20个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, message: '密码至少6位', trigger: 'blur' }
    ]
})

//定义修改员工id为null
const empId = ref(null)

//点击对话框确定按钮触发
const confirm = async () => {
    //校验表单
    // ✅ 使用 await 等待校验完成，并捕获异常
    try {
        await empFormRef.value.validate();
        // 只有校验通过才会走到这里
    } catch (error) {
        // 校验失败，自动提示错误信息
        ElMessage.error('请检查表单填写是否正确！');
        return; // ✅ 阻止后续提交
    }
    //判断是新增还是修改
    if (title.value === "新增员工") {
        //发送请求新增员工
        try {
            const result = await addEmpAPi(employee.value);
            if (result.code === 1) {
                ElMessage.success(result.data);
                //关闭对话框
                dialogFormVisible.value = false;
                //重新查询最新数据
                queryMethod()
            } else {
                ElMessage.error(result.msg);
            }
        } catch (error) {
            console.log('新增员工失败', error);
            ElMessage.error('网络错误，请检查网络');
        }
    } else {
        //修改员工
        try {
            const result = await updateEmpByIdApi(empId.value, employee.value);
            if (result.code === 1) {
                ElMessage.success(result.data);
                //关闭对话框
                dialogFormVisible.value = false;
                //重新查询最新数据
                queryMethod();
            } else {
                ElMessage.error(result.msg);
            }
        } catch (error) {
            console.log('网络错误', error);
            ElMessage.error('网络错误，请检查网络');
        }
    }
}

//记录勾选的员工的id
const selectedIds = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录(数组)
const handleSelectionChange = (selection) => {
    console.log(selection)
    selectedIds.value = selection.map(item => item.id)
}

//批量删除
const deleteByIds = () => {
    //弹出确认框
    ElMessageBox.confirm('您确认删除该员工吗？', '提示', {
        confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning'
    }).then(async () => { //确认
        if (selectedIds.value && selectedIds.value.length > 0) {
            try {
                const result = await deleteEmpByIdsApi(selectedIds.value);
                if (result.code === 1) {
                    ElMessage.success(result.data);
                    //重新查询
                    queryMethod();
                } else {
                    ElMessage.error(result.msg);
                }
            } catch (error) {
                ElMessage.error('网络错误，请检查网络后重试');
            }
        } else {
            ElMessage.info('您没有选择任何要删除的数据');
        }
    }
    ).catch(
        //用户点击取消，一般不做任何处理
    );
}

</script>

<template>
    <!-- 标题 -->
    <h3 style="margin-top: 0px;">员工管理</h3>
    <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 20px;">
        姓名:
        <el-input v-model="empQueryParam.name" size="large" style="width: 240px" placeholder="请输入姓名" clearable
            @clear="queryMethod" />

        所属部门:
        <el-select v-model="empQueryParam.deptId" size="large" placeholder="请选择部门" style="width: 240px"
            @change="queryMethod()">
            <el-option v-for="item in deptOptions" :key="item.id" :label="item.deptName" :value="item.id" />
        </el-select>

        入职日期:
        <el-date-picker v-model="empQueryParam.data" size="large" type="daterange" range-separator="至"
            start-placeholder="开始时间" end-placeholder="结束时间" style="width: 340px;" @change="queryMethod" />

        <el-button type="success" size="large" @click="queryMethod" style="margin-left: 15px;">查询</el-button>
        <el-button type="primary" size="large" @click="clearMethod">清空</el-button>
    </div>

    <!-- 新增员工和批量删除员工按钮 -->
    <div style="margin-bottom: 20px;">
        <el-button size="large" type="primary" @click="handleAdd()">新增员工+</el-button>
        <el-button size="large" type="danger" @click="deleteByIds()">批量删除-</el-button>
    </div>

    <!-- 新增员工对话框 -->
    <el-dialog v-model="dialogFormVisible" :title="title" width="800px">
        <el-form :model="employee" label-width="85px" size="large" :rules="rules" ref="empFormRef">
            <!-- 第一行 -->
            <el-row gutter="20">
                <el-col :span="12">
                    <el-form-item label="员工姓名:" prop="name">
                        <el-input v-model="employee.name" placeholder="请输入员工姓名" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="性别:" prop="gender">
                        <el-select v-model="employee.gender" placeholder="请选择性别" style="width: 100%;">
                            <el-option v-for="gender in genders" :key="gender.value" :label="gender.name"
                                :value="gender.value"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row gutter="20">
                <el-col :span="12">
                    <el-form-item label="手机号:" prop="phone">
                        <el-input v-model="employee.phone" placeholder="请输入手机号" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="身份证号:" prop="idCard">
                        <el-input v-model="employee.idCard" placeholder="请输入身份证号" />
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row gutter="20">
                <el-col :span="12">
                    <el-form-item label="入职日期:" prop="entryDate">
                        <el-date-picker v-model="employee.entryDate" type="date" placeholder="请选择入职日期"
                            value-format="YYYY-MM-DD" style="width: 100%" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="职位:" prop="position">
                        <el-input v-model="employee.position" placeholder="请输入用户职位" />
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row gutter="20">
                <el-col :span="12">
                    <el-form-item label="所属部门:" prop="deptId">
                        <el-select v-model="employee.deptId" placeholder="请选择所属部门" style="width: 100%;">
                            <el-option v-for="dept in deptOptions" :key="dept.id" :label="dept.deptName"
                                :value="dept.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="邮箱:" prop="email">
                        <el-input v-model="employee.email" placeholder="请输入邮箱地址" />
                    </el-form-item>
                </el-col>
            </el-row>
            <!-- 第五行 -->
            <el-row>
                <el-form-item label="头像" prop="avatar">
                    <el-upload class="avatar-uploader" action="/api/upload" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload"
                        :headers="{ 'token': token }">
                        <img v-if="employee.avatar" :src="employee.avatar" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
            </el-row>
            <!-- 第六行 -->
            <el-row gutter="20">
                <el-col :span="12">
                    <el-form-item label="用户名:" prop="username">
                        <el-input v-model="employee.username" placeholder="请输入登录用户名" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item label="密码:" prop="password">
                        <el-input v-model="employee.password" placeholder="请输入登录密码" />
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

    <!-- 表格 -->
    <el-table :data="empQueryResult" stripe style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center" />
        <el-table-column prop="name" label="姓名" width="180" align="center" />
        <el-table-column prop="avatar" label="头像" width="200" align="center">
            <template #default="scope">
                <img :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%;" alt="Avatar" />
            </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="200" align="center" />
        <el-table-column prop="position" label="职位" width="200" align="center" />
        <el-table-column prop="deptName" label="部门" width="200" align="center" />
        <el-table-column prop="entryDate" label="入职日期" width="200" align="center" />
        <el-table-column label="操作" width="200" align="center">
            <template #default="scope">
                <el-button size="default" type="primary" @click="editEmp(scope.row.id)">编辑</el-button>
                <el-button size="default" type="danger" @click="deleteEmp(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="demo-pagination-block" style="margin-top: 20px;">
        <el-pagination v-model:current-page="empQueryParam.pageNum" v-model:page-size="empQueryParam.pageSize"
            :page-sizes="[5, 10, 20, 30, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
            @size-change="queryMethod" @current-change="queryMethod" />
    </div>
</template>

<style scoped>
.avatar {
    height: 40px;
}

.avatar-uploader .avatar {
    width: 78px;
    height: 78px;
    display: block;
}

.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 78px;
    height: 78px;
    text-align: center;
    /* 添加灰色的虚线边框 */
    border: 1px dashed var(--el-border-color);
}
</style>