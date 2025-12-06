package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门管理mapper接口
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
