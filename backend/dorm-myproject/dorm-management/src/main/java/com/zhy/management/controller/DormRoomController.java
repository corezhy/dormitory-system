package com.zhy.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.groups.ValidationGroups;
import com.zhy.common.result.Result;
import com.zhy.management.service.DormRoomService;
import com.zhy.model.dto.AddAndEditDormRoomDto;
import com.zhy.model.dto.DormRoomDto;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.vo.DormRoomVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 宿舍管理controller类
 */

@Api(tags = "宿舍管理")
@RestController
@RequestMapping("/dorm")
public class DormRoomController {


    @Autowired
    private DormRoomService dormRoomService;

    /**
     * 条件分页查询宿舍
     *
     * @param dormRoomDto
     * @return
     */
    @GetMapping
    @ApiOperation("条件分页查询宿舍")
    public Result conditionQueryDormRoom(DormRoomDto dormRoomDto) {
        Page<DormRoomVo> dormRoomVoPage = dormRoomService.conditionQueryDormRoom(dormRoomDto);
        return Result.success(dormRoomVoPage);
    }


    /**
     * 新增宿舍接口
     *
     * @param addAndEditDormRoomDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增宿舍接口")
    public Result insertDormRoom(@Validated(ValidationGroups.Insert.class) @RequestBody AddAndEditDormRoomDto addAndEditDormRoomDto) {
        dormRoomService.insertDormRoom(addAndEditDormRoomDto);
        return Result.success("新增宿舍成功");
    }

    /**
     * 根据id查询宿舍
     *
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据Id查询宿舍")
    public Result getDormRoomById(@PathVariable Long id) {
        DormRoom dormRoom = dormRoomService.getDormRoomById(id);
        return Result.success(dormRoom);
    }


    /**
     * 根据id修改宿舍
     *
     * @param id
     * @param dto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("根据id修改宿舍")
    public Result updateById(@PathVariable Long id, @RequestBody AddAndEditDormRoomDto dto) {
        dormRoomService.updateById(id, dto);
        return Result.success("修改宿舍成功");
    }

    /**
     * 启用停用宿舍
     *
     * @return
     */
    @PutMapping("/{id}/status")
    @ApiOperation("启用停用宿舍")
    public Result startOrStop(@PathVariable Long id, @RequestParam Byte status) {
        dormRoomService.startOrStop(id, status);
        return Result.success("修改状态成功");
    }

    /**
     * 批量删除宿舍
     *
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除宿舍")
    public Result deleteBatch(@RequestParam List<Integer> ids) {
        dormRoomService.deleteBatch(ids);
        return Result.success("删除宿舍成功");
    }

}
