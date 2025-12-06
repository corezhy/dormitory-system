package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 班级表
 *
 * @author 小琪（和夫君一起写的 ❤️）
 * @since 2025-11-22
 */
@Data
@TableName("class")
@ApiModel(value = "Class对象", description = "班级表")
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("班级ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("班级编码（如 COMP2301）")
    @TableField("code")
    private String code;

    @ApiModelProperty("班级全称（如 计算机科学与技术2301班）")
    @TableField("name")
    private String name;

    @ApiModelProperty("所属专业代码（关联 major.code）")
    @TableField("major_code")
    private String majorCode;

    @ApiModelProperty("入学年级（如 2023）")
    @TableField("grade")
    private Integer grade;

    @ApiModelProperty("辅导员姓名")
    @TableField("counselor")
    private String counselor;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("最后更新时间（由应用层设置）")
    @TableField("update_time")
    private LocalDateTime updateTime;
}