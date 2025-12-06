package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.StuReportMapper;
import com.zhy.management.service.StuReportService;
import com.zhy.management.service.StudentService;
import com.zhy.model.entity.Student;
import com.zhy.model.vo.BedStatisticsVO;
import com.zhy.model.vo.StuReportBuildingVo;
import com.zhy.model.vo.StuReportGenderVo;
import com.zhy.model.vo.StuReportMajorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生报表统计service实现类
 */
@Service
public class StuReportServiceImpl extends ServiceImpl<StuReportMapper, Student> implements StuReportService {

    @Autowired
    private StuReportMapper stuReportMapper;

    /**
     * 学生性别统计
     *
     * @return
     */
    @Override
    public StuReportGenderVo statisticsStuGenderCount() {
        return stuReportMapper.statisticsStuGenderCount();
    }

    /**
     * 学生专业统计
     *
     * @return
     */
    @Override
    public List<StuReportMajorVo> statisticsStuMajorCount() {
        List<StuReportMajorVo> voList = stuReportMapper.statisticsStuMajorCount();
        if (CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计学生专业失败");
        }
        return voList;
    }

    /**
     * 按照楼栋统计学生
     *
     * @return
     */
    @Override
    public List<StuReportBuildingVo> statisticsStuBuildingCount() {
        List<StuReportBuildingVo> voList = stuReportMapper.statisticsStuBuildingCount();
        if(CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计楼栋下学生失败");
        }
        return voList;
    }

    /**
     * 统计全校床铺占用率
     *
     * @return
     */
    @Override
    public Double statisticsBedsOccupancyRate() {
        BedStatisticsVO bedStatistics = stuReportMapper.statisticsBedsOccupancyRate();
        Integer totalBeds = bedStatistics.getTotalBeds(); //总床位数
        Integer occupiedBeds = bedStatistics.getOccupiedBeds(); //占用床位数
        if (totalBeds == null || occupiedBeds == null) {
            throw new BusinessException("统计床铺失败");
        }

        // 处理除零错误
        if (totalBeds == 0) {
            return 0.0;
        }

        //计算占用率
        //转换成double再计算
        double occupancyRate = occupiedBeds * 100.0 / totalBeds;

        // 四舍五入保留1位小数
        return Math.round(occupancyRate * 10.0) / 10.0;
    }
}
