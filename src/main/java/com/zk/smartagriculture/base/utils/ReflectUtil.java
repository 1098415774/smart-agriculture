package com.zk.smartagriculture.base.utils;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static Object getFieldValue(Object obj, String fieldname){
        Object result = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldname);
            field.setAccessible(true);
            result = field.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void setFieldValue(Object obj, String fieldname, Object target){
        try {
            Field field = obj.getClass().getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(obj,target);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
