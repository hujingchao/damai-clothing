package com.fenglei.service.workFlow.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.workFlow.WorkFlowMapper;
import com.fenglei.model.workFlow.dto.Option;
import com.fenglei.model.workFlow.entity.*;
import com.fenglei.service.workFlow.CustomFormService;
import com.fenglei.service.workFlow.ITableInfoItemService;
import com.fenglei.service.workFlow.ITableInfoService;
import com.fenglei.service.workFlow.WorkFlowService;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author yzy
 */
@Service
public class WorkFlowServiceImpl extends ServiceImpl<WorkFlowMapper, WorkFlow> implements WorkFlowService {

    @Resource
    private ITableInfoService iTableInfoService;
    @Resource
    private CustomFormService customFormService;
    @Resource
    private ITableInfoItemService iTableInfoItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveWorkFlow(WorkFlow workFlow) {
        List<WorkFlow> workFlows = list(new QueryWrapper<WorkFlow>().lambda().eq(WorkFlow::getTableId, workFlow.getTableId()));
        if (workFlows.size() > ChildNodeUtil.NUMBER_INT_0) {
            throw new BizException("该关联表单已存在流程！");
        }
        workFlow.setStatus(false);
        return save(workFlow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editWorkFlow(WorkFlow workFlow) {
        List<WorkFlow> workFlows = list(new QueryWrapper<WorkFlow>().lambda().eq(WorkFlow::getTableId, workFlow.getTableId()));
        if (workFlows.size() > ChildNodeUtil.NUMBER_INT_1) {
            throw new BizException("该关联表单已存在流程！");
        }
        boolean flag;
        flag = saveOrUpdate(workFlow);
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWorkFlow(String id) {
        return removeById(id);
    }

    @Override
    public IPage<WorkFlow> workFlowPage(Page page, WorkFlow workFlow) {
        IPage<WorkFlow> iPage = page(page, new LambdaQueryWrapper<WorkFlow>()
                .like(StringUtils.isNotEmpty(workFlow.getFlowName()), WorkFlow::getFlowName, workFlow.getFlowName())
                .eq(StringUtils.isNotEmpty(workFlow.getFlowType()), WorkFlow::getFlowType, workFlow.getFlowType())
                .orderByDesc(WorkFlow::getCreateTime));
        List<WorkFlow> workFlows = iPage.getRecords();
        List<TableInfo> tableInfos = iTableInfoService.list();
        List<CustomForm> customForms = customFormService.list();
        for (WorkFlow flow : workFlows) {
            if (ChildNodeUtil.FLOW_TYPE_OUT.equals(flow.getFlowType())) {
                for (TableInfo tableInfo : tableInfos) {
                    if (flow.getTableId().equals(tableInfo.getId())) {
                        flow.setTableName(tableInfo.getTableName());
                    }
                }
            } else if (ChildNodeUtil.FLOW_TYPE_IN.equals(flow.getFlowType())) {
                for (CustomForm customForm : customForms) {
                    if (flow.getTableId().equals(customForm.getId())) {
                        flow.setTableName(customForm.getTitle());
                    }
                }
            }
        }
        iPage.setRecords(workFlows);
        return iPage;
    }

    @Override
    public WorkFlow getOneWorkFlow(String id) {
        return getById(id);
    }

    @Override
    public List<Condition> getConditions(String tableId, String flowType) {
        List<Condition> conditions = new ArrayList<>();
        if (flowType.equals(ChildNodeUtil.FLOW_TYPE_OUT)) {
            List<TableInfoItem> tableInfoItems = iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().eq(TableInfoItem::getTableInfoId, tableId).eq(TableInfoItem::getIsItAnApprovalCondition, true));
            for (TableInfoItem tableInfoItem : tableInfoItems) {
                Condition condition = new Condition();
                condition.setColumnId(tableInfoItem.getId());
                condition.setColumnName(tableInfoItem.getFieldName());
                condition.setType(ChildNodeUtil.NUMBER_2);
                condition.setColumnDbname(tableInfoItem.getFieldCode());
                condition.setColumnType(tableInfoItem.getFieldType());
                condition.setShowType(ChildNodeUtil.NUMBER_3);
                condition.setZdy1(ChildNodeUtil.NUMBER_1);
                condition.setShowName(tableInfoItem.getFieldName());
                condition.setFixedDownBoxValue(null);
                condition.setTableId(tableInfoItem.getTableInfoId());
                conditions.add(condition);
            }
        } else if (flowType.equals(ChildNodeUtil.FLOW_TYPE_IN)) {
            CustomForm customForm = customFormService.getById(tableId);
            String content = customForm.getContent();
            JSONArray jsonArray = JSONArray.parseArray(content);
            for(int i = ChildNodeUtil.NUMBER_INT_0; i < jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = (String) jsonObject.get("id");
                if (!"generalButton".equals(id)) {
                    Condition condition = new Condition();
                    String columnType = (String) jsonObject.get("columnType");
                    String columnId = (String) jsonObject.get("uuid");
                    String label = (String) jsonObject.get("label");
                    String column = (String) jsonObject.get("column");

                    condition.setColumnId(columnId);
                    condition.setColumnName(label);
                    condition.setType(ChildNodeUtil.NUMBER_2);
                    condition.setColumnDbname(column);
                    condition.setColumnType(columnType);

                    if ("Boolean".equals(columnType)) {
                        condition.setShowType(ChildNodeUtil.NUMBER_1);
                    } else if ("Number".equals(columnType)) {
                        condition.setShowType(ChildNodeUtil.NUMBER_2);
                    } else if ("Double".equals(columnType)) {
                        condition.setShowType(ChildNodeUtil.NUMBER_10);
                    } else if ("String".equals(columnType)) {
                        if ("singleLineInputBox".equals(id) || "multilineInputBox".equals(id)) {
                            condition.setShowType(ChildNodeUtil.NUMBER_3);
                        }
                        if ("singleSelectBox".equals(id) || "singleBox".equals(id) || "checkbox".equals(id)) {
                            if ("singleSelectBox".equals(id)) {
                                condition.setShowType(ChildNodeUtil.NUMBER_4);
                            }
                            if ("singleBox".equals(id)) {
                                condition.setShowType(ChildNodeUtil.NUMBER_5);
                            }
                            if ("checkbox".equals(id)) {
                                condition.setShowType(ChildNodeUtil.NUMBER_6);
                            }
                            if (Objects.nonNull(jsonObject.get("options"))) {
                                condition.setOptions(getOptions((JSONArray)jsonObject.get("options")));
                            }
                        }
                        condition.setZdy1(ChildNodeUtil.NUMBER_1);
                    } else {
                        condition.setShowType(ChildNodeUtil.NUMBER_3);
                    }
                    condition.setShowName(label);
                    condition.setTableId(customForm.getId());
                    conditions.add(condition);
                }
            }
        }
        return conditions;
    }

    public List<Option> getOptions (JSONArray jsonArray1) {
        List<Option> optionList = new ArrayList<>();
        Option option;
        JSONObject jsonObject1;
        for(int j = ChildNodeUtil.NUMBER_INT_0; j < jsonArray1.size(); j++){
            option = new Option();
            jsonObject1 = jsonArray1.getJSONObject(j);
            option.setIdx((Integer)jsonObject1.get("idx"));
            option.setLabel((String)jsonObject1.get("label"));
            option.setValue((String)jsonObject1.get("value"));
            optionList.add(option);
        }
        return optionList;
    }
}
