package com.hts.security.server.core;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 *
 *@author qj
 *
 */


public class DesUtil {

    private static final String ALGORITHM = "DES";
    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";


    public static byte[] encrypt(String key,String data) throws Exception{
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(Constants.CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(Constants.CHARSET));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(data.getBytes(Constants.CHARSET));
    }

    public static byte[] decrypt(String key,String data) throws Exception{
        byte[] byteStr = Base64.decode(data);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(Constants.CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(Constants.CHARSET));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        return cipher.doFinal(byteStr);
    }
}
