package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "CustomFormData", description = "流程自定义表单数据类")
@Data
@TableName(value = "flow_custom_form_Data")
public class CustomFormData extends BaseFlowPojo {

    private static final long serialVersionUID = -1115435917421627389L;

    @ApiModelProperty(value = "自定义表单ID")
    private String customFormId;
    @ApiModelProperty(value = "json数据")
    private String jsonData;
    @ApiModelProperty(value = "所属自定义表单对象")
    @TableField(exist = false)
    private CustomForm customForm;
    @ApiModelProperty(value = "创建人Id")
    private String creatorId;
}
