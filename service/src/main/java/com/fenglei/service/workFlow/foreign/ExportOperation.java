package com.fenglei.service.workFlow.foreign;


import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.*;
import com.fenglei.service.workFlow.operation.*;
import com.fenglei.service.workFlow.util.ActionsChain;
import com.fenglei.service.workFlow.util.AnalysisJson;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 组装计算方式，开始计算流程
 * @author yzy
 */

public class ExportOperation {

    @SuppressWarnings("unchecked")
    public static Context start(String tableCode, String formName, Object formData, WorkFlowInstantiate workFlowInstantiate, String userId, TableInfo tableInfoDTO) throws Exception{
        List<Command> commands = new ArrayList<>();
        Context context = new ContextBase();
        context.put("tableCode",tableCode);
        context.put("formData", formData);
        context.put("formName", formName);
        context.put("workFlowInstantiate", workFlowInstantiate);
        context.put("userId", userId);
        AnalysisJson analysisJson = new AnalysisJson();
        Map<String, Object> map = analysisJson.getChildNodeToList(workFlowInstantiate.getChildNode());
        List<Condition> conditionList = ClassConversionTools.castList(map.get("conditionList"), Condition.class);
        List<ChildNode> childNodeList = ClassConversionTools.castList(map.get("childNodes"), ChildNode.class);
        List<ChildNode> conditionNodes = ClassConversionTools.castList(map.get("conditionNodes"), ChildNode.class);
        List<NodeUser> nodeUserList = ClassConversionTools.castList(map.get("nodeUserList"), NodeUser.class);
        List<ChildNode> needChangeChildNodes = new ArrayList<>();
        context.put("conditionList", conditionList);
        context.put("childNodeList", childNodeList);
        context.put("conditionNodes", conditionNodes);
        context.put("nodeUserList", nodeUserList);
        context.put("tableInfoDTO", tableInfoDTO);
        context.put("flowNotices", new ArrayList<FlowNotice>());
        context.put("needChangeChildNodes", needChangeChildNodes);
        // 流程开始操作
        commands.add(new StartOperation());
        // 判断流程是否结束
        commands.add(new JudgeWhetherTheProcessIsFinishedOrNot());
          if (StringUtils.isNotEmpty(formName)) {
            // 结束流程后执行联动方法
            commands.add(new ExecutionLinkageOperation());
        }
        // 结束计算流程
        commands.add(new EndOperation());
        // 开始组装流程责任链
        ActionsChain actionsChain = new ActionsChain(commands);
        actionsChain.execute(context);

        return context;
    }

    @SuppressWarnings("unchecked")
    public static Context next(String tableCode, String formName, Object formData, WorkFlowInstantiate workFlowInstantiate, List<FlowUser> flowUsers, List<FlowDepartment> flowDepartments, List<FlowRole> flowRoles,
                               ChildNodeApprovalResult childNodeApprovalResult, String userId, List<ChildNodeApprovalResult> childNodeApprovalResults, TableInfo tableInfoDTO) throws Exception{
        List<Command> commands = new ArrayList<>();
        Context context = new ContextBase();
        context.put("formData", formData);
        context.put("formName", formName);
        context.put("tableCode", tableCode);
        context.put("workFlowInstantiate", workFlowInstantiate);
        context.put("userId", userId);
        context.put("tableInfoDTO", tableInfoDTO);
        context.put("flowNotices", new ArrayList<FlowNotice>());
        AnalysisJson analysisJson = new AnalysisJson();
        Map<String, Object> map = analysisJson.getChildNodeToList(workFlowInstantiate.getChildNode());
        List<Condition> conditionList = ClassConversionTools.castList(map.get("conditionList"), Condition.class);
        List<ChildNode> childNodeList = ClassConversionTools.castList(map.get("childNodes"), ChildNode.class);
        List<ChildNode> conditionNodes = ClassConversionTools.castList(map.get("conditionNodes"), ChildNode.class);
        List<NodeUser> nodeUserList = ClassConversionTools.castList(map.get("nodeUserList"), NodeUser.class);
        List<ChildNode> needChangeChildNodes = new ArrayList<>();
        context.put("conditionList", conditionList);
        context.put("childNodeList", childNodeList);
        context.put("conditionNodes", conditionNodes);
        context.put("nodeUserList", nodeUserList);
        context.put("flowUsers", flowUsers);
        context.put("flowDepartments", flowDepartments);
        context.put("flowRoles", flowRoles);
        context.put("childNodeApprovalResult", childNodeApprovalResult);
        context.put("childNodeApprovalResults", childNodeApprovalResults);
        context.put("needChangeChildNodes", needChangeChildNodes);
        // 解析正常节点
        commands.add(new AnalysisChildNodeOperation());
        // 解析当前节点
        commands.add(new AnalysisCurrentChildNodeOperation());
        // 处理审批
        commands.add(new ProcessingAuditOperation());
        // 解析下一节点
        commands.add(new AnalysisNextChildNodeOperation());
        // 判断流程是否结束
        commands.add(new JudgeWhetherTheProcessIsFinishedOrNot());
        if (StringUtils.isNotEmpty(formName)) {
            // 结束流程后执行联动方法
            commands.add(new ExecutionLinkageOperation());
        }
        // 结束计算流程
        commands.add(new EndOperation());
        // 开始组装流程责任链
        ActionsChain actionsChain = new ActionsChain(commands);
        actionsChain.execute(context);

        return context;
    }



}
