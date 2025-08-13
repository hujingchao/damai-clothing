package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.entity.RoleCustomFormColumn;

import java.util.Set;

/**
 * @author yzy
 */
public interface RoleCustomFormColumnService extends IService<RoleCustomFormColumn> {
    /**
     * 保存角色自定义表单列权限
     * @param roleCustomFormColumn 角色自定义表单列权限
     * @return Boolean
     */
    Boolean saveRoleCustomFormColumn(RoleCustomFormColumn roleCustomFormColumn);

    /**
     * 修改角色自定义表单列权限
     * @param roleCustomFormColumn 角色自定义表单列权限
     * @return Boolean
     */
    Boolean editRoleCustomFormColumn(RoleCustomFormColumn roleCustomFormColumn);

    /**
     * 删除角色自定义表单列权限
     * @param id ID
     * @return Boolean
     */
    Boolean deleteRoleCustomFormColumn(String id);

    /**
     * 通过角色ID获取角色自定义表单列权限
     * @param roleId 角色Id
     * @param customFormId 自定义表单Id
     * @return String 列内容
     */
    String getRoleCustomFormColumnsByRoleId(String roleId, String customFormId);

    /**
     * 通过角色ID获取角色自定义表单列权限
     * @param roleIds 角色Id拼接字符串
     * @param customFormId 自定义表单Id
     * @param companyId 公司Id
     * @return Set<String>
     */
     Set<String> getRoleCustomFormColumnIdsByRoleIds(String roleIds, String customFormId, String companyId);

}
