package com.zhy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 宿舍表实体类
 */
@Data
@ApiModel(description = "宿舍信息")
@TableName("dorm_room")
public class DormRoom {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "宿舍ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "所属楼栋ID", example = "1")
    private Long buildingId;

    @ApiModelProperty(value = "所属楼层ID", example = "1")
    private Long floorId;

    @ApiModelProperty(value = "房间号", example = "101")
    private Integer roomNumber;

    @ApiModelProperty(value = "完整编号，如\"1-1-01\"", example = "1-1-01")
    private String fullCode;

    @ApiModelProperty(value = "总床位数", example = "6")
    private Integer capacity = 6;

    @ApiModelProperty(value = "空闲床铺数（0~6）", example = "4")
    private Integer availableBeds = 6;

    @ApiModelProperty(value = "状态：1=正常，0=停用/维修", allowableValues = "0,1", example = "1")
    private Byte status = 1;

    @ApiModelProperty(value = "创建时间（由应用层设置）")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后更新时间（由应用层设置）")
    private LocalDateTime updateTime;
}