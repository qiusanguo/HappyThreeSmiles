//
// Created by qj on 2020/1/3.
//

#include <string>
#include <vector>
#include <iostream>
#include "hts-des.cpp"
#include <android/asset_manager_jni.h>
using namespace std;

const char HEX_CODE[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
const char *APP_SIGNATURE = "D0030147F0841E0DBBD8A861A69AD5CDB8A4C69E";
char *appSha1 = NULL;
string file_aes;
string file_sha1;
string file_pkg;


/**
 *
 * get sha1  by reflect
 *
 */
char* getAppSha1(JNIEnv *env, jobject context_object){
        if(appSha1 == NULL){
            jclass context_class = env->GetObjectClass(context_object);
            jmethodID methodId = env->GetMethodID(context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
            jobject package_manager = env->CallObjectMethod(context_object, methodId);
            methodId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
            jstring package_name = (jstring)env->CallObjectMethod(context_object, methodId);
            env->DeleteLocalRef(context_class);
            jclass pack_manager_class = env->GetObjectClass(package_manager);
            methodId = env->GetMethodID(pack_manager_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
            env->DeleteLocalRef(pack_manager_class);
            jobject package_info = env->CallObjectMethod(package_manager, methodId, package_name, 0x40);
            env->DeleteLocalRef(package_manager);
            jclass package_info_class = env->GetObjectClass(package_info);
            jfieldID fieldId = env->GetFieldID(package_info_class, "signatures", "[Landroid/content/pm/Signature;");
            env->DeleteLocalRef(package_info_class);
            jobjectArray signature_object_array = (jobjectArray)env->GetObjectField(package_info, fieldId);
            jobject signature_object = env->GetObjectArrayElement(signature_object_array, 0);
            env->DeleteLocalRef(package_info);
            jclass signature_class = env->GetObjectClass(signature_object);
            methodId = env->GetMethodID(signature_class, "toByteArray", "()[B");
            env->DeleteLocalRef(signature_class);
            jbyteArray signature_byte = (jbyteArray) env->CallObjectMethod(signature_object, methodId);
            jclass byte_array_input_class=env->FindClass("java/io/ByteArrayInputStream");
            methodId=env->GetMethodID(byte_array_input_class,"<init>","([B)V");
            jobject byte_array_input=env->NewObject(byte_array_input_class,methodId,signature_byte);
            jclass certificate_factory_class=env->FindClass("java/security/cert/CertificateFactory");
            methodId=env->GetStaticMethodID(certificate_factory_class,"getInstance","(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;");
            jstring x_509_jstring=env->NewStringUTF("X.509");
            jobject cert_factory=env->CallStaticObjectMethod(certificate_factory_class,methodId,x_509_jstring);
            methodId=env->GetMethodID(certificate_factory_class,"generateCertificate",("(Ljava/io/InputStream;)Ljava/security/cert/Certificate;"));
            jobject x509_cert=env->CallObjectMethod(cert_factory,methodId,byte_array_input);
            env->DeleteLocalRef(certificate_factory_class);
            jclass x509_cert_class=env->GetObjectClass(x509_cert);
            methodId=env->GetMethodID(x509_cert_class,"getEncoded","()[B");
            jbyteArray cert_byte=(jbyteArray)env->CallObjectMethod(x509_cert,methodId);
            env->DeleteLocalRef(x509_cert_class);
            jclass message_digest_class=env->FindClass("java/security/MessageDigest");
            methodId=env->GetStaticMethodID(message_digest_class,"getInstance","(Ljava/lang/String;)Ljava/security/MessageDigest;");
            jstring sha1_jstring=env->NewStringUTF("SHA1");
            jobject sha1_digest=env->CallStaticObjectMethod(message_digest_class,methodId,sha1_jstring);
            methodId=env->GetMethodID(message_digest_class,"digest","([B)[B");
            jbyteArray sha1_byte=(jbyteArray)env->CallObjectMethod(sha1_digest,methodId,cert_byte);
            env->DeleteLocalRef(message_digest_class);
            jsize array_size=env->GetArrayLength(sha1_byte);
            jbyte* sha1 =env->GetByteArrayElements(sha1_byte,NULL);
            char *hex_sha=new char[array_size*2+1];
            for (int i = 0; i <array_size ; ++i) {
                hex_sha[2*i]=HEX_CODE[((unsigned char)sha1[i])/16];
                hex_sha[2*i+1]=HEX_CODE[((unsigned char)sha1[i])%16];
            }
            hex_sha[array_size*2]='\0';
            appSha1 = hex_sha;
        }

        return appSha1;
}

/**
 *
 *check app sha1
 *
 */
jboolean checkSignature(JNIEnv *env, jobject context){
   const char *appSignatureSha1 = getAppSha1(env,context);
   jstring releaseSignature = env->NewStringUTF(file_sha1.c_str());
   jstring appSignature = env->NewStringUTF(appSignatureSha1);
   const char *charAppSignature = env->GetStringUTFChars(appSignature, NULL);
   const char *charReleaseSignature = env->GetStringUTFChars(releaseSignature, NULL);
   jboolean result = JNI_FALSE;
    if (charAppSignature != NULL && charReleaseSignature != NULL) {
       if (strcmp(charAppSignature, charReleaseSignature) == 0) {
           result = JNI_TRUE;
       }
    }
    env->ReleaseStringUTFChars(appSignature, charAppSignature);
    env->ReleaseStringUTFChars(releaseSignature, charReleaseSignature);
    return result;
}


/**
 *
 *get Application Context
 *
 */
jobject getApplication(JNIEnv *env){
    jobject application = NULL;
    jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz != NULL) {
        jmethodID currentApplication = env->GetStaticMethodID(
                activity_thread_clz, "currentApplication", "()Landroid/app/Application;");
        if (currentApplication != NULL) {
            application = env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
        }
        env->DeleteLocalRef(activity_thread_clz);
    }
    return application;
}


/**
 *
 *get security file info
 *
 */
jboolean getSecurityFile(JNIEnv *env, jobject context){
    jclass context_class = env->GetObjectClass(context);
    jmethodID methodId= env->GetMethodID(context_class,"getAssets","()Landroid/content/res/AssetManager;");
    jobject asset_manager = env->CallObjectMethod(context, methodId);
    AAssetManager *mgr = AAssetManager_fromJava(env, asset_manager);
    AAsset* asset = AAssetManager_open(mgr, "htsSec", AASSET_MODE_UNKNOWN);
    if(!asset){
        return JNI_FALSE;
    }
    int assetLength = AAsset_getLength(asset);
    char* buffer = (char*)malloc(assetLength);
    memset(buffer,0x00,assetLength);
    AAsset_read(asset,buffer,assetLength);

    //ase decrypt
    const char *desKey = "HtS&96$0";
    int outLen;
    unsigned char * decryptChar = des_decrypt((unsigned char *)buffer,assetLength,(unsigned char *)desKey,8,&outLen);
    string result((const char *)decryptChar,outLen);

    string::size_type pos1,pos2;
    string split = ";";
    pos2 = result.find(split);
    vector<string> v;
    pos1 = 0;
    while(string::npos != pos2){
        v.push_back(result.substr(pos1, pos2-pos1));
        pos1 = pos2 + split.size();
        pos2 = result.find(split, pos1);
    }
    if(pos1 != result.length())
    v.push_back(result.substr(pos1));
    if(v.size() != 3) return JNI_FALSE;
    file_sha1 = v[0];
    file_pkg = v[1];
    file_aes = v[2];

    free(decryptChar);
    free(buffer);
    AAsset_close(asset);
    return JNI_TRUE;

}

/**
 *
 *validate signature
 *
 */
jboolean checkSignature(JNIEnv *env){
    jobject appContext = getApplication(env);
    if(appContext != NULL){
        if(getSecurityFile(env,appContext)){
            return checkSignature(env,appContext);
        }
    }
    return JNI_FALSE;
}