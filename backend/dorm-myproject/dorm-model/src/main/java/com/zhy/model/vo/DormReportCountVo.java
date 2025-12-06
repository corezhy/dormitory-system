package com.zhy.model.vo;

import lombok.Data;

/**
 * 统计各楼栋宿舍数量和床位总数Vo
 */
@Data
public class DormReportCountVo {

    private String name; //楼栋名称

    private Integer roomCount; //宿舍总数

    private Integer totalBeds; //床位总数

}
