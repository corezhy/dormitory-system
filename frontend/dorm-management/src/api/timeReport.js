import request from "@/utils/request"

// 入住趋势统计（通用）
export const statisticsCheckinTrendApi = (params) => request.get('/api/statistics/checkin-trend', { params })

// 快捷接口
export const statisticsCheckinTrendByDayApi = () => request.get('/api/statistics/checkin-trend/day')

export const statisticsCheckinTrendByWeekApi = () => request.get('/api/statistics/checkin-trend/week')

export const statisticsCheckinTrendByMonthApi = () => request.get('/api/statistics/checkin-trend/month')

export const statisticsCheckinTrendByQuarterApi = () => request.get('/api/statistics/checkin-trend/quarter')

export const statisticsCheckinTrendByYearApi = () => request.get('/api/statistics/checkin-trend/year')

// 自定义日期范围
export const statisticsCheckinTrendByCustomApi = (startDate, endDate) =>
  request.get('/api/statistics/checkin-trend/custom', {
    params: { startDate, endDate }
  })