package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditFloorDto;
import com.zhy.model.dto.FloorDto;
import com.zhy.model.entity.Floor;
import com.zhy.model.vo.FloorVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 楼层管理service接口
 */
public interface FloorService extends IService<Floor> {


    /**
     * 根据条件查询楼层
     *
     * @param floorDto
     * @return
     */
    public Page<FloorVo> conditionQueryFloor(FloorDto floorDto);

    /**
     * 新增楼层
     *
     * @param addAndEditFloorDto
     * @return
     */
    public void insert(AddAndEditFloorDto addAndEditFloorDto);

    /**
     * 根据id查询回显
     *
     * @param id
     * @return
     */
    public Floor selectById(Long id);

    /**
     * 根据id修改楼层
     *
     * @param id
     * @param addAndEditFloorDto
     * @return
     */
    public void updateById(Long id, AddAndEditFloorDto addAndEditFloorDto);

    /**
     * 根据id删除楼层
     *
     * @param id
     * @return
     */
    public void deleteById(Long id);

    /**
     * 根据传递楼层数批量新增楼层
     *
     * @param buildingId
     * @param floorNumber
     * @return
     */
    public Boolean saveBatchFloor(Long buildingId, Integer floorNumber);

    /**
     * 根据楼栋Id获取该栋楼层数
     * @param buildingId
     * @return
     */
    public List<Floor> getFloorNumberByBuildingId(Long buildingId);

    /**
     * 判断该楼栋下面是否还有楼层，如果有不允许删除
     */
    public boolean hasFloorByBuildingId(Long buildingId);
}
