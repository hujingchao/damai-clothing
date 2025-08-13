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
@ApiModel(value = "WorkFlow", description = "流程配置类")
@Data
@TableName(value = "flow_work_flow")
public class WorkFlow extends BaseFlowPojo {
    private static final long serialVersionUID = -8125199595092933283L;
    @ApiModelProperty(value = "表单ID")
    private String tableId;
    @ApiModelProperty(value = "工作流版本")
    private String workFlowVersionId;
    @ApiModelProperty(value = "控制器最高级别")
    private String directorMaxLevel;
    @ApiModelProperty(value = "流程全限")
    private String flowPermission;
    @ApiModelProperty(value = "json内容")
    private String content;
    @ApiModelProperty(value = "驳回时, 是否直接作废流程")
    private Boolean whenRejectedWhetherToDirectlyVoidTheProcess;
    @ApiModelProperty(value = "流程类别")
    private String flowType;
    @ApiModelProperty(value = "流程名称")
    private String flowName;
    @ApiModelProperty(value = "表单名称")
    @TableField(exist = false)
    private String tableName;
    @ApiModelProperty(value = "状态")
    private Boolean status;
}
