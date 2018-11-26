package com.example.yami.yamiycp.Service;

import android.content.Context;

import com.example.yami.yamiycp.Utils.ApplicationUtil;

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

public class ClientServer {
    private  static OkHttpClient client;
    private  Context context;
    private static volatile ClientServer clientServer;
    private int LOGIN_OK = 1;
    private int LOGIN_FALSE = 0;

    public static ClientServer getInstance(){
        if (clientServer == null){
            synchronized (ClientServer.class){
                if (clientServer == null){
                    clientServer = new ClientServer();
                    return clientServer;
                }
            }
        }
        return clientServer;
    }

    public void ReLogin(){
        client = null;
    }

    public void getClient(Context context,final onClientListener listener){
        this.context = context;
        if (client!=null){
            listener.respond(client,1);
        }else {
            initClient(new onClientListener() {
                @Override
                public void respond(OkHttpClient client, int resultCode) {
                    listener.respond(client,resultCode);
                }
            });
        }
    }



    private ClientServer() {
    }

    private void initClient(final onClientListener listener) {
        client = null;
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
                ClientServer.client = okHttpClient;
                assert response.body() != null;
                if (response.body().string().equals("goto")){
                    ApplicationUtil.setAccount(context,ApplicationUtil.getAccount(context));
                    ApplicationUtil.setPassword(context,ApplicationUtil.getPassword(context));
                    ApplicationUtil.setName(context,"");
                    listener.respond(client,LOGIN_OK);
                }else {
                    listener.respond(client,LOGIN_FALSE);
                }
            }
        });
    }

    public interface onClientListener{
        void respond(OkHttpClient client,int resultCode);
    }
}
