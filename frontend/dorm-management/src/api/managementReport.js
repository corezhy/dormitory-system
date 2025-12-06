import request from "@/utils/request"

//统计专业违纪学生
export const violationStuCountApi = () => request.get('/managementReport/violation');

//统计各部门员工数量
export const deptEmpCountApi = () => request.get('/managementReport/empCount');