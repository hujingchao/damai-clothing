package com.fenglei.service.workFlow.operation;


import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.NodeUser;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import com.fenglei.service.workFlow.util.FlowDepartmentUtils;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 处理审批
 * @author yzy
 */

public class ProcessingAuditOperation implements Command {

    @Override
    public boolean execute(Context context) throws Exception {
        ChildNode currentNode = (ChildNode)context.get("currentNode");
        List<FlowUser> flowUsers = ClassConversionTools.castList(context.get("flowUsers"), FlowUser.class);
        List<FlowDepartment> flowDepartments = ClassConversionTools.castList(context.get("flowDepartments"), FlowDepartment.class);
        ChildNodeApprovalResult childNodeApprovalResult = (ChildNodeApprovalResult)context.get("childNodeApprovalResult");
        List<ChildNodeApprovalResult> childNodeApprovalResults = ClassConversionTools.castList(context.get("childNodeApprovalResults"), ChildNodeApprovalResult.class);
        WorkFlowInstantiate workFlowInstantiate = (WorkFlowInstantiate)context.get("workFlowInstantiate");
        String userId = (String) context.get("userId");
        String setType = currentNode.getSettype();
        switch (setType) {
            case ChildNodeUtil.NUMBER_1:
                // 1.指定成员
                designatedMember(context,currentNode,userId,childNodeApprovalResult,childNodeApprovalResults,workFlowInstantiate);
                break;
            case ChildNodeUtil.NUMBER_2:
                // 2.主管
                personInCharge(context,flowUsers,userId,flowDepartments,currentNode, workFlowInstantiate, childNodeApprovalResult);
                break;
            case ChildNodeUtil.NUMBER_3:
                // 3.角色
                roleApproval(flowUsers,context,userId,currentNode,workFlowInstantiate,childNodeApprovalResult);
                break;
            case ChildNodeUtil.NUMBER_4:
                // 4.发起人自选
                sponsorChoice(currentNode,context,userId,workFlowInstantiate,childNodeApprovalResult);
                break;
            case ChildNodeUtil.NUMBER_5:
                // 5.发起人自己
                sponsorHimself(currentNode,context,userId,workFlowInstantiate,childNodeApprovalResult);
                break;
            case ChildNodeUtil.NUMBER_7:
                // 7.连续多级主管
                continuousMultiLevelSupervisor(currentNode,context,userId,workFlowInstantiate,childNodeApprovalResult,flowDepartments,flowUsers,childNodeApprovalResults);
                break;
            default:
                break;
        }

        return false;
    }

    /**
     * 指定成员
     * @param context 内容
     * @param currentNode 当前节点
     * @param userId 用户ID
     * @param childNodeApprovalResult 节点审批结果
     * @param childNodeApprovalResults 此节点审批结果列表
     * @param workFlowInstantiate 工作流实例
     */
    @SuppressWarnings("unchecked")
    private void designatedMember(Context context, ChildNode currentNode, String userId,
                                  ChildNodeApprovalResult childNodeApprovalResult,
                                  List<ChildNodeApprovalResult> childNodeApprovalResults,
                                  WorkFlowInstantiate workFlowInstantiate) {
        List<NodeUser> nodeUsers = currentNode.getNodeUserList();
        boolean flag = false;
        for (NodeUser nodeUser : nodeUsers) {
            if (nodeUser.getTargetId().equals(userId)) {
                flag = true;
                break;
            }
        }
        childNodeApprovalResults.add(childNodeApprovalResult);
        if (flag) {
            if (childNodeApprovalResult.getResult()) {
                if (ChildNodeUtil.NUMBER_2.equals(currentNode.getExamineMode())) {
                    // 审批通过
                    if (childNodeApprovalResults.size() == nodeUsers.size()) {
                        currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        context.put("currentNode", currentNode);
                    }
                } else if (ChildNodeUtil.NUMBER_1.equals(currentNode.getExamineMode())) {
                    // 审批通过
                    if (childNodeApprovalResults.size() == nodeUsers.size()) {
                        currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        context.put("currentNode", currentNode);
                    }
                }
            } else {
                // 驳回
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                if (workFlowInstantiate.getWhenRejectedWhetherToDirectlyVoidTheProcess()) {
                    workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
                    context.put("workFlowInstantiate", workFlowInstantiate);
                }
            }
        }
    }

    /**
     * 主管审批
     * @param context 内容
     * @param flowUsers 用户列表
     * @param userId 用户ID
     * @param flowDepartments 部门列表
     * @param currentNode 当前节点
     */
    @SuppressWarnings("unchecked")
    private void personInCharge(Context context, List<FlowUser> flowUsers,
                                String userId, List<FlowDepartment> flowDepartments,
                                ChildNode currentNode, WorkFlowInstantiate workFlowInstantiate,
                                ChildNodeApprovalResult childNodeApprovalResult) {
        String managerId = null;
        FlowDepartment currentFlowDepartment = null;
        for (FlowUser flowUser : flowUsers) {
            if (flowUser.getId().equals(workFlowInstantiate.getInitiatorId())) {
                for (FlowDepartment flowDepartment : flowDepartments) {
                    if (flowDepartment.getId().equals(flowUser.getEmployeeDepartmentId())) {
                        currentFlowDepartment = flowDepartment;
                    }
                }
            }
        }
        String directorLevel = currentNode.getDirectorLevel();
        if (Objects.nonNull(currentFlowDepartment)) {
            FlowDepartment finalFlowDepartment;
            if (ChildNodeUtil.NUMBER_1.equals(directorLevel)) {
                finalFlowDepartment = currentFlowDepartment;
            } else {
                finalFlowDepartment = FlowDepartmentUtils.getCorrespondingLevelFlowDepartment(flowDepartments, directorLevel, currentFlowDepartment);
            }
            if (Objects.nonNull(finalFlowDepartment)) {
                managerId = finalFlowDepartment.getManagerId();
            }
        }
        if (StringUtils.isNotEmpty(managerId)) {
            if (managerId.equals(userId)) {
                if (childNodeApprovalResult.getResult()) {
                    currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                } else {
                    currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                    if (workFlowInstantiate.getWhenRejectedWhetherToDirectlyVoidTheProcess()) {
                        workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        context.put("workFlowInstantiate", workFlowInstantiate);
                    }
                }
                context.put("currentNode", currentNode);
            }
        } else {
            for (FlowUser flowUser : flowUsers) {
                if (flowUser.getIsAuditManager()) {
                    if (flowUser.getId().equals(userId)) {
                        currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        context.put("currentNode", currentNode);
                    }
                }
            }
        }
    }


    /**
     * 角色审批
     * @param flowUsers 用户列表
     * @param context 内容上下文
     * @param userId 审批用户ID
     * @param currentNode 当前节点
     * @param workFlowInstantiate 流程实例
     * @param childNodeApprovalResult 审批结果
     */
    @SuppressWarnings("unchecked")
    private void roleApproval(List<FlowUser> flowUsers, Context context,
                              String userId, ChildNode currentNode,
                              WorkFlowInstantiate workFlowInstantiate,
                              ChildNodeApprovalResult childNodeApprovalResult
    ) {
        for (FlowUser flowUser : flowUsers) {
            boolean flag = false;
            if (flowUser.getId().equals(userId)) {
                List<String> roleIds = flowUser.getRoleIds();
                if (Objects.nonNull(roleIds) && roleIds.size() > ChildNodeUtil.NUMBER_INT_0) {
                    List<NodeUser> nodeUsers = (currentNode.getNodeUserList()).stream().filter(s -> "2".equals(s.getType())).collect(Collectors.toList());
                    for (NodeUser nodeUser : nodeUsers) {
                        if (roleIds.contains(nodeUser.getTargetId())) {
                            flag = true;
                            break;
                        }
                    }
                }
            }

            if (flag) {
                if (childNodeApprovalResult.getResult()) {
                    currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                } else {
                    currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                    if (workFlowInstantiate.getWhenRejectedWhetherToDirectlyVoidTheProcess()) {
                        workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
                        context.put("workFlowInstantiate", workFlowInstantiate);
                    }
                }
                context.put("currentNode", currentNode);
            }
        }

    }

    /**
     * 发起人自选
     * @param currentNode 当前节点
     * @param context 内容
     * @param userId 用户ID
     */
    @SuppressWarnings("unchecked")
    private void sponsorChoice(ChildNode currentNode, Context context, String userId,
                               WorkFlowInstantiate workFlowInstantiate,
                               ChildNodeApprovalResult childNodeApprovalResult
    ) {
        List<NodeUser> nodeUsers = currentNode.getNodeUserList();
        boolean flag = false;
        for (NodeUser nodeUser : nodeUsers) {
            if (nodeUser.getTargetId().equals(userId)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            if (childNodeApprovalResult.getResult()) {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
            } else {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                if (workFlowInstantiate.getWhenRejectedWhetherToDirectlyVoidTheProcess()) {
                    workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
                    context.put("workFlowInstantiate", workFlowInstantiate);
                }
            }
            context.put("currentNode", currentNode);
        }
    }

    /**
     * 发起人自己
     * @param currentNode 当前节点
     * @param context 上下文
     * @param userId 用户ID
     * @param workFlowInstantiate 流程实例
     * @param childNodeApprovalResult 节点审批结果
     */
    @SuppressWarnings("unchecked")
    private void sponsorHimself(ChildNode currentNode, Context context, String userId,
                                WorkFlowInstantiate workFlowInstantiate,
                                ChildNodeApprovalResult childNodeApprovalResult
    ) {
        if (workFlowInstantiate.getInitiatorId().equals(userId)) {
            if (childNodeApprovalResult.getResult()) {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
            } else {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                if (workFlowInstantiate.getWhenRejectedWhetherToDirectlyVoidTheProcess()) {
                    workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_2);
                    context.put("workFlowInstantiate", workFlowInstantiate);
                }
            }
            context.put("currentNode", currentNode);
        }
    }

    /**
     * 连续多级主管审批
     * @param currentNode 当前节点
     * @param context 上下文
     * @param userId 用户ID
     * @param workFlowInstantiate 流程实例
     * @param childNodeApprovalResult 节点审批条件
     * @param flowDepartments 流程部门
     * @param flowUsers 流程用户
     * @param childNodeApprovalResults 所有节点审批调节
     */
    @SuppressWarnings("unchecked")
    private void continuousMultiLevelSupervisor(
            ChildNode currentNode, Context context, String userId,
            WorkFlowInstantiate workFlowInstantiate,
            ChildNodeApprovalResult childNodeApprovalResult,
            List<FlowDepartment> flowDepartments,
            List<FlowUser> flowUsers,
            List<ChildNodeApprovalResult> childNodeApprovalResults
    ) {
        FlowDepartment currentFlowDepartment = null;
        for (FlowUser flowUser : flowUsers) {
            if (flowUser.getId().equals(workFlowInstantiate.getInitiatorId())) {
                for (FlowDepartment flowDepartment : flowDepartments) {
                    if (flowDepartment.getId().equals(flowUser.getEmployeeDepartmentId())) {
                        currentFlowDepartment = flowDepartment;
                    }
                }
            }
        }
        List<FlowDepartment> resultFlowDepartments = new ArrayList<>();

        resultFlowDepartments = FlowDepartmentUtils.getAllDepartmentsAtTheCorrespondingSupervisorLevel(flowDepartments,
                currentNode.getExamineEndDirectorLevel(),
                currentFlowDepartment,
                resultFlowDepartments,
                ChildNodeUtil.NUMBER_1.equals(currentNode.getExamineEndDirectorLevel())
        );
        if (resultFlowDepartments.size() > ChildNodeUtil.NUMBER_INT_0) {
            if (childNodeApprovalResult.getResult()) {
                List<String> userIds = childNodeApprovalResults.stream().map(ChildNodeApprovalResult::getUserId).collect(Collectors.toList());
                List<String> managerIds = resultFlowDepartments.stream().map(FlowDepartment::getManagerId).collect(Collectors.toList());
                if ((userIds.size() + ChildNodeUtil.NUMBER_INT_1) == managerIds.size()) {
                    boolean flag = false;
                    for (String managerId : managerIds) {
                        if (managerId.equals(userId)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                    }
                }
            } else {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
            }
            context.put("currentNode", currentNode);
        } else {
            // 审批人为空时 1.自动审批通过/不允许发起 2.转交给审核管理员
            if (ChildNodeUtil.NUMBER_1.equals(currentNode.getNoHanderAction())) {
                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
            } else  if (ChildNodeUtil.NUMBER_2.equals(currentNode.getNoHanderAction())) {
                for (FlowUser flowUser : flowUsers) {
                    if (flowUser.getIsAuditManager()) {
                        if (userId.equals(flowUser.getId())) {
                            if (childNodeApprovalResult.getResult()) {
                                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
                            } else {
                                currentNode.setStatus(ChildNodeUtil.NUMBER_INT_3);
                            }
                        }
                    }
                }
                context.put("currentNode", currentNode);
            }
        }
    }

}
