import request from "@/utils/request"

//条件分页查询宿舍
export const conditionQueryDormRoomApi = (queryDormParams) => request.get('/dorm', {params: queryDormParams});

//新增宿舍
export const insertDormApi = (addDormData) => request.post('/dorm', addDormData);

//查询回显宿舍
export const getDormRoomByIdApi = (id) => request.get(`/dorm/${id}`); 

//修改宿舍
export const updateByIdApi = (id, editDormData) => request.put(`/dorm/${id}`, editDormData);

//启用停用宿舍
export const startOrStopApi = (id, status) => request.put(`/dorm/${id}/status?status=${status}`);

//批量删除宿舍
export const deleteBatchApi = (ids) => request.delete('/dorm', {params: {ids}});