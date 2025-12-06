package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工信息实体类
 */
@Data
@ApiModel(description = "员工信息表")
@TableName("employee")
public class Employee {

    @TableId(value = "id", type = IdType.AUTO) //设置主键自增
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "员工编号", required = true, example = "EMP20250001")
    private String empNo;

    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @ApiModelProperty(value = "性别：0-女，1-男，2-未知", example = "1")
    private Integer gender;

    @ApiModelProperty(value = "手机号", example = "13812345678")
    private String phone;

    @ApiModelProperty(value = "身份证号", example = "11010119900307XXXX")
    private String idCard;

    @ApiModelProperty(value = "入职日期", required = true, example = "2025-01-01")
    private LocalDate entryDate;

    @ApiModelProperty(value = "职位", example = "宿舍管理员")
    private String position;

    @ApiModelProperty(value = "部门id", example = "1")
    private Long deptId;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@example.com")
    private String email;

    @ApiModelProperty(value = "头像路径", example = "/uploads/avatar/1.jpg")
    private String avatar;

    @ApiModelProperty(value = "登录用户名", required = true, example = "zhangsan")
    private String username;

    @ApiModelProperty(value = "密码（加密存储）", required = true, example = "123456")
    private String password;

    @ApiModelProperty(value = "创建时间", example = "2025-01-01 08:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2025-01-01 08:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}