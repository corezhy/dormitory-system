package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.zhy.common.groups.ValidationGroups;

/**
 * 新增修改学生dto
 */
@Data
@ApiModel(description = "新增修改学生dto")
public class AddAndEditStuDto {

    @ApiModelProperty("学号")
    @NotBlank(message = "学号不能为空", groups = {ValidationGroups.Insert.class})
    private String studentNo;

    @ApiModelProperty("姓名")
    @NotBlank(message = "姓名不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String name;

    @ApiModelProperty("性别")
    @NotNull(message = "性别不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Byte gender;

    @ApiModelProperty("专业代码")
    @NotBlank(message = "专业代码不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String majorCode;

    @ApiModelProperty("班级名称")
    @NotBlank(message = "班级名称不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String className;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String phone;

    @ApiModelProperty("楼栋Id")
    @NotNull(message = "楼栋不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Long buildingId;

    @ApiModelProperty("楼层Id")
    @NotNull(message = "楼层不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Long floorId;

    @ApiModelProperty("房间号")
    @NotBlank(message = "房间号不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String fullCode;

    @ApiModelProperty("床位号")
    @NotNull(message = "床位号不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Integer bedNo;

    @ApiModelProperty("状态，仅限修改")
    @NotNull(message = "状态不能为空", groups = {ValidationGroups.Update.class})
    private Byte status;
}