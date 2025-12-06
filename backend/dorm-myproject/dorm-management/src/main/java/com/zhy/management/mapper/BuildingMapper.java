package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.entity.Building;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼栋管理mapper层接口
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building> {

}
