package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.system.ApprovingMapper;
import com.fenglei.model.system.entity.Approving;
import com.fenglei.service.system.ApprovingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovingServiceImpl extends ServiceImpl<ApprovingMapper, Approving> implements ApprovingService {

    @Override
    public IPage<Approving> myPage(Page page, Approving approving) {
        approving.setNodeUserId(RequestUtils.getUserId());
        IPage<Approving> iPage = baseMapper.getPage(page, approving);
        return iPage;
    }

    @Override
    public List<Approving> myList(Approving approving) {
        approving.setNodeUserId(RequestUtils.getUserId());
        List<Approving> list = baseMapper.getList(approving);
        return list;
    }
}
