package com.zhy.model.vo;

import lombok.Data;

/**
 * 学生性别统计返回VO
 */
@Data
public class StuReportGenderVo {

    private Integer maleCount; //男生数量

    private Integer femaleCount; //女生数量

}
