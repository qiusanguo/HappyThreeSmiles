package com.teach.zhly.testaarandjar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hts.security.sdk.SecurityUtil;
import com.hts.security.server.core.SecException;
import com.hts.security.server.util.HtsSecurityUtil;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvSrc, tvData;

    private String data = "ipipoi*(*()*)(*三笑4874{“dddd”}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSrc = findViewById(R.id.sample_text);
        tvData = findViewById(R.id.tv_result);
        HtsSecurityUtil.init("9B94085A0427BC75A5AD699DDA5C6849");
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_encrypt) {
            try {
                encrypt();
            } catch (SecException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.btn_decrypt) {
            try {
                decrypt();
            } catch (SecException e) {
                e.printStackTrace();
            }
        }

    }

    private void encrypt() throws SecException {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String tem = random.nextInt(100000) + data + random.nextInt(7000000);
        //客户端加密
        String encryptData = SecurityUtil.encrypt(tem);
        //服务端解密
        String decryptData = HtsSecurityUtil.decrypt(encryptData);

        tvSrc.setText(tem);
        sb.append("客户端加密数据：\n").append(encryptData).append("\n\n");
        sb.append("服务端解密数据：\n").append(decryptData).append("\n\n");
        sb.append("解密后数据与原始数据是否相等：\n");
        sb.append(decryptData.equals(tem));
        tvData.setText(sb.toString());

    }

    private void decrypt() throws SecException {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String tem = random.nextInt(100000) + data + random.nextInt(7000000);
        //模拟服务端加密
        String encryptData = HtsSecurityUtil.encrypt(tem);
        //客户端解密
        String decryptData = SecurityUtil.decrypt(encryptData);

        tvSrc.setText(tem);
        sb.append("服务端加密数据：\n").append(encryptData).append("\n\n");
        sb.append("客户端解密数据：\n").append(decryptData).append("\n\n");
        sb.append("解密后数据与原始数据是否相等：\n");
        sb.append(decryptData.equals(tem));
        tvData.setText(sb.toString());
    }
}