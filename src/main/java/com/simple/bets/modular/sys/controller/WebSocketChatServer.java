package com.simple.bets.modular.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simple.bets.config.WebSocketConfig;
import com.simple.bets.modular.sys.model.UserModel;
import com.simple.bets.modular.sys.utils.SessionUtil;
import com.simple.bets.modular.sys.utils.SocketMessageUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.tomcat.websocket.WsSession;
import org.aspectj.bridge.MessageUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: ymfa
 * @Date: 2019/3/13 14:52
 * @Description:
 */
@Component
@ServerEndpoint(value="/chat",configurator= WebSocketConfig.class)
public class WebSocketChatServer {

    /**
     * 全部在线会话  PS: 基于场景考虑 这里使用线程安全的Map存储会话对象。
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();


    /**
     * 当客户端打开连接：1.添加会话对象 2.更新在线人数
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        WsSession wsSession = (WsSession)session;
        UserModel user = (UserModel) wsSession.getUserProperties().get("user");

        onlineSessions.put(wsSession.getHttpSessionId(), wsSession);
        sendMessageToAll(SocketMessageUtil.jsonStr(SocketMessageUtil.ENTER,wsSession.getHttpSessionId(),user.getImageBase64(),user.getUsername(),"",onlineSessions.size()));
    }

    /**
     * 当客户端发送消息：1.获取它的用户名和消息 2.发送消息给所有人
     * <p>
     * PS: 这里约定传递的消息为JSON字符串 方便传递更多参数！
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        SocketMessageUtil message = JSON.parseObject(jsonStr, SocketMessageUtil.class);
        sendMessageToAll(SocketMessageUtil.jsonStr(SocketMessageUtil.SPEAK, message.getUsername(), message.getMsg(), onlineSessions.size()));
    }

    /**
     * 当关闭连接：1.移除会话对象 2.更新在线人数
     */
    @OnClose
    public void onClose(Session session) {
        WsSession wsSession = (WsSession)session;
        onlineSessions.remove(wsSession.getHttpSessionId());
        sendMessageToAll(SocketMessageUtil.jsonStr(SocketMessageUtil.QUIT, "", "", onlineSessions.size()));
    }

    /**
     * 当通信发生异常：打印错误日志
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 公共方法：发送信息给所有人
     */
    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((id, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
