package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.DormRoomMapper;
import com.zhy.management.param.RoomUpdate;
import com.zhy.management.service.BuildingService;
import com.zhy.management.service.DormRoomService;
import com.zhy.management.service.FloorService;
import com.zhy.management.service.StudentService;
import com.zhy.model.dto.AddAndEditDormRoomDto;
import com.zhy.model.dto.DormRoomDto;
import com.zhy.model.entity.Building;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.entity.Floor;
import com.zhy.model.vo.DormRoomVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * 宿舍管理service实现类
 */
@Service
public class DormRoomServiceImpl extends ServiceImpl<DormRoomMapper, DormRoom> implements DormRoomService {

    @Autowired
    private DormRoomMapper dormRoomMapper;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private StudentService studentService;


    /**
     * 条件查询宿舍
     *
     * @param dormRoomDto
     * @return
     */
    @Override
    public Page<DormRoomVo> conditionQueryDormRoom(DormRoomDto dormRoomDto) {
        //分页条件
        Page<DormRoomVo> page = Page.of(dormRoomDto.getPageNum(), dormRoomDto.getPageSize());
        //查询结果
        return dormRoomMapper.conditionQueryDormRoom(page, dormRoomDto);
    }

    /**
     * 新增宿舍
     *
     * @param dto
     * @return
     */
    @Transactional
    @Override
    public void insertDormRoom(AddAndEditDormRoomDto dto) {
        //1. 根据buildingId查询楼栋，获取编码
        Building building = buildingService.getById(dto.getBuildingId());
        if (building == null) {
            throw new BusinessException("楼栋不存在");
        }
        String code = building.getCode();
        //2. 根据楼栋id和楼层id获取本层最高房间号
        Integer roomNumberMax = dormRoomMapper.getMaxRoomNumberByBuildingIdAndFloorId(dto.getBuildingId(), dto.getFloorId());
        roomNumberMax = (roomNumberMax == null ? 0 : roomNumberMax) + 1;
        //3. 获取楼层数
        Floor floor = floorService.getById(dto.getFloorId());
        if (floor == null) {
            throw new BusinessException("楼层不存在");
        }
        Integer floorNumber = floor.getFloorNumber();
        //4. 创建宿舍对象并添加数据
        DormRoom dormRoom = new DormRoom();
        //4.1 属性拷贝
        BeanUtils.copyProperties(dto, dormRoom);
        dormRoom.setRoomNumber(roomNumberMax); //房间号
        dormRoom.setFullCode(String.format("%s-%d-%02d", code, floorNumber, roomNumberMax));//完整编号: 例 B01-2-02 楼栋编码-楼层数-房间号
        dormRoom.setAvailableBeds(dormRoom.getCapacity()); //默认空闲床铺为总床位数
        dormRoom.setStatus((byte) 1); //默认创建后正常
        dormRoom.setCreateTime(LocalDateTime.now());
        dormRoom.setUpdateTime(LocalDateTime.now());
        //5. 新增
        boolean save = save(dormRoom);
        if (!save) {
            throw new BusinessException("新增宿舍失败");
        }
    }

    /**
     * 根据id查询宿舍
     *
     * @param id
     * @return
     */
    @Override
    public DormRoom getDormRoomById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择要查询的宿舍");
        }
        //查询
        DormRoom dormRoom = getById(id);
        if (dormRoom == null) {
            throw new BusinessException("查询回显失败");
        }
        return dormRoom;
    }

    /**
     * 根据id修改宿舍
     *
     * @param id
     * @param dto
     * @return
     */
    @Override
    public void updateById(Long id, AddAndEditDormRoomDto dto) {
        if (id == null) {
            throw new BusinessException("请选择要修改的宿舍");
        }
        //根据id查询原宿舍
        DormRoom dormRoom = getById(id);
        if (dormRoom == null) {
            throw new BusinessException("宿舍不存在");
        }
        //获取宿舍总床位数
        Integer oldCapacity = dormRoom.getCapacity();
        //获取修改宿舍时传递的总床位数
        Integer newCapacity = dto.getCapacity();
        if (newCapacity == null || newCapacity <= 0) {
            throw new BusinessException("床位数必须大于0");
        }

        //根据宿舍Id获取最大床位数和该宿舍学生人数
        Map<String, Object> stats = studentService.getMaxBedAndStuCountByRoomId(id);

        //获取最大已分配床位号（用于缩容校验）
        Integer maxBed = (Integer) stats.get("maxBed");
        //获取当前入住学生人数（用于计算空闲床铺数）
        Integer stuCount = (Integer) stats.get("stuCount");

        //缩容校验
        if (newCapacity < oldCapacity) {
            if (maxBed > newCapacity) {
                throw new BusinessException("床位 [" + maxBed + "] 已被学生占用，无法将床位数减少至 " + newCapacity);
            }
        }

        //重新计算空闲床位数：总床位 - 已入住人数
        int newAvailableBeds = newCapacity - stuCount;
        if (newAvailableBeds < 0) {
            throw new BusinessException("宿舍学生人数超过新设定的床位数");
        }

        //更新宿舍信息
        dormRoom.setCapacity(newCapacity);
        dormRoom.setAvailableBeds(newAvailableBeds);
        dormRoom.setUpdateTime(LocalDateTime.now());
        if (!updateById(dormRoom)) {
            throw new BusinessException("修改宿舍失败");
        }
    }

    /**
     * 启用停用宿舍
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public void startOrStop(Long id, Byte status) {
        if (id == null) {
            throw new BusinessException("请选择要修改状态的宿舍");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new BusinessException("状态值非法，必须为0(停用)或1(启用)");
        }
        //停用宿舍时，查询该宿舍下面是否有学生，如果有，不允许停用
        if (status == 0) {
            boolean exists = studentService.hasStuByDormRoomId(id);
            if (exists) {
                throw new BusinessException("该宿舍下还有学生入住，无法停用");
            }
        }
        //修改
        boolean update = update(Wrappers.<DormRoom>lambdaUpdate()
                .set(DormRoom::getStatus, status)
                .eq(DormRoom::getId, id));
        if (!update) {
            throw new BusinessException("宿舍不存在或状态未变更");
        }
    }

    /**
     * 批量删除宿舍
     *
     * @param ids
     * @return
     */
    @Override
    public void deleteBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("请选择需要删除的宿舍");
        }
        //去重，防止前端误传重复id
        ArrayList<Integer> uniqueIds = new ArrayList<>(new LinkedHashSet<>(ids));
        //查询宿舍下是否存在学生，如果存在，不允许删除
        boolean exist = studentService.hasStuByDormRoomIds(uniqueIds);
        if (exist) {
            throw new BusinessException("所选宿舍中有学生入住，无法删除");
        }
        //删除
        removeBatchByIds(uniqueIds);
    }

    /**
     * 根据楼层Id查询是否有宿舍
     *
     * @param floorId
     * @return
     */
    @Override
    public boolean hasDormRoomByFloorId(Long floorId) {
        return dormRoomMapper.exists(Wrappers.<DormRoom>lambdaQuery()
                .eq(DormRoom::getFloorId, floorId));
    }

    /**
     * 批量新增宿舍
     *
     * @param list
     * @return
     */
    @Override
    public int insertBatch(List<DormRoom> list) {
        return dormRoomMapper.insertBatch(list);
    }

    /**
     * 根据楼栋Id，楼层Id，宿舍总数批量新增宿舍
     *
     * @param buildingId     楼栋Id
     * @param floorId        楼层Id
     * @param totalRooms     宿舍总数
     * @param buildingCode
     * @param newFloorNumber
     * @return
     */
    @Override
    public boolean insertBatchByBuildingIdAndFloorId(Long buildingId, Long floorId, Integer totalRooms, String buildingCode, int newFloorNumber) {
        //创建集合存储宿舍对象
        List<DormRoom> dormRoomList = new ArrayList<>();
        //循环插入宿舍数据
        for (int i = 1; i <= totalRooms; i++) {
            //创建宿舍对象
            DormRoom dormRoom = new DormRoom();
            //参数补全
            dormRoom.setBuildingId(buildingId);
            dormRoom.setFloorId(floorId);
            dormRoom.setRoomNumber(i); //房间号
            dormRoom.setFullCode(String.format("%s-%d-%02d", buildingCode, newFloorNumber, i)); //完整编号，例如 B02-1-02
            dormRoom.setCapacity(6); //总床位数，硬编码6位
            dormRoom.setAvailableBeds(6); //空闲床位数，新增时还没有学生入住，所以也设置6位
            dormRoom.setStatus((byte) 1); //状态，默认正常 1正常 0停用/维修
            dormRoom.setCreateTime(LocalDateTime.now());
            dormRoom.setUpdateTime(LocalDateTime.now());
            //添加到集合中
            dormRoomList.add(dormRoom);
        }
        //批量新增
        boolean b = saveBatch(dormRoomList);
        if (!b) {
            throw new BusinessException("批量新增宿舍失败");
        }
        return true;
    }

    /**
     * 根据宿舍完整编号查询宿舍Id（同时查出 buildingId 用于性别校验）
     *
     * @param fullCode
     * @return
     */
    @Override
    public DormRoom getIdAndBuildingIdByFullCode(String fullCode) {
        DormRoom dormRoom = getOne(Wrappers.<DormRoom>lambdaQuery()
                .select(DormRoom::getId, DormRoom::getBuildingId)
                .eq(DormRoom::getFullCode, fullCode));
        if (dormRoom == null) {
            throw new BusinessException("宿舍编号 [" + fullCode + "] 不存在");
        }
        return dormRoom;
    }

    /**
     * 根据Id原子减少空闲床位（高并发安全）
     *
     * @param dormRoomId
     * @return
     */
    @Override
    public int reduceAvailableBedsById(Long dormRoomId) {
        return dormRoomMapper.update(null,
                Wrappers.<DormRoom>lambdaUpdate()
                        .setSql("available_beds = available_beds - 1")
                        .eq(DormRoom::getId, dormRoomId)
                        .gt(DormRoom::getAvailableBeds, 0) // 必须还有空位
        );
    }

    /**
     * 根据宿舍Id查询宿舍编号
     *
     * @param roomId
     * @return
     */
    @Override
    public String getFullCodeById(Long roomId) {
        DormRoom dormRoom = getOne(Wrappers.<DormRoom>lambdaQuery()
                .select(DormRoom::getFullCode)
                .eq(DormRoom::getId, roomId));
        if (dormRoom == null) {
            throw new BusinessException("查询宿舍编号失败");
        }
        return dormRoom.getFullCode();
    }

    /**
     * 根据宿舍编号，楼栋Id，楼层Id查询宿舍
     *
     * @param fullCode
     * @param buildingId
     * @param floorId
     * @return
     */
    @Override
    public DormRoom getDormRoomByFullCodeAndBuildingIdAndFloorId(String fullCode, Long buildingId, Long floorId) {
        DormRoom dormRoom = getOne(Wrappers.<DormRoom>lambdaQuery()
                .eq(DormRoom::getFullCode, fullCode)
                .eq(DormRoom::getBuildingId, buildingId)
                .eq(DormRoom::getFloorId, floorId));
        if (dormRoom == null) {
            throw new BusinessException("宿舍不存在，请检查楼栋、楼层和房间号");
        }
        return dormRoom;
    }

    /**
     * 根据Id释放床位(+1) —— 仅当原宿舍存在 不需要检查返回值，DB 约束已兜底
     *
     * @param roomId
     */
    @Override
    public void releaseAvailableBedsById(Long roomId) {
        dormRoomMapper.update(null, Wrappers.<DormRoom>lambdaUpdate()
                .setSql("available_beds = available_beds + 1")
                .eq(DormRoom::getId, roomId));
    }

    /**
     * 通过宿舍Id批量将宿舍空闲床铺 + 1（宿舍相同也可以多次 + 1，避免只加一次）
     *
     * @param roomUpdates
     * @return
     */
    @Override
    public int updateAvailableBedsByCounts(List<RoomUpdate> roomUpdates) {
        return dormRoomMapper.updateAvailableBedsByCounts(roomUpdates);
    }


}
