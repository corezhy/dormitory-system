package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.DepartmentService;
import com.zhy.model.dto.DeptDto;
import com.zhy.model.entity.Department;
import com.zhy.model.vo.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理controller接口
 */
@RestController
@Api(tags = "部门管理")
@RequestMapping("/dept")//统一前缀
public class DepartmentController {


    @Autowired
    private DepartmentService departmentService;


    /**
     * 查询所有部门
     * @return
     */
    @GetMapping
    @ApiOperation("查询部门")
    public Result selectDept() {
        List<DepartmentVo> departmentVos = departmentService.selectDept();
        return Result.success(departmentVos);
    }

    /**
     * 新增部门接口
     * @param deptDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增部门")
    public Result insertDept(@RequestBody DeptDto deptDto) {
        departmentService.insertDept(deptDto);
        return Result.success("新增部门成功");
    }

    /**
     * 修改部门接口
     * @param deptDto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("修改部门")
    public Result updateDept(@PathVariable Long id, @RequestBody DeptDto deptDto) {
        departmentService.updateDept(id, deptDto);
        return Result.success("修改部门成功");
    }

    /**
     * 查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据Id查询回显")
    public Result selectDeptById(@PathVariable Long id) {
        Department department = departmentService.selectDeptById(id);
        return Result.success(department);
    }

    /**
     * 根据id删除部门
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除部门")
    public Result deleteDeptById(@PathVariable Long id) {
        departmentService.deleteDeptById(id);
        return Result.success("删除部门成功");
    }

}
