package com.zhy.management.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhy.management.exception.BusinessException;
import com.zhy.management.mapper.DormReportMapper;
import com.zhy.management.service.DormReportService;
import com.zhy.model.vo.DormReportCountVo;
import com.zhy.model.vo.DormStopReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 宿舍资源统计service实现类
 */
@Service
public class DormReportServiceImpl implements DormReportService {

    @Autowired
    private DormReportMapper dormReportMapper;

    /**
     * 统计各楼栋宿舍数量和床位总数
     *
     * @return
     */
    @Override
    public List<DormReportCountVo> statisticsDormAndBedCount() {
        List<DormReportCountVo> voList = dormReportMapper.statisticsDormAndBedCount();
        if (CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计各楼栋宿舍数量和床位总数失败");
        }
        return voList;
    }

    /**
     * 停用/维修宿舍分布
     *
     * @return
     */
    @Override
    public List<DormStopReportVo> statisticStopDorm() {
        List<DormStopReportVo> voList = dormReportMapper.statisticStopDorm();
        if (CollectionUtils.isEmpty(voList)) {
            throw new BusinessException("统计停用/维修宿舍失败");
        }
        return voList;
    }


}
