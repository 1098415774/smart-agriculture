package com.zk.smartagriculture.config;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@Slf4j
public class SocketServer implements Runnable{

    private String url = "rtmp://127.0.0.1:1935/live";

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            Socket socket = null;
            while ((socket = serverSocket.accept()) != null){
                getVideo(socket);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private void getVideo(Socket socket){
        try {
            InputStream inputStream = socket.getInputStream();
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                grabber.start();
                Frame frame = grabber.grabFrame();
                if (frame == null){
                    return;
                }

                FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(url,1280, 720,2);
//                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setFormat("flv");
                recorder.setFrameRate(30);
                recorder.setVideoBitrate(80000);
                recorder.start();
                while (frame != null){
                    recorder.record(frame);
                    frame = grabber.grabFrame();
                }
                recorder.stop();
                grabber.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
