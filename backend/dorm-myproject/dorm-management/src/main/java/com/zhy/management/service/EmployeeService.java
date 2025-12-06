package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditEmpDto;
import com.zhy.model.dto.EmployeeDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.EmployeeVo;

import java.util.List;

/**
 * 员工管理业务层接口
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 员工查询
     * @param employeeDto
     * @return
     */
    public Page<EmployeeVo> select(EmployeeDto employeeDto);

    /**
     * 新增员工接口
     * @param addAndEditEmpDto
     * @return
     */
    public void insert(AddAndEditEmpDto addAndEditEmpDto);

    /**
     * 员工查询回显
     * @param id
     * @return
     */
    public Employee selectById(Long id);

    /**
     * 修改员工
     * @param id
     * @param addAndEditEmpDto
     * @return
     */
    public void update(Long id, AddAndEditEmpDto addAndEditEmpDto);

    /**
     * 批量删除员工
     * @param ids
     * @return
     */
    public void deleteBatch(List<Integer> ids);

}
