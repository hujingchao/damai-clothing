package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.common.util.WebSocket.InfoWebSocketOperate;
import com.fenglei.mapper.system.MsgMapper;
import com.fenglei.model.system.entity.SysMsg;
import com.fenglei.service.system.IMsgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-04-13
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, SysMsg> implements IMsgService {

    @Resource
    InfoWebSocketOperate infoWebSocketOperate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(SysMsg sysMsg) {
        this.save(sysMsg);
        infoWebSocketOperate.sendAdminMessage(sysMsg);
        return true;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addCustomerMsg(SysMsg sysMsg) {
        this.save(sysMsg);
        infoWebSocketOperate.sendWechatMessage(sysMsg);
        infoWebSocketOperate.sendCustomerMessage(sysMsg);
        return true;
    }

    @Override
    public Boolean myRemoveById(String id) {
        return this.removeById(id);
    }

    @Override
    public Boolean myUpdate(SysMsg msg) {
        return this.updateById(msg);
    }

    @Override
    public IPage<SysMsg> myPage(Page page, SysMsg msg) {
        String userId = msg.getUserId();
        if (StringUtils.isEmpty(userId)){
            userId = RequestUtils.getUserId();
        }
        return this.page(page, new LambdaQueryWrapper<SysMsg>()
                .like(StringUtils.isNotEmpty(msg.getTitle()), SysMsg::getTitle, msg.getTitle())
                .like(StringUtils.isNotEmpty(msg.getContent()), SysMsg::getContent, msg.getContent())
                .eq(StringUtils.isNotEmpty(msg.getBizId()), SysMsg::getBizId, msg.getBizId())
                .like(StringUtils.isNotEmpty(msg.getBizNo()), SysMsg::getBizNo, msg.getBizNo())
                .eq(SysMsg::getUserId, userId)
                .eq(StringUtils.isNotEmpty(msg.getIsRead()), SysMsg::getIsRead, msg.getIsRead())
                .orderByDesc(SysMsg::getCreateTime)
        );
    }

    @Override
    public SysMsg detail(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean haveRead(String id) {
        return this.update(new LambdaUpdateWrapper<SysMsg>().set(SysMsg::getIsRead, true).eq(SysMsg::getId, id));
    }

    @Override
    public Boolean unRead(String id) {
        return this.update(new LambdaUpdateWrapper<SysMsg>().set(SysMsg::getIsRead, false).eq(SysMsg::getId, id));
    }

    @Override
    public Boolean allHaveRead() {
        String userId = RequestUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<SysMsg>().set(SysMsg::getIsRead, true).eq(SysMsg::getUserId, userId));
    }

    @Override
    public Integer unreadNumber() {
        String userId = RequestUtils.getUserId();
        return this.count(new LambdaQueryWrapper<SysMsg>().eq(SysMsg::getUserId, userId).eq(SysMsg::getIsRead, false));
    }
}
