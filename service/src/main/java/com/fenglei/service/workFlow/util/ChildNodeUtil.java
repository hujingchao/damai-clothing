package com.fenglei.service.workFlow.util;

import com.fenglei.model.workFlow.entity.ChildNode;

import java.util.List;
import java.util.Objects;

/**
 * @author yzy
 */
public class ChildNodeUtil {

    /**
     * 发起人类别
     */
    public final static String NODE_TYPE_INITIATOR = "0";
    /**
     * 审核人类别
     */
    public final static String NODE_TYPE_REVIEWER = "1";
    /**
     * 抄送类别
     */
    public final static String NODE_TYPE_CC = "2";
    /**
     * 条件类别
     */
    public final static String NODE_TYPE_CONDITION = "3";
    /**
     * 路由类别
     */
    public final static String NODE_TYPE_ROUTE = "4";
    /**
     * String 类型 状态字符
     */
    public static final String NUMBER_0 = "0";
    public static final String NUMBER_1 = "1";
    public static final String NUMBER_2 = "2";
    public static final String NUMBER_3 = "3";
    public static final String NUMBER_4 = "4";
    public static final String NUMBER_5 = "5";
    public static final String NUMBER_6 = "6";
    public static final String NUMBER_7 = "7";
    public static final String NUMBER_8 = "8";
    public static final String NUMBER_9 = "9";
    public static final String NUMBER_10 = "10";
    /**
     * int 类型 状态字符
     */
    public static final int NUMBER_INT_0 = 0;
    public static final int NUMBER_INT_1 = 1;
    public static final int NUMBER_INT_2 = 2;
    public static final int NUMBER_INT_3 = 3;
    public static final int NUMBER_INT_4 = 4;
    public static final int NUMBER_INT_5 = 5;
    public static final int NUMBER_INT_6 = 6;
    public static final int NUMBER_INT_7 = 7;

    /**
     * 表单类型-外部
     */
    public static final String FLOW_TYPE_OUT = "外部表单";
    /**
     * 表单类型-内部
     */
    public static final String FLOW_TYPE_IN = "内部表单";

    /**
     *  向下获取下一个节点
     * @param childNode 节点
     * @param formData 单据数据
     * @param userId 用户ID
     * @return ChildNode
     * @throws Exception 错误
     */
   public static ChildNode getNextNode(ChildNode childNode, Object formData, String userId) throws Exception{
       if ( ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getType())) {
           if (Objects.nonNull(childNode.getChildNode())) {
               if (childNode.getChildNode().getStatus() ==  ChildNodeUtil.NUMBER_INT_0) {
                  ChildNodeTreeUtil childNodeTreeUtil =new  ChildNodeTreeUtil();
                   List<ChildNode> childNodes = childNodeTreeUtil.getSplitNodeChildNodes(childNode.getChildNode(), "1", formData, userId, 0, true);
                   if (childNodes.size() ==  ChildNodeUtil.NUMBER_INT_1) {
                       return childNodes.get( ChildNodeUtil.NUMBER_INT_0);
                   } else {
                       return childNode.getChildNode();
                   }
               } else if (childNode.getChildNode().getStatus() ==  ChildNodeUtil.NUMBER_INT_1){
                   return childNode.getChildNode();
               }
           } else {
               List<ChildNode> conditionNodes = childNode.getConditionNodes();
               if (conditionNodes.size() >  ChildNodeUtil.NUMBER_INT_0) {
                   ChildNode nextConditionNode = null;
                   for (ChildNode conditionNode : conditionNodes) {
                       if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                           nextConditionNode = conditionNode;
                       }
                   }
                   if (Objects.isNull(nextConditionNode)) {
                       for (ChildNode conditionNode : conditionNodes) {
                           if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() ==  ChildNodeUtil.NUMBER_INT_0) {
                               nextConditionNode = conditionNode;
                           }
                       }
                   }
                   if (Objects.nonNull(nextConditionNode)) {
                       return getNextNode(nextConditionNode, formData, userId);
                   }
               }
           }
       } else if ( ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
           if (childNode.getStatus() ==  ChildNodeUtil.NUMBER_INT_0 || childNode.getStatus() ==  ChildNodeUtil.NUMBER_INT_1) {
               return childNode;
           }
       } else if ( ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType()) &&  ChildNodeUtil.NODE_TYPE_CONDITION.equals(childNode.getType())) {
           if (Objects.nonNull(childNode.getChildNode())) {
                return getNextNode(childNode, formData, userId);
           }
       }
       return null;
   }

    /**
     *  向下获取下一个节点，包含抄送节点
     * @param childNode 节点
     * @param formData 单据数据
     * @param userId 用户ID
     * @return ChildNode
     * @throws Exception 错误
     */
    public static ChildNode getNextNodeIncludeCc(ChildNode childNode, Object formData, String userId) throws Exception{
        if ( ChildNodeUtil.NODE_TYPE_ROUTE.equals(childNode.getType())) {
            if (Objects.nonNull(childNode.getChildNode())) {
                if (childNode.getChildNode().getStatus() ==  ChildNodeUtil.NUMBER_INT_0) {
                    ChildNodeTreeUtil childNodeTreeUtil = new ChildNodeTreeUtil();
                    List<ChildNode> childNodes = childNodeTreeUtil.getSplitNodeChildNodes(childNode.getChildNode(),  ChildNodeUtil.NUMBER_1, formData, userId,  ChildNodeUtil.NUMBER_INT_0, true);
                    if (childNodes.size() ==  ChildNodeUtil.NUMBER_INT_1) {
                        return childNodes.get( ChildNodeUtil.NUMBER_INT_0);
                    } else {
                        return childNode.getChildNode();
                    }
                } else if (childNode.getChildNode().getStatus() ==  ChildNodeUtil.NUMBER_INT_1){
                    return childNode.getChildNode();
                }
            } else {
                List<ChildNode> conditionNodes = childNode.getConditionNodes();
                if (conditionNodes.size() >  ChildNodeUtil.NUMBER_INT_0) {
                    ChildNode nextConditionNode = null;
                    for (ChildNode conditionNode : conditionNodes) {
                        if (ConditionalJudgment.domain(conditionNode, userId, formData)) {
                            nextConditionNode = conditionNode;
                        }
                    }
                    if (Objects.isNull(nextConditionNode)) {
                        for (ChildNode conditionNode : conditionNodes) {
                            if (Objects.isNull(conditionNode.getConditionList()) || conditionNode.getConditionList().size() ==  ChildNodeUtil.NUMBER_INT_0) {
                                nextConditionNode = conditionNode;
                            }
                        }
                    }
                    if (Objects.nonNull(nextConditionNode)) {
                        return getNextNode(nextConditionNode, formData, userId);
                    }
                }
            }
        } else if ( ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType()) &&  ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())) {
            if (childNode.getStatus() ==  ChildNodeUtil.NUMBER_INT_0 || childNode.getStatus() ==  ChildNodeUtil.NUMBER_INT_1) {
                return childNode;
            }
        } else if ( ChildNodeUtil.NODE_TYPE_CONDITION.equals(childNode.getType())) {
            if (Objects.nonNull(childNode.getChildNode())) {
                return getNextNode(childNode, formData, userId);
            }
        }
        return null;
    }
}
