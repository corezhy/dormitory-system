package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.management.param.RoomUpdate;
import com.zhy.model.dto.DormRoomDto;
import com.zhy.model.entity.DormRoom;
import com.zhy.model.vo.DormRoomVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 宿舍管理mapper层接口
 */
@Mapper
public interface DormRoomMapper extends BaseMapper<DormRoom> {

    /**
     * 条件分页查询宿舍
     * @param page
     * @param dormRoomDto
     * @return
     */
    public Page<DormRoomVo> conditionQueryDormRoom(Page<DormRoomVo> page, @Param("dto") DormRoomDto dormRoomDto);

    /**
     * 根据所属楼栋Id和所属楼层ID查出本层最大房间号
     * @param buildingId
     * @param floorId
     * @return
     */
    @Select("select max(room_number) from dorm_room where building_id = #{buildingId} and floor_id = #{floorId}")
    Integer getMaxRoomNumberByBuildingIdAndFloorId(Long buildingId, Long floorId);


    /**
     * 通过宿舍Id批量将宿舍空闲床铺 + 1（宿舍相同也可以多次 + 1，避免只加一次）
     * @param roomUpdates
     * @return
     */
    int updateAvailableBedsByCounts(@Param("roomUpdates") List<RoomUpdate> roomUpdates);

    /**
     * 批量新增宿舍
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<DormRoom> list);

}
