package com.example.yami.yamiycp.presenter;

import com.example.yami.yamiycp.Service.ClientServer;
import com.example.yami.yamiycp.Utils.ApplicationUtil;
import com.example.yami.yamiycp.view.ILoginView;

import okhttp3.OkHttpClient;

public class LoginPresenter implements ILoginPresenter {

    private ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void doLogin(String account, String password) {
        if (account.isEmpty() || password.isEmpty()){
            loginView.onLoginResult(false,0);
            return;
        }
        final String oldAccount = ApplicationUtil.getAccount(loginView.getContext());
        final String oldPassword = ApplicationUtil.getPassword(loginView.getContext());
        if (!oldAccount.equals(account)){
            ClientServer.getInstance().ReLogin();
        }
        ApplicationUtil.setAccount(loginView.getContext(),account);
        ApplicationUtil.setPassword(loginView.getContext(),password);
        ClientServer server = ClientServer.getInstance();
        server.getClient(loginView.getContext(), new ClientServer.onClientListener() {
            @Override
            public void respond(OkHttpClient client, int resultCode) {
                if (resultCode == 1){
                    loginView.onLoginResult(true,resultCode);
                }else {
                    ApplicationUtil.setAccount(loginView.getContext(),oldAccount);
                    ApplicationUtil.setPassword(loginView.getContext(),oldPassword);
                    ClientServer.getInstance().ReLogin();
                    loginView.onLoginResult(false,resultCode);
                }
            }
        });
    }

    @Override
    public void setProgressVisibility(int visibility) {

    }

    @Override
    public void setRemember(boolean isRemember) {
        ApplicationUtil.setIsRememberPassword(loginView.getContext(),isRemember);
    }

    @Override
    public void getRememberData() {
        String account;
        String password;
        if(ApplicationUtil.IsRememberPassword(loginView.getContext())){
            account = ApplicationUtil.getAccount(loginView.getContext());
            password = ApplicationUtil.getPassword(loginView.getContext());
            loginView.isRemember(true,account,password);
        }
    }
}
