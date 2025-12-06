package com.zhy.model.vo;

import lombok.Data;

/**
 * 床位统计VO
 */
@Data
public class BedStatisticsVO {

    private Integer totalBeds; //总床位数

    private Integer occupiedBeds; //占用床位数


}
