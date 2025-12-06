package com.zhy.management.service;

import com.zhy.model.vo.DeptEmpCountVo;
import com.zhy.model.vo.ViolationStuVo;

import java.util.List;

/**
 * 管理异常类统计service
 */

public interface ManagementService {

    /**
     * 违纪专业学生统计
     * @return
     */
    List<ViolationStuVo> violationStuCount();

    /**
     * 统计各部门员工数量
     * @return
     */
    List<DeptEmpCountVo> deptEmpCount();

}
