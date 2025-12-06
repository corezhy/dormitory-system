package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门信息实体类
 */
@Data
@ApiModel(description = "部门信息实体")
@TableName("department") //如果表名和类名不一样，一定要加，默认类名转驼峰 Dept->dept sysDept->sys_dept
public class Department {

    /**
     * 部门ID
     */
    @TableId(value = "id", type = IdType.AUTO) //设置主键自增
    @ApiModelProperty(value = "部门ID", required = true)
    private Long id;

    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码", required = true)
    private String deptCode;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", required = true)
    private String deptName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;
}