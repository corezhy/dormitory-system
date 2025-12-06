package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditClassDto;
import com.zhy.model.dto.ClassDto;
import com.zhy.model.entity.Class;
import com.zhy.model.vo.ClassVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 班级管理service接口
 */
public interface ClassService extends IService<Class> {

    /**
     * 条件查询班级
     * @param classDto
     * @return
     */
    public Page<ClassVo> conditionQueryClass(ClassDto classDto);

    /**
     * 新增班级
     * @param dto
     * @return
     */
    public void insertClass(AddAndEditClassDto dto);

    /**
     * 根据id查询回显
     * @param id
     * @return
     */
    public Class getById(Long id);

    /**
     * 根据id修改班级
     * @param id
     * @param dto
     * @return
     */
    public void editClass(Long id, AddAndEditClassDto dto);

    /**
     * 批量删除班级
     * @param ids
     * @return
     */
    public void deleteBatch(List<Integer> ids);

    /**
     * 根据班级名称查询班级Id
     * @param className
     * @return
     */
    Long getClassIdByClassName(String className);

    /**
     * 根据班级id查询班级名称
     * @param classId
     * @return
     */
    String getClassNameByClassId(Long classId);
}
