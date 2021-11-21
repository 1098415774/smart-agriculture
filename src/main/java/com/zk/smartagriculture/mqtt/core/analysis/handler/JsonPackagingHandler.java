package com.zk.smartagriculture.mqtt.core.analysis.handler;

import java.text.ParseException;

public abstract class JsonPackagingHandler {

    public abstract Object parser(String msg, Class<?> parameterType) throws ParseException;
}
