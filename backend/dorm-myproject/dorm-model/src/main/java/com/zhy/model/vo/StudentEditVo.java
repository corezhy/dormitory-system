package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学生编辑/详情 Vo（用于新增、修改、回显）
 */
@Data
@ApiModel(description = "学生编辑信息VO")
public class StudentEditVo {

    @ApiModelProperty("学生ID（编辑时必填）")
    private Long id;

    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String name; // 注意：这里叫 name，和数据库/实体一致

    @ApiModelProperty("性别 (0-男, 1-女)")
    private Byte gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("专业代码")
    private String majorCode; // 关键：不是 majorName！

    @ApiModelProperty("班级名称")
    private String className;

    @ApiModelProperty("楼栋ID")
    private Long buildingId; // 关键：不是 buildingName！

    @ApiModelProperty("楼层ID")
    private Long floorId; // 关键：不是 floorNumber！

    @ApiModelProperty("宿舍编号（房间号）")
    private String fullCode;

    @ApiModelProperty("床位号")
    private Integer bedNo;

    @ApiModelProperty("状态 (1-在住, 0-已退宿)")
    private Byte status;
}