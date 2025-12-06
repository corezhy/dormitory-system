package com.zhy.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 宿舍查询vo
 */
@Data
@ApiModel(description = "宿舍查询vo")
public class DormRoomVo {

    @ApiModelProperty("宿舍Id")
    private Long id;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("楼层号")
    private Integer floorNumber;

    @ApiModelProperty("完整编码")
    private String fullCode;

    @ApiModelProperty("总床位数")
    private Integer capacity;

    @ApiModelProperty("空闲床铺数")
    private Integer availableBeds;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("专业名称")
    private String majorName;

}
