package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 楼栋表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "楼栋信息")
@TableName("building")
public class Building {

    @ApiModelProperty(value = "楼栋ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "楼栋名称", required = true, example = "思远楼")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "楼栋编码（如B01）", required = true, example = "B01")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "性别类型：0=男，1=女", required = true, example = "0")
    @TableField("gender_type")
    private Integer genderType;

    @ApiModelProperty(value = "状态：1=启用，0=停用", required = true, example = "1")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "描述", example = "主要容纳计算机学院男生")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "创建时间", required = true)
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    @TableField("update_time")
    private LocalDateTime updateTime;
}