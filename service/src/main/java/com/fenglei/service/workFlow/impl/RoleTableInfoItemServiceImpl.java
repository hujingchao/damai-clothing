package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.RoleTableInfoItemMapper;
import com.fenglei.model.workFlow.entity.RoleTableInfoItem;
import com.fenglei.model.workFlow.entity.TableInfoItem;
import com.fenglei.service.workFlow.ITableInfoItemService;
import com.fenglei.service.workFlow.RoleTableInfoItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
@Service
public class RoleTableInfoItemServiceImpl extends ServiceImpl<RoleTableInfoItemMapper, RoleTableInfoItem> implements RoleTableInfoItemService {

    @Resource
    private ITableInfoItemService iTableInfoItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleTableInfoItem(RoleTableInfoItem roleTableInfoItem) {
        remove(new UpdateWrapper<RoleTableInfoItem>().lambda().eq(RoleTableInfoItem::getTableInfoId, roleTableInfoItem.getTableInfoId()));
        List<RoleTableInfoItem> roleTableInfoItems = roleTableInfoItem.getRoleTableInfoItems();
        return saveOrUpdateBatch(roleTableInfoItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editRoleTableInfoItem(RoleTableInfoItem roleTableInfoItem) {
        remove(new UpdateWrapper<RoleTableInfoItem>().lambda().eq(RoleTableInfoItem::getTableInfoId, roleTableInfoItem.getTableInfoId()));
        List<RoleTableInfoItem> roleTableInfoItems = roleTableInfoItem.getRoleTableInfoItems();
        return saveOrUpdateBatch(roleTableInfoItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleTableInfoItem(String id) {
        return removeById(id);
    }

    @Override
    public List<TableInfoItem> getRoleTableInfoItemsByRoleId(String roleIds, String tableInfoId) {
        List<RoleTableInfoItem> roleTableInfoItems = list(new LambdaQueryWrapper<RoleTableInfoItem>().in(RoleTableInfoItem::getRoleId, roleIds).eq(RoleTableInfoItem::getTableInfoId, tableInfoId));
        Set<String> tableInfoItemIds = roleTableInfoItems.stream().map(RoleTableInfoItem::getTableInfoItemId).collect(Collectors.toSet());
        List<TableInfoItem> tableInfoItems = new ArrayList<>();
        if (tableInfoItemIds.size() > 0) {
            tableInfoItems = iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().in(TableInfoItem::getId, tableInfoItemIds));
        }
        return tableInfoItems;
    }

}
