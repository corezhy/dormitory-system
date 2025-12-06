package com.zhy.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.zhy.common.groups.ValidationGroups;

/**
 * 新增修改楼层 DTO
 */
@Data
public class AddAndEditFloorDto {

    // 楼栋ID
    @ApiModelProperty(value = "楼栋ID", required = true, example = "1")
    @NotNull(message = "楼栋ID不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Long buildingId;

    /**
     * 限定专业代码（逻辑外键 → major.code），NULL表示不限
     */
    @ApiModelProperty(value = "限定专业代码（null 表示不限）", example = "080901")
    private String majorCode;

    // 宿舍总数：仅新增时必填且有效，编辑时不传或忽略1
    @ApiModelProperty(
            value = "宿舍总数（仅新增时需要）",
            required = false,
            example = "30"
    )
    @NotNull(message = "宿舍数量不能为空", groups = ValidationGroups.Insert.class)
    @Min(value = 1, message = "宿舍数量至少为1", groups = ValidationGroups.Insert.class)
    private Integer totalRooms;
}