package com.fenglei.service.pur.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.pur.PurPurchaseOrderItemMapper;
import com.fenglei.model.pur.entity.PurPurchaseOrderItem;
import com.fenglei.service.pur.PurPurchaseOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurPurchaseOrderItemServiceImpl extends ServiceImpl<PurPurchaseOrderItemMapper, PurPurchaseOrderItem> implements PurPurchaseOrderItemService {

}
