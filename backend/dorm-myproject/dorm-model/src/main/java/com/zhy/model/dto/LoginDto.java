package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工登录请求参数
 */
@Data
@ApiModel(description = "员工登录dto")
public class LoginDto {

    //用户名
    @ApiModelProperty(value = "用户名")
    private String username;
    //密码
    @ApiModelProperty(value = "密码")
    private String password;

}
