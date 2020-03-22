package com.hts.security.libkey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

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
public class KeyUtil {

    private final static  String DES_KEY = "HtS&96$0";


    /**
     * 秘钥文件生成在当前项目根目录
     * @param jksSha1
     * @param pkgName
     */
    public static void generate(String jksSha1,String pkgName){
        String tem = MD5Util.getMD5String(UUID.randomUUID().toString());
        StringBuilder sb = new StringBuilder();
        sb.append(jksSha1).append(";");
        sb.append(pkgName).append(";");
        sb.append(tem);
        try{
            byte[] result = DesUtil.encrypt(DES_KEY,sb.toString());
            File directory = new File("");
            String saveFile = directory.getCanonicalPath()+File.separator +"libKey";
            String htsSecName = "htsSec";
            String htsSeverName = "htsServer";
            //write hts sec file
            writeFile(result,saveFile,htsSecName);
            //write hts  sec server file
            writeStringToFile(tem,saveFile,htsSeverName);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private static void writeStringToFile(String data,String savePath,String fileName){
        File file = new File(savePath);
        if(!file.exists()) file.mkdirs();
        File saveFile = new File(file, fileName);
        if(saveFile.exists())saveFile.delete();
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(saveFile.getPath(),true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                if(bufferedWriter != null)bufferedWriter.close();
                if(fileWriter != null)fileWriter.close();
            }catch (Exception ex){}

        }
    }


    /**
     * save key to  file
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
