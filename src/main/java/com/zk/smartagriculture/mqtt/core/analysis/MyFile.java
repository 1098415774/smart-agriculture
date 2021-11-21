package com.zk.smartagriculture.mqtt.core.analysis;



import com.zk.smartagriculture.base.utils.StringUtils;

import java.io.File;
import java.util.LinkedList;

public class MyFile {

    private String path;

    private LinkedList<MyFile> myFiles;

    private String wildcard;

    public MyFile(){}

    public MyFile(String path, String wildcard){
        this.path = path;
        this.wildcard = wildcard;
        myFiles = new LinkedList<>();
    }

    public void createMyFiles(){
        if (StringUtils.isEmpty(path)){
            return;
        }
        String fdirpath = path;
        if (StringUtils.isNotEmpty(wildcard)){
            if (path.contains(wildcard)){
                fdirpath = path.substring(0, path.indexOf(wildcard));
            }
        }

        File dir = new File(fdirpath);
        if (!dir.exists()){
            return;
        }
        if (dir.isDirectory()){
            for (File file : dir.listFiles()){
                MyFile myFile = new MyFile(file.getAbsolutePath() + (path.indexOf(wildcard) > -1 ? path.substring(path.indexOf(wildcard) + wildcard.length()) : ""),wildcard);
                myFile.createMyFiles();
                myFiles.add(myFile);
            }
        }
    }

    public LinkedList<String> getPaths(){
        LinkedList<String> paths = new LinkedList<>();
        for (MyFile myFile : myFiles){
            paths.addAll(myFile.getPaths());
        }
        if (path.endsWith(".class")){
            paths.add(path);
        }
        return paths;
    }


}
