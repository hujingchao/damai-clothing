package com.fenglei.service.workFlow.operation;

import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.model.workFlow.entity.TableInfo;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import com.fenglei.service.workFlow.util.ChildNodeTreeUtil;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import com.fenglei.service.workFlow.util.SendMessages;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 解析下一节点
 * @author yzy
 */
public class AnalysisNextChildNodeOperation implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(Context context) throws Exception {
        Object formData = context.get("formData");
        List<ChildNode> needChangeChildNodes = ClassConversionTools.castList(context.get("needChangeChildNodes"), ChildNode.class);
        ChildNode currentNode = (ChildNode)context.get("currentNode");
        String userId = (String) context.get("userId");
        TableInfo tableInfoDTO = (TableInfo)context.get("tableInfoDTO");
        List<FlowNotice> flowNotices = ClassConversionTools.castList(context.get("flowNotices"), FlowNotice.class);
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate)context.get("workFlowInstantiate");
        if (currentNode.getStatus() == ChildNodeUtil.NUMBER_INT_1) {
            // 当前节点还需审批
            boolean flag = true;
            for (ChildNode childNode : needChangeChildNodes) {
                if (childNode.getId().equals(currentNode.getId())) {
                    childNode = currentNode;
                    flag = false;
                }
            }
            if (flag) {
                flowNotices.add(SendMessages.createMessage(currentNode, formData, tableInfoDTO, workFlowInstantiate, context));
                needChangeChildNodes.add(currentNode);
            }
        } else if (currentNode.getStatus() == ChildNodeUtil.NUMBER_INT_2){
            needChangeChildNodes.add(currentNode);
            // 当前节点审批完成
            ChildNode rootNode = workFlowInstantiate.getChildNode();
            ChildNode nextNode = null;
            // 检验本级路由是否执行完成
            if (Objects.isNull(currentNode.getChildNode())) {
                // 本级路由完成，向上获取下一节点
                ChildNodeTreeUtil childNodeTreeUtil = new ChildNodeTreeUtil();
                Map<String, Object> map = childNodeTreeUtil.getUpFirstChildNode(rootNode, ChildNodeUtil.NUMBER_1, formData, userId, currentNode, context);
                if (Objects.nonNull(map.get("childNode"))) {
                    nextNode = (ChildNode) map.get("childNode");
                }
                if (Objects.nonNull(flowNotices) && Objects.nonNull(map.get("flowNotices"))) {
                    flowNotices.addAll(Objects.requireNonNull(ClassConversionTools.castList(map.get("flowNotices"), FlowNotice.class)));
                }
                if (Objects.nonNull(map.get("makeACopyFor"))) {
                    List<ChildNode> makeACopyFor = ClassConversionTools.castList(map.get("makeACopyFor"), ChildNode.class);
                    if (Objects.nonNull(needChangeChildNodes) && Objects.nonNull(makeACopyFor) && makeACopyFor.size() > ChildNodeUtil.NUMBER_INT_0) {
                        needChangeChildNodes.addAll(makeACopyFor);
                    }
                }

            } else {
                // 继续向下获取下一节点
                ChildNode nextChildNode = currentNode.getChildNode();
                if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(nextChildNode.getType())) {
                    nextNode = nextChildNode;
                } else {
                    nextNode = ChildNodeUtil.getNextNode(nextChildNode, formData, userId);
                    ChildNodeTreeUtil childNodeTreeUtil = new ChildNodeTreeUtil();
                    List<ChildNode> childNodes = childNodeTreeUtil.getTheNodeOfTheSpecifiedCategoryBetweenTwoNodes(currentNode, nextNode, ChildNodeUtil.NUMBER_2, userId, formData);
                    for (ChildNode childNode : childNodes) {
                        flowNotices.add(SendMessages.createMessage(childNode, formData, tableInfoDTO, workFlowInstantiate, context));
                    }
                }
            }
            if (Objects.nonNull(nextNode)) {
                nextNode.setStatus(ChildNodeUtil.NUMBER_INT_1);
                needChangeChildNodes.add(nextNode);
                flowNotices.add(SendMessages.createMessage(nextNode, formData, tableInfoDTO, workFlowInstantiate, context));
            }
        }
        context.put("flowNotices", flowNotices);
        context.put("needChangeChildNodes", needChangeChildNodes);
        return false;
    }

}
