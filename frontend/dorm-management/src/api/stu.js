import request from "@/utils/request"

//条件分页查询宿舍
export const conditionPageQueryStuApi = (queryParams) => request.get('/stu', {params: queryParams});

//新增学生
export const addStuApi = (addStuParams) => request.post('/stu', addStuParams);

//根据Id查询回显学生
export const getStuByIdApi = (id) => request.get(`/stu/${id}`);

//修改学生
export const editStuApi = (id, editStuParams) => request.put(`/stu/${id}`, editStuParams);

//违纪扣分
export const disciplineApi = (id, score) => request.post(`/stu/${id}/deduct-score?score=${score}`);

//批量删除学生
export const deleteStuByIdApi = (ids) => request.delete('/stu', {params: {ids}});