package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.entity.RoleTableInfoItem;
import com.fenglei.model.workFlow.entity.TableInfoItem;

import java.util.List;

/**
 * @author yzy
 */
public interface RoleTableInfoItemService extends IService<RoleTableInfoItem> {

    /**
     * 保存映射关系
     * @param roleTableInfoItem 映射
     * @return Boolean
     */
    Boolean saveRoleTableInfoItem(RoleTableInfoItem roleTableInfoItem);

    /**
     * 修改映射关系
     * @param roleTableInfoItem 映射
     * @return Boolean
     */
    Boolean editRoleTableInfoItem(RoleTableInfoItem roleTableInfoItem);

    /**
     * 删除映射关系
     * @param id 映射Id
     * @return Boolean
     */
    Boolean deleteRoleTableInfoItem(String id);

    /**
     * 通过角色ID获取角色表单子项映射
     * @param roleIds 角色Ids
     * @param tableId 表单Id
     * @return List<TableInfoItem>
     */
    List<TableInfoItem> getRoleTableInfoItemsByRoleId(String roleIds, String tableId);

}
