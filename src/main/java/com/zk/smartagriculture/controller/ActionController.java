package com.zk.smartagriculture.controller;

import com.zk.smartagriculture.mqtt.annotation.MQTTRequestMapping;
import com.zk.smartagriculture.mqtt.stereotype.MQTTController;

@MQTTController
@MQTTRequestMapping("cc")
public class ActionController {

    @MQTTRequestMapping("test")
    public String test(String str){
        System.out.println(str);
        return "cc";
    }
}
