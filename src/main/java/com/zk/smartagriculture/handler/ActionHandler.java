package com.zk.smartagriculture.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("action")
@Slf4j
public class ActionHandler {

    private Map<String,WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(WebSocketSession session){
        webSocketSessionMap.put(session.getId(),session);
        log.info(String.format("新的服务加入sessionId:[%s]",session.getId()));
    }
    @OnClose
    public void onClose(WebSocketSession session){
        webSocketSessionMap.remove(session.getId());
    }
}
