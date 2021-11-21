package com.zk.smartagriculture.mqtt.core.method;

import com.zk.smartagriculture.mqtt.core.MqttMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MqttHandlerMethod {

    private Object obj;

    private Method method;

    private boolean isresponse = false;

    private MqttMessage responsemsg;

    public MqttHandlerMethod(Object obj, Method method){
        this.obj = obj;
        this.method = method;
    }

    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        if (method.getParameterCount() < 1){
            return method.invoke(obj,null);
        }
        return method.invoke(obj,args);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isResponse() {
        return isresponse;
    }

    public void setIsresponse(boolean isresponse) {
        this.isresponse = isresponse;
    }

    public MqttMessage getResponsemsg() {
        return responsemsg;
    }

    public void setResponsemsg(MqttMessage responsemsg) {
        this.responsemsg = responsemsg;
    }

}
