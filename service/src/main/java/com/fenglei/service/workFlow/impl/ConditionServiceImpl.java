package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.ConditionMapper;
import com.fenglei.model.workFlow.entity.Condition;
import com.fenglei.service.workFlow.ConditionService;
import org.springframework.stereotype.Service;

/**
 * @author yzy
 */
@Service
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements ConditionService {
}
