package com.zk.smartagriculture.base.utils;

import java.util.Base64;

public class CryptologyUtils {

    public static byte[] base64Encode(byte[] data){
        return base64Encode(data,0);
    }

    public static byte[] base64Encode(byte[] data,int offset){
        if (offset > 0){
            int a = 0x7f;
            a = (byte) (a>>offset);
            a ^= 0x7f;
            for (int i = 0; i < data.length; i++){
                int c = data[i]&a;
                data[i] = (byte) ((data[i]& (0x7f >> offset)) << offset );
                c = c>>(7-offset);
                data[i] = (byte) (data[i]|c);
            }
        }
        return Base64.getEncoder().encode(data);
    }

    public static byte[] base64Decode(byte[] data){
        return base64Decode(data,0);
    }

    public static byte[] base64Decode(byte[] data,int offset){
        data = Base64.getDecoder().decode(data);
        if (offset > 0){
            int a = 0x7f;
            a = (byte) (a>> (7 - offset));
            for (int i = 0; i < data.length; i++){
                int c = (data[i]&a) << 7-offset ;
                data[i] = (byte) (data[i] >> offset);
                data[i] = (byte) (data[i] | c);
            }
        }
        return data;
    }
}
