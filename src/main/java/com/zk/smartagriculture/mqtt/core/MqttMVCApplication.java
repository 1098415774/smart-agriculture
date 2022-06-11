package com.zk.smartagriculture.mqtt.core;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zk.smartagriculture.base.utils.SpringUtil;
import com.zk.smartagriculture.mqtt.annotation.MQTTRequestMapping;
import com.zk.smartagriculture.mqtt.annotation.MQTTResponseBody;
import com.zk.smartagriculture.mqtt.core.analysis.StrWildcard;
import com.zk.smartagriculture.mqtt.core.analysis.handler.DefultPackagingHandler;
import com.zk.smartagriculture.mqtt.core.analysis.handler.JsonPackagingHandler;
import com.zk.smartagriculture.mqtt.core.analysis.handler.MqttPreparserHandler;
import com.zk.smartagriculture.mqtt.core.method.MqttHandlerMethod;
import com.zk.smartagriculture.mqtt.stereotype.MQTTController;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MqttMVCApplication extends MyAbstractMqttMessageHandler implements MessageHandler {

    private ThreadPoolExecutor threadPoolExecutor;

    private String typeAliasesPackage;

    private ClassLoader classloader;

    private String prefixtopic = "";

    private HashMap<String, MqttHandlerMethod> visitMap;

    private HashSet<String> visitGlobbingSet = new HashSet<>();

    private HashMap<String, MqttPreparserHandler> preparserHandler;

    private DefultPackagingHandler packagingHandler = new DefultPackagingHandler();

    private JsonPackagingHandler jsonPackagingHandler;


    public MqttMVCApplication(){
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
    }


    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        System.out.println( " AA --- " + topic);
        MqttHandlerMethod mqttHandlerMethod = visitMap.get(topic);
        if (visitGlobbingSet.size() > 0){
            Iterator<String> it = visitGlobbingSet.iterator();
            while (it.hasNext()){
                String globbing = it.next();
                String noglobbing = globbing.substring(0,globbing.length() - 1);
                if (topic.indexOf(noglobbing) == 0){
                    mqttHandlerMethod = visitMap.get(globbing);
                }
            }
        }
        if (mqttHandlerMethod == null){
            return;
        }
        MqttServiceRunnable serviceRunnable = new MqttServiceRunnable(mqttHandlerMethod,message);
        serviceRunnable.setListener(new MqttSendListener() {
            @Override
            public synchronized void response(MqttMessage mqttMessage) {
                while (true){
                    try {
                        Message<String> responsemessage = MessageBuilder.withPayload(mqttMessage.getMessage()).setHeader(MqttHeaders.TOPIC,mqttMessage.getTopic()).build();
                        if (mqttPahoMessageHandler != null){
                            mqttPahoMessageHandler.handleMessage(responsemessage);
                        }
                        break;
                    }catch (Exception e){

                    }
                }
            }
        });
        serviceRunnable.setPackagingHandler(packagingHandler);
        threadPoolExecutor.execute(serviceRunnable);
    }

    @PostConstruct
    public void init(){
        visitMap = new HashMap<>();
        List<String> urls = getClassUrl(typeAliasesPackage);
        if (urls.size() <= 0){
            return;
        }
        insterVisitMap(urls);
    }

    private List<String> getClassUrl(String path){
        ArrayList<String> paths = new ArrayList<>();
        List<String> filepaths = new ArrayList<>();
        if (StringUtils.isEmpty(path)){
            path = "com.sh.doorbell.devicecontrol.**";
        }
        path = path.trim().replace('.','/');
        String currentdir = this.getClassLoader().getResource("").getPath();
        System.out.println("CUR: " + currentdir);
        for (String s : path.split(";")) {
            paths.add(currentdir + s + "/");
            System.out.println("CURS: " + currentdir + s + "/");
        }
        StrWildcard strWildcard = new StrWildcard("**");
        filepaths = strWildcard.processWildcard(paths);
        return filepaths;
    }

    private void insterVisitMap(List<String> urls){
        try {
            if (urls.size() < 1){
                return;
            }
            String  cc = getClassLoader().getResource("").getPath();
            File classloaderfile = new File(cc);
            for (String url : urls){
                boolean isresponse = false;
                File file = new File(url);
                if (!file.exists() || !file.isFile()){
                    continue;
                }
                ClassLoader loader = this.getClassLoader();
                String current = classloaderfile.getAbsolutePath() + "\\";
                url = url.replace(current,"");
                url = url.replace(".class","");
                url = url.replace("\\",".");
                Class clazz = loader.loadClass(url);
                MQTTController mqttController = (MQTTController) clazz.getAnnotation(MQTTController.class);
                MQTTRequestMapping request = (MQTTRequestMapping) clazz.getAnnotation(MQTTRequestMapping.class);
                MQTTResponseBody response = (MQTTResponseBody) clazz.getAnnotation(MQTTResponseBody.class);
                if (mqttController == null || request == null){
                    continue;
                }
                if (response != null){
                    isresponse = true;
                }
                String fvisiturl = request.value();
                Method[] methods = clazz.getDeclaredMethods();
                Object obj = SpringUtil.getBean(clazz); //基于spring实现
//                Object obj = clazz.newInstance();
                for (Method method : methods){
                    boolean ismresponse = true;
                    MQTTRequestMapping mrequest =  method.getAnnotation(MQTTRequestMapping.class);
                    if (mrequest == null){
                        continue;
                    }
                    response = method.getAnnotation(MQTTResponseBody.class);
                    if (!isresponse && response == null){
                        ismresponse = false;
                    }
                    String visiturl = prefixtopic + fvisiturl + "/" + mrequest.value();
                    MqttHandlerMethod mqttHandlerMethod = new MqttHandlerMethod(obj,method);
                    if (ismresponse){
                        mqttHandlerMethod.setIsresponse(ismresponse);
                        MqttMessage rmsg = new MqttMessage();
                        rmsg.setTopic(response.value());
                        mqttHandlerMethod.setResponsemsg(rmsg);
                    }
                    if (visiturl.endsWith("#")){
                        visitGlobbingSet.add(visiturl);
                    }
                    System.out.println(visiturl);
                    visitMap.put(visiturl,mqttHandlerMethod);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public ClassLoader getClassLoader() {
        return this.classloader == null ? Thread.currentThread().getContextClassLoader() : this.classloader;
    }

    public HashMap<String, MqttPreparserHandler> getPreparserHandler() {
        return preparserHandler;
    }

    public void setPreparserHandler(HashMap<String, MqttPreparserHandler> preparserHandler) {
        this.preparserHandler = preparserHandler;
    }

    public JsonPackagingHandler getJsonPackagingHandler() {
        return jsonPackagingHandler;
    }

    public void setJsonPackagingHandler(JsonPackagingHandler jsonPackagingHandler) {
        this.jsonPackagingHandler = jsonPackagingHandler;
        packagingHandler.setJsonPackagingHandler(jsonPackagingHandler);
    }

    public String getPrefixtopic() {
        return prefixtopic;
    }

    public void setPrefixtopic(String prefixtopic) {
        this.prefixtopic = prefixtopic;
    }

}
