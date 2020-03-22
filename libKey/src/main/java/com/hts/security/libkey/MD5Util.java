package com.hts.security.libkey;

import java.security.MessageDigest;

public class MD5Util {

    private final static String[] HEXDIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     *
     * MD5
     * @param data
     * @return
     */
    public static String getMD5String(String data){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(Constants.CHARSET));
            byte bytes[] = md.digest();
            return byteArrayToHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return HEXDIGITS[d1] + HEXDIGITS[d2];
    }

}
