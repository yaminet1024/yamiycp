package com.example.yami.yamiycp;

import android.content.Context;
import android.util.Log;

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
import okhttp3.ResponseBody;

import static android.support.constraint.Constraints.TAG;

public class YuyueService {

    private Context context;
    private String codeId;
    private String date;

    public YuyueService(Context context,String condeId,String time){
        this.context = context;
        this.codeId = condeId;
        this.date = time;
    }

    public interface OnListListener{
        void onReponse(List<String> data);
    }

    public  void getYuyueList(final OnListListener onListListener){
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
                .url("http://csnfjx.youside.cn/webphone/ajax/StuLogin.ashx?t=0.6502667345405999&loginname=" + ApplicationUtil.getAccount(context) + "&loginpwd= " + ApplicationUtil.getPassword(context) + "&log=")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Request request1 = new Request.Builder()
                        .url("http://csnfjx.youside.cn/webphone/ajax/PhoneGetyuechexinxi.ashx?codeid=" + codeId + "&xzrq=" + date)
                        .build();
                Response response1 = okHttpClient.newCall(request1).execute();
                List<String> data = new ArrayList<>();
                Document doc = Jsoup.parse(response1.body().string());
                Element element = doc.body();
                Elements elements = element.getElementsByClass("yue_r1");
                for (Element link : elements){
                    data.add(link.text());
                }
                onListListener.onReponse(data);
            }
        });
    }




}
