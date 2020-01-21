//
// Created by qj on 2020/1/4.
//
#include <string>
#include "openssl/evp.h"
#include "openssl/bio.h"
#include "openssl/buffer.h"

using namespace std;



/**
* base64 decode
* input:需要解码内容
* in_length:解码内容长度
* out_length:输出长度
* 返回值: 需要free
*/
char *  base64_decode(const char * input,int in_length,int* out_length){
    BIO * bio = NULL;
    BIO * b64 = NULL;
    char * buffer = (char *)malloc(in_length);
    memset(buffer, 0, in_length);
    b64 = BIO_new(BIO_f_base64());
    BIO_set_flags(b64, BIO_FLAGS_BASE64_NO_NL);
    bio = BIO_new_mem_buf(input, in_length);
    bio = BIO_push(b64, bio);
    *out_length = BIO_read(bio, buffer, in_length);
    BIO_free_all(bio);
    return buffer;
}


/**
* base64 encode
* input:需要编码内容
* in_len:内容长度
* 返回值: 编码后字符串
*/
string base64_encode(const char * input,int in_len){
    BIO * bmen = NULL;
    BIO * b64 = NULL;
    b64 = BIO_new(BIO_f_base64());
    BIO_set_flags(b64,BIO_FLAGS_BASE64_NO_NL);
    bmen = BIO_new(BIO_s_mem());
    b64 = BIO_push(b64, bmen);
    BIO_write(b64, input, in_len);
    //start
    BIO_flush(b64);
    char *p_encoded_data = NULL;
    auto  encoded_len    = BIO_get_mem_data(bmen, &p_encoded_data);
    string result(p_encoded_data, encoded_len);
    BIO_free_all(b64);
    return result;

}





