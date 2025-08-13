package com.fenglei.common.util.WebSocket;

import com.fenglei.common.exception.BizException;
import com.fenglei.model.system.entity.SysMsg;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yzy
 */
@Component
public class InfoWebSocketOperate {

    @Resource
    private AdminWebSocket adminWebSocket;
    @Resource
    private WechatWebSocket wechatWebSocket;
    @Resource
    private CustomerWebSocket customerWebSocket;

    //admin

    /**
     * 发送信息
     */
    public void sendAdminMessage(SysMsg msg) throws BizException {
        adminWebSocket.sendMessage(msg);
    }
    //admin

    //wechat

    /**
     * 发送信息
     */
    public void sendWechatMessage(SysMsg msg) throws BizException {
        wechatWebSocket.sendMessage(msg);
    }
    //wechat

    //customer

    /**
     * 发送信息
     */
    public void sendCustomerMessage(SysMsg sysMsg) {
        customerWebSocket.sendMessage(sysMsg);
    }

    /**
     * 批量发送信息
     */
    public void batchSendCustomerMessage(List<SysMsg> sysMsgs) {
        customerWebSocket.batchSendMessage(sysMsgs);
    }
    //customer
}
