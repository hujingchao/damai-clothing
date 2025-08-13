package com.fenglei.service.workFlow;


import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.model.workFlow.entity.WorkFlow;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yzy
 */
public interface CirculationOperationService {

    /**
     * 发起流程
     * @param workFlowId 流程ID
     * @param formData 单据数据
     * @param userId 发起人ID
     * @param childNodes 抄送节点
     * @param flowUsers 流程用户列表
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean startFlow(String workFlowId, Object formData, String userId, List<ChildNode> childNodes, List<FlowUser> flowUsers) throws Exception;

    /**
     * 执行审批，流转节点
     * @param formData 表单数据
     * @param flowUsers 用户列表
     * @param flowRoles 角色列表
     * @param flowDepartments 部门列表
     * @param userId 当前用户ID
     * @param childNodeApprovalResult 节点审批意见
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean doNextFlow(Object formData, List<FlowUser> flowUsers, List<FlowRole> flowRoles, List<FlowDepartment> flowDepartments, ChildNodeApprovalResult childNodeApprovalResult, String userId) throws Exception;

    /**
     * 查找单据的流程实例
     * @param formIds 单据ID集合
     * @param status 实例状态 0.进行中 1.已完成 2.已废弃 默认为 0 和 1
     * @param flowUser 用户
     * @param flowDepartments 流程部门列表
     * @param flowUsers 流程用户列表
     * @return List<ChildNode>
     */
    List<ChildNode> getCurrentNode(List<String> formIds, String workFlowId, String status, FlowUser flowUser, List<FlowDepartment> flowDepartments, List<FlowUser> flowUsers);

    /**
     * 获取流程信息
     * @param flowUser 流程用户
     * @param isRead 是否已读
     * @param noticeType 通知类型
     * @return List<FlowNotice>
     */
    List<FlowNotice> getFlowNotice(FlowUser flowUser, Boolean isRead, String noticeType);

    /**
     * 阅读信息
     * @param flowNoticeId 信息ID
     * @return Boolean
     */
    Boolean readFlowNotice(String flowNoticeId);

    /**
     * 通过当前节点获取下一节点
     * @param workFlowInstantiateId 流程实例ID
     * @param currentNodeId 当前节点ID
     * @param formData 单据数据
     * @param flowUsers 流程
     * @param flowDepartments 流程部门列表
     * @param userId 用户ID
     * @return ChildNode
     * @throws Exception 错误
     */
    ChildNode getNextChildNode(String workFlowInstantiateId, String currentNodeId, Object formData, List<FlowUser> flowUsers, List<FlowDepartment> flowDepartments, String userId) throws Exception;

    /**
     * 通过表单类型ID获取流程
     * @param id 表单ID
     * @param type 表单类型
     * @return WorkFlow
     */
    WorkFlow getWorkFlowByTableId(String id, String type);

    /**
     * 获取流程链条
     * @param workFlowInstantiateId 工作流程实例化ID
     * @param formData 表单数据
     * @param flowUsers 流程用户列表
     * @return List<ChildNode>
     * @throws Exception 错误
     */
    List<ChildNode> getProcessChain(String workFlowInstantiateId, Object formData, List<FlowUser> flowUsers) throws Exception;

    /**
     * 获取抄送节点
     * @param flowOperationInfo 流程操作信息
     * @return List<ChildNode>
     * @throws Exception 错误
     */
    List<ChildNode> getCcNode(FlowOperationInfo flowOperationInfo) throws Exception;

    /**
     * 获取流程
     * @param workFlowId 流程ID
     * @return WorkFlow
     */
    WorkFlow getFormWorkFlow(String workFlowId);

//    Integer whetherLast(String formId);

    Boolean  whetherReject(String formId);

    Result start(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception;
    Result doNext(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception;
    Integer whetherLast(@RequestParam("formId") String formId);
    List<ChildNode> getCurrentNodes(@RequestBody FlowOperationInfo flowOperationInfo);
}
