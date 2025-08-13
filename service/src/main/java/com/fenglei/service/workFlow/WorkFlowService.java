package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.model.workFlow.entity.WorkFlow;

import java.util.List;

/**
 * @author yzy
 */
public interface WorkFlowService extends IService<WorkFlow> {

    /**
     * 保存工作流
     * @param workFlow 流程数据
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean saveWorkFlow(WorkFlow workFlow) throws Exception;

    /**
     * 修改工作流
     * @param workFlow 流程数据
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean editWorkFlow(WorkFlow workFlow) throws Exception;

    /**
     * 通过id，删除工作流
     * @param id 流程ID
     * @return Boolean
     * @throws Exception 错误
     */
    Boolean deleteWorkFlow(String id) throws Exception;

    /**
     * 工作流分页
     * @param page 页数
     * @param workFlow 工作流
     * @return IPage<WorkFlow>
     * @throws Exception 错误
     */
    IPage<WorkFlow> workFlowPage(Page<WorkFlow> page, WorkFlow workFlow) throws Exception;

    /**
     * 通过id获取单个工作流
     * @param id 工作流id
     * @return WorkFlow
     * @throws Exception 错误
     */
    WorkFlow getOneWorkFlow(String id) throws Exception;

    /**
     * 通过表单类型和表单id，获取条件
     * @param tableId 表单ID
     * @param flowType 流程类型
     * @return List<Condition>
     * @throws Exception 错误
     */
    List<Condition> getConditions(String tableId, String flowType) throws Exception;

}
