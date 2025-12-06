package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级查询vo
 */
@Data
@ApiModel(description = "班级查询vo")
public class ClassVo {

    @ApiModelProperty("班级ID")
    private Long id;

    @ApiModelProperty("班级编码")
    private String code;

    @ApiModelProperty("班级名称")
    private String name;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("入学年级")
    private Integer grade;

    @ApiModelProperty("辅导员姓名")
    private String counselor;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime updateTime;


}
