package com.fenglei.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.ExcelUtils;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdMaterialDetailMapper;
import com.fenglei.model.basedata.BdPosition;
import com.fenglei.model.basedata.BdRepository;
import com.fenglei.model.basedata.dto.MaterialDTO;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.oms.entity.OmsSaleOutStock;
import com.fenglei.mapper.oms.OmsSaleOutStockMapper;
import com.fenglei.model.oms.entity.OmsSaleOutStockItem;
import com.fenglei.model.oms.entity.OmsSaleOutStockItemDetail;
import com.fenglei.model.oms.entity.dto.OmsSaleOutStockDTO;
import com.fenglei.model.prd.vo.CurMonthReportVo;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdPositionService;
import com.fenglei.service.basedata.BdRepositoryService;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.oms.IOmsSaleOutStockItemDetailService;
import com.fenglei.service.oms.IOmsSaleOutStockItemService;
import com.fenglei.service.oms.IOmsSaleOutStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.service.system.ISysUserService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 销售出库单 服务实现类
 * </p>
 *
 * @author zjn
 * @since 2024-04-15
 */
@Service
public class OmsSaleOutStockServiceImpl extends ServiceImpl<OmsSaleOutStockMapper, OmsSaleOutStock> implements IOmsSaleOutStockService {
    @Resource
    private IOmsSaleOutStockItemService omsSaleOutStockItemService;
    @Resource
    private IOmsSaleOutStockItemDetailService omsSaleOutStockItemDetailService;
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
    public OmsSaleOutStock add(OmsSaleOutStock omsSaleOutStock) {
        omsSaleOutStock.setCreatorId(RequestUtils.getUserId());
        save(omsSaleOutStock);
        List<OmsSaleOutStockItem> saleOutStockItems = omsSaleOutStock.getSaleOutStockItems();
        List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = new ArrayList<>();
        for (OmsSaleOutStockItem saleOutStockItem : saleOutStockItems) {
            saleOutStockItem.setPid(omsSaleOutStock.getId());
        }
        omsSaleOutStockItemService.saveBatch(saleOutStockItems);
        for (OmsSaleOutStockItem saleOutStockItem : saleOutStockItems) {
            for (OmsSaleOutStockItemDetail saleOutStockItemDetail : saleOutStockItem.getSaleOutStockItemDetails()) {
                saleOutStockItemDetail.setPid(saleOutStockItem.getId());
                saleOutStockItemDetail.setGpId(omsSaleOutStock.getId());
                omsSaleOutStockItemDetails.add(saleOutStockItemDetail);
            }
        }
        omsSaleOutStockItemDetailService.saveBatch(omsSaleOutStockItemDetails);
        return omsSaleOutStock;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        omsSaleOutStockItemDetailService.remove(new LambdaQueryWrapper<OmsSaleOutStockItemDetail>().eq(OmsSaleOutStockItemDetail::getGpId, id));
        omsSaleOutStockItemService.remove(new LambdaQueryWrapper<OmsSaleOutStockItem>().eq(OmsSaleOutStockItem::getPid, id));
        return removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        omsSaleOutStockItemDetailService.remove(new LambdaQueryWrapper<OmsSaleOutStockItemDetail>().in(OmsSaleOutStockItemDetail::getGpId, ids));
        omsSaleOutStockItemService.remove(new LambdaQueryWrapper<OmsSaleOutStockItem>().in(OmsSaleOutStockItem::getPid, ids));
        return removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(OmsSaleOutStock omsSaleOutStock) {
        omsSaleOutStock.setUpdaterId(RequestUtils.getUserId());
        if (!this.updateById(omsSaleOutStock)) {
            throw new BizException("更新失败");
        }
        String pid = omsSaleOutStock.getId();
        omsSaleOutStockItemDetailService.remove(new LambdaQueryWrapper<OmsSaleOutStockItemDetail>().eq(OmsSaleOutStockItemDetail::getGpId, pid));
        omsSaleOutStockItemService.remove(new LambdaQueryWrapper<OmsSaleOutStockItem>().eq(OmsSaleOutStockItem::getPid, pid));
        omsSaleOutStock.getSaleOutStockItems().forEach(t -> t.setPid(pid));
        if (!omsSaleOutStockItemService.saveBatch(omsSaleOutStock.getSaleOutStockItems())) {
            throw new BizException("更新失败");
        }
        List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = new ArrayList<>();
        List<OmsSaleOutStockItem> saleOutStockItems = omsSaleOutStock.getSaleOutStockItems();
        for (OmsSaleOutStockItem saleOutStockItem : saleOutStockItems) {
            for (OmsSaleOutStockItemDetail saleOutStockItemDetail : saleOutStockItem.getSaleOutStockItemDetails()) {
                saleOutStockItemDetail.setPid(saleOutStockItem.getId());
                saleOutStockItemDetail.setGpId(omsSaleOutStock.getId());
                omsSaleOutStockItemDetails.add(saleOutStockItemDetail);
            }
        }
        return omsSaleOutStockItemDetailService.saveBatch(omsSaleOutStockItemDetails);
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<OmsSaleOutStock> myPage(Page page, OmsSaleOutStock omsSaleOutStock) {
        List<String> stockerIdList = new ArrayList<>();
        List<String> billStatusList = new ArrayList<>();
        if(StringUtils.isNotEmpty(omsSaleOutStock.getStockerIds())){
            stockerIdList = Arrays.asList(omsSaleOutStock.getStockerIds().split(","));
        }
        if(StringUtils.isNotEmpty(omsSaleOutStock.getInbillStatus())){
            billStatusList = Arrays.asList(omsSaleOutStock.getInbillStatus().split(","));
        }
        IPage<OmsSaleOutStock> pageRecords = page(page, new LambdaQueryWrapper<OmsSaleOutStock>()
                .like(StringUtils.isNotEmpty(omsSaleOutStock.getNo()),OmsSaleOutStock::getNo,omsSaleOutStock.getNo())
                .eq(StringUtils.isNotEmpty(omsSaleOutStock.getBillStatus()),OmsSaleOutStock::getBillStatus,omsSaleOutStock.getBillStatus())
                .eq(StringUtils.isNotEmpty(omsSaleOutStock.getStockerId()),OmsSaleOutStock::getStockerId,omsSaleOutStock.getStockerId())
                .eq(StringUtils.isNotEmpty(omsSaleOutStock.getOutStockDate()),OmsSaleOutStock::getOutStockDate,omsSaleOutStock.getOutStockDate())
                .apply(StringUtils.isNotEmpty(omsSaleOutStock.getBizDateBegin()), "date_format(out_stock_date,'%Y-%m-%d %H:%i:%S') >= {0}", omsSaleOutStock.getBizDateBegin())
                .apply(StringUtils.isNotEmpty(omsSaleOutStock.getBizDateEnd()), "date_format(out_stock_date,'%Y-%m-%d %H:%i:%S') <= {0}",omsSaleOutStock.getBizDateEnd())
                .in(stockerIdList.size()>0,OmsSaleOutStock::getStockerId,stockerIdList)
                .in(billStatusList.size()>0,OmsSaleOutStock::getBillStatus,billStatusList)
        );
        List<OmsSaleOutStock> records = pageRecords.getRecords();
        if (records.size() > 0) {
            List<SysUser> sysUsers = sysUserService.list();
            List<BdRepository> bdRepositories = repositoryService.list();
            List<BdPosition> bdPositions = positionService.list();
            List<String> outStockIds = records.stream().map(OmsSaleOutStock::getId).collect(Collectors.toList());
            List<OmsSaleOutStockItem> omsSaleOutStockItems = omsSaleOutStockItemService.list(new LambdaQueryWrapper<OmsSaleOutStockItem>()
                    .in(OmsSaleOutStockItem::getPid, outStockIds)
            );
            omsSaleOutStockItems.stream().map(p -> bdRepositories.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getRepositoryId()) && s.getId().equals(p.getRepositoryId()))
                    .findFirst().map(s -> {
                        p.setRepositoryName(s.getName());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());

            omsSaleOutStockItems.stream().map(p -> bdPositions.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getPositionId()) && s.getId().equals(p.getPositionId()))
                    .findFirst().map(s -> {
                        p.setPositionName(s.getName());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());
            List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = omsSaleOutStockItemDetailService.list(new LambdaQueryWrapper<OmsSaleOutStockItemDetail>()
                    .in(OmsSaleOutStockItemDetail::getGpId, outStockIds)
            );
            List<String> materialDetailIds = omsSaleOutStockItemDetails.stream().map(OmsSaleOutStockItemDetail::getMaterialDetailId).collect(Collectors.toList());
            List<MaterialDTO> materialDTOList = materialDetailMapper.getMaterialDTOByDetailIds(materialDetailIds);
            omsSaleOutStockItemDetails.stream().map(p -> bdRepositories.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getRepositoryId()) && s.getId().equals(p.getRepositoryId()))
                    .findFirst().map(s -> {
                        p.setRepositoryName(s.getName());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());

            omsSaleOutStockItemDetails.stream().map(p -> bdPositions.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getPositionId()) && s.getId().equals(p.getPositionId()))
                    .findFirst().map(s -> {
                        p.setPositionName(s.getName());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());
            omsSaleOutStockItemDetails.stream().map(p -> materialDTOList.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getMaterialDetailId()) && s.getMaterialDetailId().equals(p.getMaterialDetailId()))
                    .findFirst().map(s -> {
                        p.setMaterialNumber(s.getMaterialNumber());
                        p.setMaterialName(s.getMaterialName());
                        p.setMaterialGroup(s.getMaterialGroup());
                        p.setUnitName(s.getUnitName());
                        p.setMainPicUrl(s.getMainPicUrl());
                        p.setColor(s.getColor());
                        p.setSpecification(s.getSpecification());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());
            for (OmsSaleOutStockItem omsSaleOutStockItem : omsSaleOutStockItems) {
                List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetailList = new ArrayList<>();
                for (OmsSaleOutStockItemDetail omsSaleOutStockItemDetail : omsSaleOutStockItemDetails) {
                    if (omsSaleOutStockItemDetail.getPid().equals(omsSaleOutStockItem.getId())) {
                        omsSaleOutStockItemDetailList.add(omsSaleOutStockItemDetail);
                    }
                }
                omsSaleOutStockItem.setSaleOutStockItemDetails(omsSaleOutStockItemDetailList);
            }
            records.stream().map(p -> sysUsers.stream()
                    .filter(s -> StringUtils.isNotEmpty(p.getStockerId()) && s.getId().equals(p.getStockerId()))
                    .findFirst().map(s -> {
                        p.setStockerName(s.getNickname());
                        return p;
                    }).orElse(null)).collect(Collectors.toList());

            records.stream().map(p -> {
                p.setSaleOutStockItems(omsSaleOutStockItems.stream()
                        .filter(s -> p.getId().equals(s.getPid()))
                        .collect(Collectors.toList()));
                return p;
            }).collect(Collectors.toList());
        }
        return pageRecords;
    }

    /**
     * 详情
     */
    @Override
    public OmsSaleOutStock detail(String id) {
        OmsSaleOutStock omsSaleOutStock = getById(id);
        List<SysUser> sysUsers = sysUserService.list();
        SysUser sysUser = sysUsers.stream().filter(s -> StringUtils.isNotEmpty(omsSaleOutStock.getStockerId()) && s.getId().equals(omsSaleOutStock.getStockerId())).findFirst().orElse(null);
        omsSaleOutStock.setStockerName(sysUser.getNickname());
        SysUser createUser = sysUsers.stream().filter(s -> StringUtils.isNotEmpty(omsSaleOutStock.getCreatorId()) && s.getId().equals(omsSaleOutStock.getCreatorId())).findFirst().orElse(null);
        omsSaleOutStock.setCreator(createUser.getNickname());
        List<OmsSaleOutStockItem> omsSaleOutStockItems = omsSaleOutStockItemService.list(new LambdaQueryWrapper<OmsSaleOutStockItem>()
                .eq(OmsSaleOutStockItem::getPid, id)
        );
        List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = omsSaleOutStockItemDetailService.list(new LambdaQueryWrapper<OmsSaleOutStockItemDetail>()
                .eq(OmsSaleOutStockItemDetail::getGpId, id)
        );
        List<BdRepository> bdRepositories = repositoryService.list();
        List<BdPosition> bdPositions = positionService.list();
        omsSaleOutStockItems.stream().map(p -> bdRepositories.stream()
                .filter(s -> StringUtils.isNotEmpty(p.getRepositoryId()) && s.getId().equals(p.getRepositoryId()))
                .findFirst().map(s -> {
                    p.setRepositoryName(s.getName());
                    return p;
                }).orElse(null)).collect(Collectors.toList());

        omsSaleOutStockItems.stream().map(p -> bdPositions.stream()
                .filter(s -> StringUtils.isNotEmpty(p.getPositionId()) && s.getId().equals(p.getPositionId()))
                .findFirst().map(s -> {
                    p.setPositionName(s.getName());
                    return p;
                }).orElse(null)).collect(Collectors.toList());
        for (OmsSaleOutStockItem omsSaleOutStockItem : omsSaleOutStockItems) {
            List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetailList = new ArrayList<>();
            for (OmsSaleOutStockItemDetail omsSaleOutStockItemDetail : omsSaleOutStockItemDetails) {
                if (omsSaleOutStockItemDetail.getPid().equals(omsSaleOutStockItem.getId())) {
                    omsSaleOutStockItemDetailList.add(omsSaleOutStockItemDetail);
                }
            }
            omsSaleOutStockItem.setSaleOutStockItemDetails(omsSaleOutStockItemDetailList);
        }

        List<String> materialDetailIds = omsSaleOutStockItemDetails.stream().map(OmsSaleOutStockItemDetail::getMaterialDetailId).collect(Collectors.toList());
        List<MaterialDTO> materialDTOList = materialDetailMapper.getMaterialDTOByDetailIds(materialDetailIds);
        omsSaleOutStockItemDetails.stream().map(p -> bdRepositories.stream()
                .filter(s -> StringUtils.isNotEmpty(p.getRepositoryId()) && s.getId().equals(p.getRepositoryId()))
                .findFirst().map(s -> {
                    p.setRepositoryName(s.getName());
                    return p;
                }).orElse(null)).collect(Collectors.toList());

        omsSaleOutStockItemDetails.stream().map(p -> bdPositions.stream()
                .filter(s -> StringUtils.isNotEmpty(p.getPositionId()) && s.getId().equals(p.getPositionId()))
                .findFirst().map(s -> {
                    p.setPositionName(s.getName());
                    return p;
                }).orElse(null)).collect(Collectors.toList());
        omsSaleOutStockItemDetails.stream().map(p -> materialDTOList.stream()
                .filter(s -> StringUtils.isNotEmpty(p.getMaterialDetailId()) && s.getMaterialDetailId().equals(p.getMaterialDetailId()))
                .findFirst().map(s -> {
                    p.setMaterialNumber(s.getMaterialNumber());
                    p.setMaterialName(s.getMaterialName());
                    p.setMaterialGroup(s.getMaterialGroup());
                    p.setUnitName(s.getUnitName());
                    p.setMainPicUrl(s.getMainPicUrl());
                    p.setColor(s.getColor());
                    p.setSpecification(s.getSpecification());
                    return p;
                }).orElse(null)).collect(Collectors.toList());
        omsSaleOutStock.setSaleOutStockItems(omsSaleOutStockItems);
        return omsSaleOutStock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OmsSaleOutStock submit(String id) {
        OmsSaleOutStock result = this.detail(id);
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
    public OmsSaleOutStock auditUptData(OmsSaleOutStock omsSaleOutStock) {
        omsSaleOutStock.setBillStatus(Constants.INT_STATUS_AUDITED);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        omsSaleOutStock.setAuditTime(sdf.format(new Date()));
        omsSaleOutStock.setAuditorId(RequestUtils.getUserId());
        omsSaleOutStock.setAuditor(RequestUtils.getNickname());
        List<OmsSaleOutStockItem> saleOutStockItems = omsSaleOutStock.getSaleOutStockItems();
        if (saleOutStockItems != null && !saleOutStockItems.isEmpty()) {
            List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = new ArrayList<>();
            for (OmsSaleOutStockItem saleOutStockItem : saleOutStockItems) {
                omsSaleOutStockItemDetails.addAll(saleOutStockItem.getSaleOutStockItemDetails());
            }
            if (!omsSaleOutStockItemDetails.isEmpty()) {
                List<InvInventory> invs = new ArrayList<>();
                List<InvIoBill> invIoBills = new ArrayList<>();
                for (OmsSaleOutStockItemDetail omsSaleOutStockItemDetail : omsSaleOutStockItemDetails) {
                    omsSaleOutStockItemDetail.setOutStockQty(omsSaleOutStockItemDetail.getOutStockQty().add(omsSaleOutStockItemDetail.getQty()));
                    InvInventory inv = new InvInventory();
                    inv.setMaterialDetailId(omsSaleOutStockItemDetail.getMaterialDetailId());
                    inv.setQty(omsSaleOutStockItemDetail.getQty());
                    inv.setPiQty(omsSaleOutStockItemDetail.getPiQty());
                    inv.setRepositoryId(omsSaleOutStockItemDetail.getRepositoryId());
                    inv.setPositionId(StringUtils.isNotEmpty(omsSaleOutStockItemDetail.getPositionId()) ? omsSaleOutStockItemDetail.getPositionId() : "0");
                    inv.setPrice(omsSaleOutStockItemDetail.getPrice());
                    inv.setLot(omsSaleOutStockItemDetail.getLot());
                    invs.add(inv);

                    InvIoBill invIoBill = new InvIoBill();
                    invIoBill.setSrcId(omsSaleOutStockItemDetail.getGpId());
                    invIoBill.setSrcItemId(omsSaleOutStockItemDetail.getId());
                    invIoBill.setSrcType("销售出库单");
                    invIoBills.add(invIoBill);
                }
                if (!omsSaleOutStockItemDetailService.updateBatchById(omsSaleOutStockItemDetails)) {
                    throw new BizException("审核失败，源数据入库数量更新失败");
                }
                if (!invInventoryService.batchSubQty(invs, invIoBills, 1)) {
                    throw new BizException("即时库存更新失败");
                }
            } else {
                throw new BizException("审核失败，源数据异常1");
            }
        }
        return omsSaleOutStock;
    }



    @Transactional(rollbackFor = Exception.class)
    public OmsSaleOutStock unAuditUptData(OmsSaleOutStock omsSaleOutStock) {
        omsSaleOutStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        omsSaleOutStock.setAuditTime(sdf.format(new Date()));
        omsSaleOutStock.setAuditorId(RequestUtils.getUserId());
        omsSaleOutStock.setAuditor(RequestUtils.getNickname());
        List<OmsSaleOutStockItem> saleOutStockItems = omsSaleOutStock.getSaleOutStockItems();
        if (saleOutStockItems != null && !saleOutStockItems.isEmpty()) {
            List<OmsSaleOutStockItemDetail> omsSaleOutStockItemDetails = new ArrayList<>();
            for (OmsSaleOutStockItem saleOutStockItem : saleOutStockItems) {
                omsSaleOutStockItemDetails.addAll(saleOutStockItem.getSaleOutStockItemDetails());
            }
            if (!omsSaleOutStockItemDetails.isEmpty()) {
                List<InvInventory> invs = new ArrayList<>();
                List<InvIoBill> invIoBills = new ArrayList<>();
                for (OmsSaleOutStockItemDetail omsSaleOutStockItemDetail : omsSaleOutStockItemDetails) {
                    omsSaleOutStockItemDetail.setOutStockQty(omsSaleOutStockItemDetail.getOutStockQty().subtract(omsSaleOutStockItemDetail.getQty()));
                    InvInventory inv = new InvInventory();
                    inv.setMaterialDetailId(omsSaleOutStockItemDetail.getMaterialDetailId());
                    inv.setQty(omsSaleOutStockItemDetail.getQty());
                    inv.setPiQty(omsSaleOutStockItemDetail.getPiQty());
                    inv.setRepositoryId(omsSaleOutStockItemDetail.getRepositoryId());
                    inv.setPositionId(StringUtils.isNotEmpty(omsSaleOutStockItemDetail.getPositionId()) ? omsSaleOutStockItemDetail.getPositionId() : "0");
                    inv.setPrice(omsSaleOutStockItemDetail.getPrice());
                    inv.setLot(omsSaleOutStockItemDetail.getLot());
                    invs.add(inv);

                    InvIoBill invIoBill = new InvIoBill();
                    invIoBill.setSrcId(omsSaleOutStockItemDetail.getGpId());
                    invIoBill.setSrcItemId(omsSaleOutStockItemDetail.getId());
                    invIoBill.setSrcType("销售出库单");
                    invIoBills.add(invIoBill);
                }
                if (!invInventoryService.batchAddQty(invs, invIoBills, 2)) {
                    throw new BizException("即时库存更新失败");
                }
                if (!omsSaleOutStockItemDetailService.updateBatchById(omsSaleOutStockItemDetails)) {
                    throw new BizException("审核失败，源数据入库数量更新失败");
                }
            } else {
                throw new BizException("审核失败，源数据异常1");
            }
        }
        updateById(omsSaleOutStock);
        return omsSaleOutStock;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<OmsSaleOutStock> list = this.list(
                new LambdaQueryWrapper<OmsSaleOutStock>()
                        .in(OmsSaleOutStock::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<OmsSaleOutStock> submitList = list.stream().filter(r -> (r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (OmsSaleOutStock omsSaleOutStock : submitList) {
                    this.submit(omsSaleOutStock.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(OmsSaleOutStock omsSaleOutStock) throws Exception {
        if (omsSaleOutStock.getBillStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(omsSaleOutStock.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(omsSaleOutStock.getWorkFlowId());
            flowOperationInfo.setFormData(omsSaleOutStock);
            flowOperationInfo.setUserId(omsSaleOutStock.getUserId());
            flowOperationInfo.setChildNodes(omsSaleOutStock.getChildNodes());
            flowOperationInfo.setCurrentNodeId(omsSaleOutStock.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(omsSaleOutStock.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                omsSaleOutStock.setWorkFlowId("");
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
            ids.add(omsSaleOutStock.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, omsSaleOutStock.getWorkFlow().getId());
            omsSaleOutStock.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            omsSaleOutStock.setNodeStatus(currentNodes.get(0).getStatus());
            omsSaleOutStock.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(omsSaleOutStock.getId()) == 1) {
                omsSaleOutStock = this.auditUptData(omsSaleOutStock);
            }
            // 驳回
            if (circulationOperationService.whetherLast(omsSaleOutStock.getId()) == 2) {
                omsSaleOutStock.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(omsSaleOutStock)) {
            throw new BizException("操作失败");
        }

        return Result.success(omsSaleOutStock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(OmsSaleOutStock omsSaleOutStock) throws Exception {
        List<String> ids = omsSaleOutStock.getIds();
        List<OmsSaleOutStock> mos = this.list(
                new LambdaQueryWrapper<OmsSaleOutStock>()
                        .in(OmsSaleOutStock::getId, ids)
        );
        if (mos != null && mos.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, omsSaleOutStock.getWorkFlow().getId());
            for (int i = 0; i < mos.size(); i++) {
                OmsSaleOutStock item = mos.get(i);
                item.setBillStatus(omsSaleOutStock.getBillStatus());
                item.setWorkFlowId(omsSaleOutStock.getWorkFlowId());
                item.setUserId(omsSaleOutStock.getUserId());
                item.setChildNodes(omsSaleOutStock.getChildNodes());
                item.setChildNodeApprovalResult(omsSaleOutStock.getChildNodeApprovalResult());
                item.setWorkFlow(omsSaleOutStock.getWorkFlow());
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
    public OmsSaleOutStock unAudit(String id) throws Exception {
        OmsSaleOutStock result = this.detail(id);
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
        List<OmsSaleOutStock> list = this.listByIds(idList);
        if (list != null && list.size() > 0) {
            List<OmsSaleOutStock> unAuditList = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (OmsSaleOutStock omsSaleOutStock : unAuditList) {
                    this.unAudit(omsSaleOutStock.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public List<Integer> getStatusCountAll(OmsSaleOutStock omsSaleOutStock) {
//        omsSaleOutStock.setBillStatus(null);
//        List<PrdInStockItemVo> itemVoList = inStockMapper.wxPage(omsSaleOutStock);
//        List<Integer> list = new ArrayList<>();
//        Integer allCount = itemVoList.size();
//        long count1 = itemVoList.stream().filter(s -> s.getBillStatus().equals(0)).count();
//        long count2 = itemVoList.stream().filter(s -> s.getBillStatus().equals(3)).count();
//        long count3 = itemVoList.stream().filter(s -> s.getBillStatus().equals(2)).count();
//        list.add(allCount);
//        list.add(Integer.valueOf(String.valueOf(count1)));
//        list.add(Integer.valueOf(String.valueOf(count2)));
//        list.add(Integer.valueOf(String.valueOf(count3)));
//        return list;
        return null;
    }

    @Override
    public IPage<OmsSaleOutStockDTO> getOutStockSummary(Page page, OmsSaleOutStockDTO omsSaleOutStockDTO) {
        return baseMapper.getOutStockSummary(page,omsSaleOutStockDTO);
    }

    @Override
    public void exportOutStockSummary(HttpServletResponse response, OmsSaleOutStockDTO omsSaleOutStockDTO) {
        List<OmsSaleOutStockDTO> outStockSummaryList = baseMapper.getOutStockSummary(omsSaleOutStockDTO);
        // 创建一个excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 新建sheet页
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //设置默认宽度好像必须先设置默认高度，不然不生效。。。。。。
        sheet.setDefaultRowHeight((short) (1 * 256));
        sheet.setDefaultColumnWidth(20);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setBold(true);
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyle.setFont(fontStyle);
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        HSSFCellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);
        cellStyleContent.setBorderBottom(BorderStyle.THIN);
        cellStyleContent.setBorderLeft(BorderStyle.THIN);
        cellStyleContent.setBorderRight(BorderStyle.THIN);
        HSSFFont fontStyleContent = workbook.createFont();
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyleContent.setFont(fontStyleContent);
        //第一行   头
        HSSFRow row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints(20);
        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("日期");
        title.add("商家编码");
        title.add("货品编码");
        title.add("货品名称");
        title.add("颜色");
        title.add("数量");
        title.add("包数");
        title.add("备注");
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        for (int j = 0, recordsSize = outStockSummaryList.size(); j < recordsSize; j++) {
            OmsSaleOutStockDTO record = outStockSummaryList.get(j);
            HSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            rowData.createCell(1).setCellValue(record.getOutStockDate());
            rowData.createCell(2).setCellValue(StringUtils.isNotEmpty(record.getSjNumber())?record.getSjNumber():"");
            rowData.createCell(3).setCellValue(record.getMaterialNumber());
            rowData.createCell(4).setCellValue(record.getMaterialName());
            rowData.createCell(5).setCellValue(record.getColor());
            rowData.createCell(6).setCellValue(record.getQty());
            rowData.createCell(7).setCellValue(record.getLotQty());
            rowData.createCell(8).setCellValue(record.getRemark());
            rowData.getCell(0).setCellStyle(cellStyleContent);
            rowData.getCell(1).setCellStyle(cellStyleContent);
            rowData.getCell(2).setCellStyle(cellStyleContent);
            rowData.getCell(3).setCellStyle(cellStyleContent);
            rowData.getCell(4).setCellStyle(cellStyleContent);
            rowData.getCell(5).setCellStyle(cellStyleContent);
            rowData.getCell(6).setCellStyle(cellStyleContent);
            rowData.getCell(7).setCellStyle(cellStyleContent);
            rowData.getCell(8).setCellStyle(cellStyleContent);
        }
        ExcelUtils.buildXlsxDocument("出库汇总表", workbook, response);
    }
}
