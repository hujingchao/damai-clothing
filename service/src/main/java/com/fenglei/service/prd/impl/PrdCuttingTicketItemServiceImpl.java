package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.prd.PrdCuttingTicketItemMapper;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.entity.PrdInStockItem;
import com.fenglei.model.prd.vo.CuttingTicketItemDetailVo;
import com.fenglei.service.prd.IPrdCuttingTicketItemReportService;
import com.fenglei.service.prd.IPrdCuttingTicketItemService;
import com.fenglei.service.prd.IPrdCuttingTicketItemZService;
import com.fenglei.service.prd.IPrdInStockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单 - 工票信息明细 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-02
 */
@Service
public class PrdCuttingTicketItemServiceImpl extends ServiceImpl<PrdCuttingTicketItemMapper, PrdCuttingTicketItem> implements IPrdCuttingTicketItemService {

    @Autowired
    private IPrdCuttingTicketItemReportService cuttingTicketItemReportService;
    @Autowired
    private IPrdCuttingTicketItemZService cuttingTicketItemZService;
    @Autowired
    private IPrdInStockItemService prdInStockItemService;

    /**
     * 详情
     */
    @Override
    public PrdCuttingTicketItem detail(String id) {
        PrdCuttingTicketItem byId = this.getById(id);
        if (StringUtils.isNull(byId)) {
            throw new BizException("条码不正确！");
        }
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPid(id);
        byId.setCuttingTicketItemReports(cuttingTicketItemReports);
        return byId;
    }

    @Override
    public PrdCuttingTicketItem detailByTicketNo(String ticketNo) {
        PrdCuttingTicketItem byId = this.getOne(Wrappers.lambdaQuery(PrdCuttingTicketItem.class).eq(PrdCuttingTicketItem::getTicketNo, ticketNo));
        if (StringUtils.isNull(byId)) {
            throw new BizException("条码不正确！");
        }
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPid(byId.getId());
        byId.setCuttingTicketItemReports(cuttingTicketItemReports);
        return byId;
    }

    @Override
    public List<PrdCuttingTicketItem> listbyPid(String pid) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .eq(PrdCuttingTicketItem::getPid, pid)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
    }

    @Override
    public List<PrdCuttingTicketItem> listbyPids(List<String> pids) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .in(PrdCuttingTicketItem::getPid, pids)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
    }

    /**
     * 根据gpid查找
     * @param gpId 裁床单id
     * @return 裁床单票据详情集合
     */
    @Override
    public List<PrdCuttingTicketItem> listByGpId(String gpId) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .eq(PrdCuttingTicketItem::getGpId, gpId)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
    }

    /**
     * 根据gpids查找
     * @param gpIds 裁床单ids
     * @return 裁床单票据详情集合
     */
    @Override
    public List<PrdCuttingTicketItem> listByGpIds(List<String> gpIds) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .in(PrdCuttingTicketItem::getGpId, gpIds)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
    }

    @Override
    public List<PrdCuttingTicketItem> listDetailById(String pid) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .eq(PrdCuttingTicketItem::getPid, pid)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPids(ids);
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            String id = cuttingTicketItem.getId();
            List<PrdCuttingTicketItemReport> collect = cuttingTicketItemReports.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            cuttingTicketItem.setCuttingTicketItemReports(collect);
        }
        return cuttingTicketItems;
    }

    @Override
    public List<PrdCuttingTicketItem> listDetailByPids(List<String> pids) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .in(PrdCuttingTicketItem::getPid, pids)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPids(ids);
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            String id = cuttingTicketItem.getId();
            List<PrdCuttingTicketItemReport> collect = cuttingTicketItemReports.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            cuttingTicketItem.setCuttingTicketItemReports(collect);
        }
        return cuttingTicketItems;
    }

    @Override
    public List<PrdCuttingTicketItem> listDetailByIds(List<String> ids) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.list(Wrappers.lambdaQuery(PrdCuttingTicketItem.class)
                .in(PrdCuttingTicketItem::getId, ids)
                .orderByAsc(PrdCuttingTicketItem::getPid, PrdCuttingTicketItem::getSeq)
        );
        List<PrdCuttingTicketItemReport> cuttingTicketItemReports = cuttingTicketItemReportService.listDetailByPids(ids);
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            String id = cuttingTicketItem.getId();
            List<PrdCuttingTicketItemReport> collect = cuttingTicketItemReports.stream().filter(t -> t.getPid().equals(id)).collect(Collectors.toList());
            cuttingTicketItem.setCuttingTicketItemReports(collect);
        }
        return cuttingTicketItems;
    }


    /**
     * 批量保存工票信息明细
     *
     * @param cuttingTicketItems 需要保存的工票信息明细
     * @return 已经保存的工票信息明细
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingTicketItem> batchAdd(List<PrdCuttingTicketItem> cuttingTicketItems) {
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            String ticketNo = cuttingTicketItemZService.getNo(cuttingTicketItem.getTicketNo());
            cuttingTicketItem.setTicketNo(ticketNo);
            cuttingTicketItem.setQty(cuttingTicketItem.getSaveQty());
        }
        this.saveBatch(cuttingTicketItems);
        return cuttingTicketItems;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPid(String pid) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.listbyPid(pid);
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        cuttingTicketItemReportService.removeByPids(ids);
        //判断是否有工票已入库 入库不能删除
        if(!ids.isEmpty()){
            int count = prdInStockItemService.count(new LambdaQueryWrapper<PrdInStockItem>().in(PrdInStockItem::getTicketItemId, ids));
            if(count>0){
                throw new BizException("该单据已有入库！");
            }
        }
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItem.class).eq(PrdCuttingTicketItem::getPid, pid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPids(List<String> pids) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.listbyPids(pids);
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        //判断是否有工票已入库 入库不能删除
        if(!ids.isEmpty()){
            int count = prdInStockItemService.count(new LambdaQueryWrapper<PrdInStockItem>().in(PrdInStockItem::getTicketItemId, ids));
            if(count>0){
                throw new BizException("该单据已有入库！");
            }
        }
        List<PrdCuttingTicketItemReport> list = cuttingTicketItemReportService.list(new LambdaQueryWrapper<PrdCuttingTicketItemReport>().in(PrdCuttingTicketItemReport::getPid, ids));
        if (!list.isEmpty()){
            throw new BizException("该单据存在已上数的工序！");
        }

        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItem.class).in(PrdCuttingTicketItem::getPid, pids));
    }

    /**
     * 根据gpId删除数据
     *
     * @param gpId 裁床单id
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByGpId(String gpId) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.listByGpId(gpId);
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        //判断是否有工票已入库 入库不能删除
        if(!ids.isEmpty()){
            int count = prdInStockItemService.count(new LambdaQueryWrapper<PrdInStockItem>().in(PrdInStockItem::getTicketItemId, ids));
            if(count>0){
                throw new BizException("该单据已有入库！");
            }
        }
        cuttingTicketItemReportService.removeByPids(ids);
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItem.class).eq(PrdCuttingTicketItem::getGpId, gpId));
    }

    /**
     * 根据gpId删除数据
     *
     * @param gpIds 裁床单id
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByGpIds(List<String> gpIds) {
        List<PrdCuttingTicketItem> cuttingTicketItems = this.listByGpIds(gpIds);
        List<String> ids = cuttingTicketItems.stream().map(PrdCuttingTicketItem::getId).collect(Collectors.toList());
        //判断是否有工票已入库 入库不能删除
        if(!ids.isEmpty()){
            int count = prdInStockItemService.count(new LambdaQueryWrapper<PrdInStockItem>().in(PrdInStockItem::getTicketItemId, ids));
            if(count>0){
                throw new BizException("该单据已有入库！");
            }
        }
        cuttingTicketItemReportService.removeByPids(ids);
        return this.remove(Wrappers.lambdaQuery(PrdCuttingTicketItem.class).in(PrdCuttingTicketItem::getGpId, gpIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveOrUpdate(List<PrdCuttingTicketItem> cuttingTicketItems) {
        for (PrdCuttingTicketItem cuttingTicketItem : cuttingTicketItems) {
            String ticketNo = cuttingTicketItemZService.getNo(cuttingTicketItem.getTicketNo());
            cuttingTicketItem.setTicketNo(ticketNo);
            cuttingTicketItem.setQty(cuttingTicketItem.getSaveQty());
        }
        return this.saveOrUpdateBatch(cuttingTicketItems);
    }
}
