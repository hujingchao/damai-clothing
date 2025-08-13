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
@Data
@ApiModel(value = "ChildNodeApprovalResult", description = "流程节点审批节点类")
@TableName(value = "flow_child_node_approval")
public class ChildNodeApprovalResult extends BaseFlowPojo {
    private static final long serialVersionUID = -375178079743949203L;
    @ApiModelProperty(value = "节点ID")
    private String childNodeId;
    @ApiModelProperty(value = "意见")
    private String opinion;
    @ApiModelProperty(value = "审批结果：true：审批通过 false：审批不通过")
    private Boolean result;
    @ApiModelProperty(value = "审核人ID")
    private String userId;
    @ApiModelProperty(value = "审核电子签名路径")
    private String imgPath;
    @ApiModelProperty(value = "审核人姓名")
    @TableField(exist = false)
    private String userName;
}
