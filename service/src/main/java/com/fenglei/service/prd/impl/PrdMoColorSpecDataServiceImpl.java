package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.prd.PrdCuttingTicketMapper;
import com.fenglei.mapper.prd.PrdMoColorSpecDataMapper;
import com.fenglei.model.prd.entity.PrdCuttingTicket;
import com.fenglei.model.prd.entity.PrdCuttingTicketItem;
import com.fenglei.model.prd.entity.PrdCuttingTicketItemReport;
import com.fenglei.model.prd.entity.PrdMoColorSpecData;
import com.fenglei.service.prd.PrdMoColorSpecDataService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrdMoColorSpecDataServiceImpl extends ServiceImpl<PrdMoColorSpecDataMapper, PrdMoColorSpecData> implements PrdMoColorSpecDataService {

    @Autowired
    private PrdCuttingTicketMapper cuttingTicketMapper;

    @Override
    public List<PrdMoColorSpecData> listByPid(String pid) {
        PrdMoColorSpecData srhCSD = new PrdMoColorSpecData();
        srhCSD.setPid(pid);
        return baseMapper.getList(srhCSD);
    }

    @Override
    public List<PrdMoColorSpecData> listDetailByIds(List<String> moItemIds) {
        List<PrdMoColorSpecData> moColorSpecDataList = this.list(Wrappers.lambdaQuery(PrdMoColorSpecData.class)
                .in(PrdMoColorSpecData::getId, moItemIds)
        );
        List<PrdCuttingTicket> prdCuttingTickets = cuttingTicketMapper.selectList(new LambdaQueryWrapper<PrdCuttingTicket>()
                .in(PrdCuttingTicket::getSrcItemId,moItemIds));
        for (PrdMoColorSpecData moColorSpecData : moColorSpecDataList) {
            String id = moColorSpecData.getId();
            List<PrdCuttingTicket> collect = prdCuttingTickets.stream().filter(t -> t.getSrcItemId().equals(id)).collect(Collectors.toList());
            moColorSpecData.setTicketList(collect);
        }
        return moColorSpecDataList;
    }
}
