import request from "@/utils/request"

//根据条件查询楼层
export const conditionQueryFloorApi = (searchForm) => request.post('/floor/list', searchForm);

//新增楼层
export const insertFloorApi = (addFloorData) => request.post('/floor', addFloorData);

//根据id查询回显楼层
export const selectByIdApi = (id) => request.get(`/floor/${id}`);

//根据id修改楼层
export const updateByIdApi = (id, editFloorData) => request.put(`/floor/${id}`, editFloorData);

//根据id删除楼层
export const deleteByIdApi = (id) => request.delete(`/floor/${id}`);

//根据buildingId查询楼层数
export const getFloorNumberByBuildingIdApi = (buildingId) => request.get('/floor', {params: {buildingId}});