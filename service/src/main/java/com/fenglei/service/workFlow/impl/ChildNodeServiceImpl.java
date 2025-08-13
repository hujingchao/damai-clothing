package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.ChildNodeMapper;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.workFlow.ChildNodeService;
import org.springframework.stereotype.Service;

/**
 * @author yzy
 */
@Service
public class ChildNodeServiceImpl extends ServiceImpl<ChildNodeMapper, ChildNode> implements ChildNodeService {
}
