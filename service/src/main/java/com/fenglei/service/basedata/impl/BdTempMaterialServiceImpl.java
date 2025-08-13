package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.*;
import com.fenglei.model.basedata.*;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.*;
import com.fenglei.service.system.ISysUserService;
import com.fenglei.service.system.SysFilesService;
import com.fenglei.service.workFlow.CirculationOperationService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdTempMaterialServiceImpl extends ServiceImpl<BdTempMaterialMapper, BdTempMaterial> implements BdTempMaterialService {

    @Resource
    private BdTempMaterialAttachService bdTempMaterialAttachService;
    @Resource
    private BdTempMaterialAuxService bdTempMaterialAuxService;
    @Resource
    private BdTempMaterialRawService bdTempMaterialRawService;
    @Resource
    private BdTempMaterialProcessService bdTempMaterialProcessService;
    @Resource
    private BdTempMaterialRouteService bdTempMaterialRouteService;
    @Resource
    private BdTempMaterialRouteSpecialService bdTempMaterialRouteSpecialService;
    @Resource
    private BdTempMaterialSupplierService bdTempMaterialSupplierService;
    @Resource
    private BdProcedureService bdProcedureService;
    @Resource
    private BdSupplierService bdSupplierService;
    @Resource
    private SysFilesService sysFileService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private BdTempMaterialColorService bdTempMaterialColorService;
    @Resource
    private BdTempMaterialSpecificationService bdTempMaterialSpecificationService;
    @Resource
    private BdTempMaterialDetailService bdTempMaterialDetailService;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private BdMaterialService bdMaterialService;
    @Resource
    private BdMaterialAttachService bdMaterialAttachService;
    @Resource
    private BdMaterialRouteService bdMaterialRouteService;
    @Resource
    private BdMaterialAuxService bdMaterialAuxService;
    @Resource
    private BdMaterialRawService bdMaterialRawService;
    @Resource
    private BdMaterialSupplierService bdMaterialSupplierService;
    @Resource
    private BdMaterialRouteSpecialService bdMaterialRouteSpecialService;
    @Resource
    private BdMaterialProcessService bdMaterialProcessService;

    @Resource
    private BdMaterialMapper bdMaterialMapper;
    @Resource
    private BdMaterialDetailMapper bdMaterialDetailMapper;

    @Resource
    private BdTempMaterialAttachMapper bdTempMaterialAttachMapper;
    @Resource
    private BdTempMaterialRouteMapper bdTempMaterialRouteMapper;
    @Resource
    private BdTempMaterialAuxMapper bdTempMaterialAuxMapper;
    @Resource
    private BdTempMaterialRawMapper bdTempMaterialRawMapper;
    @Resource
    private BdTempMaterialSupplierMapper bdTempMaterialSupplierMapper;
    @Resource
    private BdTempMaterialRouteSpecialMapper bdTempMaterialRouteSpecialMapper;
    @Resource
    private BdTempMaterialProcessMapper bdTempMaterialProcessMapper;
    @Resource
    private BdTempMaterialDetailMapper bdTempMaterialDetailMapper;

    @Override
    public IPage<BdTempMaterial> myPage(Page page, BdTempMaterial bdTempMaterial) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdTempMaterial.getAuxMaterialId())) {
            List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
                    new LambdaQueryWrapper<BdTempMaterialDetail>()
                            .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getAuxMaterialId())
            );
            if (mtrlDetails != null && mtrlDetails.size() > 0) {
                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
                List<BdTempMaterialAux> auxes = bdTempMaterialAuxService.list(
                        new LambdaQueryWrapper<BdTempMaterialAux>()
                                .in(BdTempMaterialAux::getMaterialDetailId, mtrlDetailIds)
                );
                if (auxes != null && auxes.size() > 0) {
                    for (BdTempMaterialAux aux : auxes) {
                        if (!pids.contains(aux.getPid())) {
                            pids.add(aux.getPid());
                        }
                    }
                } else {
                    if (pids.size() == 0) {
                        pids.add("-1");
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getRawMaterialId())) {
            List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
                    new LambdaQueryWrapper<BdTempMaterialDetail>()
                            .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getRawMaterialId())
            );
            if (mtrlDetails != null && mtrlDetails.size() > 0) {
                List<String> mtrlDetailIds = mtrlDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
                List<BdTempMaterialRaw> raws = bdTempMaterialRawService.list(
                        new LambdaQueryWrapper<BdTempMaterialRaw>()
                                .in(BdTempMaterialRaw::getMaterialDetailId, mtrlDetailIds)
                );
                if (raws != null && raws.size() > 0) {
                    for (BdTempMaterialRaw raw : raws) {
                        if (!pids.contains(raw.getPid())) {
                            pids.add(raw.getPid());
                        }
                    }
                } else {
                    if (pids.size() == 0) {
                        pids.add("-1");
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getProcedureId())) {
            List<BdTempMaterialRoute> routes = bdTempMaterialRouteService.list(
                    new LambdaQueryWrapper<BdTempMaterialRoute>()
                            .eq(BdTempMaterialRoute::getProcedureId, bdTempMaterial.getProcedureId())
            );
            if (routes != null && routes.size() > 0) {
                for (BdTempMaterialRoute route : routes) {
                    if (!pids.contains(route.getPid())) {
                        pids.add(route.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getStaffId())) {
            BdTempMaterialRouteSpecial routeSpecial = new BdTempMaterialRouteSpecial();
            routeSpecial.setStaffId(bdTempMaterial.getStaffId());
            List<BdTempMaterialRoute> routes = bdTempMaterialRouteMapper.getListWithSpecial(routeSpecial);
            if (routes != null && routes.size() > 0) {
                for (BdTempMaterialRoute route : routes) {
                    if (!pids.contains(route.getPid())) {
                        pids.add(route.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getSupplierId())) {
            List<BdTempMaterialSupplier> suppliers = bdTempMaterialSupplierService.list(
                    new LambdaQueryWrapper<BdTempMaterialSupplier>()
                            .eq(BdTempMaterialSupplier::getSupplierId, bdTempMaterial.getSupplierId())
            );
            if (suppliers != null && suppliers.size() > 0) {
                for (BdTempMaterialSupplier supplier : suppliers) {
                    if (!pids.contains(supplier.getPid())) {
                        pids.add(supplier.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getProcess())) {
            List<BdTempMaterialProcess> processes = bdTempMaterialProcessService.list(
                    new LambdaQueryWrapper<BdTempMaterialProcess>()
                            .like(BdTempMaterialProcess::getProcess, bdTempMaterial.getProcess())
            );
            if (processes != null && processes.size() > 0) {
                for (BdTempMaterialProcess process : processes) {
                    if (!pids.contains(process.getPid())) {
                        pids.add(process.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getColor())) {
            List<BdTempMaterialColor> colors = bdTempMaterialColorService.list(
                    new LambdaQueryWrapper<BdTempMaterialColor>()
                            .like(BdTempMaterialColor::getColor, bdTempMaterial.getColor())
            );
            if (colors != null && colors.size() > 0) {
                for (BdTempMaterialColor color : colors) {
                    if (!pids.contains(color.getPid())) {
                        pids.add(color.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getSpecification())) {
            List<BdTempMaterialSpecification> specifications = bdTempMaterialSpecificationService.list(
                    new LambdaQueryWrapper<BdTempMaterialSpecification>()
                            .like(BdTempMaterialSpecification::getSpecification, bdTempMaterial.getSpecification())
            );
            if (specifications != null && specifications.size() > 0) {
                for (BdTempMaterialSpecification specification : specifications) {
                    if (!pids.contains(specification.getPid())) {
                        pids.add(specification.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        bdTempMaterial.setIds(pids);

        IPage<BdTempMaterial> iPage = baseMapper.getPage(page, bdTempMaterial);
        if (iPage != null) {
            List<BdTempMaterial> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().map(BdTempMaterial::getId).collect(Collectors.toList());

                // 附件
                BdTempMaterialAttach bdTempMaterialAttach = new BdTempMaterialAttach();
                bdTempMaterialAttach.setPids(ids);
                List<BdTempMaterialAttach> attaches = bdTempMaterialAttachMapper.getList(bdTempMaterialAttach);

                // 辅料
                BdTempMaterialAux bdTempMaterialAux = new BdTempMaterialAux();
                bdTempMaterialAux.setPids(ids);
                List<BdTempMaterialAux> auxes = bdTempMaterialAuxMapper.getList(bdTempMaterialAux);

                // 原材料
                BdTempMaterialRaw bdTempMaterialRaw = new BdTempMaterialRaw();
                bdTempMaterialRaw.setPids(ids);
                List<BdTempMaterialRaw> raws = bdTempMaterialRawMapper.getList(bdTempMaterialRaw);

                // 供应商
                BdTempMaterialSupplier bdTempMaterialSupplier = new BdTempMaterialSupplier();
                bdTempMaterialSupplier.setPids(ids);
                List<BdTempMaterialSupplier> suppliers = bdTempMaterialSupplierMapper.getList(bdTempMaterialSupplier);

                // 工序
                BdTempMaterialRoute bdTempMaterialRoute = new BdTempMaterialRoute();
                bdTempMaterialRoute.setPids(ids);
                List<BdTempMaterialRoute> routes = bdTempMaterialRouteMapper.getList(bdTempMaterialRoute);
                if (routes != null && routes.size() > 0) {
                    List<String> routeIds = routes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
                    BdTempMaterialRouteSpecial routeSpecial = new BdTempMaterialRouteSpecial();
                    routeSpecial.setPids(routeIds);
                    List<BdTempMaterialRouteSpecial> routeSpecials = bdTempMaterialRouteSpecialMapper.getList(routeSpecial);
                    if (routeSpecials != null && routeSpecials.size() > 0) {
                        for (BdTempMaterialRoute route : routes) {
                            List<BdTempMaterialRouteSpecial> myRouteSpecials = routeSpecials.stream().filter(r -> r.getPid().equals(route.getId())).collect(Collectors.toList());
                            route.setRouteSpecials(myRouteSpecials);
                        }
                    }
                }

                // 工艺要求
                BdTempMaterialProcess bdTempMaterialProcess = new BdTempMaterialProcess();
                bdTempMaterialProcess.setPids(ids);
                List<BdTempMaterialProcess> processes = bdTempMaterialProcessMapper.getList(bdTempMaterialProcess);

                // 颜色/色号
                List<BdTempMaterialColor> colors = bdTempMaterialColorService.list(
                        new LambdaQueryWrapper<BdTempMaterialColor>()
                                .in(BdTempMaterialColor::getPid, ids)
                                .apply(" color is not null and color <> ''")
                );

                // 规格
                List<BdTempMaterialSpecification> specifications = bdTempMaterialSpecificationService.list(
                        new LambdaQueryWrapper<BdTempMaterialSpecification>()
                                .in(BdTempMaterialSpecification::getPid, ids)
                                .apply(" specification is not null and specification <> ''")
                );

                // 详细
                BdTempMaterialDetail mtrlDetail = new BdTempMaterialDetail();
                mtrlDetail.setPids(ids);
                List<BdTempMaterialDetail> details = bdTempMaterialDetailMapper.getList(mtrlDetail);

                for (BdTempMaterial record : records) {
                    if (attaches != null && attaches.size() > 0) {
                        List<BdTempMaterialAttach> myAttaches = attaches.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setAttaches(myAttaches);

                        List<String> urls = new ArrayList<>();
                        if (myAttaches != null && myAttaches.size() > 0) {
                            for (BdTempMaterialAttach attach : myAttaches) {
                                urls.add(attach.getSysFile().getUrl());
                            }
                        }
                        record.setAttachUrls(urls);
                    }

                    if (auxes != null && auxes.size() > 0) {
                        List<BdTempMaterialAux> myAuxes = auxes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setAuxes(myAuxes);
                    }

                    if (raws != null && raws.size() > 0) {
                        List<BdTempMaterialRaw> myRaws = raws.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setRaws(myRaws);
                    }

                    if (suppliers != null && suppliers.size() > 0) {
                        List<BdTempMaterialSupplier> mySuppliers = suppliers.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setSuppliers(mySuppliers);
                    }

                    if (routes != null && routes.size() > 0) {
                        List<BdTempMaterialRoute> myRoutes = routes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setRoutes(myRoutes);
                    }

                    if (processes != null && processes.size() > 0) {
                        List<BdTempMaterialProcess> myProcesses = processes.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setProcesses(myProcesses);
                    }

                    if (colors != null && colors.size() > 0) {
                        List<BdTempMaterialColor> myColors = colors.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setColors(myColors);
                    }

                    if (specifications != null && specifications.size() > 0) {
                        List<BdTempMaterialSpecification> mySpecifications = specifications.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setSpecifications(mySpecifications);
                    }

                    if (details != null && details.size() > 0) {
                        List<BdTempMaterialDetail> myDetails = details.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setDetails(myDetails);
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public List<BdTempMaterial> myList(BdTempMaterial bdTempMaterial) {
        List<String> pids = new ArrayList<>();

        if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdTempMaterial.getAuxMaterialId())) {
            List<String> materialIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter())) {
                List<BdTempMaterial> materials = this.list(
                        new LambdaQueryWrapper<BdTempMaterial>()
                                .like(BdTempMaterial::getNumber, bdTempMaterial.getCommFilter())
                                .or()
                                .like(BdTempMaterial::getName, bdTempMaterial.getCommFilter())
                                .or()
                                .apply(" concat(number, ' ', name) like concat('%', '" + bdTempMaterial.getCommFilter() + "', '%')")
                );
                if (materials != null && materials.size() > 0) {
                    for (BdTempMaterial material : materials) {
                        materialIds.add(material.getId());
                    }
                } else {
                    materialIds.add("-1");
                }
            }

            if (bdTempMaterial.getMaterialGroup() == 0) {
                // 成品
                for (String materialId : materialIds) {
                    pids.add(materialId);
                }
            } else {
                List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
                        new LambdaQueryWrapper<BdTempMaterialDetail>()
                                .in(materialIds != null && materialIds.size() > 0, BdTempMaterialDetail::getPid, materialIds)
                                .eq(StringUtils.isNotEmpty(bdTempMaterial.getAuxMaterialId()), BdTempMaterialDetail::getPid, bdTempMaterial.getAuxMaterialId())
                );
                if (mtrlDetails != null && mtrlDetails.size() > 0) {
                    List<String> mtrlDetailIds = mtrlDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());

                    if (bdTempMaterial.getMaterialGroup() == 1) {
                        // 辅料id
                        List<BdTempMaterialAux> auxes = bdTempMaterialAuxService.list(
                                new LambdaQueryWrapper<BdTempMaterialAux>()
                                        .in(BdTempMaterialAux::getMaterialDetailId, mtrlDetailIds)
                        );
                        if (auxes != null && auxes.size() > 0) {
                            for (BdTempMaterialAux aux : auxes) {
                                if (!pids.contains(aux.getPid())) {
                                    pids.add(aux.getPid());
                                }
                            }
                        }
                    }
                    if (bdTempMaterial.getMaterialGroup() == 2) {
                        // 原材料id
                        if (StringUtils.isNotEmpty(bdTempMaterial.getRawMaterialId())) {
                            List<BdTempMaterialRaw> raws = bdTempMaterialRawService.list(
                                    new LambdaQueryWrapper<BdTempMaterialRaw>()
                                            .in(BdTempMaterialRaw::getMaterialDetailId, mtrlDetailIds)
                            );
                            if (raws != null && raws.size() > 0) {
                                for (BdTempMaterialRaw raw : raws) {
                                    if (!pids.contains(raw.getPid())) {
                                        pids.add(raw.getPid());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        // 工序
        if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdTempMaterial.getProcedureId())) {
            List<String> procedureIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter())) {
                List<BdProcedure> procedures = bdProcedureService.list(
                        new LambdaQueryWrapper<BdProcedure>()
                                .like(BdProcedure::getNumber, bdTempMaterial.getCommFilter())
                                .or()
                                .like(BdProcedure::getName, bdTempMaterial.getCommFilter())
                                .or()
                                .apply(" concat(number, ' ', name) like concat('%', '" + bdTempMaterial.getCommFilter() + "', '%')")
                );
                if (procedures != null && procedures.size() > 0) {
                    for (BdProcedure procedure : procedures) {
                        procedureIds.add(procedure.getId());
                    }
                } else {
                    procedureIds.add("-1");
                }
            }
            List<BdTempMaterialRoute> materialRoutes = bdTempMaterialRouteService.list(
                    new LambdaQueryWrapper<BdTempMaterialRoute>()
                            .in(procedureIds != null && procedureIds.size() > 0, BdTempMaterialRoute::getProcedureId, procedureIds)
                            .eq(StringUtils.isNotEmpty(bdTempMaterial.getProcedureId()), BdTempMaterialRoute::getProcedureId, bdTempMaterial.getProcedureId())
            );
            if (materialRoutes != null && materialRoutes.size() > 0) {
                for (BdTempMaterialRoute route : materialRoutes) {
                    if (!pids.contains(route.getPid())) {
                        pids.add(route.getPid());
                    }
                }
            }
            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        // 供应商id
        if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdTempMaterial.getSupplierId())) {
            List<String> supplierIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter())) {
                List<BdSupplier> suppliers = bdSupplierService.list(
                        new LambdaQueryWrapper<BdSupplier>()
                                .like(BdSupplier::getNumber, bdTempMaterial.getCommFilter())
                                .or()
                                .like(BdSupplier::getName, bdTempMaterial.getCommFilter())
                                .or()
                                .apply(" concat(number, ' ', name) like concat('%', '" + bdTempMaterial.getCommFilter() + "', '%')")
                );
                if (suppliers != null && suppliers.size() > 0) {
                    for (BdSupplier supplier : suppliers) {
                        supplierIds.add(supplier.getId());
                    }
                } else {
                    supplierIds.add("-1");
                }
            }
            List<BdTempMaterialSupplier> materialSuppliers = bdTempMaterialSupplierService.list(
                    new LambdaQueryWrapper<BdTempMaterialSupplier>()
                            .in(supplierIds != null && supplierIds.size() > 0, BdTempMaterialSupplier::getSupplierId, supplierIds)
                            .eq(StringUtils.isNotEmpty(bdTempMaterial.getSupplierId()), BdTempMaterialSupplier::getSupplierId, bdTempMaterial.getSupplierId())
            );
            if (materialSuppliers != null && materialSuppliers.size() > 0) {
                for (BdTempMaterialSupplier materialSupplier : materialSuppliers) {
                    if (!pids.contains(materialSupplier.getPid())) {
                        pids.add(materialSupplier.getPid());
                    }
                }
            }
            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        // 工艺要求
        if (StringUtils.isNotEmpty(bdTempMaterial.getCommFilter()) || StringUtils.isNotEmpty(bdTempMaterial.getProcess())) {
            List<BdTempMaterialProcess> processes = bdTempMaterialProcessService.list(
                    new LambdaQueryWrapper<BdTempMaterialProcess>()
                            .like(StringUtils.isNotEmpty(bdTempMaterial.getCommFilter()), BdTempMaterialProcess::getProcess, bdTempMaterial.getCommFilter())
                            .like(StringUtils.isNotEmpty(bdTempMaterial.getProcess()), BdTempMaterialProcess::getProcess, bdTempMaterial.getProcess())
            );
            if (processes != null && processes.size() > 0) {
                for (BdTempMaterialProcess process : processes) {
                    if (!pids.contains(process.getPid())) {
                        pids.add(process.getPid());
                    }
                }
            }
            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        bdTempMaterial.setIds(pids);

        List<BdTempMaterial> list = baseMapper.getList(bdTempMaterial);

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempMaterial add(BdTempMaterial bdTempMaterial) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTempMaterial.getOriginalId())) {
            if (StringUtils.isNotEmpty(bdTempMaterial.getNumber())) {
                List<BdMaterial> list = bdMaterialService.list(
                        new LambdaQueryWrapper<BdMaterial>()
                                .eq(BdMaterial::getNumber, bdTempMaterial.getNumber())
                                .ne(BdMaterial::getId, bdTempMaterial.getOriginalId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的物料档案");
                }
            } else {
                BdMaterial material = bdMaterialService.getById(bdTempMaterial.getOriginalId());
                if (material != null) {
                    bdTempMaterial.setNumber(material.getNumber());
                }
            }
        }
        if (StringUtils.isEmpty(bdTempMaterial.getNumber())) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "";
            if (bdTempMaterial.getMaterialGroup() == 0) {
                no = "CP" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            } else if (bdTempMaterial.getMaterialGroup() == 1) {
                no = "FL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            } else if (bdTempMaterial.getMaterialGroup() == 2) {
                no = "YCL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            }
            List<String> nos = bdMaterialMapper.getNos(no);
            if (nos != null && nos.size() > 0) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdTempMaterial.setNumber(no);
        }

        // 成品
        if (ObjectUtils.isNotEmpty(bdTempMaterial.getMaterialGroup()) && bdTempMaterial.getMaterialGroup() == 0) {
//                if (bdTempMaterial.getAuxes() == null || bdTempMaterial.getAuxes().size() == 0) {
//                    throw new BizException("未检测到辅料明细");
//                }
            if (bdTempMaterial.getRaws() == null || bdTempMaterial.getRaws().size() == 0) {
                throw new BizException("未检测到原材料明细");
            }
            if (bdTempMaterial.getRoutes() == null || bdTempMaterial.getRoutes().size() == 0) {
                throw new BizException("未检测到工序明细");
            }
        }

        // 图片附件
        List<BdTempMaterialAttach> attaches = bdTempMaterial.getAttaches();
        if (attaches != null && attaches.size() > 0) {
            bdTempMaterial.setMainPicId(attaches.get(0).getAttachId());
        }

        bdTempMaterial.setStatus(Constants.INT_STATUS_CREATE);
        bdTempMaterial.setCreateTime(sdf.format(new Date()));
        bdTempMaterial.setCreatorId(RequestUtils.getUserId());
        bdTempMaterial.setCreator(RequestUtils.getNickname());
        if (!this.save(bdTempMaterial)) {
            throw new BizException("保存失败");
        }

        // 图片附件
        if (attaches != null && attaches.size() > 0) {
            for (BdTempMaterialAttach attach : attaches) {
                attach.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialAttachService.saveBatch(attaches)) {
                throw new BizException("保存失败，异常码1");
            }
        }
        // 辅料
        List<BdTempMaterialAux> auxes = bdTempMaterial.getAuxes();
        if (auxes != null && auxes.size() > 0) {
            for (BdTempMaterialAux aux : auxes) {
                aux.setPid(bdTempMaterial.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(aux.getAuxMaterial().getId());
                srhMtrlDtl.setColor(aux.getColor());
                srhMtrlDtl.setSpecification(aux.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    aux.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }

            if (!bdTempMaterialAuxService.saveBatch(auxes)) {
                throw new BizException("保存失败，异常码2");
            }
        }
        // 原材料
        List<BdTempMaterialRaw> raws = bdTempMaterial.getRaws();
        if (raws != null && raws.size() > 0) {
            for (BdTempMaterialRaw raw : raws) {
                raw.setPid(bdTempMaterial.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(raw.getRawMaterial().getId());
                srhMtrlDtl.setColor(raw.getColor());
                srhMtrlDtl.setSpecification(raw.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    raw.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("原材料信息异常  编码：" + raw.getRawMaterial().getNumber() + " 名称：" + raw.getRawMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }

            if (!bdTempMaterialRawService.saveBatch(raws)) {
                throw new BizException("保存失败，异常码3");
            }
        }
        // 工序
        List<BdTempMaterialRoute> routes = bdTempMaterial.getRoutes();
        if (routes != null && routes.size() > 0) {
            for (BdTempMaterialRoute route : routes) {
                route.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialRouteService.saveBatch(routes)) {
                throw new BizException("保存失败，异常码4");
            }

            List<BdTempMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
            for (BdTempMaterialRoute route : routes) {
                List<BdTempMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
                if (routeSpecials != null && routeSpecials.size() > 0) {
                    for (BdTempMaterialRouteSpecial routeSpecial : routeSpecials) {
                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
                            routeSpecial.setPid(route.getId());
                            newRouteSpecials.add(routeSpecial);
                        }
                    }
                }
            }
            if (newRouteSpecials.size() > 0) {
                if (!bdTempMaterialRouteSpecialService.saveBatch(newRouteSpecials)) {
                    throw new BizException("保存失败，异常码5");
                }
            }
        }
        // 工艺要求
        List<BdTempMaterialProcess> processes = bdTempMaterial.getProcesses();
        if (processes != null && processes.size() > 0) {
            for (BdTempMaterialProcess process : processes) {
                process.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialProcessService.saveBatch(processes)) {
                throw new BizException("保存失败，异常码6");
            }
        }
        // 供应商
        List<BdTempMaterialSupplier> suppliers = bdTempMaterial.getSuppliers();
        if (suppliers != null && suppliers.size() > 0) {
            for (BdTempMaterialSupplier supplier : suppliers) {
                supplier.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialSupplierService.saveBatch(suppliers)) {
                throw new BizException("保存失败，异常码7");
            }
        }
        // 颜色/色号
        List<BdTempMaterialColor> colors = bdTempMaterial.getColors();
        if (colors == null || colors.size() == 0) {
            BdTempMaterialColor newColor = new BdTempMaterialColor();
            colors.add(newColor);
        }
        for (BdTempMaterialColor color : colors) {
            color.setPid(bdTempMaterial.getId());

            color.setCreateTime(sdf.format(new Date()));
            color.setCreatorId(RequestUtils.getUserId());
            color.setCreator(RequestUtils.getNickname());
        }

        if (!bdTempMaterialColorService.saveBatch(colors)) {
            throw new BizException("保存失败，异常码8");
        }
        // 规格
        List<BdTempMaterialSpecification> specifications = bdTempMaterial.getSpecifications();
        if (specifications == null || specifications.size() == 0) {
            BdTempMaterialSpecification newSpec = new BdTempMaterialSpecification();
            specifications.add(newSpec);
        }
        for (BdTempMaterialSpecification specification : specifications) {
            specification.setPid(bdTempMaterial.getId());

            specification.setCreateTime(sdf.format(new Date()));
            specification.setCreatorId(RequestUtils.getUserId());
            specification.setCreator(RequestUtils.getNickname());
        }

        if (!bdTempMaterialSpecificationService.saveBatch(specifications)) {
            throw new BizException("保存失败，异常码9");
        }
        // 详细
        List<BdTempMaterialDetail> details = new ArrayList<>();
        for (BdTempMaterialColor color : colors) {
            for (BdTempMaterialSpecification specification : specifications) {
                BdTempMaterialDetail mtrlDtl = new BdTempMaterialDetail();
                mtrlDtl.setPid(bdTempMaterial.getId());
                mtrlDtl.setColorId(color.getId());
                mtrlDtl.setSpecificationId(specification.getId());

                mtrlDtl.setCreateTime(sdf.format(new Date()));
                mtrlDtl.setCreatorId(RequestUtils.getUserId());
                mtrlDtl.setCreator(RequestUtils.getNickname());

                details.add(mtrlDtl);
            }
        }
        if (details != null && details.size() > 0) {
            if (!bdTempMaterialDetailService.saveBatch(details)) {
                throw new BizException("保存失败，异常码10");
            }
        }

        return bdTempMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempMaterial saveFin(BdTempMaterial bdTempMaterial) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isEmpty(bdTempMaterial.getNumber())) {
            throw new BizException("请填写 货品编码");
        }

        List<BdMaterial> list = bdMaterialService.list(
                new LambdaQueryWrapper<BdMaterial>()
                        .eq(BdMaterial::getNumber, bdTempMaterial.getNumber())
                        .eq(BdMaterial::getMaterialGroup, 0)
        );
        if (!list.isEmpty()) {
            bdTempMaterial.setOriginalId(list.get(0).getId());

            // 存在正式源单时，一个人同一时间只能有一张修改
            List<BdTempMaterial> tempList = this.list(
                    new LambdaQueryWrapper<BdTempMaterial>()
                            .eq(BdTempMaterial::getOriginalId, bdTempMaterial.getOriginalId())
                            .eq(BdTempMaterial::getCreatorId, RequestUtils.getUserId())
            );
            if (!tempList.isEmpty()) {
                bdTempMaterial.setId(tempList.get(0).getId());
            } else {
                bdTempMaterial.setId(null);
            }
        } else {
            bdTempMaterial.setOriginalId(null);
        }

        if (StringUtils.isNotEmpty(bdTempMaterial.getId())) {
            // 源id没有其他明细时，销毁原id
            List<BdTempMaterialDetail> thisDetails = bdTempMaterialDetailService.list(
                    new LambdaQueryWrapper<BdTempMaterialDetail>()
                            .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getId())
                            .ne(StringUtils.isNotEmpty(bdTempMaterial.getMtrlDetailId()), BdTempMaterialDetail::getId, bdTempMaterial.getMtrlDetailId())
            );
            if (thisDetails.isEmpty()) {
                this.deleteById(bdTempMaterial.getId());
            }
        }

        if (bdTempMaterial.getMaterialGroup() != null && bdTempMaterial.getMaterialGroup() == 0) {
            if (bdTempMaterial.getRaws() == null || bdTempMaterial.getRaws().size() == 0) {
                throw new BizException("未检测到原材料明细");
            }
            if (bdTempMaterial.getRoutes() == null || bdTempMaterial.getRoutes().size() == 0) {
                throw new BizException("未检测到工序明细");
            }
        }

        // 主图
        List<BdTempMaterialAttach> attaches = bdTempMaterial.getAttaches();
        if (attaches != null && attaches.size() > 0) {
            bdTempMaterial.setMainPicId(attaches.get(0).getAttachId());
        }

        bdTempMaterial.setStatus(Constants.INT_STATUS_CREATE);
        bdTempMaterial.setCreateTime(sdf.format(new Date()));
        bdTempMaterial.setCreatorId(RequestUtils.getUserId());
        bdTempMaterial.setCreator(RequestUtils.getNickname());
        if (!this.saveOrUpdate(bdTempMaterial)) {
            throw new BizException("保存失败");
        }

        // 颜色/色号
        BdTempMaterialColor newColor = new BdTempMaterialColor();
        List<BdTempMaterialColor> mtrlColors = bdTempMaterialColorService.list(
                new LambdaQueryWrapper<BdTempMaterialColor>()
                        .eq(BdTempMaterialColor::getPid, bdTempMaterial.getId())
                        .eq(StringUtils.isNotEmpty(bdTempMaterial.getColor()), BdTempMaterialColor::getColor, bdTempMaterial.getColor())
                        .apply(StringUtils.isEmpty(bdTempMaterial.getColor()), " (color is null or color = '')")
        );
        if (!mtrlColors.isEmpty()) {
            BeanUtils.copyProperties(newColor, mtrlColors.get(0));

            if (!bdTempMaterialColorService.updateById(newColor)) {
                throw new BizException("保存失败，异常码1");
            }
        } else {
            newColor.setPid(bdTempMaterial.getId());

            newColor.setCreateTime(sdf.format(new Date()));
            newColor.setCreatorId(RequestUtils.getUserId());
            newColor.setCreator(RequestUtils.getNickname());

            if (!bdTempMaterialColorService.save(newColor)) {
                throw new BizException("保存失败，异常码2");
            }
        }

        // 规格
        BdTempMaterialSpecification newSpec = new BdTempMaterialSpecification();
        List<BdTempMaterialSpecification> mtrlSpecs = bdTempMaterialSpecificationService.list(
                new LambdaQueryWrapper<BdTempMaterialSpecification>()
                        .eq(BdTempMaterialSpecification::getPid, bdTempMaterial.getId())
                        .eq(StringUtils.isNotEmpty(bdTempMaterial.getSpecification()), BdTempMaterialSpecification::getSpecification, bdTempMaterial.getSpecification())
                        .apply(StringUtils.isEmpty(bdTempMaterial.getSpecification()), " (specification is null or specification = '')")
        );
        if (!mtrlSpecs.isEmpty()) {
            BeanUtils.copyProperties(newSpec, mtrlSpecs.get(0));

            if (!bdTempMaterialSpecificationService.updateById(newSpec)) {
                throw new BizException("保存失败，异常码3");
            }
        } else {
            newSpec.setPid(bdTempMaterial.getId());

            newSpec.setCreateTime(sdf.format(new Date()));
            newSpec.setCreatorId(RequestUtils.getUserId());
            newSpec.setCreator(RequestUtils.getNickname());

            if (!bdTempMaterialSpecificationService.save(newSpec)) {
                throw new BizException("保存失败，异常码4");
            }
        }

        if (StringUtils.isEmpty(bdTempMaterial.getSjNumber()) && StringUtils.isNotEmpty(bdTempMaterial.getOrigiDetailId())) {
            // 判断源单是否真实存在
            BdMaterialDetail orgiMtrlDetail = bdMaterialDetailMapper.selectById(bdTempMaterial.getOrigiDetailId());
            if (orgiMtrlDetail != null) {
                bdTempMaterial.setSjNumber(orgiMtrlDetail.getNumber());
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getId()) && StringUtils.isNotEmpty(bdTempMaterial.getSjNumber())) {
            // 商家编码不为空
            List<BdTempMaterialDetail> details = bdTempMaterialDetailService.list(
                    new LambdaQueryWrapper<BdTempMaterialDetail>()
                            .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getId())
                            .eq(BdTempMaterialDetail::getNumber, bdTempMaterial.getSjNumber())
                            .ne(StringUtils.isNotEmpty(bdTempMaterial.getMtrlDetailId()), BdTempMaterialDetail::getId, bdTempMaterial.getMtrlDetailId())
            );
            if (!details.isEmpty()) {
                throw new BizException("货品编码：" + bdTempMaterial.getNumber() + " 内已存在相同商家编码：" + bdTempMaterial.getSjNumber() + " 的成品，请修改商家编码");
            }
        }
        if (StringUtils.isEmpty(bdTempMaterial.getSjNumber())) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "SJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<String> nos = bdMaterialDetailMapper.getNos(no);
            if (!nos.isEmpty()) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }

            bdTempMaterial.setSjNumber(no);
        }

        // 详情
        // 临时表、正式表都不能重复
        BdTempMaterialDetail newDetail = new BdTempMaterialDetail();
        List<BdTempMaterialDetail> mtrlDetails = bdTempMaterialDetailService.list(
                new LambdaQueryWrapper<BdTempMaterialDetail>()
                        .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getId())
                        .eq(BdTempMaterialDetail::getColorId, newColor.getId())
                        .eq(BdTempMaterialDetail::getSpecificationId, newSpec.getId())
        );
        if (!mtrlDetails.isEmpty()) {
            if (StringUtils.isNotEmpty(bdTempMaterial.getMtrlDetailId())) {
                // 校验是否存在，不存在则覆盖其他数据
                List<BdTempMaterialDetail> chkList = bdTempMaterialDetailService.list(
                        new LambdaQueryWrapper<BdTempMaterialDetail>()
                                .eq(BdTempMaterialDetail::getId, bdTempMaterial.getMtrlDetailId())
                );
                if (!chkList.isEmpty()) {
                    for (BdTempMaterialDetail mtrlDetail : mtrlDetails) {
                        if (!StringUtils.equals(mtrlDetail.getId(), bdTempMaterial.getMtrlDetailId())) {
                            throw new BizException("已存在相同颜色规格的数据，无法重复");
                        }
                    }
                }
            }

            BeanUtils.copyProperties(newDetail, mtrlDetails.get(0));
            newDetail.setNumber(bdTempMaterial.getSjNumber());
        } else if (StringUtils.isNotEmpty(bdTempMaterial.getMtrlDetailId())) {
            mtrlDetails = bdTempMaterialDetailService.list(
                    new LambdaQueryWrapper<BdTempMaterialDetail>()
                            .eq(BdTempMaterialDetail::getId, bdTempMaterial.getMtrlDetailId())
            );
            if (!mtrlDetails.isEmpty()) {
                BeanUtils.copyProperties(newDetail, mtrlDetails.get(0));
                newDetail.setPid(bdTempMaterial.getId());
                newDetail.setNumber(bdTempMaterial.getSjNumber());
                newDetail.setColorId(newColor.getId());
                newDetail.setSpecificationId(newSpec.getId());
            }
        }
        if (StringUtils.isNotEmpty(bdTempMaterial.getOriginalId())) {
            List<BdMaterialDetail> mtrlDtls = bdMaterialDetailMapper.selectList(
                    new LambdaQueryWrapper<BdMaterialDetail>()
                            .eq(BdMaterialDetail::getPid, bdTempMaterial.getOriginalId())
                            .eq(BdMaterialDetail::getColorId, newColor.getId())
                            .eq(BdMaterialDetail::getSpecificationId, newSpec.getId())
            );
            if (!mtrlDtls.isEmpty()) {
                if (StringUtils.isNotEmpty(bdTempMaterial.getOrigiDetailId())) {
                    // 校验是否存在，不存在则覆盖其他数据
                    List<BdMaterialDetail> chkList = bdMaterialDetailMapper.selectList(
                            new LambdaQueryWrapper<BdMaterialDetail>()
                                    .eq(BdMaterialDetail::getId, bdTempMaterial.getOrigiDetailId())
                    );
                    if (!chkList.isEmpty()) {
                        for (BdMaterialDetail mtrlDetail : mtrlDtls) {
                            if (!StringUtils.equals(mtrlDetail.getId(), bdTempMaterial.getOrigiDetailId())) {
                                throw new BizException("已存在相同颜色规格的数据，无法重复");
                            }
                        }
                    }
                }

                newDetail.setOriginalId(mtrlDtls.get(0).getId());
                newDetail.setPid(bdTempMaterial.getId());
                newDetail.setNumber(bdTempMaterial.getSjNumber());
                newDetail.setColorId(newColor.getId());
                newDetail.setSpecificationId(newSpec.getId());
                newDetail.setId(null);
            } else {
                mtrlDtls = bdMaterialDetailMapper.selectList(
                        new LambdaQueryWrapper<BdMaterialDetail>()
                                .eq(BdMaterialDetail::getId, bdTempMaterial.getMtrlDetailId())
                );
                if (!mtrlDtls.isEmpty()) {
                    BeanUtils.copyProperties(newDetail, mtrlDtls.get(0));
                    newDetail.setPid(bdTempMaterial.getId());
                    newDetail.setNumber(bdTempMaterial.getSjNumber());
                    newDetail.setColorId(newColor.getId());
                    newDetail.setSpecificationId(newSpec.getId());
                }
            }
        }
        if (StringUtils.isEmpty(newDetail.getPid())) {
            newDetail.setPid(bdTempMaterial.getId());
            newDetail.setNumber(bdTempMaterial.getSjNumber());
            newDetail.setColorId(newColor.getId());
            newDetail.setSpecificationId(newSpec.getId());

            newDetail.setCreateTime(sdf.format(new Date()));
            newDetail.setCreatorId(RequestUtils.getUserId());
            newDetail.setCreator(RequestUtils.getNickname());
        }
        if (StringUtils.isEmpty(newDetail.getId())) {
            if (!bdTempMaterialDetailService.save(newDetail)) {
                throw new BizException("保存失败，异常码5");
            }
        } else {
            if (!bdTempMaterialDetailService.updateById(newDetail)) {
                throw new BizException("保存失败，异常码5-1");
            }
        }

        // 图片附件
        bdTempMaterialAttachService.remove(
                new LambdaQueryWrapper<BdTempMaterialAttach>()
                        .eq(BdTempMaterialAttach::getPid, newDetail.getId())
        );
        if (!attaches.isEmpty()) {
            for (BdTempMaterialAttach attach : attaches) {
                attach.setPid(newDetail.getId());
            }

            if (!bdTempMaterialAttachService.saveBatch(attaches)) {
                throw new BizException("保存失败，异常码7");
            }
        }

        // 辅料
        bdTempMaterialAuxService.remove(
                new LambdaQueryWrapper<BdTempMaterialAux>()
                        .eq(BdTempMaterialAux::getPid, bdTempMaterial.getId())
        );
        List<BdTempMaterialAux> auxes = bdTempMaterial.getAuxes();
        if (auxes != null && !auxes.isEmpty()) {
            for (BdTempMaterialAux aux : auxes) {
                aux.setPid(bdTempMaterial.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(aux.getAuxMaterial().getId());
                srhMtrlDtl.setColor(aux.getColor());
                srhMtrlDtl.setSpecification(aux.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (!details.isEmpty()) {
                    aux.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }

            if (!bdTempMaterialAuxService.saveBatch(auxes)) {
                throw new BizException("保存失败，异常码8");
            }
        }

        // 原材料
        bdTempMaterialRawService.remove(
                new LambdaQueryWrapper<BdTempMaterialRaw>()
                        .eq(BdTempMaterialRaw::getPid, newDetail.getId())
        );
        List<BdTempMaterialRaw> raws = bdTempMaterial.getRaws();
        if (raws != null && !raws.isEmpty()) {
            for (BdTempMaterialRaw raw : raws) {
                raw.setPid(newDetail.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(raw.getRawMaterial().getId());
                srhMtrlDtl.setColor(raw.getColor());
                srhMtrlDtl.setSpecification(raw.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (!details.isEmpty()) {
                    raw.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("原材料信息异常 编码 " + raw.getRawMaterial().getNumber() + " 名称 " + raw.getRawMaterial().getName() + " 请选择正确的颜色/色号 和 规格");
                }
            }

            if (!bdTempMaterialRawService.saveBatch(raws)) {
                throw new BizException("保存失败，异常码9");
            }
        }

        // 工序
        List<BdTempMaterialRoute> oldRoutes = bdTempMaterialRouteService.list(
                new LambdaQueryWrapper<BdTempMaterialRoute>()
                        .eq(BdTempMaterialRoute::getPid, bdTempMaterial.getId())
        );
        if (!oldRoutes.isEmpty()) {
            List<String> oldRouteIds = oldRoutes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
            bdTempMaterialRouteService.removeByIds(oldRouteIds);

            bdTempMaterialRouteSpecialService.remove(
                    new LambdaQueryWrapper<BdTempMaterialRouteSpecial>()
                            .in(BdTempMaterialRouteSpecial::getPid, oldRouteIds)
            );
        }
        List<BdTempMaterialRoute> routes = bdTempMaterial.getRoutes();
        if (routes != null && !routes.isEmpty()) {
            for (BdTempMaterialRoute route : routes) {
                route.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialRouteService.saveBatch(routes)) {
                throw new BizException("保存失败，异常码10");
            }

            List<BdTempMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
            for (BdTempMaterialRoute route : routes) {
                List<BdTempMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
                if (routeSpecials != null && !routeSpecials.isEmpty()) {
                    for (BdTempMaterialRouteSpecial routeSpecial : routeSpecials) {
                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
                            routeSpecial.setPid(route.getId());
                            newRouteSpecials.add(routeSpecial);
                        }
                    }
                }
            }
            if (!newRouteSpecials.isEmpty()) {
                if (!bdTempMaterialRouteSpecialService.saveBatch(newRouteSpecials)) {
                    throw new BizException("保存失败，异常码11");
                }
            }
        }

        // 供应商
        bdTempMaterialSupplierService.remove(
                new LambdaQueryWrapper<BdTempMaterialSupplier>()
                        .eq(BdTempMaterialSupplier::getPid, bdTempMaterial.getId())
        );
        List<BdTempMaterialSupplier> suppliers = bdTempMaterial.getSuppliers();
        if (suppliers != null && !suppliers.isEmpty()) {
            for (BdTempMaterialSupplier supplier : suppliers) {
                supplier.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialSupplierService.saveBatch(suppliers)) {
                throw new BizException("保存失败，异常码12");
            }
        }

        return bdTempMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempMaterial myUpdate(BdTempMaterial bdTempMaterial) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTempMaterial.getOriginalId())) {
            if (StringUtils.isNotEmpty(bdTempMaterial.getNumber())) {
                List<BdMaterial> list = bdMaterialService.list(
                        new LambdaQueryWrapper<BdMaterial>()
                                .eq(BdMaterial::getNumber, bdTempMaterial.getNumber())
                                .ne(BdMaterial::getId, bdTempMaterial.getOriginalId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的物料档案");
                }
            } else {
                BdMaterial material = bdMaterialService.getById(bdTempMaterial.getOriginalId());
                if (material != null) {
                    bdTempMaterial.setNumber(material.getNumber());
                }
            }
        }
        if (StringUtils.isEmpty(bdTempMaterial.getNumber())) {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "";
            if (bdTempMaterial.getMaterialGroup() == 0) {
                no = "CP" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            } else if (bdTempMaterial.getMaterialGroup() == 1) {
                no = "FL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            } else if (bdTempMaterial.getMaterialGroup() == 2) {
                no = "YCL" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            }
            List<String> nos = bdMaterialMapper.getNos(no);
            if (nos != null && nos.size() > 0) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdTempMaterial.setNumber(no);
        }

        // 成品
        if (ObjectUtils.isNotEmpty(bdTempMaterial.getMaterialGroup()) && bdTempMaterial.getMaterialGroup() == 0) {
//                if (bdTempMaterial.getAuxes() == null || bdTempMaterial.getAuxes().size() == 0) {
//                    throw new BizException("未检测到辅料明细");
//                }
            if (bdTempMaterial.getRaws() == null || bdTempMaterial.getRaws().size() == 0) {
                throw new BizException("未检测到原材料明细");
            }
            if (bdTempMaterial.getRoutes() == null || bdTempMaterial.getRoutes().size() == 0) {
                throw new BizException("未检测到工序明细");
            }
        }

        // 图片附件
        List<BdTempMaterialAttach> attaches = bdTempMaterial.getAttaches();
        if (attaches != null && attaches.size() > 0) {
            bdTempMaterial.setMainPicId(attaches.get(0).getAttachId());
        }

        bdTempMaterial.setUpdateTime(sdf.format(new Date()));
        bdTempMaterial.setUpdaterId(RequestUtils.getUserId());
        bdTempMaterial.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdTempMaterial)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        // 删除-图片附件明细
        List<BdTempMaterialAttach> oldAttaches = bdTempMaterialAttachService.list(
                new LambdaQueryWrapper<BdTempMaterialAttach>()
                        .eq(BdTempMaterialAttach::getPid, bdTempMaterial.getId())
        );
        if (oldAttaches != null && oldAttaches.size() > 0) {
            List<String> oldAttachIds = oldAttaches.stream().map(BdTempMaterialAttach::getId).collect(Collectors.toList());
            if (oldAttachIds != null && oldAttachIds.size() > 0) {
                if (!bdTempMaterialAttachService.removeByIds(oldAttachIds)) {
                    throw new BizException("物料图片更新失败");
                }
            }
        }
        // 删除-辅料明细
        List<BdTempMaterialAux> oldAuxes = bdTempMaterialAuxService.list(
                new LambdaQueryWrapper<BdTempMaterialAux>()
                        .eq(BdTempMaterialAux::getPid, bdTempMaterial.getId())
        );
        if (oldAuxes != null && oldAuxes.size() > 0) {
            List<String> oldAuxIds = oldAuxes.stream().map(BdTempMaterialAux::getId).collect(Collectors.toList());
            if (oldAuxIds != null && oldAuxIds.size() > 0) {
                if (!bdTempMaterialAuxService.removeByIds(oldAuxIds)) {
                    throw new BizException("辅料明细更新失败，异常码1");
                }
            }
        }
        // 删除-原材料明细
        List<BdTempMaterialRaw> oldRaws = bdTempMaterialRawService.list(
                new LambdaQueryWrapper<BdTempMaterialRaw>()
                        .eq(BdTempMaterialRaw::getPid, bdTempMaterial.getId())
        );
        if (oldRaws != null && oldRaws.size() > 0) {
            List<String> oldRawIds = oldRaws.stream().map(BdTempMaterialRaw::getId).collect(Collectors.toList());
            if (oldRawIds != null && oldRawIds.size() > 0) {
                if (!bdTempMaterialRawService.removeByIds(oldRawIds)) {
                    throw new BizException("原材料明细更新失败，异常码1");
                }
            }
        }
        // 删除-工序明细
        List<BdTempMaterialRoute> oldRoutes = bdTempMaterialRouteService.list(
                new LambdaQueryWrapper<BdTempMaterialRoute>()
                        .eq(BdTempMaterialRoute::getPid, bdTempMaterial.getId())
        );
        if (oldRoutes != null && oldRoutes.size() > 0) {
            List<String> oldRouteIds = oldRoutes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
            if (oldRouteIds != null && oldRouteIds.size() > 0) {
                if (!bdTempMaterialRouteService.removeByIds(oldRouteIds)) {
                    throw new BizException("工序明细更新失败，异常码1");
                }
            }

            List<BdTempMaterialRouteSpecial> oldRouteSpecials = bdTempMaterialRouteSpecialService.list(
                    new LambdaQueryWrapper<BdTempMaterialRouteSpecial>()
                            .in(BdTempMaterialRouteSpecial::getPid, oldRouteIds)
            );
            if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
                List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdTempMaterialRouteSpecial::getId).collect(Collectors.toList());
                if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
                    if (!bdTempMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
                        throw new BizException("工序明细更新失败，异常码2");
                    }
                }
            }
        }
        // 删除-工艺要求明细
        List<BdTempMaterialProcess> oldProcesses = bdTempMaterialProcessService.list(
                new LambdaQueryWrapper<BdTempMaterialProcess>()
                        .eq(BdTempMaterialProcess::getPid, bdTempMaterial.getId())
        );
        if (oldProcesses != null && oldProcesses.size() > 0) {
            List<String> oldProcessIds = oldProcesses.stream().map(BdTempMaterialProcess::getId).collect(Collectors.toList());
            if (oldProcessIds != null && oldProcessIds.size() > 0) {
                if (!bdTempMaterialProcessService.removeByIds(oldProcessIds)) {
                    throw new BizException("工艺要求明细更新失败，异常码1");
                }
            }
        }
        // 删除-供应商明细
        List<BdTempMaterialSupplier> oldSuppliers = bdTempMaterialSupplierService.list(
                new LambdaQueryWrapper<BdTempMaterialSupplier>()
                        .eq(BdTempMaterialSupplier::getPid, bdTempMaterial.getId())
        );
        if (oldSuppliers != null && oldSuppliers.size() > 0) {
            List<String> oldSupplierIds = oldSuppliers.stream().map(BdTempMaterialSupplier::getId).collect(Collectors.toList());
            if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
                if (!bdTempMaterialSupplierService.removeByIds(oldSupplierIds)) {
                    throw new BizException("供应商明细更新失败，异常码1");
                }
            }
        }
        // 删除-颜色/色号
        List<BdTempMaterialColor> oldColors = bdTempMaterialColorService.list(
                new LambdaQueryWrapper<BdTempMaterialColor>()
                        .eq(BdTempMaterialColor::getPid, bdTempMaterial.getId())
        );
        if (oldColors != null && oldColors.size() > 0) {
            List<String> oldColorIds = oldColors.stream().map(BdTempMaterialColor::getId).collect(Collectors.toList());
            if (oldColorIds != null && oldColorIds.size() > 0) {
                if (!bdTempMaterialColorService.removeByIds(oldColorIds)) {
                    throw new BizException("颜色/色号明细更新失败，异常码1");
                }
            }
        }
        // 删除-规格
        List<BdTempMaterialSpecification> oldSpecifications = bdTempMaterialSpecificationService.list(
                new LambdaQueryWrapper<BdTempMaterialSpecification>()
                        .eq(BdTempMaterialSpecification::getPid, bdTempMaterial.getId())
        );
        if (oldSpecifications != null && oldSpecifications.size() > 0) {
            List<String> oldSpecificationIds = oldSpecifications.stream().map(BdTempMaterialSpecification::getId).collect(Collectors.toList());
            if (oldSpecificationIds != null && oldSpecificationIds.size() > 0) {
                if (!bdTempMaterialSpecificationService.removeByIds(oldSpecificationIds)) {
                    throw new BizException("规格明细更新失败，异常码1");
                }
            }
        }

        // 保存新数据
        // 图片附件
        if (attaches != null && attaches.size() > 0) {
            for (BdTempMaterialAttach attach : attaches) {
                attach.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialAttachService.saveOrUpdateBatch(attaches)) {
                throw new BizException("更新失败，异常码1");
            }
        }
        // 辅料
        List<BdTempMaterialAux> auxes = bdTempMaterial.getAuxes();
        if (auxes != null && auxes.size() > 0) {
            for (BdTempMaterialAux aux : auxes) {
                aux.setPid(bdTempMaterial.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(aux.getAuxMaterial().getId());
                srhMtrlDtl.setColor(aux.getColor());
                srhMtrlDtl.setSpecification(aux.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    aux.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("辅料信息异常 编码：" + aux.getAuxMaterial().getNumber() + " 名称：" + aux.getAuxMaterial().getName() + "  请选择正确的颜色/色号 和 规格");
                }
            }

            if (!bdTempMaterialAuxService.saveOrUpdateBatch(auxes)) {
                throw new BizException("更新失败，异常码2");
            }
        }
        // 原材料
        List<BdTempMaterialRaw> raws = bdTempMaterial.getRaws();
        if (raws != null && raws.size() > 0) {
            for (BdTempMaterialRaw raw : raws) {
                raw.setPid(bdTempMaterial.getId());

                BdMaterialDetail srhMtrlDtl = new BdMaterialDetail();
                srhMtrlDtl.setPid(raw.getRawMaterial().getId());
                srhMtrlDtl.setColor(raw.getColor());
                srhMtrlDtl.setSpecification(raw.getSpecification());
                List<BdMaterialDetail> details = bdMaterialDetailMapper.getList2(srhMtrlDtl);
                if (details != null && details.size() > 0) {
                    raw.setMaterialDetailId(details.get(0).getId());
                } else {
                    throw new BizException("bdTempMaterialDetail " + raw.getRawMaterial().getNumber() + " 名称 " + raw.getRawMaterial().getName());
                }
            }

            if (!bdTempMaterialRawService.saveOrUpdateBatch(raws)) {
                throw new BizException("更新失败，异常码3");
            }
        }
        // 工序
        List<BdTempMaterialRoute> routes = bdTempMaterial.getRoutes();
        if (routes != null && routes.size() > 0) {
            for (BdTempMaterialRoute route : routes) {
                route.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialRouteService.saveOrUpdateBatch(routes)) {
                throw new BizException("更新失败，异常码4");
            }

            List<BdTempMaterialRouteSpecial> newRouteSpecials = new ArrayList<>();
            for (BdTempMaterialRoute route : routes) {
                List<BdTempMaterialRouteSpecial> routeSpecials = route.getRouteSpecials();
                if (routeSpecials != null && routeSpecials.size() > 0) {
                    for (BdTempMaterialRouteSpecial routeSpecial : routeSpecials) {
                        if (StringUtils.isNotEmpty(routeSpecial.getStaffId())) {
                            routeSpecial.setPid(route.getId());
                            newRouteSpecials.add(routeSpecial);
                        }
                    }
                }
            }
            if (newRouteSpecials.size() > 0) {
                if (!bdTempMaterialRouteSpecialService.saveOrUpdateBatch(newRouteSpecials)) {
                    throw new BizException("更新失败，异常码5");
                }
            }
        }
        // 工艺要求
        List<BdTempMaterialProcess> processes = bdTempMaterial.getProcesses();
        if (processes != null && processes.size() > 0) {
            for (BdTempMaterialProcess process : processes) {
                process.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialProcessService.saveOrUpdateBatch(processes)) {
                throw new BizException("更新失败，异常码6");
            }
        }
        // 供应商
        List<BdTempMaterialSupplier> suppliers = bdTempMaterial.getSuppliers();
        if (suppliers != null && suppliers.size() > 0) {
            for (BdTempMaterialSupplier supplier : suppliers) {
                supplier.setPid(bdTempMaterial.getId());
            }

            if (!bdTempMaterialSupplierService.saveOrUpdateBatch(suppliers)) {
                throw new BizException("更新失败，异常码7");
            }
        }
        // 颜色/色号
        List<BdTempMaterialColor> addClrs = new ArrayList<>();
        List<BdTempMaterialColor> colors = bdTempMaterial.getColors();
        if (colors == null || colors.size() == 0) {
            BdTempMaterialColor newColor = new BdTempMaterialColor();
            colors.add(newColor);
        }
        for (BdTempMaterialColor color : colors) {
            color.setPid(bdTempMaterial.getId());

            if (StringUtils.isEmpty(color.getCreateTime())) {
                color.setCreateTime(sdf.format(new Date()));
                color.setCreatorId(RequestUtils.getUserId());
                color.setCreator(RequestUtils.getNickname());
            }

            addClrs.add(color);
        }

        if (addClrs.size() > 0) {
            if (!bdTempMaterialColorService.saveBatch(addClrs)) {
                throw new BizException("更新失败，异常码8");
            }
        }
        // 规格
        List<BdTempMaterialSpecification> addSpecs = new ArrayList<>();
        List<BdTempMaterialSpecification> specifications = bdTempMaterial.getSpecifications();
        if (specifications == null || specifications.size() == 0) {
            BdTempMaterialSpecification newSpec = new BdTempMaterialSpecification();
            specifications.add(newSpec);
        }
        for (BdTempMaterialSpecification specification : specifications) {
            specification.setPid(bdTempMaterial.getId());

            if (StringUtils.isEmpty(specification.getCreateTime())) {
                specification.setCreateTime(sdf.format(new Date()));
                specification.setCreatorId(RequestUtils.getUserId());
                specification.setCreator(RequestUtils.getNickname());
            }

            addSpecs.add(specification);
        }

        if (addSpecs.size() > 0) {
            if (!bdTempMaterialSpecificationService.saveBatch(addSpecs)) {
                throw new BizException("更新失败，异常码9");
            }
        }
        // 详细
        List<BdTempMaterialDetail> oldDetails = bdTempMaterialDetailMapper.selectList(
                new LambdaQueryWrapper<BdTempMaterialDetail>()
                        .eq(BdTempMaterialDetail::getPid, bdTempMaterial.getId())
        );
        // 当前数据重复项
        List<BdTempMaterialDetail> nowDetails = new ArrayList<>();
        // 新增数据
        List<BdTempMaterialDetail> addDetails = new ArrayList<>();
        for (BdTempMaterialColor color : colors) {
            for (BdTempMaterialSpecification specification : specifications) {
                Boolean existBL = false;
                if (oldDetails != null && oldDetails.size() > 0) {
                    for (BdTempMaterialDetail oldDetail : oldDetails) {
                        if (StringUtils.equals(oldDetail.getColorId(), color.getId()) && StringUtils.equals(oldDetail.getSpecificationId(), specification.getId())) {
                            existBL = true;

                            nowDetails.add(oldDetail);

                            break;
                        }
                    }
                }

                if (existBL == false) {
                    BdTempMaterialDetail mtrlDtl = new BdTempMaterialDetail();
                    mtrlDtl.setPid(bdTempMaterial.getId());
                    mtrlDtl.setColorId(color.getId());
                    mtrlDtl.setSpecificationId(specification.getId());

                    mtrlDtl.setCreateTime(sdf.format(new Date()));
                    mtrlDtl.setCreatorId(RequestUtils.getUserId());
                    mtrlDtl.setCreator(RequestUtils.getNickname());

                    addDetails.add(mtrlDtl);
                }
            }
        }
        // 删除的项
        List<String> existIds = nowDetails.stream().map(BdTempMaterialDetail::getId).collect(Collectors.toList());
        List<String> delIds = oldDetails.stream().filter(r -> !existIds.contains(r.getId())).map(BdTempMaterialDetail::getId).collect(Collectors.toList());
        if (delIds != null && delIds.size() > 0) {
            if (!bdTempMaterialDetailService.removeByIds(delIds)) {
                throw new BizException("详细更新失败，异常码1");
            }
        }
        if (addDetails.size() > 0) {
            if (!bdTempMaterialDetailService.saveBatch(addDetails)) {
                throw new BizException("保存失败，异常码10");
            }
        }

        return bdTempMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(String id) {
        BdTempMaterialDetail mtrlDetail = bdTempMaterialDetailMapper.infoById(id);
        if (mtrlDetail == null) {
            return true;
        }

        String pid = mtrlDetail.getPid();
        BdTempMaterial bdTempMaterial = this.getById(pid);
        if (bdTempMaterial == null) {
            return true;
        }
        if (bdTempMaterial.getStatus() == Constants.INT_STATUS_APPROVING || bdTempMaterial.getStatus() == Constants.INT_STATUS_AUDITED) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        if (!bdTempMaterialDetailService.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        // 删除-原材料明细
        List<BdTempMaterialRaw> oldRaws = bdTempMaterialRawService.list(
                new LambdaQueryWrapper<BdTempMaterialRaw>()
                        .eq(BdTempMaterialRaw::getPid, mtrlDetail.getId())
        );
        if (oldRaws != null && oldRaws.size() > 0) {
            List<String> oldRawIds = oldRaws.stream().map(BdTempMaterialRaw::getId).collect(Collectors.toList());
            if (oldRawIds != null && oldRawIds.size() > 0) {
                if (!bdTempMaterialRawService.removeByIds(oldRawIds)) {
                    throw new BizException("删除失败，异常码2");
                }
            }
        }

        // 删除-图片附件明细
        List<BdTempMaterialAttach> oldAttaches = bdTempMaterialAttachService.list(
                new LambdaQueryWrapper<BdTempMaterialAttach>()
                        .eq(BdTempMaterialAttach::getPid, mtrlDetail.getId())
        );
        if (oldAttaches != null && oldAttaches.size() > 0) {
            List<String> oldAttachIds = oldAttaches.stream().map(BdTempMaterialAttach::getId).collect(Collectors.toList());
            if (oldAttachIds != null && oldAttachIds.size() > 0) {
                if (!bdTempMaterialAttachService.removeByIds(oldAttachIds)) {
                    throw new BizException("删除失败，异常码3");
                }
            }
        }

        List<BdTempMaterialDetail> details = bdTempMaterialDetailService.list(
                new LambdaQueryWrapper<BdTempMaterialDetail>()
                        .eq(BdTempMaterialDetail::getPid, pid)
                        .ne(BdTempMaterialDetail::getId, id)
        );
        if (details.isEmpty()) {
            if (!this.removeById(pid)) {
                throw new BizException("删除失败，异常码4");
            }

            // 删除-辅料明细
            List<BdTempMaterialAux> oldAuxes = bdTempMaterialAuxService.list(
                    new LambdaQueryWrapper<BdTempMaterialAux>()
                            .eq(BdTempMaterialAux::getPid, bdTempMaterial.getId())
            );
            if (oldAuxes != null && oldAuxes.size() > 0) {
                List<String> oldAuxIds = oldAuxes.stream().map(BdTempMaterialAux::getId).collect(Collectors.toList());
                if (oldAuxIds != null && oldAuxIds.size() > 0) {
                    if (!bdTempMaterialAuxService.removeByIds(oldAuxIds)) {
                        throw new BizException("删除失败，异常码3");
                    }
                }
            }

            // 删除-工序明细
            List<BdTempMaterialRoute> oldRoutes = bdTempMaterialRouteService.list(
                    new LambdaQueryWrapper<BdTempMaterialRoute>()
                            .eq(BdTempMaterialRoute::getPid, bdTempMaterial.getId())
            );
            if (oldRoutes != null && oldRoutes.size() > 0) {
                List<String> oldRouteIds = oldRoutes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
                if (oldRouteIds != null && oldRouteIds.size() > 0) {
                    if (!bdTempMaterialRouteService.removeByIds(oldRouteIds)) {
                        throw new BizException("删除失败，异常码5");
                    }
                }

                List<BdTempMaterialRouteSpecial> oldRouteSpecials = bdTempMaterialRouteSpecialService.list(
                        new LambdaQueryWrapper<BdTempMaterialRouteSpecial>()
                                .in(BdTempMaterialRouteSpecial::getPid, oldRouteIds)
                );
                if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
                    List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdTempMaterialRouteSpecial::getId).collect(Collectors.toList());
                    if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
                        if (!bdTempMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
                            throw new BizException("删除失败，异常码6");
                        }
                    }
                }
            }

            // 删除-供应商明细
            List<BdTempMaterialSupplier> oldSuppliers = bdTempMaterialSupplierService.list(
                    new LambdaQueryWrapper<BdTempMaterialSupplier>()
                            .eq(BdTempMaterialSupplier::getPid, bdTempMaterial.getId())
            );
            if (oldSuppliers != null && oldSuppliers.size() > 0) {
                List<String> oldSupplierIds = oldSuppliers.stream().map(BdTempMaterialSupplier::getId).collect(Collectors.toList());
                if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
                    if (!bdTempMaterialSupplierService.removeByIds(oldSupplierIds)) {
                        throw new BizException("删除失败，异常码8");
                    }
                }
            }

            if (StringUtils.isNotEmpty(bdTempMaterial.getOriginalId())) {
                BdMaterial material = bdMaterialService.getById(bdTempMaterial.getOriginalId());
                if (material != null && material.getStatus() != 2) {

                    bdMaterialService.removeById(bdTempMaterial.getOriginalId());

                    // 删除-图片附件明细
                    bdMaterialAttachService.remove(
                            new LambdaQueryWrapper<BdMaterialAttach>()
                                    .eq(BdMaterialAttach::getPid, bdTempMaterial.getOriginalId())
                    );
                    // 删除-辅料明细
                    bdMaterialAuxService.remove(
                            new LambdaQueryWrapper<BdMaterialAux>()
                                    .eq(BdMaterialAux::getPid, bdTempMaterial.getOriginalId())
                    );
                    // 删除-原材料明细
                    bdMaterialRawService.remove(
                            new LambdaQueryWrapper<BdMaterialRaw>()
                                    .eq(BdMaterialRaw::getPid, bdTempMaterial.getOriginalId())
                    );
                    // 删除-工序明细
                    List<BdMaterialRoute> materialRoutes = bdMaterialRouteService.list(
                            new LambdaQueryWrapper<BdMaterialRoute>()
                                    .eq(BdMaterialRoute::getPid, bdTempMaterial.getOriginalId())
                    );
                    if (materialRoutes != null && materialRoutes.size() > 0) {
                        List<String> materialRouteIds = materialRoutes.stream().map(BdMaterialRoute::getId).collect(Collectors.toList());

                        bdMaterialRouteService.removeByIds(materialRouteIds);

                        bdMaterialRouteSpecialService.remove(
                                new LambdaQueryWrapper<BdMaterialRouteSpecial>()
                                        .in(BdMaterialRouteSpecial::getPid, materialRouteIds)
                        );
                    }
                    // 删除-工艺要求明细
                    bdMaterialProcessService.remove(
                            new LambdaQueryWrapper<BdMaterialProcess>()
                                    .eq(BdMaterialProcess::getPid, bdTempMaterial.getOriginalId())
                    );
                    // 删除-供应商明细
                    bdMaterialSupplierService.remove(
                            new LambdaQueryWrapper<BdMaterialSupplier>()
                                    .eq(BdMaterialSupplier::getPid, bdTempMaterial.getOriginalId())
                    );
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempMaterial> list = this.list(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT).map(BdTempMaterial::getId).collect(Collectors.toList());
            if (delIds != null && delIds.size() > 0) {
                if (!this.removeByIds(delIds)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            // 删除-图片附件明细
            List<BdTempMaterialAttach> oldAttaches = bdTempMaterialAttachService.list(
                    new LambdaQueryWrapper<BdTempMaterialAttach>()
                            .in(BdTempMaterialAttach::getPid, delIds)
            );
            if (oldAttaches != null && oldAttaches.size() > 0) {
                List<String> oldAttachIds = oldAttaches.stream().map(BdTempMaterialAttach::getId).collect(Collectors.toList());
                if (oldAttachIds != null && oldAttachIds.size() > 0) {
                    if (!bdTempMaterialAttachService.removeByIds(oldAttachIds)) {
                        throw new BizException("删除失败，异常码2");
                    }
                }
            }
            // 删除-辅料明细
            List<BdTempMaterialAux> oldAuxes = bdTempMaterialAuxService.list(
                    new LambdaQueryWrapper<BdTempMaterialAux>()
                            .in(BdTempMaterialAux::getPid, delIds)
            );
            if (oldAuxes != null && oldAuxes.size() > 0) {
                List<String> oldAuxIds = oldAuxes.stream().map(BdTempMaterialAux::getId).collect(Collectors.toList());
                if (oldAuxIds != null && oldAuxIds.size() > 0) {
                    if (!bdTempMaterialAuxService.removeByIds(oldAuxIds)) {
                        throw new BizException("删除失败，异常码3");
                    }
                }
            }
            // 删除-原材料明细
            List<BdTempMaterialRaw> oldRaws = bdTempMaterialRawService.list(
                    new LambdaQueryWrapper<BdTempMaterialRaw>()
                            .in(BdTempMaterialRaw::getPid, delIds)
            );
            if (oldRaws != null && oldRaws.size() > 0) {
                List<String> oldRawIds = oldRaws.stream().map(BdTempMaterialRaw::getId).collect(Collectors.toList());
                if (oldRawIds != null && oldRawIds.size() > 0) {
                    if (!bdTempMaterialRawService.removeByIds(oldRawIds)) {
                        throw new BizException("删除失败，异常码4");
                    }
                }
            }
            // 删除-工序明细
            List<BdTempMaterialRoute> oldRoutes = bdTempMaterialRouteService.list(
                    new LambdaQueryWrapper<BdTempMaterialRoute>()
                            .in(BdTempMaterialRoute::getPid, delIds)
            );
            if (oldRoutes != null && oldRoutes.size() > 0) {
                List<String> oldRouteIds = oldRoutes.stream().map(BdTempMaterialRoute::getId).collect(Collectors.toList());
                if (oldRouteIds != null && oldRouteIds.size() > 0) {
                    if (!bdTempMaterialRouteService.removeByIds(oldRouteIds)) {
                        throw new BizException("删除失败，异常码5");
                    }
                }

                List<BdTempMaterialRouteSpecial> oldRouteSpecials = bdTempMaterialRouteSpecialService.list(
                        new LambdaQueryWrapper<BdTempMaterialRouteSpecial>()
                                .in(BdTempMaterialRouteSpecial::getPid, oldRouteIds)
                );
                if (oldRouteSpecials != null && oldRouteSpecials.size() > 0) {
                    List<String> oldRouteSpecialIds = oldRouteSpecials.stream().map(BdTempMaterialRouteSpecial::getId).collect(Collectors.toList());
                    if (oldRouteSpecialIds != null && oldRouteSpecials.size() > 0) {
                        if (!bdTempMaterialRouteSpecialService.removeByIds(oldRouteSpecialIds)) {
                            throw new BizException("删除失败，异常码6");
                        }
                    }
                }
            }
            // 删除-工艺要求明细
            List<BdTempMaterialProcess> oldProcesses = bdTempMaterialProcessService.list(
                    new LambdaQueryWrapper<BdTempMaterialProcess>()
                            .in(BdTempMaterialProcess::getPid, delIds)
            );
            if (oldProcesses != null && oldProcesses.size() > 0) {
                List<String> oldProcessIds = oldProcesses.stream().map(BdTempMaterialProcess::getId).collect(Collectors.toList());
                if (oldProcessIds != null && oldProcessIds.size() > 0) {
                    if (!bdTempMaterialProcessService.removeByIds(oldProcessIds)) {
                        throw new BizException("删除失败，异常码7");
                    }
                }
            }
            // 删除-供应商明细
            List<BdTempMaterialSupplier> oldSuppliers = bdTempMaterialSupplierService.list(
                    new LambdaQueryWrapper<BdTempMaterialSupplier>()
                            .in(BdTempMaterialSupplier::getPid, delIds)
            );
            if (oldSuppliers != null && oldSuppliers.size() > 0) {
                List<String> oldSupplierIds = oldSuppliers.stream().map(BdTempMaterialSupplier::getId).collect(Collectors.toList());
                if (oldSupplierIds != null && oldSupplierIds.size() > 0) {
                    if (!bdTempMaterialSupplierService.removeByIds(oldSupplierIds)) {
                        throw new BizException("删除失败，异常码8");
                    }
                }
            }
        }
    }

    @Override
    public BdTempMaterial detail(String id) {
        BdTempMaterialDetail mtrlDetail = bdTempMaterialDetailMapper.infoById(id);
        if (mtrlDetail == null) {
            return null;
        }

        String pid = mtrlDetail.getPid();
        BdTempMaterial result = baseMapper.infoById(pid);
        if (result != null) {
            List<BdTempMaterialDetail> details = new ArrayList<>();
            details.add(mtrlDetail);
            result.setDetails(details);
            result.setMtrlDetailId(mtrlDetail.getId());

            // 图片附件
            BdTempMaterialAttach bdTempMaterialAttach = new BdTempMaterialAttach();
            bdTempMaterialAttach.setPid(result.getId());
            List<BdTempMaterialAttach> attaches = bdTempMaterialAttachMapper.getList(bdTempMaterialAttach);
            result.setAttaches(attaches);

            // 颜色/色号
            List<BdTempMaterialColor> colors = new ArrayList<>();
            BdTempMaterialColor color = bdTempMaterialColorService.getById(mtrlDetail.getColorId());
            colors.add(color);
            result.setColors(colors);
            // 规格
            List<BdTempMaterialSpecification> specifications = new ArrayList<>();
            BdTempMaterialSpecification specification = bdTempMaterialSpecificationService.getById(mtrlDetail.getSpecificationId());
            specifications.add(specification);
            result.setSpecifications(specifications);

            // 原材料
            BdTempMaterialRaw bdTempMaterialRaw = new BdTempMaterialRaw();
            bdTempMaterialRaw.setPid(mtrlDetail.getId());
            List<BdTempMaterialRaw> raws = bdTempMaterialRawMapper.getList(bdTempMaterialRaw);
            if (raws != null && raws.size() > 0) {
                for (BdTempMaterialRaw raw : raws) {
                    if (raw.getRawMaterial() != null && StringUtils.isNotEmpty(raw.getRawMaterial().getMainPicId())) {
                        SysFiles sysFile = sysFileService.getById(raw.getRawMaterial().getMainPicId());
                        raw.getRawMaterial().setMainPic(sysFile);
                    }
                }
            }
            result.setRaws(raws);

            // 辅料
            BdTempMaterialAux bdTempMaterialAux = new BdTempMaterialAux();
            bdTempMaterialAux.setPid(result.getId());
            List<BdTempMaterialAux> auxes = bdTempMaterialAuxMapper.getList(bdTempMaterialAux);
            if (auxes != null && auxes.size() > 0) {
                for (BdTempMaterialAux aux : auxes) {
                    if (aux.getAuxMaterial() != null && StringUtils.isNotEmpty(aux.getAuxMaterial().getMainPicId())) {
                        SysFiles sysFile = sysFileService.getById(aux.getAuxMaterial().getMainPicId());
                        aux.getAuxMaterial().setMainPic(sysFile);
                    }
                }
            }
            result.setAuxes(auxes);

            // 工序
            BdTempMaterialRoute bdTempMaterialRoute = new BdTempMaterialRoute();
            bdTempMaterialRoute.setPid(result.getId());
            List<BdTempMaterialRoute> routes = bdTempMaterialRouteMapper.getList(bdTempMaterialRoute);
            if (routes != null && routes.size() > 0) {
                for (BdTempMaterialRoute route : routes) {
                    if (route.getRouteSpecials() != null && route.getRouteSpecials().size() > 0) {
                        for (BdTempMaterialRouteSpecial routeSpecial : route.getRouteSpecials()) {
                            SysUser staff = sysUserService.getById(routeSpecial.getStaffId());
                            routeSpecial.setStaff(staff);
                        }
                    }
                }
            }
            result.setRoutes(routes);

            // 供应商
            BdTempMaterialSupplier bdTempMaterialSupplier = new BdTempMaterialSupplier();
            bdTempMaterialSupplier.setPid(result.getId());
            List<BdTempMaterialSupplier> suppliers = bdTempMaterialSupplierMapper.getList(bdTempMaterialSupplier);
            result.setSuppliers(suppliers);

            // 工艺要求
            BdTempMaterialProcess bdTempMaterialProcess = new BdTempMaterialProcess();
            bdTempMaterialProcess.setPid(result.getId());
            List<BdTempMaterialProcess> processes = bdTempMaterialProcessMapper.getList(bdTempMaterialProcess);
            result.setProcesses(processes);

        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdTempMaterial submit(String id) throws Exception {
        BdTempMaterial result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_CREATE && result.getStatus() != Constants.INT_STATUS_RESUBMIT) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }

        result = this.auditUptData(result);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public BdTempMaterial auditUptData(BdTempMaterial bdTempMaterial) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        bdTempMaterial.setStatus(Constants.INT_STATUS_AUDITED);
        bdTempMaterial.setAuditTime(sdf.format(new Date()));
        bdTempMaterial.setAuditorId(RequestUtils.getUserId());
        bdTempMaterial.setAuditor(RequestUtils.getNickname());
//        if (!this.updateById(bdTempMaterial)) {
//            throw new BizException("操作失败");
//        }

        // 生成正式数据
        BdTempMaterial thisTemp = this.detail(bdTempMaterial.getId());
        if (thisTemp != null) {
            BdMaterial material = new BdMaterial();
            BeanUtils.copyProperties(material, thisTemp);

            // 附件
            List<BdTempMaterialAttach> tempMaterialAttaches = thisTemp.getAttaches();
            if (tempMaterialAttaches != null && tempMaterialAttaches.size() > 0) {
                List<BdMaterialAttach> materialAttaches = new ArrayList<>();

                for (BdTempMaterialAttach tempMaterialAttach : tempMaterialAttaches) {
                    BdMaterialAttach materialAttach = new BdMaterialAttach();
                    BeanUtils.copyProperties(materialAttach, tempMaterialAttach);

                    materialAttach.setId(null);
                    materialAttach.setPid(thisTemp.getOriginalId());

                    materialAttaches.add(materialAttach);
                }
                material.setAttaches(materialAttaches);
            }

            // 辅料
            List<BdTempMaterialAux> tempMaterialAuxes = thisTemp.getAuxes();
            if (tempMaterialAuxes != null && tempMaterialAuxes.size() > 0) {
                List<BdMaterialAux> materialAuxes = new ArrayList<>();

                for (BdTempMaterialAux tempMaterialAux : tempMaterialAuxes) {
                    BdMaterialAux materialAux = new BdMaterialAux();
                    BeanUtils.copyProperties(materialAux, tempMaterialAux);

                    materialAux.setId(null);
                    materialAux.setPid(thisTemp.getOriginalId());

                    materialAuxes.add(materialAux);
                }
                material.setAuxes(materialAuxes);
            }

            // 颜色
            List<BdTempMaterialColor> tempMaterialColors = thisTemp.getColors();
            if (tempMaterialColors != null && tempMaterialColors.size() > 0) {
                List<BdMaterialColor> materialColors = new ArrayList<>();

                for (BdTempMaterialColor tempMaterialColor : tempMaterialColors) {
                    BdMaterialColor materialColor = new BdMaterialColor();
                    BeanUtils.copyProperties(materialColor, tempMaterialColor);

                    materialColor.setId(null);
                    materialColor.setPid(thisTemp.getOriginalId());

                    materialColors.add(materialColor);
                }
                material.setColors(materialColors);
            }

            // 详情
            List<BdTempMaterialDetail> tempMaterialDetails = thisTemp.getDetails();
            if (tempMaterialDetails != null && tempMaterialDetails.size() > 0) {
                List<BdMaterialDetail> materialDetails = new ArrayList<>();

                for (BdTempMaterialDetail tempMaterialDetail : tempMaterialDetails) {
                    BdMaterialDetail materialDetail = new BdMaterialDetail();
                    BeanUtils.copyProperties(materialDetail, tempMaterialDetail);

                    materialDetail.setId(null);
                    materialDetail.setPid(thisTemp.getOriginalId());

                    materialDetails.add(materialDetail);
                }
                material.setDetails(materialDetails);
            }

            // 工艺要求
            List<BdTempMaterialProcess> tempMaterialProcesses = thisTemp.getProcesses();
            if (tempMaterialProcesses != null && tempMaterialProcesses.size() > 0) {
                List<BdMaterialProcess> materialProcesses = new ArrayList<>();

                for (BdTempMaterialProcess tempMaterialProcess : tempMaterialProcesses) {
                    BdMaterialProcess materialProcess = new BdMaterialProcess();
                    BeanUtils.copyProperties(materialProcess, tempMaterialProcess);

                    materialProcess.setId(null);
                    materialProcess.setPid(thisTemp.getOriginalId());

                    materialProcesses.add(materialProcess);
                }
                material.setProcesses(materialProcesses);
            }

            // 原材料
            List<BdTempMaterialRaw> tempMaterialRaws = thisTemp.getRaws();
            if (tempMaterialRaws != null && tempMaterialRaws.size() > 0) {
                List<BdMaterialRaw> materialRaws = new ArrayList<>();

                for (BdTempMaterialRaw tempMaterialRaw : tempMaterialRaws) {
                    BdMaterialRaw materialRaw = new BdMaterialRaw();
                    BeanUtils.copyProperties(materialRaw, tempMaterialRaw);

                    materialRaw.setId(null);
                    materialRaw.setPid(thisTemp.getOriginalId());

                    materialRaws.add(materialRaw);
                }
                material.setRaws(materialRaws);
            }

            // 工艺路线
            List<BdTempMaterialRoute> tempMaterialRoutes = thisTemp.getRoutes();
            if (tempMaterialRoutes != null && tempMaterialRoutes.size() > 0) {
                List<BdMaterialRoute> materialRoutes = new ArrayList<>();

                for (BdTempMaterialRoute tempMaterialRoute : tempMaterialRoutes) {
                    BdMaterialRoute materialRoute = new BdMaterialRoute();
                    BeanUtils.copyProperties(materialRoute, tempMaterialRoute);

                    materialRoute.setId(null);
                    materialRoute.setPid(thisTemp.getOriginalId());

                    // 特殊
                    List<BdTempMaterialRouteSpecial> tempMaterialRouteSpecials = tempMaterialRoute.getRouteSpecials();
                    if (tempMaterialRouteSpecials != null && tempMaterialRouteSpecials.size() > 0) {
                        List<BdMaterialRouteSpecial> materialRouteSpecials = new ArrayList<>();

                        for (BdTempMaterialRouteSpecial tempMaterialRouteSpecial : tempMaterialRouteSpecials) {
                            BdMaterialRouteSpecial materialRouteSpecial = new BdMaterialRouteSpecial();
                            BeanUtils.copyProperties(materialRouteSpecial, tempMaterialRouteSpecial);

                            materialRouteSpecial.setId(null);
                            materialRouteSpecial.setPid(null);

                            materialRouteSpecials.add(materialRouteSpecial);
                        }
                        materialRoute.setRouteSpecials(materialRouteSpecials);
                    }

                    materialRoutes.add(materialRoute);
                }
                material.setRoutes(materialRoutes);
            }

            // 规格型号
            List<BdTempMaterialSpecification> tempMaterialSpecifications = thisTemp.getSpecifications();
            if (tempMaterialSpecifications != null && tempMaterialSpecifications.size() > 0) {
                List<BdMaterialSpecification> materialSpecifications = new ArrayList<>();

                for (BdTempMaterialSpecification tempMaterialSpecification : tempMaterialSpecifications) {
                    BdMaterialSpecification materialSpecification = new BdMaterialSpecification();
                    BeanUtils.copyProperties(materialSpecification, tempMaterialSpecification);

                    materialSpecification.setId(null);
                    materialSpecification.setPid(thisTemp.getOriginalId());

                    materialSpecifications.add(materialSpecification);
                }
                material.setSpecifications(materialSpecifications);
            }

            // 供应商
            List<BdTempMaterialSupplier> tempMaterialSuppliers = thisTemp.getSuppliers();
            if (tempMaterialSuppliers != null && tempMaterialSuppliers.size() > 0) {
                List<BdMaterialSupplier> materialSuppliers = new ArrayList<>();

                for (BdTempMaterialSupplier tempMaterialSupplier : tempMaterialSuppliers) {
                    BdMaterialSupplier materialSupplier = new BdMaterialSupplier();
                    BeanUtils.copyProperties(materialSupplier, tempMaterialSupplier);

                    materialSupplier.setId(null);
                    materialSupplier.setPid(thisTemp.getOriginalId());

                    materialSuppliers.add(materialSupplier);
                }
                material.setSuppliers(materialSuppliers);
            }

            if (StringUtils.isNotEmpty(material.getOriginalId())) {
                material.setId(material.getOriginalId());
                material.setStatus(Constants.INT_STATUS_AUDITED);

                BdMaterial result = bdMaterialService.myUpdate(material);
                if (result == null) {
                    throw new BizException("审核失败，请重试1");
                }
            } else {
                material.setId(null);

                BdMaterial result = bdMaterialService.add(material);
                if (result == null) {
                    throw new BizException("审核失败，请重试2");
                } else {
                    result.setStatus(Constants.INT_STATUS_AUDITED);
                    if (!bdMaterialService.updateById(result)) {
                        throw new BizException("审核失败，请重试4");
                    }
                }
            }

            if (!this.removeById(bdTempMaterial.getId())) {
                throw new BizException("审核失败，请重试3");
            }

            // 替换为正式id
            bdTempMaterial.setId(material.getId());
        } else {
            throw new BizException("数据不存在");
        }


        return bdTempMaterial;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<BdTempMaterial> list = this.list(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<BdTempMaterial> submitList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT)).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (BdTempMaterial bdTempMaterial : submitList) {
                    this.submit(bdTempMaterial.getId());
                }
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(BdTempMaterial bdTempMaterial) throws Exception {
        if (bdTempMaterial.getStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(bdTempMaterial.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(bdTempMaterial.getWorkFlowId());
            flowOperationInfo.setFormData(bdTempMaterial);
            flowOperationInfo.setUserId(bdTempMaterial.getUserId());
            flowOperationInfo.setChildNodes(bdTempMaterial.getChildNodes());
            flowOperationInfo.setCurrentNodeId(bdTempMaterial.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(bdTempMaterial.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                bdTempMaterial.setWorkFlowId("");
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
            ids.add(bdTempMaterial.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, bdTempMaterial.getWorkFlow().getId());
            bdTempMaterial.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            bdTempMaterial.setNodeStatus(currentNodes.get(0).getStatus());
            bdTempMaterial.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(bdTempMaterial.getId()) == 1) {
                bdTempMaterial = this.auditUptData(bdTempMaterial);

                return Result.success(bdTempMaterial);
            }
            // 驳回
            if (circulationOperationService.whetherLast(bdTempMaterial.getId()) == 2) {
                bdTempMaterial.setStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(bdTempMaterial)) {
            throw new BizException("操作失败");
        }

        return Result.success(bdTempMaterial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(BdTempMaterial bdTempMaterial) throws Exception {
        List<String> ids = bdTempMaterial.getIds();
        List<BdTempMaterial> materials = this.list(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getId, ids)
        );
        if (materials != null && materials.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, bdTempMaterial.getWorkFlow().getId());
            for (int i = 0; i < materials.size(); i++) {
                BdTempMaterial item = materials.get(i);
                item.setStatus(bdTempMaterial.getStatus());
                item.setWorkFlowId(bdTempMaterial.getWorkFlowId());
                item.setUserId(bdTempMaterial.getUserId());
                item.setChildNodes(bdTempMaterial.getChildNodes());
                item.setChildNodeApprovalResult(bdTempMaterial.getChildNodeApprovalResult());
                item.setWorkFlow(bdTempMaterial.getWorkFlow());
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

    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId){
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
    public BdTempMaterial unAudit(String id) {
        BdTempMaterial result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_AUDITED) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }
        result.setStatus(Constants.INT_STATUS_RESUBMIT);
        result.setAuditTime(null);
        result.setAuditorId(null);
        result.setAuditor(null);
        if (!this.updateById(result)) {
            throw new BizException("反审核失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdTempMaterial> list = this.list(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdTempMaterial> unAuditList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (BdTempMaterial bdTempMaterial : unAuditList) {
                    this.unAudit(bdTempMaterial.getId());
                }
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    public List<BdTempMaterialColor> listColor(BdTempMaterial bdTempMaterial) {
        List<BdTempMaterialColor> list = bdTempMaterialColorService.list(
                new LambdaQueryWrapper<BdTempMaterialColor>()
                        .eq(BdTempMaterialColor::getPid, bdTempMaterial.getId())
        );
        return list;
    }

    @Override
    public List<BdTempMaterialSpecification> listSpecification(BdTempMaterial bdTempMaterial) {
        List<BdTempMaterialSpecification> list = bdTempMaterialSpecificationService.list(
                new LambdaQueryWrapper<BdTempMaterialSpecification>()
                        .eq(BdTempMaterialSpecification::getPid, bdTempMaterial.getId())
        );
        return list;
    }
}
