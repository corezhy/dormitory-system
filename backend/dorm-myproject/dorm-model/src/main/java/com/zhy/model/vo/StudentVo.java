package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学生查询Vo
 */

@Data
@ApiModel(description = "学生查询Vo")
public class StudentVo {

    @ApiModelProperty("学生Id")
    private Long id;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String studentName;

    @ApiModelProperty("性别")
    private Byte gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态")
    private Byte status;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("楼层号")
    private Integer floorNumber;

    @ApiModelProperty("宿舍编号")
    private String fullCode;

    @ApiModelProperty("床位号")
    private Integer bedNo;

    @ApiModelProperty("违纪扣分")
    private Integer violationScore;


}
