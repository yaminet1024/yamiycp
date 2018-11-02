package com.example.yami.yamiycp;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.yami.yamiycp.Adapters.OrderingAdapter;
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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main4Activity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = findViewById(R.id.recyclerView5);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh5);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUI();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Main4Activity.this,"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initUI();
    }

    private void initUI() {
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
                .url("http://csnfjx.youside.cn/webphone/ajax/StuLogin.ashx?t=0.6502667345405999&loginname=" + ApplicationUtil.getAccount(this) + "&loginpwd= " + ApplicationUtil.getPassword(this) + "&log=")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Request request1 = new Request.Builder().url("http://csnfjx.youside.cn/webphone/ajax/YueRecHandler.ashx?sele=预约").build();
                Response response1 = okHttpClient.newCall(request1).execute();
                Document doc = Jsoup.parse(response1.body().string());
                Element body = doc.body();
                Elements elements1 = body.getElementsByClass("pj_con_ul_d1_p1");
                Elements elements2 = body.getElementsByClass("pj_con_ul_d2_sp1");
                Elements elements3 = body.getElementsByClass("pj_con_ul_d2_sp2");
                final List<OrderingBean> orderingBeans = new ArrayList<OrderingBean>();
                for(Element link:elements1){
                    OrderingBean orderingBean = new OrderingBean();
                    orderingBean.setTitle(link.text());
                    orderingBeans.add(orderingBean);
                }
                for (int i = 0;i<orderingBeans.size();i++){
                    orderingBeans.get(i).setOrderDate(elements2.get(i).text());
                }
                for (int i = 0;i<orderingBeans.size();i++){
                    orderingBeans.get(i).setOrderTime(elements3.get(i).text());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(new OrderingAdapter(orderingBeans));
                    }
                });
            }
        });
    }
}
