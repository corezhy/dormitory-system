package com.zhy.management.service;

import com.zhy.model.dto.CheckinTrendDTO;
import com.zhy.model.vo.CheckinTrendVO;

/**
 * 入住时间趋势统计service
 */

public interface CheckInTimeStatisticsService {

    /**
     * 获取入住趋势数据
     */
    CheckinTrendVO getCheckinTrend(CheckinTrendDTO dto);

    /**
     * 获取近1天数据（按小时）
     */
    CheckinTrendVO getCheckinTrendByDay();

    /**
     * 获取近1周数据（按天）
     */
    CheckinTrendVO getCheckinTrendByWeek();

    /**
     * 获取近1月数据（按周）
     */
    CheckinTrendVO getCheckinTrendByMonth();

    /**
     * 获取近3月数据（按月）
     */
    CheckinTrendVO getCheckinTrendByQuarter();

    /**
     * 获取近1年数据（按月）
     */
    CheckinTrendVO getCheckinTrendByYear();

    /**
     * 获取自定义日期范围数据
     */
    CheckinTrendVO getCheckinTrendByCustom(String startDate, String endDate);
}