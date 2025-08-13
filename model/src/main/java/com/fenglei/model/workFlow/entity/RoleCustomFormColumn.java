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
@ApiModel(value = "RoleCustomFormColumn", description = "角色自定义表单列权限")
@Data
@TableName(value = "flow_role_custom_form_column")
public class RoleCustomFormColumn extends BaseFlowPojo {
    private static final long serialVersionUID = 5142716971820811996L;
    @ApiModelProperty(value = "自定义表单ID")
    private String customFormId;
    @ApiModelProperty(value = "角色ID")
    private String roleId;
    @ApiModelProperty(value = "列内容")
    private String content;
}
