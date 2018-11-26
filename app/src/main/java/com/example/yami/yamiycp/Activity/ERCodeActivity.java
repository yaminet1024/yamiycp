package com.example.yami.yamiycp.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.Utils.ApplicationUtil;

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
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ERCodeActivity extends AppCompatActivity {

    private static final String TAG = "fuck";
    private OkHttpClient client;
    private boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initUi();
        final SwipeRefreshLayout swipeRefreshLayout  = findViewById(R.id.refresh6);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TextView textView = findViewById(R.id.isLeading);
                textView.setText("加载中...");
                if (ready){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getData(client);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    Toast.makeText(ERCodeActivity.this,"请稍等再尝试刷新",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initUi() {
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
        this.client = okHttpClient;
        final Request request = new Request.Builder()
                .url("http://csnfjx.youside.cn/webphone/ajax/StuLogin.ashx?t=0.6502667345405999&loginname=" + ApplicationUtil.getAccount(this) + "&loginpwd= " + ApplicationUtil.getPassword(this) + "&log=")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ready = true;
                        getData(okHttpClient);
                    }
                });
            }
        }).start();
    }

    private void getData(OkHttpClient okHttpClient) throws IOException {
        RequestBody requestBody = new FormBody.Builder().add("type","getlist").build();
        Request request1 = new Request.Builder()
                .url("http://csnfjx.youside.cn/webphone/ajax/shangcherenzheng.ashx")
                .post(requestBody)
                .build();

        Response response1 = okHttpClient.newCall(request1).execute();
        Document document = Jsoup.parse(response1.body().string());
        Element element = document.body();
        Elements elements = element.getElementsByClass("scrz_con_r");
        if (elements.size()!=0 && elements.get(0).html().length() > 61){
            final String code = elements.get(0).html().substring(55,60);
            Log.d("fuck1", "onResponse: " + code);
            final RequestOptions requestOptions = new RequestOptions().error(R.drawable.icon).centerCrop();
            final ImageView imageView =findViewById(R.id.erweima);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(ERCodeActivity.this).load("http://csnfjx.youside.cn/webphone/ajax/codeimg/" + code + ".png")
                            .apply(requestOptions)
                            .into(imageView);
                    Toast.makeText(ERCodeActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();
                    TextView textView = findViewById(R.id.isLeading);
                    textView.setVisibility(View.INVISIBLE);
                }
            });

        }else {
            Log.d(TAG, "getData: " + "加载失败");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = findViewById(R.id.isLeading);
                    textView.setText("加载失败‘(*>﹏<*)′，下拉重试哦。 ");
                }
            });
        }
    }

}
