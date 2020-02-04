package com.example.kandiantoqq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    private String getAI(String str) {
        String res;
        int head=str.indexOf("accountId")+10;
        int tail;
        for(tail=head;str.charAt(tail)!='&';tail++) ;
        res=str.substring(head,tail);
        if(res.contains("%3D")) res=res.replaceAll("%3D","=");
        System.out.println(res);
        return res;
    }

    private static final String Base64Dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";


    private String decodeBase64(String str) {
        String res = "";
        String equals,body;
        if(!str.contains("=")) {
            equals = "";
            body = str;
        } else {
            equals = str.substring(str.indexOf("="));
            body = str.substring(0,str.indexOf("="));
        }
        String bins = "";
        for(int i=0;i<body.length();i++) {
            char c=body.charAt(i);
            int ascii = Base64Dic.indexOf(c);
            String bin = Integer.toBinaryString(ascii);
            while(bin.length()<6) {
                bin="0"+bin;
                //不要过度优化（滑稽）
            }
            bins+=bin;
            //也别过度优化这个（滑稽）
        }
        if(equals.length()>0) {
            bins=bins.substring(0, bins.length()-equals.length()*2);
        }
        String singleChar = "";
        int head=0,tail=0+8;
        while((singleChar=bins.substring(head,tail)).length()==8) {
            res+=(char)Integer.parseInt(singleChar, 2);
            head+=8; tail+=8;
            if(tail>bins.length()) break;
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText1 = findViewById(R.id.editText);
        final EditText editText2 = findViewById(R.id.editText2);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = editText1.getText().toString();
                if(string.contains("accountId")) {
                    String base64String = getAI(string);
                    System.out.println(base64String);
                    if(base64String.length()!=0) {
                        String res=decodeBase64(base64String);
                        editText2.setText(res);
                        Toast.makeText(MainActivity.this,"succeeded!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"链接无效！请仔细查看说明（或者找开发者）。",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"链接无效！请仔细查看说明（或者找开发者）。",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
