package com.fenglei.model.basedata;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "BdSpecification-规格", description = "规格类")
@TableName(value = "bd_specification")
public class BdSpecification {

    @ApiModelProperty("名称")
    private String name;


    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty("内码")
    private String id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人id")
    private String creatorId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private String updateTime;

    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updater;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改人id")
    private String updaterId;

    @TableField(exist = false)
    @ApiModelProperty("开始时间，用于过滤")
    private String beginDate;

    @TableField(exist = false)
    @ApiModelProperty("结束时间，用于过滤")
    private String endDate;

    @TableField(exist = false)
    private List<String> ids;

    @TableField(exist = false)
    @ApiModelProperty(value = "排除项ids")
    private List<String> excludeIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "公共过滤")
    private String commFilter;

    @TableField(exist = false)
    private List<Integer> inStatus;
}
