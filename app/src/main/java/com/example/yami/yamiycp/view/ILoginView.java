package com.example.yami.yamiycp.view;

import android.content.Context;

public interface ILoginView {
     Context getContext();
     void onLoginResult(Boolean result,int code);
     void onSetProgressBarVisibility(int visibility);
     void isRemember(boolean isRemember,String accountText,String passwordText);
}
