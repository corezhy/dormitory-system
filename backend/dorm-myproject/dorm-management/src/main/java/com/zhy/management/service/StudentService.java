package com.zhy.management.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.groups.ValidationGroups;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditStuDto;
import com.zhy.model.dto.StudentDto;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.StudentEditVo;
import com.zhy.model.vo.StudentVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 学生管理service接口
 */
public interface StudentService extends IService<Student> {

    /**
     * 条件分页查询学生
     * @param dto
     * @return
     */
    public Page<StudentVo> conditionPageQueryStu(StudentDto dto);

    /**
     * 新增学生
     * @param dto
     * @return
     */
    public void insertStu(AddAndEditStuDto dto);

    /**
     * 根据id查询回显学生
     * @param id
     * @return
     */
    public StudentEditVo getStuById(Long id);

    /**
     * 修改学生
     * @param id
     * @param dto
     * @return
     */
    public void updateStu(Long id, AddAndEditStuDto dto);

    /**
     * 违纪扣分
     * @param id
     * @param score
     * @return
     */
    public void discipline(Long id, Integer score);

    /**
     * 批量删除学生
     * @param uniqueIds
     * @return
     */
    public void deleteBatch(List<Integer> uniqueIds);

    /**
     * 查询该楼栋下是否有学生，如果有，不允许停用
     * @param
     * @return
     */
    boolean hasStuByBuildingId(Long buildingId);

    /**
     * 查询宿舍下是否有学生，如果有，不允许停用/删除
     * @param dormRoomId
     * @return
     */
    boolean hasStuByDormRoomId(Long dormRoomId);

    /**
     * 查询多个宿舍下是否有学生，如果有，不允许删除
     * @param dormRoomIds
     * @return
     */
    boolean hasStuByDormRoomIds(List<Integer> dormRoomIds);

    /**
     * 根据班级id集合查询下面是否有学生，如果有，不允许删除
     * @param classIds
     * @return
     */
    boolean hasStuByClassIds(List<Integer> classIds);

    /**
     * 根据专业代码查询是否存在学生
     * @param majorCode
     * @return
     */
    boolean getStuByMajorCode(String majorCode);

    /**
     * 根据专业代码集合查询是否存在学生
     * @param codes
     * @return
     */
    boolean getStuByMajorCodes(List<String> codes);

    /**
     * 删除已退宿学生
     * @return
     */
    void removeStuByStatusIsZero();
}
