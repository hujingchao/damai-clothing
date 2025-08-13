package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.workFlow.NodeUserMapper;
import com.fenglei.model.workFlow.entity.NodeUser;
import com.fenglei.service.workFlow.NodeUserService;
import org.springframework.stereotype.Service;

/**
 * @author yzy
 */
@Service
public class NodeUserServiceImpl extends ServiceImpl<NodeUserMapper, NodeUser> implements NodeUserService {
}
