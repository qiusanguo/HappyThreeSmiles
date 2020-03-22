package com.hts.security.server.core;

import java.security.MessageDigest;

/**
 *
 *
 *@author qj
 *
 */


public class KeyUtil {
    private static final char[] SIGNCODE = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };

    /**
     *
     * @param src
     * @return
     */
    public static String getAESKey(String src){
        try {
            byte[] bytes = src.getBytes(Constants.CHARSET);
            MessageDigest md5 = MessageDigest.getInstance("md5");
            StringBuilder sb = new StringBuilder();
            byte[] digest = md5.digest(bytes);
            for(int i = 0 ; i < 16; i++){
                sb.append(SIGNCODE[(digest[i] >> 2) & 0x3D]);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
