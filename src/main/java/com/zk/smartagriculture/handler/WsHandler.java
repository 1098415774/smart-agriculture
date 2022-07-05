package com.zk.smartagriculture.handler;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsHandler extends BinaryWebSocketHandler {

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    private InputStream inputStream = new PipedInputStream();

    private FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("URL:" + session.getUri());
        System.out.println("新的加入");
        clients.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        clients.remove(session.getId());
    }

    public void sendVideo(byte[] data,String pathKey) {
        BinaryMessage binaryMessage = new BinaryMessage(data);
        for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
            try {
                WebSocketSession session = sessionEntry.getValue();
                if (session.isOpen()) {
                    String path = session.getUri().getPath();
                    String key = path.substring(path.lastIndexOf("/")+1);
                    if (key.equals(pathKey)){
                        session.sendMessage(binaryMessage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
