package com.fenglei.service.pur.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.pur.PurPurchaseInstockItemMapper;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import com.fenglei.service.pur.PurPurchaseInstockItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurPurchaseInstockItemServiceImpl extends ServiceImpl<PurPurchaseInstockItemMapper, PurPurchaseInstockItem> implements PurPurchaseInstockItemService {

}
