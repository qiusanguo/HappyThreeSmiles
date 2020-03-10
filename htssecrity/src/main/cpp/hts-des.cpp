//
// Created by qj
//
#include "openssl/des.h"

/***
* src:明文
* srcLen:明文长度
* key:密钥
* keyLen:密钥长度
* outLen:密文长度
* 返回值:密文 需要free
*
*/
unsigned char *des_encrypt(unsigned char *src, int srcLen, unsigned char *aesKey, int keyLen, int *outLen)
{
    int aes_block_size = 8;
    int blockCount = 0;
    int quotient = srcLen / aes_block_size;
    int mod = srcLen % aes_block_size;
    blockCount = quotient + 1;
    int allLength = blockCount * aes_block_size;

    int padding = aes_block_size - mod;
    char *in = (char *)malloc(allLength);
    memset(in, padding, allLength);
    memcpy(in, src, srcLen);

    DES_cblock  key;
    DES_key_schedule key_schedule;
    memcpy(key,aesKey,keyLen);
    DES_set_key_unchecked(&key, &key_schedule);

   //IV
    DES_cblock ivec;
    memcpy(ivec, aesKey, keyLen);
    //out
    char *out = (char *)malloc(allLength);
    memset(out, 0x00, allLength);
    *outLen = allLength;
    DES_ncbc_encrypt((unsigned char*)in, (unsigned char*)out, allLength, &key_schedule, &ivec, DES_ENCRYPT);
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
unsigned char *des_decrypt(unsigned char *src, int srcLen, unsigned char *aesKey, int keyLen, int *outLen)
{

    DES_cblock key;
    DES_key_schedule key_schedule;
    memcpy(key,aesKey,keyLen);
    DES_set_key_unchecked(&key, &key_schedule);
    unsigned char *tmp = (unsigned char *)malloc(srcLen);
    memset(tmp,0,srcLen);
    //IV
    DES_cblock iv;
    memcpy(iv, aesKey, keyLen);
    DES_ncbc_encrypt((const unsigned char *)src, tmp, srcLen, &key_schedule, &iv, DES_DECRYPT);

    int unpadding = tmp[srcLen - 1];
    *outLen = srcLen - unpadding;
    char *out = (char *)malloc(*outLen);
    memcpy(out, tmp, *outLen);

    free(tmp);
    return (unsigned char*)out;
}