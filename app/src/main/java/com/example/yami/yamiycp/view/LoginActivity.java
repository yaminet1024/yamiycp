package com.example.yami.yamiycp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.GenerateProcessButton;
import com.example.yami.yamiycp.R;
import com.example.yami.yamiycp.presenter.ILoginPresenter;
import com.example.yami.yamiycp.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private static final String TAG = "LoginActivity";

    private EditText account;
    private EditText password;
    ILoginPresenter presenter;
    CheckBox checkBox;
    private GenerateProcessButton generateProcessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        registerListening();
    }

    private void registerListening() {
        generateProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doLogin(account.getText().toString(),password.getText().toString());
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setRemember(isChecked);
            }
        });
    }

    private void initView() {
        //toolbar设置
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        //find view
        //是否记住密码
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        generateProcessButton = findViewById(R.id.login);
        //init
        presenter = new LoginPresenter(this);
        presenter.getRememberData();

    }


    @Override
    public Context getContext() {
        return LoginActivity.this.getApplicationContext();
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        if (result){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent();
            intent.putExtra("data_return","SUCCESS");
            setResult(RESULT_OK,intent);
            finish();
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {

    }

    @Override
    public void isRemember(boolean isRemember,String accountText, String passwordText) {
        checkBox.setChecked(isRemember);
        account.setText(accountText);
        password.setText(passwordText);
    }


}
