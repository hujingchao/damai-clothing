package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.inv.entity.InvInventory;
import com.fenglei.model.inv.entity.InvIoBill;
import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.fenglei.model.prd.entity.PrdCuttingRawIo;
import com.fenglei.mapper.prd.PrdCuttingRawIoMapper;
import com.fenglei.service.inv.InvInventoryService;
import com.fenglei.service.prd.IPrdCuttingRawIoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 裁床单原材料出库记录 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-15
 */
@Service
public class PrdCuttingRawIoServiceImpl extends ServiceImpl<PrdCuttingRawIoMapper, PrdCuttingRawIo> implements IPrdCuttingRawIoService {
    @Autowired
    InvInventoryService inventoryService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdCuttingRawIo add(PrdCuttingRawIo cuttingRawIo) {
        InvInventory inv = new InvInventory();
        inv.setMaterialDetailId(cuttingRawIo.getMaterialDetailId());
        inv.setQty(cuttingRawIo.getQty());
        inv.setPiQty(cuttingRawIo.getPiQty());
        inv.setRepositoryId(cuttingRawIo.getRepositoryId());
        inv.setPositionId(cuttingRawIo.getPositionId());
        inv.setPrice(cuttingRawIo.getPrice());
        inv.setLot(cuttingRawIo.getLot());
        this.save(cuttingRawIo);

        InvIoBill invIoBill = new InvIoBill();
        invIoBill.setSrcId(cuttingRawIo.getGpId());
        invIoBill.setSrcItemId(cuttingRawIo.getId());
        invIoBill.setSrcType("裁床单-原材料");

        inventoryService.addQty(inv, invIoBill, 1);
        return cuttingRawIo;
    }

    /**
     * 批量新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingRawIo> batchAdd(List<PrdCuttingRawIo> cuttingRawIos) {
        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdCuttingRawIo cuttingRawIo : cuttingRawIos) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(cuttingRawIo.getMaterialDetailId());
            inv.setQty(cuttingRawIo.getQty());
            inv.setPiQty(cuttingRawIo.getPiQty());
            inv.setRepositoryId(cuttingRawIo.getRepositoryId());
            inv.setPositionId(cuttingRawIo.getPositionId());
            inv.setPrice(cuttingRawIo.getPrice());
            inv.setLot(cuttingRawIo.getLot());
            invs.add(inv);
        }
        this.saveBatch(cuttingRawIos);

        for (PrdCuttingRawIo cuttingRawIo : cuttingRawIos) {
            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(cuttingRawIo.getGpId());
            invIoBill.setSrcItemId(cuttingRawIo.getId());
            invIoBill.setSrcType("裁床单-原材料");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty() && !inventoryService.batchSubQty(invs, invIoBills, 1)) {
            throw new BizException("即时库存更新失败");
        }
        return cuttingRawIos;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        PrdCuttingRawIo cuttingRawIo = this.getById(id);
        InvInventory inv = new InvInventory();
        inv.setMaterialDetailId(cuttingRawIo.getMaterialDetailId());
        inv.setQty(cuttingRawIo.getQty());
        inv.setPiQty(cuttingRawIo.getPiQty());
        inv.setRepositoryId(cuttingRawIo.getRepositoryId());
        inv.setPositionId(cuttingRawIo.getPositionId());
        inv.setPrice(cuttingRawIo.getPrice());
        inv.setLot(cuttingRawIo.getLot());

        InvIoBill invIoBill = new InvIoBill();
        invIoBill.setSrcId(cuttingRawIo.getGpId());
        invIoBill.setSrcItemId(cuttingRawIo.getId());
        invIoBill.setSrcType("裁床单-原材料");

        if ( !inventoryService.addQty(inv, invIoBill, 2)) {
            throw new BizException("即时库存更新失败");
        }
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        List<PrdCuttingRawIo> cuttingRawIos = this.listByIds(ids);
        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdCuttingRawIo cuttingRawIo : cuttingRawIos) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(cuttingRawIo.getMaterialDetailId());
            inv.setQty(cuttingRawIo.getQty());
            inv.setPiQty(cuttingRawIo.getPiQty());
            inv.setRepositoryId(cuttingRawIo.getRepositoryId());
            inv.setPositionId(cuttingRawIo.getPositionId());
            inv.setPrice(cuttingRawIo.getPrice());
            inv.setLot(cuttingRawIo.getLot());
            invs.add(inv);

            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(cuttingRawIo.getGpId());
            invIoBill.setSrcItemId(cuttingRawIo.getId());
            invIoBill.setSrcType("裁床单-辅料");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty() && !inventoryService.batchAddQty(invs, invIoBills, 2)) {
            throw new BizException("即时库存更新失败");
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchRemove(List<PrdCuttingRawIo> cuttingRawIos) {
        List<InvInventory> invs = new ArrayList<>();
        List<InvIoBill> invIoBills = new ArrayList<>();
        for (PrdCuttingRawIo cuttingRawIo : cuttingRawIos) {
            InvInventory inv = new InvInventory();
            inv.setMaterialDetailId(cuttingRawIo.getMaterialDetailId());
            inv.setQty(cuttingRawIo.getQty());
            inv.setPiQty(cuttingRawIo.getPiQty());
            inv.setRepositoryId(cuttingRawIo.getRepositoryId());
            inv.setPositionId(cuttingRawIo.getPositionId());
            inv.setPrice(cuttingRawIo.getPrice());
            inv.setLot(cuttingRawIo.getLot());
            invs.add(inv);

            InvIoBill invIoBill = new InvIoBill();
            invIoBill.setSrcId(cuttingRawIo.getGpId());
            invIoBill.setSrcItemId(cuttingRawIo.getId());
            invIoBill.setSrcType("裁床单-原材料");
            invIoBills.add(invIoBill);
        }
        if (!invs.isEmpty() && !inventoryService.batchAddQty(invs, invIoBills, 2)) {
            throw new BizException("即时库存更新失败");
        }
        List<String> ids = cuttingRawIos.stream().map(PrdCuttingRawIo::getId).collect(Collectors.toList());
        return this.removeByIds(ids);
    }

    @Override
    public List<PrdCuttingRawIo> listByPids(List<String> pids) {
        return this.list(Wrappers.lambdaQuery(PrdCuttingRawIo.class).in(PrdCuttingRawIo::getPid, pids));
    }
}
