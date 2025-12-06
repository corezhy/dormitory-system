package com.zhy.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 楼层表实体类
 */
@Data
@Accessors(chain = true)
@TableName("floor")
@ApiModel(description = "楼层信息")
public class Floor {

    /**
     * 楼层ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "楼层ID", example = "1")
    private Long id;

    /**
     * 所属楼栋ID（逻辑外键 → building.id）
     */
    @ApiModelProperty(value = "所属楼栋ID", required = true, example = "1")
    private Long buildingId;

    /**
     * 楼层号
     */
    @ApiModelProperty(value = "楼层号", required = true, example = "3")
    private Integer floorNumber;

    /**
     * 限定专业代码（逻辑外键 → major.code），NULL表示不限
     */
    @ApiModelProperty(value = "限定专业代码（null 表示不限）", example = "080901")
    private String majorCode;

    /**
     * 本层宿舍总数
     */
    @ApiModelProperty(value = "本层宿舍总数", required = true, example = "20")
    private Integer totalRooms;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;
}