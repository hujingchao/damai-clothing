package com.fenglei.common.util.WebSocket;

import com.alibaba.fastjson.JSONObject;
import com.fenglei.model.system.entity.SysMsg;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocketCustomer/{userId}")
public class CustomerWebSocket {
    private String userName;

    private static final CopyOnWriteArraySet<CustomerWebSocket> WEB_SOCKETS = new CopyOnWriteArraySet<>();

    private static final Map<String, Session> SESSION_POOL = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userName) {
        this.userName = userName;
        WEB_SOCKETS.add(this);
        SESSION_POOL.put(userName, session);
        System.out.println(userName + "【websocket消息】有新的连接:" + WEB_SOCKETS);
    }

    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this);
        SESSION_POOL.remove(this.userName);
//        System.out.println("【websocket消息】连接断开:" + WEB_SOCKETS);
    }

    @OnMessage
    public void onMessage(String message) {
//        System.out.println("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 此为广播消息
     */
    public void sendMessage(SysMsg sysMsg) {
        Session session = SESSION_POOL.get("customer" + sysMsg.getUserId());
        if (session != null) {
            try {
                String json = JSONObject.toJSONString(sysMsg);
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量广播消息
     */
    public void batchSendMessage(List<SysMsg> sysMsgs) {
        for (SysMsg sysMsg : sysMsgs) {
            Session session = SESSION_POOL.get("customer" + sysMsg.getUserId());
            if (session != null) {
                try {
                    String json = JSONObject.toJSONString(sysMsg);
                    session.getAsyncRemote().sendText(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

