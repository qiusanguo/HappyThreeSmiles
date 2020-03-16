//
// Created by qj on 2020/1/3.
//


#include <jni.h>
#include <string>
#include "app-validate.cpp"
#include "hts-aes.cpp"
#include "hts-base64.cpp"
#include "hts-aes-key.cpp"
string MY_AES_KEY = "";

extern "C" {


    //数据加密
    JNIEXPORT jstring JNICALL
    Java_com_hts_security_sdk_SecurityUtil_encrypt(
            JNIEnv* env,
            jobject,jstring data) {
        const char *source = env->GetStringUTFChars(data,0);
        if(MY_AES_KEY.empty()){
            MY_AES_KEY = createKey(file_aes);
        }
        int srcLen = strlen(source);
        int outLen;
        unsigned char *result = aes_encrypt((unsigned char*)source,srcLen,(unsigned char*)MY_AES_KEY.c_str(),16,&outLen);
        string base64Str = base64_encode((const char * )result,outLen);
        jstring jsStr =  env->NewStringUTF(base64Str.c_str());
        free(result);
        env->ReleaseStringUTFChars(data, source);
        return jsStr;
    }

    //数据解密
    JNIEXPORT jstring JNICALL
    Java_com_hts_security_sdk_SecurityUtil_decrypt(
            JNIEnv* env,
            jobject,jstring data) {
        const char* source = env->GetStringUTFChars(data,0);

        if(MY_AES_KEY.empty()){
            MY_AES_KEY = createKey(file_aes);
        }
        //base64 decode
        int out_length_base64;
        char * base64DecodeChar = base64_decode(source,strlen(source),&out_length_base64);
        //aes decrypt
        int outLen;
        unsigned char *decryptChar = aes_decrypt((unsigned char*)base64DecodeChar,out_length_base64,(unsigned char*)MY_AES_KEY.c_str(),16,&outLen);
        string result((const char *)decryptChar,outLen);
        free(decryptChar);
        free(base64DecodeChar);
        env->ReleaseStringUTFChars(data, source);
        return env->NewStringUTF(result.c_str());
    }

    JNIEXPORT jint JNICALL
    JNI_OnLoad(JavaVM *vm, void *reserved) {
        JNIEnv *env;
        if (vm->GetEnv((void **) (&env), JNI_VERSION_1_6) != JNI_OK) {
            return -1;
        }
        if (checkSignature(env) != JNI_TRUE) {
            return -1;
        }

        return JNI_VERSION_1_6;

    }
}



