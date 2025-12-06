package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.CheckInTimeStatisticsService;
import com.zhy.model.dto.CheckinTrendDTO;
import com.zhy.model.vo.CheckinTrendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 入住时间趋势统计
 */

@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Api(tags = "入住时间趋势统计")
public class CheckInTimeStatisticsController {

    private final CheckInTimeStatisticsService checkInTimeStatisticsService;

    /**
     * 获取入住趋势数据（通用接口）
     * GET /api/statistics/checkin-trend
     * 参数: rangeType=day&startDate=2024-01-01&endDate=2024-01-31
     */
    @ApiOperation("获取入住趋势数据")
    @GetMapping("/checkin-trend")
    public Result getCheckinTrend(
            @RequestParam String rangeType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {
            CheckinTrendDTO dto = new CheckinTrendDTO();
            dto.setRangeType(rangeType);
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);

            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrend(dto);
            return Result.success(vo);

        } catch (IllegalArgumentException e) {
            log.warn("参数错误: {}", e.getMessage());
            return Result.error( e.getMessage());
        } catch (Exception e) {
            log.error("获取入住趋势数据失败", e);
            return Result.error( "获取数据失败");
        }
    }

    /**
     * 获取近1天数据（快捷接口）
     * GET /api/statistics/checkin-trend/day
     */
    @ApiOperation("获取近1天数据")
    @GetMapping("/checkin-trend/day")
    public Result getCheckinTrendByDay() {
        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByDay();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("获取近1天数据失败", e);
            return Result.error("获取数据失败");
        }
    }

    /**
     * 获取近1周数据（快捷接口）
     * GET /api/statistics/checkin-trend/week
     */
    @ApiOperation("获取近1周数据")
    @GetMapping("/checkin-trend/week")
    public Result getCheckinTrendByWeek() {
        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByWeek();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("获取近1周数据失败", e);
            return Result.error( "获取数据失败");
        }
    }

    /**
     * 获取近1月数据（快捷接口）
     * GET /api/statistics/checkin-trend/month
     */
    @ApiOperation("获取近1月数据")
    @GetMapping("/checkin-trend/month")
    public Result getCheckinTrendByMonth() {
        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByMonth();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("获取近1月数据失败", e);
            return Result.error( "获取数据失败");
        }
    }

    /**
     * 获取近3月数据（快捷接口）
     * GET /api/statistics/checkin-trend/quarter
     */
    @ApiOperation("获取近3月数据")
    @GetMapping("/checkin-trend/quarter")
    public Result getCheckinTrendByQuarter() {
        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByQuarter();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("获取近3月数据失败", e);
            return Result.error( "获取数据失败");
        }
    }

    /**
     * 获取近1年数据（快捷接口）
     * GET /api/statistics/checkin-trend/year
     */
    @ApiOperation("获取近1年数据")
    @GetMapping("/checkin-trend/year")
    public Result getCheckinTrendByYear() {
        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByYear();
            return Result.success(vo);
        } catch (Exception e) {
            log.error("获取近1年数据失败", e);
            return Result.error("获取数据失败");
        }
    }

    /**
     * 获取自定义范围数据（快捷接口）
     * GET /api/statistics/checkin-trend/custom?startDate=2024-01-01&endDate=2024-01-31
     */
    @ApiOperation("获取自定义范围数据")
    @GetMapping("/checkin-trend/custom")
    public Result getCheckinTrendByCustom(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {
            CheckinTrendVO vo = checkInTimeStatisticsService.getCheckinTrendByCustom(
                    startDate.toString(),
                    endDate.toString()
            );
            return Result.success(vo);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取自定义范围数据失败", e);
            return Result.error( "获取数据失败");
        }
    }
}