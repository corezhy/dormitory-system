import request from "@/utils/request"

//查询部门
export const queryAll = () => request.get('/dept');

// 新增部门
export const addDeptApi = (deptData) => request.post('/dept', deptData);

//修改部门
export const updateDeptApi = (id, deptData) => request.put(`/dept/${id}`, deptData);

//查询回显
export const queryByIdApi = (id) => request.get(`/dept/${id}`);

//删除部门
export const deleteDeptApi = (id) => request.delete(`/dept/${id}`);