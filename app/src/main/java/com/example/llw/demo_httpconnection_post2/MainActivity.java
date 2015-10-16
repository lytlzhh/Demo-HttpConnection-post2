package com.example.llw.demo_httpconnection_post2;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private EditText editText;
    private Handler handler;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.edit);
        textView = (TextView) findViewById(R.id.text);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fun();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
               String df = (String) msg.obj;
                textView.setText(df);
            }
        };
    }

    public  void fun(){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://china.ynet.com/3.1/1510/16/10453251.html?source=bdxsy");//指定要提交到的网址
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");//指定发送方式
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);//设置为输出
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(editText.getText().toString()+"UTF-8");//向指定的网址输出数据

                    // httpURLConnection.disconnect();
                    //  httpURLConnection.

                    dataOutputStream.flush();
                    dataOutputStream.close();

                   // sleep(3000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                    Message message = new Message();
                    message.obj = bufferedReader.readLine();
                    handler.sendMessage(message);;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
