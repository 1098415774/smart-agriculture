package com.zk.smartagriculture.mqtt.core.analysis.handler;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

public class DefultJsonPackagingHandler extends JsonPackagingHandler {


    public Object parser(String msg, Class<?> parameterType) throws ParseException {
        Object obj = JSONObject.parseObject(msg,parameterType);
        return obj;
    }
}
