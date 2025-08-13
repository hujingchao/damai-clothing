package com.fenglei.service.workFlow.operation;


import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import com.fenglei.service.workFlow.util.AnalysisJson;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.Objects;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 解析当前节点
 * @author yzy
 */
public class AnalysisCurrentChildNodeOperation implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(Context context) throws Exception {
        ChildNode currentNode;
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate)context.get("workFlowInstantiate");
        currentNode = AnalysisJson.getCurrentNode(workFlowInstantiate.getChildNode());
        ChildNodeApprovalResult childNodeApprovalResult = (ChildNodeApprovalResult)context.get("childNodeApprovalResult");
        if (Objects.nonNull(currentNode)) {
            childNodeApprovalResult.setChildNodeId(currentNode.getId());
            context.put("childNodeApprovalResult", childNodeApprovalResult);
        }
        context.put("currentNode", currentNode);
        return false;
    }

}
