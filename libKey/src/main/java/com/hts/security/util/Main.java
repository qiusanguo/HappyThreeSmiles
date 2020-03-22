package com.hts.security.util;

import com.hts.security.libkey.KeyUtil;

public class Main {

    public static void main(String[] args){
        String jskSha1 = "D0030147F0841E0DBBD8A861A69AD5CDB8A4C69E";
        String pkgName = "com.teach.zhly.testaarandjar";
        KeyUtil.generate(jskSha1,pkgName);
    }

}
