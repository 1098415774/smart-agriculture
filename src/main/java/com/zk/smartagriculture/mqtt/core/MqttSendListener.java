package com.zk.smartagriculture.mqtt.core;

public interface MqttSendListener {

    void response(MqttMessage mqttMessage);
}
