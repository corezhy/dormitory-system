package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.management.param.RoomUpdate;
import com.zhy.model.dto.AddAndEditDormRoomDto;
import com.zhy.model.dto.DormRoomDto;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.vo.DormRoomVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 宿舍管理service接口
 */
public interface DormRoomService extends IService<DormRoom> {

    /**
     * 条件查询宿舍
     * @param dormRoomDto
     * @return
     */
    public Page<DormRoomVo> conditionQueryDormRoom(DormRoomDto dormRoomDto);

    /**
     * 新增宿舍
     * @param addAndEditDormRoomDto
     * @return
     */
    public void insertDormRoom(AddAndEditDormRoomDto addAndEditDormRoomDto);

    /**
     * 根据id查询宿舍
     * @param id
     * @return
     */
    public DormRoom getDormRoomById(Long id);

    /**
     * 根据id修改宿舍
     * @param id
     * @param dto
     * @return
     */
    public void updateById(Long id, AddAndEditDormRoomDto dto);

    /**
     * 启用停用宿舍
     * @param id
     * @param status
     * @return
     */
    public void startOrStop(Long id, Byte status);

    /**
     * 批量删除宿舍
     * @param ids
     * @return
     */
    public void deleteBatch(List<Integer> ids);

    /**
     * 根据楼层Id查询是否有宿舍
     * @param floorId
     * @return
     */
    boolean hasDormRoomByFloorId(Long floorId);

    /**
     * 批量新增宿舍
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<DormRoom> list);

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
    boolean insertBatchByBuildingIdAndFloorId(Long buildingId, Long floorId, Integer totalRooms, String buildingCode, int newFloorNumber);

    /**
     * 根据宿舍完整编号查询宿舍Id（同时查出 buildingId 用于性别校验）
     * @param fullCode
     * @return
     */
    DormRoom getIdAndBuildingIdByFullCode(String fullCode);

    /**
     * 根据Id原子减少空闲床位（高并发安全）
     * @param dormRoomId
     * @return
     */
    int reduceAvailableBedsById(Long dormRoomId);

    /**
     * 根据宿舍Id查询宿舍编号
     * @param roomId
     * @return
     */
    String getFullCodeById(Long roomId);

    /**
     * 根据宿舍编号，楼栋Id，楼层Id查询宿舍
     * @param fullCode
     * @param buildingId
     * @param floorId
     * @return
     */
    DormRoom getDormRoomByFullCodeAndBuildingIdAndFloorId(String fullCode, Long buildingId, Long floorId);

    /**
     * 根据Id释放床位(+1) —— 仅当原宿舍存在 不需要检查返回值，DB 约束已兜底
     * @param roomId
     */
    void releaseAvailableBedsById(Long roomId);

    /**
     * 通过宿舍Id批量将宿舍空闲床铺 + 1（宿舍相同也可以多次 + 1，避免只加一次）
     * @param roomUpdates
     * @return
     */
    int updateAvailableBedsByCounts(@Param("roomUpdates") List<RoomUpdate> roomUpdates);
}
