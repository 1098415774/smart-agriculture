package com.zk.smartagriculture.base.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String s){
        if (s==null || s.length()<=0)
            return true;
        else
            return false;
    }

    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }

    public static Object ISO88591ToUTF8(Object obj) throws IllegalAccessException, UnsupportedEncodingException {
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            field.setAccessible(true);
            Object fieldobj = field.get(obj);
            if (fieldobj instanceof String){
                String str = new String(((String) fieldobj).getBytes("iso-8859-1"),"utf-8");
                field.set(obj,str);
            }
        }
        return obj;
    }

    public static String getRandomString(int length){
        StringBuffer sb = new StringBuffer();
        String string = "abcdefghijklmnopqrstuvwxyz";
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt((int) Math.round(Math.random() * (len-1))));
        }
        return sb.toString();
    }

    public static Object convertToOtherType(String s, Class<?> clazz) throws ParseException {
        Object object = null;
        if (isEmpty(s)){
            return object;
        }
        switch (clazz.getName()){
            case "java.lang.String":
                object = s;
                break;
            case "java.lang.Integer":
                object = Integer.parseInt(s);
                break;
            case "int":
                object = Integer.parseInt(s);
                break;
            case "java.util.Date":
                Pattern pattern = Pattern.compile("\\d{4}[-]\\d{1,2}[-]\\d{1,2}(\\s\\d{2}:\\d{2}(:\\d{2})?)?");
                Matcher isDate = pattern.matcher(s);
                if (isDate.matches()){
                    try {
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        object = sdf.parse(s);
                    }catch (Exception e){
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                        object = sdf.parse(s);
                    }
                    break;
                }
                pattern = Pattern.compile("\\d{4}[/]\\d{1,2}[/]\\d{1,2}(\\s\\d{2}:\\d{2}(:\\d{2})?)?");
                isDate =pattern.matcher(s);
                if (isDate.matches()){
                    try {
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        object = sdf.parse(s);
                    }catch (Exception e){
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");
                        object = sdf.parse(s);
                    }
                    break;
                }
                break;
            case "java.lang.Boolean":
                object = Boolean.parseBoolean(s);
                break;
            case "boolean":
                object = Boolean.parseBoolean(s);
                break;
            case "java.lang.Double":
                object = Double.parseDouble(s);
                break;
            case "double":
                object = Double.parseDouble(s);
                break;
            case "java.lang.Float":
                object = Float.parseFloat(s);
                break;
            case "float":
                object = Float.parseFloat(s);
                break;
            case "java.lang.Long":
                object = Long.parseLong(s);
                break;
            case "long":
                object = Long.parseLong(s);
                break;
            case "java.lang.Byte":
                object = Byte.parseByte(s);
                break;
            case "byte":
                object = Byte.parseByte(s);
                break;
            case "java.lang.Short":
                object = Short.parseShort(s);
                break;
            case "short":
                object = Short.parseShort(s);
                break;
        }

        return object;
    }

//    public static String getFieldString(Field field){
//        Class clazz = field.getClass();
//        switch (clazz.getName()){
//            case
//        }
//    }
}
