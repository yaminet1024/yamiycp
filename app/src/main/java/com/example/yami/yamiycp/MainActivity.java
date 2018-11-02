package com.example.yami.yamiycp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.yami.yamiycp.Utils.ApplicationUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private List<Teacher> teacherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("请先登陆");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        checkUpdate();
        initView();
    }

    private void checkUpdate() {
        OkHttpClient client = new OkHttpClient();

    }

    private void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout linearLayout = (LinearLayout) navigationView.getHeaderView(0);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        if (!ApplicationUtil.getAccount(this).isEmpty()){
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        getSupportActionBar().setTitle("数据加载中");
        final RecyclerView recyclerView = findViewById(R.id.index_list);
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
                Request teacherInfo = new Request.Builder()
                        .url("http://csnfjx.youside.cn/webphone/ajax/TeacherList.ashx")
                        .build();
                Response teacherBody = okHttpClient.newCall(teacherInfo).execute();
                assert teacherBody.body() != null;
                Document doc = Jsoup.parse(teacherBody.body().string());
                Element body = doc.body();
                Elements names = body.getElementsByClass("rw_list_left_up_r_p1");
                Elements numbers = body.getElementsByClass("rw_list_left_up_l");
                teacherList.clear();
                for (Element name : names){
                    Teacher teacher = new Teacher();
                    teacher.setTeacherName(name.text());
                    teacherList.add(teacher);
                }
                for (int i = 0;i<numbers.size();i++){
                    teacherList.get(i).setTeacherNumber(numbers.get(i).attributes().get("onclick").substring(8,12));
                }
                for (int j=0 ;j<teacherList.size();j++){
                    if (teacherList.get(j).getTeacherName().equals("浣伟平")){
                        Collections.swap(teacherList,0,j);
                        break;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(new IndexListAdapter(teacherList));
                        getSupportActionBar().setTitle("教练预约");
                    }
                });
                initMyInfo(okHttpClient);
            }
        });

    }

    private void initMyInfo(OkHttpClient okHttpClient) {
        if (!ApplicationUtil.getName(this).isEmpty()){
            initDrawer();
            return;
        }
        Request info = new Request.Builder()
                .url("http://csnfjx.youside.cn/webphone/bmxx.aspx")
                .build();
        Request info1 = new Request.Builder()
                .url("http://csnfjx.youside.cn/webphone/grzx.aspx")
                .build();
        try {
            Response response = okHttpClient.newCall(info).execute();
            Document doc = Jsoup.parse(response.body().string());
            Element document = doc.body();
            Elements elements = document.getElementsByClass("xm_span_r");
            ApplicationUtil.setName(this,elements.get(0).text());
            ApplicationUtil.setReportData(this,elements.get(1).text());
            ApplicationUtil.setIdCard(this,elements.get(2).text());
            ApplicationUtil.setPhone(this,elements.get(3).text());
            ApplicationUtil.setCarStyle(this,elements.get(4).text());
            ApplicationUtil.setLearnProject(this,elements.get(6).text());
            ApplicationUtil.setRemainProject(this,elements.get(7).text());
            response = okHttpClient.newCall(info1).execute();
            doc = Jsoup.parse(response.body().string());
            document = doc.body();
            elements = document.getElementsByClass("xs_span3_p1");
            ApplicationUtil.setLearning(this,elements.text());
            initDrawer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDrawer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                LinearLayout linearLayout = (LinearLayout) navigationView.getHeaderView(0);
                TextView textView = linearLayout.findViewById(R.id.user_name);
                TextView textView1 = linearLayout.findViewById(R.id.textView);
                textView.setText(ApplicationUtil.getName(MainActivity.this)+ ",您好");
                textView1.setText("当前的学习进度为：" + ApplicationUtil.getLearning(MainActivity.this));
                ImageView imageView = linearLayout.findViewById( R.id.imageView);
                Glide.with(MainActivity.this)
                        .load("http://csnfjx.youside.cn/webphone/images/tx.jpg")
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imageView);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (!ApplicationUtil.getAccount(this).isEmpty()){
                Intent intent = new Intent(MainActivity.this,Main4Activity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    assert data != null;
                    if (data.getStringExtra("data_return").equals("SUCCESS")){
                        initRecyclerView();
                    }
                }
        }
    }
}
