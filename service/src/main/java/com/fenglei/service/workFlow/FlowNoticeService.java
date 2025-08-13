package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.entity.FlowNotice;

import java.util.List;

/**
 * @author yzy
 */
public interface FlowNoticeService extends IService<FlowNotice> {

    /**
     * 获取审批消息
     * @param flowNotice
     * @return
     */
    List<FlowNotice> getUserNotice(FlowNotice flowNotice);

    /**
     * 获取新的消息数量
     * @return
     */
    Integer getNewInfoCount ();

    /**
     * 将当前用户的所有消息设置为已读状态
     * @return
     */
    Result setAllIsRead();

}
