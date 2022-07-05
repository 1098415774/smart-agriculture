package com.zk.smartagriculture.controller;

import com.zk.smartagriculture.mqtt.annotation.MQTTRequestMapping;
import com.zk.smartagriculture.mqtt.stereotype.MQTTController;

@MQTTController
@MQTTRequestMapping("car/action")
public class CarActionController {

    @MQTTRequestMapping("move")
    public String move(String str){
        System.out.println(str);
        return "cc";
    }
}
