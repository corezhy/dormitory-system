package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.model.dto.ClassDto;
import com.zhy.model.entity.Class;
import com.zhy.model.vo.ClassVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 班级管理mapper
 */
@Mapper
public interface ClassMapper extends BaseMapper<Class> {

    public Page<ClassVo> conditionQueryClass(Page<ClassVo> page, @Param("dto") ClassDto classDto);

    /**
     * 根据id查询班级
     * @param id
     * @return
     */
    @Select("select * from class where id = #{id}")
    Class getById(Long id);
}
