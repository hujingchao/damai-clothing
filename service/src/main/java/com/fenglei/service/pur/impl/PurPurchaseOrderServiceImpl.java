package com.fenglei.service.pur.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdMaterialDetailMapper;
import com.fenglei.mapper.pur.PurPurchaseInstockMapper;
import com.fenglei.mapper.pur.PurPurchaseOrderItemMapper;
import com.fenglei.mapper.pur.PurPurchaseOrderMapper;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.basedata.BdSupplier;
import com.fenglei.model.pur.entity.PurPurchaseInstock;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.model.pur.entity.PurPurchaseOrderItem;
import com.fenglei.model.pur.vo.PurPurchaseOrderItemVo;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.pur.PurPurchaseOrderItemService;
import com.fenglei.service.pur.PurPurchaseOrderService;
import com.fenglei.service.system.SysFilesService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurPurchaseOrderServiceImpl extends ServiceImpl<PurPurchaseOrderMapper, PurPurchaseOrder> implements PurPurchaseOrderService {

    @Resource
    private PurPurchaseOrderItemService purPurchaseOrderItemService;
    @Resource
    private SysFilesService sysFileService;

    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private PurPurchaseOrderItemMapper purPurchaseOrderItemMapper;
    @Resource
    private PurPurchaseInstockMapper purPurchaseInstockMapper;
    @Resource
    private BdMaterialDetailMapper bdMaterialDetailMapper;

    @Override
    public IPage<PurPurchaseOrder> myPage(Page<PurPurchaseOrder> page, PurPurchaseOrder purPurchaseOrder) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(purPurchaseOrder.getItemProductId())
                || (purPurchaseOrder.getItemProductIds() != null && !purPurchaseOrder.getItemProductIds().isEmpty())
                || StringUtils.isNotEmpty(purPurchaseOrder.getItemSupplierId())
                || (purPurchaseOrder.getItemSupplierIds() != null && !purPurchaseOrder.getItemSupplierIds().isEmpty())) {
            List<PurPurchaseOrderItem> items = purPurchaseOrderItemMapper.getItemsByMain(purPurchaseOrder);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseOrderItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                pids.add("-1");
            }
        }
        purPurchaseOrder.setIds(pids);

        IPage<PurPurchaseOrder> iPage = baseMapper.getPage(page, purPurchaseOrder);
        if (iPage != null) {
            List<PurPurchaseOrder> records = iPage.getRecords();
            if (records != null && !records.isEmpty()) {
                List<String> ids = records.stream().map(PurPurchaseOrder::getId).collect(Collectors.toList());
                PurPurchaseOrder srhPurchaseOrder = new PurPurchaseOrder();
                srhPurchaseOrder.setIds(ids);
                List<PurPurchaseOrderItem> items = purPurchaseOrderItemMapper.getItemsByMain(srhPurchaseOrder);
                if (items != null && !items.isEmpty()) {
                    for (PurPurchaseOrder record : records) {
                        List<PurPurchaseOrderItem> myItems = items.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setPurchaseOrderItems(myItems);
                    }
                }
                for (PurPurchaseOrder record : records) {
                    List<PurPurchaseOrderItem> purchaseOrderItems = record.getPurchaseOrderItems();
                    boolean flag = true;
                    for (PurPurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
                        if(purchaseOrderItem.getInstockQty().compareTo(purchaseOrderItem.getQty())!=0){
                            flag = false;
                            break;
                        }
                    }
                    record.setIsAllInStock(flag);
                }
            }
        }

        return iPage;
    }

    @Override
    public List<PurPurchaseOrder> myList(PurPurchaseOrder purPurchaseOrder) {
        List<PurPurchaseOrder> list = baseMapper.getList(purPurchaseOrder);
        if (list != null && !list.isEmpty()) {
            for (PurPurchaseOrder record : list) {

            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseOrder add(PurPurchaseOrder purPurchaseOrder) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(purPurchaseOrder.getNo())) {
            List<PurPurchaseOrder> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseOrder>()
                            .eq(PurPurchaseOrder::getNo, purPurchaseOrder.getNo())
            );
            if (list != null && !list.isEmpty()) {
                throw new BizException("已存在相同款号的采购订单，请修改");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "CG" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<PurPurchaseOrder> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseOrder>().likeRight(PurPurchaseOrder::getNo, no)
                            .orderByDesc(PurPurchaseOrder::getNo)
            );
            if (list != null && !list.isEmpty()) {
                String maxNo = list.get(0).getNo();
                int pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                int maxNoInt = Integer.parseInt(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            purPurchaseOrder.setNo(no);
        }

        purPurchaseOrder.setBillStatus(Constants.INT_STATUS_CREATE);
        purPurchaseOrder.setCreateTime(sdf.format(new Date()));
        purPurchaseOrder.setCreatorId(RequestUtils.getUserId());
        purPurchaseOrder.setCreator(RequestUtils.getNickname());
        if (!this.save(purPurchaseOrder)) {
            throw new BizException("保存失败");
        }

        // 明细
        List<PurPurchaseOrderItem> items = purPurchaseOrder.getPurchaseOrderItems();
        if (items != null && !items.isEmpty()) {
            int i = 1;
            for (PurPurchaseOrderItem item : items) {
                item.setPid(purPurchaseOrder.getId());
                item.setSeq(i++);
                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(item.getMaterial().getId());
                srhMtrlDtl.setColorId(item.getColorId());
                srhMtrlDtl.setSpecificationId(item.getSpecificationId());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && !details.isEmpty()) {
                    item.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException(purPurchaseOrder.getBillType()  + "信息异常：编码 " + item.getMaterial().getNumber() + " 名称 " + item.getMaterial().getName());
                }
            }
            if (!purPurchaseOrderItemService.saveBatch(items)) {
                throw new BizException("保存失败，明细异常");
            }
        }

        return purPurchaseOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseOrder myUpdate(PurPurchaseOrder purPurchaseOrder) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(purPurchaseOrder.getNo())) {
            List<PurPurchaseOrder> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseOrder>()
                            .eq(PurPurchaseOrder::getNo, purPurchaseOrder.getNo())
                            .ne(PurPurchaseOrder::getId, purPurchaseOrder.getId())
            );
            if (list != null && !list.isEmpty()) {
                throw new BizException("已存在相同款号的采购订单，请修改，异常码1");
            }
        } else {
            PurPurchaseOrder thisMo = this.getById(purPurchaseOrder.getId());
            if (thisMo != null) {
                purPurchaseOrder.setNo(thisMo.getNo());

                List<PurPurchaseOrder> list = this.list(
                        new LambdaQueryWrapper<PurPurchaseOrder>()
                                .eq(PurPurchaseOrder::getNo, purPurchaseOrder.getNo())
                                .ne(PurPurchaseOrder::getId, purPurchaseOrder.getId())
                );
                if (list != null && !list.isEmpty()) {
                    throw new BizException("已存在相同款号的采购订单，请修改，异常码2");
                }
            } else {
                purPurchaseOrder.setId(null);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "CG" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<PurPurchaseOrder> list = this.list(
                        new LambdaQueryWrapper<PurPurchaseOrder>().likeRight(PurPurchaseOrder::getNo, no)
                                .orderByDesc(PurPurchaseOrder::getNo)
                );
                if (list != null && !list.isEmpty()) {
                    String maxNo = list.get(0).getNo();
                    int pos = maxNo.lastIndexOf("-");
                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                    int maxNoInt = Integer.parseInt(maxIdxStr);
                    String noIdxStr = String.format("%04d", maxNoInt + 1);
                    no = no + noIdxStr;
                } else {
                    no = no + "0001";
                }
                purPurchaseOrder.setNo(no);
            }
        }

        purPurchaseOrder.setUpdateTime(sdf.format(new Date()));
        purPurchaseOrder.setUpdaterId(RequestUtils.getUserId());
        purPurchaseOrder.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(purPurchaseOrder)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        List<PurPurchaseOrderItem> oldItems = purPurchaseOrderItemService.list(
                new LambdaQueryWrapper<PurPurchaseOrderItem>()
                        .eq(PurPurchaseOrderItem::getPid, purPurchaseOrder.getId())
        );
        if (oldItems != null && !oldItems.isEmpty()) {
            List<String> oldItemIds = oldItems.stream().map(PurPurchaseOrderItem::getId).collect(Collectors.toList());
            if (!oldItemIds.isEmpty()) {
                if (!purPurchaseOrderItemService.removeByIds(oldItemIds)) {
                    throw new BizException("更新失败，明细异常");
                }
            }
        }

        // 保存新数据
        // 明细
        List<PurPurchaseOrderItem> items = purPurchaseOrder.getPurchaseOrderItems();
        if (items != null && !items.isEmpty()) {
            for (PurPurchaseOrderItem item : items) {
                item.setPid(purPurchaseOrder.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(item.getMaterial().getId());
                srhMtrlDtl.setColorId(item.getColorId());
                srhMtrlDtl.setSpecificationId(item.getSpecificationId());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && !details.isEmpty()) {
                    item.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException(purPurchaseOrder.getBillType()  + "信息异常：编码 " + item.getMaterial().getNumber() + " 名称 " + item.getMaterial().getName());
                }
            }
            if (!purPurchaseOrderItemService.saveBatch(items)) {
                throw new BizException("保存失败，明细异常");
            }
        }

        return purPurchaseOrder;
    }

    Boolean chkIsUsed(List<String> ids) {
        // 采购入库单
        List<PurPurchaseInstock> purchaseInstocks = purPurchaseInstockMapper.selectList(
                new LambdaQueryWrapper<PurPurchaseInstock>()
                        .in(PurPurchaseInstock::getSrcId, ids)
        );
        if (purchaseInstocks != null && !purchaseInstocks.isEmpty()) {
            throw new BizException("当前采购订单已下达采购入库单，无法删除");
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        PurPurchaseOrder mo = this.getById(id);
        if (mo == null) {
            return true;
        }
        if (mo.getBillStatus().equals(Constants.INT_STATUS_APPROVING) || mo.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("采购订单已入库");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        // 删除-颜色规格
        List<PurPurchaseOrderItem> oldPurchaseOrderItems = purPurchaseOrderItemService.list(
                new LambdaQueryWrapper<PurPurchaseOrderItem>()
                        .eq(PurPurchaseOrderItem::getPid, id)
        );
        if (oldPurchaseOrderItems != null && !oldPurchaseOrderItems.isEmpty()) {
            List<String> oldPurchaseOrderItemIds = oldPurchaseOrderItems.stream().map(PurPurchaseOrderItem::getId).collect(Collectors.toList());
            if (!oldPurchaseOrderItemIds.isEmpty()) {
                if (!purPurchaseOrderItemService.removeByIds(oldPurchaseOrderItemIds)) {
                    throw new BizException("删除失败，明细异常");
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<PurPurchaseOrder> list = this.listByIds(idList);
        if (list != null && !list.isEmpty()) {
            List<String> delIds = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT)).map(PurPurchaseOrder::getId).collect(Collectors.toList());
            if (!delIds.isEmpty()) {
                if (this.chkIsUsed(delIds)) {
                    throw new BizException("采购订单已入库");
                }

                if (!this.removeByIds(idList)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            // 删除-颜色规格
            List<PurPurchaseOrderItem> oldPurchaseOrderItems = purPurchaseOrderItemService.list(
                    new LambdaQueryWrapper<PurPurchaseOrderItem>()
                            .eq(PurPurchaseOrderItem::getPid, idList)
            );
            if (oldPurchaseOrderItems != null && !oldPurchaseOrderItems.isEmpty()) {
                List<String> oldPurchaseOrderItemIds = oldPurchaseOrderItems.stream().map(PurPurchaseOrderItem::getId).collect(Collectors.toList());
                if (!oldPurchaseOrderItemIds.isEmpty()) {
                    if (!purPurchaseOrderItemService.removeByIds(oldPurchaseOrderItemIds)) {
                        throw new BizException("删除失败，明细异常");
                    }
                }
            }
        }
    }

    @Override
    public PurPurchaseOrder detail(String id) {
        PurPurchaseOrder result = baseMapper.infoById(id);
        if (result != null) {
            // 明细
            List<PurPurchaseOrderItem> items = purPurchaseOrderItemMapper.listByPid(id);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseOrderItem item : items) {
                    if (item.getMaterial() != null && StringUtils.isNotEmpty(item.getMaterial().getMainPicId())) {
                        SysFiles sysFile = sysFileService.getById(item.getMaterial().getMainPicId());
                        item.getMaterial().setMainPic(sysFile);
                    }
                }
            }
            result.setPurchaseOrderItems(items);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseOrder submit(String id) throws Exception {
        PurPurchaseOrder result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (!result.getBillStatus().equals(Constants.INT_STATUS_CREATE) && !result.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT)) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }

        result = this.auditUptData(result);
        if (!this.updateById(result)) {
            throw new BizException("操作失败");
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseOrder auditUptData(PurPurchaseOrder purPurchaseOrder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        purPurchaseOrder.setBillStatus(Constants.INT_STATUS_AUDITED);
        purPurchaseOrder.setAuditTime(sdf.format(new Date()));
        purPurchaseOrder.setAuditorId(RequestUtils.getUserId());
        purPurchaseOrder.setAuditor(RequestUtils.getNickname());

        return purPurchaseOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PurPurchaseOrder> list = this.list(
                new LambdaQueryWrapper<PurPurchaseOrder>()
                        .in(PurPurchaseOrder::getId, idList)
        );
        if (list != null && !list.isEmpty()) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<PurPurchaseOrder> submitList = list.stream().filter(r -> (r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT))).collect(Collectors.toList());
            if (!submitList.isEmpty()) {
                for (PurPurchaseOrder purPurchaseOrder : submitList) {
                    this.submit(purPurchaseOrder.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(PurPurchaseOrder purPurchaseOrder) throws Exception {
        if (purPurchaseOrder.getBillStatus().equals(Constants.INT_STATUS_APPROVING) && ObjectUtils.isNotEmpty(purPurchaseOrder.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(purPurchaseOrder.getWorkFlowId());
            flowOperationInfo.setFormData(purPurchaseOrder);
            flowOperationInfo.setUserId(purPurchaseOrder.getUserId());
            flowOperationInfo.setChildNodes(purPurchaseOrder.getChildNodes());
            flowOperationInfo.setCurrentNodeId(purPurchaseOrder.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(purPurchaseOrder.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                purPurchaseOrder.setWorkFlowId("");
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
            ids.add(purPurchaseOrder.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, purPurchaseOrder.getWorkFlow().getId());
            purPurchaseOrder.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            purPurchaseOrder.setNodeStatus(currentNodes.get(0).getStatus());
            purPurchaseOrder.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(purPurchaseOrder.getId()) == 1) {
                purPurchaseOrder = this.auditUptData(purPurchaseOrder);
            }
            // 驳回
            if (circulationOperationService.whetherLast(purPurchaseOrder.getId()) == 2) {
                purPurchaseOrder.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(purPurchaseOrder)) {
            throw new BizException("操作失败");
        }

        return Result.success(purPurchaseOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(PurPurchaseOrder purPurchaseOrder) throws Exception {
        List<String> ids = purPurchaseOrder.getIds();
        List<PurPurchaseOrder> mos = this.list(
                new LambdaQueryWrapper<PurPurchaseOrder>()
                        .in(PurPurchaseOrder::getId, ids)
        );
        if (mos != null && !mos.isEmpty()) {
            List<ChildNode> childNodes = getCurrentNodes(ids, purPurchaseOrder.getWorkFlow().getId());
            for (PurPurchaseOrder item : mos) {
                item.setBillStatus(purPurchaseOrder.getBillStatus());
                item.setWorkFlowId(purPurchaseOrder.getWorkFlowId());
                item.setUserId(purPurchaseOrder.getUserId());
                item.setChildNodes(purPurchaseOrder.getChildNodes());
                item.setChildNodeApprovalResult(purPurchaseOrder.getChildNodeApprovalResult());
                item.setWorkFlow(purPurchaseOrder.getWorkFlow());
                for (ChildNode childNode : childNodes) {
                    if (childNode.getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNode.getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNode.getStatus());
                        item.setCurrentNodeId(childNode.getId());
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

    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId){
        FlowOperationInfo flowOperationInfo =new FlowOperationInfo();
        FlowUser flowUser = new FlowUser();
        flowUser.setEmployeeName(RequestUtils.getNickname());
        flowUser.setId(RequestUtils.getUserId());
        flowOperationInfo.setFlowUser(flowUser);
        flowOperationInfo.setFormIds(ids);
        flowOperationInfo.setWorkFlowId(workFlowId);

        return circulationOperationService.getCurrentNodes(flowOperationInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PurPurchaseOrder unAudit(String id) throws Exception {
        PurPurchaseOrder result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (!result.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        List<String> ids = new ArrayList<>();
        ids.add(id);
        if (this.chkIsUsed(ids)) {
            throw new BizException("采购订单已入库");
        }

        result = this.unAuditUptData(result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseOrder unAuditUptData(PurPurchaseOrder purPurchaseOrder) throws Exception {
        purPurchaseOrder.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        purPurchaseOrder.setAuditTime(null);
        purPurchaseOrder.setAuditorId(null);
        purPurchaseOrder.setAuditor(null);
        if (!this.updateById(purPurchaseOrder)) {
            throw new BizException("反审核失败");
        }

        return purPurchaseOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PurPurchaseOrder> list = this.listByIds(idList);
        if (list != null && !list.isEmpty()) {
            List<PurPurchaseOrder> unAuditList = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_AUDITED)).collect(Collectors.toList());
            if (!unAuditList.isEmpty()) {
                List<String> unAuditIds = unAuditList.stream().map(PurPurchaseOrder::getId).collect(Collectors.toList());
                if (this.chkIsUsed(unAuditIds)) {
                    throw new BizException("采购订单已入库");
                }

                for (PurPurchaseOrder purPurchaseOrder : unAuditList) {
                    this.unAudit(purPurchaseOrder.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public void itemExport(HttpServletResponse response, PurPurchaseOrder purPurchaseOrder) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(purPurchaseOrder.getItemProductId())
                || (purPurchaseOrder.getItemProductIds() != null && !purPurchaseOrder.getItemProductIds().isEmpty())
                || StringUtils.isNotEmpty(purPurchaseOrder.getItemSupplierId())
                || (purPurchaseOrder.getItemSupplierIds() != null && !purPurchaseOrder.getItemSupplierIds().isEmpty())) {
            List<PurPurchaseOrderItem> items = purPurchaseOrderItemMapper.getItemsByMain(purPurchaseOrder);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseOrderItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                pids.add("-1");
            }
        }
        purPurchaseOrder.setIds(pids);

        List<PurPurchaseOrder> list = baseMapper.getList(purPurchaseOrder);
        List<PurPurchaseOrderItemVo> itemVoList = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
            List<String> ids = list.stream().map(PurPurchaseOrder::getId).collect(Collectors.toList());
            PurPurchaseOrder srhPurchaseOrder = new PurPurchaseOrder();
            srhPurchaseOrder.setIds(ids);
            List<PurPurchaseOrderItem> items = purPurchaseOrderItemMapper.getItemsByMain(srhPurchaseOrder);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseOrder order : list) {
                    List<PurPurchaseOrderItem> myItems = items.stream().filter(r -> r.getPid().equals(order.getId())).collect(Collectors.toList());
                    for (PurPurchaseOrderItem myItem : myItems) {
                        BdMaterial material = myItem.getMaterial();
                        BdSupplier supplier = myItem.getSupplier();
                        PurPurchaseOrderItemVo itemVo = new PurPurchaseOrderItemVo();
                        itemVo.setNo(order.getNo());
                        itemVo.setBizDate(order.getBizDate());
                        itemVo.setPurchaser(order.getPurchaser());
                        itemVo.setFollower(order.getFollower());
                        itemVo.setBillType(order.getBillType());
                        itemVo.setRemark(order.getRemark());
                        itemVo.setNumber(material.getNumber());
                        itemVo.setName(material.getName());
                        itemVo.setColor(myItem.getColor());
                        itemVo.setSpecification(myItem.getSpecification());
                        itemVo.setUnitName(material.getUnitName());
                        itemVo.setQty( ObjectUtils.isNotEmpty(myItem.getQty())?myItem.getQty().toString():"" );
                        if (StringUtils.isNotNull(supplier)) {
                            itemVo.setSupplierName(supplier.getName());
                            itemVo.setContact(supplier.getContact());
                            itemVo.setContactNumber(supplier.getContactNumber());
                            itemVo.setAddress(supplier.getAddress());
                        }
                        itemVo.setItemRemark(myItem.getRemark());
                        itemVoList.add(itemVo);
                    }
                }
            }
        }
        ExcelUtils.writeExcel(response, itemVoList, PurPurchaseOrderItemVo.class, "采购订单详情导出.xlsx");
    }
}
