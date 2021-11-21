package com.zk.smartagriculture.mqtt.core.analysis.handler;

import com.zk.smartagriculture.mqtt.core.RequetMqttMessage;

import java.text.ParseException;

public interface MqttPreparserHandler {

    String parser(String msg, RequetMqttMessage requetMqttMessage, Object mqttHandlerMethod) throws ParseException;
}
