package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.common.result.Result;
import com.zhy.model.dto.AddAndEditBuildingDto;
import com.zhy.model.entity.Building;

import java.util.List;

/**
 * 楼栋管理service
 */
public interface BuildingService extends IService<Building> {

    /**
     * 查询楼栋
     * @return
     */
    public List<Building> select();

    /**
     * 根据id查询回显
     * @param id
     * @return
     */
    public Building selectById(Long id);

    /**
     * 新增楼栋
     * @param addAndEditBuildingDto
     * @return
     */
    public void insert(AddAndEditBuildingDto addAndEditBuildingDto);

    /**
     * 根据id修改楼栋
     * @param id
     * @param addAndEditBuildingDto
     * @return
     */
    public void updateById(Long id,AddAndEditBuildingDto addAndEditBuildingDto);

    /**
     * 启用停用设置
     * @param id
     * @param status
     * @return
     */
    public void updateStatusById(Long id,Integer status);

    /**
     * 删除楼栋
     * @param id
     * @return
     */
    public void deleteById(Long id);

    /**
     * 查询所有启用楼栋
     * @return
     */
    public List<Building> selectStatusIsEnabled();
}
