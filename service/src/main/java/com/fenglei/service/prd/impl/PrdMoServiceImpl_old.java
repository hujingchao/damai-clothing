//package com.fenglei.service.prd.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.fenglei.common.constant.Constants;
//import com.fenglei.common.exception.BizException;
//import com.fenglei.common.result.Result;
//import com.fenglei.common.result.ResultCode;
//import com.fenglei.common.util.RequestUtils;
//import com.fenglei.common.util.StringUtils;
//import com.fenglei.mapper.prd.*;
//import com.fenglei.model.basedata.BdTag;
//import com.fenglei.model.prd.entity.*;
//import com.fenglei.model.system.entity.SysFiles;
//import com.fenglei.model.workFlow.dto.FlowDepartment;
//import com.fenglei.model.workFlow.dto.FlowOperationInfo;
//import com.fenglei.model.workFlow.dto.FlowRole;
//import com.fenglei.model.workFlow.dto.FlowUser;
//import com.fenglei.model.workFlow.entity.ChildNode;
//import com.fenglei.service.basedata.BdTagService;
//import com.fenglei.service.prd.*;
//import com.fenglei.service.system.SysFilesService;
//import com.fenglei.service.workFlow.CirculationOperationService;
//import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class PrdMoServiceImpl_old extends ServiceImpl<PrdMoMapper, PrdMo> implements PrdMoService {
//
//    @Resource
//    private PrdMoColorSpecDataService prdMoColorSpecDataService;
//    @Resource
//    private PrdMoProcessService prdMoProcessService;
//    @Resource
//    private PrdMoMaterialDetailService prdMoMaterialDetailService;
//    @Resource
//    private PrdMoOtherCostService prdMoOtherCostService;
//    @Resource
//    private PrdMoFinSizeDataService prdMoFinSizeDataService;
//    @Resource
//    private PrdMoSecondaryProcessService prdMoSecondaryProcessService;
//    @Resource
//    private PrdMoAttachImgService prdMoAttachImgService;
//    @Resource
//    private PrdMoAttachFileService prdMoAttachFileService;
//    @Resource
//    private SysFilesService sysFilesService;
//    @Resource
//    private BdTagService bdTagService;
//
//    @Resource
//    private SystemInformationAcquisitionService systemInformationAcquisitionService;
//    @Resource
//    private CirculationOperationService circulationOperationService;
//
//    @Resource
//    private PrdMoAttachImgMapper prdMoAttachImgMapper;
//    @Resource
//    private PrdMoAttachFileMapper prdMoAttachFileMapper;
//    @Resource
//    private PrdMoColorSpecDataMapper prdMoColorSpecDataMapper;
//    @Resource
//    private PrdMoProcessMapper prdMoProcessMapper;
//    @Resource
//    private PrdMoMaterialDetailMapper prdMoMaterialDetailMapper;
//    @Resource
//    private PrdMoSecondaryProcessMapper prdMoSecondaryProcessMapper;
//    @Resource
//    private PrdCuttingMapper prdCuttingMapper;
//
//    @Override
//    public IPage<PrdMo> myPage(Page page, PrdMo prdMo) {
//        IPage<PrdMo> iPage = baseMapper.getPage(page, prdMo);
//        if (iPage != null) {
//            List<PrdMo> records = iPage.getRecords();
//            if (records != null && records.size() > 0) {
//
//                Integer moCount = this.count(
//                        new LambdaQueryWrapper<PrdMo>()
//                                .eq(PrdMo::getBillType, "生产订单")
//                );
//
//                Integer planCount = this.count(
//                        new LambdaQueryWrapper<PrdMo>()
//                                .eq(PrdMo::getBillType, "计划单")
//                );
//
//                // 款数
//                Integer finMtrlCount = baseMapper.getFinMtrlCount(prdMo);
//                // 单数
//                Integer count = baseMapper.getCount(prdMo);
//                // 件数
//                BigDecimal allQty = prdMoColorSpecDataMapper.getAllQty(prdMo);
//                // 裁床数
//                BigDecimal allCuttingQty = prdMoColorSpecDataMapper.getCuttingQty(prdMo);
//                // 入库数量
//                BigDecimal allInstockQty = prdMoColorSpecDataMapper.getInstockQty(prdMo);
//                // 入库工序总价
//                BigDecimal allInstockProcessAmt = prdMoColorSpecDataMapper.getAllInstockProcessAmt(prdMo);
//                // 生产中工序总价
//                // 入库工序总价
//                BigDecimal allIngProcessAmt = prdMoColorSpecDataMapper.getAllIngProcessAmt(prdMo);
//
//                for (PrdMo record : records) {
//                    if (StringUtils.isNotEmpty(record.getTags())) {
//                        List<String> tagIds = Arrays.asList(record.getTags().split(","));
//                        List<BdTag> tags = bdTagService.listByIds(tagIds);
//                        if (tags != null && tags.size() > 0) {
//                            List<String> tagList = tags.stream().map(BdTag::getName).distinct().collect(Collectors.toList());
//                            record.setTagList(tagList);
//                        }
//                    }
//
//                    if (StringUtils.isNotEmpty(record.getMainPicId())) {
//                        SysFiles mainPic = sysFilesService.getById(record.getMainPicId());
//                        record.setMainPic(mainPic);
//                    }
//
//                    record.setMoCount(moCount);
//                    record.setPlanCount(planCount);
//
//                    record.setFinMtrlCount(finMtrlCount);
//                    record.setCount(count);
//                    record.setAllQty(allQty);
//                    record.setAllCuttingQty(allCuttingQty);
//                    record.setAllInstockQty(allInstockQty);
//                    record.setAllInstockProcessAmt(allInstockProcessAmt);
//                    record.setAllIngQty(allCuttingQty.subtract(allInstockQty));
//                    record.setAllIngProcessAmt(allIngProcessAmt);
//
//                    // 图片详情
//                    PrdMoAttachImg prdMoAttachImg = new PrdMoAttachImg();
//                    prdMoAttachImg.setPid(record.getId());
//                    List<PrdMoAttachImg> attachImgs = prdMoAttachImgMapper.getList(prdMoAttachImg);
//                    if (attachImgs != null && attachImgs.size() > 0) {
//                        List<String> attachImgUrls = new ArrayList<>();
//                        for (PrdMoAttachImg attachImg : attachImgs) {
//                            if (attachImg.getSysFile() != null) {
//                                attachImgUrls.add(attachImg.getSysFile().getUrl());
//                            }
//                        }
//                        record.setAttachImgUrls(attachImgUrls);
//                    }
//                    record.setAttachImgs(attachImgs);
//
//                    List<PrdMoColorSpecData> colorSpecDatas = prdMoColorSpecDataService.list(
//                            new LambdaQueryWrapper<PrdMoColorSpecData>()
//                                    .eq(PrdMoColorSpecData::getPid, record.getId())
//                    );
//                    if (colorSpecDatas != null && colorSpecDatas.size() > 0) {
//                        BigDecimal sumQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
//                        record.setSumQty(sumQty);
//
//                        BigDecimal cuttingQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getCuttingQty).reduce(BigDecimal.ZERO, BigDecimal::add);
//                        record.setCuttingQty(cuttingQty);
//                    } else {
//                        record.setSumQty(BigDecimal.ZERO);
//                        record.setCuttingQty(BigDecimal.ZERO);
//                    }
//                    allQty = allQty.add(record.getSumQty());
//
//                    List<PrdMoProcess> processes = prdMoProcessMapper.listByPid(record.getId());
//                    if (processes != null && processes.size() > 0) {
//                        BigDecimal sumPrice = BigDecimal.ZERO;
//                        for (PrdMoProcess process : processes) {
//                            if (process.getPrice() != null) {
//                                sumPrice = sumPrice.add(process.getPrice());
//                            }
//                        }
//                        BigDecimal procedureSumAmt = record.getSumQty().multiply(sumPrice);
//                        record.setProcedureSumAmount(procedureSumAmt);
//                        record.setProcedureCount(processes.size());
//                    } else {
//                        record.setProcedureSumAmount(BigDecimal.ZERO);
//                        record.setProcedureCount(0);
//                    }
//
//                }
//            }
//        }
//
//        return iPage;
//    }
//
//    @Override
//    public List<PrdMo> myList(PrdMo prdMo) {
//        List<PrdMo> list = baseMapper.getList(prdMo);
//        if (list != null && list.size() > 0) {
//            for (PrdMo record : list) {
//                if (StringUtils.isNotEmpty(record.getTags())) {
//                    List<String> tagIds = Arrays.asList(record.getTags().split(","));
//                    List<BdTag> tags = bdTagService.listByIds(tagIds);
//                    if (tags != null && tags.size() > 0) {
//                        List<String> tagList = tags.stream().map(BdTag::getName).distinct().collect(Collectors.toList());
//                        record.setTagList(tagList);
//                    }
//                }
//
//                if (StringUtils.isNotEmpty(record.getMainPicId())) {
//                    SysFiles mainPic = sysFilesService.getById(record.getMainPicId());
//                    record.setMainPic(mainPic);
//                }
//
//                List<PrdMoColorSpecData> colorSpecDatas = prdMoColorSpecDataService.list(
//                        new LambdaQueryWrapper<PrdMoColorSpecData>()
//                                .eq(PrdMoColorSpecData::getPid, record.getId())
//                );
//                if (colorSpecDatas != null && colorSpecDatas.size() > 0) {
//                    BigDecimal sumQty = colorSpecDatas.stream().map(PrdMoColorSpecData::getQty).reduce(BigDecimal.ZERO, BigDecimal::add);
//                    record.setSumQty(sumQty);
//                } else {
//                    record.setSumQty(BigDecimal.ZERO);
//                }
//
//
//                List<PrdMoProcess> processes = prdMoProcessMapper.listByPid(record.getId());
//                if (processes != null && processes.size() > 0) {
//                    BigDecimal sumPrice = BigDecimal.ZERO;
//                    for (PrdMoProcess process : processes) {
//                        if (process.getPrice() != null) {
//                            sumPrice = sumPrice.add(process.getPrice());
//                        }
//                    }
//                    BigDecimal procedureSumAmt = record.getSumQty().multiply(sumPrice);
//                    record.setProcedureSumAmount(procedureSumAmt);
//                    record.setProcedureCount(processes.size());
//                } else {
//                    record.setProcedureSumAmount(BigDecimal.ZERO);
//                    record.setProcedureCount(0);
//                }
//
//            }
//        }
//
//        return list;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public PrdMo add(PrdMo prdMo) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        if (StringUtils.isNotEmpty(prdMo.getNo())) {
//            List<PrdMo> list = this.list(
//                    new LambdaQueryWrapper<PrdMo>()
//                            .eq(PrdMo::getNo, prdMo.getNo())
//            );
//            if (list != null && list.size() > 0) {
//                throw new BizException("已存在相同款号的生产订单，请修改");
//            }
//        } else {
//            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH) + 1;
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//            String no = "MO" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//            List<PrdMo> list = this.list(
//                    new LambdaQueryWrapper<PrdMo>()
//                            .likeRight(PrdMo::getNo, no)
//                            .orderByDesc(PrdMo::getNo)
//            );
//            if (list != null && list.size() > 0) {
//                String maxNo = list.get(0).getNo();
//                Integer pos = maxNo.lastIndexOf("-");
//                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
//                Integer maxNoInt = Integer.valueOf(maxIdxStr);
//                String noIdxStr = String.format("%04d", maxNoInt + 1);
//                no = no + noIdxStr;
//            } else {
//                no = no + "0001";
//            }
//            prdMo.setNo(no);
//        }
//
//        prdMo.setBillStatus(Constants.INT_STATUS_CREATE);
//        prdMo.setCreateTime(sdf.format(new Date()));
//        prdMo.setCreatorId(RequestUtils.getUserId());
//        prdMo.setCreator(RequestUtils.getNickname());
//
//        List<String> tagList = prdMo.getTagList();
//        if (tagList != null && tagList.size() > 0) {
//            String tags = String.join(",", tagList);
//            prdMo.setTags(tags);
//        }
//
//        // 主图
//        List<PrdMoAttachImg> attachImgs = prdMo.getAttachImgs();
//        if (attachImgs != null && attachImgs.size() > 0) {
//            prdMo.setMainPicId(attachImgs.get(0).getAttachId());
//        }
//
//        if (!this.save(prdMo)) {
//            throw new BizException("保存失败");
//        }
//
//        // 颜色规格
//        BigDecimal sumCount = BigDecimal.ZERO;
//        List<Map<String, Object>> moColorSpecMaps = prdMo.getMoColorSpecMaps();
//        List<PrdMoColorSpecData> moColorSpecDatas = new ArrayList<>();
//        if (moColorSpecMaps != null) {
//            for (int i = 0; i < moColorSpecMaps.size(); i++) {
//                Map<String, Object> map = moColorSpecMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "color")) {
//                        PrdMoColorSpecData newData = new PrdMoColorSpecData();
//                        newData.setPid(prdMo.getId());
//                        newData.setColor(map.get("color").toString());
//                        newData.setSpecification(s);
//                        newData.setQty(new BigDecimal(map.get(s).toString()));
//
//                        sumCount = sumCount.add(newData.getQty());
//
//                        moColorSpecDatas.add(newData);
//                    }
//                }
//            }
//        }
//        if (moColorSpecDatas.size() > 0) {
//            if (!prdMoColorSpecDataService.saveBatch(moColorSpecDatas)) {
//                throw new BizException("保存失败，规格详情异常");
//            }
//        }
//
//        // 生产工序
//        List<PrdMoProcess> moProcesses = prdMo.getMoProcesses();
//        if (moProcesses != null && moProcesses.size() > 0) {
//            for (PrdMoProcess moProcess : moProcesses) {
//                moProcess.setPid(prdMo.getId());
//                moProcess.setQty(sumCount);
//            }
//            if (!prdMoProcessService.saveBatch(moProcesses)) {
//                throw new BizException("保存失败，生产工序异常");
//            }
//        }
//
//        // 其他成本
//        List<Map<String, Object>> moOtherCostMaps = prdMo.getMoOtherCostMaps();
//        List<PrdMoOtherCost> moOtherCosts = new ArrayList<>();
//        if (moOtherCostMaps != null) {
//            for (int i = 0; i < moOtherCostMaps.size(); i++) {
//                Map<String, Object> map = moOtherCostMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "price")) {
//                        PrdMoOtherCost newData = new PrdMoOtherCost();
//                        newData.setPid(prdMo.getId());
//                        newData.setName(s);
//                        newData.setAmount(new BigDecimal(map.get(s).toString()));
//
//                        moOtherCosts.add(newData);
//                    }
//                }
//            }
//        }
//        if (moOtherCosts.size() > 0) {
//            if (!prdMoOtherCostService.saveBatch(moOtherCosts)) {
//                throw new BizException("保存失败，其他成本异常");
//            }
//        }
//
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
//
//        // 原料详情
//        List<PrdMoMaterialDetail> moMaterialDetails = prdMo.getMoMaterialDetails();
//        if (moMaterialDetails != null && moMaterialDetails.size() > 0) {
//            for (PrdMoMaterialDetail moMaterialDetail : moMaterialDetails) {
//                moMaterialDetail.setPid(prdMo.getId());
//            }
//            if (!prdMoMaterialDetailService.saveBatch(moMaterialDetails)) {
//                throw new BizException("保存失败，原料详情异常");
//            }
//        }
//
//        // 二次工艺
//        List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMo.getMoSecondaryProcesses();
//        if (moSecondaryProcesses != null && moSecondaryProcesses.size() > 0) {
//            for (PrdMoSecondaryProcess moSecondaryProcess : moSecondaryProcesses) {
//                moSecondaryProcess.setPid(prdMo.getId());
//            }
//            if (!prdMoSecondaryProcessService.saveBatch(moSecondaryProcesses)) {
//                throw new BizException("保存失败，二次工艺异常");
//            }
//        }
//
//        // 图片详情
//        if (attachImgs != null && attachImgs.size() > 0) {
//            for (PrdMoAttachImg attachImg : attachImgs) {
//                attachImg.setPid(prdMo.getId());
//            }
//
//            if (!prdMoAttachImgService.saveBatch(attachImgs)) {
//                throw new BizException("保存失败，异常码1");
//            }
//        }
//
//        // 文件详情
//        List<PrdMoAttachFile> attachFiles = prdMo.getAttachFiles();
//        if (attachFiles != null && attachFiles.size() > 0) {
//            for (PrdMoAttachFile attachFile : attachFiles) {
//                attachFile.setPid(prdMo.getId());
//            }
//
//            if (!prdMoAttachFileService.saveBatch(attachFiles)) {
//                throw new BizException("保存失败，异常码2");
//            }
//        }
//
//        return prdMo;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public PrdMo myUpdate(PrdMo prdMo) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        if (StringUtils.isNotEmpty(prdMo.getNo())) {
//            List<PrdMo> list = this.list(
//                    new LambdaQueryWrapper<PrdMo>()
//                            .eq(PrdMo::getNo, prdMo.getNo())
//                            .ne(PrdMo::getId, prdMo.getId())
//            );
//            if (list != null && list.size() > 0) {
//                throw new BizException("已存在相同款号的生产订单，请修改，异常码1");
//            }
//        } else {
//            PrdMo thisMo = this.getById(prdMo.getId());
//            if (thisMo != null) {
//                prdMo.setNo(thisMo.getNo());
//
//                List<PrdMo> list = this.list(
//                        new LambdaQueryWrapper<PrdMo>()
//                                .eq(PrdMo::getNo, prdMo.getNo())
//                                .ne(PrdMo::getId, prdMo.getId())
//                );
//                if (list != null && list.size() > 0) {
//                    throw new BizException("已存在相同款号的生产订单，请修改，异常码2");
//                }
//            } else {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH) + 1;
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//                String no = "MO" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//                List<PrdMo> list = this.list(
//                        new LambdaQueryWrapper<PrdMo>().likeRight(PrdMo::getNo, no)
//                                .orderByDesc(PrdMo::getNo)
//                );
//                if (list != null && list.size() > 0) {
//                    String maxNo = list.get(0).getNo();
//                    Integer pos = maxNo.lastIndexOf("-");
//                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
//                    Integer maxNoInt = Integer.valueOf(maxIdxStr);
//                    String noIdxStr = String.format("%04d", maxNoInt + 1);
//                    no = no + noIdxStr;
//                } else {
//                    no = no + "0001";
//                }
//                prdMo.setNo(no);
//            }
//        }
//
//        prdMo.setUpdateTime(sdf.format(new Date()));
//        prdMo.setUpdaterId(RequestUtils.getUserId());
//        prdMo.setUpdater(RequestUtils.getNickname());
//
//        List<String> tagList = prdMo.getTagList();
//        if (tagList != null && tagList.size() > 0) {
//            String tags = String.join(",", tagList);
//            prdMo.setTags(tags);
//        }
//
//        // 主图
//        List<PrdMoAttachImg> attachImgs = prdMo.getAttachImgs();
//        if (attachImgs != null && attachImgs.size() > 0) {
//            prdMo.setMainPicId(attachImgs.get(0).getAttachId());
//        }
//
//        if (!this.updateById(prdMo)) {
//            throw new BizException("更新失败");
//        }
//
//        // 删除旧数据
//        // 删除-颜色规格
//        List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
//                new LambdaQueryWrapper<PrdMoColorSpecData>()
//                        .eq(PrdMoColorSpecData::getPid, prdMo.getId())
//        );
//        if (oldColorSpecDatas != null && oldColorSpecDatas.size() > 0) {
//            List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
//            if (oldColorSpecDataIds != null && oldColorSpecDataIds.size() > 0) {
//                if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
//                    throw new BizException("更新失败，颜色规格异常");
//                }
//            }
//        }
//        // 删除-生产工序
//        List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
//                new LambdaQueryWrapper<PrdMoProcess>()
//                        .eq(PrdMoProcess::getPid, prdMo.getId())
//        );
//        if (oldProcesses != null && oldProcesses.size() > 0) {
//            List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
//            if (oldProcessIds != null && oldProcessIds.size() > 0) {
//                if (!prdMoProcessService.removeByIds(oldProcessIds)) {
//                    throw new BizException("更新失败，生产工序异常");
//                }
//            }
//        }
//        // 删除-其他成本
//        List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
//                new LambdaQueryWrapper<PrdMoOtherCost>()
//                        .eq(PrdMoOtherCost::getPid, prdMo.getId())
//        );
//        if (oldOtherCosts != null && oldOtherCosts.size() > 0) {
//            List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
//            if (oldOtherCostIds != null && oldOtherCostIds.size() > 0) {
//                if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
//                    throw new BizException("更新失败，其他成本异常");
//                }
//            }
//        }
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
//        // 删除-原料详情
//        List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
//                new LambdaQueryWrapper<PrdMoMaterialDetail>()
//                        .eq(PrdMoMaterialDetail::getPid, prdMo.getId())
//        );
//        if (oldMaterialDetails != null && oldMaterialDetails.size() > 0) {
//            List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
//            if (oldMaterialDetailIds != null && oldMaterialDetailIds.size() > 0) {
//                if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
//                    throw new BizException("更新失败，原料详情异常");
//                }
//            }
//        }
//        // 删除-二次工艺
//        List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
//                new LambdaQueryWrapper<PrdMoSecondaryProcess>()
//                        .eq(PrdMoSecondaryProcess::getPid, prdMo.getId())
//        );
//        if (oldSecondaryProcesses != null && oldSecondaryProcesses.size() > 0) {
//            List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
//            if (oldSecondaryProcessIds != null && oldSecondaryProcessIds.size() > 0) {
//                if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
//                    throw new BizException("更新失败，二次工艺异常");
//                }
//            }
//        }
//        // 删除-图片详情
//        List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
//                new LambdaQueryWrapper<PrdMoAttachImg>()
//                        .eq(PrdMoAttachImg::getPid, prdMo.getId())
//        );
//        if (oldAttachImgs != null && oldAttachImgs.size() > 0) {
//            List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
//            if (oldAttachImgIds != null && oldAttachImgIds.size() > 0) {
//                if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
//                    throw new BizException("更新失败，图片详情异常");
//                }
//            }
//        }
//        // 删除-文件详情
//        List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
//                new LambdaQueryWrapper<PrdMoAttachFile>()
//                        .eq(PrdMoAttachFile::getPid, prdMo.getId())
//        );
//        if (oldAttachFiles != null && oldAttachFiles.size() > 0) {
//            List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
//            if (oldAttachFileIds != null && oldAttachFileIds.size() > 0) {
//                if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
//                    throw new BizException("更新失败，文件详情异常");
//                }
//            }
//        }
//
//        // 保存新数据
//        // 颜色规格
//        BigDecimal sumCount = BigDecimal.ZERO;
//        List<Map<String, Object>> moColorSpecMaps = prdMo.getMoColorSpecMaps();
//        List<PrdMoColorSpecData> moColorSpecDatas = new ArrayList<>();
//        if (moColorSpecMaps != null) {
//            for (int i = 0; i < moColorSpecMaps.size(); i++) {
//                Map<String, Object> map = moColorSpecMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "color")) {
//                        PrdMoColorSpecData newData = new PrdMoColorSpecData();
//                        newData.setPid(prdMo.getId());
//                        newData.setColor(map.get("color").toString());
//                        newData.setSpecification(s);
//                        newData.setQty(new BigDecimal(map.get(s).toString()));
//
//                        sumCount = sumCount.add(newData.getQty());
//
//                        moColorSpecDatas.add(newData);
//                    }
//                }
//            }
//        }
//        if (moColorSpecDatas.size() > 0) {
//            if (!prdMoColorSpecDataService.saveBatch(moColorSpecDatas)) {
//                throw new BizException("保存失败，规格详情异常");
//            }
//        }
//        // 生产工序
//        List<PrdMoProcess> moProcesses = prdMo.getMoProcesses();
//        if (moProcesses != null && moProcesses.size() > 0) {
//            for (PrdMoProcess moProcess : moProcesses) {
//                moProcess.setPid(prdMo.getId());
//                moProcess.setQty(sumCount);
//            }
//            if (!prdMoProcessService.saveBatch(moProcesses)) {
//                throw new BizException("保存失败，生产工序异常");
//            }
//        }
//
//        // 其他成本
//        List<Map<String, Object>> moOtherCostMaps = prdMo.getMoOtherCostMaps();
//        List<PrdMoOtherCost> moOtherCosts = new ArrayList<>();
//        if (moOtherCostMaps != null) {
//            for (int i = 0; i < moOtherCostMaps.size(); i++) {
//                Map<String, Object> map = moOtherCostMaps.get(i);
//                for (String s : map.keySet()) {
//                    if (!StringUtils.equals(s, "price")) {
//                        PrdMoOtherCost newData = new PrdMoOtherCost();
//                        newData.setPid(prdMo.getId());
//                        newData.setName(s);
//                        newData.setAmount(new BigDecimal(map.get(s).toString()));
//
//                        moOtherCosts.add(newData);
//                    }
//                }
//            }
//        }
//        if (moOtherCosts.size() > 0) {
//            if (!prdMoOtherCostService.saveBatch(moOtherCosts)) {
//                throw new BizException("保存失败，其他成本异常");
//            }
//        }
//
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
//
//        // 原料详情
//        List<PrdMoMaterialDetail> moMaterialDetails = prdMo.getMoMaterialDetails();
//        if (moMaterialDetails != null && moMaterialDetails.size() > 0) {
//            for (PrdMoMaterialDetail moMaterialDetail : moMaterialDetails) {
//                moMaterialDetail.setPid(prdMo.getId());
//            }
//            if (!prdMoMaterialDetailService.saveBatch(moMaterialDetails)) {
//                throw new BizException("保存失败，原料详情异常");
//            }
//        }
//
//        // 二次工艺
//        List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMo.getMoSecondaryProcesses();
//        if (moSecondaryProcesses != null && moSecondaryProcesses.size() > 0) {
//            for (PrdMoSecondaryProcess moSecondaryProcess : moSecondaryProcesses) {
//                moSecondaryProcess.setPid(prdMo.getId());
//            }
//            if (!prdMoSecondaryProcessService.saveBatch(moSecondaryProcesses)) {
//                throw new BizException("保存失败，二次工艺异常");
//            }
//        }
//
//        // 图片详情
//        if (attachImgs != null && attachImgs.size() > 0) {
//            for (PrdMoAttachImg attachImg : attachImgs) {
//                attachImg.setPid(prdMo.getId());
//            }
//
//            if (!prdMoAttachImgService.saveBatch(attachImgs)) {
//                throw new BizException("保存失败，异常码1");
//            }
//        }
//
//        // 文件详情
//        List<PrdMoAttachFile> attachFiles = prdMo.getAttachFiles();
//        if (attachFiles != null && attachFiles.size() > 0) {
//            for (PrdMoAttachFile attachFile : attachFiles) {
//                attachFile.setPid(prdMo.getId());
//            }
//
//            if (!prdMoAttachFileService.saveBatch(attachFiles)) {
//                throw new BizException("保存失败，异常码2");
//            }
//        }
//
//        return prdMo;
//    }
//
//    Boolean chkIsUsed(List<String> ids) {
//        // 裁床单
//        List<PrdCutting> cuttings = prdCuttingMapper.selectList(
//                new LambdaQueryWrapper<PrdCutting>()
//                        .in(PrdCutting::getSrcId, ids)
//        );
//        if (cuttings != null && cuttings.size() > 0) {
//            throw new BizException("当前生产订单已下达裁床单，无法删除");
//        }
//
//        return false;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean deleteById(Long id) {
//        PrdMo mo = this.getById(id);
//        if (mo == null) {
//            return true;
//        }
//        if (mo.getBillStatus() == Constants.INT_STATUS_APPROVING || mo.getBillStatus() == Constants.INT_STATUS_AUDITED) {
//            throw new BizException("流转中 和 已审核 数据无法删除");
//        }
//
//        List<String> ids = new ArrayList<>();
//        ids.add(id.toString());
//        if (this.chkIsUsed(ids)) {
//            throw new BizException("生产订单已下达裁床");
//        }
//
//        if (!this.removeById(id)) {
//            throw new BizException("删除失败，异常码1");
//        }
//
//        // 删除-颜色规格
//        List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
//                new LambdaQueryWrapper<PrdMoColorSpecData>()
//                        .eq(PrdMoColorSpecData::getPid, id)
//        );
//        if (oldColorSpecDatas != null && oldColorSpecDatas.size() > 0) {
//            List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
//            if (oldColorSpecDataIds != null && oldColorSpecDataIds.size() > 0) {
//                if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
//                    throw new BizException("删除失败，颜色规格异常");
//                }
//            }
//        }
//        // 删除-生产工序
//        List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
//                new LambdaQueryWrapper<PrdMoProcess>()
//                        .eq(PrdMoProcess::getPid, id)
//        );
//        if (oldProcesses != null && oldProcesses.size() > 0) {
//            List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
//            if (oldProcessIds != null && oldProcessIds.size() > 0) {
//                if (!prdMoProcessService.removeByIds(oldProcessIds)) {
//                    throw new BizException("删除失败，生产工序异常");
//                }
//            }
//        }
//        // 删除-其他成本
//        List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
//                new LambdaQueryWrapper<PrdMoOtherCost>()
//                        .eq(PrdMoOtherCost::getPid, id)
//        );
//        if (oldOtherCosts != null && oldOtherCosts.size() > 0) {
//            List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
//            if (oldOtherCostIds != null && oldOtherCostIds.size() > 0) {
//                if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
//                    throw new BizException("删除失败，其他成本异常");
//                }
//            }
//        }
//        // 删除-成品尺寸
//        List<PrdMoFinSizeData> oldFinSizeDatas = prdMoFinSizeDataService.list(
//                new LambdaQueryWrapper<PrdMoFinSizeData>()
//                        .eq(PrdMoFinSizeData::getPid, id)
//        );
//        if (oldFinSizeDatas != null && oldFinSizeDatas.size() > 0) {
//            List<String> oldFinSizeDataIds = oldFinSizeDatas.stream().map(PrdMoFinSizeData::getId).collect(Collectors.toList());
//            if (oldFinSizeDataIds != null && oldFinSizeDataIds.size() > 0) {
//                if (!prdMoFinSizeDataService.removeByIds(oldFinSizeDataIds)) {
//                    throw new BizException("删除失败，成品尺寸异常");
//                }
//            }
//        }
//        // 删除-原料详情
//        List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
//                new LambdaQueryWrapper<PrdMoMaterialDetail>()
//                        .eq(PrdMoMaterialDetail::getPid, id)
//        );
//        if (oldMaterialDetails != null && oldMaterialDetails.size() > 0) {
//            List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
//            if (oldMaterialDetailIds != null && oldMaterialDetailIds.size() > 0) {
//                if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
//                    throw new BizException("删除失败，原料详情异常");
//                }
//            }
//        }
//        // 删除-二次工艺
//        List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
//                new LambdaQueryWrapper<PrdMoSecondaryProcess>()
//                        .eq(PrdMoSecondaryProcess::getPid, id)
//        );
//        if (oldSecondaryProcesses != null && oldSecondaryProcesses.size() > 0) {
//            List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
//            if (oldSecondaryProcessIds != null && oldSecondaryProcessIds.size() > 0) {
//                if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
//                    throw new BizException("删除失败，二次工艺异常");
//                }
//            }
//        }
//        // 删除-图片详情
//        List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
//                new LambdaQueryWrapper<PrdMoAttachImg>()
//                        .eq(PrdMoAttachImg::getPid, id)
//        );
//        if (oldAttachImgs != null && oldAttachImgs.size() > 0) {
//            List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
//            if (oldAttachImgIds != null && oldAttachImgIds.size() > 0) {
//                if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
//                    throw new BizException("删除失败，图片详情异常");
//                }
//            }
//        }
//        // 删除-文件详情
//        List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
//                new LambdaQueryWrapper<PrdMoAttachFile>()
//                        .eq(PrdMoAttachFile::getPid, id)
//        );
//        if (oldAttachFiles != null && oldAttachFiles.size() > 0) {
//            List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
//            if (oldAttachFileIds != null && oldAttachFileIds.size() > 0) {
//                if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
//                    throw new BizException("删除失败，文件详情异常");
//                }
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteBatch(String[] ids) {
//        List<String> idList = Arrays.asList(ids);
//        List<PrdMo> list = this.listByIds(idList);
//        if (list != null && list.size() > 0) {
//            List<String> delIds = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT).map(PrdMo::getId).collect(Collectors.toList());
//            if (delIds != null && delIds.size() > 0) {
//                if (this.chkIsUsed(delIds)) {
//                    throw new BizException("生产订单已下达裁床");
//                }
//
//                if (!this.removeByIds(idList)) {
//                    throw new BizException("删除失败，异常码1");
//                }
//            } else {
//                throw new BizException("流转中 及 已审核 数据无法删除");
//            }
//
//            // 删除-颜色规格
//            List<PrdMoColorSpecData> oldColorSpecDatas = prdMoColorSpecDataService.list(
//                    new LambdaQueryWrapper<PrdMoColorSpecData>()
//                            .in(PrdMoColorSpecData::getPid, idList)
//            );
//            if (oldColorSpecDatas != null && oldColorSpecDatas.size() > 0) {
//                List<String> oldColorSpecDataIds = oldColorSpecDatas.stream().map(PrdMoColorSpecData::getId).collect(Collectors.toList());
//                if (oldColorSpecDataIds != null && oldColorSpecDataIds.size() > 0) {
//                    if (!prdMoColorSpecDataService.removeByIds(oldColorSpecDataIds)) {
//                        throw new BizException("删除失败，颜色规格异常");
//                    }
//                }
//            }
//            // 删除-生产工序
//            List<PrdMoProcess> oldProcesses = prdMoProcessService.list(
//                    new LambdaQueryWrapper<PrdMoProcess>()
//                            .in(PrdMoProcess::getPid, idList)
//            );
//            if (oldProcesses != null && oldProcesses.size() > 0) {
//                List<String> oldProcessIds = oldProcesses.stream().map(PrdMoProcess::getId).collect(Collectors.toList());
//                if (oldProcessIds != null && oldProcessIds.size() > 0) {
//                    if (!prdMoProcessService.removeByIds(oldProcessIds)) {
//                        throw new BizException("删除失败，生产工序异常");
//                    }
//                }
//            }
//            // 删除-其他成本
//            List<PrdMoOtherCost> oldOtherCosts = prdMoOtherCostService.list(
//                    new LambdaQueryWrapper<PrdMoOtherCost>()
//                            .in(PrdMoOtherCost::getPid, idList)
//            );
//            if (oldOtherCosts != null && oldOtherCosts.size() > 0) {
//                List<String> oldOtherCostIds = oldOtherCosts.stream().map(PrdMoOtherCost::getId).collect(Collectors.toList());
//                if (oldOtherCostIds != null && oldOtherCostIds.size() > 0) {
//                    if (!prdMoOtherCostService.removeByIds(oldOtherCostIds)) {
//                        throw new BizException("删除失败，其他成本异常");
//                    }
//                }
//            }
//            // 删除-成品尺寸
//            List<PrdMoFinSizeData> oldFinSizeDatas = prdMoFinSizeDataService.list(
//                    new LambdaQueryWrapper<PrdMoFinSizeData>()
//                            .in(PrdMoFinSizeData::getPid, idList)
//            );
//            if (oldFinSizeDatas != null && oldFinSizeDatas.size() > 0) {
//                List<String> oldFinSizeDataIds = oldFinSizeDatas.stream().map(PrdMoFinSizeData::getId).collect(Collectors.toList());
//                if (oldFinSizeDataIds != null && oldFinSizeDataIds.size() > 0) {
//                    if (!prdMoFinSizeDataService.removeByIds(oldFinSizeDataIds)) {
//                        throw new BizException("删除失败，成品尺寸异常");
//                    }
//                }
//            }
//            // 删除-原料详情
//            List<PrdMoMaterialDetail> oldMaterialDetails = prdMoMaterialDetailService.list(
//                    new LambdaQueryWrapper<PrdMoMaterialDetail>()
//                            .in(PrdMoMaterialDetail::getPid, idList)
//            );
//            if (oldMaterialDetails != null && oldMaterialDetails.size() > 0) {
//                List<String> oldMaterialDetailIds = oldMaterialDetails.stream().map(PrdMoMaterialDetail::getId).collect(Collectors.toList());
//                if (oldMaterialDetailIds != null && oldMaterialDetailIds.size() > 0) {
//                    if (!prdMoMaterialDetailService.removeByIds(oldMaterialDetailIds)) {
//                        throw new BizException("删除失败，原料详情异常");
//                    }
//                }
//            }
//            // 删除-二次工艺
//            List<PrdMoSecondaryProcess> oldSecondaryProcesses = prdMoSecondaryProcessService.list(
//                    new LambdaQueryWrapper<PrdMoSecondaryProcess>()
//                            .in(PrdMoSecondaryProcess::getPid, idList)
//            );
//            if (oldSecondaryProcesses != null && oldSecondaryProcesses.size() > 0) {
//                List<String> oldSecondaryProcessIds = oldSecondaryProcesses.stream().map(PrdMoSecondaryProcess::getId).collect(Collectors.toList());
//                if (oldSecondaryProcessIds != null && oldSecondaryProcessIds.size() > 0) {
//                    if (!prdMoSecondaryProcessService.removeByIds(oldSecondaryProcessIds)) {
//                        throw new BizException("删除失败，二次工艺异常");
//                    }
//                }
//            }
//            // 删除-图片详情
//            List<PrdMoAttachImg> oldAttachImgs = prdMoAttachImgService.list(
//                    new LambdaQueryWrapper<PrdMoAttachImg>()
//                            .in(PrdMoAttachImg::getPid, idList)
//            );
//            if (oldAttachImgs != null && oldAttachImgs.size() > 0) {
//                List<String> oldAttachImgIds = oldAttachImgs.stream().map(PrdMoAttachImg::getId).collect(Collectors.toList());
//                if (oldAttachImgIds != null && oldAttachImgIds.size() > 0) {
//                    if (!prdMoAttachImgService.removeByIds(oldAttachImgIds)) {
//                        throw new BizException("删除失败，图片详情异常");
//                    }
//                }
//            }
//            // 删除-文件详情
//            List<PrdMoAttachFile> oldAttachFiles = prdMoAttachFileService.list(
//                    new LambdaQueryWrapper<PrdMoAttachFile>()
//                            .in(PrdMoAttachFile::getPid, idList)
//            );
//            if (oldAttachFiles != null && oldAttachFiles.size() > 0) {
//                List<String> oldAttachFileIds = oldAttachFiles.stream().map(PrdMoAttachFile::getId).collect(Collectors.toList());
//                if (oldAttachFileIds != null && oldAttachFileIds.size() > 0) {
//                    if (!prdMoAttachFileService.removeByIds(oldAttachFileIds)) {
//                        throw new BizException("删除失败，文件详情异常");
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public PrdMo detail(String id) {
//        PrdMo result = baseMapper.infoById(id);
//        if (result != null) {
//            if (StringUtils.isNotEmpty(result.getTags())) {
//                List<String> tagList = Arrays.asList(result.getTags().split(","));
//                result.setTagList(tagList);
//            }
//
//            if (StringUtils.isNotEmpty(result.getMainPicId())) {
//                SysFiles mainPic = sysFilesService.getById(result.getMainPicId());
//                result.setMainPic(mainPic);
//            }
//
//            // 规格详情
//            List<PrdMoColorSpecData> moColorSpecDatas = prdMoColorSpecDataService.list(
//                    new LambdaQueryWrapper<PrdMoColorSpecData>()
//                            .eq(PrdMoColorSpecData::getPid, id)
//            );
//            if (moColorSpecDatas != null && moColorSpecDatas.size() > 0) {
//                List<String> moSpecs = moColorSpecDatas.stream().map(PrdMoColorSpecData::getSpecification).distinct().collect(Collectors.toList());
//                result.setMoSpecs(moSpecs);
//
//                List<Map<String, Object>> moColorSpecMaps = new ArrayList<>();
//                List<String> moColors = moColorSpecDatas.stream().map(PrdMoColorSpecData::getColor).distinct().collect(Collectors.toList());
//                if (moColors != null && moColors.size() > 0) {
//                    for (int i = 0; i < moColors.size(); i++) {
//                        String thisColor = moColors.get(i);
//
//                        Map<String, Object> map = new HashMap<>();
//
//                        map.put("pid", id);
//                        map.put("color", thisColor);
//
//                        List<PrdMoColorSpecData> thisItems = moColorSpecDatas.stream().filter(r -> StringUtils.equals(r.getColor(), thisColor)).collect(Collectors.toList());
//                        if (thisItems != null && thisItems.size() > 0) {
//                            for (int j = 0; j < thisItems.size(); j++) {
//                                PrdMoColorSpecData thisItem = thisItems.get(j);
//
//                                map.put("customSpecs_" + thisItem.getSpecification(), thisItem.getQty());
//                            }
//                        }
//
//                        moColorSpecMaps.add(map);
//                    }
//                }
//                result.setMoColorSpecMaps(moColorSpecMaps);
//            }
//
//            // 生产工序
//            List<PrdMoProcess> moProcesses = prdMoProcessMapper.listByPid(id);
//            if (moProcesses != null && moProcesses.size() > 0) {
//                result.setProcedureCount(moProcesses.size());
//            } else {
//                result.setProcedureCount(0);
//            }
//            result.setMoProcesses(moProcesses);
//
//            // 其他成本
//            List<PrdMoOtherCost> moOtherCosts = prdMoOtherCostService.list(
//                    new LambdaQueryWrapper<PrdMoOtherCost>()
//                            .eq(PrdMoOtherCost::getPid, id)
//            );
//            if (moOtherCosts != null && moOtherCosts.size() > 0) {
//                List<String> moOtherCostLabels = moOtherCosts.stream().map(PrdMoOtherCost::getName).collect(Collectors.toList());
//                result.setMoOtherCostLabels(moOtherCostLabels);
//
//                List<Map<String, Object>> moOtherCostMaps = new ArrayList<>();
//                Map<String, Object> map = new HashMap<>();
//
//                map.put("pid", id);
//                map.put("price", "金额");
//
//                for (int i = 0; i < moOtherCosts.size(); i++) {
//                    PrdMoOtherCost thisItem = moOtherCosts.get(i);
//
//                    map.put("customcosts_" + thisItem.getName(), thisItem.getAmount());
//                }
//
//                moOtherCostMaps.add(map);
//
//                result.setMoOtherCostMaps(moOtherCostMaps);
//            }
//
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
//
//            // 原料详情
//            List<PrdMoMaterialDetail> moMaterialDetails = prdMoMaterialDetailMapper.listByPid(id);
//            if (moMaterialDetails != null && moMaterialDetails.size() > 0) {
//                for (PrdMoMaterialDetail moMaterialDetail : moMaterialDetails) {
//                    if (moMaterialDetail.getRawMaterial() != null && StringUtils.isNotEmpty(moMaterialDetail.getRawMaterial().getMainPicId())) {
//                        SysFiles mainPic = sysFilesService.getById(moMaterialDetail.getRawMaterial().getMainPicId());
//                        moMaterialDetail.getRawMaterial().setMainPic(mainPic);
//                    }
//                }
//            }
//            result.setMoMaterialDetails(moMaterialDetails);
//
//            // 二次工艺
//            List<PrdMoSecondaryProcess> moSecondaryProcesses = prdMoSecondaryProcessMapper.listByPid(id);
//            result.setMoSecondaryProcesses(moSecondaryProcesses);
//
//            // 图片详情
//            PrdMoAttachImg prdMoAttachImg = new PrdMoAttachImg();
//            prdMoAttachImg.setPid(result.getId());
//            List<PrdMoAttachImg> attachImgs = prdMoAttachImgMapper.getList(prdMoAttachImg);
//            result.setAttachImgs(attachImgs);
//
//            // 文件详情
//            PrdMoAttachFile prdMoAttachFile = new PrdMoAttachFile();
//            prdMoAttachFile.setPid(result.getId());
//            List<PrdMoAttachFile> attachFiles = prdMoAttachFileMapper.getList(prdMoAttachFile);
//            result.setAttachFiles(attachFiles);
//        }
//
//        return result;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public PrdMo submit(String id) throws Exception {
//        PrdMo result = this.detail(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getBillStatus() != Constants.INT_STATUS_CREATE && result.getBillStatus() != Constants.INT_STATUS_RESUBMIT) {
//            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
//        }
//
//        result = this.auditUptData(result);
//        if (!this.updateById(result)) {
//            throw new BizException("操作失败");
//        }
//
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public PrdMo auditUptData(PrdMo prdMo) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        prdMo.setBillStatus(Constants.INT_STATUS_AUDITED);
//        prdMo.setAuditTime(sdf.format(new Date()));
//        prdMo.setAuditorId(RequestUtils.getUserId());
//        prdMo.setAuditor(RequestUtils.getNickname());
//
//        return prdMo;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchSubmitByIds(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<PrdMo> list = this.list(
//                new LambdaQueryWrapper<PrdMo>()
//                        .in(PrdMo::getId, idList)
//        );
//        if (list != null && list.size() > 0) {
//            // 过滤 创建/重新审核 且 启用 的数据
//            List<PrdMo> submitList = list.stream().filter(r -> (r.getBillStatus() == Constants.INT_STATUS_CREATE || r.getBillStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
//            if (submitList != null && submitList.size() > 0) {
//                for (PrdMo prdMo : submitList) {
//                    this.submit(prdMo.getId());
//                }
//            }
//
//            return "提交成功";
//        } else {
//            throw new BizException("未选择数据");
//        }
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result doAction(PrdMo prdMo) throws Exception {
//        if (prdMo.getBillStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(prdMo.getWorkFlow())) {
//            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
//            flowOperationInfo.setWorkFlowId(prdMo.getWorkFlowId());
//            flowOperationInfo.setFormData(prdMo);
//            flowOperationInfo.setUserId(prdMo.getUserId());
//            flowOperationInfo.setChildNodes(prdMo.getChildNodes());
//            flowOperationInfo.setCurrentNodeId(prdMo.getCurrentNodeId());
//            flowOperationInfo.setChildNodeApprovalResult(prdMo.getChildNodeApprovalResult());
//            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
//                // 提交
//                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
//                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
//                if (!start) {
//                    throw new BizException("流程提交错误");
//                }
//                prdMo.setWorkFlowId("");
//            } else {
//                // 审批流程
//                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
//                List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
//                List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
//                Boolean next = circulationOperationService.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId());
//                if (!next) {
//                    throw new BizException("流程提交失败");
//                }
//            }
//            List<String> ids = new ArrayList<>();
//            ids.add(prdMo.getId());
//            List<ChildNode> currentNodes = getCurrentNodes(ids, prdMo.getWorkFlow().getId());
//            prdMo.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
//            prdMo.setNodeStatus(currentNodes.get(0).getStatus());
//            prdMo.setCurrentNodeId(currentNodes.get(0).getId());
//            // 审批流正常结束
//            if (circulationOperationService.whetherLast(prdMo.getId()) == 1) {
//                prdMo = this.auditUptData(prdMo);
//            }
//            // 驳回
//            if (circulationOperationService.whetherLast(prdMo.getId()) == 2) {
//                prdMo.setBillStatus(Constants.INT_STATUS_RESUBMIT);
//            }
//        }
//
//        if (!this.updateById(prdMo)) {
//            throw new BizException("操作失败");
//        }
//
//        return Result.success(prdMo);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result batchDoAction(PrdMo prdMo) throws Exception {
//        List<String> ids = prdMo.getIds();
//        List<PrdMo> mos = this.list(
//                new LambdaQueryWrapper<PrdMo>()
//                        .in(PrdMo::getId, ids)
//        );
//        if (mos != null && mos.size() > 0) {
//            List<ChildNode> childNodes = getCurrentNodes(ids, prdMo.getWorkFlow().getId());
//            for (int i = 0; i < mos.size(); i++) {
//                PrdMo item = mos.get(i);
//                item.setBillStatus(prdMo.getBillStatus());
//                item.setWorkFlowId(prdMo.getWorkFlowId());
//                item.setUserId(prdMo.getUserId());
//                item.setChildNodes(prdMo.getChildNodes());
//                item.setChildNodeApprovalResult(prdMo.getChildNodeApprovalResult());
//                item.setWorkFlow(prdMo.getWorkFlow());
//                for (int j = 0; j < childNodes.size(); j++) {
//                    if (childNodes.get(j).getFromId().equals(item.getId())) {
//                        item.setWorkFlowInstantiateStatus(childNodes.get(j).getWorkFlowInstantiateStatus());
//                        item.setNodeStatus(childNodes.get(j).getStatus());
//                        item.setCurrentNodeId(childNodes.get(j).getId());
//                        break;
//                    }
//                }
//                Result result = doAction(item);
//                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
//                    throw new BizException("操作失败");
//                }
//            }
//        }
//
//        return Result.success("操作成功");
//    }
//
//    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId){
//        FlowOperationInfo flowOperationInfo =new FlowOperationInfo();
//        FlowUser flowUser = new FlowUser();
//        flowUser.setEmployeeName(RequestUtils.getNickname());
//        flowUser.setId(RequestUtils.getUserId());
//        flowOperationInfo.setFlowUser(flowUser);
//        flowOperationInfo.setFormIds(ids);
//        flowOperationInfo.setWorkFlowId(workFlowId);
//
//        List<ChildNode> childNodeList = circulationOperationService.getCurrentNodes(flowOperationInfo);
//        return childNodeList;
//    }
//
//    @Override
//    public PrdMo unAudit(String id) throws Exception {
//        PrdMo result = this.detail(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getBillStatus() != Constants.INT_STATUS_AUDITED) {
//            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
//        }
//
//        result = this.unAuditUptData(result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public PrdMo unAuditUptData(PrdMo prdMo) throws Exception {
//        prdMo.setBillStatus(Constants.INT_STATUS_RESUBMIT);
//        prdMo.setAuditTime(null);
//        prdMo.setAuditorId(null);
//        prdMo.setAuditor(null);
//        if (!this.updateById(prdMo)) {
//            throw new BizException("反审核失败");
//        }
//
//        return prdMo;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchUnAuditByIds(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<PrdMo> list = this.listByIds(idList);
//        if (list != null && list.size() > 0) {
//            List<PrdMo> unAuditList = list.stream().filter(r -> r.getBillStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
//            if (unAuditList != null && unAuditList.size() > 0) {
//                for (PrdMo prdMo : unAuditList) {
//                    this.unAudit(prdMo.getId());
//                }
//            }
//
//            return "反审核成功";
//        } else {
//            throw new BizException("未选择数据");
//        }
//    }
//
//    @Override
//    public PrdMo top(String id) {
//        PrdMo prdMo = this.getById(id);
//        if (prdMo != null) {
//            prdMo.setTop(0);
//
//            if (!this.updateById(prdMo)) {
//                throw new BizException("置顶失败");
//            }
//        }
//
//        return prdMo;
//    }
//
//    @Override
//    public PrdMo unTop(String id) {
//        PrdMo prdMo = this.getById(id);
//        if (prdMo != null) {
//            prdMo.setTop(1);
//
//            if (!this.updateById(prdMo)) {
//                throw new BizException("取消置顶失败");
//            }
//        }
//
//        return prdMo;
//    }
//}
