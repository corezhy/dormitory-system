package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.ManagementMapper;
import com.zhy.management.service.ManagementService;
import com.zhy.model.vo.DeptEmpCountVo;
import com.zhy.model.vo.ViolationStuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理异常类统计service实现类
 */
@Service
public class ManagementServiceImpl implements ManagementService {


    @Autowired
    private ManagementMapper managementMapper;


    /**
     * 违纪专业学生统计
     *
     * @return
     */
    @Override
    public List<ViolationStuVo> violationStuCount() {
        List<ViolationStuVo> voList = managementMapper.violationStuCount();
        if (CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计专业违纪学生失败");
        }
        return voList;
    }

    /**
     * 统计各部门员工数量
     *
     * @return
     */
    @Override
    public List<DeptEmpCountVo> deptEmpCount() {
        List<DeptEmpCountVo> voList = managementMapper.deptEmpCount();
        if (CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计各部门员工总数失败");
        }
        return voList;
    }


}
