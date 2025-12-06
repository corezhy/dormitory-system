package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.StuReportService;
import com.zhy.model.vo.StuReportBuildingVo;
import com.zhy.model.vo.StuReportGenderVo;
import com.zhy.model.vo.StuReportMajorVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生报表统计controller
 */

@Api(tags = "学生报表统计")
@RestController
@RequestMapping("/stuReport")
public class StuReportController {

    @Autowired
    private StuReportService stuReportService;


    /**
     * 学生性别统计
     * @return
     */
    @GetMapping("/genderCount")
    @ApiOperation("学生性别统计")
    public Result statisticsStuGenderCount() {
        StuReportGenderVo result = stuReportService.statisticsStuGenderCount();
        return Result.success(result);
    }

    /**
     * 学生专业统计
     * @return
     */
    @GetMapping("/majorCount")
    @ApiOperation("学生专业统计")
    public Result statisticsStuMajorCount() {
        List<StuReportMajorVo> voList = stuReportService.statisticsStuMajorCount();
        return Result.success(voList);
    }

    /**
     * 按照楼栋统计学生
     * @return
     */
    @ApiOperation("学生楼栋统计")
    @GetMapping("/buildingCount")
    public Result statisticsStuBuildingCount() {
        List<StuReportBuildingVo> voList = stuReportService.statisticsStuBuildingCount();
        return Result.success(voList);
    }

    /**
     * 统计全校床铺占用率
     * @return
     */
    @GetMapping("/bedsCount")
    @ApiOperation("统计全校床铺占用率")
    public Result statisticsBedsOccupancyRate() {
         Double occupancyRate = stuReportService.statisticsBedsOccupancyRate();
         return Result.success(occupancyRate);
    }


}
