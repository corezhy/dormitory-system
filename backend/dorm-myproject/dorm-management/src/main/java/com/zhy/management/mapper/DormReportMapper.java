package com.zhy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.model.vo.DormReportCountVo;
import com.zhy.model.vo.DormStopReportVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 宿舍资源统计mapper层接口
 */
@Mapper
public interface DormReportMapper {


    /**
     * 统计各楼栋宿舍数量和床位总数
     * @return
     */
    List<DormReportCountVo> statisticsDormAndBedCount();

    /**
     * 停用/维修宿舍分布
     * @return
     */
    List<DormStopReportVo> statisticStopDorm();
}
