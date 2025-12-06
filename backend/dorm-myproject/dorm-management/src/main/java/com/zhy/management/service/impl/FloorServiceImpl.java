package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.common.result.Result;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.FloorMapper;
import com.zhy.management.service.BuildingService;
import com.zhy.management.service.DormRoomService;
import com.zhy.management.service.FloorService;
import com.zhy.model.dto.AddAndEditFloorDto;
import com.zhy.model.dto.FloorDto;
import com.zhy.model.entity.Building;
import com.zhy.model.entity.Floor;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.FloorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 专业管理实现类
 */
@Service
@Slf4j
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private DormRoomService dormRoomService;

    @Autowired
    private BuildingService buildingService;

    /**
     * 根据条件查询楼层
     *
     * @param floorDto
     * @return
     */
    @Override
    public Page<FloorVo> conditionQueryFloor(FloorDto floorDto) {
        //分页条件
        Page<FloorVo> page = Page.of(floorDto.getPageNum(), floorDto.getPageSize());
        //调用mapper方法多表查询
        return floorMapper.conditionQueryFloor(page, floorDto);
    }

    /**
     * 新增楼层
     *
     * @param addAndEditFloorDto
     * @return
     */
    @Override
    @Transactional
    public void insert(AddAndEditFloorDto addAndEditFloorDto) {
        //查询楼栋
        Building building = buildingService.getById(addAndEditFloorDto.getBuildingId());
        if (building == null) throw new BusinessException("楼栋不存在");
        //1. 根据楼栋id查询当前楼最高楼层数
        Integer floorNumberMax = floorMapper.selectFloorNumberMaxByBuilding(addAndEditFloorDto.getBuildingId());
        int newFloorNumber = (floorNumberMax == null ? 0 : floorNumberMax) + 1;
        //2. 创建楼层对象，添加值
        Floor floor = new Floor();
        // 属性拷贝
        BeanUtils.copyProperties(addAndEditFloorDto, floor);
        // 楼层号，创建时间，修改时间赋值
        floor.setFloorNumber(newFloorNumber);
        floor.setCreateTime(LocalDateTime.now());
        floor.setUpdateTime(LocalDateTime.now());
        //3. 新增楼层
        boolean save = save(floor);
        if (!save) {
            throw new BusinessException("新增楼层失败");
        }
        //4. 新增楼层时，批量添加传递的宿舍数
        Long buildingId = building.getId();
        String buildingCode = building.getCode();
        Long floorId = floor.getId();
        Integer totalRooms = addAndEditFloorDto.getTotalRooms();
        dormRoomService.insertBatchByBuildingIdAndFloorId(buildingId, floorId, totalRooms, buildingCode, newFloorNumber);
    }

    /**
     * 根据id查询回显
     *
     * @param id
     * @return
     */
    @Override
    public Floor selectById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择要查询的楼层");
        }
        //查询
        Floor floor = getById(id);
        if (floor == null) {
            throw new BusinessException("查询不到该楼层");
        }
        // 如果说majorCode等于null 将majorCode改为00000(表示不限专业)
        if (floor.getMajorCode() == null) {
            floor.setMajorCode("00000");
        }
        return floor;
    }

    /**
     * 根据id修改楼层
     *
     * @param id
     * @param addAndEditFloorDto
     * @return
     */
    @Override
    public void updateById(Long id, AddAndEditFloorDto addAndEditFloorDto) {
        if (id == null) {
            throw new BusinessException("请选择要修改的楼层");
        }
        //创建floor对象
        Floor floor = new Floor();
        //属性拷贝
        BeanUtils.copyProperties(addAndEditFloorDto, floor);
        //属性赋值
        floor.setId(id);
        floor.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = updateById(floor);
        if (!b) {
            throw new BusinessException("修改楼层失败");
        }
    }

    /**
     * 根据id删除楼层
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new BusinessException("请选择你要删除的楼层");
        }
        Floor floor = getById(id);
        if (floor == null) {
            throw new BusinessException("没有该楼层");
        }
        //查询楼层下是否有宿舍，如果有，不允许删除
        boolean exist = dormRoomService.hasDormRoomByFloorId(id);
        if (exist) {
            throw new BusinessException("该楼层下还有宿舍，不允许删除");
        }
        //删除楼层
        //只允许删除最高楼层
        //获取当前想要删除的楼层
        //拿到它的buildingId(楼栋Id)
        Long buildingId = floor.getBuildingId();
        //根据楼栋Id查询最高楼层数
        Integer floorMaxNumber = floorMapper.selectFloorNumberMaxByBuilding(buildingId);
        if (floorMaxNumber == null) {
            // 理论上不该发生，但防御性编程
            throw new BusinessException("数据异常：该楼栋无有效楼层记录");
        }
        //判断，如果删除的楼层不是最高层，不允许删除
        if (!floor.getFloorNumber().equals(floorMaxNumber)){
            throw new BusinessException("删除失败，只能删除该栋最高楼层");
        }
        //删除
        boolean b = removeById(id);
        if (!b) {
            throw new BusinessException("删除失败，请刷新后重试");
        }
    }


    /**
     * 根据传递楼层数批量新增楼层
     * @param buildingId
     * @param floorNumber
     * @return
     */
    @Override
    @Transactional
    public Boolean saveBatchFloor(Long buildingId, Integer floorNumber) {
        //创建集合存储批量新增楼层数据
        List<Floor> floorList = new ArrayList<>();
        //遍历传递的楼层数添加
        for (int i = 1; i <= floorNumber; i++) {
            Floor floor = new Floor();
            floor.setBuildingId(buildingId);
            floor.setFloorNumber(i);
            //专业代码默认不限（null）
            floor.setMajorCode(null);
            //本层宿舍总数默认为30
            floor.setTotalRooms(30);
            floor.setCreateTime(LocalDateTime.now());
            floor.setUpdateTime(LocalDateTime.now());
            floorList.add(floor);
        }
        //批量新增楼层
        return saveBatch(floorList);
    }

    /**
     * 根据楼栋Id获取该栋楼层数
     *
     * @param buildingId
     * @return
     */
    @Override
    public List<Floor> getFloorNumberByBuildingId(Long buildingId) {
        if (buildingId == null) {
            throw new BusinessException("请选择楼栋后再选择楼层查询");
        }
        return list(Wrappers.<Floor>lambdaQuery().eq(Floor::getBuildingId, buildingId));
    }

    /**
     * 判断该楼栋下面是否还有楼层，如果有不允许删除
     *
     * @param buildingId
     */
    @Override
    public boolean hasFloorByBuildingId(Long buildingId) {
        if (buildingId == null) return false;
        return floorMapper.exists(Wrappers.<Floor>lambdaQuery()
                .eq(Floor::getBuildingId, buildingId));
    }


}
