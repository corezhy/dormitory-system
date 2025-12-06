import request from "@/utils/request"

//查询楼栋
export const queryAllApi = () => request.get('/building/list'); 

//新增楼栋
export const insertBuildingApi = (buildingData) => request.post('/building', buildingData);

//根据id修改楼栋
export const updateBuildingApi = (id, buildingData) => request.put(`/building/${id}`, buildingData);

//根据id查询回显楼栋
export const selectById = (id) => request.get(`/building/${id}`);

//启用停用楼栋
export const startAndStop = (id, status) => request.put(`/building/${id}/status?status=${status}`)

//删除楼栋
export const deleteByIdApi = (id) => request.delete(`/building/${id}`)

//查询所有启用楼栋
export const queryIsEnabledApi = () => request.get('/building');