package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.LoginDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.LoginVo;

/**
 * 登录接口
 */
public interface LoginService extends IService<Employee> {


    /**
     * 登录
     * @param loginDto
     * @return
     */
    public LoginVo login(LoginDto loginDto);

}
