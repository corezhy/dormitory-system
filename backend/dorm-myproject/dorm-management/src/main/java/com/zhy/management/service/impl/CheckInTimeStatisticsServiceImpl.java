package com.zhy.management.service.impl;

import com.zhy.management.mapper.CheckInTimeStatisticsMapper;
import com.zhy.management.service.CheckInTimeStatisticsService;
import com.zhy.model.dto.CheckinTrendDTO;
import com.zhy.model.vo.CheckinTrendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 入住时间趋势统计service实现类
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckInTimeStatisticsServiceImpl implements CheckInTimeStatisticsService {

    private final CheckInTimeStatisticsMapper checkInTimeStatisticsMapper;

    @Override
    public CheckinTrendVO getCheckinTrend(CheckinTrendDTO dto) {
        try {
            // 参数验证
            validateCheckinTrendDTO(dto);

            log.info("查询入住趋势数据: rangeType={}, startDate={}, endDate={}",
                    dto.getRangeType(), dto.getStartDate(), dto.getEndDate());

            // 使用动态SQL查询
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrend(dto);

            // 转换为VO
            return convertToCheckinTrendVO(resultList);

        } catch (Exception e) {
            log.error("查询入住趋势数据失败", e);
            throw new RuntimeException("查询入住趋势数据失败: " + e.getMessage());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByDay() {
        try {
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByDay();

            // 确保24小时都有数据（补全0值）
            resultList = completeHourData(resultList);

            return convertToCheckinTrendVO(resultList);
        } catch (Exception e) {
            log.error("查询近1天入住趋势失败", e);
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByWeek() {
        try {
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByWeek();

            // 确保7天都有数据
            resultList = completeDayData(resultList);

            return convertToCheckinTrendVO(resultList);
        } catch (Exception e) {
            log.error("查询近1周入住趋势失败", e);
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByMonth() {
        try {
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByMonth();
            return convertToCheckinTrendVO(resultList);
        } catch (Exception e) {
            log.error("查询近1月入住趋势失败", e);
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByQuarter() {
        try {
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByQuarter();
            return convertToCheckinTrendVO(resultList);
        } catch (Exception e) {
            log.error("查询近3月入住趋势失败", e);
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByYear() {
        try {
            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByYear();
            return convertToCheckinTrendVO(resultList);
        } catch (Exception e) {
            log.error("查询近1年入住趋势失败", e);
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }
    }

    @Override
    public CheckinTrendVO getCheckinTrendByCustom(String startDate, String endDate) {
        try {
            // 验证日期格式
            if (!StringUtils.hasText(startDate) || !StringUtils.hasText(endDate)) {
                throw new IllegalArgumentException("开始日期和结束日期不能为空");
            }

            // 验证日期顺序
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start.after(end)) {
                throw new IllegalArgumentException("开始日期不能晚于结束日期");
            }

            // 限制查询范围（不超过1年）
            long diffDays = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
            if (diffDays > 365) {
                throw new IllegalArgumentException("查询时间范围不能超过1年");
            }

            List<Map<String, Object>> resultList = checkInTimeStatisticsMapper.selectCheckinTrendByCustom(startDate, endDate);
            return convertToCheckinTrendVO(resultList);

        } catch (Exception e) {
            log.error("查询自定义时间范围入住趋势失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    /**
     * 验证参数
     */
    private void validateCheckinTrendDTO(CheckinTrendDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getRangeType())) {
            throw new IllegalArgumentException("rangeType不能为空");
        }

        // 验证rangeType值
        List<String> validRangeTypes = Arrays.asList("day", "week", "month", "quarter", "year", "custom");
        if (!validRangeTypes.contains(dto.getRangeType())) {
            throw new IllegalArgumentException("rangeType参数无效");
        }

        // 如果是custom类型，需要验证日期参数
        if ("custom".equals(dto.getRangeType())) {
            if (dto.getStartDate() == null || dto.getEndDate() == null) {
                throw new IllegalArgumentException("自定义范围需要提供开始日期和结束日期");
            }
        }
    }

    /**
     * 将查询结果转换为VO
     */
    private CheckinTrendVO convertToCheckinTrendVO(List<Map<String, Object>> resultList) {
        if (resultList == null || resultList.isEmpty()) {
            return new CheckinTrendVO(new ArrayList<>(), new ArrayList<>());
        }

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> map : resultList) {
            String label = (String) map.get("time_label");
            Object countObj = map.get("count");

            if (label != null && countObj != null) {
                labels.add(label);
                values.add(((Number) countObj).intValue());
            }
        }

        return new CheckinTrendVO(labels, values);
    }

    /**
     * 补全24小时数据
     */
    private List<Map<String, Object>> completeHourData(List<Map<String, Object>> resultList) {
        Map<String, Integer> hourMap = resultList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("time_label"),
                        map -> ((Number) map.get("count")).intValue()
                ));

        List<Map<String, Object>> completedList = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            String hourStr = String.format("%02d:00", hour);
            int count = hourMap.getOrDefault(hourStr, 0);

            Map<String, Object> map = new HashMap<>();
            map.put("time_label", hourStr);
            map.put("count", count);
            completedList.add(map);
        }

        return completedList;
    }

    /**
     * 补全7天数据
     */
    private List<Map<String, Object>> completeDayData(List<Map<String, Object>> resultList) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar calendar = Calendar.getInstance();

        // 获取最近7天的日期
        Map<String, Integer> dayMap = resultList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("time_label"),
                        map -> ((Number) map.get("count")).intValue()
                ));

        List<Map<String, Object>> completedList = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            String dayStr = sdf.format(calendar.getTime());

            int count = dayMap.getOrDefault(dayStr, 0);

            Map<String, Object> map = new HashMap<>();
            map.put("time_label", dayStr);
            map.put("count", count);
            completedList.add(map);
        }

        return completedList;
    }
}