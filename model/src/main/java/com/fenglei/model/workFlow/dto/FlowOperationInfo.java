package com.fenglei.model.workFlow.dto;

import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yzy
 */
@ApiModel(value = "FlowOperationInfo", description = "流程操作信息类")
@Data
@NoArgsConstructor
public class FlowOperationInfo {

    @ApiModelProperty(value = "流程ID")
    private String workFlowId;
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "单据数据")
    private Object formData;
    @ApiModelProperty(value = "节点审批条件")
    private ChildNodeApprovalResult childNodeApprovalResult;
    @ApiModelProperty(value = "数据ID集")
    private List<String> formIds;
    @ApiModelProperty(value = "实例状态")
    private String status;
    @ApiModelProperty(value = "流程用户")
    private FlowUser flowUser;
    @ApiModelProperty(value = "流程实例ID")
    private String workFlowInstantiateId;
    @ApiModelProperty(value = "当前节点ID")
    private String currentNodeId;
    @ApiModelProperty(value = "抄送节点")
    private List<ChildNode> childNodes;
}
