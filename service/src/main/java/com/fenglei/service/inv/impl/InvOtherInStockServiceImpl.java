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
import com.fenglei.mapper.inv.InvOtherInStockMapper;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.inv.entity.InvOtherInStock;
import com.fenglei.model.inv.entity.InvOtherInStockItem;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdPositionService;
import com.fenglei.service.basedata.BdRepositoryService;
import com.fenglei.service.inv.IInvOtherInStockItemService;
import com.fenglei.service.inv.IInvOtherInStockService;
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

@Service
public class InvOtherInStockServiceImpl extends ServiceImpl<InvOtherInStockMapper, InvOtherInStock> implements IInvOtherInStockService {

    @Resource
    IInvOtherInStockItemService otherInStockItemService;
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
    @Resource
    private BdMaterialDetailMapper bdMaterialDetailMapper;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvOtherInStock add(InvOtherInStock invOtherInStock) {
        invOtherInStock.setCreatorId(RequestUtils.getUserId());
        save(invOtherInStock);
        List<InvOtherInStockItem> outStockItemList = invOtherInStock.getItemList();
        for (InvOtherInStockItem outStockItem : outStockItemList) {

            BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
            srhMtrlDtl.setPid(outStockItem.getMaterialId());
            srhMtrlDtl.setColorId(outStockItem.getColorId());
            srhMtrlDtl.setSpecificationId(outStockItem.getSpecificationId());
            List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
            if (details != null && details.size() > 0) {
                outStockItem.setMaterialDetailId(details.get(0).getId());
            } else {
                throw new BizException("物料信息异常 名称：" + outStockItem.getMaterialName() + "  请选择正确的颜色/色号 和 规格");
            }
            outStockItem.setPid(invOtherInStock.getId());
        }
        otherInStockItemService.saveBatch(outStockItemList);
        return invOtherInStock;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        InvOtherInStock byId = this.getById(id);
        if(byId == null){
            return true;
        }
        if (byId.getBillStatus() == Constants.INT_STATUS_APPROVING || byId.getBillStatus() == Constants.INT_STATUS_AUDITED) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        otherInStockItemService.remove(new LambdaQueryWrapper<InvOtherInStockItem>().eq(InvOtherInStockItem::getPid, id));
        return removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        List<InvOtherInStock> list = this.listByIds(ids);
        if(!list.isEmpty()){
            for (InvOtherInStock byId : list) {
                if (byId.getBillStatus() == Constants.INT_STATUS_APPROVING || byId.getBillStatus() == Constants.INT_STATUS_AUDITED) {
                    throw new BizException(byId.getNo()+ "流转中 或 已审核 数据无法删除");
                }
            }
        }
        otherInStockItemService.remove(new LambdaQueryWrapper<InvOtherInStockItem>().in(InvOtherInStockItem::getPid, ids));
        return removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(InvOtherInStock otherInStock) {
        otherInStock.setUpdaterId(RequestUtils.getUserId());
        if (!this.updateById(otherInStock)) {
            throw new BizException("更新失败");
        }
        String pid = otherInStock.getId();
        otherInStockItemService.remove(new LambdaQueryWrapper<InvOtherInStockItem>().eq(InvOtherInStockItem::getPid, pid));
        for (InvOtherInStockItem outStockItem : otherInStock.getItemList()) {
            BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
            srhMtrlDtl.setPid(outStockItem.getMaterialId());
            srhMtrlDtl.setColorId(outStockItem.getColorId());
            srhMtrlDtl.setSpecificationId(outStockItem.getSpecificationId());
            List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
            if (details != null && details.size() > 0) {
                outStockItem.setMaterialDetailId(details.get(0).getId());
            } else {
                throw new BizException("物料信息异常 名称：" + outStockItem.getMaterialName() + "  请选择正确的颜色/色号 和 规格");
            }
            outStockItem.setPid(otherInStock.getId());
        }
        if (!otherInStockItemService.saveBatch(otherInStock.getItemList())) {
            throw new BizException("更新失败");
        }
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<InvOtherInStock> myPage(Page page, InvOtherInStock invOtherInStock) {
        List<String> stockerIdList;
        List<String> billStatusList;
        if (StringUtils.isNotEmpty(invOtherInStock.getStockerIds())) {
            stockerIdList = Arrays.asList(invOtherInStock.getStockerIds().split(","));
            invOtherInStock.setStockerIdList(stockerIdList);
        }
        if (StringUtils.isNotEmpty(invOtherInStock.getInbillStatus())) {
            billStatusList = Arrays.asList(invOtherInStock.getInbillStatus().split(","));
            invOtherInStock.setInbillStatusList(billStatusList);
        }

        IPage<InvOtherInStock> result = this.baseMapper.selectInStock(page, invOtherInStock);
        return result;
    }

    /**
     * 详情
     */
    @Override
    public InvOtherInStock detail(String id) {
        return this.baseMapper.detailInStock(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvOtherInStock submit(String id) {
        InvOtherInStock result = this.detail(id);
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
    public InvOtherInStock auditUptData(InvOtherInStock otherInStock) {
        otherInStock.setBillStatus(Constants.INT_STATUS_AUDITED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        otherInStock.setAuditTime(sdf.format(new Date()));
        otherInStock.setAuditorId(RequestUtils.getUserId());
        otherInStock.setAuditor(RequestUtils.getNickname());
        List<InvOtherInStockItem> otherInStockItemList = otherInStock.getItemList();
        if (otherInStockItemList != null && !otherInStockItemList.isEmpty()) {
            List<InvInventory> invs = new ArrayList<>();
            List<InvIoBill> invIoBills = new ArrayList<>();
            for (InvOtherInStockItem outStockItem : otherInStockItemList) {
                InvInventory inv = new InvInventory();
                inv.setMaterialDetailId(outStockItem.getMaterialDetailId());
                inv.setQty(outStockItem.getInQty());
                inv.setPiQty(outStockItem.getInPiQty());
                inv.setRepositoryId(outStockItem.getRepositoryId());
                inv.setPositionId(StringUtils.isNotEmpty(outStockItem.getPositionId()) ? outStockItem.getPositionId() : "0");
                inv.setPrice(outStockItem.getPrice());
                inv.setLot(outStockItem.getLot());
                invs.add(inv);

                InvIoBill invIoBill = new InvIoBill();
                invIoBill.setSrcId(outStockItem.getPid());
                invIoBill.setSrcItemId(outStockItem.getId());
                invIoBill.setSrcType("其他入库单");
                invIoBills.add(invIoBill);
            }
            if (!invInventoryService.batchAddQty(invs, invIoBills, 1)) {
                throw new BizException("即时库存更新失败");
            }
        }
        return otherInStock;
    }


    @Transactional(rollbackFor = Exception.class)
    public InvOtherInStock unAuditUptData(InvOtherInStock otherInStock) {
        otherInStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        otherInStock.setAuditTime(sdf.format(new Date()));
        otherInStock.setAuditorId(RequestUtils.getUserId());
        otherInStock.setAuditor(RequestUtils.getNickname());

        List<InvOtherInStockItem> otherInStockItemList = otherInStock.getItemList();
        if (otherInStockItemList != null && !otherInStockItemList.isEmpty()) {
            List<InvInventory> invs = new ArrayList<>();
            List<InvIoBill> invIoBills = new ArrayList<>();
            for (InvOtherInStockItem outStockItem : otherInStockItemList) {
                InvInventory inv = new InvInventory();
                inv.setMaterialDetailId(outStockItem.getMaterialDetailId());
                inv.setQty(outStockItem.getInQty());
                inv.setPiQty(outStockItem.getInPiQty());
                inv.setRepositoryId(outStockItem.getRepositoryId());
                inv.setPositionId(StringUtils.isNotEmpty(outStockItem.getPositionId()) ? outStockItem.getPositionId() : "0");
                inv.setPrice(outStockItem.getPrice());
                inv.setLot(outStockItem.getLot());
                invs.add(inv);

                InvIoBill invIoBill = new InvIoBill();
                invIoBill.setSrcId(outStockItem.getPid());
                invIoBill.setSrcItemId(outStockItem.getId());
                invIoBill.setSrcType("其他入库单");
                invIoBills.add(invIoBill);
            }
            if (!invInventoryService.batchSubQty(invs, invIoBills, 2)) {
                throw new BizException("即时库存更新失败");
            }
        }
        updateById(otherInStock);
        return otherInStock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<InvOtherInStock> list = this.list(
                new LambdaQueryWrapper<InvOtherInStock>()
                        .in(InvOtherInStock::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<InvOtherInStock> submitList = list.stream().filter(r -> (r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (InvOtherInStock otherInStock : submitList) {
                    this.submit(otherInStock.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(InvOtherInStock otherInStock) throws Exception {
        if (otherInStock.getBillStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(otherInStock.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(otherInStock.getWorkFlowId());
            flowOperationInfo.setFormData(otherInStock);
            flowOperationInfo.setUserId(otherInStock.getUserId());
            flowOperationInfo.setChildNodes(otherInStock.getChildNodes());
            flowOperationInfo.setCurrentNodeId(otherInStock.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(otherInStock.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                otherInStock.setWorkFlowId("");
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
            ids.add(otherInStock.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, otherInStock.getWorkFlow().getId());
            otherInStock.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            otherInStock.setNodeStatus(currentNodes.get(0).getStatus());
            otherInStock.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(otherInStock.getId()) == 1) {
                otherInStock = this.auditUptData(otherInStock);
            }
            // 驳回
            if (circulationOperationService.whetherLast(otherInStock.getId()) == 2) {
                otherInStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(otherInStock)) {
            throw new BizException("操作失败");
        }

        return Result.success(otherInStock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(InvOtherInStock otherInStock) throws Exception {
        List<String> ids = otherInStock.getIds();
        List<InvOtherInStock> mos = this.list(
                new LambdaQueryWrapper<InvOtherInStock>()
                        .in(InvOtherInStock::getId, ids)
        );
        if (mos != null && mos.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, otherInStock.getWorkFlow().getId());
            for (int i = 0; i < mos.size(); i++) {
                InvOtherInStock item = mos.get(i);
                item.setBillStatus(otherInStock.getBillStatus());
                item.setWorkFlowId(otherInStock.getWorkFlowId());
                item.setUserId(otherInStock.getUserId());
                item.setChildNodes(otherInStock.getChildNodes());
                item.setChildNodeApprovalResult(otherInStock.getChildNodeApprovalResult());
                item.setWorkFlow(otherInStock.getWorkFlow());
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
        FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
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
    public InvOtherInStock unAudit(String id) throws Exception {
        InvOtherInStock result = this.detail(id);
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
        List<InvOtherInStock> list = this.listByIds(idList);
        if (list != null && list.size() > 0) {
            List<InvOtherInStock> unAuditList = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (InvOtherInStock otherInStock : unAuditList) {
                    this.unAudit(otherInStock.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }


}
