package com.fenglei.service.workFlow.util;

import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.model.workFlow.entity.NodeUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzy
 */
public class FormatToTree {

    /** 组装流程
     * @param childNodes 数据库里面获取到的全量菜单列表
     * @return
     * parseTree<br>
     */
    public static List<ChildNode> parseTree(List<ChildNode> childNodes, List<Condition> conditions, List<NodeUser> nodeUsers) {
        List<ChildNode> result = new ArrayList<>();

        // 1、获取第一级节点
        for (ChildNode childNode : childNodes) {
            if ((null == childNode.getPid() || "0".equals(childNode.getPid())) && StringUtils.isEmpty(childNode.getChildNodeId())) {
                List<Condition> conditionList = new ArrayList<>();
                for (Condition condition : conditions) {
                    if (childNode.getId().equals(condition.getChildNodeId())) {
                        conditionList.add(condition);
                    }
                }
                childNode.setConditionList(conditionList);
                List<NodeUser> nodeUserList = new ArrayList<>();
                for (NodeUser nodeUser : nodeUsers) {
                    if (childNode.getId().equals(nodeUser.getChildNodeId())) {
                        nodeUserList.add(nodeUser);
                    }
                }
                childNode.setNodeUserList(nodeUserList);
                result.add(childNode);
            }
        }
        // 2、递归获取子节点
        for (ChildNode parent : result) {
            parent = recursiveTree(parent, childNodes, conditions, nodeUsers);
        }
        return result;
    }

    public static ChildNode recursiveTree(ChildNode parent, List<ChildNode> list, List<Condition> conditions, List<NodeUser> nodeUsers) {
        for (ChildNode childNode : list) {
            if (parent.getId().equals(childNode.getPid())) {
                childNode = recursiveTree(childNode, list, conditions, nodeUsers);
                List<Condition> newConditions = getConditions(conditions, childNode.getId());
                List<NodeUser> newNodeUsers = getNodeUsers(nodeUsers, childNode.getId());
                childNode.setConditionList(newConditions);
                childNode.setNodeUserList(newNodeUsers);
                parent.getConditionNodes().add(childNode);
            }
            if (parent.getId().equals(childNode.getChildNodeId())) {
                childNode = recursiveTree(childNode, list, conditions, nodeUsers);
                List<Condition> newConditions = getConditions(conditions, childNode.getId());
                List<NodeUser> newNodeUsers = getNodeUsers(nodeUsers, childNode.getId());
                childNode.setConditionList(newConditions);
                childNode.setNodeUserList(newNodeUsers);
                parent.setChildNode(childNode);
            }
        }
        return parent;
    }

    public static List<Condition> getConditions(List<Condition> conditions, String childNodeId) {
        List<Condition> conditionList = new ArrayList<>();
        for (Condition condition : conditions) {
            if (childNodeId.equals(condition.getChildNodeId())) {
                conditionList.add(condition);
            }
        }
        return conditionList;
    }

    public static List<NodeUser> getNodeUsers(List<NodeUser> nodeUsers, String childNodeId) {
        List<NodeUser> nodeUserList = new ArrayList<>();
        for (NodeUser nodeUser : nodeUsers) {
            if (childNodeId.equals(nodeUser.getChildNodeId())) {
                nodeUserList.add(nodeUser);
            }
        }
        return nodeUserList;
    }


    /** 组装流程
     * @param childNodes 数据库里面获取到的全量菜单列表
     * @return
     * parseTree<br>
     */
    public static List<ChildNode> parseTreeByPid(List<ChildNode> childNodes, List<Condition> conditions, List<NodeUser> nodeUsers, String pid) {
        List<ChildNode> result = new ArrayList<>();
        // 1、获取第一级节点
        for (ChildNode childNode : childNodes) {
            if ( pid.equals(childNode.getId())) {
                List<Condition> conditionList = new ArrayList<>();
                for (Condition condition : conditions) {
                    if (childNode.getId().equals(condition.getChildNodeId())) {
                        conditionList.add(condition);
                    }
                }
                childNode.setConditionList(conditionList);
                List<NodeUser> nodeUserList = new ArrayList<>();
                for (NodeUser nodeUser : nodeUsers) {
                    if (childNode.getId().equals(nodeUser.getChildNodeId())) {
                        nodeUserList.add(nodeUser);
                    }
                }
                childNode.setNodeUserList(nodeUserList);
                result.add(childNode);
            }
        }
        // 2、递归获取子节点
        for (ChildNode parent : result) {
            parent = recursiveTree(parent, childNodes, conditions, nodeUsers);
        }
        return result;
    }

}
