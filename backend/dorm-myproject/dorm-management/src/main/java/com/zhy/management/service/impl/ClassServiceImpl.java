package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.ClassMapper;
import com.zhy.management.service.ClassService;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditClassDto;
import com.zhy.model.dto.ClassDto;
import com.zhy.model.entity.Class;
import com.zhy.model.vo.ClassVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 班级管理service实现类
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {


    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private StudentService studentService;

    /**
     * 条件查询班级
     *
     * @param classDto
     * @return
     */
    @Override
    public Page<ClassVo> conditionQueryClass(ClassDto classDto) {
        //分页配置
        Page<ClassVo> page = Page.of(classDto.getPageNum(), classDto.getPageSize());
        //查询
        return classMapper.conditionQueryClass(page, classDto);
    }

    /**
     * 新增班级
     *
     * @param dto
     * @return
     */
    @Override
    public void insertClass(AddAndEditClassDto dto) {
        //创建班级对象
        Class classObject = new Class();
        //属性拷贝
        BeanUtils.copyProperties(dto, classObject);
        //补全参数
        classObject.setCreateTime(LocalDateTime.now());
        classObject.setUpdateTime(LocalDateTime.now());
        //新增
        boolean save = save(classObject);
        if (!save) {
            throw new BusinessException("新增班级失败");
        }
    }

    /**
     * 根据id查询回显
     *
     * @param id
     * @return
     */
    @Override
    public Class getById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择您要查询的班级");
        }
        //查询
        Class classObject = classMapper.getById(id);
        if (classObject == null) {
            throw new BusinessException("查询不到该班级");
        }
        return classObject;
    }

    /**
     * 根据id修改班级
     *
     * @param id
     * @param dto
     * @return
     */
    @Override
    public void editClass(Long id, AddAndEditClassDto dto) {
        if (id == null) {
            throw new BusinessException("请选择您要修改的班级");
        }
        //创建班级对象
        Class classObject = new Class();
        //属性拷贝
        BeanUtils.copyProperties(dto, classObject);
        //补全参数
        classObject.setId(id);
        classObject.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(classObject);
        if (!b) {
            throw new BusinessException("修改班级失败");
        }
    }

    /**
     * 批量删除班级
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        //去重，防止前端误传重复ID
        List<Integer> uniqueIds = new ArrayList<>(new LinkedHashSet<>(ids));
        if (CollectionUtils.isEmpty(uniqueIds)) {
            throw new BusinessException("请选择需要删除的班级");
        }
        //如果班级下有学生，不允许删除
        boolean exist = studentService.hasStuByClassIds(uniqueIds);
        if (exist) {
            throw new BusinessException("该班级下存在学生，无法删除");
        }
        //删除
        boolean b = removeBatchByIds(uniqueIds);
        if (!b) {
            throw new BusinessException("所选班级不存在或已经被删除");
        }
    }

    /**
     * 根据班级名称查询班级Id
     *
     * @param className
     * @return
     */
    @Override
    public Long getClassIdByClassName(String className) {
        Class aClass = getOne(Wrappers.<Class>lambdaQuery()
                .select(Class::getId)
                .eq(Class::getName, className));
        if (aClass == null) {
            throw new BusinessException("班级 [" + className + "] 不存在");
        }
        return aClass.getId();
    }

    /**
     * 根据班级id查询班级名称
     *
     * @param classId
     * @return
     */
    @Override
    public String getClassNameByClassId(Long classId) {
        Class aClass = getOne(Wrappers.<Class>lambdaQuery()
                .select(Class::getName)
                .eq(Class::getId, classId));
        if (aClass == null) {
            throw new BusinessException("查询班级名称失败");
        }
        return aClass.getName();
    }
}
