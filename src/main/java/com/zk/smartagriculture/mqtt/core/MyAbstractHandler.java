package com.zk.smartagriculture.mqtt.core;

public abstract class MyAbstractHandler {
    protected MyAbstractHandler myAbstractHandler;

    public MyAbstractHandler getMyAbstractHandler() {
        return myAbstractHandler;
    }

    public void setMyAbstractHandler(MyAbstractHandler myAbstractHandler) {
        this.myAbstractHandler = myAbstractHandler;
    }

    public MqttMessage process(MqttMessage mqttMessage, Object... args) throws Exception{
        if (myAbstractHandler != null){
            mqttMessage = myAbstractHandler.process(mqttMessage, args);
        }
        return mqttMessage;
    }
}
