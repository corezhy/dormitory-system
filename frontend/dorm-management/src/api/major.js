import request from "@/utils/request"

//根据条件查询专业
export const queryAllApi = (queryParam) => request.post('/major/list', queryParam);

//新增专业
export const insertMajorApi = (majorSaveData) => request.post('/major', majorSaveData);

//根据id查询回显
export const queryMajorById = (code) => request.get(`/major/${code}`);

//修改专业
export const updateMajorApi = (majorEditData) => request.put('/major', majorEditData);

//启用停用专业
export const startOrStopAPi = (code, status) => request.put(`/major/${code}/status?status=${status}`)

//批量删除专业
export const deleteBatchApi = (codes) => request.delete('/major', {params: {codes}});

//查询全部启用专业
export const selectAll = () => request.get('/major');