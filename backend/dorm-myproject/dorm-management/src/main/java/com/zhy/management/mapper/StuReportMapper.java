package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.BedStatisticsVO;
import com.zhy.model.vo.StuReportBuildingVo;
import com.zhy.model.vo.StuReportGenderVo;
import com.zhy.model.vo.StuReportMajorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生报表统计mapper接口
 */
@Mapper
public interface StuReportMapper extends BaseMapper<Student> {

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
     * 统计总床位数和占用床位数
     * @return
     */
    BedStatisticsVO statisticsBedsOccupancyRate();


}
