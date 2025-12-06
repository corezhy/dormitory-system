package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 班级查询dto
 */
@Data
@ApiModel(description = "班级查询dto")
public class ClassDto {

    //分页参数
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页展示记录数")
    private Integer pageSize = 10;

    //班级名称
    @ApiModelProperty(value = "班级名称")
    private String name;

    //专业代码
    @ApiModelProperty(value = "专业代码")
    private String majorCode;

    //入学年级
    @ApiModelProperty(value = "入学年级")
    private Integer grade;

}
