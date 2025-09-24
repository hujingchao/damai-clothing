package com.fenglei.service.prd.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.exception.BizException;
import com.fenglei.model.basedata.BdProcedure;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.mapper.prd.PrdCuttingTicketItemReportMapper;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.service.basedata.BdProcedureService;
import com.fenglei.service.prd.IPrdCuttingTicketItemReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.service.system.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单 - 工票信息明细汇报 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Service
public class PrdCuttingTicketItemReportServiceImpl extends ServiceImpl<PrdCuttingTicketItemReportMapper, PrdCuttingTicketItemReport> implements IPrdCuttingTicketItemReportService {

    @Autowired
    BdProcedureService procedureService;
    @Autowired
    ISysUserService userService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdCuttingTicketItemReport add(PrdCuttingTicketItemReport prdCuttingTicketItemReport) {
        this.save(prdCuttingTicketItemReport);
        return prdCuttingTicketItemReport;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingTicketItemReport> batchAdd(List<PrdCuttingTicketItemReport> cuttingTicketItemReports) {
        this.saveBatch(cuttingTicketItemReports);
        return cuttingTicketItemReports;
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
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class).eq(PrdCuttingTicketItemReport::getPid, pid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPids(List<String> pids) {
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class).in(PrdCuttingTicketItemReport::getPid, pids));
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
    public boolean myUpdate(PrdCuttingTicketItemReport prdCuttingTicketItemReport) {
        return this.updateById(prdCuttingTicketItemReport);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<PrdCuttingTicketItemReport> cuttingTicketItemReports) {
        return this.updateBatchById(cuttingTicketItemReports);
    }

    /**
     * 详情
     */
    @Override
    public PrdCuttingTicketItemReport detail(String id) {
        return this.getById(id);
    }

    /**
     * 根据pid获取详细信息
     */
    @Override
    public List<PrdCuttingTicketItemReport> listDetailByPid(String id) {
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class).eq(PrdCuttingTicketItemReport::getPid, id));
        if (!cuttingTicketItemReports.isEmpty()) {
            List<String> collect = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getProcedureId).distinct().collect(Collectors.toList());
            List<BdProcedure> bdProcedures = procedureService.listByIds(collect);
            if (bdProcedures.size() != collect.size()) {
                throw new BizException("部分工序已被删除，请核实！");
            }

            List<String> reporterIds = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getReporterId).distinct().collect(Collectors.toList());
            List<SysUser> sysUsers = userService.listByIds(reporterIds);
//            if (sysUsers.size() != reporterIds.size()) {
//                throw new BizException("部分员工已被删除，请核实！");
//            }
            for (PrdCuttingTicketItemReport cuttingTicketItemReport : cuttingTicketItemReports) {
                BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(cuttingTicketItemReport.getProcedureId())).findFirst().orElse(new BdProcedure());
                cuttingTicketItemReport.setProcedureNum(bdProcedure.getNumber());
                cuttingTicketItemReport.setProcedureName(bdProcedure.getName());
                if (CollectionUtil.isNotEmpty(sysUsers)) {
                    SysUser sysUser = sysUsers.stream().filter(t -> t.getId().equals(cuttingTicketItemReport.getReporterId())).findFirst().orElse(new SysUser());
                    cuttingTicketItemReport.setReporterName(sysUser.getNickname());
                }
            }
        }
        return cuttingTicketItemReports;
    }

    /**
     * 根据pids获取详细信息
     */
    @Override
    public List<PrdCuttingTicketItemReport> listDetailByPids(List<String> ids) {
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class)
                .in(PrdCuttingTicketItemReport::getPid, ids)
                .orderByAsc(PrdCuttingTicketItemReport::getPid, PrdCuttingTicketItemReport::getSeq)
        );
        if (!cuttingTicketItemReports.isEmpty()) {
            List<String> collect = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getProcedureId).distinct().collect(Collectors.toList());
            List<BdProcedure> bdProcedures = procedureService.listByIds(collect);
            if (bdProcedures.size() != collect.size()) {
                throw new BizException("部分工序已被删除，请核实！");
            }
            for (PrdCuttingTicketItemReport cuttingTicketItemReport : cuttingTicketItemReports) {
                BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(cuttingTicketItemReport.getProcedureId())).findFirst().orElse(new BdProcedure());
                cuttingTicketItemReport.setProcedureNum(bdProcedure.getNumber());
                cuttingTicketItemReport.setProcedureName(bdProcedure.getName());
            }
        }
        return cuttingTicketItemReports;
    }

    @Override
    public List<PrdCuttingTicketItemReport> listDetailByCuttingRouteId(String cuttingRouteId) {
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItemReport.class)
                .eq(PrdCuttingTicketItemReport::getCuttingRouteId, cuttingRouteId)
                .orderByAsc(PrdCuttingTicketItemReport::getReportTime)
        );
        if (!cuttingTicketItemReports.isEmpty()) {
            List<String> procedureIds = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getProcedureId).distinct().collect(Collectors.toList());
            List<BdProcedure> bdProcedures = procedureService.listByIds(procedureIds);
            if (bdProcedures.size() != procedureIds.size()) {
                throw new BizException("部分工序已被删除，请核实！");
            }

            List<String> reporterIds = cuttingTicketItemReports.stream().map(PrdCuttingTicketItemReport::getReporterId).distinct().collect(Collectors.toList());
            List<SysUser> sysUsers = userService.listByIds(reporterIds);
//            if (sysUsers.size() != reporterIds.size()) {
//                throw new BizException("部分员工已被删除，请核实！");
//            }

            for (PrdCuttingTicketItemReport cuttingTicketItemReport : cuttingTicketItemReports) {
                BdProcedure bdProcedure = bdProcedures.stream().filter(t -> t.getId().equals(cuttingTicketItemReport.getProcedureId())).findFirst().orElse(new BdProcedure());
                cuttingTicketItemReport.setProcedureNum(bdProcedure.getNumber());
                cuttingTicketItemReport.setProcedureName(bdProcedure.getName());
                if (CollectionUtil.isNotEmpty(sysUsers)) {
                    SysUser sysUser = sysUsers.stream().filter(t -> t.getId().equals(cuttingTicketItemReport.getReporterId())).findFirst().orElse(new SysUser());
                    cuttingTicketItemReport.setReporterName(sysUser.getNickname());
                }
            }
        }
        return cuttingTicketItemReports;
    }

    @Override
    public List<PrdCuttingTicketItemReport> listByCuttingIds(List<String> cuttingIds) {
        return this.baseMapper.listByCuttingIds(cuttingIds);
    }

    @Override
    public List<PrdCuttingTicketItemReport> listByCuttingId(String cuttingId) {
        return this.baseMapper.listByCuttingId(cuttingId);
    }
}
