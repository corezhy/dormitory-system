import request from "@/utils/request"

//条件分页查询
export const query = (empQueryParam) => request.get('/emp', {params: empQueryParam });

//新增员工
export const addEmpAPi = (empData) => request.post('/emp', empData);

//查询回显
export const queryEmpByIdApi = (id) => request.get(`/emp/${id}`);

//修改员工
export const updateEmpByIdApi = (id, empData) => request.put(`/emp/${id}`, empData);

//批量删除员工
export const deleteEmpByIdsApi = (ids) => request.delete('/emp', {params: {ids}});