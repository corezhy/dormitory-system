package com.zhy.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 新增修改专业dto
 */
@Data
@ApiModel(description = "新增修改专业dto")
public class AddAndEditMajorDto {

    @ApiModelProperty(value = "专业代码（主键）", required = true, example = "080901")
    @TableId(value = "code", type = IdType.INPUT)
    @NotBlank(message = "专业代码不能为空")
    private String code;

    @ApiModelProperty(value = "专业名称", required = true, example = "计算机科学与技术")
    @NotBlank(message = "专业名称不能为空")
    private String name;

    @ApiModelProperty(value = "所属院系", example = "计算机学院")
    @NotBlank(message = "所属院系不能为空")
    private String department;

    @ApiModelProperty(value = "是否启用：1=是，0=否", allowableValues = "0,1", example = "1")
    @TableField("is_enabled")
    @NotNull(message = "启用状态不能为空")
    private Integer isEnabled;
}
