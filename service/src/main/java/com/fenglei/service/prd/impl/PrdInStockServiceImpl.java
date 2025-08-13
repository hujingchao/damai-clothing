package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.prd.PrdCuttingTicketItemReportMapper;
import com.fenglei.mapper.prd.PrdInStockMapper;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.oms.entity.OmsSaleOutStockItemDetail;
import com.fenglei.model.prd.entity.*;
import com.fenglei.model.prd.vo.PrdInStockItemVo;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.prd.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 包装单 服务实现类
 * </p>
 *
 * @author zgm
 * @since 2024-04-18
 */
@Service
@AllArgsConstructor
public class PrdInStockServiceImpl extends ServiceImpl<PrdInStockMapper, PrdInStock> implements IPrdInStockService {

    private IPrdInStockItemService inStockItemService;
    private PrdInStockMapper inStockMapper;
    private IPrdCuttingService cuttingService;
    private IPrdCuttingTicketItemService cuttingTicketItemService;
    private IPrdCuttingTicketService cuttingTicketService;
    private IPrdCuttingTicketItemReportService cuttingTicketItemReportService;
    private IPrdCuttingRouteService cuttingRouteService;
    private PrdMoColorSpecDataService moColorSpecDataService;
    private IPrdFloatSettingService prdFloatSettingService;
    private InvInventoryService invInventoryService;
    private IPrdTicketLogService prdTicketLogService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdInStock add(PrdInStock prdInStock) {
        List<PrdInStockItem> itemList = prdInStock.getItemList();
        this.checkLastRouteReportInfo(itemList);

        if (StringUtils.isEmpty(prdInStock.getNo())) {
            String no = this.getNo();
            prdInStock.setNo(no);
        }
        prdInStock.setBillStatus(2);
        this.save(prdInStock);
        //判断录入的数量是否符合
        if(!checkInStockCount(itemList)){
            throw new BizException("数量校验未通过且没有输入理由，请核实");
        }
        for (PrdInStockItem item : itemList) {
            item.setPid(prdInStock.getId());
        }
        inStockItemService.saveBatch(itemList);

        //入库 修改库存
        saveInv(prdInStock, itemList, "add");
        //反写入库数量
        this.rewriteInStockNum(itemList);

        //日志
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
        for (PrdInStockItem prdInStockItem : itemList) {
            remark.append("入库票号：");
            remark.append(prdInStockItem.getTicketNo());
            remark.append("，");
            remark.append("入库数量：");
            remark.append(prdInStockItem.getInStockNum());
            if (StringUtils.isNotEmpty(prdInStockItem.getReason())) {
                remark.append("，");
                remark.append("原因：");
                remark.append(prdInStockItem.getReason());
            }
            remark.append("；");
        }
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);
        return prdInStock;
    }

    /**
     * 校验末道工序是否上数
     *
     * @param itemList 入库明细
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkLastRouteReportInfo(List<PrdInStockItem> itemList) {
        List<String> ticketItemIds = itemList.stream().map(PrdInStockItem::getTicketItemId).distinct().collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listByIds(ticketItemIds);
        if (prdCuttingTicketItems.isEmpty()) {
            throw new BizException("裁床单工票明细不存在！");
        }
        List<String> cuttingIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getGpId).collect(Collectors.toList());
        for (String cuttingId : cuttingIds) {
            List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingId);
            if (cuttingRoutes.isEmpty()) {
                throw new BizException("裁床单工序不存在！");
            }
            PrdCuttingRoute lastRoute = cuttingRoutes.get(cuttingRoutes.size() - 1);
            String id = lastRoute.getId();
            int count = cuttingTicketItemReportService.count(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class)
                    .eq(PrdCuttingTicketItemReport::getCuttingRouteId, id)
            );
            if (count == 0) {
                throw new BizException("裁床单工序" + lastRoute.getProcedureName() + "未上数！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveInv(PrdInStock prdInStock, List<PrdInStockItem> itemList, String type) {

        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdInStockItem inStockItem : itemList) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(inStockItem.getSkuId());
            inv.setQty(inStockItem.getInStockNum());
            inv.setPiQty(BigDecimal.ZERO);
            inv.setRepositoryId(prdInStock.getRepositoryId());
            inv.setPositionId(StringUtils.isNotEmpty(prdInStock.getPositionId()) ? prdInStock.getPositionId() : "0");
            inv.setPrice(BigDecimal.ZERO);
            inv.setLot("");
            invs.add(inv);

            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(inStockItem.getPid());
            invIoBill.setSrcItemId(inStockItem.getId());
            invIoBill.setSrcType("生产入库单");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty()) {
            if (type.equals("add")) {
                if (!invInventoryService.batchAddQty(invs, invIoBills, 1)) {
                    throw new BizException("即时库存更新失败");
                }
            } else {
                if (!invInventoryService.batchSubQty(invs, invIoBills, 2)) {
                    throw new BizException("即时库存更新失败");
                }
            }

        }

    }

    /**
     * 顺序反写 不能调位置
     *
     * @param itemList
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewriteInStockNum(List<PrdInStockItem> itemList) {
        //反写 prd_cutting_ticket_item 裁床单工票明细
        List<String> collect = itemList.stream().map(PrdInStockItem::getTicketItemId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listDetailByIds(collect);
        for (PrdInStockItem inStockItem : itemList) {
            for (PrdCuttingTicketItem ticketItem : prdCuttingTicketItems) {
                if (inStockItem.getTicketItemId().equals(ticketItem.getId())) {
                    ticketItem.setInStockQty(ticketItem.getInStockQty().add(inStockItem.getInStockNum()));
                    ticketItem.setReportedQty(ticketItem.getInStockQty());
                    ticketItem.setReason(inStockItem.getReason());
                    break;
                }
            }
        }
        cuttingTicketItemService.updateBatchById(prdCuttingTicketItems);

        //反写 prd_cutting_ticket 裁床单工票
        List<String> ticketIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(Collectors.toList());
        List<PrdCuttingTicket> prdCuttingTickets = cuttingTicketService.listDetailByIds(ticketIds);
        for (PrdCuttingTicket prdCuttingTicket : prdCuttingTickets) {
            BigDecimal reportQty = BigDecimal.ZERO;
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicketItem cuttingTicketItem : prdCuttingTicket.getCuttingTicketItems()) {
                reportQty = reportQty.add(cuttingTicketItem.getReportedQty());
                inStockQty = inStockQty.add(cuttingTicketItem.getInStockQty());
            }
            prdCuttingTicket.setReportedQty(reportQty);
            prdCuttingTicket.setFinishQty(inStockQty);
        }
        cuttingTicketService.updateBatchById(prdCuttingTickets);

        //反写 prd_cutting 裁床单主表
        List<String> cuttingIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getGpId).collect(Collectors.toList());
        List<PrdCutting> cuttingList = cuttingService.listDetailByIds(cuttingIds);
        for (PrdCutting prdCutting : cuttingList) {
            BigDecimal reportQty = BigDecimal.ZERO;
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicket cuttingTicket : prdCutting.getCuttingTickets()) {
                reportQty = reportQty.add(cuttingTicket.getReportedQty());
                inStockQty = inStockQty.add(cuttingTicket.getFinishQty());
            }
            prdCutting.setReportedQty(reportQty);
            prdCutting.setFinishQty(inStockQty);
        }
        cuttingService.updateBatchById(cuttingList);

        //反写 prd_mo_color_spec_data 生产订单分录 入库数量
        List<String> moItemIds = prdCuttingTickets.stream().map(PrdCuttingTicket::getSrcItemId).collect(Collectors.toList());
        List<PrdMoColorSpecData> moColorSpecDataList = moColorSpecDataService.listDetailByIds(moItemIds);
        for (PrdMoColorSpecData moColorSpecData : moColorSpecDataList) {
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicket prdCuttingTicket : moColorSpecData.getTicketList()) {
                inStockQty = inStockQty.add(prdCuttingTicket.getFinishQty());
            }
            moColorSpecData.setInstockQty(inStockQty);
        }
        moColorSpecDataService.updateBatchById(moColorSpecDataList);


        //反写prd_cutting_ticket_item_report 裁床单工序汇报
        List<PrdCuttingTicketItemReport> updateList = new ArrayList<>();
        for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
            for (PrdCuttingTicketItemReport cuttingTicketItemReport : prdCuttingTicketItem.getCuttingTicketItemReports()) {
                cuttingTicketItemReport.setReportedQty(prdCuttingTicketItem.getReportedQty());
                cuttingTicketItemReport.setIsInStock(true);
            }
            updateList.addAll(prdCuttingTicketItem.getCuttingTicketItemReports());
        }
        if (updateList.isEmpty()) {
            throw new BizException("尚未上数！");
        }
        cuttingTicketItemReportService.updateBatchById(updateList);

        //反写 prd_cutting_route 工序详情
        List<String> routeIds = updateList.stream().map(PrdCuttingTicketItemReport::getCuttingRouteId).collect(Collectors.toList());
        List<PrdCuttingRoute> prdCuttingRoutes = cuttingRouteService.listDetailByIds(routeIds);
        for (PrdCuttingRoute prdCuttingRoute : prdCuttingRoutes) {
            BigDecimal reportQty = BigDecimal.ZERO;
            for (PrdCuttingTicketItemReport itemReport : prdCuttingRoute.getCuttingTicketItemReports()) {
                reportQty = reportQty.add(itemReport.getReportedQty());
            }
            prdCuttingRoute.setReportedQty(reportQty);
        }
        cuttingRouteService.updateBatchById(prdCuttingRoutes);

    }


    /**
     * 反写入库数量 删除入库单时只反写 工票上的入库信息
     *
     * @param itemList
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewriteDelInStockNum(List<PrdInStockItem> itemList) {
        //反写 prd_cutting_ticket_item 裁床单工票明细
        List<String> collect = itemList.stream().map(PrdInStockItem::getTicketItemId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listDetailByIds(collect);
        for (PrdInStockItem inStockItem : itemList) {
            for (PrdCuttingTicketItem ticketItem : prdCuttingTicketItems) {
                if (inStockItem.getTicketItemId().equals(ticketItem.getId())) {
                    ticketItem.setInStockQty(ticketItem.getInStockQty().subtract(inStockItem.getInStockNum()));
                    break;
                }
            }
        }
        cuttingTicketItemService.updateBatchById(prdCuttingTicketItems);

        //反写 prd_cutting_ticket 裁床单工票
        List<String> ticketIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(Collectors.toList());
        List<PrdCuttingTicket> prdCuttingTickets = cuttingTicketService.listDetailByIds(ticketIds);
        for (PrdCuttingTicket prdCuttingTicket : prdCuttingTickets) {
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicketItem cuttingTicketItem : prdCuttingTicket.getCuttingTicketItems()) {
                inStockQty = inStockQty.add(cuttingTicketItem.getInStockQty());
            }
            prdCuttingTicket.setFinishQty(inStockQty);
        }
        cuttingTicketService.updateBatchById(prdCuttingTickets);


        //反写 prd_cutting 裁床单主表
        List<String> cuttingIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getGpId).collect(Collectors.toList());
        List<PrdCutting> cuttingList = cuttingService.listDetailByIds(cuttingIds);
        for (PrdCutting prdCutting : cuttingList) {
            BigDecimal reportQty = BigDecimal.ZERO;
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicket cuttingTicket : prdCutting.getCuttingTickets()) {
                reportQty = reportQty.add(cuttingTicket.getReportedQty());
                inStockQty = inStockQty.add(cuttingTicket.getFinishQty());
            }
            prdCutting.setReportedQty(reportQty);
            prdCutting.setFinishQty(inStockQty);
        }
        cuttingService.updateBatchById(cuttingList);

        //反写 prd_mo_color_spec_data 生产订单分录 入库数量
        List<String> moItemIds = prdCuttingTickets.stream().map(PrdCuttingTicket::getSrcItemId).collect(Collectors.toList());
        List<PrdMoColorSpecData> moColorSpecDataList = moColorSpecDataService.listDetailByIds(moItemIds);
        for (PrdMoColorSpecData moColorSpecData : moColorSpecDataList) {
            BigDecimal inStockQty = BigDecimal.ZERO;
            for (PrdCuttingTicket prdCuttingTicket : moColorSpecData.getTicketList()) {
                inStockQty = inStockQty.add(prdCuttingTicket.getFinishQty());
            }
            moColorSpecData.setInstockQty(inStockQty);
        }
        moColorSpecDataService.updateBatchById(moColorSpecDataList);


        //new 取消入库  汇报数量改成原来的
        //反写prd_cutting_ticket_item_report 裁床单工序汇报
        List<PrdCuttingTicketItemReport> updateList = new ArrayList<>();
        for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
            for (PrdCuttingTicketItemReport cuttingTicketItemReport : prdCuttingTicketItem.getCuttingTicketItemReports()) {
                cuttingTicketItemReport.setReportedQty(cuttingTicketItemReport.getRealReportedQty());
                cuttingTicketItemReport.setIsInStock(false);
            }
            updateList.addAll(prdCuttingTicketItem.getCuttingTicketItemReports());
        }
        if (updateList.isEmpty()) {
            throw new BizException("尚未上数！");
        }
        cuttingTicketItemReportService.updateBatchById(updateList);

        //反写 prd_cutting_route 工序详情
        List<String> routeIds = updateList.stream().map(PrdCuttingTicketItemReport::getCuttingRouteId).collect(Collectors.toList());
        List<PrdCuttingRoute> prdCuttingRoutes = cuttingRouteService.listDetailByIds(routeIds);
        for (PrdCuttingRoute prdCuttingRoute : prdCuttingRoutes) {
            BigDecimal reportQty = BigDecimal.ZERO;
            for (PrdCuttingTicketItemReport itemReport : prdCuttingRoute.getCuttingTicketItemReports()) {
                reportQty = reportQty.add(itemReport.getReportedQty());
            }
            prdCuttingRoute.setReportedQty(reportQty);
        }
        cuttingRouteService.updateBatchById(prdCuttingRoutes);

    }


    @Override
    public String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "RKD" + sdf.format(new Date()) + "-";
        PrdInStock old = this.getOne(Wrappers.lambdaQuery(PrdInStock.class)
                .likeRight(PrdInStock::getNo, no)
                .orderByDesc(PrdInStock::getNo)
                .last("limit 1")
        );
        if (old != null) {
            String maxNo = old.getNo();
            int pos = maxNo.lastIndexOf("-");
            String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
            int maxNoInt = Integer.parseInt(maxIdxStr);
            String noIdxStr = String.format("%04d", maxNoInt + 1);
            no = no + noIdxStr;
        } else {
            no = no + "0001";
        }
        return no;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        PrdInStock byId = getById(id);
        boolean res = removeById(id);

        List<PrdInStockItem> list = inStockItemService.list(new LambdaQueryWrapper<PrdInStockItem>().eq(PrdInStockItem::getPid, id));

        //修改库存
        saveInv(byId, list, "sub");
        this.rewriteDelInStockNum(list);
        inStockItemService.remove(new LambdaQueryWrapper<PrdInStockItem>()
                .eq(PrdInStockItem::getPid, id));

        //日志
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
        for (PrdInStockItem prdInStockItem : list) {
            remark.append("删除入库 票号：");
            remark.append(prdInStockItem.getTicketNo());
            remark.append(",");
            remark.append("入库数量：");
            remark.append(prdInStockItem.getInStockNum());
            remark.append("；");
        }
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);

        return res;
    }


    /**
     * 分页查询
     */
    @Override
    public IPage<PrdInStock> myPage(Page page, PrdInStock prdInStock) {
        return null;
    }

    @Override
    public IPage<PrdInStockItemVo> wxPage(Page page, PrdInStockItemVo itemVo) {
        IPage<PrdInStockItemVo> prdInStockItemVoIPage = inStockMapper.wxPage(page, itemVo);
        return prdInStockItemVoIPage;
    }

    @Override
    public List<Integer> getStatusCountAll(PrdInStockItemVo itemVo) {
        itemVo.setBillStatus(null);
        List<PrdInStockItemVo> itemVoList = inStockMapper.wxPage(itemVo);
        List<Integer> list = new ArrayList<>();
        Integer allCount = itemVoList.size();
        long count1 = itemVoList.stream().filter(s -> s.getBillStatus().equals(0)).count();
        long count2 = itemVoList.stream().filter(s -> s.getBillStatus().equals(3)).count();
        long count3 = itemVoList.stream().filter(s -> s.getBillStatus().equals(2)).count();
        list.add(allCount);
        list.add(Integer.valueOf(String.valueOf(count1)));
        list.add(Integer.valueOf(String.valueOf(count2)));
        list.add(Integer.valueOf(String.valueOf(count3)));
        return list;
    }

    /**
     * 详情
     */
    @Override
    public PrdInStock itemDetail(String id, String itemId) {
        PrdInStock inStock = inStockMapper.itemDetail(id, itemId);
        return inStock;
    }

    @Override
    public boolean itemSetTop(String id, Integer setTop) {
        PrdInStockItem byId = inStockItemService.getById(id);
        byId.setSetTop(setTop);
        return inStockItemService.updateById(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean itemRemove(String id) {
        List<PrdInStockItem> removeList = new ArrayList<>();
        PrdInStockItem byId = inStockItemService.getById(id);

        PrdInStock inStockId = getById(byId.getPid());
        removeList.add(byId);
        //修改库存
        saveInv(inStockId, removeList, "sub");
        this.rewriteDelInStockNum(removeList);
        boolean b = inStockItemService.removeById(id);
        List<PrdInStockItem> list = inStockItemService.list(new LambdaQueryWrapper<PrdInStockItem>().eq(PrdInStockItem::getPid, byId.getPid()));
        if (list.isEmpty()) {
            removeById(byId.getPid());
        }

        //日志
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
            remark.append("删除入库 票号：");
            remark.append(byId.getTicketNo());
            remark.append(",");
            remark.append("入库数量：");
            remark.append(byId.getInStockNum());
            remark.append("；");
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);

        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean itemUpdate(PrdInStock prdInStock) {
        if (StringUtils.isEmpty(prdInStock.getItemId())) {
            throw new BizException("修改单据有误，请刷新重试");
        }
        List<PrdInStockItem> itemList = prdInStock.getItemList();
        this.checkLastRouteReportInfo(itemList);

        boolean b = updateById(prdInStock);
        List<String> removeIds = new ArrayList<>();
        removeIds.add(prdInStock.getItemId());
        List<PrdInStockItem> oldItemList = inStockItemService.listByIds(removeIds);

        //减少库存
        saveInv(prdInStock, oldItemList, "sub");
        this.rewriteDelInStockNum(oldItemList);
        inStockItemService.removeById(prdInStock.getItemId());

        for (PrdInStockItem item : itemList) {
            item.setPid(prdInStock.getId());
        }
        //判断录入的数量是否符合
        if(!checkInStockCount(itemList)){
            throw new BizException("数量校验未通过且没有输入理由，请核实");
        }
        inStockItemService.saveBatch(itemList);
        //增加库存
        saveInv(prdInStock, itemList, "add");
        this.rewriteInStockNum(itemList);



        //日志
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
        for (PrdInStockItem inStockItem : oldItemList) {
            for (PrdInStockItem prdInStockItem : itemList) {
                if(inStockItem.getId().equals(prdInStockItem.getId())
                        &&!(inStockItem.getInStockNum().compareTo(prdInStockItem.getInStockNum())==0)
                       ){
                    remark.append("入库修改 票号：");
                    remark.append(prdInStockItem.getTicketNo());
                    remark.append(",入库数量");
                    remark.append(inStockItem.getInStockNum());
                    remark.append("改为");
                    remark.append(prdInStockItem.getInStockNum());
                    remark.append("；");
                }
            }
        }
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);


        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkInStockCount(List<PrdInStockItem> itemList) {
        //判断录入的数量是否符合
        PrdFloatSetting setting = prdFloatSettingService.getSetting();
        List<String> ticketItemIds = itemList.stream().map(PrdInStockItem::getTicketItemId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listByIds(ticketItemIds);
        List<String> ticketIds = prdCuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(Collectors.toList());
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByIds(ticketIds);
        boolean flag = false;
        for (PrdInStockItem inStockItem : itemList) {
            for (PrdCuttingTicketItem cuttingTicketItem : prdCuttingTicketItems) {
                BigDecimal oldInStockQty = cuttingTicketItem.getInStockQty();
                if (oldInStockQty.compareTo(BigDecimal.ZERO) > 0) {
                    throw new BizException("票据号：" + cuttingTicketItem.getTicketNo() + "已收货，不能重复收货！");
                }
                if (inStockItem.getTicketItemId().equals(cuttingTicketItem.getId())) {
                    if(StringUtils.isNotEmpty(inStockItem.getReason())){
                        flag  = true;
                    }else{
                        PrdCuttingTicket prdCuttingTicket = cuttingTickets.stream().filter(t -> t.getId().equals(cuttingTicketItem.getPid())).findFirst().orElse(null);
                        if (StringUtils.isNull(prdCuttingTicket)) {
                            throw new BizException("票据信息不存在！");
                        }
                        String srcType = prdCuttingTicket.getSrcType();
                        BigDecimal minRate = srcType.equals("生产订单") ? setting.getPrdDown() : setting.getPlanDown();
                        BigDecimal maxRate = srcType.equals("生产订单") ? setting.getPrdUp() : setting.getPlanUp();
                        BigDecimal qty = cuttingTicketItem.getQty();
                        BigDecimal minQty = new BigDecimal("100").subtract(minRate).multiply(qty).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                        BigDecimal maxQty = maxRate.add(new BigDecimal("100")).multiply(qty).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                        if (minQty.compareTo(inStockItem.getInStockNum()) > 0) {
                            throw new BizException("票据号" + inStockItem.getTicketNo() + "的入库数量低于最低限度，请填写原因重新确认！");
                        }
                        if (maxQty.compareTo(inStockItem.getInStockNum()) < 0) {
                            throw new BizException("票据号" + inStockItem.getTicketNo() + "的入库数量高于最高限度，请填写原因重新确认！");
                        }
                        flag  = true;
                    }
                }
            }
        }
        return flag;
    }
}
