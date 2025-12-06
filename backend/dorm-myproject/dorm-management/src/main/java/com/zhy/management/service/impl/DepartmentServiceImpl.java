package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.DepartmentMapper;
import com.zhy.management.service.DepartmentService;
import com.zhy.model.dto.DeptDto;
import com.zhy.model.entity.Department;
import com.zhy.model.vo.DepartmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理service接口实现类
 */
@Service
@Slf4j
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {


    /**
     * 查询全部部门
     *
     * @return
     */
    @Override
    public List<DepartmentVo> selectDept() {
        List<Department> deptList = list();
        if (deptList == null) {
            throw new BusinessException("查询部门信息失败");
        }
        return deptList.stream().map(department -> {
            DepartmentVo departmentVo = new DepartmentVo();
            BeanUtils.copyProperties(department, departmentVo);
            return departmentVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增部门
     *
     * @param deptDto
     * @return
     */
    @Override
    public void insertDept(DeptDto deptDto) {
        if (StringUtils.isBlank(deptDto.getDeptCode()) || StringUtils.isBlank(deptDto.getDeptName())) {
            throw new BusinessException("部门编码或部门名称不能为空");
        }
        Department department = new Department();
        BeanUtils.copyProperties(deptDto, department);
        //创建时间和修改时间设置为当前时间
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());
        //新增部门
        boolean save = save(department);
        if (!save) {
            throw new BusinessException("新增部门失败");
        }
    }

    /**
     * 修改部门接口
     *
     * @param id      主键
     * @param deptDto
     * @return
     */
    @Override
    public void updateDept(Long id, DeptDto deptDto) {
        if (StringUtils.isBlank(deptDto.getDeptCode()) || StringUtils.isBlank(deptDto.getDeptName())) {
            throw new BusinessException("部门编码或部门名称不能为空");
        }
        boolean update = update(Wrappers.<Department>lambdaUpdate()
                .eq(Department::getId, id)
                .set(Department::getDeptCode, deptDto.getDeptCode())
                .set(Department::getDeptName, deptDto.getDeptName())
                .set(Department::getUpdateTime, LocalDateTime.now()));
        if (!update) {
            throw new BusinessException("修改部门失败");
        }
    }

    /**
     * 查询回显
     *
     * @param id
     * @return
     */
    @Override
    public Department selectDeptById(Long id) {
        if (id == null) {
            throw new BusinessException("请求参数有误");
        }
        Department department = getById(id);
        if (department == null) {
            throw new BusinessException("没有当前部门");
        }
        return department;
    }

    /**
     * 根据id删除部门
     *
     * @param id
     * @return
     */
    @Override
    public void deleteDeptById(Long id) {
        //先查后删
        Department department = getById(id);
        if (department == null) {
            throw new BusinessException("部门不存在，无法删除！");
        }
        boolean b = removeById(id);
        if (!b) {
            throw new BusinessException("删除部门失败");
        }
    }
}
