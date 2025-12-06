import request from "@/utils/request"

//学生性别统计
export const statisticsStuGenderCountApi = () => request.get('/stuReport/genderCount');

//学生专业统计
export const statisticsMajorStuCountApi = () => request.get('/stuReport/majorCount');

//按照楼栋统计学生
export const statisticsStuBuildingCountApi = () => request.get('/stuReport/buildingCount');

//统计全校床铺占用率
export const statisticsBedsOccupancyRateApi = () => request.get('/stuReport/bedsCount');