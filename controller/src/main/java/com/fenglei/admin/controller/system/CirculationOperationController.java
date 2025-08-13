package com.fenglei.admin.controller.system;


import com.fenglei.common.result.Result;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.dto.*;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.ChildNodeApprovalResult;
import com.fenglei.service.system.SysFilesService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Api(value = "流程操作接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@AllArgsConstructor
@Slf4j
public class CirculationOperationController {

    @Resource
    private CirculationOperationService circulationOperationService;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private SysFilesService sysFilesService;

    /**
     * 发起流程
     *
     * @param flowOperationInfo 流程操作信息类
     * @return Boolean
     * @throws Exception 错误
     */
    @ApiOperation(value = "发起流程")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PostMapping("/start")
    public Result startFlow(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        if (circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers)) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }


    /**
     * 执行审批，流转节点
     *
     * @param flowOperationInfo 流程操作信息类
     * @return Boolean
     * @throws Exception 错误
     */
    @ApiOperation(value = "执行流程")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PutMapping("/doNext")
    public Result doNextFlow(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
        if (circulationOperationService.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId())) {
            return Result.success("审核成功");
        }
        return Result.failed("审核失败");
    }

    /**
     * 查找单据的流程实例
     *
     * @param flowOperationInfo 流程操作信息类
     * @return List<ChildNode>
     */
    @ApiOperation(value = "保存流程")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PostMapping("/current/nodes")
    public Result getCurrentNodes(@RequestBody FlowOperationInfo flowOperationInfo) {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
        return Result.success(circulationOperationService.getCurrentNodes(flowOperationInfo));
    }


    /**
     * 获取流程信息
     *
     * @param flowUser 流程用户
     * @param isRead   是否已读
     * @return List<FlowNotice>
     */
    @ApiOperation(value = "获取流程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flowUser", value = "实体JSON对象", required = true, paramType = "body", dataType = "flowUser"),
            @ApiImplicitParam(name = "isRead", value = "是否阅读", paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "noticeType", value = "消息类型", paramType = "query", dataType = "String")
    })
    @GetMapping("/flow/notice")
    public Result getFlowNotice(FlowUser flowUser, @RequestParam(required = false) Boolean isRead, String noticeType) {
        return Result.success(circulationOperationService.getFlowNotice(flowUser, isRead, noticeType));
    }

    /**
     * 阅读信息
     *
     * @param flowNoticeId 信息ID
     * @return Boolean
     */
    @ApiOperation(value = "阅读信息")
    @ApiImplicitParam(name = "flowNoticeId", value = "信息id", required = true, paramType = "path", dataType = "String")
    @PutMapping("/read/notice/{flowNoticeId}")
    public Result readFlowNotice(@PathVariable String flowNoticeId) {
        if (circulationOperationService.readFlowNotice(flowNoticeId)) {
            return Result.success("阅读成功", null);
        }
        return Result.failed("阅读失败");
    }

    /**
     * 通过当前节点获取下一节点
     *
     * @param flowOperationInfo 流程操作信息类
     * @return ResponseBean
     */
    @ApiOperation(value = "通过当前节点获取下一节点")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PostMapping("/next/childNode")
    public Result getNextChildNode(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
        ChildNode childNode = circulationOperationService.getNextChildNode(flowOperationInfo.getWorkFlowInstantiateId(), flowOperationInfo.getCurrentNodeId(), flowOperationInfo.getFormData(), flowUsers, flowDepartments, flowOperationInfo.getUserId());
        return Result.success(childNode);
    }

    /**
     * 获取流程链条
     *
     * @param flowOperationInfo 流程操作信息类
     * @return ResponseBean
     */
    @ApiOperation(value = "获取流程链条")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PostMapping("/childNode/process/chain")
    public Result getProcessChain(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        List<ChildNode> childNodes = circulationOperationService.getProcessChain(flowOperationInfo.getWorkFlowInstantiateId(), flowOperationInfo.getFormData(), flowUsers);
        List<ChildNodeApprovalResult> childNodeApprovalResults = new ArrayList<>();
        for (ChildNode childNode : childNodes) {
            if (childNode.getChildNodeApprovalResults() != null && childNode.getChildNodeApprovalResults().size() > 0) {
                childNodeApprovalResults.addAll(childNode.getChildNodeApprovalResults());
            }
        }
        for (ChildNodeApprovalResult childNodeApprovalResult : childNodeApprovalResults) {
            for (FlowUser flowUser : flowUsers) {
                if (childNodeApprovalResult.getUserId().equals(flowUser.getId())) {
                    childNodeApprovalResult.setUserName(flowUser.getEmployeeName());
                }
            }
        }
        for (ChildNode childNode : childNodes) {
            List<ChildNodeApprovalResult> childNodeApprovalResultList = new ArrayList<>();
            for (ChildNodeApprovalResult childNodeApprovalResult : childNodeApprovalResults) {
                if (childNode.getId().equals(childNodeApprovalResult.getChildNodeId())) {
                    childNodeApprovalResultList.add(childNodeApprovalResult);
                }
            }
            childNode.setChildNodeApprovalResults(childNodeApprovalResultList);
        }
        return Result.success(childNodes);
    }

    @ApiOperation(value = "通过表单类型ID获取流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/workFlow/by/tableId/{id}/{type}")
    public Result getWorkFlowByTableId(@PathVariable String id, @PathVariable String type) {
        return Result.success(circulationOperationService.getWorkFlowByTableId(id, type));
    }

    /**
     * 审批签名保存
     *
     * @return ResponseBean
     * @throws Exception 错误
     */
    @ApiOperation(value = "上传签名")
    @ApiImplicitParams({
    })
    @PostMapping("/workFlow/signature")
    public Result signature(@RequestBody SignatureDTO signatureDTO) throws Exception {
        SysFiles sysFiles = sysFilesService.addAndReturn(signatureDTO.getImgStr(), null);
        return Result.success(sysFiles.getUrl());
    }

    /**
     * 获取抄送节点
     *
     * @param flowOperationInfo 流程操作信息
     * @return ResponseBean
     */
    @ApiOperation(value = "获取抄送节点")
    @ApiImplicitParam(name = "flowOperationInfo", value = "实体JSON对象", required = true, paramType = "body", dataType = "FlowOperationInfo")
    @PostMapping("/CC/node")
    public Result getCcNode(@RequestBody FlowOperationInfo flowOperationInfo) throws Exception {
        return Result.success(circulationOperationService.getCcNode(flowOperationInfo));
    }

    @ApiOperation(value = "获取工作流")
    @ApiImplicitParam(name = "workFlowId", value = "工作流id", paramType = "query", dataType = "String")
    @GetMapping("/form/workFlow")
    public Result getFormWorkFlow(String workFlowId) {
        return Result.success(circulationOperationService.getFormWorkFlow(workFlowId));
    }

    @ApiOperation(value = "根据formId查询是否审核完成")
    @GetMapping("/whetherLast")
    public Result<Integer> whetherLast(@RequestParam("formId") String formId) {
        return Result.success(circulationOperationService.whetherLast(formId));
    }

    @ApiOperation(value = "根据formId查询是否驳回")
    @GetMapping("/whetherReject")
    public Result whetherReject(@RequestParam("formId") String formId) {
        return Result.success(circulationOperationService.whetherReject(formId));
    }
}
