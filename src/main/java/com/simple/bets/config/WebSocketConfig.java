package com.simple.bets.config;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @Auther: ymfa
 * @Date: 2019/3/13 11:54
 * @Description:
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 将用户信息存储到socket的配置里
        sec.getUserProperties().put("user", SecurityUtils.getSubject().getPrincipal());
        super.modifyHandshake(sec, request, response);
    }
}
