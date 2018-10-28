package com.example.yami.yamiycp.Utils;

import android.content.Context;

public class ApplicationUtil {
    private static boolean rememberPassword;
    private static String account;
    private static String password;
    private static String cookies;

    public static boolean IsRememberPassword(Context context) {
        rememberPassword = SPHelper.getInstant(context).getBooleanFromSP(context,"REMEMBER_PASSWORD");
        return rememberPassword;
    }

    public static void setIsRememberPassword(Context context,boolean isRememberPassword) {
        SPHelper.getInstant(context).putData2SP(context,"REMEMBER_PASSWORD",isRememberPassword);
    }

    public static String getAccount(Context context) {
        account = SPHelper.getInstant(context).getStringFromSP(context,"ACCOUNT");
        return account;
    }

    public static void setAccount(Context context,String account) {
        SPHelper.getInstant(context).putData2SP(context,"ACCOUNT",account);
    }

    public static String getPassword(Context context) {
        password = SPHelper.getInstant(context).getStringFromSP(context,"PASSWORD");
        return password;
    }

    public static void setPassword(Context context,String password) {
        SPHelper.getInstant(context).putData2SP(context,"PASSWORD",password);
    }

    public static String getCookies(Context context) {
        cookies = SPHelper.getInstant(context).getStringFromSP(context,"COOKIES");
        return cookies;
    }

    public static void setCookies(Context context,String cookies) {
        SPHelper.getInstant(context).putData2SP(context,"COOKIES",cookies);
    }
}
