package com.zhy.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zhy.model.entity.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(description = "查询员工vo")
public class EmployeeVo {

    @TableId(value = "id", type = IdType.AUTO) //设置主键自增
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @ApiModelProperty(value = "头像路径", example = "/uploads/avatar/1.jpg")
    private String avatar;

    @ApiModelProperty(value = "部门名称", required = true)
    private String deptName;

    @ApiModelProperty(value = "手机号", example = "13812345678")
    private String phone;

    @ApiModelProperty(value = "职位", example = "宿舍管理员")
    private String position;

    @ApiModelProperty(value = "部门id", example = "1")
    private Long deptId;

    @ApiModelProperty(value = "入职日期", required = true, example = "2025-01-01")
    private LocalDate entryDate;

}
