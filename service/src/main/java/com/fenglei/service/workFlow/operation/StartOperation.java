package com.fenglei.service.workFlow.operation;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.model.workFlow.entity.TableInfo;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import com.fenglei.service.workFlow.util.*;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 开始流程的操作
 * @author yzy
 */

public class StartOperation implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(Context context) throws Exception {
        List<ChildNode> childNodeList = ClassConversionTools.castList(context.get("childNodeList"), ChildNode.class);
        String userId = (String)context.get("userId");
        Object formData = context.get("formData");
        TableInfo tableInfoDTO = (TableInfo)context.get("tableInfoDTO");
        List<ChildNode> sponsorChildNodes = childNodeList.stream().filter(s -> ChildNodeUtil.NODE_TYPE_INITIATOR.equals(s.getType())).collect(Collectors.toList());
        List<ChildNode> needChangeChildNodes = ClassConversionTools.castList(context.get("needChangeChildNodes"), ChildNode.class);
        if (sponsorChildNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
            // 发起人
            ChildNode sponsorChildNode = sponsorChildNodes.get(ChildNodeUtil.NUMBER_INT_0);
            sponsorChildNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
            // 加入需要修改节点的集合
            needChangeChildNodes.add(sponsorChildNode);

            WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate)context.get("workFlowInstantiate");
            ChildNode rootNode = workFlowInstantiate.getChildNode();
            ChildNode childNode = rootNode.getChildNode();
            getTheFirstExecutableNodeUnderRouting(childNode, userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
        }
        context.put("needChangeChildNodes", needChangeChildNodes);
        return false;
    }

    /**
     * 获取路由下第一个可执行节点
     * @param childNode 节点
     * @param userId 用户Id
     * @param formData 单据数据
     * @param needChangeChildNodes  需要修改节点集合
     * @param context 上下文
     * @param rootNode 根节点
     * @param tableInfoDTO 表单
     * @throws Exception 错误
     */
    @SuppressWarnings("unchecked")
    private void getTheFirstExecutableNodeUnderRouting(ChildNode childNode, String userId, Object formData, List<ChildNode> needChangeChildNodes, Context context, ChildNode rootNode, TableInfo tableInfoDTO) throws Exception{
        List<FlowNotice> flowNotices = ClassConversionTools.castList(context.get("flowNotices"), FlowNotice.class);
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate)context.get("workFlowInstantiate");
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getType())) {
            List<ChildNode> conditionNodes = childNode.getConditionNodes();
            // 判断执行哪一条条件线
            ChildNode toExecutionChildNode = null;
            for (ChildNode conditionNode : conditionNodes) {
                if (Objects.nonNull(conditionNode.getConditionList()) && conditionNode.getConditionList().size() > ChildNodeUtil.NUMBER_INT_0) {
                    if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                        toExecutionChildNode = conditionNode;
                    }
                }
            }
            if (Objects.isNull(toExecutionChildNode)) {
                for (ChildNode conditionNode : conditionNodes) {
                    if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() == ChildNodeUtil.NUMBER_INT_0) {
                        toExecutionChildNode = conditionNode;
                    }
                }
            }
            boolean isFinish = false;
            if (Objects.nonNull(toExecutionChildNode)) {
                toExecutionChildNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                // 加入需要修改节点的集合
                needChangeChildNodes.add(toExecutionChildNode);

                ChildNode childNodeOfConditionNode = toExecutionChildNode.getChildNode();
                if (Objects.nonNull(childNodeOfConditionNode)) {
                    // 判断该条条件的下一节点的类型，然后分开处理
                    if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNodeOfConditionNode.getType())) {
                        getTheFirstExecutableNodeUnderRouting(childNodeOfConditionNode, userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                    } else if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNodeOfConditionNode.getType())) {
                        // 设为当前节点
                        childNodeOfConditionNode.setStatus(ChildNodeUtil.NUMBER_INT_1);
                        context.put("currentNode", childNodeOfConditionNode);
                        FlowNotice message = SendMessages.createMessage(childNodeOfConditionNode, formData, tableInfoDTO, workFlowInstantiate, context);
                        flowNotices.add(message);
                        context.put("flowNotices", flowNotices);
                        needChangeChildNodes.add(childNodeOfConditionNode);
                    } else if (ChildNodeUtil.NODE_TYPE_CC.equals(childNodeOfConditionNode.getType())) {
                        childNodeOfConditionNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        FlowNotice message = SendMessages.createMessage(childNodeOfConditionNode, formData, tableInfoDTO, workFlowInstantiate, context);
                        flowNotices.add(message);
                        context.put("flowNotices", flowNotices);
                        needChangeChildNodes.add(childNodeOfConditionNode);
                        if (Objects.nonNull(childNodeOfConditionNode.getChildNode())) {
                            getTheFirstExecutableNodeUnderRouting(childNode.getChildNode(), userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                        } else {
                            ChildNode childNode1 = AnalysisJson.getRoutingNodeOfANode(childNodeOfConditionNode, rootNode);
                            if (Objects.nonNull(childNode1)) {
                                getTheFirstExecutableNodeUnderRouting(childNode1, userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                            } else {
                                isFinish = true;
                            }
                        }
                    }
                    needChangeChildNodes.add(childNodeOfConditionNode);
                }
                if (isFinish && Objects.nonNull(childNode.getChildNode())) {
                    getTheFirstExecutableNodeUnderRouting(childNode.getChildNode(), userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                }
            }
        } else if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
            // 设为当前节点
            childNode.setStatus(ChildNodeUtil.NUMBER_INT_1);
            if (ChildNodeUtil.NUMBER_2.equals(childNode.getSettype()) && ChildNodeUtil.NUMBER_1.equals(childNode.getNoHanderAction())) {
                childNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                needChangeChildNodes.add(childNode);
                if (Objects.nonNull(childNode.getChildNode())) {
                    getTheFirstExecutableNodeUnderRouting(childNode.getChildNode(), userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                } else {
                    ChildNode childNode1 = AnalysisJson.getRoutingNodeOfANode(childNode, rootNode);
                    if (Objects.nonNull(childNode1)) {
                        getTheFirstExecutableNodeUnderRouting(childNode1, userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                    }
                }
            }
            FlowNotice message = SendMessages.createMessage(childNode, formData, tableInfoDTO, workFlowInstantiate, context);
            flowNotices.add(message);
            context.put("flowNotices", flowNotices);
            context.put("currentNode", childNode);
            needChangeChildNodes.add(childNode);
        } else if (ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())) {
            FlowNotice message = SendMessages.createMessage(childNode, formData, tableInfoDTO, workFlowInstantiate, context);
            flowNotices.add(message);
            context.put("flowNotices", flowNotices);
            childNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
            needChangeChildNodes.add(childNode);
            if (Objects.nonNull(childNode.getChildNode())) {
                getTheFirstExecutableNodeUnderRouting(childNode.getChildNode(), userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
            } else {
                ChildNode childNode1 = AnalysisJson.getRoutingNodeOfANode(childNode, rootNode);
                if (Objects.nonNull(childNode1)) {
                    getTheFirstExecutableNodeUnderRouting(childNode1, userId, formData, needChangeChildNodes, context, rootNode, tableInfoDTO);
                }
            }
        }
    }

}
