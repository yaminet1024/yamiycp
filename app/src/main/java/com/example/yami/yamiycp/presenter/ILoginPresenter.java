package com.example.yami.yamiycp.presenter;

public interface ILoginPresenter{
    void doLogin(String account,String password);
    void setProgressVisibility(int visibility);
    void setRemember(boolean isRemember);
    void getRememberData();
}
