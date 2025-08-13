package com.fenglei.service.workFlow.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.model.workFlow.entity.NodeUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzy
 */
public class ConditionalJudgment {

    public static Boolean domain(ChildNode childNode, String userId, Object formData) throws Exception{
        if (ChildNodeUtil.NODE_TYPE_CONDITION.equals(childNode.getType())) {
            List<Condition> conditionList = childNode.getConditionList();
            List<NodeUser> nodeUsers = childNode.getNodeUserList();
            List<Condition> trueConditions = new ArrayList<>();
            for (Condition condition : conditionList) {
                if (ChildNodeUtil.NUMBER_1.equals(condition.getType())) {
                    boolean flag = false;
                    for (NodeUser nodeUser : nodeUsers) {
                        if (userId.equals(nodeUser.getTargetId())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        trueConditions.add(condition);
                    }
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
}
