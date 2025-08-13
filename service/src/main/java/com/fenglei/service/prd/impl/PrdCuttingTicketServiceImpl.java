package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.mapper.prd.PrdCuttingTicketMapper;
import com.fenglei.model.prd.entity.PrdCuttingRoute;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.vo.CuttingRouteDetailVo;
import com.fenglei.service.prd.IPrdCuttingRouteService;
import com.fenglei.service.prd.IPrdCuttingTicketItemReportService;
import com.fenglei.service.prd.IPrdCuttingTicketItemService;
import com.fenglei.service.prd.IPrdCuttingTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单 - 工票信息 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Service
public class PrdCuttingTicketServiceImpl extends ServiceImpl<PrdCuttingTicketMapper, PrdCuttingTicket> implements IPrdCuttingTicketService {

    @Autowired
    IPrdCuttingTicketItemService cuttingTicketItemService;
    @Autowired
    private IPrdCuttingTicketItemReportService cuttingTicketItemReportService;
    @Autowired
    private IPrdCuttingRouteService cuttingRouteService;

    /**
     * 批量保存工票信息
     *
     * @param cuttingTickets 需要保存的工票信息
     * @return 已经保存的工票信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingTicket> batchAdd(List<PrdCuttingTicket> cuttingTickets) {
        this.saveBatch(cuttingTickets);

        List<PrdCuttingTicketItem> allCuttingTicketItems = new ArrayList<>();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicket.getCuttingTicketItems();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                cuttingTicketItem.setGpId(cuttingTicket.getPid());
                cuttingTicketItem.setPid(cuttingTicket.getId());
                allCuttingTicketItems.add(cuttingTicketItem);
            }
        }
        cuttingTicketItemService.batchAdd(allCuttingTicketItems);
        return cuttingTickets;
    }

    /**
     * 根据pid删除
     *
     * @param pid 裁床单id
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPid(String pid) {
        cuttingTicketItemService.removeByGpId(pid);
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicket.class).eq(PrdCuttingTicket::getPid, pid));
    }


    /**
     * 根据pid删除
     *
     * @param pids 裁床单ids
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPids(List<String> pids) {
        cuttingTicketItemService.removeByGpIds(pids);
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicket.class).in(PrdCuttingTicket::getPid, pids));
    }

    /**
     * 批量修改工票信息
     *
     * @param cuttingTickets 需要修改的工票信息
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveOrUpdate(List<PrdCuttingTicket> cuttingTickets) {
        this.saveOrUpdateBatch(cuttingTickets);
        List<PrdCuttingTicketItem> allCuttingTicketItems = new ArrayList<>();
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicket.getCuttingTicketItems();
            for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
                cuttingTicketItem.setGpId(cuttingTicket.getPid());
                cuttingTicketItem.setPid(cuttingTicket.getId());
                allCuttingTicketItems.add(cuttingTicketItem);
            }
        }
        cuttingTicketItemService.batchSaveOrUpdate(allCuttingTicketItems);
        return true;
    }

    @Override
    public List<PrdCuttingTicket> listByPid(String pid) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicket.class)
                .eq(PrdCuttingTicket::getPid, pid)
                .orderByAsc(PrdCuttingTicket::getPid, PrdCuttingTicket::getSeq)
        );
    }

    @Override
    public List<PrdCuttingTicket> listByPids(List<String> pids) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicket.class)
                .in(PrdCuttingTicket::getPid, pids)
                .orderByAsc(PrdCuttingTicket::getPid, PrdCuttingTicket::getSeq)
        );
    }

    @Override
    public List<PrdCuttingTicket> listDetailByPid(String pid) {
        List<PrdCuttingTicket> cuttingTickets = this.list(Wrappers.lambdaQuery(PrdCuttingTicket.class)
                .eq(PrdCuttingTicket::getPid, pid)
                .orderByAsc(PrdCuttingTicket::getPid, PrdCuttingTicket::getSeq)
        );
        List<String> ids = cuttingTickets.stream().map(PrdCuttingTicket::getId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listDetailByPids(ids);
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            String id = cuttingTicket.getId();
            List<PrdCuttingTicketItem> collect = cuttingTicketItems.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            cuttingTicket.setCuttingTicketItems(collect);
        }
        return cuttingTickets;
    }

    /**
     * 根据裁床单工序id获取裁床单工票信息明细汇报列表
     *
     * @param cuttingRouteId 裁床单工序id
     * @return 返回的裁床单工票信息明细汇报列表
     */
    @Override
    public List<CuttingRouteDetailVo> listRouteDetailByCuttingRouteId(String cuttingRouteId) {
        PrdCuttingRoute cuttingRoute = cuttingRouteService.getById(cuttingRouteId);

        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByCuttingRouteId(cuttingRouteId);

        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listByGpId(cuttingRoute.getPid());

        List<String> cuttingTicketIds = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getPid).collect(Collectors.toList());
        List<PrdCuttingTicket> cuttingTickets = this.listByIds(cuttingTicketIds);

        List<CuttingRouteDetailVo> result = new ArrayList<>();
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            PrdCuttingTicket cuttingTicket = cuttingTickets.stream().filter(t -> t.getId().equals(cuttingTicketItem.getPid())).findFirst().orElse(null);
            if (null == cuttingTicket) {
                throw new BizException("票据信息不存在，请核实！");
            }
            CuttingRouteDetailVo cuttingRouteDetailVo = new CuttingRouteDetailVo();
            cuttingRouteDetailVo.setCuttingRouteId(cuttingRouteId);
            cuttingRouteDetailVo.setQty(cuttingTicketItem.getQty());
            cuttingRouteDetailVo.setCuttingTicketId(cuttingTicketItem.getPid());
            cuttingRouteDetailVo.setCuttingTicketItemId(cuttingTicketItem.getId());
            cuttingRouteDetailVo.setTicketNo(cuttingTicketItem.getTicketNo());
            cuttingRouteDetailVo.setColorId(cuttingTicket.getColorId());
            cuttingRouteDetailVo.setColor(cuttingTicket.getColor());
            cuttingRouteDetailVo.setSpecificationId(cuttingTicket.getSpecificationId());
            cuttingRouteDetailVo.setSpecification(cuttingTicket.getSpecification());
            // 上数详情
            List<PrdCuttingTicketItemReport> reports = cuttingTicketItemReports.stream().filter(t -> t.getPid().equals(cuttingTicketItem.getId())).collect(Collectors.toList());
            cuttingRouteDetailVo.setCuttingTicketItemReports(reports);
            cuttingRouteDetailVo.setReportedQty(reports.stream().map(PrdCuttingTicketItemReport::getReportedQty).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 加入返回值
            result.add(cuttingRouteDetailVo);
        }
        return result;
    }

    @Override
    public List<PrdCuttingTicket> listDetailByIds(List<String> ids) {
        List<PrdCuttingTicket> cuttingTickets = this.list(Wrappers.lambdaQuery(PrdCuttingTicket.class)
                .in(PrdCuttingTicket::getId, ids)
                .orderByAsc(PrdCuttingTicket::getPid, PrdCuttingTicket::getSeq)
        );
        List<PrdCuttingTicketItem> cuttingTicketItems = cuttingTicketItemService.listDetailByPids(ids);
        for (PrdCuttingTicket cuttingTicket : cuttingTickets) {
            String id = cuttingTicket.getId();
            List<PrdCuttingTicketItem> collect = cuttingTicketItems.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            cuttingTicket.setCuttingTicketItems(collect);
        }
        return cuttingTickets;
    }
}
