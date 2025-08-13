package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.prd.entity.PrdTicketLog;
import com.fenglei.mapper.prd.PrdTicketLogMapper;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.service.prd.IPrdTicketLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.service.system.ISysUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhaojunnan
 * @since 2024-05-06
 */
@Service
public class PrdTicketLogServiceImpl extends ServiceImpl<PrdTicketLogMapper, PrdTicketLog> implements IPrdTicketLogService {
    @Resource
    ISysUserService sysUserService;


    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrdTicketLog add(PrdTicketLog prdTicketLog) {
        return null;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return null;
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myUpdate(PrdTicketLog prdTicketLog) {
        return true;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<PrdTicketLog> myPage(Page page, PrdTicketLog prdTicketLog) {
        IPage<PrdTicketLog> prdTicketLogIPage = page(page, new LambdaQueryWrapper<PrdTicketLog>()
                .like(StringUtils.isNotEmpty(prdTicketLog.getRemark()), PrdTicketLog::getRemark, prdTicketLog.getRemark())
        );
        List<PrdTicketLog> records = prdTicketLogIPage.getRecords();
        if (records.size() > 0) {
            List<SysUser> sysUsers = sysUserService.list();
            for (PrdTicketLog record : records) {
                SysUser createUser = sysUsers.stream().filter(s -> StringUtils.isNotEmpty(record.getCreatorId()) && s.getId().equals(record.getCreatorId())).findFirst().orElse(null);
                record.setCreator(createUser.getNickname());
            }
        }
        return prdTicketLogIPage;
    }

    /**
     * 详情
     */
    @Override
    public PrdTicketLog detail(String id) {
        return null;
    }
}
