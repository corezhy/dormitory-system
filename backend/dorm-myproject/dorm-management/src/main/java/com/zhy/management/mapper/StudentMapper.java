package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.model.dto.StudentDto;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.StudentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * 学生管理mapper层接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {


    /**
     * 条件分页查询学生
     * @param page
     * @param dto
     * @return
     */
    Page<StudentVo> conditionPageQueryStu(Page<StudentVo> page,@Param("dto") StudentDto dto);

    /**
     * 违纪扣分
     * @param id
     * @param score
     * @param updateTime
     * @return
     */
    @Update("UPDATE student SET violation_score = IFNULL(violation_score, 0) + #{score}, update_time = #{updateTime} WHERE id = #{id}")
    boolean addViolationScore(@Param("id") Long id, @Param("score") Integer score, @Param("updateTime") LocalDateTime updateTime);
}
