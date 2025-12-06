import request from "@/utils/request"

//查询班级
export const conditionQueryClassAPi = (queryData) => request.get('/class', {params: queryData});

//新增班级
export const insertClassApi = (addClass) => request.post('/class', addClass);

//根据id查询回显
export const getByIdApi = (id) => request.get(`/class/${id}`);

//修改班级
export const editClassApi = (id, updateClass) => request.put(`/class/${id}`, updateClass);

//批量删除班级
export const deleteClassApi = (ids) => request.delete('/class', {params: {ids}});
