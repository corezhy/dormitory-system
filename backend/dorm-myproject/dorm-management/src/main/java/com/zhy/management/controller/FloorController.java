package com.zhy.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.groups.ValidationGroups;
import com.zhy.common.result.Result;
import com.zhy.management.service.FloorService;
import com.zhy.model.dto.AddAndEditFloorDto;
import com.zhy.model.dto.FloorDto;
import com.zhy.model.entity.Floor;
import com.zhy.model.vo.FloorVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 楼层管理controller
 */
@RequestMapping("/floor")
@RestController
@Api(tags = "楼层管理")
public class FloorController {

    @Autowired
    private FloorService floorService;

    /**
     * 根据条件查询楼层
     * @param floorDto
     * @return
     */
    @PostMapping("/list")
    @ApiOperation("根据条件查询楼层")
    public Result conditionQueryFloor(@RequestBody FloorDto floorDto) {
        Page<FloorVo> result = floorService.conditionQueryFloor(floorDto);
        return Result.success(result);
    }

    /**
     * 新增楼层
     * @return
     */
    @PostMapping
    @ApiOperation("新增楼层")
    public Result insert(@Validated(ValidationGroups.Insert.class) @RequestBody AddAndEditFloorDto addAndEditFloorDto) {
        floorService.insert(addAndEditFloorDto);
        return Result.success("新增楼层成功");
    }

    /**
     * 根据Id查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询回显")
    public Result selectById(@PathVariable Long id) {
        Floor floor = floorService.selectById(id);
        return Result.success(floor);
    }

    /**
     * 根据Id修改楼层
     * @param id
     * @param addAndEditFloorDto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("根据id修改楼层")
    public Result updateById(@Validated(ValidationGroups.Update.class) @PathVariable Long id, @RequestBody AddAndEditFloorDto addAndEditFloorDto) {
        floorService.updateById(id, addAndEditFloorDto);
        return Result.success("修改楼层成功");
    }

    /**
     * 根据id删除楼层
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除楼层")
    public Result deleteById(@PathVariable Long id) {
        floorService.deleteById(id);
        return Result.success("删除楼层成功");
    }

    /**
     * 根据楼栋Id获取该栋楼层数
     * @param buildingId
     * @return
     */
    @ApiOperation("根据楼栋Id获取该栋楼层数")
    @GetMapping
    public Result getFloorNumberByBuildingId(@RequestParam("buildingId") Long buildingId) {
        List<Floor> floorList = floorService.getFloorNumberByBuildingId(buildingId);
        return Result.success(floorList);
    }

}
