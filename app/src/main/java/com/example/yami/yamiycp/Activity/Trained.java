package com.example.yami.yamiycp.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yami.yamiycp.Adapters.OrderingAdapter;
import com.example.yami.yamiycp.Service.ClientServer;
import com.example.yami.yamiycp.model.OrderingBean;
import com.example.yami.yamiycp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Trained extends AppCompatActivity {

    private static final String TAG = "Trained";
    private int style = 2;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trained);
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
        style = getIntent().getIntExtra("style",2);
        Log.d(TAG, "onCreate: " + getIntent().getIntExtra("style",2));
        if (style == 2){
            url = "http://csnfjx.youside.cn/webphone/ajax/YueRecHandler.ashx?sele=培训";
        }else if (style == 3){
            TextView textView = findViewById(R.id.textView7);
            textView.setText("已取消");
            url = "http://csnfjx.youside.cn/webphone/ajax/YueRecHandler.ashx?sele=取消";
        }
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh7);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUI();
                Toast.makeText(Trained.this,"刷新成功",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initUI();
    }

    private void initUI() {
        ClientServer.getInstance().getClient(Trained.this,new ClientServer.onClientListener() {
            @Override
            public void respond(final OkHttpClient client,int resultCode) {
                Log.d(TAG, "respond: " + client);
                final Request request = new Request.Builder().url(url).build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = client.newCall(request).execute();
                            Document document = Jsoup.parse(response.body().string());
                            Element element = document.body();
                            Log.d(TAG, "run: " + element.html());
                            Elements titles = element.getElementsByClass("pj_con_ul_d1_p1");
                            Elements date = element.getElementsByClass("pj_con_ul_d2_sp1");
                            Elements time = element.getElementsByClass("pj_con_ul_d2_sp2");
                            Elements learing = element.getElementsByClass("pj_con_ul_d2_p2_sp1");
                            final List<OrderingBean> orderingBeans = new ArrayList<>();
                            for(Element link:titles){
                                OrderingBean orderingBean = new OrderingBean();
                                orderingBean.setTitle(link.text());
                                orderingBeans.add(orderingBean);
                            }
                            for (int i = 0;i<orderingBeans.size();i++){
                                orderingBeans.get(i).setOrderDate(date.get(i).text());
                            }
                            for (int i = 0;i<orderingBeans.size();i++){
                                orderingBeans.get(i).setLearning(learing.get(i).text());
                            }
                            for (int i = 0;i<orderingBeans.size();i++){
                                orderingBeans.get(i).setOrderTime(time.get(i).text());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView recyclerView = findViewById(R.id.recyclerView6);
                                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                                    recyclerView.setAdapter(new OrderingAdapter(orderingBeans,style));
                                    findViewById(R.id.progress_5).setVisibility(View.GONE);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

}
