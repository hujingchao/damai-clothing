package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CustomForm", description = "流程自定义表单类")
@Data
@TableName(value = "flow_custom_form")
public class CustomForm extends BaseFlowPojo {

    private static final long serialVersionUID = -6553505920013898018L;
    @ApiModelProperty(value = "表单名称")
    private String title;
    @ApiModelProperty(value = "icon")
    private String icon;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "状态")
    private Boolean status;
    @ApiModelProperty(value = "表单尺寸")
    private String toSize;
}
