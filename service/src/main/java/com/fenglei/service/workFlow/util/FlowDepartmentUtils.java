package com.fenglei.service.workFlow.util;

import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.dto.FlowDepartment;

import java.util.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zjn
 * \* Date: 2021/6/1
 * \* Time: 14:43
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class FlowDepartmentUtils {

    /**
     * 获取对应主管级别的部门
     * @param flowDepartments 部门列表
     * @param directorLevel 主管级别
     * @param currentFlowDepartment 当前节点
     * @return FlowDepartment
     */
    public static FlowDepartment getCorrespondingLevelFlowDepartment(List<FlowDepartment> flowDepartments, String directorLevel, FlowDepartment currentFlowDepartment) {
        int i = Integer.parseInt(directorLevel) - 1;
        FlowDepartment finalFlowDepartment = null;
        for (FlowDepartment flowDepartment : flowDepartments) {
            if (currentFlowDepartment.getParentId().equals(flowDepartment.getId())) {
                finalFlowDepartment = flowDepartment;
            }
        }
        if (i == 0) {
            return getCorrespondingLevelFlowDepartment(flowDepartments, Integer.toString(i), finalFlowDepartment);
        } else {
            return finalFlowDepartment;
        }
    }

    /**
     * 连续多级主管审批时，获取对应主管级别的所有部门
     * @param flowDepartments 部门列表
     * @param examineEndDirectorLevel 主管级别
     * @param currentFlowDepartment 当前节点
     * @return List<FlowDepartment>
     */
    public static List<FlowDepartment> getAllDepartmentsAtTheCorrespondingSupervisorLevel(List<FlowDepartment> flowDepartments, String examineEndDirectorLevel, FlowDepartment currentFlowDepartment, List<FlowDepartment> resultFlowDepartments, Boolean isToIterateAll) {
        int i = Integer.parseInt(examineEndDirectorLevel) - 1;
        FlowDepartment finalFlowDepartment = null;
        if (isToIterateAll) {
            for (FlowDepartment flowDepartment : flowDepartments) {
                if (currentFlowDepartment.getParentId().equals(flowDepartment.getId())) {
                    finalFlowDepartment = flowDepartment;
                }
            }
            if (Objects.nonNull(finalFlowDepartment) && StringUtils.isNotEmpty(finalFlowDepartment.getParentId()) && "0".equals(finalFlowDepartment.getParentId())) {
                return getAllDepartmentsAtTheCorrespondingSupervisorLevel(flowDepartments, Integer.toString(i), finalFlowDepartment, resultFlowDepartments, isToIterateAll);
            } else {
                resultFlowDepartments.add(finalFlowDepartment);
                return resultFlowDepartments;
            }
        } else {
            for (FlowDepartment flowDepartment : flowDepartments) {
                if (currentFlowDepartment.getParentId().equals(flowDepartment.getId())) {
                    finalFlowDepartment = flowDepartment;
                }
            }
            if (i == 0) {
                return getAllDepartmentsAtTheCorrespondingSupervisorLevel(flowDepartments, Integer.toString(i), finalFlowDepartment, resultFlowDepartments, isToIterateAll);
            } else {
                resultFlowDepartments.add(finalFlowDepartment);
                return resultFlowDepartments;
            }
        }
    }

    public static Map<String, List<FlowDepartment>> sortFlowDepartment(List<FlowDepartment> flowDepartments) {
        List<FlowDepartment> resultList = new ArrayList<>();
        // 1、获取第一级节点
        for (FlowDepartment flowDepartment : flowDepartments) {
            if (null == flowDepartment.getParentId() || "0".equals(flowDepartment.getParentId())) {
                flowDepartment.setSortIndex(0);
                resultList.add(flowDepartment);
            }
        }
        Map<String, List<FlowDepartment>> map = new HashMap<>();
        // 2、递归获取子节点
        for (FlowDepartment parent : resultList) {
            List<FlowDepartment> result = new ArrayList<>();
            result.add(parent);
            recursiveTree(parent, flowDepartments, result, parent.getSortIndex());
            map.put(parent.getId(), result);
        }
        return map;
    }

    public static List<FlowDepartment> recursiveTree(FlowDepartment parent, List<FlowDepartment> list, List<FlowDepartment> resultList, Integer sortIndex) {
        for (FlowDepartment flowDepartment : list) {
            if (parent.getId().equals(flowDepartment.getParentId())) {
                Integer s = sortIndex + 1;
                parent.setSortIndex(s);
                recursiveTree(flowDepartment, list, resultList, s);

            }
        }
        return resultList;
    }
}
