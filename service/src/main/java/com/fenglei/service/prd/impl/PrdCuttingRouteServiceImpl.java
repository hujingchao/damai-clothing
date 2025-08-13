package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.exception.BizException;
import com.fenglei.model.basedata.BdProcedure;
import com.fenglei.model.prd.entity.PrdCuttingRoute;
import com.fenglei.mapper.prd.PrdCuttingRouteMapper;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.service.basedata.BdProcedureService;
import com.fenglei.service.prd.IPrdCuttingRouteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.service.prd.IPrdCuttingTicketItemReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单 - 工序 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-03
 */
@Service
public class PrdCuttingRouteServiceImpl extends ServiceImpl<PrdCuttingRouteMapper, PrdCuttingRoute> implements IPrdCuttingRouteService {
    @Autowired
    BdProcedureService procedureService;
    @Autowired
    IPrdCuttingTicketItemReportService cuttingTicketItemReportService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdCuttingRoute add(PrdCuttingRoute prdCuttingRoute) {
        this.save(prdCuttingRoute);
        return prdCuttingRoute;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingRoute> batchAdd(List<PrdCuttingRoute> cuttingRoutes) {
        this.saveBatch(cuttingRoutes);
        return cuttingRoutes;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPid(String pid) {
        return this.remove(Wrappers.lambdaQuery(PrdCuttingRoute.class).eq(PrdCuttingRoute::getPid, pid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPids(List<String> pids) {
        return this.remove(Wrappers.lambdaQuery(PrdCuttingRoute.class).in(PrdCuttingRoute::getPid, pids));
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return this.removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(PrdCuttingRoute prdCuttingRoute) {
        return this.updateById(prdCuttingRoute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveOrUpdate(List<PrdCuttingRoute> cuttingRoutes) {
        return this.saveOrUpdateBatch(cuttingRoutes);
    }

    /**
     * 详情
     */
    @Override
    public PrdCuttingRoute detail(String id) {
        PrdCuttingRoute cuttingRoute = this.getById(id);
        BdProcedure bdProcedure = procedureService.getById(cuttingRoute.getProcedureId());
        if (null == bdProcedure) {
            throw new BizException("部分工序已被删除，请核实！");
        }
        cuttingRoute.setProcedureNum(bdProcedure.getNumber());
        cuttingRoute.setProcedureName(bdProcedure.getName());
        return cuttingRoute;
    }

    @Override
    public List<PrdCuttingRoute> listByPid(String pid) {
        List<PrdCuttingRoute> list = this.list(Wrappers.lambdaQuery(PrdCuttingRoute.class)
                .eq(PrdCuttingRoute::getPid, pid)
                .orderByAsc(PrdCuttingRoute::getProcedureNo)
        );
        if (!list.isEmpty()) {
            List<String> collect = list.stream().map(PrdCuttingRoute::getProcedureId).distinct().collect(Collectors.toList());
            List<BdProcedure> bdProcedures = procedureService.listByIds(collect);
            if (bdProcedures.size() != collect.size()) {
                throw new BizException("部分工序已被删除，请核实！");
            }
            for (PrdCuttingRoute cuttingRoute : list) {
                BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(cuttingRoute.getProcedureId())).findFirst().orElse(new BdProcedure());
                cuttingRoute.setProcedureNum(bdProcedure.getNumber());
                cuttingRoute.setProcedureName(bdProcedure.getName());
            }
        }
        return list;
    }

    /**
     * 根据pids查询
     */
    @Override
    public List<PrdCuttingRoute> listByPids(Collection<String> pids) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingRoute.class)
                .in(PrdCuttingRoute::getPid, pids)
                .orderByAsc(PrdCuttingRoute::getProcedureNo)
        );
    }

    @Override
    public List<PrdCuttingRoute> listDetailByIds(List<String> routeIds) {
        List<PrdCuttingRoute> list = this.list(Wrappers.lambdaQuery(PrdCuttingRoute.class)
                .in(PrdCuttingRoute::getId, routeIds)
        );
        List<PrdCuttingTicketItemReport> itemReportList = cuttingTicketItemReportService.list(new LambdaQueryWrapper<PrdCuttingTicketItemReport>()
                .in(PrdCuttingTicketItemReport::getCuttingRouteId, routeIds));
        for (PrdCuttingRoute cuttingRoute : list) {
            String id = cuttingRoute.getId();
            List<PrdCuttingTicketItemReport> collect = itemReportList.stream().filter(t -> t.getCuttingRouteId().equals(id)).collect(Collectors.toList());
            cuttingRoute.setCuttingTicketItemReports(collect);
        }
        return list;
    }

}
