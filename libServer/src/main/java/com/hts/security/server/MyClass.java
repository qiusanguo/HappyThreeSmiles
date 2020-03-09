package com.hts.security.server;

import com.hts.security.server.core.AesUtil;
import com.hts.security.server.core.SecException;

/**
 *
 *
 *@author qj
 *
 */


public class MyClass {

    public static void main(String[] args){
        String data = "hello world security";
        String key = "123456789abcdefg";
        try {
            String encryptData = AesUtil.encrypt(key,data);
            System.out.println("encrypt data :" + encryptData);
            String decryptData = AesUtil.decrypt(key,encryptData);
            System.out.println("decrypt data :" + decryptData);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

}
