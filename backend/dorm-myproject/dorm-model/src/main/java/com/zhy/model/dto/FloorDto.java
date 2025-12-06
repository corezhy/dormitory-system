package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询楼层dto
 */
@Data
@ApiModel(description = "员工登录dto")
public class FloorDto {

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

    //性别类型 1女 0男
    @ApiModelProperty(value = "性别类型 1女 0男")
    private Integer gender;


}
