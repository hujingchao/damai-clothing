package com.fenglei.service.workFlow.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.RoleCustomFormColumnMapper;
import com.fenglei.model.workFlow.entity.RoleCustomFormColumn;
import com.fenglei.service.workFlow.RoleCustomFormColumnService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yzy
 */
@Service
public class RoleCustomFormColumnServiceImpl extends ServiceImpl<RoleCustomFormColumnMapper, RoleCustomFormColumn> implements RoleCustomFormColumnService {

    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleCustomFormColumn(RoleCustomFormColumn roleCustomFormColumn) {
        List<RoleCustomFormColumn> roleCustomFormColumns = list(new QueryWrapper<RoleCustomFormColumn>().lambda()
                .eq(RoleCustomFormColumn::getCustomFormId, roleCustomFormColumn.getCustomFormId())
                .eq(RoleCustomFormColumn::getRoleId, roleCustomFormColumn.getRoleId())
        );
        boolean flag;
        if (roleCustomFormColumns.size() > 0) {
            RoleCustomFormColumn oldRoleCustomFormColumn = roleCustomFormColumns.get(0);
            oldRoleCustomFormColumn.setContent(roleCustomFormColumn.getContent());
            flag = saveOrUpdate(oldRoleCustomFormColumn);
        } else {
            flag = saveOrUpdate(roleCustomFormColumn);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editRoleCustomFormColumn(RoleCustomFormColumn roleCustomFormColumn) {
        List<RoleCustomFormColumn> roleCustomFormColumns = list(new QueryWrapper<RoleCustomFormColumn>().lambda()
                .eq(RoleCustomFormColumn::getCustomFormId, roleCustomFormColumn.getCustomFormId())
                .eq(RoleCustomFormColumn::getRoleId, roleCustomFormColumn.getRoleId())
        );
        boolean flag = false;
        if (roleCustomFormColumns.size() > 0) {
            RoleCustomFormColumn oldRoleCustomFormColumn = roleCustomFormColumns.get(0);
            oldRoleCustomFormColumn.setContent(roleCustomFormColumn.getContent());
            flag = saveOrUpdate(oldRoleCustomFormColumn);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRoleCustomFormColumn(String id) {
        return removeById(id);
    }

    @Override
    public String getRoleCustomFormColumnsByRoleId(String roleId, String customFormId) {
        List<RoleCustomFormColumn> roleCustomFormColumns = list(new QueryWrapper<RoleCustomFormColumn>().lambda()
                .eq(RoleCustomFormColumn::getCustomFormId, customFormId)
                .eq(RoleCustomFormColumn::getRoleId, roleId)
        );
        if (roleCustomFormColumns.size() > 0) {
            return roleCustomFormColumns.get(0).getContent();
        }
        return null;
    }

    @Override
    public Set<String> getRoleCustomFormColumnIdsByRoleIds(String roleIds, String customFormId, String companyId) {
        Set<String> result = new HashSet<>();
        if (systemInformationAcquisitionService.getEnableDataPermissions(companyId)) {
            String[] roleIdArr = roleIds.split(",");
            List<RoleCustomFormColumn> roleCustomFormColumns = list(new QueryWrapper<RoleCustomFormColumn>().lambda()
                    .eq(RoleCustomFormColumn::getCustomFormId, customFormId)
                    .in(RoleCustomFormColumn::getRoleId, Arrays.asList(roleIdArr))
            );

            if (roleCustomFormColumns.size() > 0) {
                List<Map<String, Object>> maps = new ArrayList<>();
                for (RoleCustomFormColumn roleCustomFormColumn : roleCustomFormColumns) {
                    JSONArray listObjectThir = JSONArray.parseArray(roleCustomFormColumn.getContent());
                    for (Object o : listObjectThir) {
                        maps.add(JSONObject.parseObject(JSONObject.toJSONString(o), Map.class));
                    }
                }
                for (Map<String, Object> map : maps) {
                    result.add(map.get("uuid").toString());
                }
            }
        }
        return result;
    }
}
