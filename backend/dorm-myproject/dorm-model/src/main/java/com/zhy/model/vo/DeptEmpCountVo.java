package com.zhy.model.vo;

import lombok.Data;

/**
 * 统计部门员工数量
 */
@Data
public class DeptEmpCountVo {


    private String deptName; //部门名称

    private Integer employeeCount; //员工数量

}
