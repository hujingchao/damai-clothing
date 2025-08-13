package com.fenglei.service.prd.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
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
import com.fenglei.mapper.basedata.BdProcedureMapper;
import com.fenglei.mapper.basedata.BdUnitMapper;
import com.fenglei.mapper.prd.*;
import com.fenglei.model.basedata.*;
import com.fenglei.model.basedata.dto.StaffSpecialRouteDto;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.prd.dto.CuttingFilterDto;
import com.fenglei.model.prd.dto.DoProcedureReportDto;
import com.fenglei.model.prd.dto.MoAsSrcDto;
import com.fenglei.model.prd.dto.UpdateTicketItemDto;
import com.fenglei.model.prd.entity.*;
import com.fenglei.model.prd.vo.*;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdMaterialDetailService;
import com.fenglei.service.basedata.BdMaterialRouteService;
import com.fenglei.service.basedata.BdMaterialService;
import com.fenglei.service.basedata.IBdEquipmentService;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.prd.*;
import com.fenglei.service.system.SysFilesService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 裁床单 - 主体 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Service
public class PrdCuttingServiceImpl extends ServiceImpl<PrdCuttingMapper, PrdCutting> implements IPrdCuttingService {

    @Autowired
    IPrdCuttingTicketService cuttingTicketService;
    @Autowired
    IPrdCuttingTicketItemService cuttingTicketItemService;
    @Autowired
    IPrdCuttingTicketItemReportService cuttingTicketItemReportService;
    @Autowired
    PrdCuttingTicketItemReportMapper prdCuttingTicketItemReportMapper;
    @Autowired
    IPrdCuttingRouteService cuttingRouteService;
    @Autowired
    IPrdCuttingRawService cuttingRawService;
    @Autowired
    IPrdCuttingAuxIoService cuttingAuxIoService;
    @Autowired
    IPrdCuttingRawIoService cuttingRawIoService;
    @Autowired
    BdMaterialService materialService;
    @Autowired
    BdMaterialDetailService materialDetailService;
    @Autowired
    BdMaterialRouteService materialRouteService;
    @Autowired
    IBdEquipmentService equipmentService;
    @Autowired
    SysFilesService filesService;
    @Autowired
    PrdMoMapper moMapper;
    @Autowired
    PrdMoProcessMapper moProcessMapper;
    @Autowired
    PrdMoColorSpecDataService moColorSpecDataService;
    @Autowired
    PrdMoMaterialDetailMapper moMaterialDetailMapper;
    @Autowired
    PrdMoMaterialAuxMapper moMaterialAuxMapper;
    @Autowired
    BdUnitMapper unitMapper;

    @Autowired
    InvInventoryService inventoryService;

    @Autowired
    SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Autowired
    CirculationOperationService circulationOperationService;
    @Resource
    BdProcedureMapper bdProcedureMapper;
    @Resource
    IPrdTicketLogService prdTicketLogService;


    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized PrdCutting add(PrdCutting prdCutting) {
        prdCutting.setBillStatus(Constants.INT_STATUS_CREATE);
        if (StringUtils.isEmpty(prdCutting.getNo())) {
            String no = this.getNo();
            prdCutting.setNo(no);
        }
        try {
            this.save(prdCutting);
        } catch (Exception e) {
            if (e.getMessage().contains("'no_unique'")) {
                throw new BizException("款号重复！");
            }
            throw new BizException(e.getMessage());
        }
        List<PrdCuttingTicket> cuttingTickets = prdCutting.getCuttingTickets();
        List<PrdCuttingRoute> cuttingRoutes = prdCutting.getCuttingRoutes();
        List<PrdCuttingRaw> cuttingRaws = prdCutting.getCuttingRaws();
        Map<String, BigDecimal> moColorSpecData = new HashMap<>();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            cuttingTicket.setPid(prdCutting.getId());
            String srcItemId = cuttingTicket.getSrcItemId();
            BigDecimal sumQty = cuttingTicket.getSumQty();
            if (moColorSpecData.containsKey(srcItemId)) {
                moColorSpecData.put(srcItemId, moColorSpecData.get(srcItemId).add(sumQty));
            } else {
                moColorSpecData.put(srcItemId, sumQty);
            }
        }
        for (PrdCuttingRoute cuttingRoute : cuttingRoutes) {
            cuttingRoute.setPid(prdCutting.getId());
        }
        for (PrdCuttingRaw cuttingRaw : cuttingRaws) {
            cuttingRaw.setPid(prdCutting.getId());
        }
        cuttingTicketService.batchAdd(cuttingTickets);
        cuttingRouteService.batchAdd(cuttingRoutes);
        cuttingRawService.batchAdd(cuttingRaws);

        // 反写生产订单-颜色规格详情
        this.reWrite(moColorSpecData);

        // 自动审核
        this.submit(prdCutting.getId());
        prdCutting.setBillStatus(Constants.INT_STATUS_AUDITED);
        updateById(prdCutting);
        return prdCutting;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) throws Exception {
        Map<String, BigDecimal> moColorSpecData = new HashMap<>();
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByPid(id);
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            String srcItemId = cuttingTicket.getSrcItemId();
            BigDecimal sumQty = BigDecimal.ZERO.subtract(cuttingTicket.getSumQty());
            if (moColorSpecData.containsKey(srcItemId)) {
                moColorSpecData.put(srcItemId, moColorSpecData.get(srcItemId).add(sumQty));
            } else {
                moColorSpecData.put(srcItemId, sumQty);
            }
        }
        //反写 更新库存
        PrdCutting byId = getById(id);
        if (byId.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            this.unAudit(id);
        }
        // 反写生产订单-颜色规格详情
        this.reWrite(moColorSpecData);
        cuttingTicketService.removeByPid(id);
        cuttingRouteService.removeByPid(id);
        cuttingRawService.removeByPid(id);
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) throws Exception {
        Map<String, BigDecimal> moColorSpecData = new HashMap<>();
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByPids(ids);
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            String srcItemId = cuttingTicket.getSrcItemId();
            BigDecimal sumQty = BigDecimal.ZERO.subtract(cuttingTicket.getSumQty());
            if (moColorSpecData.containsKey(srcItemId)) {
                moColorSpecData.put(srcItemId, moColorSpecData.get(srcItemId).add(sumQty));
            } else {
                moColorSpecData.put(srcItemId, sumQty);
            }
        }
        //反写 更新库存
        List<PrdCutting> prdCuttings = listByIds(ids);
        for (PrdCutting prdCutting : prdCuttings) {
            if (prdCutting.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
                this.unAudit(prdCutting.getId());
            }
        }


        // 反写生产订单-颜色规格详情
        this.reWrite(moColorSpecData);
        cuttingTicketService.removeByPids(ids);
        cuttingRouteService.removeByPids(ids);
        cuttingRawService.removeByPids(ids);
        return this.removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(PrdCutting prdCutting) throws Exception {
        //保存时将前台传过来的反写字段设为null
        prdCutting.setReportedQty(null);
        prdCutting.setFinishQty(null);
        for (PrdCuttingTicket cuttingTicket : prdCutting.getCuttingTickets()) {
            cuttingTicket.setReportedQty(null);
            cuttingTicket.setFinishQty(null);
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicket.getCuttingTicketItems()) {
                cuttingTicketItem.setReportedQty(null);
                cuttingTicketItem.setInStockQty(null);
            }
        }

        prdCutting.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        this.unAudit(prdCutting.getId());
        Map<String, BigDecimal> moColorSpecData = new HashMap<>();
        // 旧数据
        List<PrdCuttingTicket> oldCuttingTickets = cuttingTicketService.listByPid(prdCutting.getId());
        List<String> needDelCuttingTicketIds = new ArrayList<>();
        for (PrdCuttingTicket cuttingTicket : oldCuttingTickets) {
            needDelCuttingTicketIds.add(cuttingTicket.getId());
            String srcItemId = cuttingTicket.getSrcItemId();
            BigDecimal sumQty = BigDecimal.ZERO.subtract(cuttingTicket.getSumQty());
            if (moColorSpecData.containsKey(srcItemId)) {
                moColorSpecData.put(srcItemId, moColorSpecData.get(srcItemId).add(sumQty));
            } else {
                moColorSpecData.put(srcItemId, sumQty);
            }
        }

        List<PrdCuttingTicket> cuttingTickets = prdCutting.getCuttingTickets();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            cuttingTicket.setPid(prdCutting.getId());
            String srcItemId = cuttingTicket.getSrcItemId();
            BigDecimal sumQty = cuttingTicket.getSumQty();
            if (moColorSpecData.containsKey(srcItemId)) {
                moColorSpecData.put(srcItemId, moColorSpecData.get(srcItemId).add(sumQty));
            } else {
                moColorSpecData.put(srcItemId, sumQty);
            }
        }
        // 反写生产订单-颜色规格详情
        this.reWrite(moColorSpecData);
        cuttingTicketItemService.removeByPids(needDelCuttingTicketIds);
        cuttingTicketService.removeByIds(needDelCuttingTicketIds);
        cuttingTicketService.batchSaveOrUpdate(cuttingTickets);

        // 裁床单工序
        List<PrdCuttingRoute> cuttingRoutes = prdCutting.getCuttingRoutes();
        for (PrdCuttingRoute cuttingRoute : cuttingRoutes) {
            cuttingRoute.setPid(prdCutting.getId());
        }
        cuttingRouteService.removeByPid(prdCutting.getId());
        cuttingRouteService.batchSaveOrUpdate(cuttingRoutes);

        // 裁床单原材料信息
        List<PrdCuttingRaw> cuttingRaws = prdCutting.getCuttingRaws();
        for (PrdCuttingRaw cuttingRaw : cuttingRaws) {
            cuttingRaw.setPid(prdCutting.getId());
        }
        cuttingRawService.removeByPid(prdCutting.getId());
        cuttingRawService.batchSaveOrUpdate(cuttingRaws);
        this.submit(prdCutting.getId());
        prdCutting.setBillStatus(Constants.INT_STATUS_AUDITED);
        this.updateById(prdCutting);

        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<PrdCutting> myPage(Page<PrdCutting> page, CuttingFilterDto cuttingFilterDto) {
        IPage<PrdCutting> prdCuttingIPage = this.baseMapper.myPage(page, cuttingFilterDto);
        List<PrdCutting> records = prdCuttingIPage.getRecords();
        if (!records.isEmpty()) {
            List<String> cuttingIds = records.stream().map(PrdCutting::getId).collect(toList());

            List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByPids(cuttingIds);
            List<String> skuIds = cuttingTickets.stream().map(PrdCuttingTicket::getSkuId).collect(toList());
            List<BdMaterialDetail> bdMaterialDetails = materialDetailService.listDetailByIds(skuIds);
            List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listByCuttingIds(cuttingIds);
            List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPids(cuttingIds);
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listByGpIds(cuttingIds);
            for (PrdCutting record : records) {
                List<PrdCuttingTicketItemReport> cuttingReports = cuttingTicketItemReports.stream().filter(t -> t.getCuttingId().equals(record.getId())).collect(toList());
                int all = 0;
                int finished = 0;
                List<PrdCuttingTicketItem> cuttingTicketItemsFiltered = cuttingTicketItems.stream().filter(t -> t.getGpId().equals(record.getId())).collect(toList());
                List<PrdCuttingRoute> cuttingRoutesFiltered = cuttingRoutes.stream().filter(t -> t.getPid().equals(record.getId())).collect(toList());
                for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItemsFiltered) {
                    for (PrdCuttingRoute cuttingRoute : cuttingRoutesFiltered) {
                        all++;
                        BigDecimal reportedQty = cuttingReports.stream()
                                .filter(t -> t.getPid().equals(cuttingTicketItem.getId())
                                        && t.getCuttingRouteId().equals(cuttingRoute.getId()))
                                .map(PrdCuttingTicketItemReport::getReportedQty)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (reportedQty.compareTo(cuttingTicketItem.getQty()) >= 0) {
                            finished++;
                        }
                    }
                }
                record.setProgressCount(all);
                record.setFinishProgressCount(finished);
                record.setProgress(BigDecimal.valueOf(finished).divide(BigDecimal.valueOf(all), 2, RoundingMode.HALF_UP));

                List<String> collect = cuttingTickets.stream().filter(t -> t.getPid().equals(record.getId())).map(PrdCuttingTicket::getSkuId).collect(toList());
                List<String> mainPicUrls = bdMaterialDetails.stream().filter(t -> collect.contains(t.getId())).map(BdMaterialDetail::getMainPicUrl).collect(toList());
                record.setMainPics(mainPicUrls);
            }
        }
        return prdCuttingIPage;
    }

    @Override
    public void exportPrdCutting(HttpServletResponse response, CuttingFilterDto cuttingFilterDto) {

    }

    @Override
    public JSONObject listSum(CuttingFilterDto cuttingFilterDto) {
        CuttingFilterDto res = baseMapper.listSum(cuttingFilterDto);
        JSONObject result = new JSONObject();
        result.put("productQty", res.getProductQty());
        result.put("finishedQty", res.getFinishedQty());
        result.put("reportedQty", res.getReportedQty());
        return result;
    }

    /**
     * 详情
     */
    @Override
    public PrdCutting detail(String id) {
        PrdCutting byId = this.getById(id);

        BdMaterial material = materialService.getById(byId.getProductId());
        byId.setProductNum(material.getNumber());
        byId.setProductName(material.getName());
        String mainPicId = material.getMainPicId();

        SysFiles mainPic = filesService.getById(mainPicId);
        if (mainPic != null) {
            byId.setMainPicId(mainPicId);
            byId.setMainPic(mainPic.getUrl());
        }

        BdEquipment equipment = equipmentService.getById(byId.getEquipmentId());
        byId.setEquipmentNum(equipment.getNumber());

        BdUnit unit = unitMapper.selectById(byId.getUnitId());

        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listDetailByPid(id);
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            cuttingTicket.setUnitName(unit.getName());
        }
        byId.setCuttingTickets(cuttingTickets);

        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(id);
        byId.setCuttingRoutes(cuttingRoutes);

        List<PrdCuttingRaw> cuttingRaws = cuttingRawService.listByPid(id);
        byId.setCuttingRaws(cuttingRaws);
        return byId;
    }

    /**
     * app工序上数列表接口
     *
     * @param id 裁床单id
     */
    @Override
    public List<ProcedureReportVo> detailForProcedureReport(String id) {
        List<ProcedureReportVo> procedureReportVos = new ArrayList<>();

        PrdCutting cutting = this.getById(id);
        // 工序信息
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(id);
        for (PrdCuttingRoute cuttingRoute : cuttingRoutes) {
            Integer procedureSeq = cuttingRoute.getSeq();
            String procedureId = cuttingRoute.getProcedureId();
            String procedureName = cuttingRoute.getProcedureName();
            String procedureNum = cuttingRoute.getProcedureNum();
            int procedureNo = cuttingRoute.getProcedureNo();

            ProcedureReportVo procedureReportVo = new ProcedureReportVo();
            procedureReportVo.setCuttingRouteId(cuttingRoute.getId());
            procedureReportVo.setProductId(cutting.getProductId());
            procedureReportVo.setProcedureSeq(procedureSeq);
            procedureReportVo.setProcedureId(procedureId);
            procedureReportVo.setProcedureName(procedureName);
            procedureReportVo.setProcedureNo(procedureNo);
            procedureReportVo.setProcedureNum(procedureNum);
            procedureReportVo.setProcedureQty(cuttingRoute.getQty());
            procedureReportVo.setProcedureReportedQty(cuttingRoute.getReportedQty());
            procedureReportVo.setProcedureUnReportQty(cuttingRoute.getQty().subtract(cuttingRoute.getReportedQty()));
            procedureReportVo.setCuttingTickets(new ArrayList<>());
            procedureReportVo.setCuttingTicketItems(new ArrayList<>());
            procedureReportVos.add(procedureReportVo);
        }

        PrdCutting detail = this.detail(id);
        // 所有的工票信息
        List<PrdCuttingTicket> cuttingTickets = detail.getCuttingTickets();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            cuttingTicket.setUnReportedQty(cuttingTicket.getSumQty().subtract(cuttingTicket.getReportedQty()));
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicket.getCuttingTicketItems();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                cuttingTicketItem.setColor(cuttingTicket.getColor());
                cuttingTicketItem.setColorId(cuttingTicket.getColorId());
                cuttingTicketItem.setSpecification(cuttingTicket.getSpecification());
                cuttingTicketItem.setSpecificationId(cuttingTicket.getSpecificationId());
            }
        }
        // 构造工序的具体信息
        for (ProcedureReportVo procedureReportVo : procedureReportVos) {
            for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
                PrdCuttingTicket addCuttingTicket = new PrdCuttingTicket();
                BeanUtils.copyProperties(cuttingTicket, addCuttingTicket);
                addCuttingTicket.setReportedQty(BigDecimal.ZERO);
                List<PrdCuttingTicketItem> cuttingTicketItems = addCuttingTicket.getCuttingTicketItems();
                List<PrdCuttingTicketItem> addCuttingTicketItems = new ArrayList<>();
                for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                    PrdCuttingTicketItem addCuttingTicketItem = new PrdCuttingTicketItem();
                    BeanUtils.copyProperties(cuttingTicketItem, addCuttingTicketItem);

                    List<PrdCuttingTicketItemReport> cuttingTicketItemReports = addCuttingTicketItem.getCuttingTicketItemReports();
                    List<PrdCuttingTicketItemReport> collect = cuttingTicketItemReports.stream()
                            .filter(t -> t.getCuttingRouteId().equals(procedureReportVo.getCuttingRouteId())
                                    && t.getPid().equals(addCuttingTicketItem.getId())
                            )
                            .collect(toList());
                    addCuttingTicketItem.setReportedQty(BigDecimal.ZERO);
                    for (PrdCuttingTicketItemReport prdCuttingTicketItemReport : collect) {
                        addCuttingTicketItem.setReportedQty(addCuttingTicketItem.getReportedQty().add(prdCuttingTicketItemReport.getReportedQty()));
                        addCuttingTicket.setReportedQty(addCuttingTicket.getReportedQty().add(prdCuttingTicketItemReport.getReportedQty()));
                    }

                    addCuttingTicketItems.add(addCuttingTicketItem);
                }
                addCuttingTicket.setUnReportedQty(addCuttingTicket.getSumQty().subtract(addCuttingTicket.getReportedQty()));
                procedureReportVo.getCuttingTickets().add(addCuttingTicket);

                procedureReportVo.getCuttingTicketItems().addAll(addCuttingTicketItems);
            }
        }
        return procedureReportVos;
    }

    /**
     * app工序详情
     *
     * @param routeId 裁床单工序id
     */
    @Override
    public ProcedureReportVo detailCuttingRouteById(String routeId) {

        // 工序信息
        PrdCuttingRoute cuttingRoute = cuttingRouteService.detail(routeId);
        Integer procedureSeq = cuttingRoute.getSeq();
        String procedureId = cuttingRoute.getProcedureId();
        String procedureName = cuttingRoute.getProcedureName();
        String procedureNum = cuttingRoute.getProcedureNum();
        int procedureNo = cuttingRoute.getProcedureNo();

        PrdCutting cutting = this.getById(cuttingRoute.getPid());

        ProcedureReportVo procedureReportVo = new ProcedureReportVo();
        procedureReportVo.setCuttingRouteId(cuttingRoute.getId());
        procedureReportVo.setProductId(cutting.getProductId());
        procedureReportVo.setProcedureSeq(procedureSeq);
        procedureReportVo.setProcedureId(procedureId);
        procedureReportVo.setProcedureName(procedureName);
        procedureReportVo.setProcedureNo(procedureNo);
        procedureReportVo.setProcedureNum(procedureNum);
        procedureReportVo.setProcedureQty(cuttingRoute.getQty());
        procedureReportVo.setProcedureReportedQty(cuttingRoute.getReportedQty());
        procedureReportVo.setProcedureUnReportQty(cuttingRoute.getQty().subtract(cuttingRoute.getReportedQty()));
        procedureReportVo.setCuttingTickets(new ArrayList<>());
        procedureReportVo.setCuttingTicketItems(new ArrayList<>());

        PrdCutting detail = this.detail(cuttingRoute.getPid());
        // 所有的工票信息
        List<PrdCuttingTicket> cuttingTickets = detail.getCuttingTickets();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            cuttingTicket.setUnReportedQty(cuttingTicket.getSumQty().subtract(cuttingTicket.getReportedQty()));
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicket.getCuttingTicketItems();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                cuttingTicketItem.setColor(cuttingTicket.getColor());
                cuttingTicketItem.setColorId(cuttingTicket.getColorId());
                cuttingTicketItem.setSpecification(cuttingTicket.getSpecification());
                cuttingTicketItem.setSpecificationId(cuttingTicket.getSpecificationId());
            }
        }
        // 构造工序的具体信息
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            PrdCuttingTicket addCuttingTicket = new PrdCuttingTicket();
            BeanUtils.copyProperties(cuttingTicket, addCuttingTicket);
            addCuttingTicket.setReportedQty(BigDecimal.ZERO);
            List<PrdCuttingTicketItem> cuttingTicketItems = addCuttingTicket.getCuttingTicketItems();
            List<PrdCuttingTicketItem> addCuttingTicketItems = new ArrayList<>();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                PrdCuttingTicketItem addCuttingTicketItem = new PrdCuttingTicketItem();
                BeanUtils.copyProperties(cuttingTicketItem, addCuttingTicketItem);

                List<PrdCuttingTicketItemReport> cuttingTicketItemReports = addCuttingTicketItem.getCuttingTicketItemReports();
                List<PrdCuttingTicketItemReport> collect = cuttingTicketItemReports.stream()
                        .filter(t -> t.getCuttingRouteId().equals(procedureReportVo.getCuttingRouteId())
                                && t.getPid().equals(addCuttingTicketItem.getId())
                        )
                        .collect(toList());
                addCuttingTicketItem.setReportedQty(BigDecimal.ZERO);
                for (PrdCuttingTicketItemReport prdCuttingTicketItemReport : collect) {
                    addCuttingTicketItem.setReportedQty(addCuttingTicketItem.getReportedQty().add(prdCuttingTicketItemReport.getReportedQty()));
                    addCuttingTicket.setReportedQty(addCuttingTicket.getReportedQty().add(prdCuttingTicketItemReport.getReportedQty()));
                }

                addCuttingTicketItems.add(addCuttingTicketItem);
            }
            addCuttingTicket.setUnReportedQty(addCuttingTicket.getSumQty().subtract(addCuttingTicket.getReportedQty()));
            procedureReportVo.getCuttingTickets().add(addCuttingTicket);

            procedureReportVo.getCuttingTicketItems().addAll(addCuttingTicketItems);
        }
        return procedureReportVo;
    }

    /**
     * app扎号上数列表接口
     *
     * @param id 裁床单id
     */
    @Override
    public List<CuttingTicketItemDetailVo> detailForTicketReport(String id) {
        List<CuttingTicketItemDetailVo> cuttingTicketItemDetailVos = new ArrayList<>();

        PrdCutting detail = this.detail(id);
        List<PrdCuttingTicket> cuttingTickets = detail.getCuttingTickets();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicket.getCuttingTicketItems();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                CuttingTicketItemDetailVo cuttingTicketItemDetailVo = new CuttingTicketItemDetailVo();
                cuttingTicketItemDetailVo.setCuttingId(detail.getId());
                cuttingTicketItemDetailVo.setCuttingNum(detail.getNo());
                cuttingTicketItemDetailVo.setEquipmentId(detail.getEquipmentId());
                cuttingTicketItemDetailVo.setEquipmentNum(detail.getEquipmentNum());
                cuttingTicketItemDetailVo.setCuttingDate(detail.getCuttingDate());
                cuttingTicketItemDetailVo.setDeliveryDate(detail.getDeliveryDate());
                cuttingTicketItemDetailVo.setQty(cuttingTicketItem.getQty());
                cuttingTicketItemDetailVo.setReportedQty(cuttingTicketItem.getReportedQty());
                cuttingTicketItemDetailVo.setInStockQty(cuttingTicketItem.getInStockQty());
                cuttingTicketItemDetailVo.setPackQty(cuttingTicketItem.getPackQty());
                cuttingTicketItemDetailVo.setSaveQty(cuttingTicketItem.getSaveQty());
                cuttingTicketItemDetailVo.setCuttingTicketId(cuttingTicket.getId());
                cuttingTicketItemDetailVo.setCuttingTicketItemId(cuttingTicketItem.getId());
                cuttingTicketItemDetailVo.setTicketNo(cuttingTicketItem.getTicketNo());
                cuttingTicketItemDetailVo.setColorId(cuttingTicket.getColorId());
                cuttingTicketItemDetailVo.setColor(cuttingTicket.getColor());
                cuttingTicketItemDetailVo.setSpecificationId(cuttingTicket.getSpecificationId());
                cuttingTicketItemDetailVo.setSpecification(cuttingTicket.getSpecification());

                cuttingTicketItemDetailVos.add(cuttingTicketItemDetailVo);
            }
        }
        return cuttingTicketItemDetailVos;
    }

    /**
     * 获取编号
     *
     * @return 编号
     */
    @Override
    public String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "CUT" + sdf.format(new Date()) + "-";
        PrdCutting old = this.getOne(Wrappers.lambdaQuery(PrdCutting.class)
                .likeRight(PrdCutting::getNo, no)
                .orderByDesc(PrdCutting::getNo)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doProcedureReport(List<DoProcedureReportDto> doProcedureReportDtos) {
        // 保存日志
        Set<String> procedureIds = doProcedureReportDtos.stream().map(DoProcedureReportDto::getProcedureId).collect(Collectors.toSet());
        List<BdProcedure> bdProcedures = bdProcedureMapper.selectBatchIds(procedureIds);
        List<String> prdCuttingTicketItemIds = doProcedureReportDtos.stream().map(DoProcedureReportDto::getCuttingTicketItemId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listByIds(prdCuttingTicketItemIds);
        List<PrdCuttingTicketItemReport> haveReports = prdCuttingTicketItemReportMapper.listGroupData(prdCuttingTicketItemIds);

        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
        // 未完成报工的进行报工，已完成报工的移除不再报工
        for (int i = doProcedureReportDtos.size() - 1; i >= 0; i--) {
            DoProcedureReportDto doProcedureReportDto = doProcedureReportDtos.get(i);

            BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(doProcedureReportDto.getProcedureId())).findFirst().orElse(null);
            if (StringUtils.isNull(bdProcedure)) {
                throw new BizException("工序不存在！");
            }
            for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
                if(prdCuttingTicketItem.getId().equals(doProcedureReportDto.getCuttingTicketItemId())){
                    if (!prdCuttingTicketItem.getFinishReport()) {
                        // 验证工序是否已完成数量
                        List<PrdCuttingTicketItemReport> filters = haveReports.stream().filter(r -> StringUtils.equals(r.getPid(), prdCuttingTicketItem.getId()) && StringUtils.equals(r.getProcedureId(), bdProcedure.getId())).collect(toList());
                        Boolean tf = false;
                        if (filters == null || filters.size() == 0) {
                            tf = true;
                        } else {
                            BigDecimal sumReportQty = filters.stream().map(PrdCuttingTicketItemReport::getReportingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (prdCuttingTicketItem.getQty().compareTo(sumReportQty) == 1) {
                                tf = true;
                            }
                        }

                        if (tf) {
                            remark.append("工序(");
                            remark.append(bdProcedure.getName());
                            remark.append(")上数 票号：");
                            remark.append(prdCuttingTicketItem.getTicketNo());
                            remark.append(",");
                            remark.append("数量：");
                            remark.append(doProcedureReportDto.getReportingQty());
                            remark.append("；");
                        } else {
                            doProcedureReportDtos.remove(i);
                        }
                    } else {
                        doProcedureReportDtos.remove(i);
                    }

                    break;
                }
            }
        }
//        for (DoProcedureReportDto doProcedureReportDto : doProcedureReportDtos) {
//            BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(doProcedureReportDto.getProcedureId())).findFirst().orElse(null);
//            if (StringUtils.isNull(bdProcedure)) {
//                throw new BizException("工序不存在！");
//            }
//            for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
//                if(prdCuttingTicketItem.getId().equals(doProcedureReportDto.getCuttingTicketItemId())){
//                    remark.append("工序(");
//                    remark.append(bdProcedure.getName());
//                    remark.append(")上数 票号：");
//                    remark.append(prdCuttingTicketItem.getTicketNo());
//                    remark.append(",");
//                    remark.append("数量：");
//                    remark.append(doProcedureReportDto.getReportingQty());
//                    remark.append("；");
//                }
//            }
//        }
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(new Date());
        String cuttingId = doProcedureReportDtos.get(0).getCuttingId();

        // 裁床单所有工序
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingId);

        String cuttingRouteId = doProcedureReportDtos.get(0).getCuttingRouteId(); // 因为汇报的时候是同一工序，所以先查出工序，提高效率
        PrdCuttingRoute cuttingRoute = cuttingRoutes.stream()
                .filter(t -> t.getId().equals(cuttingRouteId))
                .findFirst()
                .orElse(null);
        if (null == cuttingRoute) {
            throw new BizException("裁床单工票号的工序信息不存在！");
        }

        // 末道工序的数量
        int lastRouteCount = (int) cuttingRoutes.stream().filter(t -> t.getProcedureNo().equals(cuttingRoute.getProcedureNo())).count();
        // 是否是末道工序
        boolean lastRoute = cuttingRoute.getProcedureNo().equals(cuttingRoutes.get(cuttingRoutes.size() - 1).getProcedureNo());

        List<PrdCuttingTicketItemReport> addCuttingTicketItemReports = new ArrayList<>();
        for (DoProcedureReportDto doProcedureReportDto : doProcedureReportDtos) {
            String cuttingTicketItemId = doProcedureReportDto.getCuttingTicketItemId();
            BigDecimal reportingQty = doProcedureReportDto.getReportingQty();

            // 工票汇报
            PrdCuttingTicketItemReport prdCuttingTicketItemReport = new PrdCuttingTicketItemReport();
            prdCuttingTicketItemReport.setCuttingRouteId(cuttingRouteId);
            prdCuttingTicketItemReport.setProcedureNo(cuttingRoute.getProcedureNo());
            prdCuttingTicketItemReport.setProcedureId(cuttingRoute.getProcedureId());
            // 找出汇报人是否有特殊工价
            StaffSpecialRouteDto staffSpecialRouteInfo = materialRouteService.getStaffSpecialRouteInfo(doProcedureReportDto.getProductId(), doProcedureReportDto.getProcedureId(), doProcedureReportDto.getReporterId());
            if (StringUtils.isNotNull(staffSpecialRouteInfo)) {
                prdCuttingTicketItemReport.setPrice(staffSpecialRouteInfo.getSpecialPrice());
            } else {
                prdCuttingTicketItemReport.setPrice(cuttingRoute.getPrice());
            }
            prdCuttingTicketItemReport.setReportedQty(reportingQty);
            prdCuttingTicketItemReport.setRealReportedQty(reportingQty);
            prdCuttingTicketItemReport.setReporterId(doProcedureReportDto.getReporterId());
            prdCuttingTicketItemReport.setReportTime(nowStr);
            prdCuttingTicketItemReport.setPid(cuttingTicketItemId);
            addCuttingTicketItemReports.add(prdCuttingTicketItemReport);
            cuttingRoute.setReportedQty(cuttingRoute.getReportedQty().add(reportingQty));
        }

        if (addCuttingTicketItemReports.size() > 0) {
            // 末道工序，需要反写汇报数量到票据详情，票据信息，裁床单
            if (lastRoute) {
                // 裁床单
                PrdCutting cutting = this.getById(cuttingId);
                // 裁床单所有票据（之所以查所有是用于汇总数量给裁床单）
                List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByPid(cuttingId);
                // 裁床单所有票据详情（之所以查所有是用于汇总数量给票据信息）
                List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listByGpId(cuttingId);
                // 所有涉及到的扎号的汇报数据（用于更新票据详情的汇报数量）
                List<String> cuttingTicketItemIds = doProcedureReportDtos.stream().map(DoProcedureReportDto::getCuttingTicketItemId).collect(toList());
                List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPids(cuttingTicketItemIds);
                // 过滤出末道工序的汇报
                cuttingTicketItemReports = cuttingTicketItemReports.stream()
                        .filter(t -> t.getProcedureNo() == cuttingRoute.getProcedureNo())
                        .collect(toList());
                // 加上本次汇报的末道工序
                cuttingTicketItemReports.addAll(addCuttingTicketItemReports);
                // 按票据号分组
                Map<String, List<PrdCuttingTicketItemReport>> cuttingTicketItemReportMap = cuttingTicketItemReports.stream().collect(Collectors.groupingBy(PrdCuttingTicketItemReport::getPid));
                // 需要更新的票据详情
                List<PrdCuttingTicketItem> needUpdateCuttingTicketItems = new ArrayList<>();
                // 循环依次处理
                for (String cuttingTicketItemId : cuttingTicketItemReportMap.keySet()) {
                    // 取出票据号所有的末道工序汇总数据
                    List<PrdCuttingTicketItemReport> reports = cuttingTicketItemReportMap.get(cuttingTicketItemId);
                    // 按照工序进行分组求和
                    Map<String, BigDecimal> collect = reports.stream().collect(Collectors.groupingBy(PrdCuttingTicketItemReport::getCuttingRouteId,
                            Collectors.reducing(BigDecimal.ZERO, PrdCuttingTicketItemReport::getReportedQty, BigDecimal::add))
                    );
                    // 汇报的工序数量和末道工序的数量不一致，则说明无完成数量（平行工序）
                    if (collect.keySet().size() != lastRouteCount) {
                        continue;
                    }
                    // 取出最小的数量
                    boolean first = true;
                    BigDecimal minQty = BigDecimal.ZERO;
                    for (String routeId : collect.keySet()) {
                        BigDecimal bigDecimal = collect.get(routeId);
                        if (first) {
                            first = false;
                            minQty = bigDecimal;
                        } else {
                            minQty = bigDecimal.compareTo(minQty) > 0 ? minQty : bigDecimal;
                        }
                    }
                    PrdCuttingTicketItem cuttingTicketItem = cuttingTicketItems.stream().filter(t -> t.getId().equals(cuttingTicketItemId)).findFirst().orElse(null);
                    if (null == cuttingTicketItem) {
                        throw new BizException("票据详情不存在，请核实！");
                    }
                    cuttingTicketItem.setReportedQty(minQty);
                    needUpdateCuttingTicketItems.add(cuttingTicketItem);
                }
                // 有需要更新的票据详情，则说明票据信息和裁床单也需要更新
                if (!needUpdateCuttingTicketItems.isEmpty()) {
                    // 过滤出需要更新的票据信息
                    List<String> needUpdateCuttingTicketIds = needUpdateCuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).distinct().collect(toList());
                    List<PrdCuttingTicket> needUpdateCuttingTickets = cuttingTickets.stream().filter(t -> needUpdateCuttingTicketIds.contains(t.getId())).collect(toList());
                    for (PrdCuttingTicket cuttingTicket : needUpdateCuttingTickets) {
                        // 找出该票据信息的所有票据并且求和赋值给票据信息
                        List<PrdCuttingTicketItem> collect = cuttingTicketItems.stream().filter(t -> t.getPid().equals(cuttingTicket.getId())).collect(toList());
                        BigDecimal cuttingTicketReportedQty = collect.stream().map(PrdCuttingTicketItem::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        cuttingTicket.setReportedQty(cuttingTicketReportedQty);
                    }
                    // 计算裁床单所有的已汇报数量
                    BigDecimal cuttingReportedQty = cuttingTickets.stream().map(PrdCuttingTicket::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    cutting.setReportedQty(cuttingReportedQty);
                    Optional<PrdCuttingTicketItem> any = cuttingTicketItems.stream().filter(t -> t.getReportedQty().compareTo(BigDecimal.ZERO) <= 0).findAny();
                    cutting.setReportedStatus(!any.isPresent());
                    this.updateById(cutting);
                    cuttingTicketService.updateBatchById(needUpdateCuttingTickets);
                    cuttingTicketItemService.updateBatchById(needUpdateCuttingTicketItems);
                }
            }

            cuttingRouteService.updateById(cuttingRoute);
            return cuttingTicketItemReportService.saveBatch(addCuttingTicketItemReports);
        } else {
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doBatchProcedureReport(List<DoProcedureReportDto> doProcedureReportDtos) {
        List<String> collect = doProcedureReportDtos.stream().map(DoProcedureReportDto::getCuttingRouteId).distinct().collect(toList());
        for (String cuttingRouteId : collect) {
            List<DoProcedureReportDto> dtos = doProcedureReportDtos.stream().filter(t -> t.getCuttingRouteId().equals(cuttingRouteId)).collect(toList());
            this.doProcedureReport(dtos);
        }
        return true;
    }

    /**
     * app撤数接口
     *
     * @param cuttingTicketItemReportIds 需要撤数的汇报id
     * @return 是否撤数成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelReport(List<String> cuttingTicketItemReportIds) {
        // 需要撤数的数据
        List<PrdCuttingTicketItemReport> reports = cuttingTicketItemReportService.listByIds(cuttingTicketItemReportIds);
        if (reports.size() != cuttingTicketItemReportIds.size()) {
            throw new BizException("上数信息不存在！");
        }
        // 需要撤数的数据涉及的工序
        List<String> cuttingRouteIds = reports.stream().map(PrdCuttingTicketItemReport::getCuttingRouteId).distinct().collect(toList());
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByIds(cuttingRouteIds);
        if (cuttingRoutes.size() != cuttingRouteIds.size()) {
            throw new BizException("裁床单工票号的工序信息不存在！");
        }
        // 裁床单
        String cuttingId = cuttingRoutes.get(0).getPid();
        PrdCutting cutting = this.getById(cuttingId);
        // 裁床单-工票信息明细
        List<String> cuttingTicketItemIds = reports.stream().map(PrdCuttingTicketItemReport::getPid).collect(toList());
        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listByIds(cuttingTicketItemIds);
        // 裁床单-工票信息
        List<String> cuttingTicketIds = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(toList());
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByIds(cuttingTicketIds);
        // 裁床单所有工序
        List<PrdCuttingRoute> allCuttingRoutes = cuttingRouteService.listByPid(cuttingId);

        Map<String, BigDecimal> needSubRouteData = new HashMap<>(); // 本次撤数的扎号-工序的数量

        for (PrdCuttingTicketItemReport cuttingTicketItemReport : reports) {
            // 需要回退的数量
            BigDecimal reportedQty = cuttingTicketItemReport.getReportedQty();
            // 裁床单工序
            String cuttingRouteId = cuttingTicketItemReport.getCuttingRouteId();
            PrdCuttingRoute cuttingRoute = cuttingRoutes.stream().filter(t -> t.getId().equals(cuttingRouteId)).findFirst().orElse(new PrdCuttingRoute());
            cuttingRoute.setReportedQty(cuttingRoute.getReportedQty().subtract(reportedQty));

            boolean lastRoute = cuttingRoute.getProcedureNo().equals(allCuttingRoutes.get(allCuttingRoutes.size() - 1).getProcedureNo());
            if (lastRoute) {
                String cuttingTicketItemId = cuttingTicketItemReport.getPid();

                String key = cuttingTicketItemId + "-" + cuttingRouteId;
                if (needSubRouteData.containsKey(key)) {
                    needSubRouteData.put(key, reportedQty.add(needSubRouteData.get(key)));
                } else {
                    needSubRouteData.put(key, reportedQty);
                }
            }
        }

        if (!needSubRouteData.keySet().isEmpty()) {
            List<PrdCutting> needUpdateCuttings = new ArrayList<>();
            List<PrdCuttingTicket> needUpdateCuttingTickets = new ArrayList<>();
            List<PrdCuttingTicketItem> needUpdateCuttingTicketItems = new ArrayList<>();

            // 需要撤数的数据关联的所有汇报数据
            List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPids(cuttingTicketItemIds);
            for (String key : needSubRouteData.keySet()) {
                BigDecimal subQty = needSubRouteData.get(key);
                String[] split = key.split("-");
                String cuttingTicketItemId = split[0];
                String cuttingRouteId = split[1];
                // 该扎号该工序的汇报记录
                List<PrdCuttingTicketItemReport> allReports = cuttingTicketItemReports.stream()
                        .filter(t -> t.getCuttingRouteId().equals(cuttingRouteId)
                                && t.getPid().equals(cuttingTicketItemId)
                        )
                        .collect(toList());
                BigDecimal allReportedQty = allReports.stream().map(PrdCuttingTicketItemReport::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal remainedQty = allReportedQty.subtract(subQty); // 剩余汇报数量

                // 工票详情
                PrdCuttingTicketItem cuttingTicketItem = cuttingTicketItems.stream().filter(t -> t.getId().equals(cuttingTicketItemId)).findFirst().orElse(null);
                if (null == cuttingTicketItem) {
                    throw new BizException("裁床单工票号信息不存在！");
                }
                // 剩余汇报数量小于之前反写的数量，则说明需要扣除相应的值
                BigDecimal realQty = cuttingTicketItem.getReportedQty().subtract(remainedQty);
                if (realQty.compareTo(BigDecimal.ZERO) > 0) {
                    cuttingTicketItem.setReportedQty(cuttingTicketItem.getReportedQty().subtract(realQty));
                    cuttingTicketItem.setFinishReport(cuttingTicketItem.getReportedQty().compareTo(cuttingTicketItem.getQty()) >= 0);
                    if (!needUpdateCuttingTicketItems.contains(cuttingTicketItem)) {
                        needUpdateCuttingTicketItems.add(cuttingTicketItem);
                    }

                    // 工票信息
                    PrdCuttingTicket cuttingTicket = cuttingTickets.stream().filter(t -> t.getId().equals(cuttingTicketItem.getPid())).findFirst().orElse(null);
                    if (null == cuttingTicket) {
                        throw new BizException("裁床单工票信息不存在！");
                    }
                    cuttingTicket.setReportedQty(cuttingTicket.getReportedQty().subtract(realQty));
                    if (!needUpdateCuttingTickets.contains(cuttingTicket)) {
                        needUpdateCuttingTickets.add(cuttingTicket);
                    }

                    // 裁床单
                    cutting.setReportedQty(cutting.getReportedQty().subtract(realQty));
                    if (!needUpdateCuttings.contains(cutting)) {
                        needUpdateCuttings.add(cutting);
                    }
                }
            }

            if (!needUpdateCuttings.isEmpty()) {
                PrdCutting prdCutting = needUpdateCuttings.get(0);
                prdCutting.setReportedStatus(false);
                this.updateById(prdCutting);
            }
            if (!needUpdateCuttingTickets.isEmpty()) {
                cuttingTicketService.updateBatchById(needUpdateCuttingTickets);
            }
            if (!needUpdateCuttingTicketItems.isEmpty()) {
                cuttingTicketItemService.updateBatchById(needUpdateCuttingTicketItems);
            }
        }

        cuttingRouteService.updateBatchById(cuttingRoutes);

        // 保存日志
        Set<String> procedureIds = cuttingRoutes.stream().map(PrdCuttingRoute::getProcedureId).collect(Collectors.toSet());
        List<BdProcedure> bdProcedures = bdProcedureMapper.selectBatchIds(procedureIds);
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        StringBuilder remark = new StringBuilder();
        for (PrdCuttingTicketItemReport report : reports) {
            BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(report.getProcedureId())).findFirst().orElse(null);
            if (StringUtils.isNull(bdProcedure)) {
                throw new BizException("工序不存在！");
            }
            List<PrdCuttingTicketItem> collect = cuttingTicketItems.stream().filter(t -> t.getId().equals(report.getPid())).collect(toList());
            for (PrdCuttingTicketItem prdCuttingTicketItem : collect) {
                remark.append("工序(");
                remark.append(bdProcedure.getName());
                remark.append(")撤数 票号：");
                remark.append(prdCuttingTicketItem.getTicketNo());
                remark.append(",");
                remark.append("数量：");
                remark.append(report.getReportingQty());
                remark.append("；");
            }
        }
        prdTicketLog.setRemark(remark.toString());
        prdTicketLogService.save(prdTicketLog);

        return cuttingTicketItemReportService.removeByIds(cuttingTicketItemReportIds);
    }

    /**
     * app工序重新上数接口
     *
     * @param cuttingTicketItemReport 修改后的汇报数据
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReport(PrdCuttingTicketItemReport cuttingTicketItemReport) {
        // 取出修改之前的数据
        PrdCuttingTicketItemReport old = cuttingTicketItemReportService.getById(cuttingTicketItemReport.getId());
        BigDecimal reportedQty = old.getReportedQty();
        BigDecimal reportingQty = cuttingTicketItemReport.getReportingQty();
        BigDecimal diff = reportingQty.subtract(reportedQty);
        if (diff.compareTo(BigDecimal.ZERO) != 0) {
            // 裁床单工序
            String cuttingRouteId = old.getCuttingRouteId();
            PrdCuttingRoute cuttingRoute = cuttingRouteService.getById(cuttingRouteId);
            if (null == cuttingRoute) {
                throw new BizException("裁床单工票号的工序信息不存在！");
            }
            cuttingRoute.setReportedQty(cuttingRoute.getReportedQty().add(diff));
            cuttingRouteService.updateById(cuttingRoute);
            String cuttingId = cuttingRoute.getPid();
            List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingId);
            boolean lastRoute = cuttingRoute.getProcedureNo().equals(cuttingRoutes.get(cuttingRoutes.size() - 1).getProcedureNo());

            if (lastRoute) {
                // 该扎号所有的汇报数据
                List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPid(cuttingTicketItemReport.getPid());

                // 工票详情
                PrdCuttingTicketItem cuttingTicketItem = cuttingTicketItemService.getById(old.getPid());
                if (null == cuttingTicketItem) {
                    throw new BizException("裁床单工票号信息不存在！");
                }

                if (diff.compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, BigDecimal> collect = cuttingTicketItemReports.stream()
                            .filter(t -> t.getProcedureNo() == cuttingRoute.getProcedureNo())
                            .collect(Collectors.groupingBy(PrdCuttingTicketItemReport::getCuttingRouteId,
                                    Collectors.reducing(BigDecimal.ZERO, PrdCuttingTicketItemReport::getReportedQty, BigDecimal::add)));
                    BigDecimal minQty = collect.get(cuttingRouteId).add(diff); // 该工序增加数量后的总数
                    for (String key : collect.keySet()) {
                        if (key.equals(cuttingRouteId)) {
                            continue;
                        }
                        BigDecimal bigDecimal = collect.get(key);
                        if (bigDecimal.compareTo(minQty) < 0) {
                            minQty = bigDecimal;
                        }
                    }
                    // 计算本次需要增加的数量
                    BigDecimal realQty = minQty.subtract(cuttingTicketItem.getReportedQty());
                    // 和历史反写数量比较，如果比历史数量大，则需要处理
                    if (realQty.compareTo(BigDecimal.ZERO) > 0) {
                        cuttingTicketItem.setReportedQty(cuttingTicketItem.getReportedQty().add(realQty));
                        cuttingTicketItem.setFinishReport(cuttingTicketItem.getReportedQty().compareTo(cuttingTicketItem.getQty()) >= 0);
                        cuttingTicketItemService.updateById(cuttingTicketItem);

                        // 裁床单-工票信息
                        PrdCuttingTicket cuttingTicket = cuttingTicketService.getById(cuttingTicketItem.getPid());
                        if (null == cuttingTicket) {
                            throw new BizException("裁床单工票信息不存在！");
                        }
                        cuttingTicket.setReportedQty(cuttingTicket.getReportedQty().add(realQty));
                        cuttingTicketService.updateById(cuttingTicket);

                        // 裁床单
                        PrdCutting cutting = this.getById(cuttingId);
                        cutting.setReportedQty(cutting.getReportedQty().add(realQty));
                        this.updateById(cutting);
                    }
                } else {
                    // 该扎号该工序的汇报记录
                    List<PrdCuttingTicketItemReport> allReports = cuttingTicketItemReports.stream()
                            .filter(t -> t.getCuttingRouteId().equals(cuttingRouteId))
                            .collect(toList());
                    BigDecimal allReportedQty = allReports.stream().map(PrdCuttingTicketItemReport::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal remainedQty = allReportedQty.add(diff); // 剩余汇报数量
                    // 剩余汇报数量小于之前反写的数量，则说明需要扣除相应的值
                    BigDecimal realQty = cuttingTicketItem.getReportedQty().subtract(remainedQty);
                    if (realQty.compareTo(BigDecimal.ZERO) > 0) {
                        cuttingTicketItem.setReportedQty(cuttingTicketItem.getReportedQty().subtract(realQty));
                        cuttingTicketItem.setFinishReport(cuttingTicketItem.getReportedQty().compareTo(cuttingTicketItem.getQty()) >= 0);
                        cuttingTicketItemService.updateById(cuttingTicketItem);

                        // 裁床单-工票信息
                        PrdCuttingTicket cuttingTicket = cuttingTicketService.getById(cuttingTicketItem.getPid());
                        if (null == cuttingTicket) {
                            throw new BizException("裁床单工票信息不存在！");
                        }
                        cuttingTicket.setReportedQty(cuttingTicket.getReportedQty().subtract(realQty));
                        cuttingTicketService.updateById(cuttingTicket);

                        // 裁床单
                        PrdCutting cutting = this.getById(cuttingId);
                        cutting.setReportedQty(cutting.getReportedQty().subtract(realQty));
                        this.updateById(cutting);
                    }
                }
            }
        }

        old.setReportedQty(reportingQty);
        old.setRealReportedQty(reportingQty);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date now = new Date();
//        String nowStr = sdf.format(now);
//        old.setReportTime(nowStr);
        old.setRemark(cuttingTicketItemReport.getRemark());
        return cuttingTicketItemReportService.updateById(old);
    }

    /**
     * 根据扎号id获取扎号详情列表
     *
     * @param ticketItemId 扎号id
     * @return 返回的扎号详情列表
     */
    @Override
    public CuttingTicketItemDetailVo getTicketDetailByTicketItemId(String ticketItemId) {
        PrdCuttingTicketItem cuttingTicketItem = cuttingTicketItemService.detail(ticketItemId);
        if (null == cuttingTicketItem) {
            throw new BizException("票据信息详情不存在");
        }
        PrdCuttingTicket cuttingTicket = cuttingTicketService.getById(cuttingTicketItem.getPid());

        PrdCutting detail = this.detail(cuttingTicket.getPid());

        BdMaterialDetail bdMaterialDetail = materialDetailService.getDetailByIds(cuttingTicket.getSkuId());

        CuttingTicketItemDetailVo cuttingTicketItemDetailVo = new CuttingTicketItemDetailVo();
        cuttingTicketItemDetailVo.setCuttingId(detail.getId());
        cuttingTicketItemDetailVo.setCuttingNum(detail.getNo());
        cuttingTicketItemDetailVo.setEquipmentId(detail.getEquipmentId());
        cuttingTicketItemDetailVo.setEquipmentNum(detail.getEquipmentNum());
        cuttingTicketItemDetailVo.setCuttingDate(detail.getCuttingDate());
        cuttingTicketItemDetailVo.setDeliveryDate(detail.getDeliveryDate());
        cuttingTicketItemDetailVo.setProductId(detail.getProductId());
        cuttingTicketItemDetailVo.setSkuId(cuttingTicket.getSkuId());
        cuttingTicketItemDetailVo.setProductName(detail.getProductName());
        cuttingTicketItemDetailVo.setProductNum(detail.getProductNum());
        cuttingTicketItemDetailVo.setProductQty(detail.getProductQty());
        cuttingTicketItemDetailVo.setMainPicId(bdMaterialDetail.getMainPicId());
        cuttingTicketItemDetailVo.setMainPic(bdMaterialDetail.getMainPicUrl());
        cuttingTicketItemDetailVo.setQty(cuttingTicketItem.getQty());
        cuttingTicketItemDetailVo.setReportedQty(cuttingTicketItem.getReportedQty());
        cuttingTicketItemDetailVo.setInStockQty(cuttingTicketItem.getInStockQty());
        cuttingTicketItemDetailVo.setPackQty(cuttingTicketItem.getPackQty());
        cuttingTicketItemDetailVo.setSaveQty(cuttingTicketItem.getSaveQty());
        cuttingTicketItemDetailVo.setCuttingTicketId(cuttingTicket.getId());
        cuttingTicketItemDetailVo.setCuttingTicketItemId(cuttingTicketItem.getId());
        cuttingTicketItemDetailVo.setTicketNo(cuttingTicketItem.getTicketNo());
        cuttingTicketItemDetailVo.setColorId(cuttingTicket.getColorId());
        cuttingTicketItemDetailVo.setColor(cuttingTicket.getColor());
        cuttingTicketItemDetailVo.setSpecificationId(cuttingTicket.getSpecificationId());
        cuttingTicketItemDetailVo.setSpecification(cuttingTicket.getSpecification());

        List<PrdCuttingTicketItemReport> reports = cuttingTicketItem.getCuttingTicketItemReports();
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingTicketItem.getGpId());
        for (PrdCuttingRoute cuttingRoute : cuttingRoutes) {
            List<PrdCuttingTicketItemReport> cuttingTicketItemReports = reports.stream().filter(t -> t.getCuttingRouteId().equals(cuttingRoute.getId())).collect(toList());
            BigDecimal allReportedQty = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            cuttingRoute.setReportedQty(allReportedQty);
            cuttingRoute.setCuttingTicketItemReports(cuttingTicketItemReports);
        }

        cuttingTicketItemDetailVo.setCuttingRoutes(cuttingRoutes);
        return cuttingTicketItemDetailVo;
    }

    /**
     * 根据扎号获取扎号详情列表
     *
     * @param ticketNo 扎号
     * @return 返回的扎号详情列表
     */
    @Override
    public CuttingTicketItemDetailVo getTicketDetailByTicketNo(String ticketNo) {
        PrdCuttingTicketItem cuttingTicketItem = cuttingTicketItemService.detailByTicketNo(ticketNo);
        if (null == cuttingTicketItem) {
            throw new BizException("票据信息详情不存在");
        }
        PrdCuttingTicket cuttingTicket = cuttingTicketService.getById(cuttingTicketItem.getPid());

        PrdCutting detail = this.detail(cuttingTicket.getPid());

        BdMaterialDetail bdMaterialDetail = materialDetailService.getDetailByIds(cuttingTicket.getSkuId());

        CuttingTicketItemDetailVo cuttingTicketItemDetailVo = new CuttingTicketItemDetailVo();
        cuttingTicketItemDetailVo.setCuttingId(detail.getId());
        cuttingTicketItemDetailVo.setCuttingNum(detail.getNo());
        cuttingTicketItemDetailVo.setEquipmentId(detail.getEquipmentId());
        cuttingTicketItemDetailVo.setEquipmentNum(detail.getEquipmentNum());
        cuttingTicketItemDetailVo.setCuttingDate(detail.getCuttingDate());
        cuttingTicketItemDetailVo.setDeliveryDate(detail.getDeliveryDate());
        cuttingTicketItemDetailVo.setProductId(detail.getProductId());
        cuttingTicketItemDetailVo.setSkuId(cuttingTicket.getSkuId());
        cuttingTicketItemDetailVo.setProductName(detail.getProductName());
        cuttingTicketItemDetailVo.setProductNum(detail.getProductNum());
        cuttingTicketItemDetailVo.setProductQty(detail.getProductQty());
        cuttingTicketItemDetailVo.setMainPicId(bdMaterialDetail.getMainPicId());
        cuttingTicketItemDetailVo.setMainPic(bdMaterialDetail.getMainPicUrl());
        cuttingTicketItemDetailVo.setQty(cuttingTicketItem.getQty());
        cuttingTicketItemDetailVo.setReportedQty(cuttingTicketItem.getReportedQty());
        cuttingTicketItemDetailVo.setInStockQty(cuttingTicketItem.getInStockQty());
        cuttingTicketItemDetailVo.setPackQty(cuttingTicketItem.getPackQty());
        cuttingTicketItemDetailVo.setSaveQty(cuttingTicketItem.getSaveQty());
        cuttingTicketItemDetailVo.setCuttingTicketId(cuttingTicket.getId());
        cuttingTicketItemDetailVo.setCuttingTicketItemId(cuttingTicketItem.getId());
        cuttingTicketItemDetailVo.setTicketNo(cuttingTicketItem.getTicketNo());
        cuttingTicketItemDetailVo.setColorId(cuttingTicket.getColorId());
        cuttingTicketItemDetailVo.setColor(cuttingTicket.getColor());
        cuttingTicketItemDetailVo.setSpecificationId(cuttingTicket.getSpecificationId());
        cuttingTicketItemDetailVo.setSpecification(cuttingTicket.getSpecification());

        List<PrdCuttingTicketItemReport> reports = cuttingTicketItem.getCuttingTicketItemReports();
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingTicketItem.getGpId());
        for (PrdCuttingRoute cuttingRoute : cuttingRoutes) {
            List<PrdCuttingTicketItemReport> cuttingTicketItemReports = reports.stream().filter(t -> t.getCuttingRouteId().equals(cuttingRoute.getId())).collect(toList());
            BigDecimal allReportedQty = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            cuttingRoute.setReportedQty(allReportedQty);
            cuttingRoute.setCuttingTicketItemReports(cuttingTicketItemReports);
        }

        cuttingTicketItemDetailVo.setCuttingRoutes(cuttingRoutes);
        return cuttingTicketItemDetailVo;
    }

    /**
     * 根据cuttingId查找详细信息
     *
     * @param cuttingId 裁床单id
     * @return 裁床单票据详情的详细信息的集合
     */
    @Override
    public List<CuttingTicketItemDetailVo> listTicketItemDetailByCuttingId(String cuttingId) {
        PrdCutting cutting = this.detail(cuttingId);
        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listByGpId(cuttingId);

        List<String> cuttingTicketIds = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(toList());
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.listByIds(cuttingTicketIds);

        List<String> skuIds = cuttingTickets.stream().map(PrdCuttingTicket::getSkuId).collect(toList());
        List<BdMaterialDetail> bdMaterialDetails = materialDetailService.listDetailByIds(skuIds);
        List<CuttingTicketItemDetailVo> cuttingTicketItemDetailVos = new ArrayList<>();
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            PrdCuttingTicket prdCuttingTicket = cuttingTickets.stream().filter(t -> t.getId().equals(cuttingTicketItem.getPid())).findFirst().orElse(null);
            assert prdCuttingTicket != null;
            CuttingTicketItemDetailVo cuttingTicketItemDetailVo = new CuttingTicketItemDetailVo();
            cuttingTicketItemDetailVo.setCuttingId(cuttingTicketItem.getGpId());
            cuttingTicketItemDetailVo.setCuttingNum(cutting.getNo());
            cuttingTicketItemDetailVo.setSrcNo(cutting.getSrcNo());
            cuttingTicketItemDetailVo.setEquipmentId(cutting.getEquipmentId());
            cuttingTicketItemDetailVo.setEquipmentNum(cutting.getEquipmentNum());
            cuttingTicketItemDetailVo.setProductId(cutting.getProductId());
            cuttingTicketItemDetailVo.setProductNum(cutting.getProductNum());
            cuttingTicketItemDetailVo.setProductName(cutting.getProductName());
            cuttingTicketItemDetailVo.setProductQty(cutting.getProductQty());
            BdMaterialDetail bdMaterialDetail = bdMaterialDetails.stream().filter(t -> prdCuttingTicket.getSkuId().equals(t.getId())).findFirst().orElse(null);
            if (null == bdMaterialDetail) {
                throw new BizException("物料详情不存在！");
            }
            cuttingTicketItemDetailVo.setMainPic(bdMaterialDetail.getMainPicUrl());
            cuttingTicketItemDetailVo.setDeliveryDate(cutting.getDeliveryDate());
            cuttingTicketItemDetailVo.setCuttingDate(cutting.getCuttingDate());
            cuttingTicketItemDetailVo.setRemark(cutting.getRemark());
            cuttingTicketItemDetailVo.setCuttingTicketId(cuttingTicketItem.getPid());
            cuttingTicketItemDetailVo.setCuttingTicketItemId(cuttingTicketItem.getId());
            cuttingTicketItemDetailVo.setTicketNo(cuttingTicketItem.getTicketNo());
            cuttingTicketItemDetailVo.setColorId(prdCuttingTicket.getColorId());
            cuttingTicketItemDetailVo.setColor(prdCuttingTicket.getColor());
            cuttingTicketItemDetailVo.setSpecificationId(prdCuttingTicket.getSpecificationId());
            cuttingTicketItemDetailVo.setSpecification(prdCuttingTicket.getSpecification());
            cuttingTicketItemDetailVo.setQty(cuttingTicketItem.getQty());
            cuttingTicketItemDetailVo.setReportedQty(cuttingTicketItem.getReportedQty());
            cuttingTicketItemDetailVo.setInStockQty(cuttingTicketItem.getInStockQty());
            cuttingTicketItemDetailVo.setPackQty(cuttingTicketItem.getPackQty());
            cuttingTicketItemDetailVo.setSaveQty(cuttingTicketItem.getSaveQty());

            cuttingTicketItemDetailVos.add(cuttingTicketItemDetailVo);
        }
        return cuttingTicketItemDetailVos;
    }

    /**
     * 根据工序条码获取工序详情
     *
     * @param barCode 条码
     * @return 返回的工序详情
     */
    @Override
    public CuttingRouteDetailVo getRouteDetailByBarCode(String barCode) {
        // todo 条码暂时当初裁床单工序id
        PrdCuttingRoute cuttingRoute = cuttingRouteService.detail(barCode);
        PrdCutting cutting = this.detail(cuttingRoute.getPid());
        CuttingRouteDetailVo cuttingRouteDetailVo = new CuttingRouteDetailVo();
        cuttingRouteDetailVo.setCuttingId(cutting.getId());
        cuttingRouteDetailVo.setCuttingNum(cutting.getNo());
        cuttingRouteDetailVo.setEquipmentId(cutting.getEquipmentId());
        cuttingRouteDetailVo.setEquipmentNum(cutting.getEquipmentNum());
        cuttingRouteDetailVo.setCuttingRouteId(cuttingRoute.getId());
        cuttingRouteDetailVo.setProcedureId(cuttingRoute.getProcedureId());
        cuttingRouteDetailVo.setProcedureNo(cuttingRoute.getProcedureNo());
        cuttingRouteDetailVo.setProcedureName(cuttingRoute.getProcedureName());
        cuttingRouteDetailVo.setProcedureNum(cuttingRoute.getProcedureNum());
        return cuttingRouteDetailVo;
    }

    /**
     * 本月上数数据
     *
     * @param page             分页数据
     * @param cuttingFilterDto 过滤条件
     * @return 返回的数据
     */
    @Override
    public IPage<CurMonthReportVo> pageCurMonthReport(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        return this.baseMapper.pageCurMonthReport(page, cuttingFilterDto);
    }

    /**
     * 本单上数数据
     *
     * @param cuttingFilterDto 过滤条件
     * @return 返回的数据
     */
    @Override
    public List<CurMonthReportVo> listReportByCuttingId(CuttingFilterDto cuttingFilterDto) {
        return this.baseMapper.listReportByCuttingId(cuttingFilterDto);
    }

    /**
     * 提交（未启用审批流时调用，直接审核）
     *
     * @param id 裁床单id
     * @return 已提交的裁床单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdCutting submit(String id) {
        PrdCutting result = this.detail(id);
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

    /**
     * 反审核
     *
     * @param id 裁床单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdCutting unAudit(String id) throws Exception {
        PrdCutting result = this.detail(id);
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
    public PrdCutting unAuditUptData(PrdCutting prdCutting) {
        prdCutting.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        prdCutting.setAuditTime(null);
        prdCutting.setAuditorId(null);
        prdCutting.setAuditor(null);
        if (!this.updateById(prdCutting)) {
            throw new BizException("反审核失败");
        }

        List<PrdCuttingRaw> cuttingRaws = cuttingRawService.listByPid(prdCutting.getId());
        if (!cuttingRaws.isEmpty()) {
            List<String> collect = cuttingRaws.stream().map(PrdCuttingRaw::getId).collect(toList());
            List<PrdCuttingRawIo> cuttingRawIos = cuttingRawIoService.listByPids(collect);
            if (!cuttingRawIos.isEmpty()) {
                cuttingRawIoService.batchRemove(cuttingRawIos);
            }
        }

        List<PrdCuttingAuxIo> cuttingAuxIos = cuttingAuxIoService.listByPid(prdCutting.getId());
        if (!cuttingAuxIos.isEmpty()) {
            cuttingAuxIoService.batchRemove(cuttingAuxIos);
        }
        return prdCutting;
    }

    /**
     * 批量反审核
     *
     * @param ids 需要反审核的裁床单id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PrdCutting> list = this.listByIds(idList);
        if (!list.isEmpty()) {
            List<PrdCutting> unAuditList = list.stream().filter(r -> r.getBillStatus().equals(Constants.INT_STATUS_AUDITED)).collect(toList());
            if (!unAuditList.isEmpty()) {
                for (PrdCutting prdCutting : unAuditList) {
                    this.unAudit(prdCutting.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    /**
     * 审核后处理数据
     *
     * @param prdCutting 审核的裁床单
     */
    @Transactional(rollbackFor = Exception.class)
    public PrdCutting auditUptData(PrdCutting prdCutting) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        prdCutting.setBillStatus(Constants.INT_STATUS_AUDITED);
        prdCutting.setAuditTime(sdf.format(new Date()));
        prdCutting.setAuditorId(RequestUtils.getUserId());
        prdCutting.setAuditor(RequestUtils.getNickname());

        // 原材料信息
        List<PrdCuttingRaw> cuttingRaws = cuttingRawService.listByPid(prdCutting.getId());
        List<String> materialDetailIds = cuttingRaws.stream().map(PrdCuttingRaw::getMaterialDetailId).collect(toList());

        // 辅料信息
        PrdMoMaterialAux srhAux = new PrdMoMaterialAux();
        srhAux.setPid(prdCutting.getSrcId());
        List<PrdMoMaterialAux> moMaterialAuxes = moMaterialAuxMapper.getList(srhAux);
        materialDetailIds.addAll(moMaterialAuxes.stream().map(PrdMoMaterialAux::getMaterialDetailId).collect(toList()));
        LinkedHashSet<String> set = new LinkedHashSet<>(materialDetailIds);
        materialDetailIds = new ArrayList<>(set);

        InvInventory invInventory = new InvInventory();
        invInventory.setMaterialDetailIds(materialDetailIds);
        List<InvInventory> invInventories = inventoryService.myList(invInventory);

        List<PrdCuttingRawIo> cuttingRawIos = new ArrayList<>();
        int seq = 1;
        for (PrdCuttingRaw cuttingRaw : cuttingRaws) {
            if (cuttingRaw.getUseWaste()) {
                continue;
            }
            BigDecimal realQty = cuttingRaw.getRealQty();
            BigDecimal realPiQty = cuttingRaw.getRealPiQty();
            List<InvInventory> collect = invInventories.stream().filter(t -> t.getMaterialDetailId().equals(cuttingRaw.getMaterialDetailId())).collect(toList());
            for (InvInventory inventory : collect) {
                if (realQty.compareTo(BigDecimal.ZERO) <= 0 && realPiQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                if (inventory.getQty().compareTo(BigDecimal.ZERO) <= 0 || inventory.getPiQty().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                PrdCuttingRawIo prdCuttingRawIo = new PrdCuttingRawIo();
                prdCuttingRawIo.setMaterialDetailId(inventory.getMaterialDetailId());
                prdCuttingRawIo.setGpId(cuttingRaw.getPid());
                prdCuttingRawIo.setPid(cuttingRaw.getId());
                prdCuttingRawIo.setQty(realQty.compareTo(inventory.getQty()) > 0 ? inventory.getQty() : realQty);
                prdCuttingRawIo.setPiQty(realPiQty.compareTo(inventory.getPiQty()) > 0 ? inventory.getPiQty() : realPiQty);
                prdCuttingRawIo.setRepositoryId(inventory.getRepositoryId());
                prdCuttingRawIo.setPositionId(StringUtils.isNotEmpty(inventory.getPositionId()) ? inventory.getPositionId() : "0");
                prdCuttingRawIo.setPrice(inventory.getPrice());
                prdCuttingRawIo.setLot(inventory.getLot());
                prdCuttingRawIo.setSeq(seq++);
                cuttingRawIos.add(prdCuttingRawIo);

                realQty = realQty.subtract(inventory.getQty());
                realPiQty = realPiQty.subtract(inventory.getPiQty());

                inventory.setQty(inventory.getQty().subtract(prdCuttingRawIo.getQty()));
                inventory.setPiQty(inventory.getPiQty().subtract(prdCuttingRawIo.getPiQty()));
            }
            if (realQty.compareTo(BigDecimal.ZERO) > 0 || realPiQty.compareTo(BigDecimal.ZERO) > 0) {
                throw new BizException("原材料：" + cuttingRaw.getProductName() + "，颜色：" + cuttingRaw.getColor() + "，尺码：" + cuttingRaw.getSpecification() + "库存不足！");
            }
        }
        if(!cuttingRawIos.isEmpty()) {
            cuttingRawIoService.batchAdd(cuttingRawIos);
        }


        BigDecimal allQty = prdCutting.getProductQty();
        if (StringUtils.isNull(allQty)) {
            PrdCutting cutting = this.getById(prdCutting.getId());
            allQty = cutting.getProductQty();
        }
        List<PrdCuttingAuxIo> cuttingAuxIos = new ArrayList<>();
        seq = 1;
        for (PrdMoMaterialAux moMaterialAux : moMaterialAuxes) {
            BigDecimal denominator = moMaterialAux.getDenominator();
            BigDecimal numerator = moMaterialAux.getNumerator();
            BigDecimal realQty = allQty.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
            List<InvInventory> collect = invInventories.stream().filter(t -> t.getMaterialDetailId().equals(moMaterialAux.getMaterialDetailId())).collect(toList());

            for (InvInventory inventory : collect) {
                if (realQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                if (inventory.getQty().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                PrdCuttingAuxIo prdCuttingAuxIo = new PrdCuttingAuxIo();
                prdCuttingAuxIo.setMaterialDetailId(inventory.getMaterialDetailId());
                prdCuttingAuxIo.setQty(realQty.compareTo(inventory.getQty()) > 0 ? inventory.getQty() : realQty);
                prdCuttingAuxIo.setRepositoryId(inventory.getRepositoryId());
                prdCuttingAuxIo.setPositionId(StringUtils.isNotEmpty(inventory.getPositionId()) ? inventory.getPositionId() : "0");
                prdCuttingAuxIo.setPrice(inventory.getPrice());
                prdCuttingAuxIo.setLot(inventory.getLot());
                prdCuttingAuxIo.setPid(prdCutting.getId());
                prdCuttingAuxIo.setSeq(seq++);
                cuttingAuxIos.add(prdCuttingAuxIo);

                realQty = realQty.subtract(inventory.getQty());

                inventory.setQty(inventory.getQty().subtract(prdCuttingAuxIo.getQty()));
            }
            if (realQty.compareTo(BigDecimal.ZERO) > 0) {
                throw new BizException("辅料：" + moMaterialAux.getAuxMaterial().getName() + "，颜色：" + moMaterialAux.getColor() + "，尺码：" + moMaterialAux.getSpecification() + "库存不足！");
            }
        }
        if (!cuttingAuxIos.isEmpty()) {
            cuttingAuxIoService.batchAdd(cuttingAuxIos);
        }

        return prdCutting;
    }

    /**
     * 获取源单信息
     *
     * @param srcId 源单id（生产订单）
     * @return 源单信息Dto
     */
    @Override
    public MoAsSrcDto getSrcData(String srcId) {
        PrdMo mo = moMapper.infoById(srcId);
        MoAsSrcDto moAsSrcDto = new MoAsSrcDto();
        moAsSrcDto.setDeliveryDate(mo.getDeliveryDate());
        moAsSrcDto.setId(mo.getId());
        moAsSrcDto.setNo(mo.getNo());
        moAsSrcDto.setBillType(mo.getBillType());

        BdMaterial material = materialService.getById(mo.getProductId());
        moAsSrcDto.setProductId(mo.getProductId());
        moAsSrcDto.setProductNum(material.getNumber());
        moAsSrcDto.setProductName(material.getName());
        moAsSrcDto.setUnitId(material.getUnitId());
        BdUnit unit = unitMapper.selectById(material.getUnitId());
        moAsSrcDto.setUnitName(unit.getName());
        SysFiles files = filesService.getById(material.getMainPicId());
        if (files != null) {
            moAsSrcDto.setMainPic(files.getUrl());
        }

        // 工序详情
        List<PrdMoProcess> moProcesses = moProcessMapper.listByPid(srcId);
        List<PrdCuttingRoute> cuttingRoutes = getCuttingRoutes(moProcesses);
        moAsSrcDto.setCuttingRoutes(cuttingRoutes);
        // 工票信息
        List<PrdMoColorSpecData> moColorSpecData = moColorSpecDataService.listByPid(srcId);
        List<PrdCuttingTicket> cuttingTickets = getCuttingTickets(moColorSpecData, mo);
        moAsSrcDto.setCuttingTickets(cuttingTickets);
        // 原材料信息
        PrdMoMaterialDetail srhDetail = new PrdMoMaterialDetail();
        srhDetail.setMoId(srcId);
        List<PrdMoMaterialDetail> moMaterialDetails = moMaterialDetailMapper.getList(srhDetail);
        List<PrdCuttingRaw> cuttingRaws = getCuttingRaws(moMaterialDetails);
        moAsSrcDto.setCuttingRaws(cuttingRaws);
        return moAsSrcDto;
    }

    /**
     * 反写生产订单-颜色规格详情
     *
     * @param reWriteData 需要反写的信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reWrite(Map<String, BigDecimal> reWriteData) {
        Set<String> srcItemIds = reWriteData.keySet();
        List<PrdMoColorSpecData> moColorSpecData = moColorSpecDataService.listByIds(srcItemIds);
        for (PrdMoColorSpecData moColorSpec : moColorSpecData) {
            BigDecimal bigDecimal = reWriteData.get(moColorSpec.getId());
            moColorSpec.setCuttingQty(moColorSpec.getCuttingQty().add(bigDecimal));
        }
        moColorSpecDataService.updateBatchById(moColorSpecData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<PrdCutting> list = this.listByIds(idList);
        if (list.isEmpty()) {
            throw new BizException("未选择数据");
        }
        // 过滤 创建/重新审核 且 启用 的数据
        List<PrdCutting> submitList = list.stream().filter(r -> (r.getBillStatus().equals(Constants.INT_STATUS_CREATE) || r.getBillStatus().equals(Constants.INT_STATUS_RESUBMIT))).collect(toList());
        if (!submitList.isEmpty()) {
            for (PrdCutting cutting : submitList) {
                this.submit(cutting.getId());
            }
        }
        return "提交成功";
    }

    /**
     * 执行操作 提交/审核/反审核
     *
     * @param cutting 裁床单
     * @return 操作执行完成的裁床单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<PrdCutting> doAction(PrdCutting cutting) throws Exception {
        if (cutting.getBillStatus().equals(Constants.INT_STATUS_APPROVING) && ObjectUtils.isNotEmpty(cutting.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(cutting.getWorkFlowId());
            flowOperationInfo.setFormData(cutting);
            flowOperationInfo.setUserId(cutting.getUserId());
            flowOperationInfo.setChildNodes(cutting.getChildNodes());
            flowOperationInfo.setCurrentNodeId(cutting.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(cutting.getChildNodeApprovalResult());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                cutting.setWorkFlowId("");
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
            ids.add(cutting.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, cutting.getWorkFlow().getId());
            cutting.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            cutting.setNodeStatus(currentNodes.get(0).getStatus());
            cutting.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(cutting.getId()) == 1) {
                cutting = this.auditUptData(cutting);
            }
            // 驳回
            if (circulationOperationService.whetherLast(cutting.getId()) == 2) {
                cutting.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(cutting)) {
            throw new BizException("操作失败");
        }

        return Result.success(cutting);
    }

    /**
     * 批量执行操作 提交/审核/反审核
     *
     * @param cutting 裁床单
     * @return 操作执行结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> batchDoAction(PrdCutting cutting) throws Exception {
        List<String> ids = cutting.getIds();
        List<PrdCutting> cuttings = this.list(new LambdaQueryWrapper<PrdCutting>()
                .in(PrdCutting::getId, ids)
        );
        if (!cuttings.isEmpty()) {
            List<ChildNode> childNodes = getCurrentNodes(ids, cutting.getWorkFlow().getId());
            for (PrdCutting item : cuttings) {
                item.setBillStatus(cutting.getBillStatus());
                item.setWorkFlowId(cutting.getWorkFlowId());
                item.setUserId(cutting.getUserId());
                item.setChildNodes(cutting.getChildNodes());
                item.setChildNodeApprovalResult(cutting.getChildNodeApprovalResult());
                item.setWorkFlow(cutting.getWorkFlow());
                for (ChildNode childNode : childNodes) {
                    if (childNode.getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNode.getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNode.getStatus());
                        item.setCurrentNodeId(childNode.getId());
                        break;
                    }
                }
                Result<PrdCutting> result = doAction(item);
                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
                    throw new BizException("操作失败");
                }
            }
        }

        return Result.success("操作成功");
    }

    /**
     * 改菲接口
     *
     * @param updateTicketItemDto 改菲Dto
     * @return 是否改菲成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTicketItemQty(UpdateTicketItemDto updateTicketItemDto) {
        BigDecimal curQty = updateTicketItemDto.getQty();
        String ticketItemId = updateTicketItemDto.getCuttingTicketItemId();
        PrdCuttingTicketItem ticketItem = cuttingTicketItemService.getById(ticketItemId);
        if (ticketItem == null) {
            throw new BizException("工票号不存在");
        }

        BigDecimal oldQty = ticketItem.getQty();

        // 修改票的数量
        PrdCuttingTicketItem cuttingTicketItem = new PrdCuttingTicketItem();
        cuttingTicketItem.setId(ticketItemId);
        cuttingTicketItem.setQty(curQty);
        cuttingTicketItemService.updateById(cuttingTicketItem);

        // 修改上数数据
        List<PrdCuttingTicketItemReport> reports = cuttingTicketItemReportService.listDetailByPid(ticketItemId);
        for (PrdCuttingTicketItemReport report : reports) {
            report.setReportingQty(curQty);
            report.setRemark("改菲");
            this.updateReport(report);
        }

        // 日志
        PrdTicketLog prdTicketLog = new PrdTicketLog();
        prdTicketLog.setCreatorId(RequestUtils.getUserId());
        prdTicketLog.setCreator(RequestUtils.getNickname());
        prdTicketLog.setRemark("改菲 工票号：" + cuttingTicketItem.getTicketNo() + ",数量" +oldQty +"改为"+  cuttingTicketItem.getReportedQty()+"；");
        prdTicketLogService.save(prdTicketLog);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTicketItemQty(List<UpdateTicketItemDto> updateTicketItemDtos) {
        for (UpdateTicketItemDto updateTicketItemDto : updateTicketItemDtos) {
            this.updateTicketItemQty(updateTicketItemDto);
        }
        return true;
    }

    @Override
    public Map<String, Object> pageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        List<CurMonthReportVo> reportVoList = baseMapper.pageProductionSchedule(cuttingFilterDto);
        List<CurMonthReportVo> reportVos = new ArrayList<>();
        List<BdProcedure> bdProcedures = new ArrayList<>();
        if (!reportVoList.isEmpty()) {
            Set<String> cuttingIds = reportVoList.stream().map(CurMonthReportVo::getCuttingId).collect(Collectors.toSet());
            List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPids(cuttingIds);
            Set<String> procedureIds = cuttingRoutes.stream().map(PrdCuttingRoute::getProcedureId).collect(Collectors.toSet());
//            Set<String> procedureIds = reportVoList.stream().map(CurMonthReportVo::getProcedureId).collect(Collectors.toSet());
            bdProcedures = bdProcedureMapper.selectBatchIds(procedureIds);
            reportVos = reportVoList.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(
                                    CurMonthReportVo::getTicketNo))), ArrayList::new));
            Map<String, List<CurMonthReportVo>> reportMap = reportVoList.stream().collect(Collectors.groupingBy(CurMonthReportVo::getTicketNo));
            for (CurMonthReportVo reportVo : reportVos) {
                List<ProcedureVo> procedureVoList = new ArrayList<>();
                for (BdProcedure bdProcedure : bdProcedures) {
                    ProcedureVo procedureVo = new ProcedureVo();
                    procedureVo.setProcedureName(bdProcedure.getName());
                    procedureVo.setProcedureId(bdProcedure.getId());
                    procedureVoList.add(procedureVo);
                }
                reportVo.setProcedureVoList(procedureVoList);
            }
            for (CurMonthReportVo reportVo : reportVos) {
                List<CurMonthReportVo> curMonthReportVos = reportMap.get(reportVo.getTicketNo());
                if (!curMonthReportVos.isEmpty()) {
                    List<ProcedureVo> procedureVoList = reportVo.getProcedureVoList();
                    for (CurMonthReportVo curMonthReportVo : curMonthReportVos) {
                        for (ProcedureVo procedureVo : procedureVoList) {
                            if (procedureVo.getProcedureId().equals(curMonthReportVo.getProcedureId())) {
                                procedureVo.setTicketNo(curMonthReportVo.getTicketNo());
                                procedureVo.setJobNumber(curMonthReportVo.getJobNumber());
                                procedureVo.setNickname(curMonthReportVo.getNickname());
                                procedureVo.setReportTime(curMonthReportVo.getReportTime());
                                procedureVo.setReportedQty(curMonthReportVo.getReportedQty().add(procedureVo.getReportedQty() == null?BigDecimal.ZERO : procedureVo.getReportedQty()));
                                procedureVo.setReason(curMonthReportVo.getReason());
                            }
                        }
                    }
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        List<CurMonthReportVo> voList = reportVos.stream()
                .skip(page.getSize() * (page.getCurrent() - 1))
                .limit(page.getSize()).collect(toList());
        map.put("records", voList);
        map.put("total", reportVos.size());
        map.put("size", page.getSize());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("procedures", bdProcedures);
        return map;
    }

    @Override
    public void exportProductionSchedule(HttpServletResponse response, CuttingFilterDto cuttingFilterDto) {
        List<CurMonthReportVo> reportVoList = baseMapper.pageProductionSchedule(cuttingFilterDto);
        List<CurMonthReportVo> reportVos = new ArrayList<>();
        List<BdProcedure> bdProcedures = new ArrayList<>();
        if (!reportVoList.isEmpty()) {
            Set<String> cuttingIds = reportVoList.stream().map(CurMonthReportVo::getCuttingId).collect(Collectors.toSet());
            List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPids(cuttingIds);
            Set<String> procedureIds = cuttingRoutes.stream().map(PrdCuttingRoute::getProcedureId).collect(Collectors.toSet());
//            Set<String> procedureIds = reportVoList.stream().map(CurMonthReportVo::getProcedureId).collect(Collectors.toSet());
            bdProcedures = bdProcedureMapper.selectBatchIds(procedureIds);
            reportVos = reportVoList.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(
                                    CurMonthReportVo::getTicketNo))), ArrayList::new));
            Map<String, List<CurMonthReportVo>> reportMap = reportVoList.stream().collect(Collectors.groupingBy(CurMonthReportVo::getTicketNo));
            for (CurMonthReportVo reportVo : reportVos) {
                List<ProcedureVo> procedureVoList = new ArrayList<>();
                for (BdProcedure bdProcedure : bdProcedures) {
                    ProcedureVo procedureVo = new ProcedureVo();
                    procedureVo.setProcedureName(bdProcedure.getName());
                    procedureVo.setProcedureId(bdProcedure.getId());
                    procedureVoList.add(procedureVo);
                }
                reportVo.setProcedureVoList(procedureVoList);
            }
            for (CurMonthReportVo reportVo : reportVos) {
                List<CurMonthReportVo> curMonthReportVos = reportMap.get(reportVo.getTicketNo());
                if (!curMonthReportVos.isEmpty()) {
                    List<ProcedureVo> procedureVoList = reportVo.getProcedureVoList();
                    for (CurMonthReportVo curMonthReportVo : curMonthReportVos) {
                        for (ProcedureVo procedureVo : procedureVoList) {
                            if (procedureVo.getProcedureId().equals(curMonthReportVo.getProcedureId())) {
                                procedureVo.setTicketNo(curMonthReportVo.getTicketNo());
                                procedureVo.setJobNumber(curMonthReportVo.getJobNumber());
                                procedureVo.setNickname(curMonthReportVo.getNickname());
                                procedureVo.setReportTime(curMonthReportVo.getReportTime());
                                procedureVo.setReportedQty(curMonthReportVo.getReportedQty().add(procedureVo.getReportedQty() == null?BigDecimal.ZERO : procedureVo.getReportedQty()));
                                procedureVo.setReason(curMonthReportVo.getReason());
                            }
                        }
                    }
                }
            }
        }
        // 创建一个excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 新建sheet页
        XSSFSheet sheet = workbook.createSheet("sheet1");
        //设置默认宽度好像必须先设置默认高度，不然不生效。。。。。。
        sheet.setDefaultRowHeight((short) (1 * 256));
        sheet.setDefaultColumnWidth(20);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont fontStyle = workbook.createFont();
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
        XSSFCellStyle cellStyleContent = workbook.createCellStyle();
        cellStyleContent.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyleContent.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置边框
        cellStyleContent.setBorderTop(BorderStyle.THIN);
        cellStyleContent.setBorderBottom(BorderStyle.THIN);
        cellStyleContent.setBorderLeft(BorderStyle.THIN);
        cellStyleContent.setBorderRight(BorderStyle.THIN);
        XSSFFont fontStyleContent = workbook.createFont();
        fontStyle.setFontHeightInPoints((short) 12);
        cellStyleContent.setFont(fontStyleContent);
        //第一行   头
        XSSFRow row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints(20);
        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("排单号");
        title.add("筐号");
        title.add("订单号");
        title.add("款号");
        title.add("颜色");
        title.add("尺码");
        title.add("排单日期");
        title.add("排单数");
        for (int i = 0; i < title.size(); i++) {
            XSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        int count = 0;
        for (BdProcedure bdProcedure : bdProcedures) {
            int startIndex = 9 + count;
            int endIndex = 9 + count + 5;
            for (int j = 0; j < 6; j++) {
                XSSFCell cel = row2.createCell(count + 9);
                cel.setCellStyle(cellStyle);
                cel.setCellValue(bdProcedure.getName());
                count++;
            }
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, startIndex, endIndex);
            sheet.addMergedRegion(cellRangeAddress);
        }
        //第二行   头
        XSSFRow row3 = sheet.createRow(sheet.getLastRowNum() + 1);
        row3.setHeightInPoints(20);
        for (int i = 0; i < title.size(); i++) {
            XSSFCell cel = row3.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        count = 0;

        for (int i = 0; i < bdProcedures.size(); i++) {
            for (int k = 0; k < 6; k++) {
                XSSFCell cel = row3.createCell(count + 9);
                cel.setCellStyle(cellStyle);
                if (k == 0) {
                    cel.setCellValue("条码");
                }
                if (k == 1) {
                    cel.setCellValue("日期");
                }
                if (k == 2) {
                    cel.setCellValue("工号");
                }
                if (k == 3) {
                    cel.setCellValue("姓名");
                }
                if (k == 4) {
                    cel.setCellValue("正品");
                }
                if (k == 5) {
                    cel.setCellValue("次品");
                }
                count++;
            }
        }


//        // 数据
        for (int j = 0, recordsSize = reportVos.size(); j < recordsSize; j++) {
            CurMonthReportVo record = reportVos.get(j);
            XSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            rowData.createCell(1).setCellValue(record.getCuttingNo());
            rowData.createCell(2).setCellValue(record.getTicketNo());
            rowData.createCell(3).setCellValue(record.getOrderNo());
            rowData.createCell(4).setCellValue(record.getProductName());
            rowData.createCell(5).setCellValue(record.getColor());
            rowData.createCell(6).setCellValue(record.getSpecification());
            rowData.createCell(7).setCellValue(record.getCuttingDate());
            rowData.createCell(8).setCellValue(String.valueOf(record.getQty()));
            rowData.getCell(0).setCellStyle(cellStyleContent);
            rowData.getCell(1).setCellStyle(cellStyleContent);
            rowData.getCell(2).setCellStyle(cellStyleContent);
            rowData.getCell(3).setCellStyle(cellStyleContent);
            rowData.getCell(4).setCellStyle(cellStyleContent);
            rowData.getCell(5).setCellStyle(cellStyleContent);
            rowData.getCell(6).setCellStyle(cellStyleContent);
            rowData.getCell(7).setCellStyle(cellStyleContent);
            rowData.getCell(8).setCellStyle(cellStyleContent);
            List<ProcedureVo> procedureVoList = record.getProcedureVoList();
            count = 0;
            for (int i = 0; i < bdProcedures.size(); i++) {
                String procedureId = bdProcedures.get(i).getId();
                ProcedureVo procedureVo = procedureVoList.stream().filter(s -> s.getProcedureId().equals(procedureId)).findFirst().orElse(null);
                for (int k = 0; k < 6; k++) {
                    XSSFCell cel = rowData.createCell(count + 9);
                    cel.setCellStyle(cellStyleContent);
                    if (ObjectUtil.isNotNull(procedureVo) && StringUtils.isNotEmpty(procedureVo.getTicketNo())) {
                        if (k == 0) {
                            cel.setCellValue(procedureVo.getTicketNo());
                        }
                        if (k == 1) {
                            cel.setCellValue(procedureVo.getReportTime());
                        }
                        if (k == 2) {
                            cel.setCellValue(procedureVo.getJobNumber());
                        }
                        if (k == 3) {
                            cel.setCellValue(procedureVo.getNickname());
                        }
                        if (k == 4) {
                            cel.setCellValue(String.valueOf(procedureVo.getReportedQty()));
                        }
                        if (k == 5) {
                            cel.setCellValue(String.valueOf(procedureVo.getDefectQty()));
                        }
                    } else {
                        cel.setCellValue("");
                    }
                    count++;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            //合并
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 1, i, i);
            sheet.addMergedRegion(cellRangeAddress);
        }
        ExcelUtils.buildXlsxDocument("生产进度表", workbook, response);
    }

    @Override
    public List<PrdCutting> listDetailByIds(List<String> ids) {
        List<PrdCutting> prdCuttings = listByIds(ids);
        List<PrdCuttingTicket> cuttingTickets = cuttingTicketService.list(new LambdaQueryWrapper<PrdCuttingTicket>()
                .in(PrdCuttingTicket::getPid, ids));
        for (PrdCutting prdCutting : prdCuttings) {
            String id = prdCutting.getId();
            List<PrdCuttingTicket> collect = cuttingTickets.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            prdCutting.setCuttingTickets(collect);
        }
        return prdCuttings;
    }

    /**
     * 生产进度分页列表
     *
     * @param page             分页参数
     * @param cuttingFilterDto 过滤条件
     */
    @Override
    public IPage<CuttingTicketDetailVo> pageForProgress(Page<CuttingTicketDetailVo> page, CuttingFilterDto cuttingFilterDto) {
        return this.baseMapper.pageForProgress(page, cuttingFilterDto);
    }

    /**
     * 生产进度详情
     *
     * @param ticketId 工票信息id
     */
    @Override
    public CuttingTicketDetailVo detailForProgress(String ticketId) {
        CuttingTicketDetailVo record = this.baseMapper.detailForProgress(ticketId);

        String cuttingId = record.getCuttingId();
        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listDetailById(ticketId);
        List<PrdCuttingRoute> cuttingRoutes = cuttingRouteService.listByPid(cuttingId);
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listByCuttingId(cuttingId);
        List<PrdCuttingTicketItem> ticketItems = cuttingTicketItems.stream().filter(t -> t.getPid().equals(record.getCuttingTicketId())).collect(toList());
        record.setCuttingTicketItems(ticketItems);

        List<PrdCuttingTicketItemReport> cuttingReports = cuttingTicketItemReports.stream().filter(t -> t.getCuttingId().equals(record.getCuttingId())).collect(toList());
        int all = 0;
        int finished = 0;
        List<PrdCuttingRoute> cuttingRoutesFiltered = cuttingRoutes.stream().filter(t -> t.getPid().equals(record.getCuttingId())).collect(toList());
        for (PrdCuttingTicketItem cuttingTicketItem : ticketItems) {
            int ticketItemAll = cuttingRoutesFiltered.size();
            int ticketItemFinished = 0;
            for (PrdCuttingRoute cuttingRoute : cuttingRoutesFiltered) {
                all++;
                BigDecimal reportedQty = cuttingReports.stream()
                        .filter(t -> t.getPid().equals(cuttingTicketItem.getId())
                                && t.getCuttingRouteId().equals(cuttingRoute.getId()))
                        .map(PrdCuttingTicketItemReport::getReportedQty)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (reportedQty.compareTo(cuttingTicketItem.getQty()) >= 0) {
                    finished++;
                    ticketItemFinished++;
                }
            }
            cuttingTicketItem.setProgressCount(ticketItemAll);
            cuttingTicketItem.setFinishProgressCount(ticketItemFinished);
            cuttingTicketItem.setProgress(BigDecimal.valueOf(ticketItemFinished).divide(BigDecimal.valueOf(ticketItemAll), 2, RoundingMode.HALF_UP));
        }
        record.setProgressCount(all);
        record.setFinishProgressCount(finished);
        record.setProgress(BigDecimal.valueOf(finished).divide(BigDecimal.valueOf(all), 2, RoundingMode.HALF_UP));
        return record;
    }

    @Override
    public IPage<CurMonthReportVo> appPageProductionSchedule(Page<CurMonthReportVo> page, CuttingFilterDto cuttingFilterDto) {
        return baseMapper.pageProductionSchedule(page, cuttingFilterDto);
    }

    @Override
    public JSONObject appProductionScheduleSum(CuttingFilterDto cuttingFilterDto) {
        CuttingFilterDto res = baseMapper.productionScheduleSum(cuttingFilterDto);
        JSONObject result = new JSONObject();
        if(ObjectUtil.isNotNull(res)){
            result.put("totalCount", res.getTotalCount());
            result.put("totalAmount", res.getTotalAmount());
        }else{
            result.put("totalCount", 0);
            result.put("totalAmount", 0);
        }
        return result;
    }

    /**
     * 构造源单原材料信息
     *
     * @param moMaterialDetails 源数据
     */
    @NotNull
    private static List<PrdCuttingRaw> getCuttingRaws(List<PrdMoMaterialDetail> moMaterialDetails) {
        List<PrdCuttingRaw> cuttingRaws = new ArrayList<>();
        for (int i = 0; i < moMaterialDetails.size(); i++) {
            PrdMoMaterialDetail moMaterialDetail = moMaterialDetails.get(i);
            PrdCuttingRaw cuttingRaw = new PrdCuttingRaw();
            cuttingRaw.setParentSkuId(moMaterialDetail.getParentSkuId());
            cuttingRaw.setMaterialDetailId(moMaterialDetail.getMaterialDetailId());
            cuttingRaw.setNumerator(moMaterialDetail.getNumerator());
            cuttingRaw.setDenominator(moMaterialDetail.getDenominator());
            cuttingRaw.setTheoryQty(BigDecimal.ZERO);
            cuttingRaw.setRealQty(BigDecimal.ZERO);
            cuttingRaw.setProductNum(moMaterialDetail.getRawMaterial().getNumber());
            cuttingRaw.setProductName(moMaterialDetail.getRawMaterial().getName());
            cuttingRaw.setMainPic(moMaterialDetail.getRawMaterial().getMainPicUrl());
            cuttingRaw.setUnitName(moMaterialDetail.getRawMaterial().getUnitName());
            cuttingRaw.setColor(moMaterialDetail.getColor());
            cuttingRaw.setSpecification(moMaterialDetail.getSpecification());
            cuttingRaw.setUseWaste(false);
            cuttingRaw.setSeq(i + 1);

            cuttingRaws.add(cuttingRaw);
        }
        return cuttingRaws;
    }

    /**
     * 构造源单票据信息
     *
     * @param moColorSpecData 源数据
     * @param mo              生产订单
     */
    @NotNull
    private static List<PrdCuttingTicket> getCuttingTickets(List<PrdMoColorSpecData> moColorSpecData, PrdMo mo) {
        List<PrdCuttingTicket> cuttingTickets = new ArrayList<>();
        for (int i = 0; i < moColorSpecData.size(); i++) {
            PrdMoColorSpecData moColorSpecDatum = moColorSpecData.get(i);
            PrdCuttingTicket prdCuttingTicket = new PrdCuttingTicket();
            prdCuttingTicket.setSkuId(moColorSpecDatum.getProductId());
            prdCuttingTicket.setColor(moColorSpecDatum.getColor());
            prdCuttingTicket.setSpecification(moColorSpecDatum.getSpecification());
            prdCuttingTicket.setSumQty(BigDecimal.ZERO);
            prdCuttingTicket.setCount(0);
            prdCuttingTicket.setEachQty(BigDecimal.ZERO);
            prdCuttingTicket.setSrcType(mo.getBillType());
            prdCuttingTicket.setSrcId(mo.getId());
            prdCuttingTicket.setSrcNo(mo.getNo());
            prdCuttingTicket.setSrcItemId(moColorSpecDatum.getId());
            prdCuttingTicket.setSrcAllQty(moColorSpecDatum.getQty());
            prdCuttingTicket.setSrcCuttingQty(moColorSpecDatum.getCuttingQty());
            prdCuttingTicket.setSrcQty(moColorSpecDatum.getQty().subtract(moColorSpecDatum.getCuttingQty()));

            prdCuttingTicket.setCuttingTicketItems(new ArrayList<>());
            prdCuttingTicket.setUnitName(moColorSpecDatum.getUnitName());
            prdCuttingTicket.setSeq(i + 1);

            cuttingTickets.add(prdCuttingTicket);
        }
        return cuttingTickets;
    }

    /**
     * 构造源单工序信息
     *
     * @param moProcesses 源数据
     */
    @NotNull
    private static List<PrdCuttingRoute> getCuttingRoutes(List<PrdMoProcess> moProcesses) {
        List<PrdCuttingRoute> cuttingRoutes = new ArrayList<>();
        for (int i = 0; i < moProcesses.size(); i++) {
            PrdMoProcess moProcess = moProcesses.get(i);
            PrdCuttingRoute cuttingRoute = new PrdCuttingRoute();
            cuttingRoute.setUniqueKey(moProcess.getId());
            cuttingRoute.setProcedureNo(moProcess.getNo());
            cuttingRoute.setProcedureId(moProcess.getProcedureId());
            cuttingRoute.setPrice(moProcess.getPrice());
            cuttingRoute.setQty(BigDecimal.ZERO);
            cuttingRoute.setMoProcessQty(moProcess.getQty());
            cuttingRoute.setProcedureName(moProcess.getProcedureName());
            cuttingRoute.setProcedureNum(moProcess.getProcedureNumber());
            cuttingRoute.setSeq(i + 1);

            cuttingRoutes.add(cuttingRoute);
        }
        return cuttingRoutes;
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
