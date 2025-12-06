package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Null;

import com.zhy.common.groups.ValidationGroups;

/**
 * 新增修改宿舍dto
 */
@Data
@ApiModel("新增修改宿舍dto")
public class AddAndEditDormRoomDto {

    @ApiModelProperty("所属楼栋Id（新增必填，修改时禁止传入）")
    @NotNull(message = "所属楼栋不能为空", groups = ValidationGroups.Insert.class)
    @Null(message = "修改宿舍时不能更改所属楼栋", groups = ValidationGroups.Update.class)
    private Long buildingId;

    @ApiModelProperty("所属楼层Id（新增必填，修改时禁止传入）")
    @NotNull(message = "所属楼层不能为空", groups = ValidationGroups.Insert.class)
    @Null(message = "修改宿舍时不能更改所属楼层", groups = ValidationGroups.Update.class)
    private Long floorId;

    @ApiModelProperty("总床位数（新增/修改均必填，且必须大于0）")
    @NotNull(message = "总床位数不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    @Positive(message = "总床位数必须大于0", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Integer capacity;
}