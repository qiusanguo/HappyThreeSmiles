package com.hts.security.server.util;

import com.hts.security.server.core.AesUtil;
import com.hts.security.server.core.KeyUtil;
import com.hts.security.server.core.SecException;

public class HtsSecurityUtil {

    private static String AES_SRC;
    private static String AES_KEY;

    /**
     *
     * @param data
     */
    public static void init(String data){
        AES_SRC = data;
    }

    /**
     * encrypt data
     * @param data
     * @return
     * @throws SecException
     */
    public static String encrypt(String data) throws SecException {
        return AesUtil.encrypt(getAesKey(),data);
    }

    /**
     * decrypt data
     * @param data
     * @return
     * @throws SecException
     */
    public static String decrypt(String data) throws SecException {
        return AesUtil.decrypt(getAesKey(),data);
    }

    /**
     * get AES KEY
     * @return
     */
    private static String getAesKey(){
        if(AES_KEY == null || AES_KEY.length() == 0){
            AES_KEY = KeyUtil.getAESKey(AES_SRC);
        }
        return AES_KEY;
    }


}
