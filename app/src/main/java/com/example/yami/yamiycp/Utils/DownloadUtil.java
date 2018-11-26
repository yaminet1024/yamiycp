package com.example.yami.yamiycp.Utils;

import android.os.AsyncTask;


/**
 * Params:启动任务时输入的参数类型
 * Progress:后台任务执行中返回进度值的类型
 * Result:后台任务执行完成后返回结果的类型
 */

public class DownloadUtil extends AsyncTask<Integer,Integer,Integer> {

    public DownloadUtil() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        return null;
    }
}
