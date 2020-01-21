package com.hts.security.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hts.security.sdk.SecurityUtil;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tvSrc,tvData;


    private String data = "ipipoi*(*()*)(*三笑4874{“dddd”}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvSrc =  findViewById(R.id.sample_text);
        tvData = findViewById(R.id.tv_result);
    }

    public void onClick(View view) {
        encrypt();
    }

    private void encrypt(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String tem = random.nextInt(100000) + data + random.nextInt(7000000);
        String encryptData = SecurityUtil.encrypt(tem);
        String decryptData = SecurityUtil.decrypt(encryptData);

        tvSrc.setText(tem);

        sb.append("加密数据：\n").append(encryptData).append("\n\n");
        sb.append("解密数据：\n").append(decryptData).append("\n\n");
        sb.append("解密后数据与原始数据是否相等：\n");
        sb.append(decryptData.endsWith(tem));

        tvData.setText(sb.toString());


    }
}
