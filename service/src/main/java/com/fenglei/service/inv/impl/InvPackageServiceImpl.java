package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.common.util.im.ExcelUtil;
import com.fenglei.mapper.inv.InvPackageMapper;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvPackage;
import com.fenglei.model.inv.entity.InvPackageItem;
import com.fenglei.model.inv.entity.InvPackageNo;
import com.fenglei.model.inv.vo.InvPackageItemVo;
import com.fenglei.model.pur.vo.PurPurchaseOrderItemVo;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.inv.IInvPackageNoService;
import com.fenglei.service.inv.IInvPackageItemService;
import com.fenglei.service.inv.IInvPackageService;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 入库后打包 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Service
public class InvPackageServiceImpl extends ServiceImpl<InvPackageMapper, InvPackage> implements IInvPackageService {
    @Autowired
    IInvPackageItemService packageItemService;
    @Autowired
    IInvPackageNoService packageNoService;
    @Autowired
    InvInventoryService inventoryService;
    @Autowired
    CirculationOperationService circulationOperationService;
    @Autowired
    SystemInformationAcquisitionService systemInformationAcquisitionService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvPackage add(InvPackage invPackage) {
        String no = invPackage.getNo();
        if (StringUtils.isEmpty(no)) {
            no = this.getNo();
            invPackage.setNo(no);
        }
        invPackage.setPackerId(RequestUtils.getUserId());
        invPackage.setPacker(RequestUtils.getNickname());
        this.save(invPackage);
        List<InvPackageItem> packageItems = invPackage.getPackageItems();
        for (InvPackageItem packageItem : packageItems) {
            packageItem.setPid(invPackage.getId());
        }
        packageItemService.batchAdd(packageItems);

        List<InvPackageNo> packageNos = invPackage.getPackageNos();
        for (InvPackageNo packageNo : packageNos) {
            packageNo.setPid(invPackage.getId());
        }
        packageNoService.batchAdd(packageNos);
        return invPackage;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        packageItemService.removeByPid(id);
        packageNoService.removeByPid(id);
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        for (String id : ids) {
            this.myRemoveById(id);
        }
        return true;
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(InvPackage invPackage) {
        packageItemService.removeByPid(invPackage.getId());
        packageNoService.removeByPid(invPackage.getId());
        List<InvPackageItem> packageItems = invPackage.getPackageItems();
        for (InvPackageItem packageItem : packageItems) {
            packageItem.setPid(invPackage.getId());
            packageItem.setId(null);
        }
        packageItemService.batchAdd(packageItems);

        List<InvPackageNo> packageNos = invPackage.getPackageNos();
        for (InvPackageNo packageNo : packageNos) {
            packageNo.setPid(invPackage.getId());
            packageNo.setId(null);
        }
        packageNoService.batchAdd(packageNos);

        this.updateById(invPackage);
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<InvPackage> myPage(Page page, InvPackage invPackage) {
        return this.page(page, Wrappers.lambdaQuery(InvPackage.class)
                .like(StringUtils.isNotEmpty(invPackage.getNo()), InvPackage::getNo, invPackage.getNo())
        );
    }

    @Override
    public IPage<InvPackageItemVo> itemPage(Page page, InvPackage invPackage) {
        IPage<InvPackageItemVo>  result=   baseMapper.itemPage(page,invPackage);
        return result;
    }

    /**
     * 详情
     */
    @Override
    public InvPackage detail(String id) {
        InvPackage byId = this.getById(id);
        List<InvPackageItem> packageItems = packageItemService.listByPid(id);
        byId.setPackageItems(packageItems);
        List<InvPackageNo> packageNos = packageNoService.listByPid(id);
        byId.setPackageNos(packageNos);
        return byId;
    }

    @Override
    public String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "P" + sdf.format(new Date());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvPackage submit(String id) {
        InvPackage result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (!Objects.equals(result.getBillStatus(), Constants.INT_STATUS_CREATE) && !Objects.equals(result.getBillStatus(), Constants.INT_STATUS_RESUBMIT)) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }

        result = this.auditUptData(result);
        if (!this.updateById(result)) {
            throw new BizException("操作失败");
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<InvPackage> list = this.listByIds(idList);
        if (list.isEmpty()) {
            throw new BizException("未选择数据");
        }
        // 过滤 创建/重新审核 且 启用 的数据
        List<InvPackage> submitList = list.stream().filter(r -> (r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT))).collect(Collectors.toList());
        if (!submitList.isEmpty()) {
            for (InvPackage invPackage : submitList) {
                this.submit(invPackage.getId());
            }
        }
        return "提交成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<InvPackage> doAction(InvPackage invPackage) throws Exception {
        if (invPackage.getBillStatus().equals(Constants.INT_STATUS_APPROVING) && ObjectUtils.isNotEmpty(invPackage.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(invPackage.getWorkFlowId());
            flowOperationInfo.setFormData(invPackage);
            flowOperationInfo.setUserId(invPackage.getUserId());
            flowOperationInfo.setChildNodes(invPackage.getChildNodes());
            flowOperationInfo.setCurrentNodeId(invPackage.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(invPackage.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                invPackage.setWorkFlowId("");
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
            ids.add(invPackage.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, invPackage.getWorkFlow().getId());
            invPackage.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            invPackage.setNodeStatus(currentNodes.get(0).getStatus());
            invPackage.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(invPackage.getId()) == 1) {
                invPackage = this.auditUptData(invPackage);
            }
            // 驳回
            if (circulationOperationService.whetherLast(invPackage.getId()) == 2) {
                invPackage.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(invPackage)) {
            throw new BizException("操作失败");
        }

        return Result.success(invPackage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> batchDoAction(InvPackage invPackage) throws Exception {
        List<String> ids = invPackage.getIds();
        List<InvPackage> packages = this.list(new LambdaQueryWrapper<InvPackage>()
                .in(InvPackage::getId, ids)
        );
        if (!packages.isEmpty()) {
            List<ChildNode> childNodes = getCurrentNodes(ids, invPackage.getWorkFlow().getId());
            for (InvPackage item : packages) {
                item.setBillStatus(item.getBillStatus());
                item.setWorkFlowId(item.getWorkFlowId());
                item.setUserId(item.getUserId());
                item.setChildNodes(item.getChildNodes());
                item.setChildNodeApprovalResult(item.getChildNodeApprovalResult());
                item.setWorkFlow(item.getWorkFlow());
                for (ChildNode childNode : childNodes) {
                    if (childNode.getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNode.getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNode.getStatus());
                        item.setCurrentNodeId(childNode.getId());
                        break;
                    }
                }
                Result<InvPackage> result = doAction(item);
                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
                    throw new BizException("操作失败");
                }
            }
        }

        return Result.success("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InvPackage unAudit(String id) {
        InvPackage result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (!result.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        result = this.unAuditUptData(result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<InvPackage> list = this.listByIds(idList);
        if (!list.isEmpty()) {
            List<InvPackage> unAuditList = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_AUDITED)).collect(Collectors.toList());
            if (!unAuditList.isEmpty()) {
                for (InvPackage invPackage : unAuditList) {
                    this.unAudit(invPackage.getId());
                }
            }
            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public List<InvPackageNo> listPackageNoById(String id) {
        return packageNoService.listByPid(id);
    }

    @Override
    public void itemExport(HttpServletResponse response, InvPackage invPackage) {
        List<InvPackageItemVo>  result=   baseMapper.itemPage(invPackage);

        ExcelUtils.writeExcel(response, result, InvPackageItemVo.class, "包装单导出.xlsx");
    }

    @Transactional(rollbackFor = Exception.class)
    public InvPackage auditUptData(InvPackage invPackage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        invPackage.setBillStatus(Constants.INT_STATUS_AUDITED);
        invPackage.setAuditTime(sdf.format(new Date()));
        invPackage.setAuditorId(RequestUtils.getUserId());
        invPackage.setAuditor(RequestUtils.getNickname());

        List<InvPackageItem> packageItems = packageItemService.listByPid(invPackage.getId());
        List<InvPackageNo> packageNos = packageNoService.listByPid(invPackage.getId());

        List<InvInventory> addInvs = new ArrayList<>();
        List<InvInventory> subInvs = new ArrayList<>();
        for (InvPackageItem packageItem : packageItems) {
            for (InvPackageNo packageNo : packageNos) {

                InvInventory sub = subInvs.stream().filter(t -> t.getMaterialDetailId().equals(packageItem.getMaterialDetailId())
                        && t.getRepositoryId().equals(packageItem.getRepositoryId())
                        && t.getPositionId().equals(packageItem.getPositionId())
                ).findFirst().orElse(null);
                if (sub == null) {
                    sub = new InvInventory();
                    sub.setMaterialDetailId(packageItem.getMaterialDetailId());
                    sub.setQty(packageItem.getPerQty());
                    sub.setPiQty(BigDecimal.ZERO);
                    sub.setRepositoryId(packageItem.getRepositoryId());
                    sub.setPositionId(packageItem.getPositionId());
                    sub.setPrice(packageItem.getPrice());
                    sub.setLot("");
                    subInvs.add(sub);
                } else {
                    sub.setQty(sub.getQty().add(packageItem.getPerQty()));
                }


                InvInventory add = new InvInventory();
                add.setMaterialDetailId(packageItem.getMaterialDetailId());
                add.setQty(packageItem.getPerQty());
                add.setPiQty(BigDecimal.ZERO);
                add.setRepositoryId(packageItem.getRepositoryId());
                add.setPositionId(packageItem.getPositionId());
                add.setPrice(packageItem.getPrice());
                add.setLot(packageNo.getPackageNo());
                addInvs.add(add);
            }
        }

        if (!inventoryService.batchSubQty(subInvs, null, 3)) {
            throw new BizException("即时库存更新失败");
        }

        if (!inventoryService.batchAddQty(addInvs, null, 3)) {
            throw new BizException("即时库存更新失败");
        }


        return invPackage;
    }

    @Transactional(rollbackFor = Exception.class)
    public InvPackage unAuditUptData(InvPackage invPackage) {
        invPackage.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        invPackage.setAuditTime(null);
        invPackage.setAuditorId(null);
        invPackage.setAuditor(null);
        if (!this.updateById(invPackage)) {
            throw new BizException("反审核失败");
        }

        List<InvPackageItem> packageItems = packageItemService.listByPid(invPackage.getId());
        List<InvPackageNo> packageNos = packageNoService.listByPid(invPackage.getId());

        List<InvInventory> addInvs = new ArrayList<>();
        List<InvInventory> subInvs = new ArrayList<>();
        for (InvPackageItem packageItem : packageItems) {
            for (InvPackageNo packageNo : packageNos) {

                InvInventory sub = new InvInventory();
                sub.setMaterialDetailId(packageItem.getMaterialDetailId());
                sub.setQty(packageItem.getPerQty());
                sub.setPiQty(BigDecimal.ZERO);
                sub.setRepositoryId(packageItem.getRepositoryId());
                sub.setPositionId(packageItem.getPositionId());
                sub.setPrice(packageItem.getPrice());
                sub.setLot(packageNo.getPackageNo());
                subInvs.add(sub);

                InvInventory add = addInvs.stream().filter(t -> t.getMaterialDetailId().equals(packageItem.getMaterialDetailId())
                        && t.getRepositoryId().equals(packageItem.getRepositoryId())
                        && t.getPositionId().equals(packageItem.getPositionId())
                ).findFirst().orElse(null);
                if (add == null) {
                    add = new InvInventory();
                    add.setMaterialDetailId(packageItem.getMaterialDetailId());
                    add.setQty(packageItem.getPerQty());
                    add.setPiQty(BigDecimal.ZERO);
                    add.setRepositoryId(packageItem.getRepositoryId());
                    add.setPositionId(packageItem.getPositionId());
                    add.setPrice(packageItem.getPrice());
                    add.setLot("");
                    addInvs.add(add);
                } else {
                    add.setQty(add.getQty().add(packageItem.getPerQty()));
                }
            }
        }

        if (!inventoryService.batchSubQty(subInvs, null, 3)) {
            throw new BizException("即时库存更新失败");
        }

        if (!inventoryService.batchAddQty(addInvs, null, 3)) {
            throw new BizException("即时库存更新失败");
        }
        return invPackage;
    }

    /**
     * 审批流获取当前节点
     */
    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId) {
        FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
        FlowUser flowUser = new FlowUser();
        flowUser.setEmployeeName(RequestUtils.getNickname());
        flowUser.setId(RequestUtils.getUserId());
        flowOperationInfo.setFlowUser(flowUser);
        flowOperationInfo.setFormIds(ids);
        flowOperationInfo.setWorkFlowId(workFlowId);

        return circulationOperationService.getCurrentNodes(flowOperationInfo);
    }
}
