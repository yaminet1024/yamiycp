package com.example.yami.yamiycp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import com.example.yami.yamiycp.Adapters.IndexListAdapter;
import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.model.Teacher;
import com.example.yami.yamiycp.model.UpdateMessage;
import com.example.yami.yamiycp.Utils.ApplicationUtil;
import com.example.yami.yamiycp.view.LoginActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
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

public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "IndexActivity";
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
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://193.112.120.245/static/yamiycp/update.xml")
                .build();
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert info != null;
        final Float version = Float.valueOf(info.versionName);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<UpdateMessage> updateMessageList = parseXMLWithPull(client.newCall(request).execute().body().string());
                    Log.d(TAG, "run: " + updateMessageList.get(0).getVersion() + "  new" + version);
                    if (Float.valueOf(updateMessageList.get(0).getVersion()) > version ){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                                builder.setTitle("有新版本可用，是否下载？");
                                builder.setMessage(updateMessageList.get(1).getMessage().replaceAll("@","\n"));
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Uri uri = Uri.parse(updateMessageList.get(2).getMessage());
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout linearLayout = (LinearLayout) navigationView.getHeaderView(0);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this,LoginActivity.class);
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
                textView.setText(ApplicationUtil.getName(IndexActivity.this)+ ",您好");
                textView1.setText("当前的学习进度为：" + ApplicationUtil.getLearning(IndexActivity.this));
                ImageView imageView = linearLayout.findViewById( R.id.imageView);
                Glide.with(IndexActivity.this)
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
                Intent intent = new Intent(IndexActivity.this,RecodeActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_gallery) {
            if (!ApplicationUtil.getAccount(this).isEmpty()){
                Intent intent = new Intent(IndexActivity.this,Trained.class);
                intent.putExtra("style",2);
                startActivity(intent);
            }

        } else if (id == R.id.nav_slideshow) {
            if (!ApplicationUtil.getAccount(this).isEmpty()){
                Intent intent = new Intent(IndexActivity.this,Trained.class);
                intent.putExtra("style",3);
                startActivity(intent);
            }

        } else if (id == R.id.nav_manage) {
            if (!ApplicationUtil.getAccount(this).isEmpty()){
                Intent intent = new Intent(IndexActivity.this,ERCodeActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            checkUpdate();
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

    private List<UpdateMessage> parseXMLWithPull(String responseData) {
        List<UpdateMessage> updateMessages = new ArrayList<>();
        try{
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(responseData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            String message = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG :
                        if ("id".equals(nodeName)){
                            id = xmlPullParser.nextText();
                        }else if ("name".equals(nodeName)){
                            name = xmlPullParser.nextText();
                        }else if ("version".equals(nodeName)){
                            version = xmlPullParser.nextText();
                        }else if ("message".equals(nodeName)){
                            message = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)){
                            UpdateMessage updateMessage = new UpdateMessage();
                            updateMessage.setId(id);
                            updateMessage.setName(name);
                            updateMessage.setMessage(message);
                            updateMessage.setVersion(version);
                            updateMessages.add(updateMessage);
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return updateMessages;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return updateMessages;
        }
    }
}
