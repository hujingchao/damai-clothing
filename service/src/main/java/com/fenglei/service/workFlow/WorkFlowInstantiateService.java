package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.model.workFlow.entity.WorkFlow;
import com.fenglei.model.workFlow.entity.WorkFlowInstantiate;

import java.util.List;

/**
 * @author yzy
 */
public interface WorkFlowInstantiateService extends IService<WorkFlowInstantiate> {

    /**
     * 保存流程实例
     * @param workFlow 流程
     * @param formId 表单ID
     * @param userId 用户ID
     * @param ccChildNodes 抄送节点
     * @param flowUsers 流程用户列表
     * @return WorkFlowInstantiate 流程实例
     * @throws Exception 错误
     */
    WorkFlowInstantiate saveWorkFlowInstantiate(WorkFlow workFlow, String formId, String userId, List<ChildNode> ccChildNodes, List<FlowUser> flowUsers) throws Exception;

    /**
     * 通过流程实例id删除流程实例
     * @param id 流程实例ID
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean deleteWorkFlowInstantiate(String id) throws Exception;

    /**
     * 流程实例分页展示
     * @param page 分页信息
     * @param workFlowInstantiate 流程实例
     * @return IPage<WorkFlowInstantiate>
     * @throws Exception 错误
     */
    IPage<WorkFlowInstantiate> workFlowInstantiatePage(Page<WorkFlowInstantiate> page, WorkFlowInstantiate workFlowInstantiate) throws Exception;

    /**
     * 通过流程实例id获取完整的流程实例（包含节点、条件）
     * @param id 流程实例ID
     * @return WorkFlowInstantiate 流程实例
     * @throws Exception 错误
     */
    WorkFlowInstantiate getOneWorkFlowInstantiate(String id) throws Exception;
}
