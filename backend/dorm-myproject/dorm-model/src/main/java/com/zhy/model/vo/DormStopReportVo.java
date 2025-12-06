package com.zhy.model.vo;

import lombok.Data;

/**
 * 停用/维修宿舍分布
 */
@Data
public class DormStopReportVo {

    private String name; //楼栋名称

    private Integer floorNumber; //楼层号

    private String fullCode; //房间完整编号

}
