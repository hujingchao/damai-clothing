package com.fenglei.service.workFlow.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.*;
import com.fenglei.service.workFlow.*;
import com.fenglei.service.workFlow.foreign.ExportOperation;
import com.fenglei.service.workFlow.util.*;
import lombok.AllArgsConstructor;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
@Service
@AllArgsConstructor
public class CirculationOperationServiceImpl implements CirculationOperationService {

    private WorkFlowInstantiateService workFlowInstantiateService;
    private WorkFlowService workFlowService;
    private ITableInfoService iTableInfoService;
    private ChildNodeApprovalResultService childNodeApprovalResultService;
    private ChildNodeService childNodeService;
    private NodeUserService nodeUserService;
    private ConditionService conditionService;
    private WorkFlowDefService workFlowDefService;
    private FlowNoticeService flowNoticeService;
    private CustomFormService customFormService;
    private SystemInformationAcquisitionService systemInformationAcquisitionService;


    /**
     * 发起流程
     * @param workFlowId 流程ID
     * @param formData 单据数据
     * @param userId 发起人ID
     * @param childNodes 抄送节点
     * @param flowUsers 流程用户
     * @return Boolean
     * @throws Exception 所有错误
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean startFlow(String workFlowId, Object formData, String userId, List<ChildNode> childNodes, List<FlowUser> flowUsers) throws Exception {
        WorkFlow workFlow = workFlowService.getOneWorkFlow(workFlowId);
        String formDataStr = JSON.toJSONString(formData);
        JSONObject jsonObject = JSONObject.parseObject(formDataStr);
        WorkFlowInstantiate workFlowInstantiate = workFlowInstantiateService.saveWorkFlowInstantiate(workFlow, jsonObject.get("id").toString(), userId, childNodes, flowUsers);
        if (Objects.nonNull(workFlowInstantiate)) {
            WorkFlowInstantiate workFlowInstantiate1 = workFlowInstantiateService.getOneWorkFlowInstantiate(workFlowInstantiate.getId());
            TableInfo tableInfoDTO = new TableInfo();
            if (ChildNodeUtil.FLOW_TYPE_OUT.equals(workFlow.getFlowType())) {
                tableInfoDTO = iTableInfoService.findById(workFlow.getTableId());
            } else if (ChildNodeUtil.FLOW_TYPE_IN.equals(workFlow.getFlowType())) {
                CustomForm customForm = customFormService.getById(workFlow.getTableId());
                tableInfoDTO.setTableName(customForm.getTitle());
                tableInfoDTO.setFormName(customForm.getTitle());
                tableInfoDTO.setId(customForm.getId());
            }
            long startTime = System.currentTimeMillis();
            System.out.println("------>startTime：" + startTime);
//            ObjectDTO objectDTO = new ObjectDTO();
//            objectDTO.setEntity(tableInfoDTO.getFormName());
//            objectDTO.setService(tableInfoDTO.getTableCode());
//            objectDTO.setData(formData);
//            omsWorkFlowClient.changeBillStatus(objectDTO);
            Context context = ExportOperation.start(tableInfoDTO.getTableCode(),tableInfoDTO.getFormName(), formData, workFlowInstantiate1, userId, tableInfoDTO);
            long endTime = System.currentTimeMillis();
            System.out.println("------>endTime：" + endTime);
            System.out.println("------>耗时：" + (endTime - startTime) + "毫秒");
            List<FlowNotice> flowNotices = ClassConversionTools.castList(context.get("flowNotices"),FlowNotice.class);
            List<ChildNode> needChangeChildNodes = ClassConversionTools.castList(context.get("needChangeChildNodes"),ChildNode.class);

            // 清空单据历史通知
            List<String> fnFormIds = flowNotices.stream().map(FlowNotice::getFormId).collect(Collectors.toList());
            flowNoticeService.remove(new LambdaQueryWrapper<FlowNotice>()
                    .in(ObjectUtil.isNotNull(flowNotices), FlowNotice::getFormId, fnFormIds)
            );

            // 清空单据历史子节点
            List<String> cnWorkFlowInstantiateIds = needChangeChildNodes.stream().map(ChildNode::getWorkFlowInstantiateId).collect(Collectors.toList());
            childNodeService.remove(
                    new LambdaQueryWrapper<ChildNode>()
                            .in(ObjectUtil.isNotNull(needChangeChildNodes), ChildNode::getWorkFlowInstantiateId, cnWorkFlowInstantiateIds)
            );

            flowNoticeService.saveOrUpdateBatch(flowNotices);
            childNodeService.saveOrUpdateBatch(needChangeChildNodes);
            if (Objects.nonNull(context.get("workFlowInstantiate"))) {
                WorkFlowInstantiate workFlowInstantiate2 = (WorkFlowInstantiate)context.get("workFlowInstantiate");

                // 清空单据历史审批流
                workFlowInstantiateService.remove(new LambdaQueryWrapper<WorkFlowInstantiate>()
                        .eq(StringUtils.isNotBlank(workFlowInstantiate2.getTableId()), WorkFlowInstantiate::getTableId, workFlowInstantiate2.getTableId())
                        .eq(StringUtils.isNotBlank(workFlowInstantiate2.getWorkFlowId()), WorkFlowInstantiate::getWorkFlowId, workFlowInstantiate2.getWorkFlowId())
                        .eq(StringUtils.isNotBlank(workFlowInstantiate2.getFormId()), WorkFlowInstantiate::getFormId, workFlowInstantiate2.getFormId())
                );

                workFlowInstantiateService.saveOrUpdate(workFlowInstantiate2);
            }
            return true;
        } else {
            throw new BizException("流程启动失败");
        }
    }

    /**
     * 执行审批，流转节点
     * @param formData 表单数据
     * @param flowUsers 用户列表
     * @param flowRoles 角色列表
     * @param flowDepartments 部门列表
     * @param userId 当前用户ID
     * @return Boolean
     * @throws Exception 所有错误
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean doNextFlow(Object formData, List<FlowUser> flowUsers, List<FlowRole> flowRoles,
                                           List<FlowDepartment> flowDepartments, ChildNodeApprovalResult childNodeApprovalResult, String userId
    ) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(formData));
        List<WorkFlowInstantiate> workFlowInstantiates = workFlowInstantiateService.list(new QueryWrapper<WorkFlowInstantiate>().lambda()
                .eq(WorkFlowInstantiate::getFormId, jsonObject.get("id").toString())
                .eq(WorkFlowInstantiate::getStatus, ChildNodeUtil.NUMBER_INT_0)
        );
        childNodeApprovalResult.setChildNodeId(jsonObject.get("currentNodeId").toString());
        if (workFlowInstantiates.size() > ChildNodeUtil.NUMBER_INT_0) {
            WorkFlowInstantiate workFlowInstantiate1 = workFlowInstantiateService.getOneWorkFlowInstantiate(workFlowInstantiates.get(ChildNodeUtil.NUMBER_INT_0).getId());
            WorkFlow workFlow = workFlowService.getOneWorkFlow(workFlowInstantiate1.getWorkFlowId());
            TableInfo tableInfoDTO = new TableInfo();
            if (ChildNodeUtil.FLOW_TYPE_OUT.equals(workFlow.getFlowType())) {
                tableInfoDTO = iTableInfoService.findById(workFlow.getTableId());
            } else if (ChildNodeUtil.FLOW_TYPE_IN.equals(workFlow.getFlowType())) {
                CustomForm customForm = customFormService.getById(workFlow.getTableId());
                tableInfoDTO.setTableName(customForm.getTitle());
                tableInfoDTO.setFormName(customForm.getTitle());
                tableInfoDTO.setId(customForm.getId());
            }
            List<ChildNodeApprovalResult> childNodeApprovalResults = childNodeApprovalResultService.list(new QueryWrapper<ChildNodeApprovalResult>().lambda().eq(ChildNodeApprovalResult::getChildNodeId, childNodeApprovalResult.getChildNodeId()));
            long startTime = System.currentTimeMillis();
            System.out.println("------>startTime：" + startTime);
            Context context = ExportOperation.next(tableInfoDTO.getTableCode(),tableInfoDTO.getFormName(), formData, workFlowInstantiate1, flowUsers, flowDepartments, flowRoles, childNodeApprovalResult, userId, childNodeApprovalResults, tableInfoDTO);
            long endTime = System.currentTimeMillis();
            System.out.println("------>endTime：" + endTime);
            System.out.println("------>耗时：" + (endTime - startTime) + "毫秒");
            WorkFlowInstantiate workFlowInstantiate2 = (WorkFlowInstantiate)context.get("workFlowInstantiate");
            List<FlowNotice> flowNotices = ClassConversionTools.castList(context.get("flowNotices"), FlowNotice.class);
            List<ChildNode> needChangeChildNodes = ClassConversionTools.castList(context.get("needChangeChildNodes"), ChildNode.class);

            childNodeApprovalResultService.saveOrUpdate(childNodeApprovalResult);
            if (Objects.nonNull(workFlowInstantiate2)) {
                workFlowInstantiateService.saveOrUpdate(workFlowInstantiate2);
            }
            if (Objects.nonNull(flowNotices) && flowNotices.size() > ChildNodeUtil.NUMBER_INT_0) {
                flowNoticeService.saveOrUpdateBatch(flowNotices);
            }
            if (Objects.nonNull(needChangeChildNodes) && needChangeChildNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
                childNodeService.saveOrUpdateBatch(needChangeChildNodes);
            }
            return true;
        } else {
            throw new BizException("未找到该流程实例");
        }
    }

    /**
     * 查找单据的流程实例
     * @param formIds 单据ID集合
     * @param status 实例状态 0.进行中 1.已完成 2.已废弃 默认为 0 和 1
     * @param flowUser 用户
     * @return List<ChildNode>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized List<ChildNode> getCurrentNode(List<String> formIds, String workFlowId, String status, FlowUser flowUser, List<FlowDepartment> flowDepartments, List<FlowUser> flowUsers) {
        List<WorkFlowInstantiate> workFlowInstantiates = getWorkFlowInstantiates(formIds, workFlowId, status);
        List<ChildNode> currentNodeList = new ArrayList<>();
        List<WorkFlowInstantiate> workFlowInstantiateList = workFlowInstantiates.stream().filter(w -> StringUtils.isEmpty(status) ?  (w.getStatus() == 0 || w.getStatus() == 1) : w.getStatus() == Integer.parseInt(status)).collect(Collectors.toList());
        for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiateList) {
            ChildNode currentNode = AnalysisJson.getCurrentNode(workFlowInstantiate.getChildNode());
            if (Objects.nonNull(currentNode)) {
                currentNode.setFromId(workFlowInstantiate.getFormId());
                currentNodeList.add(currentNode);
            }
        }

        for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiates) {
            ChildNode currentNode = AnalysisJson.getCurrentNode(workFlowInstantiate.getChildNode());
            if (Objects.nonNull(currentNode)) {
                currentNode.setFromId(workFlowInstantiate.getFormId());
                currentNodeList.add(currentNode);
            }
        }
        List<ChildNode> childNodes = getEffectiveChildNodes(currentNodeList, workFlowInstantiates, flowUser, flowDepartments, flowUsers);
        FlowUser thisFlowUser = null;
        for (FlowUser user : flowUsers) {
            if (user.getId().equals(flowUser.getId())) {
                thisFlowUser = user;
            }
        }
        for (ChildNode childNode : childNodes) {
            for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiateList) {
                if (childNode.getWorkFlowInstantiateId().equals(workFlowInstantiate.getId())) {
                    List<NodeUser> nodeUsers = childNode.getNodeUserList();
                    if (thisFlowUser != null) {
                        List<String> roleIds = thisFlowUser.getRoleIds();
                        boolean flag = false;
                        for (NodeUser nodeUser : nodeUsers) {
                            if (thisFlowUser.getId().equals(nodeUser.getTargetId())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            for (NodeUser nodeUser : nodeUsers) {
                                if (roleIds.contains(nodeUser.getTargetId())) {
                                    flag = true;
                                    break;
                                }
                            }
                        }

                        if (flag) {
                            childNode.setWorkFlowInstantiateStatus(workFlowInstantiate.getStatus());
                        } else {
                            childNode.setWorkFlowInstantiateStatus(3);
                        }
                    } else {
                        childNode.setWorkFlowInstantiateStatus(3);
                    }
                }
            }
        }
        List<ChildNode> newChildNodes = new ArrayList<>();
        for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiates) {
            boolean flag = true;
            for (ChildNode childNode : childNodes) {
                if (workFlowInstantiate.getId().equals(childNode.getWorkFlowInstantiateId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ChildNode childNode = new ChildNode();
                childNode.setWorkFlowInstantiateStatus(workFlowInstantiate.getStatus());
                childNode.setWorkFlowInstantiateId(workFlowInstantiate.getId());
                childNode.setFromId(workFlowInstantiate.getFormId());
                newChildNodes.add(childNode);
            }
        }
        childNodes.addAll(newChildNodes);
        return childNodes;
    }

    /**
     * 获取流程信息
     * @param flowUser 流程人员
     * @param isRead 是否已读
     * @return List<FlowNotice>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FlowNotice> getFlowNotice(FlowUser flowUser, Boolean isRead, String noticeType) {
        List<String> roleIds = flowUser.getRoleIds();
        StringBuilder roleIdStr = new StringBuilder();
        if (Objects.nonNull(roleIds) && roleIds.size() > ChildNodeUtil.NUMBER_INT_0) {
            for (String roleId : roleIds) {
                if (StringUtils.isNotEmpty(roleIdStr)) {
                    roleIdStr.append(",");
                }
                roleIdStr.append(roleId.toString());
            }
        }
        return flowNoticeService.list(new QueryWrapper<FlowNotice>().lambda()
                .eq(FlowNotice::getNoticeType, noticeType)
                .like(StringUtils.isNotEmpty(flowUser.getId()), FlowNotice::getUserIds, flowUser.getId())
                .like(StringUtils.isNotEmpty(roleIdStr), FlowNotice::getRoleIds, roleIdStr)
        );
    }

    /**
     * 阅读信息
     * @param flowNoticeId 信息ID
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean readFlowNotice(String flowNoticeId) {
        FlowNotice flowNotice = flowNoticeService.getById(flowNoticeId);
        flowNotice.setIsRead(true);
        return flowNoticeService.saveOrUpdate(flowNotice);
    }

    /**
     * 通过当前节点获取下一节点
     * @param workFlowInstantiateId 流程实例ID
     * @param currentNodeId 当前节点ID
     * @param formData 单据数据
     * @return ChildNode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChildNode getNextChildNode(String workFlowInstantiateId, String currentNodeId, Object formData, List<FlowUser> flowUsers, List<FlowDepartment> flowDepartments, String userId) throws Exception{

        List<ChildNode> childNodes = childNodeService.list(new QueryWrapper<ChildNode>().lambda().eq(ChildNode::getWorkFlowInstantiateId, workFlowInstantiateId));
        List<Condition> conditions = conditionService.list(new QueryWrapper<Condition>().lambda().eq(Condition::getWorkFlowInstantiateId, workFlowInstantiateId));
        List<NodeUser> nodeUsers = nodeUserService.list(new QueryWrapper<NodeUser>().lambda().eq(NodeUser::getWorkFlowInstantiateId, workFlowInstantiateId));
        List<ChildNode> childNodes1 = FormatToTree.parseTreeByPid(childNodes, conditions, nodeUsers, currentNodeId);

        ChildNode currentNode = childNodes1.get(ChildNodeUtil.NUMBER_INT_0);
        WorkFlowInstantiate workFlowInstantiate = workFlowInstantiateService.getOneWorkFlowInstantiate(workFlowInstantiateId);
        // 设置类型 1.指定成员 2.主管 3.角色 4.发起人自选 5.发起人自己 7.连续多级主管
        String setType = currentNode.getSettype();
        boolean flag = false;
        if (ChildNodeUtil.NUMBER_1.equals(setType) || ChildNodeUtil.NUMBER_2.equals(setType) || ChildNodeUtil.NUMBER_3.equals(setType) || ChildNodeUtil.NUMBER_4.equals(setType) || ChildNodeUtil.NUMBER_5.equals(setType)) {
            flag = true;
        } else if (ChildNodeUtil.NUMBER_7.equals(setType)) {
            List<ChildNodeApprovalResult> childNodeApprovalResults = childNodeApprovalResultService.list(new QueryWrapper<ChildNodeApprovalResult>().lambda().eq(ChildNodeApprovalResult::getChildNodeId, currentNode.getId()));
            FlowDepartment currentFlowDepartment = null;
            if (Objects.nonNull(flowUsers) && flowUsers.size() > ChildNodeUtil.NUMBER_INT_0) {
                for (FlowUser flowUser : flowUsers) {
                    if (flowUser.getId().equals(workFlowInstantiate.getInitiatorId())) {
                        for (FlowDepartment flowDepartment : flowDepartments) {
                            if (flowDepartment.getId().equals(flowUser.getEmployeeDepartmentId())) {
                                currentFlowDepartment = flowDepartment;
                            }
                        }
                    }
                }
            }
            List<FlowDepartment> resultFlowDepartments = new ArrayList<>();
            if (Objects.nonNull(currentFlowDepartment)) {
                resultFlowDepartments = FlowDepartmentUtils.getAllDepartmentsAtTheCorrespondingSupervisorLevel(
                        flowDepartments, currentNode.getExamineEndDirectorLevel(), currentFlowDepartment,
                        resultFlowDepartments, ChildNodeUtil.NUMBER_1.equals(currentNode.getExamineEndDirectorLevel()));
            }
            if (resultFlowDepartments.size() > ChildNodeUtil.NUMBER_INT_0) {
                List<String> userIds = childNodeApprovalResults.stream().map(ChildNodeApprovalResult::getUserId).collect(Collectors.toList());
                List<String> managerIds = resultFlowDepartments.stream().map(FlowDepartment::getManagerId).collect(Collectors.toList());
                if ((userIds.size() + ChildNodeUtil.NUMBER_INT_1) == managerIds.size()) {
                    flag = true;
                }
            }
        }
        if (flag) {
            // 当前节点审批完成
            ChildNode rootNode = workFlowInstantiate.getChildNode();
            ChildNode nextNode = null;
            // 继续向下获取下一节点
            ChildNode nextChildNode = currentNode.getChildNode();
            if (Objects.nonNull(nextChildNode)) {
                if (ChildNodeUtil.NODE_TYPE_REVIEWER.equals(nextChildNode.getType())) {
                    nextNode = nextChildNode;
                } else {
                    nextNode = ChildNodeUtil.getNextNodeIncludeCc(nextChildNode, formData, userId);
                }
            }

            // 检验本级路由是否执行完成
            if (Objects.isNull(currentNode.getChildNode())) {
                // 本级路由完成，向上获取下一节点
                ChildNodeTreeUtil childNodeTreeUtil = new ChildNodeTreeUtil();
                ChildNode childNode1 = childNodeTreeUtil.getUpFirstChildNodeForStatic(rootNode, formData, currentNode, userId);
                if (Objects.nonNull(childNode1)) {
                    nextNode = childNode1;
                }
            } else {
                nextNode = currentNode.getChildNode();
            }
            return nextNode;
        } else {
            // 当前节点还需审批
            return currentNode;
        }
    }

    /**
     * 通过表单类型ID获取流程
     * @param id 表单ID
     * @param type 表单类型
     * @return WorkFlow
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlow getWorkFlowByTableId(String id, String type) {
         List<WorkFlow> workFlows = workFlowService.list(new QueryWrapper<WorkFlow>().lambda()
                .eq(WorkFlow::getTableId, id)
                .eq(WorkFlow::getFlowType, type)
                .eq(WorkFlow::getStatus, true)
        );
        WorkFlow workFlow = null;
        if (Objects.nonNull(workFlows) && workFlows.size() > ChildNodeUtil.NUMBER_INT_0) {
            workFlow = workFlows.get(ChildNodeUtil.NUMBER_INT_0);
        }
        return workFlow;
    }

    /**
     * 获取流程链条
     * @param workFlowInstantiateId 工作流程实例化ID
     * @param formData 表单数据
     * @return List<ChildNode>
     * @throws Exception 错误
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ChildNode> getProcessChain(String workFlowInstantiateId, Object formData, List<FlowUser> flowUsers) throws Exception {
        WorkFlowInstantiate workFlowInstantiate = workFlowInstantiateService.getOneWorkFlowInstantiate(workFlowInstantiateId);
        String initiatorName = null;
        for (FlowUser flowUser : flowUsers) {
            if (flowUser.getId().equals(workFlowInstantiate.getInitiatorId())) {
                initiatorName = flowUser.getEmployeeName();
            }
        }
        // 当前节点审批完成
        ChildNode rootNode = workFlowInstantiate.getChildNode();
        rootNode.setInitiatorName(initiatorName);
        return ProcessRoutingUtil.getProcessChain(rootNode, formData);
    }

    /**
     * 获取抄送节点
     * @param flowOperationInfo 流程操作信息
     * @return List<ChildNode>
     * @throws Exception 错误
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ChildNode> getCcNode(FlowOperationInfo flowOperationInfo) throws Exception {
        WorkFlow workFlow = workFlowService.getOneWorkFlow(flowOperationInfo.getWorkFlowId());
        String content = workFlow.getContent();
        JSONObject jsonObject = JSON.parseObject(content);
        Object formData = flowOperationInfo.getFormData();
        List<ChildNode> childNodeList = new ArrayList<>();
        if (Objects.nonNull(jsonObject.get("nodeConfig"))) {
            JSONObject nodeConfig = (JSONObject)jsonObject.get("nodeConfig");
            if (Objects.nonNull(nodeConfig)) {
                ChildNode childNode1 = JSONObject.toJavaObject(nodeConfig, ChildNode.class);
                childNodeList = ProcessRoutingUtil.getCcChildNodes(childNode1, formData);
            }
        }
        return childNodeList;
    }

    /**
     * 获取流程
     * @param workFlowId 流程ID
     * @return WorkFlow
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlow getFormWorkFlow(String workFlowId) {
        WorkFlow workFlow = null;
        try {
            workFlow = workFlowService.getOneWorkFlow(workFlowId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workFlow;
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Integer whetherLast(String formId) {
//        //根据formId查询启用中的流程实例
//        List<WorkFlowInstantiate> workFlowInstantiateList =workFlowInstantiateService.list(new LambdaQueryWrapper<WorkFlowInstantiate>()
//                .eq(WorkFlowInstantiate::getFormId,formId)
//                .orderByDesc(WorkFlowInstantiate::getCreateTime)
//        );
//        if (workFlowInstantiateList!=null&&workFlowInstantiateList.size()>0){
//            WorkFlowInstantiate workFlowInstantiate = workFlowInstantiateList.get(0);
//                return workFlowInstantiate.getStatus();
//        }
//        return -1;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean whetherReject(String formId) {
        //根据formId查询启用中的流程实例
        List<WorkFlowInstantiate> workFlowInstantiateList =workFlowInstantiateService.list(new LambdaQueryWrapper<WorkFlowInstantiate>()
                .eq(WorkFlowInstantiate::getFormId,formId)
                .orderByDesc(WorkFlowInstantiate::getCreateTime)
        );
        if(workFlowInstantiateList.size()>0){
            WorkFlowInstantiate workFlowInstantiate =workFlowInstantiateList.get(0);
            if (workFlowInstantiate.getStatus()==2){
                return true;
            }
            return false;
        }
        return false;

    }

    /**
     * 获取流程实例
     * @param formIds 数据表单ID
     * @param status 状态
     * @return List<WorkFlowInstantiate>
     */
    @Transactional(rollbackFor = Exception.class)
    public List<WorkFlowInstantiate> getWorkFlowInstantiates(
            List<String> formIds,
            String workFlowId,
            String status) {
        List<String> statusList = new ArrayList<>();
        if (StringUtils.isNotEmpty(status)) {
            statusList.add(status);
        } else {
            statusList.add(ChildNodeUtil.NUMBER_0);
            statusList.add(ChildNodeUtil.NUMBER_1);
            statusList.add(ChildNodeUtil.NUMBER_2);
        }
        List<WorkFlowInstantiate> workFlowInstantiates = workFlowInstantiateService.list(new QueryWrapper<WorkFlowInstantiate>().lambda()
                .in(WorkFlowInstantiate::getFormId, formIds)
                .eq(WorkFlowInstantiate::getWorkFlowId, workFlowId)
                .in(WorkFlowInstantiate::getStatus, statusList)
        );
        Set<String> workFlowInstantiateIds = workFlowInstantiates.stream().map(WorkFlowInstantiate::getId).collect(Collectors.toSet());
        if (workFlowInstantiateIds.size() > ChildNodeUtil.NUMBER_INT_0) {
            List<ChildNode> generalChildNodes = childNodeService.list(new QueryWrapper<ChildNode>().lambda().in(ChildNode::getWorkFlowInstantiateId, workFlowInstantiateIds));
            List<Condition> conditions = conditionService.list(new QueryWrapper<Condition>().lambda().in(Condition::getWorkFlowInstantiateId, workFlowInstantiateIds));
            List<NodeUser> nodeUsers = nodeUserService.list(new QueryWrapper<NodeUser>().lambda().in(NodeUser::getWorkFlowInstantiateId, workFlowInstantiateIds));
            List<WorkFlowDef> workFlowDefs = workFlowDefService.list(new QueryWrapper<WorkFlowDef>().lambda().in(WorkFlowDef::getWorkFlowInstantiateId, workFlowInstantiateIds));

            for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiates) {
                List<WorkFlowDef> workFlowInstantiateWorkFlowDefs = workFlowDefs.stream().filter(s -> s.getWorkFlowInstantiateId().equals(workFlowInstantiate.getId())).collect(Collectors.toList());
                List<ChildNode> resultChildNodes = FormatToTree.parseTree(
                        generalChildNodes.stream().filter(s -> s.getWorkFlowInstantiateId().equals(workFlowInstantiate.getId())).collect(Collectors.toList()),
                        conditions.stream().filter(s -> s.getWorkFlowInstantiateId().equals(workFlowInstantiate.getId())).collect(Collectors.toList()),
                        nodeUsers.stream().filter(s -> s.getWorkFlowInstantiateId().equals(workFlowInstantiate.getId())).collect(Collectors.toList())
                );
                if (resultChildNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
                    workFlowInstantiate.setChildNode(resultChildNodes.get(ChildNodeUtil.NUMBER_INT_0));
                }
                if (workFlowInstantiateWorkFlowDefs.size() > ChildNodeUtil.NUMBER_INT_0) {
                    workFlowInstantiate.setWorkFlowDef(workFlowInstantiateWorkFlowDefs.get(ChildNodeUtil.NUMBER_INT_0));
                }
            }
        }

        return workFlowInstantiates;
    }

    /**
     * 处理连续多级主管审批时转交给审核管理员
     * @param flowDepartments 流程部门
     * @param currentNode 当前节点
     * @param currentFlowDepartment 当前流程部门
     * @return NodeUser
     */
    @Transactional(rollbackFor = Exception.class)
    public NodeUser handleExamineEndDirectorLevelAndForwardToReviewManager(
            List<FlowDepartment> flowDepartments,
            ChildNode currentNode,
            FlowDepartment currentFlowDepartment,
            FlowUser flowUser,
            WorkFlowInstantiate workFlowInstantiate
    ) {
        NodeUser nodeUser = null;
        List<FlowDepartment> resultFlowDepartments = new ArrayList<>();
        resultFlowDepartments = FlowDepartmentUtils.getAllDepartmentsAtTheCorrespondingSupervisorLevel(flowDepartments,
                currentNode.getExamineEndDirectorLevel(),
                currentFlowDepartment,
                resultFlowDepartments,
                ChildNodeUtil.NUMBER_1.equals(currentNode.getExamineEndDirectorLevel())
        );
        Map<String, List<FlowDepartment>> map = FlowDepartmentUtils.sortFlowDepartment(flowDepartments);
        Set<String> keys = map.keySet();
        for (FlowDepartment resultFlowDepartment : resultFlowDepartments) {
            List<FlowDepartment> result = null;
            for (String key : keys) {
                if (Objects.nonNull(map.get(key))) {
                    result =  ClassConversionTools.castList(map.get(key), FlowDepartment.class);
                }
            }
            if (Objects.nonNull(result)) {
                for (FlowDepartment flowDepartment : result) {
                    if (resultFlowDepartment.getId().equals(flowDepartment.getId())) {
                        resultFlowDepartment.setSortIndex(flowDepartment.getSortIndex());
                    }
                }
            }
        }
        List<ChildNodeApprovalResult> childNodeApprovalResultList = currentNode.getChildNodeApprovalResults();
        List<FlowDepartment> surplusFlowDepartments = resultFlowDepartments.stream().filter(s -> {
            if (childNodeApprovalResultList.size() > ChildNodeUtil.NUMBER_INT_0) {
                for (ChildNodeApprovalResult childNodeApprovalResult : childNodeApprovalResultList) {
                    if (childNodeApprovalResult.getUserId().equals(s.getManagerId())) {
                        return false;
                    }
                }
            }
            return true;
        }).sorted(Comparator.comparing(FlowDepartment::getSortIndex)).collect(Collectors.toList());
        if (surplusFlowDepartments.size() > ChildNodeUtil.NUMBER_INT_0) {
            FlowDepartment flowDepartment = surplusFlowDepartments.get(ChildNodeUtil.NUMBER_INT_0);
            nodeUser = new NodeUser();
            nodeUser.setTargetId(flowDepartment.getManagerId());
            nodeUser.setType(ChildNodeUtil.NUMBER_1);
            nodeUser.setName(flowUser.getEmployeeName());
            nodeUser.setChildNodeId(currentNode.getId());
            nodeUser.setWorkFlowInstantiateId(workFlowInstantiate.getId());
            nodeUser.setSortIndex(ChildNodeUtil.NUMBER_INT_1);
        }
        return nodeUser;
    }

    /**
     * 获取有效的当前节点
     * @param currentNodeList 当前节点集合
     * @param workFlowInstantiates 流程实例列表
     * @param flowUser 流程用户
     * @param flowDepartments 流程部门列表
     * @param flowUsers 流程用户列表
     * @return List<ChildNode> 有效节点的集合
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ChildNode> getEffectiveChildNodes(List<ChildNode> currentNodeList, List<WorkFlowInstantiate> workFlowInstantiates, FlowUser flowUser,
                                                  List<FlowDepartment> flowDepartments, List<FlowUser> flowUsers
    ) {
        List<ChildNode> childNodes = new ArrayList<>();
        if (currentNodeList.size() > ChildNodeUtil.NUMBER_INT_0) {
            Set<String> currentNodeIds = currentNodeList.stream().map(ChildNode::getId).collect(Collectors.toSet());
            List<ChildNodeApprovalResult> childNodeApprovalResults = childNodeApprovalResultService.list(new QueryWrapper<ChildNodeApprovalResult>().lambda().in(ChildNodeApprovalResult::getChildNodeId, currentNodeIds));
            for (ChildNode childNode : currentNodeList) {
                List<ChildNodeApprovalResult> currentChildNodeApprovalResults = new ArrayList<>();
                for (ChildNodeApprovalResult childNodeApprovalResult : childNodeApprovalResults) {
                    if (childNode.getId().equals(childNodeApprovalResult.getChildNodeId())) {
                        currentChildNodeApprovalResults.add(childNodeApprovalResult);
                    }
                }
                childNode.setChildNodeApprovalResults(currentChildNodeApprovalResults);
            }
            for (WorkFlowInstantiate workFlowInstantiate : workFlowInstantiates) {
                ChildNode currentNode = null;
                for (ChildNode currentNode1 : currentNodeList) {
                    if (workFlowInstantiate.getId().equals(currentNode1.getWorkFlowInstantiateId())) {
                        currentNode = currentNode1;
                    }
                }
                if (Objects.nonNull(currentNode)) {
                    List<NodeUser> nodeUserList = new ArrayList<>();
                    FlowDepartment currentFlowDepartment = null;
                    FlowUser currentFlowUser = null;
                    for (FlowUser flowUser1 : flowUsers) {
                        if (flowUser1.getId().equals(workFlowInstantiate.getInitiatorId())) {
                            currentFlowUser = flowUser1;
                        }
                    }
                    if (Objects.nonNull(currentFlowUser)) {
                        for (FlowDepartment flowDepartment : flowDepartments) {
                            if (flowDepartment.getId().equals(currentFlowUser.getEmployeeDepartmentId())) {
                                currentFlowDepartment = flowDepartment;
                            }
                        }
                    }
                    NodeUser nodeUser = new NodeUser();
                    if (ChildNodeUtil.NUMBER_2.equals(currentNode.getSettype()) && ChildNodeUtil.NUMBER_2.equals(currentNode.getNoHanderAction())) {
                        FlowDepartment finalFlowDepartment = FlowDepartmentUtils.getCorrespondingLevelFlowDepartment(flowDepartments, currentNode.getDirectorLevel(), currentFlowDepartment);
                        if (Objects.nonNull(finalFlowDepartment)) {
                            nodeUser.setTargetId(finalFlowDepartment.getManagerId());
                        }
                        nodeUser.setType(ChildNodeUtil.NUMBER_1);
                        nodeUser.setName(flowUser.getEmployeeName());
                        nodeUser.setChildNodeId(currentNode.getId());
                        nodeUser.setWorkFlowInstantiateId(workFlowInstantiate.getId());
                        nodeUser.setSortIndex(ChildNodeUtil.NUMBER_INT_1);
                        nodeUserList.add(nodeUser);
                    } else if (ChildNodeUtil.NUMBER_7.equals(currentNode.getSettype()) && ChildNodeUtil.NUMBER_2.equals(currentNode.getNoHanderAction())) {
                        nodeUser = handleExamineEndDirectorLevelAndForwardToReviewManager(flowDepartments, currentNode, currentFlowDepartment, flowUser, workFlowInstantiate);
                        nodeUserList.add(nodeUser);
                    } else {
                        List<ChildNodeApprovalResult> childNodeApprovalResultList = currentNode.getChildNodeApprovalResults();
                        for (NodeUser nodeUser1 : currentNode.getNodeUserList()) {
                            for (ChildNodeApprovalResult childNodeApprovalResult : childNodeApprovalResultList) {
                                if (nodeUser1.getTargetId().equals(childNodeApprovalResult.getUserId())) {
                                    nodeUserList.add(nodeUser1);
                                }
                            }
                        }
                    }
                    if(nodeUserList.size() > ChildNodeUtil.NUMBER_INT_0) {
                        // 依次审批时需要去除其他的审批人
                        if (ChildNodeUtil.NUMBER_1.equals(currentNode.getExamineMode())) {
                            nodeUserList.sort(Comparator.comparing(NodeUser::getSortIndex));
                            List<NodeUser> tNodeUsers = new ArrayList<>();
                            tNodeUsers.add(nodeUserList.get(ChildNodeUtil.NUMBER_INT_0));
                            currentNode.setNodeUserList(tNodeUsers);
                        }
                    }
                    childNodes.add(currentNode);
                }
            }
        }
        return childNodes;
    }

    @Override
    public Result start(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        if (this.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers)) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }


    @Override
    public Result doNext(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception{
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
        if (this.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId())) {
            return Result.success("审核成功");
        }
        return Result.failed("审核失败");
    }

    @Override
    public Integer whetherLast(@RequestParam("formId") String formId){
        //根据formId查询启用中的流程实例
        List<WorkFlowInstantiate> workFlowInstantiateList =workFlowInstantiateService.list(new LambdaQueryWrapper<WorkFlowInstantiate>()
                .eq(WorkFlowInstantiate::getFormId,formId)
                .orderByDesc(WorkFlowInstantiate::getCreateTime)
        );
        if (workFlowInstantiateList!=null&&workFlowInstantiateList.size()>0){
            WorkFlowInstantiate workFlowInstantiate = workFlowInstantiateList.get(0);
            return workFlowInstantiate.getStatus();
        }
        return -1;
    }

    @Override
    public List<ChildNode> getCurrentNodes(@RequestBody FlowOperationInfo flowOperationInfo){
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
        return this.getCurrentNode(flowOperationInfo.getFormIds(), flowOperationInfo.getWorkFlowId(), flowOperationInfo.getStatus(), flowOperationInfo.getFlowUser(), flowDepartments, flowUsers);

    }
}
