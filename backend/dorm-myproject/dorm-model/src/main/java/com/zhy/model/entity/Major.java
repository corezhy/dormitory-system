package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 专业字典表实体类（手动管理创建/修改时间）
 */
@Data
@TableName("major")
@ApiModel(description = "专业字典表")
public class Major {

    @ApiModelProperty(value = "专业代码（主键）", required = true, example = "080901")
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    @ApiModelProperty(value = "专业名称", required = true, example = "计算机科学与技术")
    private String name;

    @ApiModelProperty(value = "所属院系", example = "计算机学院")
    private String department;

    @ApiModelProperty(value = "是否启用：1=是，0=否", allowableValues = "0,1", example = "1")
    @TableField("is_enabled")
    private Integer isEnabled;

    @ApiModelProperty(value = "创建时间", readOnly = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间", readOnly = true)
    private LocalDateTime updateTime;
}