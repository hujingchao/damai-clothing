package com.fenglei.service.workFlow.util;


import com.fenglei.model.workFlow.dto.FlowDepartment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author yzy
 */
public class FlowDepartmentUtil {

    /**
     * parseMenuTree<br> 部门菜单<br>
     * @param list 数据库里面获取到的全量部门列表
     * @return List<MenuDTO>
     */
    public static List<FlowDepartment> parseDepartmentTree(List<FlowDepartment> list) {
        List<FlowDepartment> result = new ArrayList<>();
        // 1、获取第一级节点
        for (FlowDepartment department : list) {
            if (null == department.getParentId() || ChildNodeUtil.NUMBER_0.equals(department.getParentId())) {
                result.add(department);
            }
        }
        // 2、递归获取子节点
        for (FlowDepartment parent : result) {
            recursiveTree(parent, list);
        }
        result.sort(Comparator.comparing(FlowDepartment::getSortIndex));
        return result;
    }

    /**
     * 递归树
     * @param parent 父流程部门
     * @param list 流程部门列表
     */
    public static void recursiveTree(FlowDepartment parent, List<FlowDepartment> list) {
        for (FlowDepartment department : list) {
            if (parent.getId().equals(department.getParentId())) {
                recursiveTree(department, list);
                parent.getChildren().add(department);
            }
        }
        parent.getChildren().sort(Comparator.comparing(FlowDepartment::getSortIndex));
    }

}
