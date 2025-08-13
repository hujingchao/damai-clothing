package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdPositionMapper;
import com.fenglei.mapper.pur.PurPurchaseInstockItemMapper;
import com.fenglei.model.basedata.BdPosition;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
import com.fenglei.service.basedata.BdPositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdPositionServiceImpl extends ServiceImpl<BdPositionMapper, BdPosition> implements BdPositionService {

    @Override
    public List<BdPosition> myList(BdPosition bdPosition) {
        List<BdPosition> list = this.list(
                new LambdaQueryWrapper<BdPosition>()
                        .eq(StringUtils.isNotEmpty(bdPosition.getPid()), BdPosition::getPid, bdPosition.getPid())
                        .like(StringUtils.isNotEmpty(bdPosition.getName()), BdPosition::getName, bdPosition.getName())
        );

        return list;
    }

}
