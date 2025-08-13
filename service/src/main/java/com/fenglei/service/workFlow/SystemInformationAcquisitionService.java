package com.fenglei.service.workFlow;

import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;

import java.util.List;

/**
 * 系统信息采集
 * @author yzy
 */
public interface SystemInformationAcquisitionService {

    /**
     * 获取流程部门
     * @param name 名称
     * @return List<FlowDepartment>
     */
    List<FlowDepartment> getFlowDepartments(String name);

    /**
     * 获取流程用户
     * @param name 用户名称
     * @return List<FlowUser>
     */
    List<FlowUser> getFlowUsers(String name);

    /**
     * 获取流程角色
     * @param name 角色名称
     * @return List<FlowRole>
     */
    List<FlowRole> getFlowRoles(String name);

    /**
     * 获取是否本人可见上级查看
     * @param companyId 公司Id
     * @return Boolean
     */
    Boolean getCheckBySuperior(String companyId);

    /**
     * 获取是否启用数据权限
     * @param companyId 公司Id
     * @return Boolean
     */
    Boolean getEnableDataPermissions(String companyId);

    /**
     * 获取数据可读用户Id集合
     * @param userId 用户Id
     * @param companyId 公司Id
     * @return List<String>
     */
    List<String> getDataReadableUserIds(String userId, String companyId);
}
