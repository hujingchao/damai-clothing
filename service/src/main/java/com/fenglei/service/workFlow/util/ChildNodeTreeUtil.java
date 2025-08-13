package com.fenglei.service.workFlow.util;

import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.model.workFlow.entity.TableInfo;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import lombok.Data;
import org.apache.commons.chain.Context;

import java.util.*;

/**
 * @author yzy
 */
@Data
public class ChildNodeTreeUtil {

    List<ChildNode> childNodes = new ArrayList<>();
    List<ChildNode> routeChildNodes = new ArrayList<>();
    /**
     * 把根节点拆分成
     * @param root 根节点
     * @return List<ChildNode>
     */
    public List<ChildNode> getSplitNodeChildNodes(ChildNode root, String type, Object formData, String userId, Integer status, Boolean isJustTakeTheFirstOne) throws Exception{
        addChildNode(root, type, formData, userId, status, isJustTakeTheFirstOne);
        return childNodes;
    }

    /**
     * 添加节点
     * @param root 根节点
     * @param type 节点类型
     * @param formData 单据数据
     * @param userId 用户ID
     * @param status 状态
     * @param isJustTakeTheFirstOne 是否只拿取第一个
     * @throws Exception 错误
     */
    private void addChildNode (ChildNode root, String type, Object formData, String userId, Integer status, Boolean isJustTakeTheFirstOne) throws Exception{
        if (root.getType().equals(type)) {
            if (root.getStatus().intValue() == status.intValue()) {
                childNodes.add(root);
                if (Objects.nonNull(isJustTakeTheFirstOne) && isJustTakeTheFirstOne) {
                    return;
                }
            }
        }
        if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_CONDITION.equals(root.getType())) {
            if (Objects.nonNull(root.getChildNode())) {
                addChildNode(root.getChildNode(), type, formData, userId, status, isJustTakeTheFirstOne);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(root.getType())) {
            if (Objects.nonNull(root.getChildNode())) {
                addChildNode(root.getChildNode(), type, formData, userId, status, isJustTakeTheFirstOne);
            }
            if (Objects.nonNull(root.getConditionNodes()) && root.getConditionNodes().size() > 0) {
                List<ChildNode> conditionNodes = root.getConditionNodes();
                if (conditionNodes.size() > 0) {
                    ChildNode nextConditionNode = null;
                    for (ChildNode conditionNode : conditionNodes) {
                        if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                            nextConditionNode = conditionNode;
                        }
                    }
                    if (Objects.isNull(nextConditionNode)) {
                        for (ChildNode conditionNode : conditionNodes) {
                            if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() == 0) {
                                nextConditionNode = conditionNode;
                            }
                        }
                    }
                    if (Objects.nonNull(nextConditionNode)) {
                        addChildNode(nextConditionNode, type, formData, userId, status, isJustTakeTheFirstOne);
                    }
                }
            }
        }
    }

    /**
     * 向下获取连个节点之间的指定节点的集合
     * @param startNode 开始节点
     * @param endNode 结束节点
     * @param type 节点类型
     * @return List<ChildNode>
     */
    public List<ChildNode> getTheNodeOfTheSpecifiedCategoryBetweenTwoNodes(ChildNode startNode, ChildNode endNode, String type, String userId, Object formData) throws Exception{
        addChildNode2(startNode, endNode, type, userId, formData);
        return childNodes;
    }

    /**
     * 添加节点
     * @param startNode 开始节点
     * @param endNode 结束节点
     * @param type 节点类型
     * @param userId 用户ID
     * @param formData 单据数据
     * @throws Exception 错误
     */
    private void addChildNode2(ChildNode startNode, ChildNode endNode, String type, String userId, Object formData) throws Exception{
        if (startNode.getType().equals(type)) {
            childNodes.add(startNode);
        }
        if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(startNode.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(startNode.getType()) || ChildNodeUtil.NODE_TYPE_CONDITION.equals(startNode.getType())) {
            if (Objects.nonNull(startNode.getChildNode())) {
                addChildNode2(startNode.getChildNode(), endNode, type, userId, formData);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(startNode.getType())) {
            if (Objects.nonNull(startNode.getChildNode())) {
                addChildNode2(startNode.getChildNode(), endNode, type, userId, formData);
            }
            if (Objects.nonNull(startNode.getConditionNodes()) && startNode.getConditionNodes().size() > 0) {
                List<ChildNode> conditionNodes = startNode.getConditionNodes();
                if (conditionNodes.size() > 0) {
                    ChildNode nextConditionNode = null;
                    for (ChildNode conditionNode : conditionNodes) {
                        if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                            nextConditionNode = conditionNode;
                        }
                    }
                    if (Objects.isNull(nextConditionNode)) {
                        for (ChildNode conditionNode : conditionNodes) {
                            if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() == 0) {
                                nextConditionNode = conditionNode;
                            }
                        }
                    }
                    if (Objects.nonNull(nextConditionNode)) {
                        addChildNode2(nextConditionNode, endNode, type, userId, formData);
                    }
                }
            }
        }
    }

    /**
     * 向上获取第一个可执行节点
     * @param root        根节点
     * @param type        节点类型
     * @param formData    单据数据
     * @param userId      用户id
     * @param currentNode 当前节点
     * @return Map<String, Object>
     * @throws Exception 错误
     */
    public Map<String, Object> getUpFirstChildNode(ChildNode root, String type, Object formData, String userId, ChildNode currentNode, Context context) throws Exception{
        TableInfo tableInfoDTO = (TableInfo) context.get("tableInfoDTO");
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate) context.get("workFlowInstantiate");
        addRouteChildNodes(root, currentNode);
        List<ChildNode> incompleteRoutes = new ArrayList<>();
        for (ChildNode routeChildNode : routeChildNodes) {
            if (verifyRoutingIsComplete(routeChildNode)) {
                incompleteRoutes.add(routeChildNode);
            }
        }
        Map<String, Object> map = new HashMap<>();
        if (incompleteRoutes.size() > 0) {
            ChildNode routeChildNode = incompleteRoutes.get((incompleteRoutes.size()-1));
            List<ChildNode> childNodeList = new ArrayList<>();
            getRouteToChildNodeList(childNodeList, routeChildNode);
            List<FlowNotice> flowNotices = new ArrayList<>();
            List<ChildNode> makeACopyFor = new ArrayList<>();
            for (ChildNode childNode : childNodeList) {
                if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
                    if (childNode.getStatus() == 0) {
                        map.put("childNode", childNode);
                        break;
                    }
                } else if (ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())){
                    if (childNode.getStatus() == null || childNode.getStatus() == 0) {
                        childNode.setStatus(2);
                        makeACopyFor.add(childNode);
                        FlowNotice flowNotice = SendMessages.createMessage(childNode, formData, tableInfoDTO, workFlowInstantiate, context);
                        flowNotices.add(flowNotice);
                        break;
                    }
                }
            }
            if (map.get("childNode") == null) {
                List<ChildNode> conditionNodes = routeChildNode.getConditionNodes();
                ChildNode conditionNode1 = null;
                for (ChildNode conditionNode : conditionNodes) {
                    if (conditionNode.getConditionList() != null && conditionNode.getConditionList().size() > 0) {
                        if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                            conditionNode1 = conditionNode;
                        }
                    }
                }
                if (Objects.isNull(conditionNode1)) {
                    for (ChildNode conditionNode : conditionNodes) {
                        if (conditionNode.getConditionList() == null || conditionNode.getConditionList().size() == 0) {
                            conditionNode1 = conditionNode;
                        }
                    }
                }
                if (Objects.nonNull(conditionNode1) && Objects.nonNull(conditionNode1.getChildNode())) {
                    List<ChildNode> childNodeList1 = new ArrayList<>();
                    getRouteToChildNodeList(childNodeList1, conditionNode1.getChildNode());
                    if (childNodeList1.size() > 0) {
                        for (ChildNode childNode : childNodeList1) {
                            if (childNode.getStatus() == 0) {
                                map.put("childNode", childNode);
                            }
                        }
                    }
                }

            }
            map.put("flowNotices", flowNotices);
            map.put("makeACopyFor", makeACopyFor);
        }

        return map;
    }

    private ChildNode getIsTreeRoute(ChildNode root, ChildNode currentNode) {
        if (ChildNodeUtil.NODE_TYPE_INITIATOR.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_REVIEWER.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_CONDITION.equals(root.getType())) {
            if (Objects.nonNull(root.getChildNode())) {
                return getIsTreeRoute(root.getChildNode(), currentNode);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(root.getType())) {
            Boolean flag = AnalysisJson.idIsInRoute(root, currentNode.getId());
            if (flag) {
                return root;
            } else {
                return null;
            }
        }
        return null;
    }

    private void addRouteChildNodes(ChildNode root, ChildNode currentNode) {
        ChildNode routeChildNode = getIsTreeRoute(root, currentNode);
        if (Objects.nonNull(routeChildNode)) {
            boolean flag = true;
            for (ChildNode childNode : routeChildNodes) {
                if (childNode.getId().equals(routeChildNode.getId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                routeChildNodes.add(routeChildNode);
            }
        }
        ChildNode childNode;
        if (ChildNodeUtil.NODE_TYPE_INITIATOR.equals(root.getType()) && ChildNodeUtil.NODE_TYPE_REVIEWER.equals(root.getType()) && ChildNodeUtil.NODE_TYPE_CC.equals(root.getType()) && ChildNodeUtil.NODE_TYPE_CONDITION.equals(root.getType())) {
            childNode = root.getChildNode();
            if (Objects.nonNull(childNode)) {
                addRouteChildNodes(routeChildNode, currentNode);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(root.getType())) {
            List<ChildNode> conditionNodes = root.getConditionNodes();
            if (conditionNodes.size() > 0) {
                for (ChildNode conditionNode : conditionNodes) {
                    addRouteChildNodes(conditionNode, currentNode);
                }
            }
            ChildNode childNode1 = root.getChildNode();
            if (Objects.nonNull(childNode1)) {
                addRouteChildNodes(childNode1, currentNode);
            }
        }
    }

    /**
     * 验证路由是否完成
     * @param root 根节点
     * @return Boolean
     */
    private Boolean verifyRoutingIsComplete(ChildNode root) {
        List<ChildNode> childNodeList = new ArrayList<>();
        getRouteToChildNodeList(childNodeList, root);
        boolean flag = true;
        for (ChildNode childNode : childNodeList) {
            if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
                if (childNode.getStatus() == 1 && childNode.getStatus() == 0) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 获取路由节点集合
     * @param childNodeList 节点集合
     * @param root 根节点
     */
    private void getRouteToChildNodeList(List<ChildNode> childNodeList, ChildNode root) {
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(root.getType())) {
            if (Objects.nonNull(root.getChildNode())) {
                getRouteToChildNodeList(childNodeList, root.getChildNode());
            }
        }
        if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(root.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(root.getType())) {
            childNodeList.add(root);
            if (Objects.nonNull(root.getChildNode())) {
                getRouteToChildNodeList(childNodeList, root.getChildNode());
            }
        }
    }

    /**
     * 向上获取第一个节可用点
     * @param root 根节点
     * @param formData 单据数据
     * @param currentNode 当前节点
     * @param userId 用户ID
     * @return ChildNode
     * @throws Exception 错误
     */
    public ChildNode getUpFirstChildNodeForStatic(ChildNode root, Object formData, ChildNode currentNode, String userId) throws Exception{
        addRouteChildNodes(root, currentNode);
        List<ChildNode> incompleteRoutes = new ArrayList<>();
        for (ChildNode routeChildNode : routeChildNodes) {
            if (verifyRoutingIsComplete(routeChildNode)) {
                incompleteRoutes.add(routeChildNode);
            }
        }
        ChildNode childNode1 = null;
        if (incompleteRoutes.size() > 0) {
            ChildNode routeChildNode = incompleteRoutes.get((incompleteRoutes.size()-1));
            List<ChildNode> childNodeList = new ArrayList<>();
            getRouteToChildNodeList(childNodeList, routeChildNode);
            for (ChildNode childNode : childNodeList) {
                if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
                    if (childNode.getStatus() == 0) {
                        childNode1 = childNode;
                        break;
                    }
                } else if (ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())){
                    if (childNode.getStatus() == 0) {
                        childNode1 = childNode;
                        break;
                    }
                }
            }
            if (childNode1 == null) {
                List<ChildNode> conditionNodes = routeChildNode.getConditionNodes();
                ChildNode conditionNode1 = null;
                for (ChildNode conditionNode : conditionNodes) {
                    if (conditionNode.getConditionList() != null && conditionNode.getConditionList().size() > 0) {
                        if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                            conditionNode1 = conditionNode;
                        }
                    }
                }
                if (Objects.isNull(conditionNode1)) {
                    for (ChildNode conditionNode : conditionNodes) {
                        if (conditionNode.getConditionList() == null || conditionNode.getConditionList().size() == 0) {
                            conditionNode1 = conditionNode;
                        }
                    }
                }
                if (Objects.nonNull(conditionNode1) && Objects.nonNull(conditionNode1.getChildNode())) {
                    List<ChildNode> childNodeList1 = new ArrayList<>();
                    getRouteToChildNodeList(childNodeList1, conditionNode1.getChildNode());
                    if (childNodeList1.size() > 0) {
                        for (ChildNode childNode : childNodeList1) {
                            if (childNode.getStatus() == 0) {
                                childNode1 = childNode;
                            }
                        }
                    }
                }

            }
        }
        return childNode1;
    }

}
