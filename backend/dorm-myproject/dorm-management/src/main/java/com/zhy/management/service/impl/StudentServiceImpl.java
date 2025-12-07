package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.BuildingMapper;
import com.zhy.management.mapper.ClassMapper;
import com.zhy.management.mapper.DormRoomMapper;
import com.zhy.management.mapper.StudentMapper;
import com.zhy.management.param.RoomUpdate;
import com.zhy.management.service.BuildingService;
import com.zhy.management.service.ClassService;
import com.zhy.management.service.DormRoomService;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditStuDto;
import com.zhy.model.dto.StudentDto;
import com.zhy.model.entity.Building;
import com.zhy.model.entity.Class;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.StudentEditVo;
import com.zhy.model.vo.StudentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 学生管理service实现类
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ClassService classService;

    @Autowired
    private DormRoomService dormRoomService;

    @Autowired
    private BuildingService buildingService;


    /**
     * 条件分页查询学生
     *
     * @param dto
     * @return
     */
    @Override
    public Page<StudentVo> conditionPageQueryStu(StudentDto dto) {
        //分页参数
        Page<StudentVo> page = Page.of(dto.getPageNum(), dto.getPageSize());
        //调用mapper层查询
        return studentMapper.conditionPageQueryStu(page, dto);
    }

    /**
     * 新增学生
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public void insertStu(AddAndEditStuDto dto) {
        // 1. 根据宿舍完整编号查询宿舍Id（同时查出 buildingId 用于性别校验）
        DormRoom dormRoom = dormRoomService.getIdAndBuildingIdByFullCode(dto.getFullCode());
        Long dormRoomId = dormRoom.getId();

        //判断新增学生床铺号是否在宿舍包含范围内
        if (dto.getBedNo() > dormRoom.getCapacity()) {
            throw new BusinessException("宿舍[" + dto.getFullCode() + "没有" + dto.getBedNo() + "这个床位");
        }

        // 2. 性别校验：通过 buildingId 查询楼栋性别限制
        Building building = buildingService.selectById(dormRoom.getBuildingId());
        if (building.getStatus() != 1) {
            throw new BusinessException("楼栋[" + building.getName() + "]停用中");
        }
        Integer genderTypeInt = building.getGenderType();
        Byte genderType = genderTypeInt.byteValue();
        if (!Objects.equals(genderType, dto.getGender())) {
            String allowedGender = building.getGenderType() == 0 ? "男生" : "女生";
            throw new BusinessException("该宿舍仅限" + allowedGender + "入住");
        }

        // 3. 原子减少空闲床位（高并发安全）
        int update = dormRoomService.reduceAvailableBedsById(dormRoomId);
        if (update == 0) {
            throw new BusinessException("宿舍[" + dto.getFullCode() + "]床铺已满，无法入住");
        }

        // 4. 根据班级名称查询班级Id
        Long classId = classService.getClassIdByClassName(dto.getClassName());

        // 5. 创建并填充学生对象
        Student stu = new Student();
        BeanUtils.copyProperties(dto, stu);
        stu.setClassId(classId);
        stu.setRoomId(dormRoomId);
        stu.setCheckInTime(LocalDateTime.now());
        stu.setCreateTime(LocalDateTime.now());
        stu.setUpdateTime(LocalDateTime.now());
        stu.setViolationScore(0); // 违纪扣分（默认为0）
        stu.setStatus((byte) 1);  // 状态（1=在住，0=已退宿）

        // 6. 新增学生
        boolean save = save(stu);
        if (!save) {
            throw new BusinessException("新增学生失败");
        }
    }

    /**
     * 根据id查询回显学生
     *
     * @param id
     * @return
     */
    @Override
    public StudentEditVo getStuById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择要查询的学生");
        }
        //查询
        Student student = getById(id);
        if (student == null) {
            throw new BusinessException("查询的学生不存在或已被删除");
        }
        //创建返回VO对象
        StudentEditVo studentEditVo = new StudentEditVo();
        //属性拷贝
        BeanUtils.copyProperties(student, studentEditVo);
        //根据班级id查询班级名称
        String className = classService.getClassNameByClassId(student.getClassId());
        studentEditVo.setClassName(className);
        //根据宿舍Id查询宿舍编号，只有在住的学生才有宿舍信息
        if (student.getRoomId() != null) {
            String fullCode = dormRoomService.getFullCodeById(student.getRoomId());
            studentEditVo.setFullCode(fullCode);
        }else {
            studentEditVo.setFullCode("");
        }
        return studentEditVo;
    }

    /**
     * 修改学生
     *
     * @param id
     * @param studentNew
     * @return
     */
    @Override
    @Transactional
    public void updateStu(Long id, AddAndEditStuDto studentNew) {
        //根据id查询该学生
        Student studentOld = getById(id);
        if (studentOld == null) throw new BusinessException("学生不存在");
        //学生一旦创建，不允许修改性别
        if (!studentOld.getGender().equals(studentNew.getGender())) {
            throw new BusinessException("不允许修改学生的性别");
        }

        //如果学生已退宿，释放空闲床位，结束后续操作
        if (studentNew.getStatus() == 0) { //1 在住 0 已退宿
            // 1.释放床位
            if (studentOld.getRoomId() != null) {
                dormRoomService.releaseAvailableBedsById(studentOld.getRoomId());
            }
            //2. 清空学生宿舍相关字段
            boolean update = update(Wrappers.<Student>lambdaUpdate()
                    .eq(Student::getId, id)
                    .set(Student::getBuildingId, null)
                    .set(Student::getFloorId, null)
                    .set(Student::getRoomId, null)
                    .set(Student::getBedNo, null)
                    .set(Student::getStatus, (byte) 0)
                    .set(Student::getUpdateTime, LocalDateTime.now()));
            if (!update) {
                throw new BusinessException("修改已退宿学生失败");
            }
            return;
        }


        //获取原宿舍 fullCode
        String oldFullCode = "";
        if (studentOld.getRoomId() != null) {
            oldFullCode = dormRoomService.getFullCodeById(studentOld.getRoomId());
        }
        //1. 判断是否修改了宿舍信息(buildingId 或 floorId 或fullCode)
        boolean dormChanged = !Objects.equals(studentOld.getBuildingId(), studentNew.getBuildingId()) ||
                !Objects.equals(studentOld.getFloorId(), studentNew.getFloorId()) ||
                !Objects.equals(oldFullCode, studentNew.getFullCode());
        //定义变量接收宿舍Id(默认不变)
        Long newRoomId = studentOld.getRoomId();
        if (dormChanged) {
            //2.校验新宿舍合法性
            Building building = buildingService.selectById(studentNew.getBuildingId());
            if (building.getStatus() != 1) { //1启用 0停用
                throw new BusinessException("[" + building.getName() + "]停用中, 暂不可用");
            }
            //3.性别校验：学生性别vs楼栋性别限制
            Integer genderTypeInteger = building.getGenderType(); //0男 1女
            Byte genderType = genderTypeInteger.byteValue();
            if (!genderType.equals(studentOld.getGender())) {
                throw new BusinessException("该楼层仅限" + (genderType == 0 ? "男生" : "女生") + "入住");
            }
            //4.检查新房间是否存在
            DormRoom dormRoomNew = dormRoomService.getDormRoomByFullCodeAndBuildingIdAndFloorId(studentNew.getFullCode(),
                    studentNew.getBuildingId(), studentNew.getFloorId());
            //校验宿舍是否可用
            if (dormRoomNew.getStatus() != 1) {
                throw new BusinessException("宿舍[" + dormRoomNew.getFullCode() + "]当前不可用（停用或者维修中）");
            }
            //判断学生床铺号是否在床位数内
            if (studentNew.getBedNo() > dormRoomNew.getCapacity()) {
                throw new BusinessException("宿舍[" + dormRoomNew.getFullCode() + "]没有" + studentNew.getBedNo() + "这个床位");
            }
            //房间号存在，为newRoomId赋值
            newRoomId = dormRoomNew.getId();
            //5. 更新原房间：释放床位(+1) —— 仅当原宿舍存在
            if (studentOld.getRoomId() != null) {
                dormRoomService.releaseAvailableBedsById(studentOld.getRoomId());
                // 不需要检查返回值，DB 约束已兜底
            }

            //6. 更新新房间：占用床位（-1）
            int updateNew = dormRoomService.reduceAvailableBedsById(dormRoomNew.getId());
            if (updateNew == 0) {
                throw new BusinessException("宿舍[" + dormRoomNew.getFullCode() + "]床铺已满，无法入住");
            }
        }
        //7.更新学生信息
        //创建学生对象
        Student stu = new Student();
        //属性拷贝
        BeanUtils.copyProperties(studentNew, stu);
        //参数补全
        //根据班级名称查询班级id
        Long classId = classService.getClassIdByClassName(studentNew.getClassName());
        stu.setId(id);
        stu.setRoomId(newRoomId);
        stu.setClassId(classId);
        stu.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(stu);
        if (!b) {
            throw new BusinessException("修改学生失败");
        }
    }

    /**
     * 违纪扣分
     *
     * @param id
     * @param score
     * @return
     */
    @Override
    public void discipline(Long id, Integer score) {
        if (id == null || score == null || score <= 0) {
            throw new BusinessException("参数无效");
        }
        boolean b = studentMapper.addViolationScore(id, score, LocalDateTime.now());
        if (!b) {
            throw new BusinessException("学生不存在或扣分失败");
        }
    }

    /**
     * 批量删除学生
     *
     * @param uniqueIds
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Integer> uniqueIds) {
        if (CollectionUtils.isEmpty(uniqueIds)) {
            throw new BusinessException("请选择要删除的学生");
        }
        //获取要删除学生的宿舍id，并将空闲床铺 + 1
        List<Student> studentList = listByIds(uniqueIds);
        if (CollectionUtils.isEmpty(studentList)) {
            throw new BusinessException("所选学生不存在或已经被删除");
        }
        if (studentList.size() != uniqueIds.size()) {
            throw new BusinessException("部分学生不存在，无法删除");
        }

        //统计每个宿舍要释放的床位数
        Map<Long, Long> roomIdToCount = studentList.stream()
                .map(Student::getRoomId)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        //转为RoomUpdate列表
        if (!roomIdToCount.isEmpty()) {
            List<RoomUpdate> updates = roomIdToCount.entrySet().stream()
                    .map(entry -> new RoomUpdate(entry.getKey(), entry.getValue().intValue()))
                    .collect(Collectors.toList());
            int updatedRows = dormRoomService.updateAvailableBedsByCounts(updates);
            log.debug("更新了{}个宿舍的空闲床位", updatedRows);
        }
        //批量删除
        studentMapper.deleteBatchIds(uniqueIds);
    }

    /**
     * 根据楼栋Id查询该楼栋下是否有学生，如果有，不允许停用/删除
     *
     * @param buildingId
     * @return
     */
    @Override
    public boolean hasStuByBuildingId(Long buildingId) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .eq(Student::getBuildingId, buildingId));
    }

    /**
     * 查询宿舍下是否有学生，如果有，不允许停用/删除
     *
     * @param dormRoomId
     * @return
     */
    @Override
    public boolean hasStuByDormRoomId(Long dormRoomId) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .eq(Student::getRoomId, dormRoomId));
    }

    /**
     * 查询多个宿舍下是否有学生，如果有，不允许删除
     *
     * @param dormRoomIds
     * @return
     */
    @Override
    public boolean hasStuByDormRoomIds(List<Integer> dormRoomIds) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .in(Student::getRoomId, dormRoomIds));
    }

    /**
     * 根据班级id集合查询下面是否有学生，如果有，不允许删除
     *
     * @param classIds
     * @return
     */
    @Override
    public boolean hasStuByClassIds(List<Integer> classIds) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .in(Student::getClassId, classIds));
    }

    /**
     * 根据专业代码查询是否存在学生
     *
     * @param majorCode
     * @return
     */
    @Override
    public boolean getStuByMajorCode(String majorCode) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .eq(Student::getMajorCode, majorCode));
    }

    /**
     * 根据专业代码集合查询是否存在学生
     *
     * @param codes
     * @return
     */
    @Override
    public boolean getStuByMajorCodes(List<String> codes) {
        return studentMapper.exists(Wrappers.<Student>lambdaQuery()
                .in(Student::getMajorCode, codes));
    }

    /**
     * 删除已退宿学生
     *
     * @return
     */
    @Override
    public void removeStuByStatusIsZero() {
        int count = studentMapper.delete(Wrappers.<Student>lambdaQuery().eq(Student::getStatus, 0));//1在住 0已退宿
        log.info("删除 {} 名已退宿学生", count);
    }

    /**
     * 根据宿舍Id获取最大床位号和该宿舍学生人数
     *
     * @param roomId 宿舍ID
     * @return 包含 "maxBed" 和 "stuCount" 的Map
     */
    @Override
    public Map<String, Object> getMaxBedAndStuCountByRoomId(Long roomId) {
        if (roomId == null) {
            return Map.of("maxBed", 0, "stuCount", 0);
        }

        Map<String, Object> stats = studentMapper.getMaxBedAndStuCountByRoomId(roomId);

        // 处理 max_bed：可能为 null（没人时）
        Object maxBedObj = stats.get("max_bed");
        Integer maxBed = (maxBedObj instanceof Number)
                ? ((Number) maxBedObj).intValue()
                : 0;

        // 处理 stu_count：通常是 Long，转为 int
        Object stuCountObj = stats.get("stu_count");
        Integer stuCount = (stuCountObj instanceof Number)
                ? ((Number) stuCountObj).intValue()
                : 0;

        // 返回结果（使用不可变 Map）
        return Map.of("maxBed", maxBed, "stuCount", stuCount);
    }


}
