package com.fenglei.service.workFlow.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.WorkFlowInstantiateMapper;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.*;
import com.fenglei.service.workFlow.*;
import com.fenglei.service.workFlow.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
@Service
public class WorkFlowInstantiateServiceImpl extends ServiceImpl<WorkFlowInstantiateMapper, WorkFlowInstantiate> implements WorkFlowInstantiateService {

    @Resource
    private ChildNodeService childNodeService;
    @Resource
    private ConditionService conditionService;
    @Resource
    private NodeUserService nodeUserService;
    @Resource
    private WorkFlowDefService workFlowDefService;
    private static final String COLUMN_NAME_DEF = "workFlowDef";
    private static final String COLUMN_NAME_CONFIG = "nodeConfig";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkFlowInstantiate saveWorkFlowInstantiate(WorkFlow workFlow, String formId, String userId, List<ChildNode> ccChildNodes, List<FlowUser> flowUsers) throws Exception {
        String content = workFlow.getContent();
        JSONObject jsonObject = JSON.parseObject(content);
        WorkFlowInstantiate workFlowInstantiate = new WorkFlowInstantiate();
        String workFlowInstantiateId = UUIDUTIL.getUUID();
        workFlowInstantiate.setId(workFlowInstantiateId);
        workFlowInstantiate.setTableId(workFlow.getTableId());
        workFlowInstantiate.setWorkFlowId(workFlow.getId());
        workFlowInstantiate.setFormId(formId);
        workFlowInstantiate.setStatus(ChildNodeUtil.NUMBER_INT_0);
        workFlowInstantiate.setWhenRejectedWhetherToDirectlyVoidTheProcess(true);
        workFlowInstantiate.setWorkFlowVersionId(workFlow.getWorkFlowVersionId());
        workFlowInstantiate.setDirectorMaxLevel(workFlow.getDirectorMaxLevel());
        workFlowInstantiate.setFlowPermission(workFlow.getFlowPermission());
//        workFlowInstantiate.setWhenRejectedWhetherToDirectlyVoidTheProcess(workFlow.getWhenRejectedWhetherToDirectlyVoidTheProcess());
        workFlowInstantiate.setInitiatorId(userId);
        if (Objects.nonNull(jsonObject.get(COLUMN_NAME_DEF))) {
            JSONObject workFlowDef = (JSONObject)jsonObject.get("workFlowDef");
            WorkFlowDef workFlowDef1;
            if (Objects.nonNull(workFlowDef)) {
                workFlowDef1 = JSONObject.toJavaObject(workFlowDef, WorkFlowDef.class);
                workFlowDef1.setWorkFlowInstantiateId(workFlowInstantiateId);
                workFlowDefService.save(workFlowDef1);
                workFlowInstantiate.setWorkFlowDef(workFlowDef1);
                System.out.println("------>");
            }
        }
        if (Objects.nonNull(jsonObject.get(COLUMN_NAME_CONFIG))) {
            JSONObject nodeConfig = (JSONObject)jsonObject.get("nodeConfig");
            if (Objects.nonNull(nodeConfig)) {
                Map<String, Object> map = parsingNode(nodeConfig, workFlowInstantiateId, ccChildNodes, flowUsers);
                List<ChildNode> childNodes = ClassConversionTools.castList(map.get("childNodes"), ChildNode.class);
                List<NodeUser> nodeUserList = ClassConversionTools.castList(map.get("nodeUserList"), NodeUser.class);
                List<Condition> conditionList = ClassConversionTools.castList (map.get("conditionList"), Condition.class);
                List<ChildNode> conditionNodes = ClassConversionTools.castList(map.get("conditionNodes"), ChildNode.class);
                if (conditionNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
                    for (ChildNode conditionNode : conditionNodes) {
                        conditionNode.setWorkFlowInstantiateId(workFlowInstantiateId);
                    }
                    childNodeService.saveOrUpdateBatch(conditionNodes);
                }
                if (childNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
                    for (ChildNode childNode : childNodes) {
                        childNode.setWorkFlowInstantiateId(workFlowInstantiateId);
                    }
                    childNodeService.saveOrUpdateBatch(childNodes);
                }
                if (nodeUserList.size() > ChildNodeUtil.NUMBER_INT_0) {
                    for (NodeUser nodeUser : nodeUserList) {
                        nodeUser.setWorkFlowInstantiateId(workFlowInstantiateId);
                    }
                    nodeUserService.saveOrUpdateBatch(nodeUserList);
                }
                if (conditionList.size() > ChildNodeUtil.NUMBER_INT_0) {
                    for (Condition condition : conditionList) {
                        condition.setWorkFlowInstantiateId(workFlowInstantiateId);
                    }
                    conditionService.saveOrUpdateBatch(conditionList);
                }
                workFlowInstantiate.setConditionNodes(conditionNodes);
                workFlowInstantiate.setChildNodes(childNodes);
                workFlowInstantiate.setNodeUserList(nodeUserList);
                workFlowInstantiate.setConditionList(conditionList);
            }
        }
        saveOrUpdate(workFlowInstantiate);
        return workFlowInstantiate;
    }

    private Map<String, Object> parsingNode (JSON nodeConfig, String workFlowInstantiateId, List<ChildNode> ccChildNodes, List<FlowUser> flowUsers) {
        ChildNode childNode1 = JSONObject.toJavaObject(nodeConfig, ChildNode.class);
        ChildNode childNode2 = AnalysisJson.getChildNode(childNode1, workFlowInstantiateId);
        AnalysisJson analysisJson = new AnalysisJson();
        Map<String, Object> map = analysisJson.getChildNodeToList(childNode2);
        List<ChildNode> conditionNodes = ClassConversionTools.castList(map.get("conditionNodes"), ChildNode.class);
        for (ChildNode conditionNode : conditionNodes) {
            conditionNode.setStatus(ChildNodeUtil.NUMBER_INT_2);
        }
        List<ChildNode> childNodes = ClassConversionTools.castList(map.get("childNodes"), ChildNode.class);
        List<NodeUser> nodeUserList = ClassConversionTools.castList(map.get("nodeUserList"), NodeUser.class);
        List<Condition> conditionList = ClassConversionTools.castList (map.get("conditionList"), Condition.class);
        List<ChildNode> ccChildNodeList =
                childNodes.stream().filter(c -> ChildNodeUtil.NODE_TYPE_CC.equals(c.getType())).collect(Collectors.toList());
        List<NodeUser> newCcNodeUsers = new ArrayList<>();
        if (ccChildNodes.size() > 0) {
            for (int i = 0; i < ccChildNodeList.size(); i++) {
                if (i < ccChildNodes.size()) {
                    ChildNode ccChildNode = ccChildNodeList.get(i);
                    ChildNode ccChildNode1 = ccChildNodes.get(i);
                    for (int j = 0; j < nodeUserList.size(); j++) {
                        if (nodeUserList.get(j).getChildNodeId().equals(ccChildNode.getId())) {
                            nodeUserList.remove(j);
                            j--;
                        }
                    }
                    List<String> nodeUserIds = ccChildNode1.getNodeUserIds();
                    for (int k = ChildNodeUtil.NUMBER_INT_0; k < nodeUserIds.size(); k++) {
                        NodeUser nodeUser = new NodeUser();
                        nodeUser.setTargetId(nodeUserIds.get(k));
                        nodeUser.setType(ChildNodeUtil.NUMBER_1);
                        nodeUser.setChildNodeId(ccChildNode.getChildNodeId());
                        nodeUser.setWorkFlowInstantiateId(workFlowInstantiateId);
                        nodeUser.setSortIndex(k);
                        newCcNodeUsers.add(nodeUser);
                    }
                }
            }
            for (NodeUser newCcNodeUser : newCcNodeUsers) {
                for (FlowUser flowUser : flowUsers) {
                    if (newCcNodeUser.getTargetId().equals(flowUser.getId())) {
                        newCcNodeUser.setName(flowUser.getEmployeeName());
                    }
                }
            }
            nodeUserList.addAll(newCcNodeUsers);
        }
        Map<String, Object> map1 = new HashMap<>(4);
        map1.put("childNodes", childNodes);
        map1.put("nodeUserList", nodeUserList);
        map1.put("conditionList", conditionList);
        map1.put("conditionNodes", conditionNodes);
        return map1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWorkFlowInstantiate(String id){
        childNodeService.remove(new UpdateWrapper<ChildNode>().lambda().eq(ChildNode::getWorkFlowInstantiateId, id));
        nodeUserService.remove(new UpdateWrapper<NodeUser>().lambda().eq(NodeUser::getWorkFlowInstantiateId, id));
        conditionService.remove(new UpdateWrapper<Condition>().lambda().eq(Condition::getWorkFlowInstantiateId, id));
        return removeById(id);
    }

    @Override
    public IPage<WorkFlowInstantiate> workFlowInstantiatePage(Page<WorkFlowInstantiate> page, WorkFlowInstantiate workFlowInstantiate) {
        return page(page, new QueryWrapper<WorkFlowInstantiate>().lambda().orderByDesc(WorkFlowInstantiate::getCreateTime));
    }

    @Override
    public WorkFlowInstantiate getOneWorkFlowInstantiate(String id) {
        WorkFlowInstantiate workFlowInstantiate = getById(id);
        List<ChildNode> childNodes = childNodeService.list(new QueryWrapper<ChildNode>().lambda().eq(ChildNode::getWorkFlowInstantiateId, workFlowInstantiate.getId()));
        List<Condition> conditions = conditionService.list(new QueryWrapper<Condition>().lambda().eq(Condition::getWorkFlowInstantiateId, workFlowInstantiate.getId()));
        List<NodeUser> nodeUsers = nodeUserService.list(new QueryWrapper<NodeUser>().lambda().eq(NodeUser::getWorkFlowInstantiateId, workFlowInstantiate.getId()));
        List<WorkFlowDef> workFlowDefs = workFlowDefService.list(new QueryWrapper<WorkFlowDef>().lambda().eq(WorkFlowDef::getWorkFlowInstantiateId, workFlowInstantiate.getId()));
        List<ChildNode> resultChildNodes = FormatToTree.parseTree(childNodes, conditions, nodeUsers);
        if (resultChildNodes.size() > ChildNodeUtil.NUMBER_INT_0) {
            workFlowInstantiate.setChildNode(resultChildNodes.get(ChildNodeUtil.NUMBER_INT_0));
        }
        if (workFlowDefs.size() > ChildNodeUtil.NUMBER_INT_0) {
            workFlowInstantiate.setWorkFlowDef(workFlowDefs.get(ChildNodeUtil.NUMBER_INT_0));
        }
        return workFlowInstantiate;
    }

}
