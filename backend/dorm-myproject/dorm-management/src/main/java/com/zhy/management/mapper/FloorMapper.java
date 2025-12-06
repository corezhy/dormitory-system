package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhy.model.dto.FloorDto;
import com.zhy.model.entity.Floor;
import com.zhy.model.vo.FloorVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 楼层管理mapper接口
 */
@Mapper
public interface FloorMapper extends BaseMapper<Floor> {

    /**
     * 根据条件查询楼层
     * @param floorDto
     * @return
     */
    public Page<FloorVo> conditionQueryFloor(Page<FloorVo> page, @Param("floorDto") FloorDto floorDto);

    /**
     * 根据楼栋id查询当前楼最高楼层数
     * @param buildingId
     * @return
     */
    @Select("select max(floor_number) from floor where building_id = #{buildingId}")
    public Integer selectFloorNumberMaxByBuilding(Long buildingId);

}
