import request from "@/utils/request"

//统计各楼栋宿舍数量和床位总数
export const statisticsDormAndBedCountApi = () => request.get('/dormReport/dormAndBedCount');

//停用/维修宿舍分布
export const statisticStopDormApi = () => request.get('/dormReport/stopDorm');