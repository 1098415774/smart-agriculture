package com.zk.smartagriculture.config;

import com.zk.smartagriculture.mqtt.core.MqttMVCApplication;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.xml.PointToPointChannelParser;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;

//@Configuration
public class BaseConfig {
    @Value("${mqtt.url}")
    private String mqttUrl;
    @Value("${mqtt.clientId}")
    private String mqttClientId;
    @Value("${mqtt.topic}")
    private String mqttTopic;
    @Value("${mqtt.username}")
    private String mqttUsername;
    @Value("${mqtt.password}")
    private String mqttPassword;
    @Bean
    public DefaultMqttPahoClientFactory defaultMqttPahoClientFactory(){
        DefaultMqttPahoClientFactory mqttPahoClientFactory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(mqttUsername);
        mqttConnectOptions.setPassword(mqttPassword.toCharArray());
        mqttConnectOptions.setKeepAliveInterval(2);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttPahoClientFactory.setConnectionOptions(mqttConnectOptions);
        return mqttPahoClientFactory;
    }

    @Bean
    public MessageChannel messageChannel(){
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter(DefaultMqttPahoClientFactory mqttPahoClientFactory,MessageChannel messageChannel){
        MqttPahoMessageDrivenChannelAdapter mqttPahoMessageDrivenChannelAdapter = new MqttPahoMessageDrivenChannelAdapter(mqttUrl,mqttClientId,mqttPahoClientFactory,mqttTopic);
        mqttPahoMessageDrivenChannelAdapter.setOutputChannel(messageChannel);
        return mqttPahoMessageDrivenChannelAdapter;
    }

    @Bean
    public MqttPahoMessageHandler mqttPahoMessageHandler(DefaultMqttPahoClientFactory mqttPahoClientFactory){
        return new MqttPahoMessageHandler("TEST_1",mqttPahoClientFactory);
    }
    @Bean
    @ServiceActivator(inputChannel = "messageChannel")
    public MqttMVCApplication mqttMVCApplication(MqttPahoMessageHandler mqttPahoMessageHandler){
        MqttMVCApplication mqttMVCApplication = new MqttMVCApplication();
        mqttMVCApplication.setMqttPahoMessageHandler(mqttPahoMessageHandler);
        String pre = mqttTopic;
        if (mqttTopic.endsWith("#")){
            pre = mqttTopic.substring(0,mqttTopic.lastIndexOf("#"));
        }
        mqttMVCApplication.setPrefixtopic(pre);
        mqttMVCApplication.setTypeAliasesPackage("com.zk.smartagriculture.**");
        return mqttMVCApplication;
    }

}
