package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.ManagementService;
import com.zhy.model.vo.DeptEmpCountVo;
import com.zhy.model.vo.ViolationStuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理异常类统计controller
 */
@Api(tags = "管理异常统计")
@RestController
@RequestMapping("/managementReport")
public class ManagementController {


    @Autowired
    private ManagementService managementService;

    /**
     * 违纪专业学生统计
     * @return
     */
    @GetMapping("/violation")
    @ApiOperation("专业违纪学生统计")
    public Result violationStuCount() {
        List<ViolationStuVo> voList = managementService.violationStuCount();
        return Result.success(voList);
    }

    /**
     * 统计各部门员工数量
     * @return
     */
    @GetMapping("/empCount")
    @ApiOperation("统计部门员工数量")
    public Result deptEmpCount() {
        List<DeptEmpCountVo> voList = managementService.deptEmpCount();
        return Result.success(voList);
    }


}
