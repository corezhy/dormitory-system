package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.BuildingMapper;
import com.zhy.management.mapper.DormRoomMapper;
import com.zhy.management.mapper.StudentMapper;
import com.zhy.management.service.BuildingService;
import com.zhy.management.service.DormRoomService;
import com.zhy.management.service.FloorService;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditBuildingDto;
import com.zhy.model.entity.Building;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.entity.Floor;
import com.zhy.model.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 楼栋管理service实现类
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Autowired
    private FloorService floorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DormRoomService dormRoomService;

    /**
     * 查询楼栋
     *
     * @return
     */
    @Override
    public List<Building> select() {
        List<Building> buildings = list();
        if (CollectionUtils.isEmpty(buildings)) {
            throw new BusinessException("查询楼栋失败");
        }
        return buildings;
    }

    /**
     * 根据id查询回显
     *
     * @param id
     * @return
     */
    @Override
    public Building selectById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择要查询的楼栋");
        }
        Building building = getById(id);
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }
        return building;
    }

    /**
     * 新增楼栋
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public void insert(AddAndEditBuildingDto dto) {
        // ====== ✅ 新增：业务规则校验 ======
        boolean isCustom = dto.getFloorConfigs() != null && !dto.getFloorConfigs().isEmpty();
        boolean isUnified = dto.getRoomsPerFloor() != null;

        if (!isCustom && !isUnified) {
            throw new BusinessException("请提供统一宿舍数（roomsPerFloor）或自定义楼层配置（floorConfigs）");
        }

        if (isCustom && isUnified) {
            throw new BusinessException("统一模式与自定义模式不能同时使用，请只传其一");
        }

        if (isUnified) {
            if (dto.getRoomsPerFloor() <= 0 || dto.getRoomsPerFloor() > 50) {
                throw new BusinessException("每层宿舍数必须为1-50之间的整数");
            }
        }

        if (isCustom) {
            if (dto.getFloorNum() == null) {
                throw new BusinessException("楼层数（floorNum）不能为空");
            }
            if (dto.getFloorConfigs().size() != dto.getFloorNum()) {
                throw new BusinessException("自定义楼层配置数量必须等于楼层数");
            }
            for (AddAndEditBuildingDto.FloorConfig cfg : dto.getFloorConfigs()) {
                if (cfg.getFloorNumber() < 1 || cfg.getFloorNumber() > dto.getFloorNum()) {
                    throw new BusinessException("楼层号必须在 1 到 " + dto.getFloorNum() + " 之间");
                }
                if (cfg.getRoomCount() < 0 || cfg.getRoomCount() > 50) {
                    throw new BusinessException("宿舍数量必须为 0-50 之间的整数");
                }
            }
        }

        LocalDateTime now = LocalDateTime.now();

        // 1. 保存 Building
        Building building = new Building();
        BeanUtils.copyProperties(dto, building);
        building.setStatus(1);
        building.setCreateTime(now);
        building.setUpdateTime(now);

        if (!save(building)) {
            throw new BusinessException("新增楼栋失败");
        }
        Long buildingId = building.getId();

        List<Floor> floorsToSave = new ArrayList<>();
        List<DormRoom> roomsToSave = new ArrayList<>();

        if (dto.getFloorConfigs() != null && !dto.getFloorConfigs().isEmpty()) {
            // 自定义模式
            for (AddAndEditBuildingDto.FloorConfig config : dto.getFloorConfigs()) {
                Floor floor = new Floor();
                floor.setBuildingId(buildingId);
                floor.setFloorNumber(config.getFloorNumber());
                floor.setTotalRooms(config.getRoomCount()); // 注意：实体是 totalRooms
                floor.setCreateTime(now);
                floor.setUpdateTime(now);
                floorsToSave.add(floor);
            }
        } else {
            // 统一模式
            int floorNum = dto.getFloorNum();
            int roomsPerFloor = dto.getRoomsPerFloor();
            for (int i = 1; i <= floorNum; i++) {
                Floor floor = new Floor();
                floor.setBuildingId(buildingId);
                floor.setFloorNumber(i);
                floor.setTotalRooms(roomsPerFloor);
                floor.setCreateTime(now);
                floor.setUpdateTime(now);
                floorsToSave.add(floor);
            }
        }

        // 2. 批量保存 Floor（必须先保存，才能获取 floor.id）
        if (!floorsToSave.isEmpty()) {
            boolean floorSaved = floorService.saveBatch(floorsToSave);
            if (!floorSaved) {
                throw new BusinessException("批量保存楼层失败");
            }
        }

        // 3. 查询刚插入的 Floors（按 buildingId + floorNumber 映射 id）
        List<Floor> savedFloors = floorService.list(
                new LambdaQueryWrapper<Floor>().eq(Floor::getBuildingId, buildingId)
        );

        // 构建 floorNumber -> Floor 对象的映射
        Map<Integer, Floor> floorMap = savedFloors.stream()
                .collect(Collectors.toMap(Floor::getFloorNumber, f -> f));

        // 4. 生成宿舍（必须用 floor.id 作为 floorId）
        if (dto.getFloorConfigs() != null && !dto.getFloorConfigs().isEmpty()) {
            for (AddAndEditBuildingDto.FloorConfig config : dto.getFloorConfigs()) {
                Floor floor = floorMap.get(config.getFloorNumber());
                if (floor == null) continue; // 理论不会发生

                for (int i = 1; i <= config.getRoomCount(); i++) {
                    DormRoom room = new DormRoom();
                    room.setBuildingId(buildingId);
                    room.setFloorId(floor.getId()); // 👈 关键：使用 floor.id
                    room.setRoomNumber(i);
                    String fullCode = String.format("%s-%d-%02d",
                            building.getCode(),           // ← 关键修改：用 code 代替 id
                            floor.getFloorNumber(),
                            i);
                    room.setFullCode(fullCode);
                    room.setCapacity(6);           // 默认容量
                    room.setAvailableBeds(6);      // 初始全空
                    room.setStatus((byte) 1);      // 正常
                    room.setCreateTime(now);
                    room.setUpdateTime(now);
                    roomsToSave.add(room);
                }
            }
        } else {
            int floorNum = dto.getFloorNum();
            int roomsPerFloor = dto.getRoomsPerFloor();
            for (int floorNo = 1; floorNo <= floorNum; floorNo++) {
                Floor floor = floorMap.get(floorNo);
                if (floor == null) continue;

                for (int i = 1; i <= roomsPerFloor; i++) {
                    DormRoom room = new DormRoom();
                    room.setBuildingId(buildingId);
                    room.setFloorId(floor.getId());
                    room.setRoomNumber(i);
                    String fullCode = String.format("%s-%d-%02d", building.getCode(), floorNo, i);
                    room.setFullCode(fullCode);
                    room.setCapacity(6);
                    room.setAvailableBeds(6);
                    room.setStatus((byte) 1);
                    room.setCreateTime(now);
                    room.setUpdateTime(now);
                    roomsToSave.add(room);
                }
            }
        }

        // 5. 批量保存宿舍
        if (!roomsToSave.isEmpty()) {
            int inserted = dormRoomService.insertBatch(roomsToSave);
            if (inserted != roomsToSave.size()) {
                throw new BusinessException("批量保存宿舍失败");
            }
        }
    }

    /**
     * 根据id修改楼栋
     *
     * @param id
     * @param addAndEditBuildingDto
     * @return
     */
    @Override
    public void updateById(Long id, AddAndEditBuildingDto addAndEditBuildingDto) {
        if (id == null) {
            throw new BusinessException("请选择要修改的楼栋");
        }
        //创建Building对象
        Building building = new Building();
        building.setId(id);
        //属性拷贝
        BeanUtils.copyProperties(addAndEditBuildingDto, building);
        //修改时间
        building.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(building);
        if (!b) {
            throw new BusinessException("修改楼栋失败");
        }
    }

    /**
     * 启用停用设置
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public void updateStatusById(Long id, Integer status) {
        if (id == null || status == null) {
            throw new BusinessException("请选择要设置的楼栋");
        }
        //参数校验
        if (status != 0 && status != 1) {
            throw new BusinessException("只能设置启用或者停用");
        }
        if (status == 0) { //0停用 1 启用
            //如果是停用，查询该楼栋下是否有学生，如果有，不允许停用
            boolean exists = studentService.hasStuByBuildingId(id);
            if (exists) {
                throw new BusinessException("该楼栋下还有学生入住，无法停用");
            }
        }
        //创建Building对象
        Building building = new Building();
        building.setId(id);
        building.setStatus(status);
        building.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(building);

        if (!b) {
            throw new BusinessException("设置状态失败");
        }
    }

    /**
     * 删除楼栋
     *
     * @param id
     * @return
     */
    @Override
    public void deleteById(Long id) {
        //参数校验
        if (id == null) {
            throw new BusinessException("请选择要删除的楼栋");
        }
        //1. 判断该楼栋下面是否还有楼层，如果有不允许删除
        boolean whetherExistsFloor = floorService.hasFloorByBuildingId(id);
        if (whetherExistsFloor) {
            throw new BusinessException("请先删除该楼栋下的所有楼层");
        }
        //删除楼栋
        boolean b = removeById(id);
        if (!b) {
            throw new BusinessException("删除楼栋失败");
        }
    }

    /**
     * 查询所有启用楼栋
     *
     * @return
     */
    @Override
    public List<Building> selectStatusIsEnabled() {
        //1启用 0停用
        List<Building> buildings = list(Wrappers.<Building>lambdaQuery().eq(Building::getStatus, 1));
        if (CollectionUtils.isEmpty(buildings)) {
            throw new BusinessException("查询启用楼栋失败");
        }
        return buildings;
    }


}
