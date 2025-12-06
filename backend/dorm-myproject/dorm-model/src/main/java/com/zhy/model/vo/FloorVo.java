package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询楼层返回VO
 */
@Data
@ApiModel(description = "查询楼层返回VO")
public class FloorVo {

    /**
     * 楼层主键id
     */
    @ApiModelProperty(value = "楼层主键id")
    private Long id;

    /**
     * 楼层号
     */
    @ApiModelProperty(value = "楼层号")
    private Integer floorNumber;

    /**
     * 所属楼栋名称
     */
    @ApiModelProperty(value = "所属楼栋名称")
    private String buildingName;

    /**
     * 专业名称
     */
    @ApiModelProperty(value = "专业名称")
    private String majorName;

    /**
     * 性别类型 0男 1女
     */
    @ApiModelProperty(value = "性别类型")
    private Integer genderType;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime updateTime;
}
