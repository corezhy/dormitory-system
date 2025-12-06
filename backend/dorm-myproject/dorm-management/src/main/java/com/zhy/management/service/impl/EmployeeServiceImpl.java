package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.EmployeeMapper;
import com.zhy.management.service.EmployeeService;
import com.zhy.model.dto.AddAndEditEmpDto;
import com.zhy.model.dto.EmployeeDto;
import com.zhy.model.entity.Employee;
import com.zhy.model.vo.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 员工管理业务层接口实现类
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    // 直接创建实例
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * 员工查询
     *
     * @param employeeDto
     * @return
     */
    @Override
    public Page<EmployeeVo> select(EmployeeDto employeeDto) {
        //分页条件
        Page<EmployeeVo> page = Page.of(employeeDto.getPageNum(), employeeDto.getPageSize());

        //调用mapper方法多表查询
        return employeeMapper.selectEmpAndDeptName(page, employeeDto);
    }

    /**
     * 新增员工接口
     *
     * @param addAndEditEmpDto
     * @return
     */
    @Override
    public void insert(AddAndEditEmpDto addAndEditEmpDto) {
        //创建员工对象
        Employee employee = new Employee();
        //属性拷贝
        BeanUtils.copyProperties(addAndEditEmpDto, employee);
        //生成员工编号
        String empNo = generateEmpNo();
        employee.setEmpNo(empNo);
        //创建时间和修改时间为当前时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //使用BCrypt对密码明文加密
        String encodePassword = passwordEncoder.encode(addAndEditEmpDto.getPassword());
        employee.setPassword(encodePassword);
        boolean save = save(employee);
        if (!save) {
            throw new BusinessException("新增员工失败");
        }
    }

    /**
     * 员工查询回显
     *
     * @param id
     * @return
     */
    @Override
    public Employee selectById(Long id) {
        Employee employee = getById(id);
        if (employee == null) {
            throw new BusinessException("没有当前员工");
        }
        //前端提供修改密码，但是BCrypt不支持解密，所以我们直接返回******即可
        employee.setPassword("******");
        return employee;
    }

    /**
     * 修改员工
     *
     * @param id
     * @param addAndEditEmpDto
     * @return
     */
    @Override
    public void update(Long id, AddAndEditEmpDto addAndEditEmpDto) {
        //参数校验
        if (id == null) {
            throw new BusinessException("员工Id不能为空");
        }
        //创建修改条件
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
        //构造修改条件
        updateWrapper.set(Employee::getName, addAndEditEmpDto.getName())
                .set(Employee::getGender, addAndEditEmpDto.getGender())
                .set(Employee::getPhone, addAndEditEmpDto.getPhone())
                .set(Employee::getIdCard, addAndEditEmpDto.getIdCard())
                .set(Employee::getEntryDate, addAndEditEmpDto.getEntryDate())
                .set(Employee::getPosition, addAndEditEmpDto.getPosition())
                .set(Employee::getDeptId, addAndEditEmpDto.getDeptId())
                .set(Employee::getEmail, addAndEditEmpDto.getEmail())
                .set(Employee::getAvatar, addAndEditEmpDto.getAvatar())
                .set(Employee::getUsername, addAndEditEmpDto.getUsername())
                .eq(Employee::getId, id);
        //密码比较特殊，由于我们回显给前端的是******，如果传递的还是******说明用户没有修改密码，此时密码不需要更新
        if (!addAndEditEmpDto.getPassword().equals("******")) {
            //对用户输入的密码进行BCrypt加密
            String rawPassword = addAndEditEmpDto.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            updateWrapper.set(Employee::getPassword, encodedPassword);
        }

        //修改时间改为当前时间
        updateWrapper.set(Employee::getUpdateTime, LocalDateTime.now());

        //修改
        boolean update = update(updateWrapper);
        if (!update) {
            throw new BusinessException("修改员工失败");
        }

        /*
        //没有考虑到回显的密码是******,如果用户没有修改密码，数据库密码就会变成******
        //1.创建员工对象，并赋值id
        Employee employee = new Employee();
        employee.setId(id);
        //2.属性拷贝
        BeanUtils.copyProperties(addAndEditEmpDto, employee);
        //3.如果密码不为******就修改密码，并对密码加密
        if (!addAndEditEmpDto.getPassword().equals("******")) {
            String rawPassword = addAndEditEmpDto.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            employee.setPassword(encodedPassword);
        }
        //4.修改时间改为当前时间
        employee.setUpdateTime(LocalDateTime.now());
        //5.修改
        boolean b = updateById(employee);
        if (!b) {
            return Result.error("修改员工失败");
        }
        return Result.success("修改员工成功");*/
        /*
        //由于updateById不会报唯一性约束异常，所以该代码冗余
        //先查询员工如果数据库唯一约束的数据一致，不参与修改
        Employee employee = getById(editEmpDto.getId());
        if (employee == null) {
            return Result.success("员工不存在");
        }
        //创建MybatisPlus修改条件
        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();

        //判断唯一约束字段如果传递的和原来的一致，不参与修改（emp_no(不会传递), id_card, phone, username）
        if (!editEmpDto.getIdCard().equals(employee.getIdCard())) {
            updateWrapper.set(Employee::getIdCard, editEmpDto.getIdCard());
        }
        if (!editEmpDto.getPhone().equals(employee.getPhone())) {
            updateWrapper.set(Employee::getPhone, editEmpDto.getPhone());
        }
        if (!editEmpDto.getUsername().equals(employee.getUsername())) {
            updateWrapper.set(Employee::getUsername, editEmpDto.getUsername());
        }

        //其余字段都参与修改
        updateWrapper.set(Employee::getEntryDate, editEmpDto.getEntryDate())
                .set(Employee::getGender, editEmpDto.getGender())
                .set(Employee::getPosition, editEmpDto.getPosition())
                .set(Employee::getName, editEmpDto.getName())
                .set(Employee::getDeptId, editEmpDto.getDeptId())
                .set(Employee::getEmail, editEmpDto.getEmail())
                .set(Employee::getAvatar, editEmpDto.getAvatar())
                .set(Employee::getPassword, passwordEncoder.encode(editEmpDto.getPassword()))
                .set(Employee::getUpdateTime, LocalDateTime.now())
                .eq(Employee::getId, editEmpDto.getId());
        //修改
        boolean update = update(updateWrapper);
        if (!update) {
            return Result.error("修改员工失败");
        }
        return Result.success("修改员工成功");*/
    }

    /**
     * 批量删除员工
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("请选择要删除的员工");
        }
        //去重，防止前端误传重复 ID
        List<Integer> uniqueIds = new ArrayList<>(new LinkedHashSet<>(ids));

        boolean b = removeBatchByIds(uniqueIds);
        if(!b) {
            throw new BusinessException("删除失败，部分员工不存在或已经被删除");
        }
    }

    //生成员工编号
    public String generateEmpNo() {
        String prefix = "EMP" + LocalDate.now().getYear();
        String maxNo = employeeMapper.findMaxEmpNoByPrefix(LocalDate.now().getYear()); // 查询 LIKE 'EMP2025%'

        int seq = 1;
        if (maxNo != null) {
            // 从 EMP20250015 提取 0015 → 转成 15
            String numPart = maxNo.substring(prefix.length());
            seq = Integer.parseInt(numPart) + 1;
        }
        return String.format("%s%04d", prefix, seq);
    }


}
