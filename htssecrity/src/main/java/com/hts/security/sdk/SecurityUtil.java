package com.hts.security.sdk;

/**
 *
 *
 *@author qj
 *@date 2020/1/8 16:14
 *
 */


public class SecurityUtil {
    static {
        System.loadLibrary("hts-security");
    }


    public static void init(){

    }

    /**
     * 数据加密
     * @param data
     * @return
     */
    public static native String encrypt(String data);

    /**
     * 数据解密
     * @param data
     * @return
     */
    public static native String decrypt(String data);



}
