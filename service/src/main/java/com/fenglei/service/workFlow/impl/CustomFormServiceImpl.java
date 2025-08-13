package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.CustomFormMapper;
import com.fenglei.model.workFlow.entity.CustomForm;
import com.fenglei.service.workFlow.CustomFormService;
import org.springframework.stereotype.Service;

/**
 * @author yzy
 */
@Service
public class CustomFormServiceImpl extends ServiceImpl<CustomFormMapper, CustomForm> implements CustomFormService {
}
