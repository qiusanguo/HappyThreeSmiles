//
// Created by qj on 2020/1/8.
//


#include <jni.h>
#include <string>
#include "app-signature.cpp"

extern "C" {

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



