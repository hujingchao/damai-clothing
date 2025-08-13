package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.WorkFlowDefMapper;
import com.fenglei.model.workFlow.entity.WorkFlowDef;
import com.fenglei.service.workFlow.WorkFlowDefService;
import org.springframework.stereotype.Service;

/**
 * @author yzy
 */
@Service
public class WorkFlowDefServiceImpl extends ServiceImpl<WorkFlowDefMapper, WorkFlowDef> implements WorkFlowDefService {
}
