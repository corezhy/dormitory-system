package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询部门后返回
 */
@Data
@ApiModel(description = "查询部门vo")
public class DepartmentVo {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", required = true)
    private String deptName;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

}
