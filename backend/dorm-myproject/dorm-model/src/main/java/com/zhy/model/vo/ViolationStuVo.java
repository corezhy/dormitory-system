package com.zhy.model.vo;

import lombok.Data;

/**
 * 统计该专业违纪学生次数VO
 */

@Data
public class ViolationStuVo {

    private String majorName; //专业名称

    private Integer violationCount; //违纪次数


}
