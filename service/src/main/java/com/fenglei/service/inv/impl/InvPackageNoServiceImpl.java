package com.fenglei.service.inv.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.inv.InvPackageNoMapper;
import com.fenglei.model.inv.entity.InvPackageNo;
import com.fenglei.service.inv.IInvPackageNoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 打包单包号明细 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
@Service
public class InvPackageNoServiceImpl extends ServiceImpl<InvPackageNoMapper, InvPackageNo> implements IInvPackageNoService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InvPackageNo> batchAdd(List<InvPackageNo> packageNos) {
        this.saveBatch(packageNos);
        return packageNos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPid(String pid) {
        return this.remove(Wrappers.lambdaQuery(InvPackageNo.class).eq(InvPackageNo::getPid, pid));
    }

    @Override
    public List<InvPackageNo> listByPid(String pid) {
        return this.list(Wrappers.lambdaQuery(InvPackageNo.class).eq(InvPackageNo::getPid, pid));
    }
}
