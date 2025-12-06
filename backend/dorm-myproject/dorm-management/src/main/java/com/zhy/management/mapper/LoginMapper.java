package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录接口
 */
@Mapper
public interface LoginMapper extends BaseMapper<Employee> {
}
