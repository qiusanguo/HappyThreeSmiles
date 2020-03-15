package com.hts.security.libkey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 *生成秘钥文件
 *1，秘钥文件包含sha1+pkg+aesSrc;其中sha1和pkg用于标识改秘钥的作用范围。即一个秘钥文件只使用与一个APP
 *2，秘钥文件的信息使用DES加密，APP使用同样的秘钥进行解密验证
 *
 *@author qj
 *
 *
 *
 */


public class GenerateKey {

    private final static  String DES_KEY = "HtS&96$0";

    //证书sha1
    private static String jksSha1 = "D0030147F0841E0DBBD8A861A69AD5CDB8A4C69E";
    //app 包名
    private static String pkgName = "com.hts.security.app";
    //
    private static String aesSrc = "8098oiiIUIUJKJOYYU*23";

    public static void main(String[] args){
        StringBuilder sb = new StringBuilder();
        sb.append(jksSha1).append(";");
        sb.append(pkgName).append(";");
        sb.append(aesSrc);
        try{
            byte[] result = DesUtil.encrypt(DES_KEY,sb.toString());
            //秘钥文件生成在当前项目根目录
            File directory = new File("");
            String saveFile = directory.getCanonicalPath()+File.separator +"libKey";
            writeFile(result,saveFile);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * 保存生成的秘钥文件
     * @param data
     * @param savePath
     * @param fileName
     */
    private static void writeFile(byte[] data,String savePath,String fileName){
        File file = new File(savePath);
        if(!file.exists()) file.mkdirs();
        File saveFile = new File(file, fileName);
        if(saveFile.exists())saveFile.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFile);
            fos.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }

    private static void writeFile(byte[] data,String savePath){
        String fileName = "htsSec";
        writeFile(data,savePath,fileName);
    }

}
