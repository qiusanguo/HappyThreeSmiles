//
// Created by qj on 2020/1/4.
//
#include "openssl/aes.h"

/***
* src:明文
* srcLen:明文长度
* key:密钥
* keyLen:密钥长度
* outLen:密文长度
* 返回值:密文 需要free
*
*/
unsigned char *aes_encrypt(unsigned char *src, int srcLen, unsigned char *key, int keyLen, int *outLen)
{
    int blockCount = 0;
    int quotient = srcLen / AES_BLOCK_SIZE;
    int mod = srcLen % AES_BLOCK_SIZE;
    blockCount = quotient + 1;

    int padding = AES_BLOCK_SIZE - mod;
    char *in = (char *)malloc(AES_BLOCK_SIZE*blockCount);
    memset(in, padding, AES_BLOCK_SIZE*blockCount);
    memcpy(in, src, srcLen);

    //out
    char *out = (char *)malloc(AES_BLOCK_SIZE*blockCount);
    memset(out, 0x00, AES_BLOCK_SIZE*blockCount);
    *outLen = AES_BLOCK_SIZE*blockCount;
    unsigned char iv[]="123456789abcdefg";
    //encrypt
    AES_KEY aes;
    if (AES_set_encrypt_key((unsigned char*)key, keyLen*8, &aes) < 0)
    {
        return NULL;
    }
    AES_cbc_encrypt((unsigned char*)in, (unsigned char*)out, AES_BLOCK_SIZE*blockCount, &aes, iv, AES_ENCRYPT);
    free(in);

    return (unsigned char*)out;
}


/**
* decrypt
* src:密文
* srcLen:密文长度
* key:密钥
* keyLen:密钥长度
* outLen:明文长度
* 返回值: 明文 需要free
*/
unsigned char *aes_decrypt(unsigned char *src, int srcLen, unsigned char *key, int keyLen, int *outLen)
{

    unsigned char iv[]="123456789abcdefg";

    AES_KEY aes;
    if (AES_set_decrypt_key((unsigned char*)key, keyLen * 8, &aes) < 0)
    {
     return NULL;
    }
    char *tmp = (char *)malloc(srcLen);
    memset(tmp, 0x00, srcLen);
    AES_cbc_encrypt((unsigned char*)src, (unsigned char*)tmp, srcLen, &aes, iv, AES_DECRYPT);
    int unpadding = tmp[srcLen - 1];
    *outLen = srcLen - unpadding;
    char *out = (char *)malloc(*outLen);
    memcpy(out, tmp, *outLen);

    free(tmp);
    return (unsigned char*)out;
}