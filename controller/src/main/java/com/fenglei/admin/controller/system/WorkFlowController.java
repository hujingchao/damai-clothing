package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.model.workFlow.entity.WorkFlow;
import com.fenglei.service.workFlow.FlowNoticeService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import com.fenglei.service.workFlow.WorkFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "流程接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@Slf4j
@AllArgsConstructor
public class WorkFlowController {

    @Resource
    private WorkFlowService workFlowService;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private FlowNoticeService flowNoticeService;

    @ApiOperation(value = "保存流程")
    @ApiImplicitParam(name = "workFlow", value = "实体JSON对象", required = true, paramType = "body", dataType = "WorkFlow")
    @PostMapping("/workFlow")
    public Result saveWorkFlow(@RequestBody WorkFlow workFlow) throws Exception {
        if (workFlowService.saveWorkFlow(workFlow)) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "修改流程")
    @ApiImplicitParam(name = "workFlow", value = "实体JSON对象", required = true, paramType = "body", dataType = "WorkFlow")
    @PutMapping("/workFlow")
    public Result editWorkFlow(@RequestBody WorkFlow workFlow) throws Exception {
        if (workFlowService.editWorkFlow(workFlow)) {
            return Result.success("修改成功");
        }
        return Result.failed("修改失败");
    }

    @ApiOperation(value = "删除流程")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
    @DeleteMapping("/workFlow/{id}")
    public Result deleteWorkFlow(@PathVariable String id) throws Exception {
        if (workFlowService.deleteWorkFlow(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "流程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页对象", required = true, paramType = "body", dataType = "Page"),
            @ApiImplicitParam(name = "workFlow", value = "实体JSON对象", required = true, paramType = "body", dataType = "WorkFlow")
    })
    @GetMapping("/workFlow")
    public Result workFlowPage(Page page, WorkFlow workFlow) throws Exception {
        return Result.success(workFlowService.workFlowPage(page, workFlow));
    }

    @ApiOperation(value = "获取单个流程")
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String")
    @GetMapping("/workFlow/{id}")
    public Result getOneWorkFlow(@PathVariable String id) throws Exception {
        return Result.success(workFlowService.getOneWorkFlow(id));
    }

    @ApiOperation(value = "获取流程条件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "flowType", value = "流程类型", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("/workFlow/condition/{id}/{flowType}")
    public Result getConditions(@PathVariable String id, @PathVariable String flowType) throws Exception {
        List<Condition> conditions = workFlowService.getConditions(id, flowType);
        return Result.success(conditions);
    }

    @ApiOperation(value = "获取流程用户")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String")
    @GetMapping("/workFlow/user")
    public Result getUsers(@RequestParam(required = false) String name) {
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(name);
        return Result.success(flowUsers);
    }

    @ApiOperation(value = "获取流程部门")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String")
    @GetMapping("/workFlow/department")
    public Result getDepartments(@RequestParam(required = false) String name) {
        List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(name);
        Map<String, Object> map = new HashMap<>();
        map.put("childDepartments", flowDepartments);
        List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
        map.put("employees", flowUsers);
        map.put("titleDepartments", new ArrayList<>());
        return Result.success(map);
    }

    @ApiOperation(value = "获取流程角色")
    @ApiImplicitParam(name = "name", value = "名称", paramType = "query", dataType = "String")
    @GetMapping("/workFlow/role")
    public Result getRole(@RequestParam(required = false) String name) throws Exception {
        List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(name);
        return Result.success(flowRoles);
    }


    @ApiOperation(value = "获取对应人或角色对应的审批流推送消息")
    @ApiImplicitParam(name = "sysUser", value = "用户对象", paramType = "query", dataType = "String")
    @PostMapping("/workFlow/notice")
    public Result getNotice(@RequestBody FlowNotice flowNotice) throws Exception {
        List<FlowNotice> flowNotices = flowNoticeService.getUserNotice(flowNotice);
        return Result.success(flowNotices);
    }

    @ApiOperation(value = "获取对应人或角色对应的审批流推送消息的数量")
    @GetMapping("/workFlow/newInfoCount")
    public Result getCount() throws Exception {
        return Result.success(flowNoticeService.getNewInfoCount());
    }

    @ApiOperation(value = "设置当前用户的所有相关的审批消息为已读状态")
    @PostMapping("/workFlow/setAllIsRead")
    public Result setAllIsRead() {

        return Result.success(flowNoticeService.setAllIsRead());
    }
}
