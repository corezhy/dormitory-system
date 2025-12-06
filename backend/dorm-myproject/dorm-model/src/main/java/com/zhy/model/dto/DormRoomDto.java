package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 宿舍查询dto
 */
@Data
@ApiModel(description = "宿舍查询Dto")
public class DormRoomDto {

    //分页参数
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页展示记录数")
    private Integer pageSize = 10;

    //楼栋id
    @ApiModelProperty(value = "楼栋id")
    private Integer buildingId;

    //专业code
    @ApiModelProperty(value = "专业code")
    private Integer majorCode;

    @ApiModelProperty(value = "楼层id")
    private Long floorId;

}
