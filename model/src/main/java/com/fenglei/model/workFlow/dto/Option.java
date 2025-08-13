package com.fenglei.model.workFlow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yzy
 */
@ApiModel(value = "Option", description = "条件选项")
@Data
public class Option {
    @ApiModelProperty(value = "序号")
    private Integer idx;
    @ApiModelProperty(value = "标签")
    private String  label;
    @ApiModelProperty(value = "值")
    private String  value;
}
