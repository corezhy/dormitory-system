package com.zhy.management.controller;

import com.zhy.common.result.Result;
import com.zhy.management.service.LoginService;
import com.zhy.model.dto.LoginDto;
import com.zhy.model.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 */
@RestController
@Api(tags = "员工登录接口")
public class LoginController {


    @Autowired
    private LoginService loginService;


    /**
     * 登录
     * @param loginDto 登录参数
     * @return 响应数据
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = loginService.login(loginDto);
        return Result.success(loginVo);
    }

}
