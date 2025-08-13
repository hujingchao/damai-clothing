//package com.fenglei.service.basedata.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
//import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.fenglei.common.constant.Constants;
//import com.fenglei.common.exception.BizException;
//import com.fenglei.common.result.Result;
//import com.fenglei.common.result.ResultCode;
//import com.fenglei.common.util.RequestUtils;
//import com.fenglei.mapper.basedata.*;
//import com.fenglei.mapper.prd.PrdMoColorSpecDataMapper;
//import com.fenglei.model.basedata.*;
//import com.fenglei.model.prd.entity.PrdMoColorSpecData;
//import com.fenglei.model.prd.entity.PrdMoMaterialDetail;
//import com.fenglei.model.system.entity.SysFiles;
//import com.fenglei.model.system.entity.SysUser;
//import com.fenglei.model.workFlow.dto.FlowDepartment;
//import com.fenglei.model.workFlow.dto.FlowOperationInfo;
//import com.fenglei.model.workFlow.dto.FlowRole;
//import com.fenglei.model.workFlow.dto.FlowUser;
//import com.fenglei.model.workFlow.entity.ChildNode;
//import com.fenglei.service.basedata.*;
//import com.fenglei.service.prd.PrdMoMaterialDetailService;
//import com.fenglei.service.system.ISysUserService;
//import com.fenglei.service.system.SysFilesService;
//import com.fenglei.service.workFlow.CirculationOperationService;
//import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author ljw
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class BdMaterialServiceImpl_odl extends ServiceImpl<BdMaterialMapper, BdMaterial> implements BdMaterialService {
//
//    @Resource
//    private BdMaterialAttachService bdMaterialAttachService;
//    @Resource
//    private BdMaterialAuxService bdMaterialAuxService;
//    @Resource
//    private BdMaterialRawService bdMaterialRawService;
//    @Resource
//    private BdMaterialProcessService bdMaterialProcessService;
//    @Resource
//    private BdMaterialRouteService bdMaterialRouteService;
//    @Resource
//    private BdMaterialRouteSpecialService bdMaterialRouteSpecialService;
//    @Resource
//    private BdMaterialSupplierService bdMaterialSupplierService;
//    @Resource
//    private BdProcedureService bdProcedureService;
//    @Resource
//    private BdSupplierService bdSupplierService;
//    @Resource
//    private SysFilesService sysFileService;
//    @Resource
//    private ISysUserService sysUserService;
//    @Resource
//    private BdMaterialColorService bdMaterialColorService;
//    @Resource
//    private BdMaterialSpecificationService bdMaterialSpecificationService;
//    @Resource
//    private BdMaterialDetailService bdMaterialDetailService;
//    @Resource
//    private SystemInformationAcquisitionService systemInformationAcquisitionService;
//    @Resource
//    private CirculationOperationService circulationOperationService;
//
//    @Resource
//    private BdMaterialAttachMapper bdMaterialAttachMapper;
//    @Resource
//    private BdMaterialRouteMapper bdMaterialRouteMapper;
//    @Resource
//    private BdMaterialAuxMapper bdMaterialAuxMapper;
//    @Resource
//    private BdMaterialRawMapper bdMaterialRawMapper;
//    @Resource
//    private BdMaterialSupplierMapper bdMaterialSupplierMapper;
//    @Resource
//    private BdMaterialRouteSpecialMapper bdMaterialRouteSpecialMapper;
//    @Resource
//    private BdMaterialProcessMapper bdMaterialProcessMapper;
//    @Resource
//    private BdMaterialDetailMapper bdMaterialDetailMapper;
//    @Resource
//    private BdMaterialSpecificationMapper bdMaterialSpecificationMapper;
//    @Resource
//    private BdMaterialColorMapper bdMaterialColorMapper;
//
//    @Resource
//    private BdTempMaterialMapper bdTempMaterialMapper;
//    @Resource
//    private BdTempMaterialAttachService bdTempMaterialAttachService;
//    @Resource
//    private BdTempMaterialAuxService bdTempMaterialAuxService;
//    @Resource
//    private BdTempMaterialColorService bdTempMaterialColorService;
//    @Resource
//    private BdTempMaterialDetailService bdTempMaterialDetailService;
//    @Resource
//    private BdTempMaterialProcessService bdTempMaterialProcessService;
//    @Resource
//    private BdTempMaterialRawService bdTempMaterialRawService;
//    @Resource
//    private BdTempMaterialRouteService bdTempMaterialRouteService;
//    @Resource
//    private BdTempMaterialRouteSpecialService bdTempMaterialRouteSpecialService;
//    @Resource
//    private BdTempMaterialSpecificationService bdTempMaterialSpecificationService;
//    @Resource
//    private BdTempMaterialSupplierService bdTempMaterialSupplierService;
//    @Resource
//    private BdTempMaterialRouteMapper bdTempMaterialRouteMapper;
//    @Resource
//    private BdTempMaterialAttachMapper bdTempMaterialAttachMapper;
//    @Resource
//    private BdTempMaterialAuxMapper bdTempMaterialAuxMapper;
//    @Resource
//    private BdTempMaterialRawMapper bdTempMaterialRawMapper;
//    @Resource
//    private BdTempMaterialSupplierMapper bdTempMaterialSupplierMapper;
//    @Resource
//    private BdTempMaterialRouteSpecialMapper bdTempMaterialRouteSpecialMapper;
//    @Resource
//    private BdTempMaterialProcessMapper bdTempMaterialProcessMapper;
//    @Resource
//    private BdTempMaterialDetailMapper bdTempMaterialDetailMapper;
//
//    @Resource
//    private PrdMoColorSpecDataMapper prdMoColorSpecDataMapper;
//    @Resource
//    private PrdMoMaterialDetailService prdMoMaterialDetailService;
//
//    @Override
//    public IPage<BdMaterial> myPage(Page page, BdMaterial bdMaterial) {
//
//        List<String> pids = new ArrayList<>();
//        if (StringUtils.isNotEmpty(bdMaterial.getAuxMaterialId())) {
//            List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, bdMaterial.getAuxMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//                List<BdMaterialAux> auxes = bdMaterialAuxService.list(
//                        new LambdaQueryWrapper<BdMaterialAux>()
//                                .in(BdMaterialAux::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (auxes != null && auxes.size() > 0) {
//                    for (BdMaterialAux aux : auxes) {
//                        if (!pids.contains(aux.getPid())) {
//                            pids.add(aux.getPid());
//                        }
//                    }
//                } else {
//                    if (pids.size() == 0) {
//                        pids.add("-1");
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getRawMaterialId())) {
//            List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, bdMaterial.getRawMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//                List<BdMaterialRaw> raws = bdMaterialRawService.list(
//                        new LambdaQueryWrapper<BdMaterialRaw>()
//                                .in(BdMaterialRaw::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (raws != null && raws.size() > 0) {
//                    for (BdMaterialRaw raw : raws) {
//                        if (!pids.contains(raw.getPid())) {
//                            pids.add(raw.getPid());
//                        }
//                    }
//                } else {
//                    if (pids.size() == 0) {
//                        pids.add("-1");
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcedureId())) {
//            List<BdMaterialRoute> routes = bdMaterialRouteService.list(
//                    new LambdaQueryWrapper<BdMaterialRoute>()
//                            .eq(BdMaterialRoute::getProcedureId, bdMaterial.getProcedureId())
//            );
//            if (routes != null && routes.size() > 0) {
//                for (BdMaterialRoute route : routes) {
//                    if (!pids.contains(route.getPid())) {
//                        pids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getStaffId())) {
//            BdMaterialRouteSpecial routeSpecial = new BdMaterialRouteSpecial();
//            routeSpecial.setStaffId(bdMaterial.getStaffId());
//            List<BdMaterialRoute> routes = bdMaterialRouteMapper.getListWithSpecial(routeSpecial);
//            if (routes != null && routes.size() > 0) {
//                for (BdMaterialRoute route : routes) {
//                    if (!pids.contains(route.getPid())) {
//                        pids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSupplierId())) {
//            List<BdMaterialSupplier> suppliers = bdMaterialSupplierService.list(
//                    new LambdaQueryWrapper<BdMaterialSupplier>()
//                            .eq(BdMaterialSupplier::getSupplierId, bdMaterial.getSupplierId())
//            );
//            if (suppliers != null && suppliers.size() > 0) {
//                for (BdMaterialSupplier supplier : suppliers) {
//                    if (!pids.contains(supplier.getPid())) {
//                        pids.add(supplier.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcess())) {
//            List<BdMaterialProcess> processes = bdMaterialProcessService.list(
//                    new LambdaQueryWrapper<BdMaterialProcess>()
//                            .like(BdMaterialProcess::getProcess, bdMaterial.getProcess())
//            );
//            if (processes != null && processes.size() > 0) {
//                for (BdMaterialProcess process : processes) {
//                    if (!pids.contains(process.getPid())) {
//                        pids.add(process.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getColor())) {
//            List<BdMaterialColor> colors = bdMaterialColorService.list(
//                    new LambdaQueryWrapper<BdMaterialColor>()
//                            .like(BdMaterialColor::getColor, bdMaterial.getColor())
//            );
//            if (colors != null && colors.size() > 0) {
//                for (BdMaterialColor color : colors) {
//                    if (!pids.contains(color.getPid())) {
//                        pids.add(color.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSpecification())) {
//            List<BdMaterialSpecification> specifications = bdMaterialSpecificationService.list(
//                    new LambdaQueryWrapper<BdMaterialSpecification>()
//                            .like(BdMaterialSpecification::getSpecification, bdMaterial.getSpecification())
//            );
//            if (specifications != null && specifications.size() > 0) {
//                for (BdMaterialSpecification specification : specifications) {
//                    if (!pids.contains(specification.getPid())) {
//                        pids.add(specification.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        bdMaterial.setIds(pids);
//
//        IPage<BdMaterial> iPage = baseMapper.getPage(page, bdMaterial);
//        if (iPage != null) {
//            List<BdMaterial> records = iPage.getRecords();
//            if (records != null && records.size() > 0) {
//                List<String> ids = records.stream().map(BdMaterial::getId).collect(Collectors.toList());
//
//                // 附件
//                BdMaterialAttach bdMaterialAttach = new BdMaterialAttach();
//                bdMaterialAttach.setPids(ids);
//                List<BdMaterialAttach> attaches = bdMaterialAttachMapper.getList(bdMaterialAttach);
//
//                // 辅料
//                BdMaterialAux bdMaterialAux = new BdMaterialAux();
//                bdMaterialAux.setPids(ids);
//                List<BdMaterialAux> auxes = bdMaterialAuxMapper.getList(bdMaterialAux);
//
//                // 原材料
//                BdMaterialRaw bdMaterialRaw = new BdMaterialRaw();
//                bdMaterialRaw.setPids(ids);
//                List<BdMaterialRaw> raws = bdMaterialRawMapper.getList(bdMaterialRaw);
//
//                // 供应商
//                BdMaterialSupplier bdMaterialSupplier = new BdMaterialSupplier();
//                bdMaterialSupplier.setPids(ids);
//                List<BdMaterialSupplier> suppliers = bdMaterialSupplierMapper.getList(bdMaterialSupplier);
//
//                // 工序
//                BdMaterialRoute bdMaterialRoute = new BdMaterialRoute();
//                bdMaterialRoute.setPids(ids);
//                List<BdMaterialRoute> routes = bdMaterialRouteMapper.getList(bdMaterialRoute);
//                if (routes != null && routes.size() > 0) {
//                    List<String> routeIds = routes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//                    BdMaterialRouteSpecial routeSpecial = new BdMaterialRouteSpecial();
//                    routeSpecial.setPids(routeIds);
//                    List<BdMaterialRouteSpecial> routeSpecials = bdMaterialRouteSpecialMapper.getList(routeSpecial);
//                    if (routeSpecials != null && routeSpecials.size() > 0) {
//                        for (BdMaterialRoute route : routes) {
//                            List<BdMaterialRouteSpecial> myRouteSpecials = routeSpecials.stream().filter(r -> r.getPid().equals(route.getId())).collect(Collectors.toList());
//                            route.setRouteSpecials(myRouteSpecials);
//                        }
//                    }
//                }
//
//                // 工艺要求
//                BdMaterialProcess bdMaterialProcess = new BdMaterialProcess();
//                bdMaterialProcess.setPids(ids);
//                List<BdMaterialProcess> processes = bdMaterialProcessMapper.getList(bdMaterialProcess);
//
//                // 颜色/色号
//                List<BdMaterialColor> colors = bdMaterialColorService.list(
//                        new LambdaQueryWrapper<BdMaterialColor>()
//                                .in(BdMaterialColor::getPid, ids)
//                                .apply(" color is not null and color <> ''")
//                );
//
//                // 规格
//                List<BdMaterialSpecification> specifications = bdMaterialSpecificationService.list(
//                        new LambdaQueryWrapper<BdMaterialSpecification>()
//                                .in(BdMaterialSpecification::getPid, ids)
//                                .apply(" specification is not null and specification <> ''")
//                );
//
//                // 详细
//                BdMaterialDetail mtrlDetail = new BdMaterialDetail();
//                mtrlDetail.setPids(ids);
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList(mtrlDetail);
//
//                for (BdMaterial record : records) {
//                    if (attaches != null && attaches.size() > 0) {
//                        List<BdMaterialAttach> myAttaches = attaches.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setAttaches(myAttaches);
//
//                        List<String> urls = new ArrayList<>();
//                        if (myAttaches != null && myAttaches.size() > 0) {
//                            for (BdMaterialAttach attach : myAttaches) {
//                                urls.add(attach.getSysFile().getUrl());
//                            }
//                        }
//                        record.setAttachUrls(urls);
//                    }
//
//                    if (auxes != null && auxes.size() > 0) {
//                        List<BdMaterialAux> myAuxes = auxes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setAuxes(myAuxes);
//                    }
//
//                    if (raws != null && raws.size() > 0) {
//                        List<BdMaterialRaw> myRaws = raws.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setRaws(myRaws);
//                    }
//
//                    if (suppliers != null && suppliers.size() > 0) {
//                        List<BdMaterialSupplier> mySuppliers = suppliers.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setSuppliers(mySuppliers);
//                    }
//
//                    if (routes != null && routes.size() > 0) {
//                        List<BdMaterialRoute> myRoutes = routes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setRoutes(myRoutes);
//                    }
//
//                    if (processes != null && processes.size() > 0) {
//                        List<BdMaterialProcess> myProcesses = processes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setProcesses(myProcesses);
//                    }
//
//                    if (colors != null && colors.size() > 0) {
//                        List<BdMaterialColor> myColors = colors.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setColors(myColors);
//                    }
//
//                    if (specifications != null && specifications.size() > 0) {
//                        List<BdMaterialSpecification> mySpecifications = specifications.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setSpecifications(mySpecifications);
//                    }
//
//                    if (details != null && details.size() > 0) {
//                        List<BdMaterialDetail> myDetails = details.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                        record.setDetails(myDetails);
//                    }
//                }
//            }
//        }
//
//        return iPage;
//    }
//
//    @Override
//    public IPage<BdMaterial> myPageWithTemp(Page page, BdMaterial bdMaterial) throws Exception {
//
//        List<String> pids = new ArrayList<>();
//        if (StringUtils.isNotEmpty(bdMaterial.getAuxMaterialId())) {
//            List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, bdMaterial.getAuxMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//                List<BdMaterialAux> auxes = bdMaterialAuxService.list(
//                        new LambdaQueryWrapper<BdMaterialAux>()
//                                .in(BdMaterialAux::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (auxes != null && auxes.size() > 0) {
//                    for (BdMaterialAux aux : auxes) {
//                        if (!pids.contains(aux.getPid())) {
//                            pids.add(aux.getPid());
//                        }
//                    }
//                } else {
//                    if (pids.size() == 0) {
//                        pids.add("-1");
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getRawMaterialId())) {
//            List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, bdMaterial.getRawMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//                List<BdMaterialRaw> raws = bdMaterialRawService.list(
//                        new LambdaQueryWrapper<BdMaterialRaw>()
//                                .in(BdMaterialRaw::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (raws != null && raws.size() > 0) {
//                    for (BdMaterialRaw raw : raws) {
//                        if (!pids.contains(raw.getPid())) {
//                            pids.add(raw.getPid());
//                        }
//                    }
//                } else {
//                    if (pids.size() == 0) {
//                        pids.add("-1");
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcedureId())) {
//            List<BdMaterialRoute> routes = bdMaterialRouteService.list(
//                    new LambdaQueryWrapper<BdMaterialRoute>()
//                            .eq(BdMaterialRoute::getProcedureId, bdMaterial.getProcedureId())
//            );
//            if (routes != null && routes.size() > 0) {
//                for (BdMaterialRoute route : routes) {
//                    if (!pids.contains(route.getPid())) {
//                        pids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getStaffId())) {
//            BdMaterialRouteSpecial routeSpecial = new BdMaterialRouteSpecial();
//            routeSpecial.setStaffId(bdMaterial.getStaffId());
//            List<BdMaterialRoute> routes = bdMaterialRouteMapper.getListWithSpecial(routeSpecial);
//            if (routes != null && routes.size() > 0) {
//                for (BdMaterialRoute route : routes) {
//                    if (!pids.contains(route.getPid())) {
//                        pids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSupplierId())) {
//            List<BdMaterialSupplier> suppliers = bdMaterialSupplierService.list(
//                    new LambdaQueryWrapper<BdMaterialSupplier>()
//                            .eq(BdMaterialSupplier::getSupplierId, bdMaterial.getSupplierId())
//            );
//            if (suppliers != null && suppliers.size() > 0) {
//                for (BdMaterialSupplier supplier : suppliers) {
//                    if (!pids.contains(supplier.getPid())) {
//                        pids.add(supplier.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcess())) {
//            List<BdMaterialProcess> processes = bdMaterialProcessService.list(
//                    new LambdaQueryWrapper<BdMaterialProcess>()
//                            .like(BdMaterialProcess::getProcess, bdMaterial.getProcess())
//            );
//            if (processes != null && processes.size() > 0) {
//                for (BdMaterialProcess process : processes) {
//                    if (!pids.contains(process.getPid())) {
//                        pids.add(process.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getColor())) {
//            List<BdMaterialColor> colors = bdMaterialColorService.list(
//                    new LambdaQueryWrapper<BdMaterialColor>()
//                            .like(BdMaterialColor::getColor, bdMaterial.getColor())
//            );
//            if (colors != null && colors.size() > 0) {
//                for (BdMaterialColor color : colors) {
//                    if (!pids.contains(color.getPid())) {
//                        pids.add(color.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSpecification())) {
//            List<BdMaterialSpecification> specifications = bdMaterialSpecificationService.list(
//                    new LambdaQueryWrapper<BdMaterialSpecification>()
//                            .like(BdMaterialSpecification::getSpecification, bdMaterial.getSpecification())
//            );
//            if (specifications != null && specifications.size() > 0) {
//                for (BdMaterialSpecification specification : specifications) {
//                    if (!pids.contains(specification.getPid())) {
//                        pids.add(specification.getPid());
//                    }
//                }
//            } else {
//                if (pids.size() == 0) {
//                    pids.add("-1");
//                }
//            }
//        }
//        bdMaterial.setIds(pids);
//
//        // 临时
//        List<String> tempPids = new ArrayList<>();
//        if (StringUtils.isNotEmpty(bdMaterial.getAuxMaterialId())) {
//            List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdTempMaterialDetail>()
//                            .eq(BdTempMaterialDetail::getPid, bdMaterial.getAuxMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
//                List<BdTempMaterialAux> auxes = bdTempMaterialAuxService.list(
//                        new LambdaQueryWrapper<BdTempMaterialAux>()
//                                .in(BdTempMaterialAux::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (auxes != null && auxes.size() > 0) {
//                    for (BdTempMaterialAux aux : auxes) {
//                        if (!tempPids.contains(aux.getPid())) {
//                            tempPids.add(aux.getPid());
//                        }
//                    }
//                } else {
//                    if (tempPids.size() == 0) {
//                        tempPids.add("-1");
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getRawMaterialId())) {
//            List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdTempMaterialDetail>()
//                            .eq(BdTempMaterialDetail::getPid, bdMaterial.getRawMaterialId())
//            );
//            if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
//                List<BdTempMaterialRaw> raws = bdTempMaterialRawService.list(
//                        new LambdaQueryWrapper<BdTempMaterialRaw>()
//                                .in(BdTempMaterialRaw::getMaterialDetailId, mtrlDetailIds)
//                );
//                if (raws != null && raws.size() > 0) {
//                    for (BdTempMaterialRaw raw : raws) {
//                        if (!tempPids.contains(raw.getPid())) {
//                            tempPids.add(raw.getPid());
//                        }
//                    }
//                } else {
//                    if (tempPids.size() == 0) {
//                        tempPids.add("-1");
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcedureId())) {
//            List<BdTempMaterialRoute> routes = bdTempMaterialRouteService.list(
//                    new LambdaQueryWrapper<BdTempMaterialRoute>()
//                            .eq(BdTempMaterialRoute::getProcedureId, bdMaterial.getProcedureId())
//            );
//            if (routes != null && routes.size() > 0) {
//                for (BdTempMaterialRoute route : routes) {
//                    if (!tempPids.contains(route.getPid())) {
//                        tempPids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getStaffId())) {
//            BdTempMaterialRouteSpecial routeSpecial = new BdTempMaterialRouteSpecial();
//            routeSpecial.setStaffId(bdMaterial.getStaffId());
//            List<BdTempMaterialRoute> routes = bdTempMaterialRouteMapper.getListWithSpecial(routeSpecial);
//            if (routes != null && routes.size() > 0) {
//                for (BdTempMaterialRoute route : routes) {
//                    if (!tempPids.contains(route.getPid())) {
//                        tempPids.add(route.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSupplierId())) {
//            List<BdTempMaterialSupplier> suppliers = bdTempMaterialSupplierService.list(
//                    new LambdaQueryWrapper<BdTempMaterialSupplier>()
//                            .eq(BdTempMaterialSupplier::getSupplierId, bdMaterial.getSupplierId())
//            );
//            if (suppliers != null && suppliers.size() > 0) {
//                for (BdTempMaterialSupplier supplier : suppliers) {
//                    if (!tempPids.contains(supplier.getPid())) {
//                        tempPids.add(supplier.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getProcess())) {
//            List<BdTempMaterialProcess> processes = bdTempMaterialProcessService.list(
//                    new LambdaQueryWrapper<BdTempMaterialProcess>()
//                            .like(BdTempMaterialProcess::getProcess, bdMaterial.getProcess())
//            );
//            if (processes != null && processes.size() > 0) {
//                for (BdTempMaterialProcess process : processes) {
//                    if (!tempPids.contains(process.getPid())) {
//                        tempPids.add(process.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getColor())) {
//            List<BdTempMaterialColor> colors = bdTempMaterialColorService.list(
//                    new LambdaQueryWrapper<BdTempMaterialColor>()
//                            .like(BdTempMaterialColor::getColor, bdMaterial.getColor())
//            );
//            if (colors != null && colors.size() > 0) {
//                for (BdTempMaterialColor color : colors) {
//                    if (!tempPids.contains(color.getPid())) {
//                        tempPids.add(color.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        if (StringUtils.isNotEmpty(bdMaterial.getSpecification())) {
//            List<BdTempMaterialSpecification> specifications = bdTempMaterialSpecificationService.list(
//                    new LambdaQueryWrapper<BdTempMaterialSpecification>()
//                            .like(BdTempMaterialSpecification::getSpecification, bdMaterial.getSpecification())
//            );
//            if (specifications != null && specifications.size() > 0) {
//                for (BdTempMaterialSpecification specification : specifications) {
//                    if (!tempPids.contains(specification.getPid())) {
//                        tempPids.add(specification.getPid());
//                    }
//                }
//            } else {
//                if (tempPids.size() == 0) {
//                    tempPids.add("-1");
//                }
//            }
//        }
//        bdMaterial.setTempIds(tempPids);
//        bdMaterial.setCreatorId(RequestUtils.getUserId());
//
//        BdTempMaterial srhTemp = new BdTempMaterial();
//        srhTemp.setCreatorId(RequestUtils.getUserId());
//        srhTemp.setMaterialGroup(bdMaterial.getMaterialGroup());
//        List<BdTempMaterial> tempList = bdTempMaterialMapper.getList(srhTemp);
//        if (tempList != null && tempList.size() > 0) {
//            List<String> excludeIds = bdMaterial.getExcludeIds();
//            if (excludeIds == null) {
//                excludeIds = new ArrayList<>();
//            }
//
//            for (BdTempMaterial tempMaterial : tempList) {
//                if (StringUtils.isNotEmpty(tempMaterial.getOriginalId())) {
//                    excludeIds.add(tempMaterial.getOriginalId());
//                }
//            }
//            bdMaterial.setExcludeIds(excludeIds);
//        }
//
//        IPage<BdMaterial> iPage = baseMapper.getPageWithTemp(page, bdMaterial);
//        if (iPage != null) {
//            List<BdMaterial> records = iPage.getRecords();
//            if (records != null && records.size() > 0) {
//                List<String> ids = records.stream().filter(r -> (StringUtils.isEmpty(r.getOriginalId()) || StringUtils.equals(r.getOriginalId(), "0")) && r.getStatus() == 2).map(BdMaterial::getId).collect(Collectors.toList());
//                if (ids == null || ids.size() == 0) {
//                    ids = new ArrayList<>();
//                    ids.add("-1");
//                }
//
//                // 附件
//                BdMaterialAttach bdMaterialAttach = new BdMaterialAttach();
//                bdMaterialAttach.setPids(ids);
//                List<BdMaterialAttach> attaches = bdMaterialAttachMapper.getList(bdMaterialAttach);
//
//                // 辅料
//                BdMaterialAux bdMaterialAux = new BdMaterialAux();
//                bdMaterialAux.setPids(ids);
//                List<BdMaterialAux> auxes = bdMaterialAuxMapper.getList(bdMaterialAux);
//
//                // 原材料
//                BdMaterialRaw bdMaterialRaw = new BdMaterialRaw();
//                bdMaterialRaw.setPids(ids);
//                List<BdMaterialRaw> raws = bdMaterialRawMapper.getList(bdMaterialRaw);
//
//                // 供应商
//                BdMaterialSupplier bdMaterialSupplier = new BdMaterialSupplier();
//                bdMaterialSupplier.setPids(ids);
//                List<BdMaterialSupplier> suppliers = bdMaterialSupplierMapper.getList(bdMaterialSupplier);
//
//                // 工序
//                BdMaterialRoute bdMaterialRoute = new BdMaterialRoute();
//                bdMaterialRoute.setPids(ids);
//                List<BdMaterialRoute> routes = bdMaterialRouteMapper.getList(bdMaterialRoute);
//                if (routes != null && routes.size() > 0) {
//                    List<String> routeIds = routes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//                    BdMaterialRouteSpecial routeSpecial = new BdMaterialRouteSpecial();
//                    routeSpecial.setPids(routeIds);
//                    List<BdMaterialRouteSpecial> routeSpecials = bdMaterialRouteSpecialMapper.getList(routeSpecial);
//                    if (routeSpecials != null && routeSpecials.size() > 0) {
//                        for (BdMaterialRoute route : routes) {
//                            List<BdMaterialRouteSpecial> myRouteSpecials = routeSpecials.stream().filter(r -> r.getPid().equals(route.getId())).collect(Collectors.toList());
//                            route.setRouteSpecials(myRouteSpecials);
//                        }
//                    }
//                }
//
//                // 工艺要求
//                BdMaterialProcess bdMaterialProcess = new BdMaterialProcess();
//                bdMaterialProcess.setPids(ids);
//                List<BdMaterialProcess> processes = bdMaterialProcessMapper.getList(bdMaterialProcess);
//
//                // 颜色/色号
//                List<BdMaterialColor> colors = bdMaterialColorService.list(
//                        new LambdaQueryWrapper<BdMaterialColor>()
//                                .in(BdMaterialColor::getPid, ids)
//                                .apply(" color is not null and color <> ''")
//                );
//
//                // 规格
//                List<BdMaterialSpecification> specifications = bdMaterialSpecificationService.list(
//                        new LambdaQueryWrapper<BdMaterialSpecification>()
//                                .in(BdMaterialSpecification::getPid, ids)
//                                .apply(" specification is not null and specification <> ''")
//                );
//
//                // 详细
//                BdMaterialDetail mtrlDetail = new BdMaterialDetail();
//                mtrlDetail.setPids(ids);
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList(mtrlDetail);
//
//
//                // 临时
//                List<String> tempIds = new ArrayList<>();
//                for (BdMaterial record : records) {
//                    if (!ids.contains(record.getId())) {
//                        tempIds.add(record.getId());
//                    }
//                }
//                if (tempIds.size() == 0) {
//                    tempIds.add("-1");
//                }
//
//                // 附件
//                BdTempMaterialAttach bdTempMaterialAttach = new BdTempMaterialAttach();
//                bdTempMaterialAttach.setPids(tempIds);
//                List<BdTempMaterialAttach> tempAttaches = bdTempMaterialAttachMapper.getList(bdTempMaterialAttach);
//
//                // 辅料
//                BdTempMaterialAux bdTempMaterialAux = new BdTempMaterialAux();
//                bdTempMaterialAux.setPids(tempIds);
//                List<BdTempMaterialAux> tempAuxes = bdTempMaterialAuxMapper.getList(bdTempMaterialAux);
//
//                // 原材料
//                BdTempMaterialRaw bdTempMaterialRaw = new BdTempMaterialRaw();
//                bdTempMaterialRaw.setPids(tempIds);
//                List<BdTempMaterialRaw> tempRaws = bdTempMaterialRawMapper.getList(bdTempMaterialRaw);
//
//                // 供应商
//                BdTempMaterialSupplier bdTempMaterialSupplier = new BdTempMaterialSupplier();
//                bdTempMaterialSupplier.setPids(tempIds);
//                List<BdTempMaterialSupplier> tempSuppliers = bdTempMaterialSupplierMapper.getList(bdTempMaterialSupplier);
//
//                // 工序
//                BdTempMaterialRoute bdTempMaterialRoute = new BdTempMaterialRoute();
//                bdTempMaterialRoute.setPids(tempIds);
//                List<BdTempMaterialRoute> tempRoutes = bdTempMaterialRouteMapper.getList(bdTempMaterialRoute);
//                if (tempRoutes != null && tempRoutes.size() > 0) {
//                    List<String> routeIds = tempRoutes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
//                    BdTempMaterialRouteSpecial routeSpecial = new BdTempMaterialRouteSpecial();
//                    routeSpecial.setPids(routeIds);
//                    List<BdTempMaterialRouteSpecial> routeSpecials = bdTempMaterialRouteSpecialMapper.getList(routeSpecial);
//                    if (routeSpecials != null && routeSpecials.size() > 0) {
//                        for (BdTempMaterialRoute route : tempRoutes) {
//                            List<BdTempMaterialRouteSpecial> myRouteSpecials = routeSpecials.stream().filter(r -> r.getPid().equals(route.getId())).collect(Collectors.toList());
//                            route.setRouteSpecials(myRouteSpecials);
//                        }
//                    }
//                }
//
//                // 工艺要求
//                BdTempMaterialProcess bdTempMaterialProcess = new BdTempMaterialProcess();
//                bdTempMaterialProcess.setPids(tempIds);
//                List<BdTempMaterialProcess> tempProcesses = bdTempMaterialProcessMapper.getList(bdTempMaterialProcess);
//
//                // 颜色/色号
//                List<BdTempMaterialColor> tempColors = bdTempMaterialColorService.list(
//                        new LambdaQueryWrapper<BdTempMaterialColor>()
//                                .in(BdTempMaterialColor::getPid, tempIds)
//                                .apply(" color is not null and color <> ''")
//                );
//
//                // 规格
//                List<BdTempMaterialSpecification> tempSpecifications = bdTempMaterialSpecificationService.list(
//                        new LambdaQueryWrapper<BdTempMaterialSpecification>()
//                                .in(BdTempMaterialSpecification::getPid, tempIds)
//                                .apply(" specification is not null and specification <> ''")
//                );
//
//                // 详细
//                BdTempMaterialDetail tempMtrlDetail = new BdTempMaterialDetail();
//                tempMtrlDetail.setPids(tempIds);
//                List<BdTempMaterialDetail> tempDetails = bdTempMaterialDetailMapper.getList(tempMtrlDetail);
//
//                for (BdMaterial record : records) {
//                    if ((StringUtils.isEmpty(record.getOriginalId()) || StringUtils.equals(record.getOriginalId(), "0")) && record.getStatus() == 2) {
//                        if (attaches != null && attaches.size() > 0) {
//                            List<BdMaterialAttach> myAttaches = attaches.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setAttaches(myAttaches);
//
//                            List<String> urls = new ArrayList<>();
//                            if (myAttaches != null && myAttaches.size() > 0) {
//                                for (BdMaterialAttach attach : myAttaches) {
//                                    urls.add(attach.getSysFile().getUrl());
//                                }
//                            }
//                            record.setAttachUrls(urls);
//                        }
//
//                        if (auxes != null && auxes.size() > 0) {
//                            List<BdMaterialAux> myAuxes = auxes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setAuxes(myAuxes);
//                        }
//
//                        if (raws != null && raws.size() > 0) {
//                            List<BdMaterialRaw> myRaws = raws.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setRaws(myRaws);
//                        }
//
//                        if (suppliers != null && suppliers.size() > 0) {
//                            List<BdMaterialSupplier> mySuppliers = suppliers.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setSuppliers(mySuppliers);
//                        }
//
//                        if (routes != null && routes.size() > 0) {
//                            List<BdMaterialRoute> myRoutes = routes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setRoutes(myRoutes);
//                        }
//
//                        if (processes != null && processes.size() > 0) {
//                            List<BdMaterialProcess> myProcesses = processes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setProcesses(myProcesses);
//                        }
//
//                        if (colors != null && colors.size() > 0) {
//                            List<BdMaterialColor> myColors = colors.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setColors(myColors);
//                        }
//
//                        if (specifications != null && specifications.size() > 0) {
//                            List<BdMaterialSpecification> mySpecifications = specifications.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setSpecifications(mySpecifications);
//                        }
//
//                        if (details != null && details.size() > 0) {
//                            List<BdMaterialDetail> myDetails = details.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setDetails(myDetails);
//                        }
//                    } else {
//                        if (tempAttaches != null && tempAttaches.size() > 0) {
//                            List<BdTempMaterialAttach> myTempAttaches = tempAttaches.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempAttaches != null && myTempAttaches.size() > 0) {
//                                List<BdMaterialAttach> myAttaches = new ArrayList<>();
//                                List<String> urls = new ArrayList<>();
//
//                                for (BdTempMaterialAttach myTempAttach : myTempAttaches) {
//                                    BdMaterialAttach attach = new BdMaterialAttach();
//                                    BeanUtils.copyProperties(attach, myTempAttach);
//                                    myAttaches.add(attach);
//
//                                    urls.add(myTempAttach.getSysFile().getUrl());
//                                }
//                                record.setAttaches(myAttaches);
//                                record.setAttachUrls(urls);
//                            }
//                        }
//
//                        if (tempAuxes != null && tempAuxes.size() > 0) {
//                            List<BdTempMaterialAux> myTempAuxes = tempAuxes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempAuxes != null && myTempAuxes.size() > 0) {
//                                List<BdMaterialAux> myAuxes = new ArrayList<>();
//
//                                for (BdTempMaterialAux myTempAux : myTempAuxes) {
//                                    BdMaterialAux aux = new BdMaterialAux();
//                                    BeanUtils.copyProperties(aux, myTempAux);
//
//                                    myAuxes.add(aux);
//                                }
//                                record.setAuxes(myAuxes);
//                            }
//                        }
//
//                        if (tempRaws != null && tempRaws.size() > 0) {
//                            List<BdTempMaterialRaw> myTempRaws = tempRaws.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempRaws != null && myTempRaws.size() > 0) {
//                                List<BdMaterialRaw> myRaws = new ArrayList<>();
//
//                                for (BdTempMaterialRaw myTempRaw : myTempRaws) {
//                                    BdMaterialRaw raw = new BdMaterialRaw();
//                                    BeanUtils.copyProperties(raw, myTempRaw);
//
//                                    myRaws.add(raw);
//                                }
//                                record.setRaws(myRaws);
//                            }
//                        }
//
//                        if (tempSuppliers != null && tempSuppliers.size() > 0) {
//                            List<BdTempMaterialSupplier> myTempSuppliers = tempSuppliers.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempSuppliers != null && myTempSuppliers.size() > 0) {
//                                List<BdMaterialSupplier> mySuppliers = new ArrayList<>();
//
//                                for (BdTempMaterialSupplier myTempSupplier : myTempSuppliers) {
//                                    if (myTempSupplier.getPrice() == null) {
//                                        myTempSupplier.setPrice(BigDecimal.ZERO);
//                                    }
//                                    BdMaterialSupplier supplier = new BdMaterialSupplier();
//                                    BeanUtils.copyProperties(supplier, myTempSupplier);
//
//                                    mySuppliers.add(supplier);
//                                }
//                                record.setSuppliers(mySuppliers);
//                            }
//                        }
//
//                        if (routes != null && routes.size() > 0) {
//                            List<BdMaterialRoute> myRoutes = routes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            record.setRoutes(myRoutes);
//                        }
//
//                        if (tempProcesses != null && tempProcesses.size() > 0) {
//                            List<BdTempMaterialProcess> myTempProcesses = tempProcesses.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempProcesses != null && myTempProcesses.size() > 0) {
//                                List<BdMaterialProcess> myProcesses = new ArrayList<>();
//
//                                for (BdTempMaterialProcess myTempProcess : myTempProcesses) {
//                                    BdMaterialProcess process = new BdMaterialProcess();
//                                    BeanUtils.copyProperties(process, myTempProcess);
//
//                                    myProcesses.add(process);
//                                }
//                                record.setProcesses(myProcesses);
//                            }
//                        }
//
//                        if (tempColors != null && tempColors.size() > 0) {
//                            List<BdTempMaterialColor> myTempColors = tempColors.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempColors != null && myTempColors.size() > 0) {
//                                List<BdMaterialColor> myColors = new ArrayList<>();
//
//                                for (BdTempMaterialColor myTempColor : myTempColors) {
//                                    BdMaterialColor color = new BdMaterialColor();
//                                    BeanUtils.copyProperties(color, myTempColor);
//
//                                    myColors.add(color);
//                                }
//                                record.setColors(myColors);
//                            }
//                        }
//
//                        if (tempSpecifications != null && tempSpecifications.size() > 0) {
//                            List<BdTempMaterialSpecification> myTempSpecifications = tempSpecifications.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempSpecifications != null && myTempSpecifications.size() > 0) {
//                                List<BdMaterialSpecification> mySpecifications = new ArrayList<>();
//
//                                for (BdTempMaterialSpecification myTempSpecification : myTempSpecifications) {
//                                    BdMaterialSpecification specification = new BdMaterialSpecification();
//                                    BeanUtils.copyProperties(specification, myTempSpecification);
//
//                                    mySpecifications.add(specification);
//                                }
//                                record.setSpecifications(mySpecifications);
//                            }
//                        }
//
//                        if (tempDetails != null && tempDetails.size() > 0) {
//                            List<BdTempMaterialDetail> myTempDetails = tempDetails.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
//                            if (myTempDetails != null && myTempDetails.size() > 0) {
//                                List<BdMaterialDetail> myDetails = new ArrayList<>();
//
//                                for (BdTempMaterialDetail myTempDetail : myTempDetails) {
//                                    BdMaterialDetail detail = new BdMaterialDetail();
//                                    BeanUtils.copyProperties(detail, myTempDetail);
//
//                                    myDetails.add(detail);
//                                }
//                                record.setDetails(myDetails);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return iPage;
//    }
//
//    @Override
//    public List<BdMaterial> myList(BdMaterial bdMaterial) {
//        List<String> pids = new ArrayList<>();
//
//        if (StringUtils.isNotEmpty(bdMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdMaterial.getAuxMaterialId())) {
//            List<String> materialIds = new ArrayList<>();
//            if (StringUtils.isNotEmpty(bdMaterial.getCommFilter())) {
//                List<BdMaterial> materials = this.list(
//                        new LambdaQueryWrapper<BdMaterial>()
//                                .like(BdMaterial::getNumber, bdMaterial.getCommFilter())
//                                .or()
//                                .like(BdMaterial::getName, bdMaterial.getCommFilter())
//                                .or()
//                                .apply(" concat(number, ' ', name) like concat('%', '" + bdMaterial.getCommFilter() + "', '%')")
//                );
//                if (materials != null && materials.size() > 0) {
//                    for (BdMaterial material : materials) {
//                        materialIds.add(material.getId());
//                    }
//                } else {
//                    materialIds.add("-1");
//                }
//            }
//
//            if (bdMaterial.getMaterialGroup() == 0) {
//                // 成品
//                for (String materialId : materialIds) {
//                    pids.add(materialId);
//                }
//            } else {
//                List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .in(materialIds != null && materialIds.size() > 0, BdMaterialDetail::getPid, materialIds)
//                                .eq(StringUtils.isNotEmpty(bdMaterial.getAuxMaterialId()), BdMaterialDetail::getPid, bdMaterial.getAuxMaterialId())
//                );
//                if (mtrlDetails != null && mtrlDetails.size() > 0) {
//                    List<String> mtrlDetailIds = mtrlDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//
//                    if (bdMaterial.getMaterialGroup() == 1) {
//                        // 辅料id
//                        List<BdMaterialAux> auxes = bdMaterialAuxService.list(
//                                new LambdaQueryWrapper<BdMaterialAux>()
//                                        .in(BdMaterialAux::getMaterialDetailId, mtrlDetailIds)
//                        );
//                        if (auxes != null && auxes.size() > 0) {
//                            for (BdMaterialAux aux : auxes) {
//                                if (!pids.contains(aux.getPid())) {
//                                    pids.add(aux.getPid());
//                                }
//                            }
//                        }
//                    }
//                    if (bdMaterial.getMaterialGroup() == 2) {
//                        // 原材料id
//                        if (StringUtils.isNotEmpty(bdMaterial.getRawMaterialId())) {
//                            List<BdMaterialRaw> raws = bdMaterialRawService.list(
//                                    new LambdaQueryWrapper<BdMaterialRaw>()
//                                            .in(BdMaterialRaw::getMaterialDetailId, mtrlDetailIds)
//                            );
//                            if (raws != null && raws.size() > 0) {
//                                for (BdMaterialRaw raw : raws) {
//                                    if (!pids.contains(raw.getPid())) {
//                                        pids.add(raw.getPid());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (pids.size() == 0) {
//                pids.add("-1");
//            }
//        }
//
//        // 工序
//        if (StringUtils.isNotEmpty(bdMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdMaterial.getProcedureId())) {
//            List<String> procedureIds = new ArrayList<>();
//            if (StringUtils.isNotEmpty(bdMaterial.getCommFilter())) {
//                List<BdProcedure> procedures = bdProcedureService.list(
//                        new LambdaQueryWrapper<BdProcedure>()
//                                .like(BdProcedure::getNumber, bdMaterial.getCommFilter())
//                                .or()
//                                .like(BdProcedure::getName, bdMaterial.getCommFilter())
//                                .or()
//                                .apply(" concat(number, ' ', name) like concat('%', '" + bdMaterial.getCommFilter() + "', '%')")
//                );
//                if (procedures != null && procedures.size() > 0) {
//                    for (BdProcedure procedure : procedures) {
//                        procedureIds.add(procedure.getId());
//                    }
//                } else {
//                    procedureIds.add("-1");
//                }
//            }
//            List<BdMaterialRoute> materialRoutes = bdMaterialRouteService.list(
//                    new LambdaQueryWrapper<BdMaterialRoute>()
//                            .in(procedureIds != null && procedureIds.size() > 0, BdMaterialRoute::getProcedureId, procedureIds)
//                            .eq(StringUtils.isNotEmpty(bdMaterial.getProcedureId()), BdMaterialRoute::getProcedureId, bdMaterial.getProcedureId())
//            );
//            if (materialRoutes != null && materialRoutes.size() > 0) {
//                for (BdMaterialRoute route : materialRoutes) {
//                    if (!pids.contains(route.getPid())) {
//                        pids.add(route.getPid());
//                    }
//                }
//            }
//            if (pids.size() == 0) {
//                pids.add("-1");
//            }
//        }
//
//        // 供应商id
//        if (StringUtils.isNotEmpty(bdMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdMaterial.getSupplierId())) {
//            List<String> supplierIds = new ArrayList<>();
//            if (StringUtils.isNotEmpty(bdMaterial.getCommFilter())) {
//                List<BdSupplier> suppliers = bdSupplierService.list(
//                        new LambdaQueryWrapper<BdSupplier>()
//                                .like(BdSupplier::getNumber, bdMaterial.getCommFilter())
//                                .or()
//                                .like(BdSupplier::getName, bdMaterial.getCommFilter())
//                                .or()
//                                .apply(" concat(number, ' ', name) like concat('%', '" + bdMaterial.getCommFilter() + "', '%')")
//                );
//                if (suppliers != null && suppliers.size() > 0) {
//                    for (BdSupplier supplier : suppliers) {
//                        supplierIds.add(supplier.getId());
//                    }
//                } else {
//                    supplierIds.add("-1");
//                }
//            }
//            List<BdMaterialSupplier> materialSuppliers = bdMaterialSupplierService.list(
//                    new LambdaQueryWrapper<BdMaterialSupplier>()
//                            .in(supplierIds != null && supplierIds.size() > 0, BdMaterialSupplier::getSupplierId, supplierIds)
//                            .eq(StringUtils.isNotEmpty(bdMaterial.getSupplierId()), BdMaterialSupplier::getSupplierId, bdMaterial.getSupplierId())
//            );
//            if (materialSuppliers != null && materialSuppliers.size() > 0) {
//                for (BdMaterialSupplier materialSupplier : materialSuppliers) {
//                    if (!pids.contains(materialSupplier.getPid())) {
//                        pids.add(materialSupplier.getPid());
//                    }
//                }
//            }
//            if (pids.size() == 0) {
//                pids.add("-1");
//            }
//        }
//
//        // 工艺要求
//        if (StringUtils.isNotEmpty(bdMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdMaterial.getProcess())) {
//            List<BdMaterialProcess> processes = bdMaterialProcessService.list(
//                    new LambdaQueryWrapper<BdMaterialProcess>()
//                            .like(StringUtils.isNotEmpty(bdMaterial.getCommFilter()), BdMaterialProcess::getProcess, bdMaterial.getCommFilter())
//                            .like(StringUtils.isNotEmpty(bdMaterial.getProcess()), BdMaterialProcess::getProcess, bdMaterial.getProcess())
//            );
//            if (processes != null && processes.size() > 0) {
//                for (BdMaterialProcess process : processes) {
//                    if (!pids.contains(process.getPid())) {
//                        pids.add(process.getPid());
//                    }
//                }
//            }
//            if (pids.size() == 0) {
//                pids.add("-1");
//            }
//        }
//
//        bdMaterial.setIds(pids);
//
//        List<BdMaterial> list = baseMapper.getList(bdMaterial);
//
//        return list;
//    }
//
//    @Override
//    public List<BdMaterial> listMtrlNum(BdMaterial bdMaterial) {
//        List<BdMaterial> list = baseMapper.listMtrlNum(bdMaterial);
//
//        return list;
//    }
//
//    @Override
//    public BdMaterial detailMtrlNum(BdMaterial bdMaterial) {
//        if (StringUtils.isNotEmpty(bdMaterial.getId())) {
//            BdMaterial result = this.detailFin(bdMaterial.getId());
//
//            return result;
//        } else if (StringUtils.isNotEmpty(bdMaterial.getNumber())) {
//            List<BdMaterial> list = this.list(
//                    new LambdaQueryWrapper<BdMaterial>()
//                            .eq(BdMaterial::getNumber, bdMaterial.getNumber())
//                            .eq(BdMaterial::getMaterialGroup, 0)
//            );
//            if (list != null && list.size() > 0) {
//                BdMaterial result = this.detailFin(list.get(0).getId());
//
//                return result;
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterial add(BdMaterial bdMaterial) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        if (StringUtils.isNotEmpty(bdMaterial.getNumber())) {
//            List<BdMaterial> list = this.list(
//                    new LambdaQueryWrapper<BdMaterial>()
//                            .eq(BdMaterial::getNumber, bdMaterial.getNumber())
//            );
//            if (list != null && list.size() > 0) {
//                throw new BizException("已存在相同编码的物料档案");
//            }
//        } else {
//            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH) + 1;
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//            String no = "";
//            if (bdMaterial.getMaterialGroup() == 0) {
//                no = "CP" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//            } else if (bdMaterial.getMaterialGroup() == 1) {
//                no = "FL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//            } else if (bdMaterial.getMaterialGroup() == 2) {
//                no = "YCL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//            }
//            List<String> nos = baseMapper.getNos(no);
//            if (nos != null && nos.size() > 0) {
//                String maxNo = nos.get(0);
//                Integer pos = maxNo.lastIndexOf("-");
//                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
//                Integer maxNoInt = Integer.valueOf(maxIdxStr);
//                String noIdxStr = String.format("%04d", maxNoInt + 1);
//                no = no + noIdxStr;
//            } else {
//                no = no + "0001";
//            }
//            bdMaterial.setNumber(no);
//        }
//
//        // 成品
//        if (ObjectUtils.isNotEmpty(bdMaterial.getMaterialGroup()) && bdMaterial.getMaterialGroup() == 0) {
////                if (bdMaterial.getAuxes() == null || bdMaterial.getAuxes().size() == 0) {
////                    throw new BizException("未检测到辅料明细");
////                }
//            if (bdMaterial.getRaws() == null || bdMaterial.getRaws().size() == 0) {
//                throw new BizException("未检测到原材料明细");
//            }
//            if (bdMaterial.getRoutes() == null || bdMaterial.getRoutes().size() == 0) {
//                throw new BizException("未检测到工序明细");
//            }
//        }
//
//        // 主图
//        List<BdMaterialAttach> attaches = bdMaterial.getAttaches();
//        if (attaches != null && attaches.size() > 0) {
//            bdMaterial.setMainPicId(attaches.get(0).getAttachId());
//        }
//
//        bdMaterial.setStatus(Constants.INT_STATUS_CREATE);
//        bdMaterial.setCreateTime(sdf.format(new Date()));
//        bdMaterial.setCreatorId(RequestUtils.getUserId());
//        bdMaterial.setCreator(RequestUtils.getNickname());
//        if (!this.save(bdMaterial)) {
//            throw new BizException("保存失败");
//        }
//
//        // 图片附件
//        if (attaches != null && attaches.size() > 0) {
//            for (BdMaterialAttach attach : attaches) {
//                attach.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialAttachService.saveBatch(attaches)) {
//                throw new BizException("保存失败，异常码1");
//            }
//        }
//
//        // 辅料
//        List<BdMaterialAux> auxes = bdMaterial.getAuxes();
//        if (auxes != null && auxes.size() > 0) {
//            for (BdMaterialAux aux : auxes) {
//                aux.setPid(bdMaterial.getId());
//
//                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
//                srhMtrlDtl.setPid(aux.getAuxMaterial().getId());
//                srhMtrlDtl.setColor(aux.getColor());
//                srhMtrlDtl.setSpecification(aux.getSpecification());
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
//                if (details != null && details.size() > 0) {
//                    aux.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialAuxService.saveBatch(auxes)) {
//                throw new BizException("保存失败，异常码2");
//            }
//        }
//
//        // 原材料
//        List<BdMaterialRaw> raws = bdMaterial.getRaws();
//        if (raws != null && raws.size() > 0) {
//            for (BdMaterialRaw raw : raws) {
//                raw.setPid(bdMaterial.getId());
//
//                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
//                srhMtrlDtl.setPid(raw.getRawMaterial().getId());
//                srhMtrlDtl.setColor(raw.getColor());
//                srhMtrlDtl.setSpecification(raw.getSpecification());
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
//                if (details != null && details.size() > 0) {
//                    raw.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("原材料信息异常 编码 " + raw.getRawMaterial().getNumber() + " 名称 " + raw.getRawMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialRawService.saveBatch(raws)) {
//                throw new BizException("保存失败，异常码3");
//            }
//        }
//
//        // 工序
//        List<BdMaterialRoute> routes = bdMaterial.getRoutes();
//        if (routes != null && routes.size() > 0) {
//            for (BdMaterialRoute route : routes) {
//                route.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialRouteService.saveBatch(routes)) {
//                throw new BizException("保存失败，异常码4");
//            }
//
//            List<BdMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
//            for (BdMaterialRoute route : routes) {
//                List<BdMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
//                if (routeSpecials != null && routeSpecials.size() > 0) {
//                    for (BdMaterialRouteSpecial routeSpecial : routeSpecials) {
//                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
//                            routeSpecial.setPid(route.getId());
//                            newRouteSpecials.add(routeSpecial);
//                        }
//                    }
//                }
//            }
//            if (newRouteSpecials.size() > 0) {
//                if (!bdMaterialRouteSpecialService.saveBatch(newRouteSpecials)) {
//                    throw new BizException("保存失败，异常码5");
//                }
//            }
//        }
//
//        // 工艺要求
//        List<BdMaterialProcess> processes = bdMaterial.getProcesses();
//        if (processes != null && processes.size() > 0) {
//            for (BdMaterialProcess process : processes) {
//                process.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialProcessService.saveBatch(processes)) {
//                throw new BizException("保存失败，异常码6");
//            }
//        }
//
//        // 供应商
//        List<BdMaterialSupplier> suppliers = bdMaterial.getSuppliers();
//        if (suppliers != null && suppliers.size() > 0) {
//            for (BdMaterialSupplier supplier : suppliers) {
//                supplier.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialSupplierService.saveBatch(suppliers)) {
//                throw new BizException("保存失败，异常码7");
//            }
//        }
//
//        // 颜色/色号
//        List<BdMaterialColor> colors = bdMaterial.getColors();
//        if (colors == null || colors.size() == 0) {
//            BdMaterialColor newColor = new BdMaterialColor();
//            colors.add(newColor);
//        }
//        for (BdMaterialColor color : colors) {
//            color.setPid(bdMaterial.getId());
//
//            color.setCreateTime(sdf.format(new Date()));
//            color.setCreatorId(RequestUtils.getUserId());
//            color.setCreator(RequestUtils.getNickname());
//        }
//        if (!bdMaterialColorService.saveBatch(colors)) {
//            throw new BizException("保存失败，异常码8");
//        }
//
//        // 规格
//        List<BdMaterialSpecification> specifications = bdMaterial.getSpecifications();
//        if (specifications == null || specifications.size() == 0) {
//            BdMaterialSpecification newSpec = new BdMaterialSpecification();
//            specifications.add(newSpec);
//        }
//        for (BdMaterialSpecification specification : specifications) {
//            specification.setPid(bdMaterial.getId());
//
//            specification.setCreateTime(sdf.format(new Date()));
//            specification.setCreatorId(RequestUtils.getUserId());
//            specification.setCreator(RequestUtils.getNickname());
//        }
//        if (!bdMaterialSpecificationService.saveBatch(specifications)) {
//            throw new BizException("保存失败，异常码9");
//        }
//
//        // 详细
//        List<BdMaterialDetail> details = new ArrayList<>();
//        for (BdMaterialColor color : colors) {
//            for (BdMaterialSpecification specification : specifications) {
//                BdMaterialDetail mtrlDtl = new BdMaterialDetail();
//                mtrlDtl.setPid(bdMaterial.getId());
//                mtrlDtl.setColorId(color.getId());
//                mtrlDtl.setSpecificationId(specification.getId());
//
//                mtrlDtl.setCreateTime(sdf.format(new Date()));
//                mtrlDtl.setCreatorId(RequestUtils.getUserId());
//                mtrlDtl.setCreator(RequestUtils.getNickname());
//
//                details.add(mtrlDtl);
//            }
//        }
//        if (details != null && details.size() > 0) {
//            if (!bdMaterialDetailService.saveBatch(details)) {
//                throw new BizException("保存失败，异常码10");
//            }
//        }
//
//        return bdMaterial;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterial myUpdate(BdMaterial bdMaterial) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        if (StringUtils.isNotEmpty(bdMaterial.getNumber())) {
//            List<BdMaterial> list = this.list(
//                    new LambdaQueryWrapper<BdMaterial>()
//                            .eq(BdMaterial::getNumber, bdMaterial.getNumber())
//                            .ne(BdMaterial::getId, bdMaterial.getId())
//            );
//            if (list != null && list.size() > 0) {
//                throw new BizException("已存在相同编码的物料档案，请修改，异常码1");
//            }
//        } else {
//            BdMaterial thisMaterial = this.getById(bdMaterial.getId());
//            if (thisMaterial != null) {
//                bdMaterial.setNumber(thisMaterial.getNumber());
//
//                List<BdMaterial> list = this.list(
//                        new LambdaQueryWrapper<BdMaterial>()
//                                .eq(BdMaterial::getNumber, bdMaterial.getNumber())
//                                .ne(BdMaterial::getId, bdMaterial.getId())
//                );
//                if (list != null && list.size() > 0) {
//                    throw new BizException("已存在相同编码的物料档案，请修改，异常码2");
//                }
//            } else {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH) + 1;
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//                String no = "";
//                if (bdMaterial.getMaterialGroup() == 0) {
//                    no = "CP" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//                } else if (bdMaterial.getMaterialGroup() == 1) {
//                    no = "FL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//                } else if (bdMaterial.getMaterialGroup() == 2) {
//                    no = "YCL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//                }
//                List<String> nos = baseMapper.getNos(no);
//                if (nos != null && nos.size() > 0) {
//                    String maxNo = nos.get(0);
//                    Integer pos = maxNo.lastIndexOf("-");
//                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
//                    Integer maxNoInt = Integer.valueOf(maxIdxStr);
//                    String noIdxStr = String.format("%04d", maxNoInt + 1);
//                    no = no + noIdxStr;
//                } else {
//                    no = no + "0001";
//                }
//                bdMaterial.setNumber(no);
//            }
//        }
//
//        // 成品
//        if (ObjectUtils.isNotEmpty(bdMaterial.getMaterialGroup()) && bdMaterial.getMaterialGroup() == 0) {
////                if (bdMaterial.getAuxes() == null || bdMaterial.getAuxes().size() == 0) {
////                    throw new BizException("未检测到辅料明细");
////                }
//            if (bdMaterial.getRaws() == null || bdMaterial.getRaws().size() == 0) {
//                throw new BizException("未检测到原材料明细");
//            }
//            if (bdMaterial.getRoutes() == null || bdMaterial.getRoutes().size() == 0) {
//                throw new BizException("未检测到工序明细");
//            }
//        }
//
//        // 图片附件
//        List<BdMaterialAttach> attaches = bdMaterial.getAttaches();
//        if (attaches != null && attaches.size() > 0) {
//            bdMaterial.setMainPicId(attaches.get(0).getAttachId());
//        }
//
//        bdMaterial.setUpdateTime(sdf.format(new Date()));
//        bdMaterial.setUpdaterId(RequestUtils.getUserId());
//        bdMaterial.setUpdater(RequestUtils.getNickname());
//        if (!this.updateById(bdMaterial)) {
//            throw new BizException("更新失败");
//        }
//
//        // 删除旧数据
//        // 删除-图片附件明细
//        List<BdMaterialAttach> oldAttaches = bdMaterialAttachService.list(
//                new LambdaQueryWrapper<BdMaterialAttach>()
//                        .eq(BdMaterialAttach::getPid, bdMaterial.getId())
//        );
//        if (oldAttaches != null && oldAttaches.size() > 0) {
//            List<String> oldAttachIds = oldAttaches.stream().map(BdMaterialAttach::getId).collect(Collectors.toList());
//            if (oldAttachIds != null && oldAttachIds.size() > 0) {
//                if (!bdMaterialAttachService.removeByIds(oldAttachIds)) {
//                    throw new BizException("物料图片更新失败");
//                }
//            }
//        }
//
//        // 删除-辅料明细
//        List<BdMaterialAux> oldAuxes = bdMaterialAuxService.list(
//                new LambdaQueryWrapper<BdMaterialAux>()
//                        .eq(BdMaterialAux::getPid, bdMaterial.getId())
//        );
//        if (oldAuxes != null && oldAuxes.size() > 0) {
//            List<String> oldAuxIds = oldAuxes.stream().map(BdMaterialAux::getId).collect(Collectors.toList());
//            if (oldAuxIds != null && oldAuxIds.size() > 0) {
//                if (!bdMaterialAuxService.removeByIds(oldAuxIds)) {
//                    throw new BizException("辅料明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 删除-原材料明细
//        List<BdMaterialRaw> oldRaws = bdMaterialRawService.list(
//                new LambdaQueryWrapper<BdMaterialRaw>()
//                        .eq(BdMaterialRaw::getPid, bdMaterial.getId())
//        );
//        if (oldRaws != null && oldRaws.size() > 0) {
//            List<String> oldRawIds = oldRaws.stream().map(BdMaterialRaw::getId).collect(Collectors.toList());
//            if (oldRawIds != null && oldRawIds.size() > 0) {
//                if (!bdMaterialRawService.removeByIds(oldRawIds)) {
//                    throw new BizException("原材料明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 删除-工序明细
//        List<BdMaterialRoute> oldRoutes = bdMaterialRouteService.list(
//                new LambdaQueryWrapper<BdMaterialRoute>()
//                        .eq(BdMaterialRoute::getPid, bdMaterial.getId())
//        );
//        if (oldRoutes != null && oldRoutes.size() > 0) {
//            List<String> oldRouteIds = oldRoutes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//            if (oldRouteIds != null && oldRouteIds.size() > 0) {
//                if (!bdMaterialRouteService.removeByIds(oldRouteIds)) {
//                    throw new BizException("工序明细更新失败，异常码1");
//                }
//            }
//
//            List<BdMaterialRouteSpecial> oldRouteSpecials = bdMaterialRouteSpecialService.list(
//                    new LambdaQueryWrapper<BdMaterialRouteSpecial>()
//                            .in(BdMaterialRouteSpecial::getPid, oldRouteIds)
//            );
//            if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
//                List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdMaterialRouteSpecial::getId).collect(Collectors.toList());
//                if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
//                    if (!bdMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
//                        throw new BizException("工序明细更新失败，异常码2");
//                    }
//                }
//            }
//        }
//
//        // 删除-工艺要求明细
//        List<BdMaterialProcess> oldProcesses = bdMaterialProcessService.list(
//                new LambdaQueryWrapper<BdMaterialProcess>()
//                        .eq(BdMaterialProcess::getPid, bdMaterial.getId())
//        );
//        if (oldProcesses != null && oldProcesses.size() > 0) {
//            List<String> oldProcessIds = oldProcesses.stream().map(BdMaterialProcess::getId).collect(Collectors.toList());
//            if (oldProcessIds != null && oldProcessIds.size() > 0) {
//                if (!bdMaterialProcessService.removeByIds(oldProcessIds)) {
//                    throw new BizException("工艺要求明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 删除-供应商明细
//        List<BdMaterialSupplier> oldSuppliers = bdMaterialSupplierService.list(
//                new LambdaQueryWrapper<BdMaterialSupplier>()
//                        .eq(BdMaterialSupplier::getPid, bdMaterial.getId())
//        );
//        if (oldSuppliers != null && oldSuppliers.size() > 0) {
//            List<String> oldSupplierIds = oldSuppliers.stream().map(BdMaterialSupplier::getId).collect(Collectors.toList());
//            if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
//                if (!bdMaterialSupplierService.removeByIds(oldSupplierIds)) {
//                    throw new BizException("供应商明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 删除-颜色/色号
//        List<BdMaterialColor> oldColors = bdMaterialColorService.list(
//                new LambdaQueryWrapper<BdMaterialColor>()
//                        .eq(BdMaterialColor::getPid, bdMaterial.getId())
//        );
//        if (oldColors != null && oldColors.size() > 0) {
//            List<String> oldColorIds = oldColors.stream().map(BdMaterialColor::getId).collect(Collectors.toList());
//            if (oldColorIds != null && oldColorIds.size() > 0) {
//                if (!bdMaterialColorService.removeByIds(oldColorIds)) {
//                    throw new BizException("颜色/色号明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 删除-规格
//        List<BdMaterialSpecification> oldSpecifications = bdMaterialSpecificationService.list(
//                new LambdaQueryWrapper<BdMaterialSpecification>()
//                        .eq(BdMaterialSpecification::getPid, bdMaterial.getId())
//        );
//        if (oldSpecifications != null && oldSpecifications.size() > 0) {
//            List<String> oldSpecificationIds = oldSpecifications.stream().map(BdMaterialSpecification::getId).collect(Collectors.toList());
//            if (oldSpecificationIds != null && oldSpecificationIds.size() > 0) {
//                if (!bdMaterialSpecificationService.removeByIds(oldSpecificationIds)) {
//                    throw new BizException("规格明细更新失败，异常码1");
//                }
//            }
//        }
//
//        // 保存新数据
//        // 图片附件
//        if (attaches != null && attaches.size() > 0) {
//            for (BdMaterialAttach attach : attaches) {
//                attach.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialAttachService.saveOrUpdateBatch(attaches)) {
//                throw new BizException("更新失败，异常码1");
//            }
//        }
//
//        // 辅料
//        List<BdMaterialAux> auxes = bdMaterial.getAuxes();
//        if (auxes != null && auxes.size() > 0) {
//            for (BdMaterialAux aux : auxes) {
//                aux.setPid(bdMaterial.getId());
//
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.selectList(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .eq(BdMaterialDetail::getPid, aux.getAuxMaterial().getId())
//                                .eq(StringUtils.isNotEmpty(aux.getColorId()), BdMaterialDetail::getColorId, aux.getColorId())
//                                .apply(StringUtils.isEmpty(aux.getColorId()), " (color_id is null or color_id = '')")
//                                .eq(StringUtils.isNotEmpty(aux.getSpecificationId()), BdMaterialDetail::getSpecificationId, aux.getSpecificationId())
//                                .apply(StringUtils.isEmpty(aux.getSpecificationId()), " (specification_id is null or specification_id = '')")
//                );
//                if (details != null && details.size() > 0) {
//                    aux.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialAuxService.saveOrUpdateBatch(auxes)) {
//                throw new BizException("更新失败，异常码2");
//            }
//        }
//
//        // 原材料
//        List<BdMaterialRaw> raws = bdMaterial.getRaws();
//        if (raws != null && raws.size() > 0) {
//            for (BdMaterialRaw raw : raws) {
//                raw.setPid(bdMaterial.getId());
//
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.selectList(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .eq(BdMaterialDetail::getPid, raw.getRawMaterial().getId())
//                                .eq(StringUtils.isNotEmpty(raw.getColorId()), BdMaterialDetail::getColorId, raw.getColorId())
//                                .apply(StringUtils.isEmpty(raw.getColorId()), " (color_id is null or color_id = '')")
//                                .eq(StringUtils.isNotEmpty(raw.getSpecificationId()), BdMaterialDetail::getSpecificationId, raw.getSpecificationId())
//                                .apply(StringUtils.isEmpty(raw.getSpecificationId()), " (specification_id is null or specification_id = '')")
//                );
//                if (details != null && details.size() > 0) {
//                    raw.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("原材料信息异常 编码 " + raw.getRawMaterial().getNumber() + " 名称 " + raw.getRawMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialRawService.saveOrUpdateBatch(raws)) {
//                throw new BizException("更新失败，异常码3");
//            }
//        }
//
//        // 工序
//        List<BdMaterialRoute> routes = bdMaterial.getRoutes();
//        if (routes != null && routes.size() > 0) {
//            for (BdMaterialRoute route : routes) {
//                route.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialRouteService.saveOrUpdateBatch(routes)) {
//                throw new BizException("更新失败，异常码4");
//            }
//
//            List<BdMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
//            for (BdMaterialRoute route : routes) {
//                List<BdMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
//                if (routeSpecials != null && routeSpecials.size() > 0) {
//                    for (BdMaterialRouteSpecial routeSpecial : routeSpecials) {
//                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
//                            routeSpecial.setPid(route.getId());
//                            newRouteSpecials.add(routeSpecial);
//                        }
//                    }
//                }
//            }
//            if (newRouteSpecials.size() > 0) {
//                if (!bdMaterialRouteSpecialService.saveOrUpdateBatch(newRouteSpecials)) {
//                    throw new BizException("更新失败，异常码5");
//                }
//            }
//        }
//
//        // 工艺要求
//        List<BdMaterialProcess> processes = bdMaterial.getProcesses();
//        if (processes != null && processes.size() > 0) {
//            for (BdMaterialProcess process : processes) {
//                process.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialProcessService.saveOrUpdateBatch(processes)) {
//                throw new BizException("更新失败，异常码6");
//            }
//        }
//
//        // 供应商
//        List<BdMaterialSupplier> suppliers = bdMaterial.getSuppliers();
//        if (suppliers != null && suppliers.size() > 0) {
//            for (BdMaterialSupplier supplier : suppliers) {
//                supplier.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialSupplierService.saveOrUpdateBatch(suppliers)) {
//                throw new BizException("更新失败，异常码7");
//            }
//        }
//
//        // 颜色/色号
//        List<BdMaterialColor> addClrs = new ArrayList<>();
//        List<BdMaterialColor> colors = bdMaterial.getColors();
//        if (colors == null || colors.size() == 0) {
//            BdMaterialColor newColor = new BdMaterialColor();
//            colors.add(newColor);
//        }
//        for (BdMaterialColor color : colors) {
//            color.setPid(bdMaterial.getId());
//
//            if (StringUtils.isEmpty(color.getCreateTime())) {
//                color.setCreateTime(sdf.format(new Date()));
//                color.setCreatorId(RequestUtils.getUserId());
//                color.setCreator(RequestUtils.getNickname());
//            }
//
//            addClrs.add(color);
//        }
//        if (addClrs.size() > 0) {
//            if (!bdMaterialColorService.saveBatch(addClrs)) {
//                throw new BizException("更新失败，异常码8");
//            }
//        }
//
//        // 规格
//        List<BdMaterialSpecification> addSpecs = new ArrayList<>();
//        List<BdMaterialSpecification> specifications = bdMaterial.getSpecifications();
//        if (specifications == null || specifications.size() == 0) {
//            BdMaterialSpecification newSpec = new BdMaterialSpecification();
//            specifications.add(newSpec);
//        }
//        for (BdMaterialSpecification specification : specifications) {
//            specification.setPid(bdMaterial.getId());
//
//            if (StringUtils.isEmpty(specification.getCreateTime())) {
//                specification.setCreateTime(sdf.format(new Date()));
//                specification.setCreatorId(RequestUtils.getUserId());
//                specification.setCreator(RequestUtils.getNickname());
//            }
//            addSpecs.add(specification);
//        }
//        if (addSpecs.size() > 0) {
//            if (!bdMaterialSpecificationService.saveBatch(addSpecs)) {
//                throw new BizException("更新失败，异常码9");
//            }
//        }
//
//        // 详细
//        List<BdMaterialDetail> oldDetails = bdMaterialDetailMapper.selectList(
//                new LambdaQueryWrapper<BdMaterialDetail>()
//                        .eq(BdMaterialDetail::getPid, bdMaterial.getId())
//        );
//        // 当前数据重复项
//        List<BdMaterialDetail> nowDetails = new ArrayList<>();
//        // 新增数据
//        List<BdMaterialDetail> addDetails = new ArrayList<>();
//        for (BdMaterialColor color : colors) {
//            for (BdMaterialSpecification specification : specifications) {
//                Boolean existBL = false;
//                if (oldDetails != null && oldDetails.size() > 0) {
//                    for (BdMaterialDetail oldDetail : oldDetails) {
//                        if (StringUtils.equals(oldDetail.getColorId(), color.getId()) && StringUtils.equals(oldDetail.getSpecificationId(), specification.getId())) {
//                            existBL = true;
//
//                            nowDetails.add(oldDetail);
//
//                            break;
//                        }
//                    }
//                }
//
//                if (existBL == false) {
//                    BdMaterialDetail mtrlDtl = new BdMaterialDetail();
//                    mtrlDtl.setPid(bdMaterial.getId());
//                    mtrlDtl.setColorId(color.getId());
//                    mtrlDtl.setSpecificationId(specification.getId());
//
//                    mtrlDtl.setCreateTime(sdf.format(new Date()));
//                    mtrlDtl.setCreatorId(RequestUtils.getUserId());
//                    mtrlDtl.setCreator(RequestUtils.getNickname());
//
//                    addDetails.add(mtrlDtl);
//                }
//            }
//        }
//        // 删除的项
//        List<String> existIds = nowDetails.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//        List<String> delIds = oldDetails.stream().filter(r -> !existIds.contains(r.getId())).map(BdMaterialDetail::getId).collect(Collectors.toList());
//        if (delIds != null && delIds.size() > 0) {
//            if (!bdMaterialDetailService.removeByIds(delIds)) {
//                throw new BizException("详细更新失败，异常码1");
//            }
//        }
//        if (addDetails.size() > 0) {
//            if (!bdMaterialDetailService.saveBatch(addDetails)) {
//                throw new BizException("保存失败，异常码10");
//            }
//        }
//
//        return bdMaterial;
//    }
//
//    Boolean chkIsUsed(List<String> ids) {
//        // 存货
//        List<BdMaterialDetail> details = bdMaterialDetailService.list(
//                new LambdaQueryWrapper<BdMaterialDetail>()
//                        .in(BdMaterialDetail::getPid, ids)
//        );
//        if (details != null && details.size() > 0) {
//            List<String> detailIds = details.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//
//            // 辅料
//            List<BdMaterialAux> auxes = bdMaterialAuxService.list(
//                    new LambdaQueryWrapper<BdMaterialAux>()
//                            .in(BdMaterialAux::getMaterialDetailId, detailIds)
//            );
//            if (auxes != null && auxes.size() > 0) {
//                throw new BizException("当前辅料已被成品引用，无法删除1");
//            }
//
//            // 原材料
//            List<BdMaterialRaw> raws = bdMaterialRawService.list(
//                    new LambdaQueryWrapper<BdMaterialRaw>()
//                            .in(BdMaterialRaw::getMaterialDetailId, detailIds)
//            );
//            if (raws != null && raws.size() > 0) {
//                throw new BizException("当前原材料已被成品引用，无法删除1");
//            }
//
//            // 生产订单-原材料
//            List<PrdMoMaterialDetail> moMaterialDetails = prdMoMaterialDetailService.list(
//                    new LambdaQueryWrapper<PrdMoMaterialDetail>()
//                            .in(PrdMoMaterialDetail::getMaterialDetailId, detailIds)
//            );
//            if (moMaterialDetails != moMaterialDetails && raws.size() > 0) {
//                throw new BizException("当前原材料已被生产订单引用，无法删除1");
//            }
//        }
//
//        // 存货-临时
//        List<BdTempMaterialDetail> tempDetails = bdTempMaterialDetailService.list(
//                new LambdaQueryWrapper<BdTempMaterialDetail>()
//                        .in(BdTempMaterialDetail::getPid, ids)
//        );
//        if (tempDetails != null && tempDetails.size() > 0) {
//            List<String> detailIds = tempDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
//
//            List<BdTempMaterialAux> auxes = bdTempMaterialAuxService.list(
//                    new LambdaQueryWrapper<BdTempMaterialAux>()
//                            .in(BdTempMaterialAux::getMaterialDetailId, detailIds)
//            );
//            if (auxes != null && auxes.size() > 0) {
//                throw new BizException("当前辅料已被成品引用，无法删除2");
//            }
//
//            List<BdTempMaterialRaw> raws = bdTempMaterialRawService.list(
//                    new LambdaQueryWrapper<BdTempMaterialRaw>()
//                            .in(BdTempMaterialRaw::getMaterialDetailId, detailIds)
//            );
//            if (raws != null && raws.size() > 0) {
//                throw new BizException("当前原材料已被成品引用，无法删除2");
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean deleteById(String id) {
//        BdMaterial bdMaterial = this.getById(id);
//        if (bdMaterial == null) {
//            return true;
//        }
//
//        if (bdMaterial.getStatus() == Constants.INT_STATUS_APPROVING || bdMaterial.getStatus() == Constants.INT_STATUS_AUDITED) {
//            throw new BizException("流转中 和 已审核 数据无法删除");
//        }
//
//        // 删除-图片附件明细
//        List<BdMaterialAttach> oldAttaches = bdMaterialAttachService.list(
//                new LambdaQueryWrapper<BdMaterialAttach>()
//                        .eq(BdMaterialAttach::getPid, bdMaterial.getId())
//        );
//        if (oldAttaches != null && oldAttaches.size() > 0) {
//            List<String> oldAttachIds = oldAttaches.stream().map(BdMaterialAttach::getId).collect(Collectors.toList());
//            if (oldAttachIds != null && oldAttachIds.size() > 0) {
//                if (!bdMaterialAttachService.removeByIds(oldAttachIds)) {
//                    throw new BizException("删除失败，异常码2");
//                }
//            }
//        }
//
//        List<String> ids = new ArrayList<>();
//        ids.add(id);
//        if (this.chkIsUsed(ids)) {
//            throw new BizException("存货已被引用");
//        }
//
//        if (!this.removeById(id)) {
//            throw new BizException("删除失败，异常码1");
//        }
//
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteBatch(String[] ids) {
//
//        List<String> idList = Arrays.asList(ids);
//        List<BdMaterial> list = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .in(BdMaterial::getId, idList)
//        );
//        if (!list.isEmpty()) {
//            List<String> delIds = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT).map(BdMaterial::getId).collect(Collectors.toList());
//            if (!delIds.isEmpty()) {
//                if (this.chkIsUsed(delIds)) {
//                    throw new BizException("存货已被引用");
//                }
//                if (!this.removeByIds(delIds)) {
//                    throw new BizException("删除失败，异常码1");
//                }
//
//                // 删除-图片附件明细
//                List<BdMaterialAttach> oldAttaches = bdMaterialAttachService.list(
//                        new LambdaQueryWrapper<BdMaterialAttach>()
//                                .in(BdMaterialAttach::getPid, delIds)
//                );
//                if (oldAttaches != null && oldAttaches.size() > 0) {
//                    List<String> oldAttachIds = oldAttaches.stream().map(BdMaterialAttach::getId).collect(Collectors.toList());
//                    if (oldAttachIds != null && oldAttachIds.size() > 0) {
//                        if (!bdMaterialAttachService.removeByIds(oldAttachIds)) {
//                            throw new BizException("删除失败，异常码2");
//                        }
//                    }
//                }
//            } else {
//                throw new BizException("流转中 及 已审核 数据无法删除");
//            }
//        }
//
//    }
//
//    @Override
//    public BdMaterial detail(String id) {
//
//        BdMaterial result = baseMapper.infoById(id);
//        if (result != null) {
//            // 图片附件
//            BdMaterialAttach bdMaterialAttach = new BdMaterialAttach();
//            bdMaterialAttach.setPid(result.getId());
//            List<BdMaterialAttach> attaches = bdMaterialAttachMapper.getList(bdMaterialAttach);
//            result.setAttaches(attaches);
//
//            // 辅料
//            BdMaterialAux bdMaterialAux = new BdMaterialAux();
//            bdMaterialAux.setPid(result.getId());
//            List<BdMaterialAux> auxes = bdMaterialAuxMapper.getList(bdMaterialAux);
//            if (auxes != null && auxes.size() > 0) {
//                for (BdMaterialAux aux : auxes) {
//                    if (aux.getAuxMaterial() != null && StringUtils.isNotEmpty(aux.getAuxMaterial().getMainPicId())) {
//                        SysFiles sysFile = sysFileService.getById(aux.getAuxMaterial().getMainPicId());
//                        aux.getAuxMaterial().setMainPic(sysFile);
//                    }
//                }
//            }
//            result.setAuxes(auxes);
//
//            // 颜色/色号
//            List<BdMaterialColor> colors = bdMaterialColorService.list(
//                    new LambdaQueryWrapper<BdMaterialColor>()
//                            .eq(BdMaterialColor::getPid, result.getId())
//                            .apply(" color is not null and color <> ''")
//            );
//            result.setColors(colors);
//            // 规格
//            List<BdMaterialSpecification> specifications = bdMaterialSpecificationService.list(
//                    new LambdaQueryWrapper<BdMaterialSpecification>()
//                            .eq(BdMaterialSpecification::getPid, result.getId())
//                            .apply(" specification is not null and specification <> ''")
//            );
//            result.setSpecifications(specifications);
//
//            // 详细
//            BdMaterialDetail mtrlDetail = new BdMaterialDetail();
//            mtrlDetail.setPid(result.getId());
//            List<BdMaterialDetail> details = bdMaterialDetailMapper.getList(mtrlDetail);
//            result.setDetails(details);
//
//        }
//
//        return result;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterial submit(String id) throws Exception {
//        BdMaterial result = this.detail(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getStatus() != Constants.INT_STATUS_CREATE && result.getStatus() != Constants.INT_STATUS_RESUBMIT) {
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
//    public BdMaterial auditUptData(BdMaterial bdMaterial) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        bdMaterial.setStatus(Constants.INT_STATUS_AUDITED);
//        bdMaterial.setAuditTime(sdf.format(new Date()));
//        bdMaterial.setAuditorId(RequestUtils.getUserId());
//        bdMaterial.setAuditor(RequestUtils.getNickname());
//
//        return bdMaterial;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchSubmitByIds(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<BdMaterial> list = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .in(BdMaterial::getId, idList)
//        );
//        if (list != null && list.size() > 0) {
//            // 过滤 创建/重新审核 且 启用 的数据
//            List<BdMaterial> submitList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
//            if (submitList != null && submitList.size() > 0) {
//                for (BdMaterial bdMaterial : submitList) {
//                    this.submit(bdMaterial.getId());
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
//    public Result doAction(BdMaterial bdMaterial) throws Exception {
//        if (bdMaterial.getStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(bdMaterial.getWorkFlow())) {
//            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
//            flowOperationInfo.setWorkFlowId(bdMaterial.getWorkFlowId());
//            flowOperationInfo.setFormData(bdMaterial);
//            flowOperationInfo.setUserId(bdMaterial.getUserId());
//            flowOperationInfo.setChildNodes(bdMaterial.getChildNodes());
//            flowOperationInfo.setCurrentNodeId(bdMaterial.getCurrentNodeId());
//            flowOperationInfo.setChildNodeApprovalResult(bdMaterial.getChildNodeApprovalResult());
//            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
//                // 提交
//                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
//                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
//                if (!start) {
//                    throw new BizException("流程提交错误");
//                }
//                bdMaterial.setWorkFlowId("");
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
//            ids.add(bdMaterial.getId());
//            List<ChildNode> currentNodes = getCurrentNodes(ids, bdMaterial.getWorkFlow().getId());
//            bdMaterial.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
//            bdMaterial.setNodeStatus(currentNodes.get(0).getStatus());
//            bdMaterial.setCurrentNodeId(currentNodes.get(0).getId());
//            // 审批流正常结束
//            if (circulationOperationService.whetherLast(bdMaterial.getId()) == 1) {
//                bdMaterial = this.auditUptData(bdMaterial);
//
//                return Result.success(bdMaterial);
//            }
//            // 驳回
//            if (circulationOperationService.whetherLast(bdMaterial.getId()) == 2) {
//                bdMaterial.setStatus(Constants.INT_STATUS_RESUBMIT);
//            }
//        }
//
//        if (!this.updateById(bdMaterial)) {
//            throw new BizException("操作失败");
//        }
//
//        return Result.success(bdMaterial);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result batchDoAction(BdMaterial bdMaterial) throws Exception {
//        List<String> ids = bdMaterial.getIds();
//        List<BdMaterial> materials = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .in(BdMaterial::getId, ids)
//        );
//        if (materials != null && materials.size() > 0) {
//            List<ChildNode> childNodes = getCurrentNodes(ids, bdMaterial.getWorkFlow().getId());
//            for (int i = 0; i < materials.size(); i++) {
//                BdMaterial item = materials.get(i);
//                item.setStatus(bdMaterial.getStatus());
//                item.setWorkFlowId(bdMaterial.getWorkFlowId());
//                item.setUserId(bdMaterial.getUserId());
//                item.setChildNodes(bdMaterial.getChildNodes());
//                item.setChildNodeApprovalResult(bdMaterial.getChildNodeApprovalResult());
//                item.setWorkFlow(bdMaterial.getWorkFlow());
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
//    public BdMaterial unAudit(String id) throws Exception {
//        BdMaterial result = this.detail(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getStatus() != Constants.INT_STATUS_AUDITED) {
//            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
//        }
//
//        result = this.unAuditUptData(result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterial unAuditUptData(BdMaterial bdMaterial) throws Exception {
//        bdMaterial.setStatus(Constants.INT_STATUS_RESUBMIT);
//        bdMaterial.setAuditTime(null);
//        bdMaterial.setAuditorId(null);
//        bdMaterial.setAuditor(null);
//        if (!this.updateById(bdMaterial)) {
//            throw new BizException("反审核失败");
//        }
//
//        // 生成临时数据
//        BdMaterial thisData = this.detail(bdMaterial.getId());
//        if (thisData != null) {
//            BdTempMaterial tempMaterial = new BdTempMaterial();
//            BeanUtils.copyProperties(tempMaterial, thisData);
//
//            tempMaterial.setOriginalId(tempMaterial.getId());
//            tempMaterial.setStatus(3);
//
//            IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
//            String id = identifierGenerator.nextId(new Object()).toString();
//            tempMaterial.setId(id);
//
//            if (bdTempMaterialMapper.insert(tempMaterial) < 1) {
//                throw new BizException("反审核失败，请重试");
//            }
//
//            // 附件
//            List<BdMaterialAttach> materialAttaches = thisData.getAttaches();
//            if (materialAttaches != null && materialAttaches.size() > 0) {
//                List<BdTempMaterialAttach> tempMaterialAttaches = new ArrayList<>();
//
//                for (BdMaterialAttach materialAttach : materialAttaches) {
//                    BdTempMaterialAttach tempMaterialAttach = new BdTempMaterialAttach();
//                    BeanUtils.copyProperties(tempMaterialAttach, materialAttach);
//
//                    tempMaterialAttach.setId(null);
//                    tempMaterialAttach.setPid(id);
//
//                    tempMaterialAttaches.add(tempMaterialAttach);
//                }
//                if (!bdTempMaterialAttachService.saveBatch(tempMaterialAttaches)) {
//                    throw new BizException("反审核失败，附件异常，请重试");
//                }
//            }
//
//            // 辅料
//            List<BdMaterialAux> materialAuxes = thisData.getAuxes();
//            if (materialAuxes != null && materialAuxes.size() > 0) {
//                List<BdTempMaterialAux> tempMaterialAuxes = new ArrayList<>();
//
//                for (BdMaterialAux materialAux : materialAuxes) {
//                    BdTempMaterialAux tempMaterialAux = new BdTempMaterialAux();
//                    BeanUtils.copyProperties(tempMaterialAux, materialAux);
//
//                    tempMaterialAux.setId(null);
//                    tempMaterialAux.setPid(id);
//
//                    tempMaterialAuxes.add(tempMaterialAux);
//                }
//                if (!bdTempMaterialAuxService.saveBatch(tempMaterialAuxes)) {
//                    throw new BizException("反审核失败，辅料异常，请重试");
//                }
//            }
//
//            // 颜色
//            List<BdMaterialColor> materialColors = thisData.getColors();
//            if (materialColors != null && materialColors.size() > 0) {
//                List<BdTempMaterialColor> tempMaterialColors = new ArrayList<>();
//
//                for (BdMaterialColor materialColor : materialColors) {
//                    BdTempMaterialColor tempMaterialColor = new BdTempMaterialColor();
//                    BeanUtils.copyProperties(tempMaterialColor, materialColor);
//
//                    tempMaterialColor.setId(null);
//                    tempMaterialColor.setPid(id);
//
//                    tempMaterialColors.add(tempMaterialColor);
//                }
//                if (!bdTempMaterialColorService.saveBatch(tempMaterialColors)) {
//                    throw new BizException("反审核失败，颜色/色号异常，请重试");
//                }
//            }
//
//            // 详情
//            List<BdMaterialDetail> materialDetails = thisData.getDetails();
//            if (materialDetails != null && materialDetails.size() > 0) {
//                List<BdTempMaterialDetail> tempMaterialDetails = new ArrayList<>();
//
//                for (BdMaterialDetail materialDetail : materialDetails) {
//                    BdTempMaterialDetail tempMaterialDetail = new BdTempMaterialDetail();
//                    BeanUtils.copyProperties(tempMaterialDetail, materialDetail);
//
//                    tempMaterialDetail.setId(null);
//                    tempMaterialDetail.setPid(id);
//
//                    tempMaterialDetails.add(tempMaterialDetail);
//                }
//                if (!bdTempMaterialDetailService.saveBatch(tempMaterialDetails)) {
//                    throw new BizException("反审核失败，明细异常，请重试");
//                }
//            }
//
//            // 工艺要求
//            List<BdMaterialProcess> materialProcesses = thisData.getProcesses();
//            if (materialProcesses != null && materialProcesses.size() > 0) {
//                List<BdTempMaterialProcess> tempMaterialProcesses = new ArrayList<>();
//
//                for (BdMaterialProcess materialProcess : materialProcesses) {
//                    BdTempMaterialProcess tempMaterialProcess = new BdTempMaterialProcess();
//                    BeanUtils.copyProperties(tempMaterialProcess, materialProcess);
//
//                    tempMaterialProcess.setId(null);
//                    tempMaterialProcess.setPid(id);
//
//                    tempMaterialProcesses.add(tempMaterialProcess);
//                }
//                if (!bdTempMaterialProcessService.saveBatch(tempMaterialProcesses)) {
//                    throw new BizException("反审核失败，工艺要求异常，请重试");
//                }
//            }
//
//            // 原材料
//            List<BdMaterialRaw> materialRaws = thisData.getRaws();
//            if (materialRaws != null && materialRaws.size() > 0) {
//                List<BdTempMaterialRaw> tempMaterialRaws = new ArrayList<>();
//
//                for (BdMaterialRaw materialRaw : materialRaws) {
//                    BdTempMaterialRaw tempMaterialRaw = new BdTempMaterialRaw();
//                    BeanUtils.copyProperties(tempMaterialRaw, materialRaw);
//
//                    tempMaterialRaw.setId(null);
//                    tempMaterialRaw.setPid(id);
//
//                    tempMaterialRaws.add(tempMaterialRaw);
//                }
//                if (!bdTempMaterialRawService.saveBatch(tempMaterialRaws)) {
//                    throw new BizException("反审核失败，原材料异常，请重试");
//                }
//            }
//
//            // 工艺路线
//            List<BdMaterialRoute> materialRoutes = thisData.getRoutes();
//            if (materialRoutes != null && materialRoutes.size() > 0) {
//                List<BdTempMaterialRoute> tempMaterialRoutes = new ArrayList<>();
//
//                for (BdMaterialRoute materialRoute : materialRoutes) {
//                    BdTempMaterialRoute tempMaterialRoute = new BdTempMaterialRoute();
//                    BeanUtils.copyProperties(tempMaterialRoute, materialRoute);
//
//                    String routeId = identifierGenerator.nextId(new Object()).toString();
//                    tempMaterialRoute.setId(routeId);
//                    tempMaterialRoute.setPid(id);
//
//                    // 特殊
//                    List<BdMaterialRouteSpecial> materialRouteSpecials = materialRoute.getRouteSpecials();
//                    if (materialRouteSpecials != null && materialRouteSpecials.size() > 0) {
//                        List<BdTempMaterialRouteSpecial> tempMaterialRouteSpecials = new ArrayList<>();
//
//                        for (BdMaterialRouteSpecial materialRouteSpecial : materialRouteSpecials) {
//                            BdTempMaterialRouteSpecial tempMaterialRouteSpecial = new BdTempMaterialRouteSpecial();
//                            BeanUtils.copyProperties(tempMaterialRouteSpecial, materialRouteSpecial);
//
//                            tempMaterialRouteSpecial.setId(null);
//                            tempMaterialRouteSpecial.setPid(routeId);
//
//                            tempMaterialRouteSpecials.add(tempMaterialRouteSpecial);
//                        }
//                        if (!bdTempMaterialRouteSpecialService.saveBatch(tempMaterialRouteSpecials)) {
//                            throw new BizException("反审核失败，特殊工艺路线异常，请重试");
//                        }
//                    }
//
//                    tempMaterialRoutes.add(tempMaterialRoute);
//                }
//                if (!bdTempMaterialRouteService.saveBatch(tempMaterialRoutes)) {
//                    throw new BizException("反审核失败，工艺路线异常，请重试");
//                }
//            }
//
//            // 规格型号
//            List<BdMaterialSpecification> materialSpecifications = thisData.getSpecifications();
//            if (materialSpecifications != null && materialSpecifications.size() > 0) {
//                List<BdTempMaterialSpecification> tempMaterialSpecifications = new ArrayList<>();
//
//                for (BdMaterialSpecification materialSpecification : materialSpecifications) {
//                    BdTempMaterialSpecification tempMaterialSpecification = new BdTempMaterialSpecification();
//                    BeanUtils.copyProperties(tempMaterialSpecification, materialSpecification);
//
//                    tempMaterialSpecification.setId(null);
//                    tempMaterialSpecification.setPid(id);
//
//                    tempMaterialSpecifications.add(tempMaterialSpecification);
//                }
//                if (!bdTempMaterialSpecificationService.saveBatch(tempMaterialSpecifications)) {
//                    throw new BizException("反审核失败，规格型号异常，请重试");
//                }
//            }
//
//            // 供应商
//            List<BdMaterialSupplier> materialSuppliers = thisData.getSuppliers();
//            if (materialSuppliers != null && materialSuppliers.size() > 0) {
//                List<BdTempMaterialSupplier> tempMaterialSuppliers = new ArrayList<>();
//
//                for (BdMaterialSupplier materialSupplier : materialSuppliers) {
//                    BdTempMaterialSupplier tempMaterialSupplier = new BdTempMaterialSupplier();
//                    BeanUtils.copyProperties(tempMaterialSupplier, materialSupplier);
//
//                    tempMaterialSupplier.setId(null);
//                    tempMaterialSupplier.setPid(id);
//
//                    tempMaterialSuppliers.add(tempMaterialSupplier);
//                }
//                if (!bdTempMaterialSupplierService.saveBatch(tempMaterialSuppliers)) {
//                    throw new BizException("反审核失败，供应商异常，请重试");
//                }
//            }
//
//            // 替换为临时id
//            bdMaterial.setOriginalId(bdMaterial.getId());
//            bdMaterial.setId(id);
//        } else {
//            throw new BizException("数据不存在");
//        }
//
//        return bdMaterial;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchUnAuditByIds(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<BdMaterial> list = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .in(BdMaterial::getId, idList)
//        );
//        if (list != null && list.size() > 0) {
//            List<BdMaterial> unAuditList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
//            if (unAuditList != null && unAuditList.size() > 0) {
//                for (BdMaterial bdMaterial : unAuditList) {
//                    this.unAudit(bdMaterial.getId());
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
//    public List<BdMaterialColor> listColor(BdMaterial bdMaterial) {
//        List<BdMaterialColor> list = bdMaterialColorService.list(
//                new LambdaQueryWrapper<BdMaterialColor>()
//                        .eq(BdMaterialColor::getPid, bdMaterial.getId())
//        );
//        return list;
//    }
//
//    @Override
//    public List<BdMaterialSpecification> listSpecification(BdMaterial bdMaterial) {
//        List<BdMaterialSpecification> list = bdMaterialSpecificationService.list(
//                new LambdaQueryWrapper<BdMaterialSpecification>()
//                        .eq(BdMaterialSpecification::getPid, bdMaterial.getId())
//        );
//        return list;
//    }
//
//    @Override
//    public List<BdMaterialSupplier> listSupplier(BdMaterial bdMaterial) {
//        BdMaterialSupplier srhMtrlSupp = new BdMaterialSupplier();
//        srhMtrlSupp.setPid(bdMaterial.getId());
//        List<BdMaterialSupplier> list = bdMaterialSupplierMapper.getList(srhMtrlSupp);
//        return list;
//    }
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterial saveFin(BdMaterial bdMaterial) throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        if (StringUtils.isEmpty(bdMaterial.getNumber())) {
//            throw new BizException("请填写 货品编码");
//        }
//
//        List<BdMaterial> list = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .eq(BdMaterial::getNumber, bdMaterial.getNumber())
//                        .eq(BdMaterial::getMaterialGroup, 0)
//        );
//        if (!list.isEmpty()) {
//            String thisId = list.get(0).getId();
//            if (StringUtils.isNotEmpty(bdMaterial.getId()) && !StringUtils.equals(bdMaterial.getId(), thisId)) {
//                // 源id没有其他明细时，销毁原id
//                List<BdMaterialDetail> thisDetails = bdMaterialDetailService.list(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .eq(BdMaterialDetail::getPid, bdMaterial.getId())
//                                .ne(StringUtils.isNotEmpty(bdMaterial.getMtrlDetailId()), BdMaterialDetail::getId, bdMaterial.getMtrlDetailId())
//                );
//                if (thisDetails.isEmpty()) {
//                    this.deleteByIdFin(bdMaterial.getId());
//                }
//            }
//
//            bdMaterial.setId(thisId);
//        } else {
//            if (StringUtils.isNotEmpty(bdMaterial.getId())) {
//                // 源id没有其他明细时，销毁原id
//                List<BdMaterialDetail> thisDetails = bdMaterialDetailService.list(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .eq(BdMaterialDetail::getPid, bdMaterial.getId())
//                                .ne(StringUtils.isNotEmpty(bdMaterial.getMtrlDetailId()), BdMaterialDetail::getId, bdMaterial.getMtrlDetailId())
//                );
//                if (thisDetails.isEmpty()) {
//                    this.deleteByIdFin(bdMaterial.getId());
//                }
//            }
//
//            bdMaterial.setId(null);
//        }
//
//        if (bdMaterial.getMaterialGroup() != null && bdMaterial.getMaterialGroup() == 0) {
//            if (bdMaterial.getRaws() == null || bdMaterial.getRaws().size() == 0) {
//                throw new BizException("未检测到原材料明细");
//            }
//            if (bdMaterial.getRoutes() == null || bdMaterial.getRoutes().size() == 0) {
//                throw new BizException("未检测到工序明细");
//            }
//        }
//
//        bdMaterial.setCreateTime(sdf.format(new Date()));
//        bdMaterial.setCreatorId(RequestUtils.getUserId());
//        bdMaterial.setCreator(RequestUtils.getNickname());
//        if (!this.saveOrUpdate(bdMaterial)) {
//            throw new BizException("保存失败");
//        }
//
//        // 颜色/色号
//        BdMaterialColor newColor = new BdMaterialColor();
//        List<BdMaterialColor> mtrlColors = bdMaterialColorService.list(
//                new LambdaQueryWrapper<BdMaterialColor>()
//                        .eq(BdMaterialColor::getPid, bdMaterial.getId())
//                        .eq(StringUtils.isNotEmpty(bdMaterial.getColor()), BdMaterialColor::getColor, bdMaterial.getColor())
//                        .apply(StringUtils.isEmpty(bdMaterial.getColor()), " (color is null or color = '')")
//        );
//        if (!mtrlColors.isEmpty()) {
//            BeanUtils.copyProperties(newColor, mtrlColors.get(0));
//
//            if (!bdMaterialColorService.updateById(newColor)) {
//                throw new BizException("保存失败，异常码1");
//            }
//        } else {
//            newColor.setPid(bdMaterial.getId());
//
//            newColor.setCreateTime(sdf.format(new Date()));
//            newColor.setCreatorId(RequestUtils.getUserId());
//            newColor.setCreator(RequestUtils.getNickname());
//
//            if (!bdMaterialColorService.save(newColor)) {
//                throw new BizException("保存失败，异常码2");
//            }
//        }
//
//        // 规格
//        BdMaterialSpecification newSpec = new BdMaterialSpecification();
//        List<BdMaterialSpecification> mtrlSpecs = bdMaterialSpecificationService.list(
//                new LambdaQueryWrapper<BdMaterialSpecification>()
//                        .eq(BdMaterialSpecification::getPid, bdMaterial.getId())
//                        .eq(StringUtils.isNotEmpty(bdMaterial.getSpecification()), BdMaterialSpecification::getSpecification, bdMaterial.getSpecification())
//                        .apply(StringUtils.isEmpty(bdMaterial.getSpecification()), " (specification is null or specification = '')")
//        );
//        if (!mtrlSpecs.isEmpty()) {
//            BeanUtils.copyProperties(newSpec, mtrlSpecs.get(0));
//
//            if (!bdMaterialSpecificationService.updateById(newSpec)) {
//                throw new BizException("保存失败，异常码3");
//            }
//        } else {
//            newSpec.setPid(bdMaterial.getId());
//
//            newSpec.setCreateTime(sdf.format(new Date()));
//            newSpec.setCreatorId(RequestUtils.getUserId());
//            newSpec.setCreator(RequestUtils.getNickname());
//
//            if (!bdMaterialSpecificationService.save(newSpec)) {
//                throw new BizException("保存失败，异常码4");
//            }
//        }
//
//        if (StringUtils.isNotEmpty(bdMaterial.getId()) && StringUtils.isNotEmpty(bdMaterial.getSjNumber())) {
//            // 商家编码不为空
//            List<BdMaterialDetail> details = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, bdMaterial.getId())
//                            .eq(BdMaterialDetail::getNumber, bdMaterial.getSjNumber())
//                            .ne(StringUtils.isNotEmpty(bdMaterial.getMtrlDetailId()), BdMaterialDetail::getId, bdMaterial.getMtrlDetailId())
//            );
//            if (!details.isEmpty()) {
//                throw new BizException("货品编码：" + bdMaterial.getNumber() + " 内已存在相同商家编码：" + bdMaterial.getSjNumber() + " 的成品，请修改商家编码");
//            }
//        }
//        if (StringUtils.isEmpty(bdMaterial.getSjNumber())) {
//            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH) + 1;
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//            String no = "SJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
//            List<String> nos = bdMaterialDetailMapper.getNos(no);
//            if (!nos.isEmpty()) {
//                String maxNo = nos.get(0);
//                Integer pos = maxNo.lastIndexOf("-");
//                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
//                Integer maxNoInt = Integer.valueOf(maxIdxStr);
//                String noIdxStr = String.format("%04d", maxNoInt + 1);
//                no = no + noIdxStr;
//            } else {
//                no = no + "0001";
//            }
//
//            bdMaterial.setSjNumber(no);
//        }
//
//        // 详情
//        BdMaterialDetail newDetail = new BdMaterialDetail();
//        newDetail.setStatus(Constants.INT_STATUS_CREATE);
//        List<BdMaterialDetail> mtrlDetails = bdMaterialDetailService.list(
//                new LambdaQueryWrapper<BdMaterialDetail>()
//                        .eq(BdMaterialDetail::getPid, bdMaterial.getId())
//                        .eq(BdMaterialDetail::getColorId, newColor.getId())
//                        .eq(BdMaterialDetail::getSpecificationId, newSpec.getId())
//        );
//        if (!mtrlDetails.isEmpty()) {
//            if (StringUtils.isNotEmpty(bdMaterial.getMtrlDetailId())) {
//                // 校验是否存在，不存在则覆盖其他数据
//                List<BdMaterialDetail> chkList = bdMaterialDetailService.list(
//                        new LambdaQueryWrapper<BdMaterialDetail>()
//                                .eq(BdMaterialDetail::getId, bdMaterial.getMtrlDetailId())
//                );
//                if (!chkList.isEmpty()) {
//                    for (BdMaterialDetail mtrlDetail : mtrlDetails) {
//                        if (!StringUtils.equals(mtrlDetail.getId(), bdMaterial.getMtrlDetailId())) {
//                            throw new BizException("已存在相同颜色规格的数据，无法重复");
//                        }
//                    }
//                }
//            }
//
//            BeanUtils.copyProperties(newDetail, mtrlDetails.get(0));
//            newDetail.setNumber(bdMaterial.getSjNumber());
//            newDetail.setPid(bdMaterial.getId());
//
//            if (!bdMaterialDetailService.updateById(newDetail)) {
//                throw new BizException("保存失败，异常码5");
//            }
//        } else if (StringUtils.isNotEmpty(bdMaterial.getMtrlDetailId())) {
//            mtrlDetails = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getId, bdMaterial.getMtrlDetailId())
//            );
//            if (!mtrlDetails.isEmpty()) {
//                BeanUtils.copyProperties(newDetail, mtrlDetails.get(0));
//                newDetail.setPid(bdMaterial.getId());
//                newDetail.setNumber(bdMaterial.getSjNumber());
//                newDetail.setColorId(newColor.getId());
//                newDetail.setSpecificationId(newSpec.getId());
//
//                if (!bdMaterialDetailService.updateById(newDetail)) {
//                    throw new BizException("保存失败，异常码5-1");
//                }
//            }
//        } else {
//            newDetail.setPid(bdMaterial.getId());
//            newDetail.setNumber(bdMaterial.getSjNumber());
//            newDetail.setColorId(newColor.getId());
//            newDetail.setSpecificationId(newSpec.getId());
//
//            newDetail.setCreateTime(sdf.format(new Date()));
//            newDetail.setCreatorId(RequestUtils.getUserId());
//            newDetail.setCreator(RequestUtils.getNickname());
//
//            if (!bdMaterialDetailService.save(newDetail)) {
//                throw new BizException("保存失败，异常码6");
//            }
//        }
//
//        // 图片附件
//        bdMaterialAttachService.remove(
//                new LambdaQueryWrapper<BdMaterialAttach>()
//                        .eq(BdMaterialAttach::getPid, newDetail.getId())
//        );
//        List<BdMaterialAttach> attaches = bdMaterial.getAttaches();
//        if (!attaches.isEmpty()) {
//            for (BdMaterialAttach attach : attaches) {
//                attach.setPid(newDetail.getId());
//            }
//
//            if (!bdMaterialAttachService.saveBatch(attaches)) {
//                throw new BizException("保存失败，异常码7");
//            }
//        }
//
//        // 辅料
//        bdMaterialAuxService.remove(
//                new LambdaQueryWrapper<BdMaterialAux>()
//                        .eq(BdMaterialAux::getPid, bdMaterial.getId())
//        );
//        List<BdMaterialAux> auxes = bdMaterial.getAuxes();
//        if (!auxes.isEmpty()) {
//            for (BdMaterialAux aux : auxes) {
//                aux.setPid(bdMaterial.getId());
//
//                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
//                srhMtrlDtl.setPid(aux.getAuxMaterial().getId());
//                srhMtrlDtl.setColor(aux.getColor());
//                srhMtrlDtl.setSpecification(aux.getSpecification());
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
//                if (!details.isEmpty()) {
//                    aux.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialAuxService.saveBatch(auxes)) {
//                throw new BizException("保存失败，异常码8");
//            }
//        }
//
//        // 原材料
//        bdMaterialRawService.remove(
//                new LambdaQueryWrapper<BdMaterialRaw>()
//                        .eq(BdMaterialRaw::getPid, newDetail.getId())
//        );
//        List<BdMaterialRaw> raws = bdMaterial.getRaws();
//        if (!raws.isEmpty()) {
//            for (BdMaterialRaw raw : raws) {
//                raw.setPid(newDetail.getId());
//
//                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
//                srhMtrlDtl.setPid(raw.getRawMaterial().getId());
//                srhMtrlDtl.setColor(raw.getColor());
//                srhMtrlDtl.setSpecification(raw.getSpecification());
//                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
//                if (!details.isEmpty()) {
//                    raw.setMaterialDetailId(details.get(0).getId());
//                } else {
//                    throw new BizException("原材料信息异常 编码 " + raw.getRawMaterial().getNumber() + " 名称 " + raw.getRawMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
//                }
//            }
//
//            if (!bdMaterialRawService.saveBatch(raws)) {
//                throw new BizException("保存失败，异常码9");
//            }
//        }
//
//        // 工序
//        List<BdMaterialRoute> oldRoutes = bdMaterialRouteService.list(
//                new LambdaQueryWrapper<BdMaterialRoute>()
//                        .eq(BdMaterialRoute::getPid, bdMaterial.getId())
//        );
//        if (!oldRoutes.isEmpty()) {
//            List<String> oldRouteIds = oldRoutes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//            bdMaterialRouteService.removeByIds(oldRouteIds);
//
//            bdMaterialRouteSpecialService.remove(
//                    new LambdaQueryWrapper<BdMaterialRouteSpecial>()
//                            .in(BdMaterialRouteSpecial::getPid, oldRouteIds)
//            );
//        }
//        List<BdMaterialRoute> routes = bdMaterial.getRoutes();
//        if (!routes.isEmpty()) {
//            for (BdMaterialRoute route : routes) {
//                route.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialRouteService.saveBatch(routes)) {
//                throw new BizException("保存失败，异常码10");
//            }
//
//            List<BdMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
//            for (BdMaterialRoute route : routes) {
//                List<BdMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
//                if (!routeSpecials.isEmpty()) {
//                    for (BdMaterialRouteSpecial routeSpecial : routeSpecials) {
//                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
//                            routeSpecial.setPid(route.getId());
//                            newRouteSpecials.add(routeSpecial);
//                        }
//                    }
//                }
//            }
//            if (!newRouteSpecials.isEmpty()) {
//                if (!bdMaterialRouteSpecialService.saveBatch(newRouteSpecials)) {
//                    throw new BizException("保存失败，异常码11");
//                }
//            }
//        }
//
//        // 供应商
//        bdMaterialSupplierService.remove(
//                new LambdaQueryWrapper<BdMaterialSupplier>()
//                        .eq(BdMaterialSupplier::getPid, bdMaterial.getId())
//        );
//        List<BdMaterialSupplier> suppliers = bdMaterial.getSuppliers();
//        if (!suppliers.isEmpty()) {
//            for (BdMaterialSupplier supplier : suppliers) {
//                supplier.setPid(bdMaterial.getId());
//            }
//
//            if (!bdMaterialSupplierService.saveBatch(suppliers)) {
//                throw new BizException("保存失败，异常码12");
//            }
//        }
//
//        return bdMaterial;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Boolean deleteByIdFin(String id) {
//            BdMaterialDetail mtrlDetail = bdMaterialDetailMapper.infoById(id);
//            if (mtrlDetail == null) {
//                return true;
//            }
//
//            if (mtrlDetail.getStatus() == Constants.INT_STATUS_APPROVING || mtrlDetail.getStatus() == Constants.INT_STATUS_AUDITED) {
//                throw new BizException("流转中 和 已审核 数据无法删除");
//            }
//
//            // 删除-图片附件明细
//            List<BdMaterialAttach> oldAttaches = bdMaterialAttachService.list(
//                    new LambdaQueryWrapper<BdMaterialAttach>()
//                            .eq(BdMaterialAttach::getPid, id)
//            );
//            if (oldAttaches != null && oldAttaches.size() > 0) {
//                List<String> oldAttachIds = oldAttaches.stream().map(BdMaterialAttach::getId).collect(Collectors.toList());
//                if (oldAttachIds != null && oldAttachIds.size() > 0) {
//                    if (!bdMaterialAttachService.removeByIds(oldAttachIds)) {
//                        throw new BizException("删除失败，异常码2");
//                    }
//                }
//            }
//
//            List<String> ids = new ArrayList<>();
//            ids.add(id);
//            if (this.chkIsUsedFin(ids)) {
//                throw new BizException("存货已被引用");
//            }
//
//            if (!bdMaterialDetailService.removeById(id)) {
//                throw new BizException("删除失败，异常码1");
//            }
//
//            // 删除-原材料明细
//            List<BdMaterialRaw> oldRaws = bdMaterialRawService.list(
//                    new LambdaQueryWrapper<BdMaterialRaw>()
//                            .eq(BdMaterialRaw::getPid, id)
//            );
//            if (oldRaws != null && oldRaws.size() > 0) {
//                List<String> oldRawIds = oldRaws.stream().map(BdMaterialRaw::getId).collect(Collectors.toList());
//                if (oldRawIds != null && oldRawIds.size() > 0) {
//                    if (!bdMaterialRawService.removeByIds(oldRawIds)) {
//                        throw new BizException("删除失败，异常码3");
//                    }
//                }
//            }
//
//            List<BdMaterialDetail> details = bdMaterialDetailService.list(
//                    new LambdaQueryWrapper<BdMaterialDetail>()
//                            .eq(BdMaterialDetail::getPid, mtrlDetail.getPid())
//                            .ne(BdMaterialDetail::getId, id)
//            );
//            if (details.isEmpty()) {
//                if (this.removeById(mtrlDetail.getPid())) {
//                    throw new BizException("删除失败，异常码4");
//                }
//
//                // 删除-辅料明细
//                List<BdMaterialAux> oldAuxes = bdMaterialAuxService.list(
//                        new LambdaQueryWrapper<BdMaterialAux>()
//                                .eq(BdMaterialAux::getPid, mtrlDetail.getPid())
//                );
//                if (oldAuxes != null && oldAuxes.size() > 0) {
//                    List<String> oldAuxIds = oldAuxes.stream().map(BdMaterialAux::getId).collect(Collectors.toList());
//                    if (oldAuxIds != null && oldAuxIds.size() > 0) {
//                        if (!bdMaterialAuxService.removeByIds(oldAuxIds)) {
//                            throw new BizException("删除失败，异常码5");
//                        }
//                    }
//                }
//
//                // 删除-工序明细
//                List<BdMaterialRoute> oldRoutes = bdMaterialRouteService.list(
//                        new LambdaQueryWrapper<BdMaterialRoute>()
//                                .eq(BdMaterialRoute::getPid, mtrlDetail.getPid())
//                );
//                if (oldRoutes != null && oldRoutes.size() > 0) {
//                    List<String> oldRouteIds = oldRoutes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//                    if (oldRouteIds != null && oldRouteIds.size() > 0) {
//                        if (!bdMaterialRouteService.removeByIds(oldRouteIds)) {
//                            throw new BizException("删除失败，异常码6");
//                        }
//                    }
//
//                    List<BdMaterialRouteSpecial> oldRouteSpecials = bdMaterialRouteSpecialService.list(
//                            new LambdaQueryWrapper<BdMaterialRouteSpecial>()
//                                    .in(BdMaterialRouteSpecial::getPid, oldRouteIds)
//                    );
//                    if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
//                        List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdMaterialRouteSpecial::getId).collect(Collectors.toList());
//                        if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
//                            if (!bdMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
//                                throw new BizException("删除失败，异常码7");
//                            }
//                        }
//                    }
//                }
//
//                // 删除-供应商明细
//                List<BdMaterialSupplier> oldSuppliers = bdMaterialSupplierService.list(
//                        new LambdaQueryWrapper<BdMaterialSupplier>()
//                                .eq(BdMaterialSupplier::getPid, mtrlDetail.getPid())
//                );
//                if (oldSuppliers != null && oldSuppliers.size() > 0) {
//                    List<String> oldSupplierIds = oldSuppliers.stream().map(BdMaterialSupplier::getId).collect(Collectors.toList());
//                    if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
//                        if (!bdMaterialSupplierService.removeByIds(oldSupplierIds)) {
//                            throw new BizException("删除失败，异常码8");
//                        }
//                    }
//                }
//            }
//
//        return true;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteBatchFin(String[] ids) {
//        List<String> idList = Arrays.asList(ids);
//
//        List<BdMaterialDetail> list = bdMaterialDetailService.listByIds(idList);
//        if (!list.isEmpty()) {
//            List<BdMaterialDetail> filters = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT).collect(Collectors.toList());
//            List<String> delIds = filters.stream().map(BdMaterialDetail::getId).collect(Collectors.toList());
//            if (!delIds.isEmpty()) {
//                if (this.chkIsUsedFin(delIds)) {
//                    throw new BizException("存货已被引用");
//                }
//                if (!bdMaterialDetailService.removeByIds(delIds)) {
//                    throw new BizException("删除失败，异常码1");
//                }
//
//                // 删除-图片附件明细
//                List<BdMaterialAttach> oldAttaches = bdMaterialAttachService.list(
//                        new LambdaQueryWrapper<BdMaterialAttach>()
//                                .in(BdMaterialAttach::getPid, delIds)
//                );
//                if (oldAttaches != null && oldAttaches.size() > 0) {
//                    List<String> oldAttachIds = oldAttaches.stream().map(BdMaterialAttach::getId).collect(Collectors.toList());
//                    if (oldAttachIds != null && oldAttachIds.size() > 0) {
//                        if (!bdMaterialAttachService.removeByIds(oldAttachIds)) {
//                            throw new BizException("删除失败，异常码2");
//                        }
//                    }
//                }
//
//                // 删除-原材料明细
//                List<BdMaterialRaw> oldRaws = bdMaterialRawService.list(
//                        new LambdaQueryWrapper<BdMaterialRaw>()
//                                .in(BdMaterialRaw::getPid, delIds)
//                );
//                if (oldRaws != null && oldRaws.size() > 0) {
//                    List<String> oldRawIds = oldRaws.stream().map(BdMaterialRaw::getId).collect(Collectors.toList());
//                    if (oldRawIds != null && oldRawIds.size() > 0) {
//                        if (!bdMaterialRawService.removeByIds(oldRawIds)) {
//                            throw new BizException("删除失败，异常码3");
//                        }
//                    }
//                }
//
//                List<String> pids = new ArrayList<>();
//                for (BdMaterialDetail filter : filters) {
//                    List<BdMaterialDetail> details = bdMaterialDetailService.list(
//                            new LambdaQueryWrapper<BdMaterialDetail>()
//                                    .eq(BdMaterialDetail::getPid, filter.getPid())
//                                    .ne(BdMaterialDetail::getId, filter.getId())
//                    );
//                    if (details.isEmpty()) {
//                        pids.add(filter.getPid());
//                    }
//                }
//                if (!pids.isEmpty()) {
//                    if (!this.removeByIds(pids)) {
//                        throw new BizException("删除失败，异常码4");
//                    }
//
//                    // 删除-辅料明细
//                    List<BdMaterialAux> oldAuxes = bdMaterialAuxService.list(
//                            new LambdaQueryWrapper<BdMaterialAux>()
//                                    .in(BdMaterialAux::getPid, pids)
//                    );
//                    if (oldAuxes != null && oldAuxes.size() > 0) {
//                        List<String> oldAuxIds = oldAuxes.stream().map(BdMaterialAux::getId).collect(Collectors.toList());
//                        if (oldAuxIds != null && oldAuxIds.size() > 0) {
//                            if (!bdMaterialAuxService.removeByIds(oldAuxIds)) {
//                                throw new BizException("删除失败，异常码5");
//                            }
//                        }
//                    }
//
//                    // 删除-工序明细
//                    List<BdMaterialRoute> oldRoutes = bdMaterialRouteService.list(
//                            new LambdaQueryWrapper<BdMaterialRoute>()
//                                    .in(BdMaterialRoute::getPid, pids)
//                    );
//                    if (oldRoutes != null && oldRoutes.size() > 0) {
//                        List<String> oldRouteIds = oldRoutes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());
//                        if (oldRouteIds != null && oldRouteIds.size() > 0) {
//                            if (!bdMaterialRouteService.removeByIds(oldRouteIds)) {
//                                throw new BizException("删除失败，异常码6");
//                            }
//                        }
//
//                        List<BdMaterialRouteSpecial> oldRouteSpecials = bdMaterialRouteSpecialService.list(
//                                new LambdaQueryWrapper<BdMaterialRouteSpecial>()
//                                        .in(BdMaterialRouteSpecial::getPid, oldRouteIds)
//                        );
//                        if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
//                            List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdMaterialRouteSpecial::getId).collect(Collectors.toList());
//                            if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
//                                if (!bdMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
//                                    throw new BizException("删除失败，异常码7");
//                                }
//                            }
//                        }
//                    }
//
//                    // 删除-供应商明细
//                    List<BdMaterialSupplier> oldSuppliers = bdMaterialSupplierService.list(
//                            new LambdaQueryWrapper<BdMaterialSupplier>()
//                                    .in(BdMaterialSupplier::getPid, pids)
//                    );
//                    if (oldSuppliers != null && oldSuppliers.size() > 0) {
//                        List<String> oldSupplierIds = oldSuppliers.stream().map(BdMaterialSupplier::getId).collect(Collectors.toList());
//                        if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
//                            if (!bdMaterialSupplierService.removeByIds(oldSupplierIds)) {
//                                throw new BizException("删除失败，异常码8");
//                            }
//                        }
//                    }
//                }
//
//            } else {
//                throw new BizException("流转中 及 已审核 数据无法删除");
//            }
//        }
//
//    }
//
//    @Override
//    public BdMaterial detailFin(String id) {
//
//        BdMaterialDetail mtrlDetail = bdMaterialDetailMapper.infoById(id);
//        if (mtrlDetail == null) {
//            return null;
//        }
//
//        String pid = mtrlDetail.getPid();
//        BdMaterial result = baseMapper.infoById(pid);
//        if (result != null) {
//            List<BdMaterialDetail> details = new ArrayList<>();
//            details.add(mtrlDetail);
//            result.setDetails(details);
//            result.setMtrlDetailId(mtrlDetail.getId());
//
//            // 图片附件
//            BdMaterialAttach bdMaterialAttach = new BdMaterialAttach();
//            bdMaterialAttach.setPid(result.getId());
//            List<BdMaterialAttach> attaches = bdMaterialAttachMapper.getList(bdMaterialAttach);
//            result.setAttaches(attaches);
//
//            // 颜色/色号
//            List<BdMaterialColor> colors = new ArrayList<>();
//            BdMaterialColor color = bdMaterialColorService.getById(mtrlDetail.getColorId());
//            colors.add(color);
//            result.setColors(colors);
//            // 规格
//            List<BdMaterialSpecification> specifications = new ArrayList<>();
//            BdMaterialSpecification specification = bdMaterialSpecificationService.getById(mtrlDetail.getSpecificationId());
//            specifications.add(specification);
//            result.setSpecifications(specifications);
//
//            // 原材料
//            BdMaterialRaw bdMaterialRaw = new BdMaterialRaw();
//            bdMaterialRaw.setPid(mtrlDetail.getId());
//            List<BdMaterialRaw> raws = bdMaterialRawMapper.getList(bdMaterialRaw);
//            if (raws != null && raws.size() > 0) {
//                for (BdMaterialRaw raw : raws) {
//                    if (raw.getRawMaterial() != null && StringUtils.isNotEmpty(raw.getRawMaterial().getMainPicId())) {
//                        SysFiles sysFile = sysFileService.getById(raw.getRawMaterial().getMainPicId());
//                        raw.getRawMaterial().setMainPic(sysFile);
//                    }
//                }
//            }
//            result.setRaws(raws);
//
//            // 辅料
//            BdMaterialAux bdMaterialAux = new BdMaterialAux();
//            bdMaterialAux.setPid(result.getId());
//            List<BdMaterialAux> auxes = bdMaterialAuxMapper.getList(bdMaterialAux);
//            if (auxes != null && auxes.size() > 0) {
//                for (BdMaterialAux aux : auxes) {
//                    if (aux.getAuxMaterial() != null && StringUtils.isNotEmpty(aux.getAuxMaterial().getMainPicId())) {
//                        SysFiles sysFile = sysFileService.getById(aux.getAuxMaterial().getMainPicId());
//                        aux.getAuxMaterial().setMainPic(sysFile);
//                    }
//                }
//            }
//            result.setAuxes(auxes);
//
//            // 工序
//            BdMaterialRoute bdMaterialRoute = new BdMaterialRoute();
//            bdMaterialRoute.setPid(result.getId());
//            List<BdMaterialRoute> routes = bdMaterialRouteMapper.getList(bdMaterialRoute);
//            if (routes != null && routes.size() > 0) {
//                for (BdMaterialRoute route : routes) {
//                    if (route.getRouteSpecials() != null && route.getRouteSpecials().size() > 0) {
//                        for (BdMaterialRouteSpecial routeSpecial : route.getRouteSpecials()) {
//                            SysUser staff = sysUserService.getById(routeSpecial.getStaffId());
//                            routeSpecial.setStaff(staff);
//                        }
//                    }
//                }
//            }
//            result.setRoutes(routes);
//
//            // 供应商
//            BdMaterialSupplier bdMaterialSupplier = new BdMaterialSupplier();
//            bdMaterialSupplier.setPid(result.getId());
//            List<BdMaterialSupplier> suppliers = bdMaterialSupplierMapper.getList(bdMaterialSupplier);
//            result.setSuppliers(suppliers);
//
//            // 工艺要求
//            BdMaterialProcess bdMaterialProcess = new BdMaterialProcess();
//            bdMaterialProcess.setPid(result.getId());
//            List<BdMaterialProcess> processes = bdMaterialProcessMapper.getList(bdMaterialProcess);
//            result.setProcesses(processes);
//        }
//
//        return result;
//
//    }
//
//    Boolean chkIsUsedFin(List<String> ids) {
//        // 生产订单
//        List<PrdMoColorSpecData> moColorSpecDataList = prdMoColorSpecDataMapper.selectList(
//                new LambdaQueryWrapper<PrdMoColorSpecData>()
//                        .in(PrdMoColorSpecData::getProductId, ids)
//        );
//        if (moColorSpecDataList != null && moColorSpecDataList.size() > 0) {
//            throw new BizException("当前成品已被生产订单引用，无法删除");
//        }
//
//        return false;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterialDetail submitFin(String id) throws Exception {
//        BdMaterialDetail result = bdMaterialDetailService.getById(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getStatus() != Constants.INT_STATUS_CREATE && result.getStatus() != Constants.INT_STATUS_RESUBMIT) {
//            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
//        }
//
//        result = this.auditUptDataFin(result);
//        if (!bdMaterialDetailService.updateById(result)) {
//            throw new BizException("操作失败");
//        }
//
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterialDetail auditUptDataFin(BdMaterialDetail bdMaterialDetail) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        bdMaterialDetail.setStatus(Constants.INT_STATUS_AUDITED);
//        bdMaterialDetail.setAuditTime(sdf.format(new Date()));
//        bdMaterialDetail.setAuditorId(RequestUtils.getUserId());
//        bdMaterialDetail.setAuditor(RequestUtils.getNickname());
//
//        return bdMaterialDetail;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchSubmitByIdsFin(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<BdMaterial> list = this.list(
//                new LambdaQueryWrapper<BdMaterial>()
//                        .in(BdMaterial::getId, idList)
//        );
//        if (list != null && list.size() > 0) {
//            // 过滤 创建/重新审核 且 启用 的数据
//            List<BdMaterial> submitList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
//            if (submitList != null && submitList.size() > 0) {
//                for (BdMaterial bdMaterial : submitList) {
//                    this.submit(bdMaterial.getId());
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
//    public Result doActionFin(BdMaterialDetail bdMaterialDetail) throws Exception {
//        if (bdMaterialDetail.getStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(bdMaterialDetail.getWorkFlow())) {
//            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
//            flowOperationInfo.setWorkFlowId(bdMaterialDetail.getWorkFlowId());
//            flowOperationInfo.setFormData(bdMaterialDetail);
//            flowOperationInfo.setUserId(bdMaterialDetail.getUserId());
//            flowOperationInfo.setChildNodes(bdMaterialDetail.getChildNodes());
//            flowOperationInfo.setCurrentNodeId(bdMaterialDetail.getCurrentNodeId());
//            flowOperationInfo.setChildNodeApprovalResult(bdMaterialDetail.getChildNodeApprovalResult());
//            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
//                // 提交
//                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
//                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
//                if (!start) {
//                    throw new BizException("流程提交错误");
//                }
//                bdMaterialDetail.setWorkFlowId("");
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
//            ids.add(bdMaterialDetail.getId());
//            List<ChildNode> currentNodes = getCurrentNodes(ids, bdMaterialDetail.getWorkFlow().getId());
//            bdMaterialDetail.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
//            bdMaterialDetail.setNodeStatus(currentNodes.get(0).getStatus());
//            bdMaterialDetail.setCurrentNodeId(currentNodes.get(0).getId());
//            // 审批流正常结束
//            if (circulationOperationService.whetherLast(bdMaterialDetail.getId()) == 1) {
//                bdMaterialDetail = this.auditUptDataFin(bdMaterialDetail);
//
//                return Result.success(bdMaterialDetail);
//            }
//            // 驳回
//            if (circulationOperationService.whetherLast(bdMaterialDetail.getId()) == 2) {
//                bdMaterialDetail.setStatus(Constants.INT_STATUS_RESUBMIT);
//            }
//        }
//
//        if (!bdMaterialDetailService.updateById(bdMaterialDetail)) {
//            throw new BizException("操作失败");
//        }
//
//        return Result.success(bdMaterialDetail);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Result batchDoActionFin(BdMaterialDetail bdMaterialDetail) throws Exception {
//        List<String> ids = bdMaterialDetail.getIds();
//        List<BdMaterialDetail> details = bdMaterialDetailService.list(
//                new LambdaQueryWrapper<BdMaterialDetail>()
//                        .in(BdMaterialDetail::getId, ids)
//        );
//        if (details != null && details.size() > 0) {
//            List<ChildNode> childNodes = getCurrentNodes(ids, bdMaterialDetail.getWorkFlow().getId());
//            for (int i = 0; i < details.size(); i++) {
//                BdMaterialDetail item = details.get(i);
//                item.setStatus(bdMaterialDetail.getStatus());
//                item.setWorkFlowId(bdMaterialDetail.getWorkFlowId());
//                item.setUserId(bdMaterialDetail.getUserId());
//                item.setChildNodes(bdMaterialDetail.getChildNodes());
//                item.setChildNodeApprovalResult(bdMaterialDetail.getChildNodeApprovalResult());
//                item.setWorkFlow(bdMaterialDetail.getWorkFlow());
//                for (int j = 0; j < childNodes.size(); j++) {
//                    if (childNodes.get(j).getFromId().equals(item.getId())) {
//                        item.setWorkFlowInstantiateStatus(childNodes.get(j).getWorkFlowInstantiateStatus());
//                        item.setNodeStatus(childNodes.get(j).getStatus());
//                        item.setCurrentNodeId(childNodes.get(j).getId());
//                        break;
//                    }
//                }
//                Result result = doActionFin(item);
//                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
//                    throw new BizException("操作失败");
//                }
//            }
//        }
//
//        return Result.success("操作成功");
//    }
//
//    @Override
//    public BdMaterialDetail unAuditFin(String id) throws Exception {
//        BdMaterialDetail result = bdMaterialDetailService.getById(id);
//        if (result == null) {
//            throw new BizException("未检索到数据");
//        }
//        if (result.getStatus() != Constants.INT_STATUS_AUDITED) {
//            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
//        }
//
//        result = this.unAuditUptDataFin(result);
//        return result;
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public BdMaterialDetail unAuditUptDataFin(BdMaterialDetail bdMaterialDetail) throws Exception {
//        bdMaterialDetail.setStatus(Constants.INT_STATUS_RESUBMIT);
//        bdMaterialDetail.setAuditTime(null);
//        bdMaterialDetail.setAuditorId(null);
//        bdMaterialDetail.setAuditor(null);
//        if (!bdMaterialDetailService.updateById(bdMaterialDetail)) {
//            throw new BizException("反审核失败");
//        }
//
//        return bdMaterialDetail;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchUnAuditByIdsFin(String[] ids) throws Exception {
//        List<String> idList = Arrays.asList(ids);
//        List<BdMaterialDetail> list = bdMaterialDetailService.list(
//                new LambdaQueryWrapper<BdMaterialDetail>()
//                        .in(BdMaterialDetail::getId, idList)
//        );
//        if (list != null && list.size() > 0) {
//            List<BdMaterialDetail> unAuditList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
//            if (unAuditList != null && unAuditList.size() > 0) {
//                for (BdMaterialDetail bdMaterialDetail : unAuditList) {
//                    this.unAuditFin(bdMaterialDetail.getId());
//                }
//            }
//
//            return "反审核成功";
//        } else {
//            throw new BizException("未选择数据");
//        }
//    }
//
//}
