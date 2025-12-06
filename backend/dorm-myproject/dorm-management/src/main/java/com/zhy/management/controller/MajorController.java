package com.zhy.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.result.Result;
import com.zhy.management.service.MajorService;
import com.zhy.model.dto.AddAndEditMajorDto;
import com.zhy.model.dto.MajorDto;
import com.zhy.model.entity.Major;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 专业管理controller接口
 */
@RestController
@RequestMapping("/major")
@Api(tags = "专业管理")
public class MajorController {

    @Autowired
    private MajorService majorService;

    /**
     * 根据条件查询专业
     * @param majorDto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation("根据条件查询专业")
    public Result select(@RequestBody MajorDto majorDto) {
        Page<Major> result = majorService.select(majorDto);
        return Result.success(result);
    }

    /**
     * 新增专业
     * @param addAndEditMajorDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增专业")
    public Result insert(@Valid @RequestBody AddAndEditMajorDto addAndEditMajorDto) {
        majorService.insert(addAndEditMajorDto);
        return Result.success("新增专业成功");
    }

    /**
     * 根据code查询回显
     * @param code
     * @return
     */
    @ApiOperation("根据id查询回显")
    @GetMapping("/{code}")
    public Result selectById(@PathVariable String code) {
        Major major = majorService.selectById(code);
        return Result.success(major);
    }

    /**
     * 修改专业
     * @param addAndEditMajorDto
     * @return
     */
    @ApiOperation("根据Id修改专业")
    @PutMapping
    public Result updateById(@Valid @RequestBody AddAndEditMajorDto addAndEditMajorDto) {
        majorService.updateById( addAndEditMajorDto);
        return Result.success("修改专业成功");
    }

    /**
     * 启用停用专业
     * @param code
     * @param status
     * @return
     */
    @PutMapping("/{code}/status")
    @ApiOperation("启用停用专业")
    public Result startOrStop(@PathVariable String code,@RequestParam Integer status) {
        majorService.startOrStop(code, status);
        return Result.success("设置专业状态成功");
    }

    /**
     * 批量删除专业
     * @param codes
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除专业")
    public Result deleteBatch(@RequestParam List<String> codes) {
        majorService.deleteBatch(codes);
        return Result.success("删除专业成功");
    }

    /**
     * 查询全部启用专业
     * @return
     */
    @GetMapping
    @ApiOperation("查询全部启用专业")
    public Result selectEnabled() {
        List<Major> majors = majorService.selectEnabled();
        return Result.success(majors);
    }
}
