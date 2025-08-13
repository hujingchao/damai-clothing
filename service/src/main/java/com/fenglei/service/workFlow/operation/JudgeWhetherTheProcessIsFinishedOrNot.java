package com.fenglei.service.workFlow.operation;


import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.List;
import java.util.Objects;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 判断流程是否结束
 * @author yzy
 */

public class JudgeWhetherTheProcessIsFinishedOrNot implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(Context context) throws Exception {
        List<ChildNode> childNodeList = ClassConversionTools.castList(context.get("childNodeList"), ChildNode.class);
        List<ChildNode> needChangeChildNodes = ClassConversionTools.castList(context.get("needChangeChildNodes"), ChildNode.class);
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate) context.get("workFlowInstantiate");
        boolean isJudgeWhetherTheProcessIsFinished = false;
        boolean isTheProcessEffective = true;
        ChildNodeApprovalResult childNodeApprovalResult = null;
        if (Objects.nonNull(context.get("childNodeApprovalResult"))) {
            childNodeApprovalResult = (ChildNodeApprovalResult)context.get("childNodeApprovalResult");
            if (!childNodeApprovalResult.getResult()) {
                isTheProcessEffective = false;
            }
        }
        if (childNodeList.size() > ChildNodeUtil.NUMBER_INT_0) {
            for (ChildNode childNode : childNodeList) {
                for (ChildNode changeChildNode : needChangeChildNodes) {
                    if (childNode.getId().equals(changeChildNode.getId())) {
                        childNode.setStatus(changeChildNode.getStatus());
                    }
                }
            }
            boolean flag = true;
            for (ChildNode childNode : childNodeList) {
                if (childNode.getStatus() != null && ChildNodeUtil.NUMBER_INT_2 != childNode.getStatus()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                isJudgeWhetherTheProcessIsFinished = true;
            }
        }
        if (isJudgeWhetherTheProcessIsFinished) {
            if (isTheProcessEffective) {
                workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_1);
            } else {
                workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
            }
        }
        context.put("workFlowInstantiate", workFlowInstantiate);
        context.put("isJudgeWhetherTheProcessIsFinished", isJudgeWhetherTheProcessIsFinished);
        return false;
    }

}
