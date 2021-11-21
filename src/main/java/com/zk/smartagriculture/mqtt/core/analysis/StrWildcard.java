package com.zk.smartagriculture.mqtt.core.analysis;

import java.util.LinkedList;
import java.util.List;

public class StrWildcard {

    private int svalue = 0;

    private String str = null;

    public StrWildcard(String str){
//        this.svalue = calculateValue(str);
        this.str = str;
    }

    protected int calculateValue(String str){
        byte[] bytes = str.getBytes();
        int value = 0;
        for (byte b : bytes){
            value = (value << 8) | b;
        }
        return value;
    }

    public List<String> processWildcard(List<String> paths){
        LinkedList<String> propaths = new LinkedList<>();
        for (String path : paths){
            System.out.println("PAT: "+ path);
            path = path.replace("/" + str + "/","/*/");
            if (!path.contains("/*/")){
                continue;
            }
            MyFile file = new MyFile(path,"/*/");
            file.createMyFiles();
            propaths.addAll(file.getPaths());
        }
        return propaths;
    }

}
