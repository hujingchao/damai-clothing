package com.fenglei.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysMsg;

/**
 * <p>
 * 消息 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-04-13
 */
public interface IMsgService extends IService<SysMsg> {
    Boolean add(SysMsg msg);

    Boolean addCustomerMsg(SysMsg sysMsg);

    Boolean myRemoveById(String id);

    Boolean myUpdate(SysMsg msg);

    IPage<SysMsg> myPage(Page page, SysMsg msg);

    SysMsg detail(String id);

    Boolean haveRead(String id);

    Boolean unRead(String id);

    Boolean allHaveRead();

    Integer unreadNumber();
}
