import request from "@/utils/request"

//员工登录
export const empLogin = (loginParam) => request.post('/login', loginParam)