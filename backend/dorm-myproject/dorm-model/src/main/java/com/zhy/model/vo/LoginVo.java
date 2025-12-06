package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工登录后返回
 */
@Data
@ApiModel(description = "员工登录vo")
public class LoginVo {

    //员工id（主键自增）
    @ApiModelProperty(value = "员工id")
    private Long id;

    //用户名
    @ApiModelProperty("用户名")
    private String username;

    //员工姓名
    @ApiModelProperty("员工姓名")
    private String name;

    //token
    @ApiModelProperty("token")
    private String token;
}
