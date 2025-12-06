package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.LoginMapper;
import com.zhy.management.service.LoginService;
import com.zhy.model.dto.LoginDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.LoginVo;
import com.zhy.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口实现类
 */
@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Employee> implements LoginService {

    // 直接创建实例 参数： 强度参数（4~31，默认10）
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    @Override
    public LoginVo login(LoginDto loginDto) {
        //用户名
        String username = loginDto.getUsername();
        //密码
        String password = loginDto.getPassword();
        //参数校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            throw new BusinessException("用户名或密码不能为空");
        }
        //1.根据用户名查询用户（不查密码）
        Employee employee = getOne(Wrappers.<Employee>lambdaQuery().eq(Employee::getUsername, username));
        if (employee == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String storedPassword = employee.getPassword();
        boolean passwordValid = false;

        //2.判断是否已经是BCrypt加密（以 $2a$ ,$2b$, $2y$ 开头）
        if (storedPassword != null && (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$"))) {
            //新密码：用BCrypt验证
            passwordValid = passwordEncoder.matches(password, storedPassword);
        }else {
            //老密码：尝试明文比对
            if (password.equals(storedPassword)) {
                passwordValid = true;
                //登陆成功！立即升级为BCrypt
                employee.setPassword(passwordEncoder.encode(password));
                employee.setUpdateTime(LocalDateTime.now());
                //更新
                updateById(employee);
            }
        }

        if (!passwordValid) {
            throw new BusinessException("用户名或密码错误");
        }

        //生成JWT令牌
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", employee.getId());
        dataMap.put("username", employee.getUsername());
        String token = JwtUtils.generateJwt(dataMap);
        //返回data数据
        LoginVo loginVo = new LoginVo();
        //属性拷贝
        BeanUtils.copyProperties(employee,loginVo);
        //添加token
        loginVo.setToken(token);
        //返回数据
        return loginVo;
    }
}
