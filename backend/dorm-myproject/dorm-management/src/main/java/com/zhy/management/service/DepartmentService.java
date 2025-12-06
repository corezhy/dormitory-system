package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.DeptDto;
import com.zhy.model.entity.Department;
import com.zhy.model.vo.DepartmentVo;

import java.util.List;

/**
 * 部门管理service接口
 */
public interface DepartmentService extends IService<Department> {


    /**
     * 查询全部部门
     * @return
     */
    public List<DepartmentVo> selectDept();


    /**
     * 新增部门
     * @param deptDto
     * @return
     */
    public void insertDept(DeptDto deptDto);


    /**
     * 修改部门接口
     * @param id 主键
     * @param deptDto
     * @return
     */
    public void updateDept(Long id, DeptDto deptDto);


    /**
     * 查询回显
     * @param id
     * @return
     */
    public Department selectDeptById(Long id);

    /**
     * 根据id删除部门
     * @param id
     * @return
     */
    public void deleteDeptById(Long id);

}
