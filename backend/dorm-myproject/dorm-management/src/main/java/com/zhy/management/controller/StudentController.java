package com.zhy.management.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.groups.ValidationGroups;
import com.zhy.common.result.Result;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditStuDto;
import com.zhy.model.dto.StudentDto;
import com.zhy.model.vo.StudentEditVo;
import com.zhy.model.vo.StudentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 学生管理controller
 */
@RestController
@RequestMapping("/stu")
@Api(tags = "学生管理")
@Slf4j
public class StudentController {


    @Autowired
    private StudentService studentService;


    /**
     * 条件分页查询学生
     * @param dto
     * @return
     */
    @GetMapping
    @ApiOperation("条件分页查询学生")
    public Result conditionPageQueryStu(StudentDto dto) {
        Page<StudentVo> result = studentService.conditionPageQueryStu(dto);
        return Result.success(result);
    }

    /**
     * 新增学生
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("新增学生")
    public Result insertStu(@Validated(ValidationGroups.Insert.class) @RequestBody AddAndEditStuDto dto) {
        studentService.insertStu(dto);
        return Result.success("新增学生成功");
    }

    /**
     * 根据id查询回显学生
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询回显学生")
    public Result getStuById(@PathVariable Long id) {
        StudentEditVo result = studentService.getStuById(id);
        return Result.success(result);
    }

    /**
     * 修改学生
     * @param id
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("修改学生")
    public Result updateStu(@Validated(ValidationGroups.Update.class) @PathVariable Long id, @RequestBody AddAndEditStuDto dto) {
        studentService.updateStu(id, dto);
        return Result.success("修改学生成功");
    }

    /**
     * 违纪扣分
     * @return
     */
    @PostMapping("/{id}/deduct-score")
    @ApiOperation("违纪扣分")
    public Result discipline(@PathVariable Long id, @RequestParam Integer score) {
        studentService.discipline(id, score);
        return Result.success("违纪扣分成功");
    }


    /**
     * 批量删除学生
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除学生")
    public Result deleteBatch(@RequestParam List<Integer> ids) {
        //去重，防止前端误传重复ID
        ArrayList<Integer> uniqueIds = new ArrayList<>(new LinkedHashSet<>(ids));
        log.info("批量删除学生,ids: {}", uniqueIds);
        studentService.deleteBatch(uniqueIds);
        return Result.success("成功删除 " + uniqueIds.size() + " 个学生");
    }




}
