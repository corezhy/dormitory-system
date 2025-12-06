package com.zhy.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.common.result.Result;
import com.zhy.management.service.EmployeeService;
import com.zhy.model.dto.AddAndEditEmpDto;
import com.zhy.model.dto.EmployeeDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.EmployeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@Api(tags = "员工管理接口")
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工信息查询
     * @param employeeDto 查询参数
     * @return 响应数据
     */
    @GetMapping
    @ApiOperation("员工查询")
    public Result select(EmployeeDto employeeDto) {
        Page<EmployeeVo> result = employeeService.select(employeeDto); //result永远不会是null
        return Result.success(result);
    }

    /**
     * 新增员工
     * @param addAndEditEmpDto
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result insert(@Valid @RequestBody AddAndEditEmpDto addAndEditEmpDto) {
        employeeService.insert(addAndEditEmpDto);
        return Result.success("新增员工成功");
    }

    /**
     * 员工查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result selectById(@PathVariable Long id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工
     * @param addAndEditEmpDto
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("修改员工")
    public Result update(@PathVariable Long id, @RequestBody AddAndEditEmpDto addAndEditEmpDto) {
        employeeService.update(id, addAndEditEmpDto);
        return Result.success("修改员工成功");
    }

    /**
     * 批量删除员工
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除员工")
    public Result deleteBatch(@RequestParam List<Integer> ids) {
        log.info("批量删除员工: ids={}", ids);
        employeeService.deleteBatch(ids);
        return Result.success("删除员工成功");
    }

}
