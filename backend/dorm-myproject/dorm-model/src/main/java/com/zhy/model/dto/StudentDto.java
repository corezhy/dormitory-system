package com.zhy.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 学生查询DTO（支持分页 + 条件筛选）
 */
@Data
@ApiModel(description = "学生查询条件")
public class StudentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // ========== 分页参数 ==========

    @ApiModelProperty(value = "当前页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页展示记录数", example = "10")
    @Min(value = 1, message = "每页记录数不能小于1")
    private Integer pageSize = 10;


    // ========== 查询条件（全部非必填）==========

    @ApiModelProperty("学号")
    @Pattern(regexp = "^[\\w]{1,20}$", message = "学号格式不合法")
    private String studentNo; // 驼峰命名更规范

    @ApiModelProperty("学生姓名")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,50}$", message = "姓名只能包含中英文、数字，长度1-50")
    private String name;

    @ApiModelProperty("性别: 0=男, 1=女")
    @Min(value = 0, message = "性别值只能为0或1")
    private Integer gender; // 修正：不能用 Type

    @ApiModelProperty("手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("状态: 1=在住，0=已退宿")
    @Min(value = 0, message = "状态值只能为0或1")
    private Integer status; // 修正：不能用 Type

    @ApiModelProperty("楼栋ID")
    @Min(value = 1, message = "楼栋ID必须为正整数")
    private Long buildingId;

    @ApiModelProperty("楼层ID")
    @Min(value = 1, message = "楼层ID必须为正整数")
    private Long floorId;

    @ApiModelProperty("专业Code，例：050101")
    @Pattern(regexp = "^[\\w]{1,30}$", message = "专业代码格式不合法")
    private String majorCode; // 修正：应为 String

    @ApiModelProperty("班级名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\s]{1,50}$", message = "班级名称只能包含中英文、数字、空格，长度1-50")
    private String className;

    @ApiModelProperty("宿舍编号，例：B05-2-07")
    @Pattern(regexp = "^[A-Z]\\d{2}-\\d{1,2}-\\d{2}$", message = "宿舍编号格式应为：B01-1-01")
    private String fullCode;
}