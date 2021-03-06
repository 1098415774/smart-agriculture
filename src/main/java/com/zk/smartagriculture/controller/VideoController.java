package com.zk.smartagriculture.controller;

import com.zk.smartagriculture.handler.WsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("rtsp")
public class VideoController {
    @Autowired
    private WsHandler wsHandler;
    @RequestMapping("receive")
    public String receive(HttpServletRequest request){

        try {
            ServletInputStream inputStream = request.getInputStream();
            int len = -1;
            while ((len =inputStream.available()) !=-1) {
                byte[] data = new byte[len];
                inputStream.read(data);
                wsHandler.sendVideo(data,"receive");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    @RequestMapping("receive2")
    public String receive2(HttpServletRequest request){

        try {
            ServletInputStream inputStream = request.getInputStream();
            int len = -1;
            while ((len =inputStream.available()) !=-1) {
                byte[] data = new byte[len];
                inputStream.read(data);
                wsHandler.sendVideo(data,"receive2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
