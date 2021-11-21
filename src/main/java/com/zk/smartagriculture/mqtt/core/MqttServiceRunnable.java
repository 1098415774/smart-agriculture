package com.zk.smartagriculture.mqtt.core;

import com.alibaba.fastjson.JSON;
import com.zk.smartagriculture.mqtt.core.analysis.handler.DefultPackagingHandler;
import com.zk.smartagriculture.mqtt.core.analysis.handler.MqttPreparserHandler;
import com.zk.smartagriculture.mqtt.core.method.MqttHandlerMethod;
import org.springframework.messaging.Message;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class MqttServiceRunnable implements Runnable{

    private MqttSendListener listener;

    private MqttHandlerMethod mqttHandlerMethod;

    private Message message;

    private DefultPackagingHandler packagingHandler;

    private MqttPreparserHandler preparserHandler;

    public MqttServiceRunnable(MqttHandlerMethod mqttHandlerMethod, Message<?> message){
        this.mqttHandlerMethod = mqttHandlerMethod;
        this.message = message;
    }

    @Override
    public void run() {
        if (mqttHandlerMethod == null){
            return;
        }
        String msg = message.getPayload().toString();
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        RequetMqttMessage requetMqttMessage = new RequetMqttMessage();
        requetMqttMessage.setTopic(topic);
        requetMqttMessage.setMessage(msg);
        try {
            Object[] args = preparse(msg,requetMqttMessage);//参数封装
            Object result = mqttHandlerMethod.invoke(args);
            if (result == null || !mqttHandlerMethod.isResponse()){
                return;
            }
            MqttMessage mqttMessage = mqttHandlerMethod.getResponsemsg();
            if (result instanceof MqttMessage){
                mqttMessage = (MqttMessage) result;
            } else if (result instanceof String){
                mqttMessage.setMessage((String) result);
            } else {
                String resultmsg = JSON.toJSONString(result); //此处暂定用json实现，可自定义类实现回复封装
                mqttMessage.setMessage(resultmsg);
            }
            listener.response(mqttMessage);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public MqttSendListener getListener() {
        return listener;
    }

    public void setListener(MqttSendListener listener) {
        this.listener = listener;
    }

    private Object[] preparse(String msg, RequetMqttMessage requetMqttMessage) throws ParseException {
        if (this.preparserHandler != null){
            msg = preparserHandler.parser(msg,requetMqttMessage,mqttHandlerMethod);
        }
        return packagingHandler.parser(msg,requetMqttMessage,mqttHandlerMethod);
    }

    public MqttPreparserHandler getPreparserHandler() {
        return preparserHandler;
    }

    public void setPreparserHandler(MqttPreparserHandler preparserHandler) {
        this.preparserHandler = preparserHandler;
    }

    public DefultPackagingHandler getPackagingHandler() {
        return packagingHandler;
    }

    public void setPackagingHandler(DefultPackagingHandler packagingHandler) {
        this.packagingHandler = packagingHandler;
    }
}
