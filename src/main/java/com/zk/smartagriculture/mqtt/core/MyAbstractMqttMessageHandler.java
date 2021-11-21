package com.zk.smartagriculture.mqtt.core;

import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageHandler;

public abstract class MyAbstractMqttMessageHandler extends MyAbstractHandler implements MessageHandler {

    protected MqttPahoMessageHandler mqttPahoMessageHandler;

    public MqttPahoMessageHandler getMqttPahoMessageHandler() {
        return mqttPahoMessageHandler;
    }

    public void setMqttPahoMessageHandler(MqttPahoMessageHandler mqttPahoMessageHandler) {
        this.mqttPahoMessageHandler = mqttPahoMessageHandler;
    }
}
