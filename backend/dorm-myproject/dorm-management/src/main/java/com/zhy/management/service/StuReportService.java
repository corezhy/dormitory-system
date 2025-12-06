package com.zhy.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.StuReportBuildingVo;
import com.zhy.model.vo.StuReportGenderVo;
import com.zhy.model.vo.StuReportMajorVo;

import java.util.List;

/**
 * 学生报表统计service接口
 */

public interface StuReportService extends IService<Student> {

    /**
     * 学生性别统计
     * @return
     */
    StuReportGenderVo statisticsStuGenderCount();

    /**
     * 学生专业统计
     * @return
     */
    List<StuReportMajorVo> statisticsStuMajorCount();


    /**
     * 按照楼栋统计学生
     * @return
     */
    List<StuReportBuildingVo> statisticsStuBuildingCount();

    /**
     * 统计全校床铺占用率
     * @return
     */
    Double statisticsBedsOccupancyRate();
}
