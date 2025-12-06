package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@ApiModel("新增修改员工dto")
public class AddAndEditEmpDto {

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别取值范围：0-女，1-男，2-未知")
    @Max(value = 2, message = "性别取值范围：0-女，1-男，2-未知")
    @ApiModelProperty(value = "性别：0-女，1-男，2-未知", required = true, example = "1")
    private Integer gender;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", required = true, example = "13812345678")
    private String phone;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(
            regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$",
            message = "身份证号格式不正确"
    )
    @ApiModelProperty(value = "身份证号", required = true, example = "11010119900307789X")
    private String idCard;

    @NotNull(message = "入职日期不能为空")
    @ApiModelProperty(value = "入职日期", required = true, example = "2025-01-01")
    private LocalDate entryDate;

    @NotBlank(message = "职位不能为空")
    @Size(max = 100, message = "职位名称长度不能超过100个字符")
    @ApiModelProperty(value = "职位", required = true, example = "宿舍管理员")
    private String position;

    @NotNull(message = "部门ID不能为空")
    @Positive(message = "部门ID必须为正整数")
    @ApiModelProperty(value = "部门id", required = true, example = "1")
    private Long deptId;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @ApiModelProperty(value = "邮箱", required = true, example = "zhangsan@example.com")
    private String email;

    @Size(max = 255, message = "头像路径长度不能超过255个字符")
    @ApiModelProperty(value = "头像路径", example = "/uploads/avatar/1.jpg")
    private String avatar;

    @NotBlank(message = "用户名不能为空")
    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9_]{2,19}$",
            message = "用户名必须以字母开头，由3-20位字母、数字或下划线组成"
    )
    @ApiModelProperty(value = "登录用户名", required = true, example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    @ApiModelProperty(value = "密码（加密存储）", required = true, example = "123456")
    private String password;


}