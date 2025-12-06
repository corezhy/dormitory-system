package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 新增修改班级 DTO
 */
@Data
@ApiModel(description = "新增或修改班级信息的数据传输对象")
public class AddAndEditClassDto {

    /**
     * 班级编码（唯一标识）
     */
    @ApiModelProperty(value = "班级编码，格式如 COMP2301", example = "COMP2301", required = true)
    @NotBlank(message = "班级编码不能为空")
    @Pattern(regexp = "^[A-Z]{2,6}\\d{2,4}$", message = "班级编码格式不正确，应为大写字母+数字，如：COMP2301")
    private String code;

    /**
     * 班级名称
     */
    @ApiModelProperty(value = "班级全称", example = "计算机科学与技术2301班", required = true)
    @NotBlank(message = "班级名称不能为空")
    private String name;

    /**
     * 所属专业编码
     */
    @ApiModelProperty(value = "所属专业的编码，需已存在", example = "CS2023", required = true)
    @NotBlank(message = "专业编码不能为空")
    private String majorCode;

    /**
     * 入学年份（年级）
     */
    @ApiModelProperty(value = "入学年份，如 2023", example = "2023", required = true)
    @NotNull(message = "年级不能为空")
    private Integer grade;

    /**
     * 辅导员姓名
     */
    @ApiModelProperty(value = "辅导员姓名", example = "张老师", required = true)
    @NotBlank(message = "辅导员姓名不能为空")
    private String counselor;
}