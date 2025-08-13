package com.fenglei.service.workFlow.util;

import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.model.workFlow.entity.NodeUser;
import lombok.Data;

import java.util.*;

/**
 * @author yzy
 */
@Data
public class AnalysisJson {

    /**
     * 创建ChildNode tree
     * @param childNode 节点
     * @param workFlowInstantiateId 流程实例ID
     * @return ChildNode
     */
    public static ChildNode getChildNode(ChildNode childNode, String workFlowInstantiateId) {
        if (StringUtils.isEmpty(childNode.getId())) {
            childNode.setId(UUIDUTIL.getUUID());
        }
        // ------> 个人节点
        ChildNode childNode1 = childNode.getChildNode();
        if (Objects.nonNull(childNode1)) {
            if (StringUtils.isEmpty(childNode1.getId())) {
                childNode1.setId(UUIDUTIL.getUUID());
            }
            childNode1.setChildNodeId(childNode.getId());
            childNode1.setWorkFlowInstantiateId(workFlowInstantiateId);
            getChildNode(childNode1, workFlowInstantiateId);
            childNode.setChildNode(childNode1);
        }
        // ------> 条件
        List<Condition> conditionList = childNode.getConditionList();
        if (Objects.nonNull(conditionList)) {
            for (Condition condition : conditionList) {
                condition.setChildNodeId(childNode.getId());
                condition.setWorkFlowInstantiateId(workFlowInstantiateId);
            }
            childNode.setConditionList(conditionList);
        }

        // ------> 节点用户
        List<NodeUser> nodeUserList = childNode.getNodeUserList();
        if (Objects.nonNull(nodeUserList)) {
            for (NodeUser nodeUser : nodeUserList) {
                nodeUser.setChildNodeId(childNode.getId());
                nodeUser.setWorkFlowInstantiateId(workFlowInstantiateId);
            }
            childNode.setNodeUserList(nodeUserList);
        }

        // ------> 条件节点
        List<ChildNode> conditionNodes = childNode.getConditionNodes();
        if (Objects.nonNull(conditionNodes)) {
            // 获取每个顶层元素的子数据集合
            for (ChildNode childNode2 : conditionNodes) {
                childNode2.setPid(childNode.getId());
                getChildNode(childNode2, workFlowInstantiateId);
            }
            childNode.setConditionNodes(conditionNodes);
        }

        return childNode;
    }

    private List<ChildNode> conditionNodes1 = new ArrayList<>();
    private List<ChildNode> childNodes1 = new ArrayList<>();
    private List<NodeUser> nodeUserList1 = new ArrayList<>();
    private List<Condition> conditionList1 = new ArrayList<>();

    /**
     * 树形节点转为多个List
     * @param childNode 节点
     * @return Map<String, Object>
     */
    public Map<String, Object> getChildNodeToList(ChildNode childNode) {
        childNodes1.add(childNode);
        getConditionNodes(childNode.getChildNode());
        Map<String, Object> map = new HashMap<>();
        map.put("conditionNodes", conditionNodes1);
        map.put("childNodes", childNodes1);
        map.put("nodeUserList", nodeUserList1);
        map.put("conditionList", conditionList1);
        return map;
    }

    /**
     * 获取条件节点
     * @param childNode 节点
     */
    public void getConditionNodes(ChildNode childNode) {
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getType())) {
            childNodes1.add(childNode);
            // ------> 条件节点
            List<ChildNode> conditionNodes = childNode.getConditionNodes();
            if (Objects.nonNull(conditionNodes) && conditionNodes.size() > 0) {
                conditionNodes1.addAll(conditionNodes);
                for (ChildNode childNode2 : conditionNodes) {
                    if (Objects.nonNull(childNode2)) {
                        getConditionNodes(childNode2);
                    }
                }
            }
            if (Objects.nonNull(childNode.getChildNode())) {
                getConditionNodes(childNode.getChildNode());
            }
        }
        if (ChildNodeUtil.NODE_TYPE_CONDITION.equals(childNode.getType())) {
            // ------> 条件
            List<Condition> conditionList = childNode.getConditionList();
            if (Objects.nonNull(conditionList)) {
                conditionList1.addAll(conditionList);
            }
            if (Objects.nonNull(childNode.getChildNode())) {
                getConditionNodes(childNode.getChildNode());
            }
        }

        if (ChildNodeUtil.NODE_TYPE_INITIATOR.equals(childNode.getType()) || ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())) {
            childNodes1.add(childNode);
            // ------> 节点用户
            List<NodeUser> nodeUserList = childNode.getNodeUserList();
            if (Objects.nonNull(nodeUserList)) {
                nodeUserList1.addAll(nodeUserList);
            }
            ChildNode childNode1 = childNode.getChildNode();
            if (Objects.nonNull(childNode1)) {
                getConditionNodes(childNode1);
            }
        }

    }

    /**
     * 判断某节点是否在某个路由中
     * @param routeNode 路由节点
     * @param id 当前节点ID
     * @return Boolean
     */
    public static Boolean idIsInRoute (ChildNode routeNode, String id) {
        if (Objects.nonNull(routeNode.getChildNode())) {
            if (routeNode.getChildNode().getId().equals(id)) {
                return true;
            } else {
                if (Objects.nonNull(routeNode.getConditionNodes()) && routeNode.getConditionNodes().size() != 0) {
                    for ( ChildNode conditionNode : routeNode.getConditionNodes()) {
                        if (Objects.nonNull(conditionNode.getChildNode())) {
                            return idIsInRoute(conditionNode, id);
                        }
                        ChildNode childNode = getRoutingNodeUnderANode(conditionNode);
                        if (Objects.nonNull(childNode)) {
                            return idIsInRoute(childNode, id);
                        }
                    }
                    if (Objects.nonNull(routeNode.getChildNode())) {
                        return idIsInRoute(routeNode.getChildNode(), id);
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取某节点下的第一个路由节点
     * @param childNode 某节点
     * @return ChildNode
     */
    public static ChildNode getRoutingNodeUnderANode(ChildNode childNode) {
        if (Objects.nonNull(childNode.getChildNode())) {
            if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getChildNode().getType())) {
                return childNode.getChildNode();
            } else {
                return getRoutingNodeUnderANode(childNode.getChildNode());
            }
        } else {
            return null;
        }
    }

    /**
     * id是否在节点非路由链条中
     * @param childNode 节点
     * @param id 节点ID
     * @return Boolean
     */
    public static Boolean isInChildNodeNotRouteList (ChildNode childNode, String id) {
        if (childNode.getId().equals(id)) {
            return true;
        } else {
            ChildNode node = childNode.getChildNode();
            if (Objects.nonNull(node)) {
                if (!ChildNodeUtil.NODE_TYPE_ROUTE.equals(node.getType())) {
                    return isInChildNodeNotRouteList(node, id);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * 判断路由是否是节点的直接路由
     * @param routeNode 路由节点
     * @param id 节点ID
     * @return Boolean
     */
    public static Boolean isFirstParentRounte (ChildNode routeNode, String id) {
        Boolean flag = isInChildNodeNotRouteList(routeNode, id);
        if (flag) {
            return true;
        } else {
            List<ChildNode> conditionNodes = routeNode.getConditionNodes();
            boolean flag1 = false;
            for (ChildNode conditionNode : conditionNodes) {
                if (Objects.nonNull(conditionNode.getChildNode())) {
                    if (isInChildNodeNotRouteList(conditionNode, id)) {
                        flag1 = true;
                        break;
                    }
                }
            }
            return flag1;
        }
    }

    /**
     * 解析当前节点
     * @param childNode 节点
     * @return ChildNode
     */
    public static ChildNode getCurrentNode(ChildNode childNode) {
        if (ChildNodeUtil.NODE_TYPE_INITIATOR.equals(childNode.getType()) || ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())) {
            if (childNode.getStatus() != null && childNode.getStatus() == 1) {
                return childNode;
            } else {
                // ------> 个人节点
                ChildNode childNode1 = childNode.getChildNode();
                if (Objects.nonNull(childNode1)) {
                    if (childNode1.getStatus() != null && childNode1.getStatus() == 1) {
                        return childNode1;
                    } else {
                        return getCurrentNode(childNode1);
                    }
                }
            }
        }
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getType())) {
            // ------> 条件节点
            List<ChildNode> conditionNodes = childNode.getConditionNodes();
            if (Objects.nonNull(conditionNodes)) {
                for (ChildNode childNode2 : conditionNodes) {
                    if (Objects.nonNull(childNode2.getChildNode())) {
                        ChildNode childNode3 = getCurrentNode(childNode2.getChildNode());
                        if (Objects.nonNull(childNode3)) {
                            return childNode3;
                        }
                    }
                }
            }
        }
        return null;
    }

    private List<ChildNode> changeStatusChildNodes = new ArrayList<>();

    /**
     * 获取某普通节点的上游路由节点，需要获取其他类型节点的请使用 getRoutingNodeUnderANodeByType
     * @param childNode 一个节点
     * @param rootNode  根节点
     * @return 路由节点
     */
    public static ChildNode getRoutingNodeOfANode(ChildNode childNode, ChildNode rootNode) {
        ChildNode routeNode = getRoutingNodeUnderANode(rootNode);
        if (idIsInRoute(rootNode, childNode.getId())) {
            if (isFirstParentRounte(routeNode, childNode.getId())) {
                return routeNode;
            } else {
                List<ChildNode> conditionNodes = rootNode.getConditionNodes();
                for (ChildNode conditionNode : conditionNodes) {
                    ChildNode childNode1 = getRoutingNodeOfANode(conditionNode, rootNode);
                    if (Objects.nonNull(childNode1)) {
                        return childNode1;
                    }
                }
            }
        }
        return null;
    }

}
