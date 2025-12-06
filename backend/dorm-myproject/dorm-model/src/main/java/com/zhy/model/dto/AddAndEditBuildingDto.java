// 文件：com.zhy.model.dto.AddAndEditBuildingDto

package com.zhy.model.dto;

import com.zhy.common.groups.ValidationGroups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Data
@ApiModel("新增/修改楼栋 DTO")
public class AddAndEditBuildingDto {

    @NotBlank(message = "楼栋名称不能为空")
    @Size(max = 50, message = "楼栋名称长度不能超过50个字符")
    @ApiModelProperty(value = "楼栋名称", required = true, example = "思远楼")
    private String name;

    @NotBlank(message = "楼栋编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "楼栋编码只能包含字母和数字，长度1-20")
    @ApiModelProperty(value = "楼栋编码（如B01）", required = true, example = "B01")
    private String code;

    @NotNull(message = "性别类型不能为空")
    @Min(value = 0, message = "性别类型只能为0或1")
    @Max(value = 1, message = "性别类型只能为0或1")
    @ApiModelProperty(value = "性别类型：0=男，1=女", required = true, example = "0")
    private Integer genderType;

    @Size(max = 200, message = "描述长度不能超过200个字符")
    @ApiModelProperty(value = "描述", example = "主要容纳计算机学院男生")
    private String description;

    // 👇 新增时 floorNum 必填（合理）
    @NotNull(message = "楼层数不能为空", groups = ValidationGroups.Insert.class)
    @Min(value = 1, message = "楼层数至少为1层", groups = ValidationGroups.Insert.class)
    @Max(value = 100, message = "楼层数不能超过100层", groups = ValidationGroups.Insert.class)
    @ApiModelProperty(
            value = "楼层数（仅新增时必填）",
            example = "10",
            notes = "新增楼栋时必须填写，用于生成楼层；编辑时可忽略"
    )
    private Integer floorNum;

    // ✅ 关键修改：移除 @NotNull！允许为空
    @Min(value = 1, message = "每层宿舍数至少为1间")
    @Max(value = 50, message = "每层宿舍数不能超过50间")
    @ApiModelProperty(
            value = "每层宿舍数量（仅统一模式使用）",
            example = "30",
            notes = "若提供了 floorConfigs，则此字段被忽略"
    )
    private Integer roomsPerFloor; // ← 不再加 @NotNull！

    @Valid
    @ApiModelProperty(
            value = "自定义楼层宿舍配置（可选）",
            notes = "格式：[{\"floorNumber\":1,\"roomCount\":30}, ...]。若提供，则 roomsPerFloor 被忽略"
    )
    private List<FloorConfig> floorConfigs;

    @Data
    @ApiModel("楼层宿舍配置")
    public static class FloorConfig {
        @NotNull(message = "楼层号不能为空")
        @Min(value = 1, message = "楼层号至少为1")
        @Max(value = 100, message = "楼层号不能超过100")
        @ApiModelProperty(value = "楼层号", example = "1", required = true)
        private Integer floorNumber;

        @NotNull(message = "宿舍数量不能为空")
        @Min(value = 0, message = "宿舍数量不能小于0")
        @Max(value = 50, message = "宿舍数量不能超过50")
        @ApiModelProperty(value = "该楼层宿舍数量", example = "30", required = true)
        private Integer roomCount;
    }
}