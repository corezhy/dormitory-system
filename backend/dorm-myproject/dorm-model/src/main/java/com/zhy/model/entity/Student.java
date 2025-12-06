package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生表
 */
@Data
@TableName("student")
@ApiModel(description = "学生信息")
public class Student {

    /**
     * 学生ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "学生ID", example = "1")
    private Long id;

    /**
     * 学号
     */
    @TableField("student_no")
    @ApiModelProperty(value = "学号", required = true, example = "202308090101")
    private String studentNo;

    /**
     * 姓名
     */
    @TableField("name")
    @ApiModelProperty(value = "姓名", required = true, example = "张三")
    private String name;

    /**
     * 性别：0=男，1=女
     */
    @TableField("gender")
    @ApiModelProperty(value = "性别：0=男，1=女", allowableValues = "0,1", example = "0")
    private Byte gender;

    /**
     * 专业代码（逻辑外键 → major.code）
     */
    @TableField("major_code")
    @ApiModelProperty(value = "专业代码", required = true, example = "080901")
    private String majorCode;

    /**
     * 班级ID（逻辑外键 → class.id）
     */
    @TableField("class_id")
    @ApiModelProperty(value = "班级ID", required = true, example = "1")
    private Long classId;

    /**
     * 手机号
     */
    @TableField("phone")
    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    /**
     * 违纪扣分（默认0）
     */
    @TableField("violation_score")
    @ApiModelProperty(value = "违纪扣分", example = "0")
    private Integer violationScore;

    /**
     * 所在楼栋ID（逻辑外键）
     */
    @TableField("building_id")
    @ApiModelProperty(value = "所在楼栋ID", required = true, example = "1")
    private Long buildingId;

    /**
     * 所在楼层ID（逻辑外键）
     */
    @TableField("floor_id")
    @ApiModelProperty(value = "所在楼层ID", required = true, example = "1")
    private Long floorId;

    /**
     * 所在宿舍ID（逻辑外键）
     */
    @TableField("room_id")
    @ApiModelProperty(value = "所在宿舍ID", required = true, example = "1")
    private Long roomId;

    /**
     * 床位号（1~6）
     */
    @TableField("bed_no")
    @ApiModelProperty(value = "床位号（1~6）", allowableValues = "range[1,6]", example = "1")
    private Integer bedNo;

    /**
     * 入住时间
     */
    @TableField("check_in_time")
    @ApiModelProperty(value = "入住时间", example = "2023-09-01T08:00:00")
    private LocalDateTime checkInTime;

    /**
     * 状态：1=在住，0=已退宿
     */
    @TableField("status")
    @ApiModelProperty(value = "状态：1=在住，0=已退宿", allowableValues = "0,1", example = "1")
    private Byte status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间", example = "2023-09-01T08:00:00")
    private LocalDateTime createTime;

    /**
     * 最后更新时间（由应用层设置）
     */
    @TableField("update_time")
    @ApiModelProperty(value = "最后更新时间", example = "2023-09-01T08:00:00")
    private LocalDateTime updateTime;
}