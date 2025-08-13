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
import com.fenglei.mapper.pur.PurPurchaseInstockItemMapper;
import com.fenglei.mapper.pur.PurPurchaseInstockMapper;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.basedata.BdSupplier;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.pur.entity.PurPurchaseInstock;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import com.fenglei.model.pur.entity.PurPurchaseOrder;
import com.fenglei.model.pur.entity.PurPurchaseOrderItem;
import com.fenglei.model.pur.vo.PurPurchaseInstockItemVo;
import com.fenglei.model.pur.vo.PurPurchaseOrderItemVo;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.pur.PurPurchaseInstockItemService;
import com.fenglei.service.pur.PurPurchaseInstockService;
import com.fenglei.service.pur.PurPurchaseOrderItemService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurPurchaseInstockServiceImpl extends ServiceImpl<PurPurchaseInstockMapper, PurPurchaseInstock> implements PurPurchaseInstockService {

    @Resource
    private PurPurchaseInstockItemService purPurchaseInstockItemService;
    @Resource
    private SysFilesService sysFileService;
    @Resource
    private PurPurchaseOrderItemService purPurchaseOrderItemService;
    @Resource
    private InvInventoryService invInventoryService;

    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private PurPurchaseInstockItemMapper purPurchaseInstockItemMapper;
    @Resource
    private BdMaterialDetailMapper bdMaterialDetailMapper;

    @Override
    public IPage<PurPurchaseInstock> myPage(Page page, PurPurchaseInstock purPurchaseInstock) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(purPurchaseInstock.getItemProductId())
                || (purPurchaseInstock.getItemProductIds() != null && purPurchaseInstock.getItemProductIds().size() > 0)
                || StringUtils.isNotEmpty(purPurchaseInstock.getItemSupplierId())
                || (purPurchaseInstock.getItemSupplierIds() != null && purPurchaseInstock.getItemSupplierIds().size() > 0)
                || StringUtils.isNotEmpty(purPurchaseInstock.getItemRepositoryId())
                || (purPurchaseInstock.getItemRepositoryIds() != null && purPurchaseInstock.getItemRepositoryIds().size() > 0)) {
            List<PurPurchaseInstockItem> items = purPurchaseInstockItemMapper.getItemsByMain(purPurchaseInstock);
            if (items != null && items.size() > 0) {
                for (PurPurchaseInstockItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        purPurchaseInstock.setIds(pids);

        IPage<PurPurchaseInstock> iPage = baseMapper.getPage(page, purPurchaseInstock);
        if (iPage != null) {
            List<PurPurchaseInstock> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().map(PurPurchaseInstock::getId).collect(Collectors.toList());
                PurPurchaseInstock srhPurchaseInstock = new PurPurchaseInstock();
                srhPurchaseInstock.setIds(ids);
                List<PurPurchaseInstockItem> items = purPurchaseInstockItemMapper.getItemsByMain(srhPurchaseInstock);
                if (items != null && items.size() > 0) {
                    for (PurPurchaseInstock record : records) {
                        List<PurPurchaseInstockItem> myItems = items.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setPurchaseInstockItems(myItems);
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public List<PurPurchaseInstock> myList(PurPurchaseInstock purPurchaseInstock) {
        List<PurPurchaseInstock> list = baseMapper.getList(purPurchaseInstock);
        if (list != null && list.size() > 0) {
            for (PurPurchaseInstock record : list) {

            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseInstock add(PurPurchaseInstock purPurchaseInstock) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(purPurchaseInstock.getNo())) {
            List<PurPurchaseInstock> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseInstock>()
                            .eq(PurPurchaseInstock::getNo, purPurchaseInstock.getNo())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同款号的采购入库单，请修改");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "CGRK" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<PurPurchaseInstock> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseInstock>().likeRight(PurPurchaseInstock::getNo, no)
                            .orderByDesc(PurPurchaseInstock::getNo)
            );
            if (list != null && list.size() > 0) {
                String maxNo = list.get(0).getNo();
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            purPurchaseInstock.setNo(no);
        }

        purPurchaseInstock.setBillStatus(Constants.INT_STATUS_CREATE);
        purPurchaseInstock.setCreateTime(sdf.format(new Date()));
        purPurchaseInstock.setCreatorId(RequestUtils.getUserId());
        purPurchaseInstock.setCreator(RequestUtils.getNickname());
        if (!this.save(purPurchaseInstock)) {
            throw new BizException("保存失败");
        }

        // 明细
        List<PurPurchaseInstockItem> items = purPurchaseInstock.getPurchaseInstockItems();
        if (items != null && items.size() > 0) {
            int i = 1;
            for (PurPurchaseInstockItem item : items) {
                item.setPid(purPurchaseInstock.getId());
                item.setSeq(i);
                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(item.getMaterial().getId());
                srhMtrlDtl.setColorId(item.getColorId());
                srhMtrlDtl.setSpecificationId(item.getSpecificationId());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    item.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException(purPurchaseInstock.getSrcType() + "信息异常：编码 " + item.getMaterial().getNumber() + " 名称 " + item.getMaterial().getName());
                }
            }
            if (!purPurchaseInstockItemService.saveBatch(items)) {
                throw new BizException("保存失败，明细异常");
            }
        }

        return purPurchaseInstock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseInstock myUpdate(PurPurchaseInstock purPurchaseInstock) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(purPurchaseInstock.getNo())) {
            List<PurPurchaseInstock> list = this.list(
                    new LambdaQueryWrapper<PurPurchaseInstock>()
                            .eq(PurPurchaseInstock::getNo, purPurchaseInstock.getNo())
                            .ne(PurPurchaseInstock::getId, purPurchaseInstock.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同款号的采购入库单，请修改，异常码1");
            }
        } else {
            PurPurchaseInstock thisMo = this.getById(purPurchaseInstock.getId());
            if (thisMo != null) {
                purPurchaseInstock.setNo(thisMo.getNo());

                List<PurPurchaseInstock> list = this.list(
                        new LambdaQueryWrapper<PurPurchaseInstock>()
                                .eq(PurPurchaseInstock::getNo, purPurchaseInstock.getNo())
                                .ne(PurPurchaseInstock::getId, purPurchaseInstock.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同款号的采购入库单，请修改，异常码2");
                }
            } else {
                purPurchaseInstock.setId(null);

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "CGRK" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<PurPurchaseInstock> list = this.list(
                        new LambdaQueryWrapper<PurPurchaseInstock>().likeRight(PurPurchaseInstock::getNo, no)
                                .orderByDesc(PurPurchaseInstock::getNo)
                );
                if (list != null && list.size() > 0) {
                    String maxNo = list.get(0).getNo();
                    Integer pos = maxNo.lastIndexOf("-");
                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                    Integer maxNoInt = Integer.valueOf(maxIdxStr);
                    String noIdxStr = String.format("%04d", maxNoInt + 1);
                    no = no + noIdxStr;
                } else {
                    no = no + "0001";
                }
                purPurchaseInstock.setNo(no);
            }
        }

        purPurchaseInstock.setUpdateTime(sdf.format(new Date()));
        purPurchaseInstock.setUpdaterId(RequestUtils.getUserId());
        purPurchaseInstock.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(purPurchaseInstock)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        List<PurPurchaseInstockItem> oldItems = purPurchaseInstockItemService.list(
                new LambdaQueryWrapper<PurPurchaseInstockItem>()
                        .eq(PurPurchaseInstockItem::getPid, purPurchaseInstock.getId())
        );
        if (oldItems != null && oldItems.size() > 0) {
            List<String> oldItemIds = oldItems.stream().map(PurPurchaseInstockItem::getId).collect(Collectors.toList());
            if (oldItemIds != null && oldItemIds.size() > 0) {
                if (!purPurchaseInstockItemService.removeByIds(oldItemIds)) {
                    throw new BizException("更新失败，明细异常");
                }
            }
        }

        // 保存新数据
        // 明细
        List<PurPurchaseInstockItem> items = purPurchaseInstock.getPurchaseInstockItems();
        if (items != null && items.size() > 0) {
            for (PurPurchaseInstockItem item : items) {
                item.setPid(purPurchaseInstock.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(item.getMaterial().getId());
                srhMtrlDtl.setColorId(item.getColorId());
                srhMtrlDtl.setSpecificationId(item.getSpecificationId());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    item.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException(purPurchaseInstock.getSrcType() + "信息异常：编码 " + item.getMaterial().getNumber() + " 名称 " + item.getMaterial().getName());
                }
            }
            if (!purPurchaseInstockItemService.saveBatch(items)) {
                throw new BizException("保存失败，明细异常");
            }
        }

        return purPurchaseInstock;
    }

    Boolean chkIsUsed(List<String> ids) {
        // 库存数据


        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        PurPurchaseInstock mo = this.getById(id);
        if (mo == null) {
            return true;
        }
        if (mo.getBillStatus().equals(Constants.INT_STATUS_APPROVING) || mo.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        // 删除-颜色规格
        List<PurPurchaseInstockItem> oldPurchaseOrderItems = purPurchaseInstockItemService.list(
                new LambdaQueryWrapper<PurPurchaseInstockItem>()
                        .eq(PurPurchaseInstockItem::getPid, id)
        );
        if (oldPurchaseOrderItems != null && !oldPurchaseOrderItems.isEmpty()) {
            List<String> oldPurchaseOrderItemIds = oldPurchaseOrderItems.stream().map(PurPurchaseInstockItem::getId).collect(Collectors.toList());
            if (!oldPurchaseOrderItemIds.isEmpty()) {
                if (!purPurchaseInstockItemService.removeByIds(oldPurchaseOrderItemIds)) {
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
        List<PurPurchaseInstock> list = this.listByIds(idList);
        if (list != null && !list.isEmpty()) {
            List<String> delIds = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT)).map(PurPurchaseInstock::getId).collect(Collectors.toList());
            if (!delIds.isEmpty()) {
                if (!this.removeByIds(idList)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            // 删除-颜色规格
            List<PurPurchaseInstockItem> oldPurchaseOrderItems = purPurchaseInstockItemService.list(
                    new LambdaQueryWrapper<PurPurchaseInstockItem>()
                            .eq(PurPurchaseInstockItem::getPid, idList)
            );
            if (oldPurchaseOrderItems != null && !oldPurchaseOrderItems.isEmpty()) {
                List<String> oldPurchaseOrderItemIds = oldPurchaseOrderItems.stream().map(PurPurchaseInstockItem::getId).collect(Collectors.toList());
                if (!oldPurchaseOrderItemIds.isEmpty()) {
                    if (!purPurchaseInstockItemService.removeByIds(oldPurchaseOrderItemIds)) {
                        throw new BizException("删除失败，明细异常");
                    }
                }
            }
        }
    }

    @Override
    public PurPurchaseInstock detail(String id) {
        PurPurchaseInstock result = baseMapper.infoById(id);
        if (result != null) {
            // 明细
            List<PurPurchaseInstockItem> items = purPurchaseInstockItemMapper.listByPid(id);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseInstockItem item : items) {
                    if (item.getMaterial() != null && StringUtils.isNotEmpty(item.getMaterial().getMainPicId())) {
                        SysFiles sysFile = sysFileService.getById(item.getMaterial().getMainPicId());
                        item.getMaterial().setMainPic(sysFile);
                    }
                }
            }
            result.setPurchaseInstockItems(items);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseInstock submit(String id) throws Exception {
        PurPurchaseInstock result = this.detail(id);
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
    public PurPurchaseInstock auditUptData(PurPurchaseInstock purPurchaseInstock) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        purPurchaseInstock.setBillStatus(Constants.INT_STATUS_AUDITED);
        purPurchaseInstock.setAuditTime(sdf.format(new Date()));
        purPurchaseInstock.setAuditorId(RequestUtils.getUserId());
        purPurchaseInstock.setAuditor(RequestUtils.getNickname());

        List<PurPurchaseInstockItem> instockItems = purPurchaseInstock.getPurchaseInstockItems();
        if (instockItems != null && !instockItems.isEmpty()) {
            List<String> srcItemIds = instockItems.stream().map(PurPurchaseInstockItem::getSrcItemId).collect(Collectors.toList());
            List<PurPurchaseOrderItem> udtOrderItems = purPurchaseOrderItemService.listByIds(srcItemIds);
            if (udtOrderItems != null && !udtOrderItems.isEmpty()) {
                List<InvInventory> invs = new ArrayList<>();
                List<InvIoBill> invIoBills = new ArrayList<>();

                for (PurPurchaseInstockItem instockItem : instockItems) {
                    PurPurchaseOrderItem orderItem = udtOrderItems.stream().filter(r -> StringUtils.equals(r.getId(), instockItem.getSrcItemId())).findFirst().orElse(null);
                    if (orderItem != null) {
                        orderItem.setInstockQty(orderItem.getInstockQty().add(instockItem.getQty()));
                        if (orderItem.getInstockQty().compareTo(orderItem.getQty()) > 0) {
                            throw new BizException("审核失败，入库数量不能超过订单数量！");
                        }
                    } else {
                        throw new BizException("审核失败，源数据异常2");
                    }

                    InvInventory inv = new InvInventory();
                    inv.setMaterialDetailId(instockItem.getMaterialDetailId());
                    inv.setQty(instockItem.getQty());
                    inv.setPiQty(instockItem.getPiQty());
                    inv.setRepositoryId(instockItem.getRepositoryId());
                    inv.setPositionId(StringUtils.isNotEmpty(instockItem.getPositionId()) ? instockItem.getPositionId() : "0");
                    inv.setPrice(instockItem.getPrice());
                    inv.setLot("");
                    invs.add(inv);

                    InvIoBill invIoBill = new InvIoBill();
                    invIoBill.setSrcId(instockItem.getPid());
                    invIoBill.setSrcItemId(instockItem.getId());
                    invIoBill.setSrcType("采购入库单");
                    invIoBills.add(invIoBill);
                }
                if (!invs.isEmpty()) {
                    if (!invInventoryService.batchAddQty(invs, invIoBills, 1)) {
                        throw new BizException("即时库存更新失败");
                    }
                }

                if (!purPurchaseOrderItemService.updateBatchById(udtOrderItems)) {
                    throw new BizException("审核失败，源数据入库数量更新失败");
                }
            } else {
                throw new BizException("审核失败，源数据异常1");
            }
        }

        return purPurchaseInstock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PurPurchaseInstock> list = this.list(
                new LambdaQueryWrapper<PurPurchaseInstock>()
                        .in(PurPurchaseInstock::getId, idList)
        );
        if (list != null && !list.isEmpty()) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<PurPurchaseInstock> submitList = list.stream().filter(r -> (r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT))).collect(Collectors.toList());
            if (!submitList.isEmpty()) {
                for (PurPurchaseInstock purPurchaseInstock : submitList) {
                    this.submit(purPurchaseInstock.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(PurPurchaseInstock purPurchaseInstock) throws Exception {
        if (purPurchaseInstock.getBillStatus().equals(Constants.INT_STATUS_APPROVING) && ObjectUtils.isNotEmpty(purPurchaseInstock.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(purPurchaseInstock.getWorkFlowId());
            flowOperationInfo.setFormData(purPurchaseInstock);
            flowOperationInfo.setUserId(purPurchaseInstock.getUserId());
            flowOperationInfo.setChildNodes(purPurchaseInstock.getChildNodes());
            flowOperationInfo.setCurrentNodeId(purPurchaseInstock.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(purPurchaseInstock.getChildNodeApprovalResult());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                purPurchaseInstock.setWorkFlowId("");
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
            ids.add(purPurchaseInstock.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, purPurchaseInstock.getWorkFlow().getId());
            purPurchaseInstock.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            purPurchaseInstock.setNodeStatus(currentNodes.get(0).getStatus());
            purPurchaseInstock.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(purPurchaseInstock.getId()) == 1) {
                purPurchaseInstock = this.auditUptData(purPurchaseInstock);
            }
            // 驳回
            if (circulationOperationService.whetherLast(purPurchaseInstock.getId()) == 2) {
                purPurchaseInstock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(purPurchaseInstock)) {
            throw new BizException("操作失败");
        }

        return Result.success(purPurchaseInstock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(PurPurchaseInstock purPurchaseInstock) throws Exception {
        List<String> ids = purPurchaseInstock.getIds();
        List<PurPurchaseInstock> mos = this.list(
                new LambdaQueryWrapper<PurPurchaseInstock>()
                        .in(PurPurchaseInstock::getId, ids)
        );
        if (mos != null && !mos.isEmpty()) {
            List<ChildNode> childNodes = getCurrentNodes(ids, purPurchaseInstock.getWorkFlow().getId());
            for (PurPurchaseInstock item : mos) {
                item.setBillStatus(purPurchaseInstock.getBillStatus());
                item.setWorkFlowId(purPurchaseInstock.getWorkFlowId());
                item.setUserId(purPurchaseInstock.getUserId());
                item.setChildNodes(purPurchaseInstock.getChildNodes());
                item.setChildNodeApprovalResult(purPurchaseInstock.getChildNodeApprovalResult());
                item.setWorkFlow(purPurchaseInstock.getWorkFlow());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseInstock unAudit(String id) throws Exception {
        PurPurchaseInstock result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (!result.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        result = this.unAuditUptData(result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PurPurchaseInstock unAuditUptData(PurPurchaseInstock purPurchaseInstock) throws Exception {
        purPurchaseInstock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        purPurchaseInstock.setAuditTime(null);
        purPurchaseInstock.setAuditorId(null);
        purPurchaseInstock.setAuditor(null);
        if (!this.updateById(purPurchaseInstock)) {
            throw new BizException("反审核失败");
        }

        List<PurPurchaseInstockItem> instockItems = purPurchaseInstock.getPurchaseInstockItems();
        if (instockItems != null && !instockItems.isEmpty()) {
            List<String> srcItemIds = instockItems.stream().map(PurPurchaseInstockItem::getSrcItemId).collect(Collectors.toList());
            List<PurPurchaseOrderItem> udtOrderItems = purPurchaseOrderItemService.listByIds(srcItemIds);
            if (udtOrderItems != null && !udtOrderItems.isEmpty()) {
                List<InvInventory> invs = new ArrayList<>();
                List<InvIoBill> invIoBills = new ArrayList<>();
                for (PurPurchaseInstockItem instockItem : instockItems) {
                    PurPurchaseOrderItem orderItem = udtOrderItems.stream().filter(r -> StringUtils.equals(r.getId(), instockItem.getSrcItemId())).findFirst().orElse(null);
                    if (orderItem != null) {
                        orderItem.setInstockQty(orderItem.getInstockQty().subtract(instockItem.getQty()));
                    } else {
                        throw new BizException("反审核失败，源数据异常2");
                    }

                    InvInventory inv = new InvInventory();
                    inv.setMaterialDetailId(instockItem.getMaterialDetailId());
                    inv.setQty(instockItem.getQty());
                    inv.setPiQty(instockItem.getPiQty());
                    inv.setRepositoryId(instockItem.getRepositoryId());
                    inv.setPositionId(StringUtils.isNotEmpty(instockItem.getPositionId()) ? instockItem.getPositionId() : "0");
                    inv.setPrice(instockItem.getPrice());
                    inv.setLot("");
                    invs.add(inv);

                    InvIoBill invIoBill = new InvIoBill();
                    invIoBill.setSrcId(instockItem.getPid());
                    invIoBill.setSrcItemId(instockItem.getId());
                    invIoBill.setSrcType("采购入库单");
                    invIoBills.add(invIoBill);
                }
                if (!invs.isEmpty()) {
                    if (!invInventoryService.batchSubQty(invs, invIoBills, 2)) {
                        throw new BizException("即时库存更新失败");
                    }
                }

                if (!purPurchaseOrderItemService.updateBatchById(udtOrderItems)) {
                    throw new BizException("反审核失败，源数据入库数量更新失败");
                }
            } else {
                throw new BizException("反审核失败，源数据异常1");
            }
        }

        return purPurchaseInstock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PurPurchaseInstock> list = this.listByIds(idList);
        if (list != null && !list.isEmpty()) {
            List<PurPurchaseInstock> unAuditList = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_AUDITED)).collect(Collectors.toList());
            if (!unAuditList.isEmpty()) {
                for (PurPurchaseInstock purPurchaseInstock : unAuditList) {
                    this.unAudit(purPurchaseInstock.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public void itemExport(HttpServletResponse response, PurPurchaseInstock purPurchaseInstock) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(purPurchaseInstock.getItemProductId())
                || (purPurchaseInstock.getItemProductIds() != null && !purPurchaseInstock.getItemProductIds().isEmpty())
                || StringUtils.isNotEmpty(purPurchaseInstock.getItemSupplierId())
                || (purPurchaseInstock.getItemSupplierIds() != null && !purPurchaseInstock.getItemSupplierIds().isEmpty())
                || StringUtils.isNotEmpty(purPurchaseInstock.getItemRepositoryId())
                || (purPurchaseInstock.getItemRepositoryIds() != null && !purPurchaseInstock.getItemRepositoryIds().isEmpty())) {
            List<PurPurchaseInstockItem> items = purPurchaseInstockItemMapper.getItemsByMain(purPurchaseInstock);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseInstockItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                pids.add("-1");
            }
        }
        purPurchaseInstock.setIds(pids);

        List<PurPurchaseInstock> list = baseMapper.getList(purPurchaseInstock);
        List<PurPurchaseInstockItemVo> itemVoList = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
            List<String> ids = list.stream().map(PurPurchaseInstock::getId).collect(Collectors.toList());
            PurPurchaseInstock srhPurchaseInstock = new PurPurchaseInstock();
            srhPurchaseInstock.setIds(ids);
            List<PurPurchaseInstockItem> items = purPurchaseInstockItemMapper.getItemsByMain(srhPurchaseInstock);
            if (items != null && !items.isEmpty()) {
                for (PurPurchaseInstock instock : list) {
                    List<PurPurchaseInstockItem> myItems = items.stream().filter(r -> r.getPid().equals(instock.getId())).collect(Collectors.toList());
                    for (PurPurchaseInstockItem myItem : myItems) {
                        BdMaterial material = myItem.getMaterial() == null ? new BdMaterial() : myItem.getMaterial();
                        BdSupplier supplier = myItem.getSupplier() == null ? new BdSupplier() : myItem.getSupplier();
                        PurPurchaseInstockItemVo itemVo = new PurPurchaseInstockItemVo();
                        itemVo.setNo(instock.getNo());
                        itemVo.setBizDate(instock.getBizDate());
                        itemVo.setStocker(instock.getStocker());
                        itemVo.setSrcNo(instock.getSrcNo());
                        itemVo.setSrcType(instock.getSrcType());
                        itemVo.setRemark(instock.getRemark());
                        itemVo.setNumber(material.getNumber());
                        itemVo.setName(material.getName());
                        itemVo.setColor(myItem.getColor());
                        itemVo.setSpecification(myItem.getSpecification());
                        itemVo.setUnitName(material.getUnitName());
                        itemVo.setQty(ObjectUtils.isNotEmpty(myItem.getQty()) ? myItem.getQty().toString() : "");
                        itemVo.setPiQty(ObjectUtils.isNotEmpty(myItem.getPiQty()) ? myItem.getPiQty().toString() : "");
                        itemVo.setRepositoryName(myItem.getRepositoryName());
                        itemVo.setPositionName(myItem.getPositionName());
                        itemVo.setPrice(ObjectUtils.isNotEmpty(myItem.getPrice()) ? myItem.getPrice().toString() : "");
                        itemVo.setSupplierName(supplier.getName());
                        itemVo.setContact(supplier.getContact());
                        itemVo.setContactNumber(supplier.getContactNumber());
                        itemVo.setAddress(supplier.getAddress());
                        itemVo.setItemRemark(myItem.getRemark());
                        itemVoList.add(itemVo);
                    }
                }
            }
        }
        ExcelUtils.writeExcel(response, itemVoList, PurPurchaseInstockItemVo.class, "采购入库详情导出.xlsx");
    }
}
