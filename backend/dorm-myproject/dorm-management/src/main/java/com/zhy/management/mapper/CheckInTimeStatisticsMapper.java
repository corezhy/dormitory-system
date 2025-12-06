package com.zhy.management.mapper;

import com.zhy.model.dto.CheckinTrendDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 入住时间趋势统计Mapper
 */
@Mapper
public interface CheckInTimeStatisticsMapper {

    /**
     * 查询入住趋势数据
     */
    List<Map<String, Object>> selectCheckinTrend(@Param("dto") CheckinTrendDTO dto);

    /**
     * 查询按天统计（近1天）
     */
    List<Map<String, Object>> selectCheckinTrendByDay();

    /**
     * 查询按周统计（近7天）
     */
    List<Map<String, Object>> selectCheckinTrendByWeek();

    /**
     * 查询按月统计（近1月）
     */
    List<Map<String, Object>> selectCheckinTrendByMonth();

    /**
     * 查询按季度统计（近3月）
     */
    List<Map<String, Object>> selectCheckinTrendByQuarter();

    /**
     * 查询按年统计（近1年）
     */
    List<Map<String, Object>> selectCheckinTrendByYear();

    /**
     * 查询自定义时间范围
     */
    List<Map<String, Object>> selectCheckinTrendByCustom(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

}