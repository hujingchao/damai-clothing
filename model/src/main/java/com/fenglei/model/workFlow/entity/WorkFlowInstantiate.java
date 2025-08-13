package com.fenglei.model.workFlow.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fenglei.model.base.pojo.BaseFlowPojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yzy
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkFlowInstantiate", description = "流程实例类")
@Data
@TableName(value = "flow_work_flow_instantiate")
public class WorkFlowInstantiate extends BaseFlowPojo {
    private static final long serialVersionUID = 7826120165867276890L;
    @ApiModelProperty(value = "表单ID")
    private String tableId;
    @ApiModelProperty(value = "流程ID")
    private String workFlowId;
    @ApiModelProperty(value = "单据ID")
    private String formId;
    @ApiModelProperty(value = "工作流版本")
    private String workFlowVersionId;
    @ApiModelProperty(value = "控制器最高级别")
    private String directorMaxLevel;
    @ApiModelProperty(value = "流程全限")
    private String flowPermission;
    @ApiModelProperty(value = "节点配置")
    @TableField(exist = false)
    private ChildNode childNode;
    @ApiModelProperty(value = "流程信息类")
    @TableField(exist = false)
    private WorkFlowDef workFlowDef;
    @ApiModelProperty(value = "条件节点列表")
    @TableField(exist = false)
    private List<ChildNode> conditionNodes;
    @ApiModelProperty(value = "节点信息类")
    @TableField(exist = false)
    private List<ChildNode> childNodes;
    @ApiModelProperty(value = "节点用户列表")
    @TableField(exist = false)
    private List<NodeUser> nodeUserList;
    @ApiModelProperty(value = "条件列表")
    @TableField(exist = false)
    private List<Condition> conditionList;
    @ApiModelProperty(value = "实例状态 0.启用中 1.已完结 2.已作废")
    private Integer status;
    @ApiModelProperty(value = "驳回时, 是否直接作废流程")
    private Boolean whenRejectedWhetherToDirectlyVoidTheProcess;
    @ApiModelProperty(value = "发起人ID")
    private String initiatorId;
}
