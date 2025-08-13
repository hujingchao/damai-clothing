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
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author yzy
 */
@Component
@ServerEndpoint("/websocketWechat/{userId}")
public class WechatWebSocket {

    private Session session;

    private static final CopyOnWriteArraySet<WechatWebSocket> WEB_SOCKETS = new CopyOnWriteArraySet<>();

    private static final Map<String, Session> SESSION_POOL = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userName) {
        this.session = session;
        WEB_SOCKETS.add(this);
        SESSION_POOL.put(userName, session);
        System.out.println(userName + "【websocket消息】有新的连接，总数为:" + WEB_SOCKETS.size());
    }

    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this);
        System.out.println("【websocket消息】连接断开，总数为:" + WEB_SOCKETS.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息:" + message);
    }

    /**
     * 此为广播消息
     */
    public void sendMessage(SysMsg msg) {
        Session session = SESSION_POOL.get("wechat" + msg.getUserId());
        if (session != null) {
            try {
                String json = JSONObject.toJSONString(msg);
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
