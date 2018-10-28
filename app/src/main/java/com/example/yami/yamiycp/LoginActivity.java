package com.example.yami.yamiycp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.GenerateProcessButton;
import com.example.yami.yamiycp.Utils.ApplicationUtil;
import com.example.yami.yamiycp.Utils.SPHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText account;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        registerListening();
    }

    private void registerListening() {
        GenerateProcessButton generateProcessButton = findViewById(R.id.login);
        generateProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final String accountText = account.getText().toString();
        final String passwordText = password.getText().toString();
        ApplicationUtil.setAccount(this,accountText);
        ApplicationUtil.setPassword(this,passwordText);
        new Thread(new Runnable() {
           @Override
           public void run() {
               final HashMap<String,List<Cookie>> cookieStore = new HashMap<>();
               final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                       .cookieJar(new CookieJar() {
                           @Override
                           public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                               cookieStore.put(url.host(),cookies);
                           }

                           @Override
                           public List<Cookie> loadForRequest(HttpUrl url) {
                               List<Cookie> cookies = cookieStore.get(url.host());
                               return cookies!=null ? cookies : new ArrayList<Cookie>();
                           }
                       })
                       .build();
               Request request = new Request.Builder()
                       .url("http://csnfjx.youside.cn/webphone/ajax/StuLogin.ashx?t=0.6502667345405999&loginname=" + accountText + "&loginpwd= " + passwordText + "&log=")
                       .build();
               Call call = okHttpClient.newCall(request);
               call.enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {

                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       if (response.body().string().equals("goto")){
                           ApplicationUtil.setAccount(LoginActivity.this,accountText);
                           ApplicationUtil.setPassword(LoginActivity.this,passwordText);
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                               }
                           });
                           finish();
                       }else {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   }
               });

           }
       }).start();
    }

    private void initView() {
        //toolbar设置
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //是否记住密码
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ApplicationUtil.setIsRememberPassword(LoginActivity.this,isChecked);
            }
        });
        if(ApplicationUtil.IsRememberPassword(this)){
            checkBox.setChecked(true);
            if (!ApplicationUtil.getAccount(this).isEmpty()){
                account.setText(ApplicationUtil.getAccount(this));
            }
            if (!ApplicationUtil.getPassword(this).isEmpty()){
                password.setText(ApplicationUtil.getPassword(this));
            }
        }
        
    }



}
