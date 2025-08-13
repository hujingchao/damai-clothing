package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdPacking;
import com.fenglei.mapper.prd.PrdPackingMapper;
import com.fenglei.model.prd.entity.PrdPackingItem;
import com.fenglei.model.prd.vo.PrdPackingItemVo;
import com.fenglei.service.prd.IPrdCuttingTicketItemService;
import com.fenglei.service.prd.IPrdPackingItemService;
import com.fenglei.service.prd.IPrdPackingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 包装单 服务实现类
 * </p>
 *
 * @author zgm
 * @since 2024-04-11
 */
@Service
@AllArgsConstructor
public class PrdPackingServiceImpl extends ServiceImpl<PrdPackingMapper, PrdPacking> implements IPrdPackingService {

    private IPrdPackingItemService packingItemService;
    private PrdPackingMapper packingMapper;
    private IPrdCuttingTicketItemService cuttingTicketItemService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdPacking add(PrdPacking prdPacking) {
        if (StringUtils.isEmpty(prdPacking.getNo())) {
            String no = this.getNo();
            prdPacking.setNo(no);
        }
        prdPacking.setBillStatus(2);
        save(prdPacking);
        List<PrdPackingItem> itemList = prdPacking.getItemList();
        for (PrdPackingItem item : itemList) {
            item.setPid(prdPacking.getId());
        }
        packingItemService.saveBatch(itemList);
        rewriteTickPackNum(itemList, "add");
        return prdPacking;
    }




    @Override
    public String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String no = "BZD" + sdf.format(new Date()) + "-";
        PrdPacking old = this.getOne(Wrappers.lambdaQuery(PrdPacking.class)
                .likeRight(PrdPacking::getNo, no)
                .orderByDesc(PrdPacking::getNo)
        );
        if (old != null) {
            String maxNo = old.getNo();
            int pos = maxNo.lastIndexOf("-");
            String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
            int maxNoInt = Integer.parseInt(maxIdxStr);
            String noIdxStr = String.format("%04d", maxNoInt + 1);
            no = no + noIdxStr;
        } else {
            no = no + "0001";
        }
        return no;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        boolean res = removeById(id);
        List<PrdPackingItem> list = packingItemService.list(new LambdaQueryWrapper<PrdPackingItem>().eq(PrdPackingItem::getPid, id));
        rewriteTickPackNum(list, "delete");
        packingItemService.remove(new LambdaQueryWrapper<PrdPackingItem>()
                .eq(PrdPackingItem::getPid, id));
        return res;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        boolean b = removeByIds(ids);
        List<PrdPackingItem> list = packingItemService.list(new LambdaQueryWrapper<PrdPackingItem>().in(PrdPackingItem::getPid, ids));
        rewriteTickPackNum(list, "delete");
        packingItemService.removeByIds(list.stream().map(PrdPackingItem::getId).collect(Collectors.toList()));
        return b;
    }



    /**
     * 分页查询
     */
    @Override
    public IPage<PrdPacking> myPage(Page page, PrdPacking prdPacking) {
        return null;
    }

    @Override
    public IPage<PrdPackingItemVo> wxPage(Page page, PrdPackingItemVo itemVo) {
        IPage<PrdPackingItemVo> prdPackingItemVoIPage = packingMapper.wxPage(page, itemVo);
        return prdPackingItemVoIPage;
    }

    @Override
    public List<Integer> getStatusCountAll(PrdPackingItemVo itemVo) {
        itemVo.setBillStatus(null);
        List<PrdPackingItemVo> itemVoList = packingMapper.wxPage(itemVo);
        List<Integer> list = new ArrayList<>();
        Integer allCount = itemVoList.size();
        long count1 = itemVoList.stream().filter(s -> s.getBillStatus().equals(0)).count();
        long count2 = itemVoList.stream().filter(s -> s.getBillStatus().equals(3)).count();
        long count3 = itemVoList.stream().filter(s -> s.getBillStatus().equals(2)).count();
        list.add(allCount);
        list.add(Integer.valueOf(String.valueOf(count1)));
        list.add(Integer.valueOf(String.valueOf(count2)));
        list.add(Integer.valueOf(String.valueOf(count3)));
        return list;
    }

    /**
     * 详情
     */
    @Override
    public PrdPacking itemDetail(String id, String itemId) {
        PrdPacking packing = packingMapper.itemDetail(id, itemId);
        return packing;
    }

    @Override
    public boolean itemSetTop(String id, Integer setTop) {
        PrdPackingItem byId = packingItemService.getById(id);
        byId.setSetTop(setTop);
        return packingItemService.updateById(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean itemRemove(String id) {
    List<PrdPackingItem> removeList = new ArrayList<>();
        PrdPackingItem byId = packingItemService.getById(id);
        removeList.add(byId);
        rewriteTickPackNum(removeList, "delete");
        boolean b = packingItemService.removeById(id);
        List<PrdPackingItem> list = packingItemService.list(new LambdaQueryWrapper<PrdPackingItem>().eq(PrdPackingItem::getPid, byId.getPid()));
        if(list.isEmpty()){
            removeById(byId.getPid());
        }

        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean itemUpdate(PrdPacking prdPacking) {
        if (StringUtils.isEmpty(prdPacking.getItemId())) {
            throw new BizException("修改单据有误，请刷新重试");
        }
        boolean b = updateById(prdPacking);
        List<String> removeIds = new ArrayList<>();
        removeIds.add(prdPacking.getItemId());
        List<PrdPackingItem> oldItemList = packingItemService.listByIds(removeIds);
        rewriteTickPackNum(oldItemList, "delete");
        packingItemService.removeById(prdPacking.getItemId());
        for (PrdPackingItem item : prdPacking.getItemList()) {
            item.setPid(prdPacking.getId());
        }
        packingItemService.saveBatch(prdPacking.getItemList());

        rewriteTickPackNum(prdPacking.getItemList(), "add");
        return b;
    }

    /**
     * 反写裁床单的数量
     *
     * @param itemList
     * @param type
     */
    @Transactional(rollbackFor = Exception.class)
    public void rewriteTickPackNum(List<PrdPackingItem> itemList, String type) {
        List<String> collect = itemList.stream().map(PrdPackingItem::getTicketItemId).collect(Collectors.toList());
        List<PrdCuttingTicketItem> prdCuttingTicketItems = cuttingTicketItemService.listByIds(collect);
        if(type.equals("add")){
            for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
                for (PrdPackingItem prdPackingItem : itemList) {
                    if(prdCuttingTicketItem.getId().equals(prdPackingItem.getTicketItemId())){
                        prdCuttingTicketItem.setPackQty(prdCuttingTicketItem.getPackQty().add(prdPackingItem.getPackNum()));
                    }
                }
            }
        }else{
            for (PrdCuttingTicketItem prdCuttingTicketItem : prdCuttingTicketItems) {
                for (PrdPackingItem prdPackingItem : itemList) {
                    if(prdCuttingTicketItem.getId().equals(prdPackingItem.getTicketItemId())){
                        prdCuttingTicketItem.setPackQty(prdCuttingTicketItem.getPackQty().subtract(prdPackingItem.getPackNum()));
                    }
                }
            }
        }
        cuttingTicketItemService.updateBatchById(prdCuttingTicketItems);

    }
}
