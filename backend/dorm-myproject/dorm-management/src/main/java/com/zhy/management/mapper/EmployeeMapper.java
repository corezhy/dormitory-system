package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.model.dto.EmployeeDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.EmployeeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 员工管理mapper层
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 员工查询返回数据
     * @param page
     * @param employeeDto
     * @return
     */
    public Page<EmployeeVo> selectEmpAndDeptName(Page<EmployeeVo> page, EmployeeDto employeeDto);

    /**
     * 查询当前年份下，最大的员工编号
     * @param year
     * @return
     */
    @Select("SELECT MAX(emp_no) FROM employee WHERE emp_no LIKE CONCAT('EMP', #{year}, '%')")
    String findMaxEmpNoByPrefix(@Param("year") int year);


}
