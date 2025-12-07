package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.model.dto.StudentDto;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.StudentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Map;

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


    /**
     * 根据宿舍Id获取最大床位数和该宿舍学生人数
     * @param roomId
     * @return
     */
    @Select("select max(bed_no) as max_bed, count(*) as stu_count from student where room_id = #{roomId} and status = 1")
    Map<String, Object> getMaxBedAndStuCountByRoomId(Long roomId);
}
