package com.fenglei.service.workFlow.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.Condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yzy
 */
public class ProcessRoutingUtil {

    public static List<ChildNode> getProcessChain(ChildNode rootNode, Object formData) throws Exception{
        List<ChildNode> childNodes = new ArrayList<>();
        childNodes.add(rootNode);
        if (Objects.nonNull(rootNode.getChildNode())) {
             putRoutingChildNode(childNodes, rootNode, formData, rootNode.getChildNode());
        }
        return childNodes;
    }

    private static void putRoutingChildNode(List<ChildNode> childNodes, ChildNode rootNode, Object formData, ChildNode currentNode) throws Exception{
        // 4.路由
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(currentNode.getType())) {
            List<ChildNode> conditionNodes = currentNode.getConditionNodes();
            boolean flag = true;
            for (ChildNode conditionNode : conditionNodes) {
                if (conditionNode.getConditionList().size() > 0) {
                    if (domain(conditionNode, formData)) {
                        if (Objects.nonNull(conditionNode.getChildNode())) {
                            putRoutingChildNode( childNodes, rootNode, formData, conditionNode.getChildNode());
                        }
                        flag = false;
                    }
                }
            }

            if (flag) {
                for (ChildNode conditionNode : conditionNodes) {
                    if (conditionNode.getConditionList().size() == 0) {
                        if (Objects.nonNull(conditionNode.getChildNode())) {
                            putRoutingChildNode( childNodes, rootNode, formData, conditionNode.getChildNode());
                        }
                    }
                }
            }
            if (Objects.nonNull(currentNode.getChildNode())) {
                putRoutingChildNode( childNodes, rootNode, formData, currentNode.getChildNode());
            }

        } else if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(currentNode.getType()) || ChildNodeUtil.NODE_TYPE_CC.equals(currentNode.getType())) {
            childNodes.add(currentNode);
            if (Objects.nonNull(currentNode.getChildNode())) {
                putRoutingChildNode( childNodes, rootNode, formData, currentNode.getChildNode());
            }
        }

    }

    public static Boolean domain(ChildNode childNode, Object formData) throws Exception{
        if (ChildNodeUtil.NODE_TYPE_CONDITION.equals(childNode.getType())) {
            List<Condition> conditionList = childNode.getConditionList();
            List<Condition> trueConditions = new ArrayList<>();
            for (Condition condition : conditionList) {
                if (ChildNodeUtil.NUMBER_1.equals(condition.getType())) {
                    trueConditions.add(condition);
                }
                if (ChildNodeUtil.NUMBER_2.equals(condition.getType())) {
                    // 判断自定义条件
                    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(formData));
                    Object data = jsonObject.get(condition.getColumnDbname());
                    StringBuilder content = new StringBuilder();
                    String columnType = condition.getColumnType();
                    if ("Number".equals(columnType) || "Double".equals(columnType)) {
                        if (ChildNodeUtil.NUMBER_1.equals(condition.getOptType())
                                || ChildNodeUtil.NUMBER_2.equals(condition.getOptType())
                                || ChildNodeUtil.NUMBER_3.equals(condition.getOptType())
                                || ChildNodeUtil.NUMBER_4.equals(condition.getOptType())
                                || ChildNodeUtil.NUMBER_5.equals(condition.getOptType())
                        ) {
                            content.append(data.toString());
                            switch (condition.getOptType()) {
                                case ChildNodeUtil.NUMBER_1:
                                    content.append("<");
                                    break;
                                case ChildNodeUtil.NUMBER_2:
                                    content.append(">");
                                    break;
                                case ChildNodeUtil.NUMBER_3:
                                    content.append("<=");
                                    break;
                                case ChildNodeUtil.NUMBER_4:
                                    content.append("==");
                                    break;
                                case ChildNodeUtil.NUMBER_5:
                                    content.append(">=");
                                default:
                                    break;
                            }
                            content.append(condition.getZdy1());
                        } else if (ChildNodeUtil.NUMBER_6.equals(condition.getOptType())) {
                            content.append(condition.getOpt1());
                            content.append(condition.getZdy1());
                            content.append(data.toString());
                            content.append(condition.getOpt2());
                            content.append(condition.getZdy2());
                        }
                    } else if ("String".equals(columnType) || "Boolean".equals(columnType)) {
                        content.append("\"");
                        content.append(data.toString());
                        content.append("\"");
                        content.append(condition.getOpt1());
                        content.append("\"");
                        content.append(condition.getFixedDownBoxValue());
                        content.append("\"");
                    }

                    // 对公式进行逻辑运算
                    Boolean result = FormulaCalculationUtil.logicOperation(content.toString());
                    if (result) {
                        trueConditions.add(condition);
                    }
                }
            }
            return trueConditions.size() == conditionList.size();
        }
        return false;
    }

    /**
     * 获取下个抄送节点
     * @param childNode 原节点
     * @param rootChildNode 根节点
     * @return ChildNode 抄送节点
     * @throws Exception 错误
     */
    public static ChildNode getNextCcChildNode(ChildNode childNode, ChildNode rootChildNode) throws Exception{
        AnalysisJson analysisJson = new AnalysisJson();
        Map<String, Object> map = analysisJson.getChildNodeToList(rootChildNode);
        List<ChildNode> childNodeList = ClassConversionTools.castList(map.get("childNodes1"), ChildNode.class);
        ChildNode nextCcChildNode = null;
        if (childNodeList.size() > 0) {
            for (int i = 0; i < childNodeList.size(); i++) {
                if (childNodeList.get(i).getId().equals(childNode.getId()) && (i+2) <= childNodeList.size() ) {
                    if (ChildNodeUtil.NODE_TYPE_CC.equals(childNodeList.get(i+1).getType())) {
                        nextCcChildNode = childNodeList.get(i+1);
                    }
                }
            }
        }
        return nextCcChildNode;
    }

    public static List<ChildNode> getCcChildNodes(ChildNode rootNode, Object formData) throws Exception{
        List<ChildNode> childNodeList = new ArrayList<>();
        toAddCcChildNode(childNodeList, rootNode, formData);
        return childNodeList;
    }

    public static void toAddCcChildNode(List<ChildNode> childNodeList, ChildNode rootNode, Object formData) throws Exception{
        if (ChildNodeUtil.NODE_TYPE_ROUTE.equals(rootNode.getType())) {
            List<ChildNode> conditionNodes = rootNode.getConditionNodes();
            boolean flag = true;
            for (ChildNode conditionNode : conditionNodes) {
                if (Objects.nonNull(conditionNode.getConditionList()) && conditionNode.getConditionList().size() > 0) {
                    if ( ProcessRoutingUtil.domain(conditionNode, formData)) {
                        if (Objects.nonNull(conditionNode.getChildNode())) {
                            toAddCcChildNode(childNodeList, conditionNode.getChildNode(), formData);
                        }
                        flag = false;
                    }
                }
            }
            if (flag) {
                for (ChildNode conditionNode : conditionNodes) {
                    if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() == 0) {
                        toAddCcChildNode(childNodeList, conditionNode.getChildNode(), formData);
                    }
                }
            }
            if (Objects.nonNull(rootNode.getChildNode())) {
                toAddCcChildNode(childNodeList, rootNode.getChildNode(), formData);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_INITIATOR.equals(rootNode.getType())) {
            if (Objects.nonNull(rootNode.getChildNode())) {
                toAddCcChildNode(childNodeList, rootNode.getChildNode(), formData);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(rootNode.getType())) {
            if (Objects.nonNull(rootNode.getChildNode())) {
                toAddCcChildNode(childNodeList, rootNode.getChildNode(), formData);
            }
        }
        if (ChildNodeUtil.NODE_TYPE_CC.equals(rootNode.getType())) {
            childNodeList.add(rootNode);
            if (Objects.nonNull(rootNode.getChildNode())) {
                toAddCcChildNode(childNodeList, rootNode.getChildNode(), formData);
            }
        }
    }

}
