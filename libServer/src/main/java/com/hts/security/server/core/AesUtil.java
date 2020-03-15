package com.hts.security.server.core;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 *
 *@author qj
 *
 */


public class AesUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String IV = "123456789abcdefg";


    /**
     * encrypt data
     * @param key
     * @param data
     * @return
     * @throws SecException
     */
    public static String encrypt(String key,String data) throws SecException {
        String str;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] ret = cipher.doFinal(data.getBytes(Constants.CHARSET));
            str = Base64.encode(ret);
        } catch (Exception e) {
            throw new SecException(SecExceptionCode.ERROR_UNKNOWN_ERROR, e.getCause());
        }
        return str;
    }

    /**
     * decrypt data
     * @param key
     * @param data
     * @return
     * @throws SecException
     */
    public static String decrypt(String key, String data) throws SecException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] retByte = cipher.doFinal(Base64.decode(data));
            return new String(retByte,Constants.CHARSET);
        } catch (Exception e) {
            throw new SecException(SecExceptionCode.ERROR_UNKNOWN_ERROR, e.getCause());
        }
    }


}
