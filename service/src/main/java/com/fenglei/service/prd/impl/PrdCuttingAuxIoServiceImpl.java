package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.mapper.prd.PrdCuttingAuxIoMapper;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.prd.entity.PrdCuttingAuxIo;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.prd.IPrdCuttingAuxIoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单辅料出库记录 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-17
 */
@Service
public class PrdCuttingAuxIoServiceImpl extends ServiceImpl<PrdCuttingAuxIoMapper, PrdCuttingAuxIo> implements IPrdCuttingAuxIoService {
    @Autowired
    InvInventoryService inventoryService;

    /**
     * 批量新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingAuxIo> batchAdd(List<PrdCuttingAuxIo> cuttingAuxIos) {
        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdCuttingAuxIo prdCuttingAuxIo : cuttingAuxIos) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(prdCuttingAuxIo.getMaterialDetailId());
            inv.setQty(prdCuttingAuxIo.getQty());
            inv.setRepositoryId(prdCuttingAuxIo.getRepositoryId());
            inv.setPositionId(prdCuttingAuxIo.getPositionId());
            inv.setPrice(prdCuttingAuxIo.getPrice());
            inv.setLot(prdCuttingAuxIo.getLot());
            invs.add(inv);
        }
        this.saveBatch(cuttingAuxIos);

        for (PrdCuttingAuxIo cuttingAuxIo : cuttingAuxIos) {
            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(cuttingAuxIo.getPid());
            invIoBill.setSrcItemId(cuttingAuxIo.getId());
            invIoBill.setSrcType("裁床单-辅料");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty() && !inventoryService.batchSubQty(invs, invIoBills, 1)) {
            throw new BizException("即时库存更新失败");
        }
        return cuttingAuxIos;
    }

    /**
     * 根据pid查找
     *
     * @param pid 裁床单id
     */
    @Override
    public List<PrdCuttingAuxIo> listByPid(String pid) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingAuxIo.class).eq(PrdCuttingAuxIo::getPid, pid));
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemove(List<PrdCuttingAuxIo> cuttingAuxIos) {
        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdCuttingAuxIo prdCuttingAuxIo : cuttingAuxIos) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(prdCuttingAuxIo.getMaterialDetailId());
            inv.setQty(prdCuttingAuxIo.getQty());
            inv.setRepositoryId(prdCuttingAuxIo.getRepositoryId());
            inv.setPositionId(prdCuttingAuxIo.getPositionId());
            inv.setPrice(prdCuttingAuxIo.getPrice());
            inv.setLot(prdCuttingAuxIo.getLot());
            invs.add(inv);

            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(prdCuttingAuxIo.getPid());
            invIoBill.setSrcItemId(prdCuttingAuxIo.getId());
            invIoBill.setSrcType("裁床单-辅料");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty() && !inventoryService.batchAddQty(invs, invIoBills, 2)) {
            throw new BizException("即时库存更新失败");
        }
        List<String> ids = cuttingAuxIos.stream().map(PrdCuttingAuxIo::getId).collect(Collectors.toList());
        return this.removeByIds(ids);
    }
}
