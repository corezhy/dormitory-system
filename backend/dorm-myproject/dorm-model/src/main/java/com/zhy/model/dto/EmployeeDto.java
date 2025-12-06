package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * 员工查询dto
 */
@Data
@ApiModel(description = "员工查询dto")
public class EmployeeDto {

    //分页参数
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页展示记录数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @ApiModelProperty(value = "部门Id", example = "1")
    private Long deptId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入职日期开始时间")
    private LocalDate begin; //入职日期

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "入职日期结束时间")
    private LocalDate end; //入职日期


}
