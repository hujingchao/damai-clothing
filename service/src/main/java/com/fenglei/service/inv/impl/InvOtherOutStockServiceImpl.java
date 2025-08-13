package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdMaterialDetailMapper;
import com.fenglei.mapper.inv.InvOtherOutStockMapper;
import com.fenglei.model.inv.entity.*;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdPositionService;
import com.fenglei.service.basedata.BdRepositoryService;
import com.fenglei.service.inv.IInvOtherOutStockItemService;
import com.fenglei.service.inv.IInvOtherOutStockService;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.system.ISysUserService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zgm
 * @since 2024-04-28
 */
@Service
public class InvOtherOutStockServiceImpl extends ServiceImpl<InvOtherOutStockMapper, InvOtherOutStock> implements IInvOtherOutStockService {

    @Resource
    IInvOtherOutStockItemService otherOutStockItemService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private BdRepositoryService repositoryService;
    @Resource
    private BdPositionService positionService;
    @Resource
    private BdMaterialDetailMapper materialDetailMapper;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;
    @Resource
    private InvInventoryService invInventoryService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvOtherOutStock add(InvOtherOutStock invOtherOutStock) {
        invOtherOutStock.setCreatorId(RequestUtils.getUserId());
        save(invOtherOutStock);
        List<InvOtherOutStockItem> outStockItemList = invOtherOutStock.getItemList();
        for (InvOtherOutStockItem outStockItem : outStockItemList) {
            outStockItem.setPid(invOtherOutStock.getId());
        }
        otherOutStockItemService.saveBatch(outStockItemList);
        return invOtherOutStock;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        InvOtherOutStock byId = this.getById(id);
        if(byId == null){
            return true;
        }
        if (byId.getBillStatus() == Constants.INT_STATUS_APPROVING || byId.getBillStatus() == Constants.INT_STATUS_AUDITED) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        otherOutStockItemService.remove(new LambdaQueryWrapper<InvOtherOutStockItem>().eq(InvOtherOutStockItem::getPid, id));
        return removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        List<InvOtherOutStock> list = this.listByIds(ids);
        if(!list.isEmpty()){
            for (InvOtherOutStock byId : list) {
                if (byId.getBillStatus() == Constants.INT_STATUS_APPROVING || byId.getBillStatus() == Constants.INT_STATUS_AUDITED) {
                    throw new BizException(byId.getNo()+ "流转中 或 已审核 数据无法删除");
                }
            }
        }
        otherOutStockItemService.remove(new LambdaQueryWrapper<InvOtherOutStockItem>().in(InvOtherOutStockItem::getPid, ids));
        return removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(InvOtherOutStock otherOutStock) {
        otherOutStock.setUpdaterId(RequestUtils.getUserId());
        if (!this.updateById(otherOutStock)) {
            throw new BizException("更新失败");
        }
        String pid = otherOutStock.getId();
        otherOutStockItemService.remove(new LambdaQueryWrapper<InvOtherOutStockItem>().eq(InvOtherOutStockItem::getPid, pid));
        otherOutStock.getItemList().forEach(t -> t.setPid(pid));
        if (!otherOutStockItemService.saveBatch(otherOutStock.getItemList())) {
            throw new BizException("更新失败");
        }
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<InvOtherOutStock> myPage(Page page, InvOtherOutStock invOtherOutStock) {
        List<String> stockerIdList;
        List<String> billStatusList;
        if(StringUtils.isNotEmpty(invOtherOutStock.getStockerIds())){
            stockerIdList = Arrays.asList(invOtherOutStock.getStockerIds().split(","));
            invOtherOutStock.setStockerIdList(stockerIdList);
        }
        if(StringUtils.isNotEmpty(invOtherOutStock.getInbillStatus())){
            billStatusList = Arrays.asList(invOtherOutStock.getInbillStatus().split(","));
            invOtherOutStock.setInbillStatusList(billStatusList);
        }

        IPage<InvOtherOutStock>  result= this.baseMapper.selectOutStock(page,invOtherOutStock);
        return result;
    }

    /**
     * 详情
     */
    @Override
    public InvOtherOutStock detail(String id) {
       return this.baseMapper.detailOutStock(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvOtherOutStock submit(String id) {
        InvOtherOutStock result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getBillStatus() != Constants.INT_STATUS_CREATE && result.getBillStatus() != Constants.INT_STATUS_RESUBMIT) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }
        result = this.auditUptData(result);
        if (!this.updateById(result)) {
            throw new BizException("操作失败");
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public InvOtherOutStock auditUptData(InvOtherOutStock otherOutStock) {
        otherOutStock.setBillStatus(Constants.INT_STATUS_AUDITED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        otherOutStock.setAuditTime(sdf.format(new Date()));
        otherOutStock.setAuditorId(RequestUtils.getUserId());
        otherOutStock.setAuditor(RequestUtils.getNickname());
        List<InvOtherOutStockItem> otherOutStockItemList = otherOutStock.getItemList();
        if (otherOutStockItemList != null && !otherOutStockItemList.isEmpty()) {
                List<InvInventory> invs = new ArrayList<>();
                List<InvIoBill> invIoBills = new ArrayList<>();
                for (InvOtherOutStockItem outStockItem : otherOutStockItemList) {
                    InvInventory inv = new InvInventory();
                    inv.setId(outStockItem.getInvId());
                    inv.setMaterialDetailId(outStockItem.getMaterialDetailId());
                    inv.setQty(outStockItem.getOutQty());
                    inv.setPiQty(outStockItem.getOutPiQty());
                    inv.setRepositoryId(outStockItem.getRepositoryId());
                    inv.setPositionId(StringUtils.isNotEmpty(outStockItem.getPositionId()) ? outStockItem.getPositionId() : "0");
                    inv.setPrice(outStockItem.getPrice());
                    inv.setLot(outStockItem.getLot());
                    invs.add(inv);

                    InvIoBill invIoBill = new InvIoBill();
                    invIoBill.setSrcId(outStockItem.getPid());
                    invIoBill.setSrcItemId(outStockItem.getId());
                    invIoBill.setSrcType("其他出库单");
                    invIoBills.add(invIoBill);
                }
                if (!invInventoryService.batchSubQty(invs, invIoBills, 1)) {
                    throw new BizException("即时库存更新失败");
                }
        }
        return otherOutStock;
    }



    @Transactional(rollbackFor = Exception.class)
    public InvOtherOutStock unAuditUptData(InvOtherOutStock otherOutStock) {
        otherOutStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        otherOutStock.setAuditTime(sdf.format(new Date()));
        otherOutStock.setAuditorId(RequestUtils.getUserId());
        otherOutStock.setAuditor(RequestUtils.getNickname());

        List<InvOtherOutStockItem> otherOutStockItemList = otherOutStock.getItemList();
        if (otherOutStockItemList != null && !otherOutStockItemList.isEmpty()) {
            List<InvInventory> invs = new ArrayList<>();
            List<InvIoBill> invIoBills = new ArrayList<>();
            for (InvOtherOutStockItem outStockItem : otherOutStockItemList) {
                InvInventory inv = new InvInventory();
                inv.setId(outStockItem.getInvId());
                inv.setMaterialDetailId(outStockItem.getMaterialDetailId());
                inv.setQty(outStockItem.getOutQty());
                inv.setPiQty(outStockItem.getOutPiQty());
                inv.setRepositoryId(outStockItem.getRepositoryId());
                inv.setPositionId(StringUtils.isNotEmpty(outStockItem.getPositionId()) ? outStockItem.getPositionId() : "0");
                inv.setPrice(outStockItem.getPrice());
                inv.setLot(outStockItem.getLot());
                invs.add(inv);

                InvIoBill invIoBill = new InvIoBill();
                invIoBill.setSrcId(outStockItem.getPid());
                invIoBill.setSrcItemId(outStockItem.getId());
                invIoBill.setSrcType("其他出库单");
                invIoBills.add(invIoBill);
            }
            if (!invInventoryService.batchAddQty(invs, invIoBills, 2)) {
                throw new BizException("即时库存更新失败");
            }
        }
        updateById(otherOutStock);
        return otherOutStock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<InvOtherOutStock> list = this.list(
                new LambdaQueryWrapper<InvOtherOutStock>()
                        .in(InvOtherOutStock::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<InvOtherOutStock> submitList = list.stream().filter(r -> (r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (InvOtherOutStock otherOutStock : submitList) {
                    this.submit(otherOutStock.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(InvOtherOutStock otherOutStock) throws Exception {
        if (otherOutStock.getBillStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(otherOutStock.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(otherOutStock.getWorkFlowId());
            flowOperationInfo.setFormData(otherOutStock);
            flowOperationInfo.setUserId(otherOutStock.getUserId());
            flowOperationInfo.setChildNodes(otherOutStock.getChildNodes());
            flowOperationInfo.setCurrentNodeId(otherOutStock.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(otherOutStock.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                otherOutStock.setWorkFlowId("");
            } else {
                // 审批流程
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
                List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
                Boolean next = circulationOperationService.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId());
                if (!next) {
                    throw new BizException("流程提交失败");
                }
            }
            List<String> ids = new ArrayList<>();
            ids.add(otherOutStock.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, otherOutStock.getWorkFlow().getId());
            otherOutStock.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            otherOutStock.setNodeStatus(currentNodes.get(0).getStatus());
            otherOutStock.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(otherOutStock.getId()) == 1) {
                otherOutStock = this.auditUptData(otherOutStock);
            }
            // 驳回
            if (circulationOperationService.whetherLast(otherOutStock.getId()) == 2) {
                otherOutStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(otherOutStock)) {
            throw new BizException("操作失败");
        }

        return Result.success(otherOutStock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(InvOtherOutStock otherOutStock) throws Exception {
        List<String> ids = otherOutStock.getIds();
        List<InvOtherOutStock> mos = this.list(
                new LambdaQueryWrapper<InvOtherOutStock>()
                        .in(InvOtherOutStock::getId, ids)
        );
        if (mos != null && mos.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, otherOutStock.getWorkFlow().getId());
            for (int i = 0; i < mos.size(); i++) {
                InvOtherOutStock item = mos.get(i);
                item.setBillStatus(otherOutStock.getBillStatus());
                item.setWorkFlowId(otherOutStock.getWorkFlowId());
                item.setUserId(otherOutStock.getUserId());
                item.setChildNodes(otherOutStock.getChildNodes());
                item.setChildNodeApprovalResult(otherOutStock.getChildNodeApprovalResult());
                item.setWorkFlow(otherOutStock.getWorkFlow());
                for (int j = 0; j < childNodes.size(); j++) {
                    if (childNodes.get(j).getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNodes.get(j).getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNodes.get(j).getStatus());
                        item.setCurrentNodeId(childNodes.get(j).getId());
                        break;
                    }
                }
                Result result = doAction(item);
                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
                    throw new BizException("操作失败");
                }
            }
        }

        return Result.success("操作成功");
    }

    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId) {
        FlowOperationInfo flowOperationInfo =new FlowOperationInfo();
        FlowUser flowUser = new FlowUser();
        flowUser.setEmployeeName(RequestUtils.getNickname());
        flowUser.setId(RequestUtils.getUserId());
        flowOperationInfo.setFlowUser(flowUser);
        flowOperationInfo.setFormIds(ids);
        flowOperationInfo.setWorkFlowId(workFlowId);

        List<ChildNode> childNodeList = circulationOperationService.getCurrentNodes(flowOperationInfo);
        return childNodeList;
    }


    @Override
    public InvOtherOutStock unAudit(String id) throws Exception {
        InvOtherOutStock result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getBillStatus() != Constants.INT_STATUS_AUDITED) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        List<String> ids = new ArrayList<>();
        ids.add(id);
        result = this.unAuditUptData(result);
        return result;
    }


    @Override
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<InvOtherOutStock> list = this.listByIds(idList);
        if (list != null && list.size() > 0) {
            List<InvOtherOutStock> unAuditList = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (InvOtherOutStock otherOutStock : unAuditList) {
                    this.unAudit(otherOutStock.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }


}
