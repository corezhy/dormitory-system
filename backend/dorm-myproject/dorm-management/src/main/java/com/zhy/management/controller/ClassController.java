package com.zhy.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.result.Result;
import com.zhy.management.service.ClassService;
import com.zhy.model.dto.AddAndEditClassDto;
import com.zhy.model.dto.ClassDto;
import com.zhy.model.entity.Class;
import com.zhy.model.vo.ClassVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 班级管理controller
 */
@RestController
@RequestMapping("/class")
@Api(tags = "班级管理")
public class ClassController {

    @Autowired
    private ClassService classService;

    /**
     * 条件查询班级
     * @param classDto
     * @return
     */
    @GetMapping
    @ApiOperation("查询班级")
    public Result conditionQueryClass(ClassDto classDto) {
        Page<ClassVo> result = classService.conditionQueryClass(classDto);
        return Result.success(result);
    }

    /**
     * 新增班级
     * @return
     */
    @PostMapping
    @ApiOperation("新增班级")
    public Result insertClass(@Valid @RequestBody AddAndEditClassDto dto) {
        classService.insertClass(dto);
        return Result.success("新增班级成功");
    }

    /**
     * 根据id查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询回显")
    public Result getById(@PathVariable Long id) {
        Class aClass = classService.getById(id);
        return Result.success(aClass);
    }

    /**
     * 根据id修改班级
     * @param id
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("修改班级")
    public Result editClass(@Valid @PathVariable Long id, @RequestBody AddAndEditClassDto dto) {
        classService.editClass(id, dto);
        return Result.success("修改班级成功");
    }

    /**
     * 批量删除班级
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除班级")
    public Result deleteBatch(@RequestParam List<Integer> ids) {
        classService.deleteBatch(ids);
        return Result.success("删除班级成功");
    }

}
