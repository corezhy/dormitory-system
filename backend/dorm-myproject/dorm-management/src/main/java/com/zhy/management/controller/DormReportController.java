package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.DormReportService;
import com.zhy.model.vo.DormReportCountVo;
import com.zhy.model.vo.DormStopReportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 宿舍资源统计
 */
@Api(tags = "宿舍资源统计")
@RestController
@RequestMapping("/dormReport")
public class DormReportController {


    @Autowired
    private DormReportService dormReportService;

    /**
     * 统计各楼栋宿舍数量和床位总数
     * @return
     */
    @ApiOperation("统计各楼栋宿舍数量和床位总数")
    @GetMapping("/dormAndBedCount")
    public Result statisticsDormAndBedCount() {
        List<DormReportCountVo> voList = dormReportService.statisticsDormAndBedCount();
        return Result.success(voList);
    }

    /**
     * 停用/维修宿舍分布
     * @return
     */
    @ApiOperation("停用/维修宿舍分布")
    @GetMapping("stopDorm")
    public Result statisticStopDorm() {
        List<DormStopReportVo> voList = dormReportService.statisticStopDorm();
        return Result.success(voList);
    }



}
