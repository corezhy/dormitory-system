package com.zhy.management.controller;

import com.zhy.common.groups.ValidationGroups;
import com.zhy.common.result.Result;
import com.zhy.management.service.BuildingService;
import com.zhy.model.dto.AddAndEditBuildingDto;
import com.zhy.model.entity.Building;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 楼栋管理controller接口
 */
@Api(tags = "楼栋管理")
@RestController
@RequestMapping("/building")
public class BuildingController {


    @Autowired
    private BuildingService buildingService;

    /**
     * 查询楼栋
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询楼栋")
    public Result select() {
        List<Building> buildingList = buildingService.select();
        return Result.success(buildingList);
    }

    /**
     * 新增楼栋
     *
     * @param addAndEditBuildingDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增楼栋")
    public Result insert(
            @Validated(ValidationGroups.Insert.class) //指定使用Insert分组校验
            @RequestBody AddAndEditBuildingDto addAndEditBuildingDto) {
        buildingService.insert(addAndEditBuildingDto);
        return Result.success("新增楼栋成功");
    }

    /**
     * 根据id查询回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询回显")
    public Result selectById(@PathVariable Long id) {
        Building building = buildingService.selectById(id);
        return Result.success(building);
    }

    /**
     * 根据id修改楼栋
     *
     * @return
     */
    @ApiOperation("根据id修改楼栋")
    @PutMapping("/{id}")
    public Result updateById(@PathVariable Long id, @RequestBody AddAndEditBuildingDto addAndEditBuildingDto) {
        buildingService.updateById(id, addAndEditBuildingDto);
        return Result.success("修改楼栋成功");
    }

    /**
     * 启用停用设置
     *
     * @param id
     * @param status
     * @return
     */
    @PutMapping("/{id}/status")
    @ApiOperation("启用停用设置")
    public Result updateStatusById(@PathVariable Long id, @RequestParam Integer status) {
        buildingService.updateStatusById(id, status);
        return Result.success("设置状态成功");
    }

    /**
     * 删除楼栋
     *
     * @param id
     * @return
     */
    @ApiOperation("删除楼栋")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        buildingService.deleteById(id);
        return Result.success("删除楼栋成功");
    }

    /**
     * 查询所有启用楼栋
     *
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有启用楼栋")
    public Result selectStatusIsEnabled() {
        List<Building> buildings = buildingService.selectStatusIsEnabled();
        return Result.success(buildings);
    }

}
