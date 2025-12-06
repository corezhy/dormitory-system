package com.zhy.management.service;

import com.zhy.model.vo.DormReportCountVo;
import com.zhy.model.vo.DormStopReportVo;

import java.util.List;

/**
 * 宿舍资源统计service接口
 */
public interface DormReportService {


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
