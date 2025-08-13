package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
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
import com.fenglei.mapper.basedata.BdMaterialAttachMapper;
import com.fenglei.mapper.basedata.BdMaterialDetailMapper;
import com.fenglei.mapper.basedata.BdMaterialRawMapper;
import com.fenglei.mapper.inv.InvInventoryMapper;
import com.fenglei.mapper.prd.*;
import com.fenglei.model.basedata.BdMaterialAttach;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.basedata.BdMaterialRaw;
import com.fenglei.model.basedata.BdTag;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.oms.entity.dto.OmsSaleOutStockDTO;
import com.fenglei.model.prd.dto.PrdMoDTO;
import com.fenglei.model.prd.entity.*;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdMaterialDetailService;
import com.fenglei.service.basedata.BdTagService;
import com.fenglei.service.prd.*;
import com.fenglei.service.system.SysFilesService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
public class PrdMoServiceImpl extends ServiceImpl<PrdMoMapper, PrdMo> implements PrdMoService {

    @Resource
    private PrdMoColorSpecDataService prdMoColorSpecDataService;
    @Resource
    private PrdMoProcessService prdMoProcessService;
    @Resource
    private PrdMoMaterialAuxService prdMoMaterialAuxService;
    @Resource
    private PrdMoMaterialDetailService prdMoMaterialDetailService;
    @Resource
    private PrdMoOtherCostService prdMoOtherCostService;
    @Resource
    private PrdMoFinSizeDataService prdMoFinSizeDataService;
    @Resource
    private PrdMoSecondaryProcessService prdMoSecondaryProcessService;
    @Resource
    private PrdMoAttachImgService prdMoAttachImgService;
    @Resource
    private PrdMoAttachFileService prdMoAttachFileService;
    @Resource
    private SysFilesService sysFilesService;
    @Resource
    private BdTagService bdTagService;

    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private PrdMoAttachImgMapper prdMoAttachImgMapper;
    @Resource
    private PrdMoAttachFileMapper prdMoAttachFileMapper;
    @Resource
    private PrdMoColorSpecDataMapper prdMoColorSpecDataMapper;
    @Resource
    private PrdMoProcessMapper prdMoProcessMapper;
    @Resource
    private PrdMoMaterialDetailMapper prdMoMaterialDetailMapper;
    @Resource
    private BdMaterialDetailMapper bdMaterialDetailMapper;
    @Resource
    private BdMaterialDetailService bdMaterialDetailService;
    @Resource
    private PrdMoMaterialAuxMapper prdMoMaterialAuxMapper;
    @Resource
    private PrdMoSecondaryProcessMapper prdMoSecondaryProcessMapper;
    @Resource
    private PrdCuttingMapper prdCuttingMapper;
    @Resource
    private BdMaterialRawMapper bdMaterialRawMapper;
    @Resource
    private BdMaterialAttachMapper bdMaterialAttachMapper;
    @Resource
    private InvInventoryMapper invInventoryMapper;

    @Override
    public IPage<PrdMo> myPage(Page page, PrdMo prdMo) {
        IPage<PrdMo> iPage = baseMapper.getPage(page, prdMo);
        if (iPage != null) {
            List<PrdMo> records = iPage.getRecords();
            if (records != null && !records.isEmpty()) {

                Set<String> moIds = records.stream().map(PrdMo::getId).collect(Collectors.toSet());

                // 图片详情
                PrdMoAttachImg prdMoAttachImg = new PrdMoAttachImg();
                prdMoAttachImg.setPids(moIds);
                List<PrdMoAttachImg> prdMoAttachImgs = prdMoAttachImgMapper.getList(prdMoAttachImg);
                // 规格详情
                List<PrdMoColorSpecData> prdMoColorSpecDataList = prdMoColorSpecDataService.list(
                        new LambdaQueryWrapper<PrdMoColorSpecData>()
                                .in(PrdMoColorSpecData::getPid, moIds)
                );
                // 工序
                List<PrdMoProcess> prdMoProcesses = prdMoProcessMapper.listByPids(moIds);
                // 主图
                List<SysFiles> mainPics = new ArrayList<>();
                List<String> mainPicIds = records.stream().map(PrdMo::getMainPicId).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                if (!mainPicIds.isEmpty()) {
                    mainPics = sysFilesService.listByIds(mainPicIds);
                }

                for (PrdMo record : records) {
                    if (StringUtils.isNotEmpty(record.getTags())) {
                        List<String> tagIds = Arrays.asList(record.getTags().split(","));
                        List<BdTag> tags = bdTagService.listByIds(tagIds);
                        if (tags != null && tags.size() > 0) {
                            List<String> tagList = tags.stream().map(BdTag::getName).distinct().collect(Collectors.toList());
                            record.setTagList(tagList);
                        }
                    }

                    if (StringUtils.isNotEmpty(record.getMainPicId())) {
                        record.setMainPic(mainPics.stream().filter(t -> t.getId().equals(record.getMainPicId())).findFirst().orElse(null));
                    }

                    // 图片详情
                    List<PrdMoAttachImg> attachImgs = prdMoAttachImgs.stream().filter(t -> t.getPid().equals(record.getId())).collect(Collectors.toList());
                    if (!attachImgs.isEmpty()) {
                        List<String> attachImgUrls = new ArrayList<>();
                        for (PrdMoAttachImg attachImg : attachImgs) {
                            if (attachImg.getSysFile() != null) {
                                attachImgUrls.add(attachImg.getSysFile().getUrl());
                            }
                        }
                        record.setAttachImgUrls(attachImgUrls);
                    }
                    record.setAttachImgs(attachImgs);

                    List<PrdMoColorSpecData> colorSpecDatas = prdMoColorSpecDataList.stream().filter(t -> t.getPid().equals(record.getId())).collect(Collectors.toList());
                    if (!colorSpecDatas.isEmpty()) {
                        BigDecimal sumQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        record.setSumQty(sumQty);

                        BigDecimal cuttingQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getCuttingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        record.setCuttingQty(cuttingQty);

                        BigDecimal inStockQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getInstockQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        record.setInStockQty(inStockQty);
                    } else {
                        record.setSumQty(BigDecimal.ZERO);
                        record.setCuttingQty(BigDecimal.ZERO);
                        record.setInStockQty(BigDecimal.ZERO);
                    }

                    List<PrdMoProcess> processes = prdMoProcesses.stream().filter(t -> t.getPid().equals(record.getId())).collect(Collectors.toList());
                    if (!processes.isEmpty()) {
                        BigDecimal sumPrice = BigDecimal.ZERO;
                        for (PrdMoProcess process : processes) {
                            if (process.getPrice() != null) {
                                sumPrice = sumPrice.add(process.getPrice());
                            }
                        }
                        BigDecimal procedureSumAmt = record.getSumQty().multiply(sumPrice);
                        record.setProcedureSumAmount(procedureSumAmt);
                        record.setProcedureCount(processes.size());
                    } else {
                        record.setProcedureSumAmount(BigDecimal.ZERO);
                        record.setProcedureCount(0);
                    }

                }
            }
        }

        return iPage;
    }

    @Override
    public PrdMo getPageSummaryData(PrdMo prdMo) {
        Integer moCount = this.count(
                new LambdaQueryWrapper<PrdMo>()
                        .eq(PrdMo::getBillType, "生产订单")
        );

        Integer planCount = this.count(
                new LambdaQueryWrapper<PrdMo>()
                        .eq(PrdMo::getBillType, "计划单")
        );

        // 款数
        Integer finMtrlCount = baseMapper.getFinMtrlCount(prdMo);
        // 单数
        Integer count = baseMapper.getCount(prdMo);
        // 件数
        BigDecimal allQty = prdMoColorSpecDataMapper.getAllQty(prdMo);
        // 裁床数
        BigDecimal allCuttingQty = prdMoColorSpecDataMapper.getCuttingQty(prdMo);
        // 入库数量
        BigDecimal allInstockQty = prdMoColorSpecDataMapper.getInstockQty(prdMo);
        // 入库工序总价
        BigDecimal allInstockProcessAmt = prdMoColorSpecDataMapper.getAllInstockProcessAmt(prdMo);
        // 生产中工序总价
        // 入库工序总价
        BigDecimal allIngProcessAmt = prdMoColorSpecDataMapper.getAllIngProcessAmt(prdMo);

        PrdMo record = new PrdMo();

        record.setMoCount(moCount);
        record.setPlanCount(planCount);

        record.setFinMtrlCount(finMtrlCount);
        record.setCount(count);
        record.setAllQty(allQty);
        record.setAllCuttingQty(allCuttingQty);
        record.setAllInstockQty(allInstockQty);
        record.setAllInstockProcessAmt(allInstockProcessAmt);
        record.setAllIngQty(allCuttingQty.subtract(allInstockQty));
        record.setAllIngProcessAmt(allIngProcessAmt);

        return record;
    }

    @Override
    public List<PrdMo> myList(PrdMo prdMo) {
        List<PrdMo> list = baseMapper.getList(prdMo);
        if (list != null && list.size() > 0) {
            for (PrdMo record : list) {
                if (StringUtils.isNotEmpty(record.getTags())) {
                    List<String> tagIds = Arrays.asList(record.getTags().split(","));
                    List<BdTag> tags = bdTagService.listByIds(tagIds);
                    if (tags != null && tags.size() > 0) {
                        List<String> tagList = tags.stream().map(BdTag::getName).distinct().collect(Collectors.toList());
                        record.setTagList(tagList);
                    }
                }

                if (StringUtils.isNotEmpty(record.getMainPicId())) {
                    SysFiles mainPic = sysFilesService.getById(record.getMainPicId());
                    record.setMainPic(mainPic);
                }

                List<PrdMoColorSpecData> colorSpecDatas = prdMoColorSpecDataService.list(
                        new LambdaQueryWrapper<PrdMoColorSpecData>()
                                .eq(PrdMoColorSpecData::getPid, record.getId())
                );
                if (colorSpecDatas != null && colorSpecDatas.size() > 0) {
                    BigDecimal sumQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                    record.setSumQty(sumQty);
                } else {
                    record.setSumQty(BigDecimal.ZERO);
                }


                List<PrdMoProcess> processes = prdMoProcessMapper.listByPid(record.getId());
                if (processes != null && processes.size() > 0) {
                    BigDecimal sumPrice = BigDecimal.ZERO;
                    for (PrdMoProcess process : processes) {
                        if (process.getPrice() != null) {
                            sumPrice = sumPrice.add(process.getPrice());
                        }
                    }
                    BigDecimal procedureSumAmt = record.getSumQty().multiply(sumPrice);
                    record.setProcedureSumAmount(procedureSumAmt);
                    record.setProcedureCount(processes.size());
                } else {
                    record.setProcedureSumAmount(BigDecimal.ZERO);
                    record.setProcedureCount(0);
                }

            }
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdMo add(PrdMo prdMo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(prdMo.getNo())) {
            List<PrdMo> list = this.list(
                    new LambdaQueryWrapper<PrdMo>()
                            .eq(PrdMo::getNo, prdMo.getNo())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同款号的生产订单，请修改");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "MO" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<PrdMo> list = this.list(
                    new LambdaQueryWrapper<PrdMo>()
                            .likeRight(PrdMo::getNo, no)
                            .orderByDesc(PrdMo::getNo)
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
            prdMo.setNo(no);
        }

        prdMo.setBillStatus(Constants.INT_STATUS_CREATE);
        prdMo.setCreateTime(sdf.format(new Date()));
        prdMo.setCreatorId(RequestUtils.getUserId());
        prdMo.setCreator(RequestUtils.getNickname());

        List<String> tagList = prdMo.getTagList();
        if (tagList != null && !tagList.isEmpty()) {
            String tags = String.join(",", tagList);
            prdMo.setTags(tags);
        }

        // 主图
        List<PrdMoAttachImg> attachImgs = prdMo.getAttachImgs();
        if (attachImgs != null && !attachImgs.isEmpty()) {
            prdMo.setMainPicId(attachImgs.get(0).getAttachId());
        }

        if (!this.save(prdMo)) {
            throw new BizException("保存失败");
        }

        IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();

        // 颜色规格
        BigDecimal sumCount = BigDecimal.ZERO;
        List<PrdMoColorSpecData> moColorSpecDatas = prdMo.getMoColorSpecDatas();
        if (moColorSpecDatas != null && !moColorSpecDatas.isEmpty()) {
            // 获取关联原材料
            List<String> productIds = moColorSpecDatas.stream().map(PrdMoColorSpecData::getProductId).collect(Collectors.toList());
            BdMaterialRaw srhRaw = new BdMaterialRaw();
            srhRaw.setPids(productIds);
            List<BdMaterialRaw> raws = bdMaterialRawMapper.getList(srhRaw);

            List<PrdMoMaterialDetail> addDetails = new ArrayList<>();
            for (PrdMoColorSpecData moColorSpecData : moColorSpecDatas) {
                String id = identifierGenerator.nextId(new Object()).toString();
                moColorSpecData.setId(id);
                moColorSpecData.setPid(prdMo.getId());
                sumCount = sumCount.add(moColorSpecData.getQty());

                // 获取当前行关联原材料
                List<BdMaterialRaw> thisRaws = raws.stream().filter(r -> StringUtils.equals(r.getPid(), moColorSpecData.getProductId())).collect(Collectors.toList());
                for (BdMaterialRaw thisRaw : thisRaws) {
                    PrdMoMaterialDetail newDetail = new PrdMoMaterialDetail();
                    newDetail.setPid(id);
                    newDetail.setMaterialDetailId(thisRaw.getMaterialDetailId());
                    newDetail.setNumerator(thisRaw.getNumerator());
                    newDetail.setDenominator(thisRaw.getDenominator());

                    BigDecimal numerator = StringUtils.isNotNull(newDetail.getNumerator()) ? newDetail.getNumerator() : BigDecimal.ZERO;
                    BigDecimal denominator = StringUtils.isNotNull(newDetail.getDenominator()) ? newDetail.getDenominator() : BigDecimal.ZERO;
                    if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                        newDetail.setTheoryQty(BigDecimal.ZERO);
                    } else {
                        BigDecimal theoryQty = moColorSpecData.getQty().multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                        newDetail.setTheoryQty(theoryQty);
                    }

                    addDetails.add(newDetail);
                }
            }
            if (!prdMoColorSpecDataService.saveBatch(moColorSpecDatas)) {
                throw new BizException("保存失败，规格详情异常");
            }
            if (!addDetails.isEmpty()) {
                if (!prdMoMaterialDetailService.saveBatch(addDetails)) {
                    throw new BizException("保存失败，原料详情异常");
                }
            }
        }

        // 辅料
        List<PrdMoMaterialAux> moMaterialAuxes = prdMo.getMoMaterialAuxs();
        if (moMaterialAuxes != null && !moMaterialAuxes.isEmpty()) {
            for (PrdMoMaterialAux moMaterialAux : moMaterialAuxes) {
                moMaterialAux.setPid(prdMo.getId());
                BigDecimal numerator = StringUtils.isNotNull(moMaterialAux.getNumerator()) ? moMaterialAux.getNumerator() : BigDecimal.ZERO;
                BigDecimal denominator = StringUtils.isNotNull(moMaterialAux.getDenominator()) ? moMaterialAux.getDenominator() : BigDecimal.ZERO;
                if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                    moMaterialAux.setTheoryQty(BigDecimal.ZERO);
                } else {
                    BigDecimal theoryQty = sumCount.multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                    moMaterialAux.setTheoryQty(theoryQty);
                }

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(moMaterialAux.getAuxMaterial().getId());
                srhMtrlDtl.setColor(moMaterialAux.getColor());
                srhMtrlDtl.setSpecification(moMaterialAux.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (!details.isEmpty()) {
                    moMaterialAux.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("辅料信息异常 编码：" + moMaterialAux.getAuxMaterial().getNumber() + " 名称：" + moMaterialAux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }
            if (!prdMoMaterialAuxService.saveBatch(moMaterialAuxes)) {
                throw new BizException("保存失败，辅料详情异常");
            }
        }

        // 生产工序
        List<PrdMoProcess> moProcesses = prdMo.getMoProcesses();
        if (moProcesses != null && moProcesses.size() > 0) {
            for (PrdMoProcess moProcess : moProcesses) {
                moProcess.setPid(prdMo.getId());
                moProcess.setQty(sumCount);
            }
            if (!prdMoProcessService.saveBatch(moProcesses)) {
                throw new BizException("保存失败，生产工序异常");
            }
        }

        // 其他成本
        List<Map<String, Object>> moOtherCostMaps = prdMo.getMoOtherCostMaps();
        List<PrdMoOtherCost> moOtherCosts = new ArrayList<>();
        if (moOtherCostMaps != null) {
            for (int i = 0; i < moOtherCostMaps.size(); i++) {
                Map<String, Object> map = moOtherCostMaps.get(i);
                for (String s : map.keySet()) {
                    if (!StringUtils.equals(s, "price")) {
                        PrdMoOtherCost newData = new PrdMoOtherCost();
                        newData.setPid(prdMo.getId());
                        newData.setName(s);
                        newData.setAmount(new BigDecimal(map.get(s).toString()));

                        moOtherCosts.add(newData);
                    }
                }
            }
        }
        if (moOtherCosts.size() > 0) {
            if (!prdMoOtherCostService.saveBatch(moOtherCosts)) {
                throw new BizException("保存失败，其他成本异常");
            }
        }

//        // 成品尺寸
//        List<Map<String, Object>> moFinSizeMaps = prdMo.getMoFinSizeMaps();
//        List<PrdMoFinSizeData> moFinSizeDatas = new ArrayList<>();
//        if (moFinSizeMaps != null) {
//            for (int i = 0; i < moFinSizeMaps.size(); i++) {
//                Map<String, Object> map = moFinSizeMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "rowIdx")) {
//                        PrdMoFinSizeData newData = new PrdMoFinSizeData();
//                        newData.setPid(prdMo.getId());
//                        newData.setRowIdx(Integer.valueOf(map.get("rowIdx").toString()));
//                        newData.setColumnName(s);
//
//                        String val = map.get(s) != null ? map.get(s).toString() : "";
//                        newData.setValue(val);
//
//                        moFinSizeDatas.add(newData);
//                    }
//                }
//            }
//        }
//        if (moFinSizeDatas.size() > 0) {
//            if (!prdMoFinSizeDataService.saveBatch(moFinSizeDatas)) {
//                throw new BizException("保存失败，成品尺寸异常");
//            }
//        }

        // 二次工艺
        List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMo.getMoSecondaryProcesses();
        if (moSecondaryProcesses != null && moSecondaryProcesses.size() > 0) {
            for (PrdMoSecondaryProcess moSecondaryProcess : moSecondaryProcesses) {
                moSecondaryProcess.setPid(prdMo.getId());
            }
            if (!prdMoSecondaryProcessService.saveBatch(moSecondaryProcesses)) {
                throw new BizException("保存失败，二次工艺异常");
            }
        }

        // 图片详情
        if (attachImgs != null && attachImgs.size() > 0) {
            for (PrdMoAttachImg attachImg : attachImgs) {
                attachImg.setPid(prdMo.getId());
            }

            if (!prdMoAttachImgService.saveBatch(attachImgs)) {
                throw new BizException("保存失败，异常码1");
            }
        }

        // 文件详情
        List<PrdMoAttachFile> attachFiles = prdMo.getAttachFiles();
        if (attachFiles != null && attachFiles.size() > 0) {
            for (PrdMoAttachFile attachFile : attachFiles) {
                attachFile.setPid(prdMo.getId());
            }

            if (!prdMoAttachFileService.saveBatch(attachFiles)) {
                throw new BizException("保存失败，异常码2");
            }
        }

        return prdMo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdMo myUpdate(PrdMo prdMo) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(prdMo.getNo())) {
            List<PrdMo> list = this.list(
                    new LambdaQueryWrapper<PrdMo>()
                            .eq(PrdMo::getNo, prdMo.getNo())
                            .ne(PrdMo::getId, prdMo.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同款号的生产订单，请修改，异常码1");
            }
        } else {
            PrdMo thisMo = this.getById(prdMo.getId());
            if (thisMo != null) {
                prdMo.setNo(thisMo.getNo());

                List<PrdMo> list = this.list(
                        new LambdaQueryWrapper<PrdMo>()
                                .eq(PrdMo::getNo, prdMo.getNo())
                                .ne(PrdMo::getId, prdMo.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同款号的生产订单，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "MO" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<PrdMo> list = this.list(
                        new LambdaQueryWrapper<PrdMo>().likeRight(PrdMo::getNo, no)
                                .orderByDesc(PrdMo::getNo)
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
                prdMo.setNo(no);
            }
        }

        prdMo.setUpdateTime(sdf.format(new Date()));
        prdMo.setUpdaterId(RequestUtils.getUserId());
        prdMo.setUpdater(RequestUtils.getNickname());

        List<String> tagList = prdMo.getTagList();
        if (tagList != null && !tagList.isEmpty()) {
            String tags = String.join(",", tagList);
            prdMo.setTags(tags);
        }

        // 主图
        List<PrdMoAttachImg> attachImgs = prdMo.getAttachImgs();
        if (attachImgs != null && !attachImgs.isEmpty()) {
            prdMo.setMainPicId(attachImgs.get(0).getAttachId());
        }

        if (!this.updateById(prdMo)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        // 删除-颜色规格
        List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
                new LambdaQueryWrapper<PrdMoColorSpecData>()
                        .eq(PrdMoColorSpecData::getPid, prdMo.getId())
        );
        if (!oldColorSpecDatas.isEmpty()) {
            List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
            if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
                throw new BizException("更新失败，颜色规格异常");
            }

            // 删除原材料
            List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
                    new LambdaQueryWrapper<PrdMoMaterialDetail>()
                            .in(PrdMoMaterialDetail::getPid, oldColorSpecDataIds)
            );
            if (!oldMaterialDetails.isEmpty()) {
                List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
                if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
                    throw new BizException("更新失败，原料详情异常");
                }
            }
        }
        // 删除辅料
        List<PrdMoMaterialAux> oldMoMaterialAuxes = prdMoMaterialAuxService.list(
                new LambdaQueryWrapper<PrdMoMaterialAux>()
                        .eq(PrdMoMaterialAux::getPid, prdMo.getId())
        );
        if (!oldMoMaterialAuxes.isEmpty()) {
            List<String> oldMoMaterialAuxIds = oldMoMaterialAuxes.stream().map(PrdMoMaterialAux::getId).collect(Collectors.toList());
            if (!prdMoMaterialAuxService.removeByIds(oldMoMaterialAuxIds)) {
                throw new BizException("更新失败，生产辅料异常");
            }
        }

        // 删除-生产工序
        List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
                new LambdaQueryWrapper<PrdMoProcess>()
                        .eq(PrdMoProcess::getPid, prdMo.getId())
        );
        if (!oldProcesses.isEmpty()) {
            List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
            if (!prdMoProcessService.removeByIds(oldProcessIds)) {
                throw new BizException("更新失败，生产工序异常");
            }
        }
        // 删除-其他成本
        List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
                new LambdaQueryWrapper<PrdMoOtherCost>()
                        .eq(PrdMoOtherCost::getPid, prdMo.getId())
        );
        if (!oldOtherCosts.isEmpty()) {
            List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
            if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
                throw new BizException("更新失败，其他成本异常");
            }
        }
//        // 删除-成品尺寸
//        List<PrdMoFinSizeData> oldFinSizeDatas = prdMoFinSizeDataService.list(
//                new LambdaQueryWrapper<PrdMoFinSizeData>()
//                        .eq(PrdMoFinSizeData::getPid, prdMo.getId())
//        );
//        if (oldFinSizeDatas != null && oldFinSizeDatas.size() > 0) {
//            List<String> oldFinSizeDataIds = oldFinSizeDatas.stream().map(PrdMoFinSizeData::getId).collect(Collectors.toList());
//            if (oldFinSizeDataIds != null && oldFinSizeDataIds.size() > 0) {
//                if (!prdMoFinSizeDataService.removeByIds(oldFinSizeDataIds)) {
//                    throw new BizException("更新失败，成品尺寸异常");
//                }
//            }
//        }
        // 删除-二次工艺
        List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
                new LambdaQueryWrapper<PrdMoSecondaryProcess>()
                        .eq(PrdMoSecondaryProcess::getPid, prdMo.getId())
        );
        if (!oldSecondaryProcesses.isEmpty()) {
            List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
            if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
                throw new BizException("更新失败，二次工艺异常");
            }
        }
        // 删除-图片详情
        List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
                new LambdaQueryWrapper<PrdMoAttachImg>()
                        .eq(PrdMoAttachImg::getPid, prdMo.getId())
        );
        if (!oldAttachImgs.isEmpty()) {
            List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
            if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
                throw new BizException("更新失败，图片详情异常");
            }
        }
        // 删除-文件详情
        List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
                new LambdaQueryWrapper<PrdMoAttachFile>()
                        .eq(PrdMoAttachFile::getPid, prdMo.getId())
        );
        if (!oldAttachFiles.isEmpty()) {
            List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
            if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
                throw new BizException("更新失败，文件详情异常");
            }
        }

        IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();

        // 保存新数据
        // 颜色规格
        BigDecimal sumCount = BigDecimal.ZERO;
        List<PrdMoColorSpecData> moColorSpecDatas = prdMo.getMoColorSpecDatas();
        if (moColorSpecDatas != null && !moColorSpecDatas.isEmpty()) {
            // 获取关联原材料
            List<String> productIds = moColorSpecDatas.stream().map(PrdMoColorSpecData::getProductId).collect(Collectors.toList());
            BdMaterialRaw srhRaw = new BdMaterialRaw();
            srhRaw.setPids(productIds);
            List<BdMaterialRaw> raws = bdMaterialRawMapper.getList(srhRaw);

            List<PrdMoMaterialDetail> addDetails = new ArrayList<>();
            for (PrdMoColorSpecData moColorSpecData : moColorSpecDatas) {
                String id = identifierGenerator.nextId(new Object()).toString();
                moColorSpecData.setId(id);
                moColorSpecData.setPid(prdMo.getId());
                sumCount = sumCount.add(moColorSpecData.getQty());

                // 获取当前行关联原材料
                List<BdMaterialRaw> thisRaws = raws.stream().filter(r -> StringUtils.equals(r.getPid(), moColorSpecData.getProductId())).collect(Collectors.toList());
                for (BdMaterialRaw thisRaw : thisRaws) {
                    PrdMoMaterialDetail newDetail = new PrdMoMaterialDetail();
                    newDetail.setPid(id);
                    newDetail.setMaterialDetailId(thisRaw.getMaterialDetailId());
                    newDetail.setNumerator(thisRaw.getNumerator());
                    newDetail.setDenominator(thisRaw.getDenominator());

                    BigDecimal numerator = StringUtils.isNotNull(newDetail.getNumerator()) ? newDetail.getNumerator() : BigDecimal.ZERO;
                    BigDecimal denominator = StringUtils.isNotNull(newDetail.getDenominator()) ? newDetail.getDenominator() : BigDecimal.ZERO;
                    if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                        newDetail.setTheoryQty(BigDecimal.ZERO);
                    } else {
                        BigDecimal theoryQty = moColorSpecData.getQty().multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                        newDetail.setTheoryQty(theoryQty);
                    }

                    addDetails.add(newDetail);
                }
            }
            if (!prdMoColorSpecDataService.saveBatch(moColorSpecDatas)) {
                throw new BizException("保存失败，规格详情异常");
            }
            if (!addDetails.isEmpty()) {
                if (!prdMoMaterialDetailService.saveBatch(addDetails)) {
                    throw new BizException("保存失败，原料详情异常");
                }
            }
        }
        // 辅料
        List<PrdMoMaterialAux> moMaterialAuxes = prdMo.getMoMaterialAuxs();
        if (moMaterialAuxes != null && !moMaterialAuxes.isEmpty()) {
            for (PrdMoMaterialAux moMaterialAux : moMaterialAuxes) {
                moMaterialAux.setPid(prdMo.getId());
                BigDecimal numerator = StringUtils.isNotNull(moMaterialAux.getNumerator()) ? moMaterialAux.getNumerator() : BigDecimal.ZERO;
                BigDecimal denominator = StringUtils.isNotNull(moMaterialAux.getDenominator()) ? moMaterialAux.getDenominator() : BigDecimal.ZERO;
                if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                    moMaterialAux.setTheoryQty(BigDecimal.ZERO);
                } else {
                    BigDecimal theoryQty = sumCount.multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                    moMaterialAux.setTheoryQty(theoryQty);
                }
                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(moMaterialAux.getAuxMaterial().getId());
                srhMtrlDtl.setColor(moMaterialAux.getColor());
                srhMtrlDtl.setSpecification(moMaterialAux.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (!details.isEmpty()) {
                    moMaterialAux.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("辅料信息异常 编码：" + moMaterialAux.getAuxMaterial().getNumber() + " 名称：" + moMaterialAux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }
            if (!prdMoMaterialAuxService.saveBatch(moMaterialAuxes)) {
                throw new BizException("保存失败，辅料详情异常");
            }
        }
        // 生产工序
        List<PrdMoProcess> moProcesses = prdMo.getMoProcesses();
        if (moProcesses != null && moProcesses.size() > 0) {
            for (PrdMoProcess moProcess : moProcesses) {
                moProcess.setPid(prdMo.getId());
                moProcess.setQty(sumCount);
            }
            if (!prdMoProcessService.saveBatch(moProcesses)) {
                throw new BizException("保存失败，生产工序异常");
            }
        }

        // 其他成本
        List<Map<String, Object>> moOtherCostMaps = prdMo.getMoOtherCostMaps();
        List<PrdMoOtherCost> moOtherCosts = new ArrayList<>();
        if (moOtherCostMaps != null) {
            for (int i = 0; i < moOtherCostMaps.size(); i++) {
                Map<String, Object> map = moOtherCostMaps.get(i);
                for (String s : map.keySet()) {
                    if (!StringUtils.equals(s, "price")) {
                        PrdMoOtherCost newData = new PrdMoOtherCost();
                        newData.setPid(prdMo.getId());
                        newData.setName(s);
                        newData.setAmount(new BigDecimal(map.get(s).toString()));

                        moOtherCosts.add(newData);
                    }
                }
            }
        }
        if (moOtherCosts.size() > 0) {
            if (!prdMoOtherCostService.saveBatch(moOtherCosts)) {
                throw new BizException("保存失败，其他成本异常");
            }
        }

//        // 成品尺寸
//        List<Map<String, Object>> moFinSizeMaps = prdMo.getMoFinSizeMaps();
//        List<PrdMoFinSizeData> moFinSizeDatas = new ArrayList<>();
//        if (moFinSizeMaps != null) {
//            for (int i = 0; i < moFinSizeMaps.size(); i++) {
//                Map<String, Object> map = moFinSizeMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "rowIdx")) {
//                        PrdMoFinSizeData newData = new PrdMoFinSizeData();
//                        newData.setPid(prdMo.getId());
//                        newData.setRowIdx(Integer.valueOf(map.get("rowIdx").toString()));
//                        newData.setColumnName(s);
//
//                        String val = map.get(s) != null ? map.get(s).toString() : "";
//                        newData.setValue(val);
//
//                        moFinSizeDatas.add(newData);
//                    }
//                }
//            }
//        }
//        if (moFinSizeDatas.size() > 0) {
//            if (!prdMoFinSizeDataService.saveBatch(moFinSizeDatas)) {
//                throw new BizException("保存失败，成品尺寸异常");
//            }
//        }

        // 二次工艺
        List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMo.getMoSecondaryProcesses();
        if (moSecondaryProcesses != null && moSecondaryProcesses.size() > 0) {
            for (PrdMoSecondaryProcess moSecondaryProcess : moSecondaryProcesses) {
                moSecondaryProcess.setPid(prdMo.getId());
            }
            if (!prdMoSecondaryProcessService.saveBatch(moSecondaryProcesses)) {
                throw new BizException("保存失败，二次工艺异常");
            }
        }

        // 图片详情
        if (attachImgs != null && attachImgs.size() > 0) {
            for (PrdMoAttachImg attachImg : attachImgs) {
                attachImg.setPid(prdMo.getId());
            }

            if (!prdMoAttachImgService.saveBatch(attachImgs)) {
                throw new BizException("保存失败，异常码1");
            }
        }

        // 文件详情
        List<PrdMoAttachFile> attachFiles = prdMo.getAttachFiles();
        if (attachFiles != null && attachFiles.size() > 0) {
            for (PrdMoAttachFile attachFile : attachFiles) {
                attachFile.setPid(prdMo.getId());
            }

            if (!prdMoAttachFileService.saveBatch(attachFiles)) {
                throw new BizException("保存失败，异常码2");
            }
        }

        return prdMo;
    }

    Boolean chkIsUsed(List<String> ids) {
        // 裁床单
        List<PrdCutting> cuttings = prdCuttingMapper.selectList(
                new LambdaQueryWrapper<PrdCutting>()
                        .in(PrdCutting::getSrcId, ids)
        );
        if (cuttings != null && cuttings.size() > 0) {
            throw new BizException("当前生产订单已下达裁床单，无法删除");
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        PrdMo mo = this.getById(id);
        if (mo == null) {
            return true;
        }
        if (mo.getBillStatus().equals(Constants.INT_STATUS_APPROVING) || mo.getBillStatus().equals(Constants.INT_STATUS_AUDITED)) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("生产订单已下达裁床");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        // 删除-颜色规格
        List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
                new LambdaQueryWrapper<PrdMoColorSpecData>()
                        .eq(PrdMoColorSpecData::getPid, id)
        );
        if (oldColorSpecDatas != null && oldColorSpecDatas.size() > 0) {
            List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
            if (oldColorSpecDataIds != null && oldColorSpecDataIds.size() > 0) {
                if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
                    throw new BizException("删除失败，颜色规格异常");
                }
            }

            // 删除-原料详情
            List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
                    new LambdaQueryWrapper<PrdMoMaterialDetail>()
                            .in(PrdMoMaterialDetail::getPid, oldColorSpecDataIds)
            );
            if (oldMaterialDetails != null && oldMaterialDetails.size() > 0) {
                List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
                if (oldMaterialDetailIds != null && oldMaterialDetailIds.size() > 0) {
                    if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
                        throw new BizException("删除失败，原料详情异常");
                    }
                }
            }
        }
        // 删除辅料
        List<PrdMoMaterialAux> oldMoMaterialAuxes = prdMoMaterialAuxService.list(
                new LambdaQueryWrapper<PrdMoMaterialAux>()
                        .eq(PrdMoMaterialAux::getPid, id)
        );
        if (!oldMoMaterialAuxes.isEmpty()) {
            List<String> oldMoMaterialAuxIds = oldMoMaterialAuxes.stream().map(PrdMoMaterialAux::getId).collect(Collectors.toList());
            if (!prdMoMaterialAuxService.removeByIds(oldMoMaterialAuxIds)) {
                throw new BizException("更新失败，生产辅料异常");
            }
        }
        // 删除-生产工序
        List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
                new LambdaQueryWrapper<PrdMoProcess>()
                        .eq(PrdMoProcess::getPid, id)
        );
        if (oldProcesses != null && oldProcesses.size() > 0) {
            List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
            if (oldProcessIds != null && oldProcessIds.size() > 0) {
                if (!prdMoProcessService.removeByIds(oldProcessIds)) {
                    throw new BizException("删除失败，生产工序异常");
                }
            }
        }
        // 删除-其他成本
        List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
                new LambdaQueryWrapper<PrdMoOtherCost>()
                        .eq(PrdMoOtherCost::getPid, id)
        );
        if (oldOtherCosts != null && oldOtherCosts.size() > 0) {
            List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
            if (oldOtherCostIds != null && oldOtherCostIds.size() > 0) {
                if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
                    throw new BizException("删除失败，其他成本异常");
                }
            }
        }
        // 删除-成品尺寸
        List<PrdMoFinSizeData> oldFinSizeDatas = prdMoFinSizeDataService.list(
                new LambdaQueryWrapper<PrdMoFinSizeData>()
                        .eq(PrdMoFinSizeData::getPid, id)
        );
        if (oldFinSizeDatas != null && oldFinSizeDatas.size() > 0) {
            List<String> oldFinSizeDataIds = oldFinSizeDatas.stream().map(PrdMoFinSizeData::getId).collect(Collectors.toList());
            if (oldFinSizeDataIds != null && oldFinSizeDataIds.size() > 0) {
                if (!prdMoFinSizeDataService.removeByIds(oldFinSizeDataIds)) {
                    throw new BizException("删除失败，成品尺寸异常");
                }
            }
        }
        // 删除-二次工艺
        List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
                new LambdaQueryWrapper<PrdMoSecondaryProcess>()
                        .eq(PrdMoSecondaryProcess::getPid, id)
        );
        if (oldSecondaryProcesses != null && oldSecondaryProcesses.size() > 0) {
            List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
            if (oldSecondaryProcessIds != null && oldSecondaryProcessIds.size() > 0) {
                if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
                    throw new BizException("删除失败，二次工艺异常");
                }
            }
        }
        // 删除-图片详情
        List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
                new LambdaQueryWrapper<PrdMoAttachImg>()
                        .eq(PrdMoAttachImg::getPid, id)
        );
        if (oldAttachImgs != null && oldAttachImgs.size() > 0) {
            List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
            if (oldAttachImgIds != null && oldAttachImgIds.size() > 0) {
                if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
                    throw new BizException("删除失败，图片详情异常");
                }
            }
        }
        // 删除-文件详情
        List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
                new LambdaQueryWrapper<PrdMoAttachFile>()
                        .eq(PrdMoAttachFile::getPid, id)
        );
        if (oldAttachFiles != null && oldAttachFiles.size() > 0) {
            List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
            if (oldAttachFileIds != null && oldAttachFileIds.size() > 0) {
                if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
                    throw new BizException("删除失败，文件详情异常");
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<PrdMo> list = this.listByIds(idList);
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT).map(PrdMo::getId).collect(Collectors.toList());
            if (delIds != null && delIds.size() > 0) {
                if (this.chkIsUsed(delIds)) {
                    throw new BizException("生产订单已下达裁床");
                }

                if (!this.removeByIds(idList)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            // 删除-颜色规格
            List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
                    new LambdaQueryWrapper<PrdMoColorSpecData>()
                            .in(PrdMoColorSpecData::getPid, idList)
            );
            if (oldColorSpecDatas != null && oldColorSpecDatas.size() > 0) {
                List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
                if (oldColorSpecDataIds != null && oldColorSpecDataIds.size() > 0) {
                    if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
                        throw new BizException("删除失败，颜色规格异常");
                    }
                }

                // 删除-原料详情
                List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
                        new LambdaQueryWrapper<PrdMoMaterialDetail>()
                                .in(PrdMoMaterialDetail::getPid, oldColorSpecDataIds)
                );
                if (oldMaterialDetails != null && oldMaterialDetails.size() > 0) {
                    List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
                    if (oldMaterialDetailIds != null && oldMaterialDetailIds.size() > 0) {
                        if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
                            throw new BizException("删除失败，原料详情异常");
                        }
                    }
                }
            }
            // 删除辅料
            List<PrdMoMaterialAux> oldMoMaterialAuxes = prdMoMaterialAuxService.list(
                    new LambdaQueryWrapper<PrdMoMaterialAux>()
                            .in(PrdMoMaterialAux::getPid, idList)
            );
            if (!oldMoMaterialAuxes.isEmpty()) {
                List<String> oldMoMaterialAuxIds = oldMoMaterialAuxes.stream().map(PrdMoMaterialAux::getId).collect(Collectors.toList());
                if (!prdMoMaterialAuxService.removeByIds(oldMoMaterialAuxIds)) {
                    throw new BizException("更新失败，生产辅料异常");
                }
            }
            // 删除-生产工序
            List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
                    new LambdaQueryWrapper<PrdMoProcess>()
                            .in(PrdMoProcess::getPid, idList)
            );
            if (oldProcesses != null && oldProcesses.size() > 0) {
                List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
                if (oldProcessIds != null && oldProcessIds.size() > 0) {
                    if (!prdMoProcessService.removeByIds(oldProcessIds)) {
                        throw new BizException("删除失败，生产工序异常");
                    }
                }
            }
            // 删除-其他成本
            List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
                    new LambdaQueryWrapper<PrdMoOtherCost>()
                            .in(PrdMoOtherCost::getPid, idList)
            );
            if (oldOtherCosts != null && oldOtherCosts.size() > 0) {
                List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
                if (oldOtherCostIds != null && oldOtherCostIds.size() > 0) {
                    if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
                        throw new BizException("删除失败，其他成本异常");
                    }
                }
            }
            // 删除-成品尺寸
            List<PrdMoFinSizeData> oldFinSizeDatas = prdMoFinSizeDataService.list(
                    new LambdaQueryWrapper<PrdMoFinSizeData>()
                            .in(PrdMoFinSizeData::getPid, idList)
            );
            if (oldFinSizeDatas != null && oldFinSizeDatas.size() > 0) {
                List<String> oldFinSizeDataIds = oldFinSizeDatas.stream().map(PrdMoFinSizeData::getId).collect(Collectors.toList());
                if (oldFinSizeDataIds != null && oldFinSizeDataIds.size() > 0) {
                    if (!prdMoFinSizeDataService.removeByIds(oldFinSizeDataIds)) {
                        throw new BizException("删除失败，成品尺寸异常");
                    }
                }
            }
            // 删除-二次工艺
            List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
                    new LambdaQueryWrapper<PrdMoSecondaryProcess>()
                            .in(PrdMoSecondaryProcess::getPid, idList)
            );
            if (oldSecondaryProcesses != null && oldSecondaryProcesses.size() > 0) {
                List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
                if (oldSecondaryProcessIds != null && oldSecondaryProcessIds.size() > 0) {
                    if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
                        throw new BizException("删除失败，二次工艺异常");
                    }
                }
            }
            // 删除-图片详情
            List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
                    new LambdaQueryWrapper<PrdMoAttachImg>()
                            .in(PrdMoAttachImg::getPid, idList)
            );
            if (oldAttachImgs != null && oldAttachImgs.size() > 0) {
                List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
                if (oldAttachImgIds != null && oldAttachImgIds.size() > 0) {
                    if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
                        throw new BizException("删除失败，图片详情异常");
                    }
                }
            }
            // 删除-文件详情
            List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
                    new LambdaQueryWrapper<PrdMoAttachFile>()
                            .in(PrdMoAttachFile::getPid, idList)
            );
            if (oldAttachFiles != null && oldAttachFiles.size() > 0) {
                List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
                if (oldAttachFileIds != null && oldAttachFileIds.size() > 0) {
                    if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
                        throw new BizException("删除失败，文件详情异常");
                    }
                }
            }
        }
    }

    @Override
    public PrdMo detail(String id) {
        PrdMo result = baseMapper.infoById(id);
        if (result != null) {
            if (StringUtils.isNotEmpty(result.getTags())) {
                List<String> tagList = Arrays.asList(result.getTags().split(","));
                result.setTagList(tagList);
            }

            if (StringUtils.isNotEmpty(result.getMainPicId())) {
                SysFiles mainPic = sysFilesService.getById(result.getMainPicId());
                result.setMainPic(mainPic);
            }

            // 规格详情
            BigDecimal sumSurplusQty = BigDecimal.ZERO;
            List<String> rawDetailIds = new ArrayList<>();
            List<PrdMoMaterialDetail> moMaterialDetails = new ArrayList<>();
            PrdMoColorSpecData srhCSD = new PrdMoColorSpecData();
            srhCSD.setPid(id);
            List<PrdMoColorSpecData> moColorSpecDatas = prdMoColorSpecDataMapper.getList(srhCSD);
            if (!moColorSpecDatas.isEmpty()) {
                List<String> ids = moColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
                PrdMoMaterialDetail srhDetail = new PrdMoMaterialDetail();
                srhDetail.setPids(ids);
                List<PrdMoMaterialDetail> details = prdMoMaterialDetailMapper.getList(srhDetail);

                for (PrdMoColorSpecData moColorSpecData : moColorSpecDatas) {
                    BigDecimal surplusQty = moColorSpecData.getQty().subtract(moColorSpecData.getCuttingQty());
                    sumSurplusQty = sumSurplusQty.add(surplusQty);

                    if (!details.isEmpty()) {
                        List<PrdMoMaterialDetail> thisDetails = details.stream().filter(r -> StringUtils.equals(r.getPid(), moColorSpecData.getId())).collect(Collectors.toList());
                        for (PrdMoMaterialDetail thisDetail : thisDetails) {
                            thisDetail.setSurplusTheoryQty(BigDecimal.ZERO);

                            if (surplusQty.compareTo(BigDecimal.ZERO) == 1) {
                                BigDecimal numerator = StringUtils.isNotNull(thisDetail.getNumerator()) ? thisDetail.getNumerator() : BigDecimal.ZERO;
                                BigDecimal denominator = StringUtils.isNotNull(thisDetail.getDenominator()) ? thisDetail.getDenominator() : BigDecimal.ZERO;
                                if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                                    BigDecimal surplusTheoryQty = surplusQty.multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                                    thisDetail.setSurplusTheoryQty(surplusTheoryQty);
                                }
                            }

                            Boolean tf = false;
                            for (PrdMoMaterialDetail moMaterialDetail : moMaterialDetails) {
                                if (StringUtils.equals(moMaterialDetail.getMaterialDetailId(), thisDetail.getMaterialDetailId())) {
                                    moMaterialDetail.setTheoryQty(moMaterialDetail.getTheoryQty().add(thisDetail.getTheoryQty()));
                                    moMaterialDetail.setSurplusTheoryQty(moMaterialDetail.getSurplusTheoryQty().add(thisDetail.getSurplusTheoryQty()));

                                    tf = true;
                                }
                            }
                            if (tf == false) {
                                moMaterialDetails.add(thisDetail);
                            }

                            if (!rawDetailIds.contains(thisDetail.getMaterialDetailId())) {
                                rawDetailIds.add(thisDetail.getMaterialDetailId());
                            }
                        }
                    }
                }

                // 成品图片
                BdMaterialAttach bdMaterialAttach = new BdMaterialAttach();
                bdMaterialAttach.setPids(rawDetailIds);
                List<BdMaterialAttach> attaches = bdMaterialAttachMapper.getList(bdMaterialAttach);
                if (!attaches.isEmpty()) {
                    for (PrdMoColorSpecData moColorSpecData : moColorSpecDatas) {
                        List<BdMaterialAttach> myAttaches = attaches.stream().filter(r -> r.getPid().equals(moColorSpecData.getProductId())).collect(Collectors.toList());
                        moColorSpecData.setAttaches(myAttaches);

                        List<String> urls = new ArrayList<>();
                        if (myAttaches != null && myAttaches.size() > 0) {
                            for (BdMaterialAttach attach : myAttaches) {
                                urls.add(attach.getSysFile().getUrl());
                            }
                        }
                        moColorSpecData.setAttachUrls(urls);
                    }
                }
            }
            result.setMoColorSpecDatas(moColorSpecDatas);
            // 原材料
            if (!moMaterialDetails.isEmpty()) {
                InvInventory srhInv = new InvInventory();
                srhInv.setMaterialDetailIds(rawDetailIds);
                List<InvInventory> invs = invInventoryMapper.getList(srhInv);
                if (!invs.isEmpty()) {
                    for (PrdMoMaterialDetail moMaterialDetail : moMaterialDetails) {
                        List<InvInventory> thisInvs = invs.stream().filter(r -> StringUtils.equals(r.getMaterialDetailId(), moMaterialDetail.getMaterialDetailId())).collect(Collectors.toList());
                        if (!thisInvs.isEmpty()) {
                            BigDecimal reserveQty = thisInvs.stream().map(InvInventory::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            moMaterialDetail.setReserveQty(reserveQty);
                        } else {
                            moMaterialDetail.setReserveQty(BigDecimal.ZERO);
                        }
                    }
                }
            }
            result.setMoMaterialDetails(moMaterialDetails);

            // 辅料详情
            PrdMoMaterialAux srhAux = new PrdMoMaterialAux();
            srhAux.setPid(id);
            List<PrdMoMaterialAux> moMaterialAuxes = prdMoMaterialAuxMapper.getList(srhAux);
            if (!moMaterialAuxes.isEmpty()) {
                List<String> auxDetailIds = moMaterialAuxes.stream().map(PrdMoMaterialAux::getMaterialDetailId).collect(Collectors.toList());

                InvInventory srhInv = new InvInventory();
                srhInv.setMaterialDetailIds(auxDetailIds);
                List<InvInventory> invs = invInventoryMapper.getList(srhInv);

                for (PrdMoMaterialAux moMaterialAux : moMaterialAuxes) {
                    if (!invs.isEmpty()) {
                        List<InvInventory> thisInvs = invs.stream().filter(r -> StringUtils.equals(r.getMaterialDetailId(), moMaterialAux.getMaterialDetailId())).collect(Collectors.toList());
                        if (!thisInvs.isEmpty()) {
                            BigDecimal reserveQty = thisInvs.stream().map(InvInventory::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            moMaterialAux.setReserveQty(reserveQty);
                        } else {
                            moMaterialAux.setReserveQty(BigDecimal.ZERO);
                        }
                    }

                    moMaterialAux.setSurplusTheoryQty(BigDecimal.ZERO);

                    if (sumSurplusQty.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal numerator = StringUtils.isNotNull(moMaterialAux.getNumerator()) ? moMaterialAux.getNumerator() : BigDecimal.ZERO;
                        BigDecimal denominator = StringUtils.isNotNull(moMaterialAux.getDenominator()) ? moMaterialAux.getDenominator() : BigDecimal.ZERO;

                        if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                            BigDecimal surplusTheoryQty = sumSurplusQty.multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP);
                            moMaterialAux.setSurplusTheoryQty(surplusTheoryQty);
                        }
                    }
                }
            }
            result.setMoMaterialAuxs(moMaterialAuxes);

            // 生产工序
            List<PrdMoProcess> moProcesses = prdMoProcessMapper.listByPid(id);
            if (moProcesses != null && moProcesses.size() > 0) {
                result.setProcedureCount(moProcesses.size());
            } else {
                result.setProcedureCount(0);
            }
            result.setMoProcesses(moProcesses);

            // 其他成本
            List<PrdMoOtherCost> moOtherCosts = prdMoOtherCostService.list(
                    new LambdaQueryWrapper<PrdMoOtherCost>()
                            .eq(PrdMoOtherCost::getPid, id)
            );
            if (moOtherCosts != null && moOtherCosts.size() > 0) {
                List<String> moOtherCostLabels = moOtherCosts.stream().map(PrdMoOtherCost::getName).collect(Collectors.toList());
                result.setMoOtherCostLabels(moOtherCostLabels);

                List<Map<String, Object>> moOtherCostMaps = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();

                map.put("pid", id);
                map.put("price", "金额");

                for (int i = 0; i < moOtherCosts.size(); i++) {
                    PrdMoOtherCost thisItem = moOtherCosts.get(i);

                    map.put("customcosts_" + thisItem.getName(), thisItem.getAmount());
                }

                moOtherCostMaps.add(map);

                result.setMoOtherCostMaps(moOtherCostMaps);
            }

//            // 成品尺寸
//            List<PrdMoFinSizeData> moFinSizeDatas = prdMoFinSizeDataService.list(
//                    new LambdaQueryWrapper<PrdMoFinSizeData>()
//                            .eq(PrdMoFinSizeData::getPid, id)
//            );
//            if (moFinSizeDatas != null && moFinSizeDatas.size() > 0) {
//                List<String> moFinSizeLabels = moFinSizeDatas.stream().map(PrdMoFinSizeData::getColumnName).distinct().collect(Collectors.toList());
//                result.setMoFinSizeLabels(moFinSizeLabels);
//
//                List<Map<String, Object>> moFinSizeMaps = new ArrayList<>();
//                List<Integer> rowIdxs = moFinSizeDatas.stream().map(PrdMoFinSizeData::getRowIdx).distinct().collect(Collectors.toList());
//                if (rowIdxs != null && rowIdxs.size() > 0) {
//                    for (int i = 0; i < rowIdxs.size(); i++) {
//                        Integer thisRowIdx = rowIdxs.get(i);
//
//                        Map<String, Object> map = new HashMap<>();
//
//                        map.put("pid", id);
//                        map.put("rowIdx", thisRowIdx);
//
//                        List<PrdMoFinSizeData> thisItems = moFinSizeDatas.stream().filter(r -> r.getRowIdx() == thisRowIdx).collect(Collectors.toList());
//                        if (thisItems != null && thisItems.size() > 0) {
//                            for (int j = 0; j < thisItems.size(); j++) {
//                                PrdMoFinSizeData thisItem = thisItems.get(j);
//
//                                map.put("customSpecs_" + thisItem.getColumnName(), thisItem.getValue());
//                            }
//                        }
//
//                        moFinSizeMaps.add(map);
//                    }
//                }
//                result.setMoFinSizeMaps(moFinSizeMaps);
//            }

            // 二次工艺
            List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMoSecondaryProcessMapper.listByPid(id);
            result.setMoSecondaryProcesses(moSecondaryProcesses);

            // 图片详情
            PrdMoAttachImg prdMoAttachImg = new PrdMoAttachImg();
            prdMoAttachImg.setPid(result.getId());
            List<PrdMoAttachImg> attachImgs = prdMoAttachImgMapper.getList(prdMoAttachImg);
            result.setAttachImgs(attachImgs);

            // 文件详情
            PrdMoAttachFile prdMoAttachFile = new PrdMoAttachFile();
            prdMoAttachFile.setPid(result.getId());
            List<PrdMoAttachFile> attachFiles = prdMoAttachFileMapper.getList(prdMoAttachFile);
            result.setAttachFiles(attachFiles);

            // 计算其他订单用量
            // 辅料
            List<PrdMoMaterialAux> myAuxs = result.getMoMaterialAuxs();
            if (myAuxs != null && !myAuxs.isEmpty()) {
                List<String> auxDtlIds = myAuxs.stream().map(PrdMoMaterialAux::getMaterialDetailId).distinct().collect(Collectors.toList());
                PrdMoMaterialAux srhMyAux = new PrdMoMaterialAux();
                srhMyAux.setMaterialDetailIds(auxDtlIds);
                srhMyAux.setPid(result.getId());
                List<PrdMoMaterialAux> otherAuxs = prdMoMaterialAuxMapper.getListOtherBill(srhMyAux);
                if (!otherAuxs.isEmpty()) {
                    List<String> pids = otherAuxs.stream().map(PrdMoMaterialAux::getPid).distinct().collect(Collectors.toList());
                    PrdMoColorSpecData srhOtherCSD = new PrdMoColorSpecData();
                    srhOtherCSD.setPids(pids);
                    List<PrdMoColorSpecData> otherCSDs = prdMoColorSpecDataMapper.getList(srhOtherCSD);
                    if (!otherCSDs.isEmpty()) {
                        for (String pid : pids) {
                            BigDecimal thisSumQty = BigDecimal.ZERO;
                            List<PrdMoColorSpecData> thisOtherCSDs = otherCSDs.stream().filter(r -> StringUtils.equals(r.getPid(), pid)).collect(Collectors.toList());
                            if (!thisOtherCSDs.isEmpty()) {
                                for (PrdMoColorSpecData thisOtherCSD : thisOtherCSDs) {
                                    if (thisOtherCSD.getQty().compareTo(thisOtherCSD.getCuttingQty()) >= 0) {
                                        thisSumQty = thisSumQty.add(thisOtherCSD.getQty().subtract(thisOtherCSD.getCuttingQty()));
                                    }
                                }
                            }

                            List<PrdMoMaterialAux> thisOtherAuxs = otherAuxs.stream().filter(r -> StringUtils.equals(r.getPid(), pid)).collect(Collectors.toList());
                            if (!thisOtherAuxs.isEmpty()) {
                                for (PrdMoMaterialAux thisOtherAux : thisOtherAuxs) {
                                    BigDecimal denominator = StringUtils.isNotNull(thisOtherAux.getDenominator()) ? thisOtherAux.getDenominator() : BigDecimal.ZERO;
                                    BigDecimal numerator = StringUtils.isNotNull(thisOtherAux.getNumerator()) ? thisOtherAux.getNumerator() : BigDecimal.ZERO;

                                    if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                                        thisOtherAux.setSurplusTheoryQty(thisSumQty.multiply(numerator).divide(denominator, 3, BigDecimal.ROUND_HALF_UP));
                                    } else {
                                        thisOtherAux.setSurplusTheoryQty(BigDecimal.ZERO);
                                    }
                                }
                            }
                        }
                    }

                    for (PrdMoMaterialAux myAux : myAuxs) {
                        List<PrdMoMaterialAux> myOtherAuxs = otherAuxs.stream().filter(r -> StringUtils.equals(r.getMaterialDetailId(), myAux.getMaterialDetailId())).collect(Collectors.toList());
                        BigDecimal myOtherBillQty = myOtherAuxs.stream().map(PrdMoMaterialAux::getSurplusTheoryQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        myAux.setOtherBillQty(myOtherBillQty);
                    }
                }
            }

            // 原材料
            List<PrdMoMaterialDetail> myDetails = result.getMoMaterialDetails();
            if (!myDetails.isEmpty()) {
                List<String> rawDtlIds = myDetails.stream().map(PrdMoMaterialDetail::getMaterialDetailId).distinct().collect(Collectors.toList());
                PrdMoMaterialDetail srhMyRaw = new PrdMoMaterialDetail();
                srhMyRaw.setMaterialDetailIds(rawDtlIds);
                srhMyRaw.setMoId(result.getId());
                List<PrdMoMaterialDetail> otherRaws = prdMoMaterialDetailMapper.getListOtherBill(srhMyRaw);
                if (!otherRaws.isEmpty()) {
                    for (PrdMoMaterialDetail myDetail : myDetails) {
                        List<PrdMoMaterialDetail> myOtherRaws = otherRaws.stream().filter(r -> StringUtils.equals(r.getMaterialDetailId(), myDetail.getMaterialDetailId())).collect(Collectors.toList());
                        BigDecimal myOtherBillQty = myOtherRaws.stream().map(PrdMoMaterialDetail::getSurplusTheoryQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        myDetail.setOtherBillQty(myOtherBillQty);
                    }
                }
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdMo submit(String id) throws Exception {
        PrdMo result = this.detail(id);
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
    public PrdMo auditUptData(PrdMo prdMo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        prdMo.setBillStatus(Constants.INT_STATUS_AUDITED);
        prdMo.setAuditTime(sdf.format(new Date()));
        prdMo.setAuditorId(RequestUtils.getUserId());
        prdMo.setAuditor(RequestUtils.getNickname());

        return prdMo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PrdMo> list = this.list(
                new LambdaQueryWrapper<PrdMo>()
                        .in(PrdMo::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<PrdMo> submitList = list.stream().filter(r -> (r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (PrdMo prdMo : submitList) {
                    this.submit(prdMo.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(PrdMo prdMo) throws Exception {
        if (prdMo.getBillStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(prdMo.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(prdMo.getWorkFlowId());
            flowOperationInfo.setFormData(prdMo);
            flowOperationInfo.setUserId(prdMo.getUserId());
            flowOperationInfo.setChildNodes(prdMo.getChildNodes());
            flowOperationInfo.setCurrentNodeId(prdMo.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(prdMo.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                prdMo.setWorkFlowId("");
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
            ids.add(prdMo.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, prdMo.getWorkFlow().getId());
            prdMo.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            prdMo.setNodeStatus(currentNodes.get(0).getStatus());
            prdMo.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(prdMo.getId()) == 1) {
                prdMo = this.auditUptData(prdMo);
            }
            // 驳回
            if (circulationOperationService.whetherLast(prdMo.getId()) == 2) {
                prdMo.setBillStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(prdMo)) {
            throw new BizException("操作失败");
        }

        return Result.success(prdMo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(PrdMo prdMo) throws Exception {
        List<String> ids = prdMo.getIds();
        List<PrdMo> mos = this.list(
                new LambdaQueryWrapper<PrdMo>()
                        .in(PrdMo::getId, ids)
        );
        if (mos != null && mos.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, prdMo.getWorkFlow().getId());
            for (int i = 0; i < mos.size(); i++) {
                PrdMo item = mos.get(i);
                item.setBillStatus(prdMo.getBillStatus());
                item.setWorkFlowId(prdMo.getWorkFlowId());
                item.setUserId(prdMo.getUserId());
                item.setChildNodes(prdMo.getChildNodes());
                item.setChildNodeApprovalResult(prdMo.getChildNodeApprovalResult());
                item.setWorkFlow(prdMo.getWorkFlow());
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
    public PrdMo unAudit(String id) throws Exception {
        PrdMo result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getBillStatus() != Constants.INT_STATUS_AUDITED) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        result = this.unAuditUptData(result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PrdMo unAuditUptData(PrdMo prdMo) throws Exception {
        prdMo.setBillStatus(Constants.INT_STATUS_RESUBMIT);
        prdMo.setAuditTime(null);
        prdMo.setAuditorId(null);
        prdMo.setAuditor(null);
        if (!this.updateById(prdMo)) {
            throw new BizException("反审核失败");
        }

        return prdMo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<PrdMo> list = this.listByIds(idList);
        if (list != null && list.size() > 0) {
            List<PrdMo> unAuditList = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (PrdMo prdMo : unAuditList) {
                    this.unAudit(prdMo.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public PrdMo top(String id) {
        PrdMo prdMo = this.getById(id);
        if (prdMo != null) {
            prdMo.setTop(0);

            if (!this.updateById(prdMo)) {
                throw new BizException("置顶失败");
            }
        }

        return prdMo;
    }

    @Override
    public PrdMo unTop(String id) {
        PrdMo prdMo = this.getById(id);
        if (prdMo != null) {
            prdMo.setTop(1);

            if (!this.updateById(prdMo)) {
                throw new BizException("取消置顶失败");
            }
        }

        return prdMo;
    }

    @Override
    public void exportPrdMo(HttpServletResponse response, PrdMo prdMo) {
        List<PrdMoDTO> prdMoDTOList = baseMapper.getPrdMoDTO(prdMo);
        Set<String> auxIds = prdMoDTOList.stream().map(PrdMoDTO::getAuxId).collect(Collectors.toSet());
        Set<String> detailIds = prdMoDTOList.stream().map(PrdMoDTO::getDetailId).collect(Collectors.toSet());
        List<String> ids = new ArrayList<>();
        ids.addAll(auxIds);
        ids.addAll(detailIds);
        List<BdMaterialDetail> bdMaterialDetails = bdMaterialDetailService.listDetailByIds(ids);

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
        title.add("编号");
        title.add("单据类型");
        title.add("交货日期");
        title.add("跟单员");
        title.add("预开工日");
        title.add("预完工日");
        title.add("负责人");
        title.add("标签");
        title.add("大货款号");
        title.add("商家编码");
        title.add("颜色");
        title.add("规格");
        title.add("数量");
        title.add("工序名称");
        title.add("原材料编码");
        title.add("原材料名称");
        title.add("原材料颜色");
        title.add("原材料规格");
        title.add("原材料单位");
        title.add("原材料用量");
        title.add("辅料编码");
        title.add("辅料名称");
        title.add("辅料颜色");
        title.add("辅料规格");
        title.add("辅料单位");
        title.add("辅料用量");
        for (int i = 0; i < title.size(); i++) {
            HSSFCell cel = row2.createCell(i);
            cel.setCellStyle(cellStyle);
            cel.setCellValue(title.get(i));
        }
        for (int j = 0, recordsSize = prdMoDTOList.size(); j < recordsSize; j++) {
            PrdMoDTO record = prdMoDTOList.get(j);
            HSSFRow rowData = sheet.createRow(sheet.getLastRowNum() + 1);
            rowData.setHeightInPoints(15);
            rowData.createCell(0).setCellValue(j + 1);
            rowData.createCell(1).setCellValue(record.getNo());
            rowData.createCell(2).setCellValue(record.getBillType());
            rowData.createCell(3).setCellValue(record.getDeliveryDate());
            rowData.createCell(4).setCellValue(record.getFollower());
            rowData.createCell(5).setCellValue(record.getPreBeginDate());
            rowData.createCell(6).setCellValue(record.getPreEndDate());
            rowData.createCell(7).setCellValue(record.getLeader());
            rowData.createCell(8).setCellValue(record.getTags());
            rowData.createCell(9).setCellValue(record.getProductName());
            rowData.createCell(10).setCellValue(record.getProductNumber());
            rowData.getCell(0).setCellStyle(cellStyleContent);
            rowData.getCell(1).setCellStyle(cellStyleContent);
            rowData.getCell(2).setCellStyle(cellStyleContent);
            rowData.getCell(3).setCellStyle(cellStyleContent);
            rowData.getCell(4).setCellStyle(cellStyleContent);
            rowData.getCell(5).setCellStyle(cellStyleContent);
            rowData.getCell(6).setCellStyle(cellStyleContent);
            rowData.getCell(7).setCellStyle(cellStyleContent);
            rowData.getCell(8).setCellStyle(cellStyleContent);
            rowData.getCell(9).setCellStyle(cellStyleContent);
            rowData.getCell(10).setCellStyle(cellStyleContent);
        }
        ExcelUtils.buildXlsxDocument("生产订单", workbook, response);
    }

    @Override
    public boolean closeOrder(String id) {
        PrdMo prdMo = this.getById(id);
        if (prdMo == null) {
            throw new BizException("未检索到生产订单");
        }
        if (prdMo.getCloseStatus()) {
            throw new BizException("订单已关闭，请勿重复关闭");
        }

        prdMo.setCloseStatus(true);
        return this.updateById(prdMo);
    }
}
