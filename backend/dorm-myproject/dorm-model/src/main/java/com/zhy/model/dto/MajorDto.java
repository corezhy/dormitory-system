package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ApiModel(description = "专业查询dto")
public class MajorDto {

    //分页参数
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页展示记录数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "专业名称", required = true, example = "软件工程")
    private String name;

    @ApiModelProperty(value = "所属院系", example = "计算机学院")
    private String department;

    @ApiModelProperty(value = "是否启用", example = "1")
    private Integer isEnabled;

}