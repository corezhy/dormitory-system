package com.zhy.management.mapper;

import com.zhy.model.vo.DeptEmpCountVo;
import com.zhy.model.vo.ViolationStuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 管理异常类统计mapper
 */
@Mapper
public interface ManagementMapper {


    /**
     * 按照专业统计违纪学生总数
     * @return
     */
    List<ViolationStuVo> violationStuCount();

    /**
     * 统计各部门员工数量
     * @return
     */
    List<DeptEmpCountVo> deptEmpCount();

}
