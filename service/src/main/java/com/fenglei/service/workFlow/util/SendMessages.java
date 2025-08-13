package com.fenglei.service.workFlow.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.*;
import org.apache.commons.chain.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
public class SendMessages {
    public static FlowNotice createMessage (ChildNode childNode, Object formData, TableInfo tableInfoDTO, WorkFlowInstantiate workFlowInstantiate, Context context) {
        FlowNotice flowNotice = new FlowNotice();
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(formData));
        String id = (String) data.get("id");
        flowNotice.setNoticeType(childNode.getType());
        flowNotice.setIsRead(false);
        List<FlowUser> flowUsers = ClassConversionTools.castList(context.get("flowUsers"), FlowUser.class);
        List<FlowDepartment> flowDepartments = ClassConversionTools.castList(context.get("flowDepartments"), FlowDepartment.class);
        List<ChildNodeApprovalResult> childNodeApprovalResults = ClassConversionTools.castList(context.get("childNodeApprovalResults"), ChildNodeApprovalResult.class);
        ChildNodeApprovalResult childNodeApprovalResult = null;
        if (Objects.nonNull(context.get("childNodeApprovalResult"))) {
            childNodeApprovalResult = (ChildNodeApprovalResult)context.get("childNodeApprovalResult");
            childNodeApprovalResults = ClassConversionTools.castList(context.get("childNodeApprovalResults"), ChildNodeApprovalResult.class);
            childNodeApprovalResults.add(childNodeApprovalResult);
        }

        if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(childNode.getType())) {
             SendMessages.getReviewerFlowNotice(flowNotice, childNode,
                    flowUsers, workFlowInstantiate,
                    flowDepartments, childNodeApprovalResults,
                    id, tableInfoDTO
            );
        } else if (ChildNodeUtil.NODE_TYPE_CC.equals(childNode.getType())) {
             SendMessages.getCcFlowNotice(flowNotice, childNode, id, tableInfoDTO);
        }
        return flowNotice;
    }

    private static void getReviewerFlowNotice(FlowNotice flowNotice, ChildNode childNode, List<FlowUser> flowUsers, WorkFlowInstantiate workFlowInstantiate,
            List<FlowDepartment> flowDepartments, List<ChildNodeApprovalResult> childNodeApprovalResults, String id, TableInfo tableInfoDTO) {
        FlowDepartment currentFlowDepartment = null;
        flowNotice.setNoticeType(ChildNodeUtil.NUMBER_1);
        List<NodeUser> nodeUsers = childNode.getNodeUserList();
        StringBuilder userIds = new StringBuilder();
        StringBuilder roleIds = new StringBuilder();
         SendMessages.getUserIds(childNode, nodeUsers, flowUsers, workFlowInstantiate, flowDepartments, currentFlowDepartment, childNodeApprovalResults, userIds, roleIds);
        flowNotice.setUserIds(userIds.toString());
        flowNotice.setRoleIds(roleIds.toString());
        flowNotice.setFormId(id);
        String tableInfoName = tableInfoDTO.getTableName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        flowNotice.setNoticeTime(simpleDateFormat.format(new Date()));
        flowNotice.setContent("您有一条" + tableInfoName + "审批消息");
    }
    public static void getUserIds(ChildNode childNode, List<NodeUser> nodeUsers,
                                                 List<FlowUser> flowUsers, WorkFlowInstantiate workFlowInstantiate,
                                                 List<FlowDepartment> flowDepartments, FlowDepartment currentFlowDepartment,
                                                 List<ChildNodeApprovalResult> childNodeApprovalResults,
                                  StringBuilder userIds, StringBuilder roleIds
    ) {
        // 设置类型
        String setType = childNode.getSettype();
        switch (setType) {
            case ChildNodeUtil.NUMBER_1:
                // 1.指定成员
                for (NodeUser nodeUser : nodeUsers) {
                    if (ChildNodeUtil.NUMBER_1.equals(nodeUser.getType())) {
                        if (StringUtils.isNotEmpty(userIds.toString())) {
                            userIds.append(",");
                        }
                        userIds.append(nodeUser.getTargetId());
                    }
                }
                break;
            case ChildNodeUtil.NUMBER_2:
                // 2.主管
                String managerId = null;
                for (FlowUser flowUser : flowUsers) {
                    if (flowUser.getId().equals(workFlowInstantiate.getInitiatorId())) {
                        for (FlowDepartment flowDepartment : flowDepartments) {
                            if (flowDepartment.getId().equals(flowUser.getEmployeeDepartmentId())) {
                                currentFlowDepartment = flowDepartment;
                            }
                        }
                    }
                }
                String directorLevel = childNode.getDirectorLevel();
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
                    userIds.append(managerId);
                }
                break;
            case ChildNodeUtil.NUMBER_3:
                // 3.角色
                for (NodeUser nodeUser : nodeUsers) {
                    if (ChildNodeUtil.NUMBER_2.equals(nodeUser.getType())) {
                        if (StringUtils.isNotEmpty(roleIds.toString())) {
                            roleIds.append(",");
                        }
                        roleIds.append(nodeUser.getTargetId());
                    }
                }
                break;
            case ChildNodeUtil.NUMBER_4:
                // 4.发起人自选
                for (NodeUser nodeUser : nodeUsers) {
                    if (ChildNodeUtil.NUMBER_1.equals(nodeUser.getType())) {
                        if (StringUtils.isNotEmpty(userIds.toString())) {
                            userIds.append(",");
                        }
                        userIds.append(nodeUser.getTargetId());
                    } else if (ChildNodeUtil.NUMBER_2.equals(nodeUser.getType())) {
                        if (StringUtils.isNotEmpty(roleIds.toString())) {
                            roleIds.append(",");
                        }
                        roleIds.append(nodeUser.getTargetId());
                    }
                }
                break;
            case ChildNodeUtil.NUMBER_5:
                // 5.发起人自己
                userIds.append(workFlowInstantiate.getInitiatorId());
                break;
            case ChildNodeUtil.NUMBER_7:
                 SendMessages.continuousMultiLevelSupervisor(flowUsers, workFlowInstantiate, flowDepartments, currentFlowDepartment, childNode, childNodeApprovalResults, userIds);
                break;
            default:
                break;
        }

    }

    public static void continuousMultiLevelSupervisor(List<FlowUser> flowUsers, WorkFlowInstantiate workFlowInstantiate, List<FlowDepartment> flowDepartments, FlowDepartment currentFlowDepartment,
                                                      ChildNode childNode, List<ChildNodeApprovalResult> childNodeApprovalResults, StringBuilder userIds) {
        // 7.连续多级主管
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
                childNode.getExamineEndDirectorLevel(),
                currentFlowDepartment,
                resultFlowDepartments,
                ChildNodeUtil.NUMBER_1.equals(childNode.getExamineEndDirectorLevel())
        );
        if (resultFlowDepartments.size() > ChildNodeUtil.NUMBER_INT_0) {
            List<String> userIdList = childNodeApprovalResults.stream().map(ChildNodeApprovalResult::getUserId).collect(Collectors.toList());
            List<String> managerIds = resultFlowDepartments.stream().map(FlowDepartment::getManagerId).collect(Collectors.toList());
            managerIds = managerIds.stream().filter(s -> {
                boolean flag = true;
                for (String sid : userIdList) {
                    if (s.equals(sid)) {
                        flag = false;
                        break;
                    }
                }
                return flag;
            }).collect(Collectors.toList());
            if (managerIds.size() > ChildNodeUtil.NUMBER_INT_0) {
                userIds.append(managerIds.get(ChildNodeUtil.NUMBER_INT_0));
            }
        } else if (ChildNodeUtil.NUMBER_2.equals(childNode.getNoHanderAction())) {
            for (FlowUser flowUser : flowUsers) {
                if (flowUser.getIsAuditManager()) {
                    userIds.append(flowUser.getId());
                }
            }
        }
    }

    public static void getCcFlowNotice(FlowNotice flowNotice, ChildNode childNode, String id, TableInfo tableInfoDTO) {
        flowNotice.setNoticeType(ChildNodeUtil.NUMBER_2);
        List<NodeUser> nodeUsers = childNode.getNodeUserList();
        StringBuilder userIds = new StringBuilder();
        StringBuilder roleIds = new StringBuilder();
        for (NodeUser nodeUser : nodeUsers) {
            if (ChildNodeUtil.NUMBER_1.equals(nodeUser.getType())) {
                if (StringUtils.isNotEmpty(userIds.toString())) {
                    userIds.append(",");
                }
                userIds.append(nodeUser.getTargetId());
            } else if (ChildNodeUtil.NUMBER_2.equals(nodeUser.getType())) {
                if (!StringUtils.isNotEmpty(roleIds.toString())) {
                    roleIds.append(",");
                }
                roleIds.append(nodeUser.getTargetId());
            }
        }
        flowNotice.setUserIds(userIds.toString());
        flowNotice.setRoleIds(roleIds.toString());
        flowNotice.setFormId(id);
        String tableInfoName = tableInfoDTO.getTableName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        flowNotice.setNoticeTime(simpleDateFormat.format(new Date()));
        flowNotice.setContent("您有一条" + tableInfoName + "抄送消息");
    }

}
