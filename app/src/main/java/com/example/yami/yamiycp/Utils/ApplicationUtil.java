package com.example.yami.yamiycp.Utils;

import android.content.Context;

public class ApplicationUtil {
    private static boolean rememberPassword;
    private static String account;
    private static String password;
    private static String Name;
    private static String ReportData;
    private static String IdCard;
    private static String Phone;
    private static String LearnProject;
    private static String RemainProject;
    private static String learning;
    private static String carStyle;

    public static String getCarStyle(Context context) {
        carStyle = SPHelper.getInstant(context).getStringFromSP(context,"CARSTYLE");
        return carStyle;
    }

    public static void setCarStyle(Context context,String carStyle) {
        SPHelper.getInstant(context).putData2SP(context,"CARSTYLE",carStyle);
    }

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



    //

    public static String getName(Context context) {
        Name = SPHelper.getInstant(context).getStringFromSP(context,"NAME");
        return Name;
    }

    public static void setName(Context context,String name) {
        SPHelper.getInstant(context).putData2SP(context,"NAME",name);
    }

    public static String getReportData(Context context) {
        ReportData = SPHelper.getInstant(context).getStringFromSP(context,"REPORTDATA");
        return ReportData;
    }

    public static void setReportData(Context context,String reportData) {
        SPHelper.getInstant(context).putData2SP(context,"REPORTDATA",reportData);
    }

    public static String getIdCard(Context context) {
        IdCard = SPHelper.getInstant(context).getStringFromSP(context,"IDCARD");
        return IdCard;
    }

    public static void setIdCard(Context context,String idCard) {
        SPHelper.getInstant(context).putData2SP(context,"IDCARD",idCard);
    }

    public static String getPhone(Context context) {
        Phone = SPHelper.getInstant(context).getStringFromSP(context,"PHONE");
        return Phone;
    }

    public static void setPhone(Context context,String phone) {
        SPHelper.getInstant(context).putData2SP(context,"PHONE",phone);
    }

    public static String getLearnProject(Context context) {
        LearnProject = SPHelper.getInstant(context).getStringFromSP(context,"LEARNPROJECT");
        return LearnProject;
    }

    public static void setLearnProject(Context context,String learnProject) {
        SPHelper.getInstant(context).putData2SP(context,"LEARNPROJECT",learnProject);
    }

    public static String getRemainProject(Context context) {
        RemainProject = SPHelper.getInstant(context).getStringFromSP(context,"REMAINPROJECT");
        return RemainProject;
    }

    public static void setRemainProject(Context context,String remainProject) {
        SPHelper.getInstant(context).putData2SP(context,"REMAINPROJECT",remainProject);
    }

    public static String getLearning(Context context) {
        learning = SPHelper.getInstant(context).getStringFromSP(context,"LEARNING");
        return learning;
    }

    public static void setLearning(Context context,String learning) {
        if (learning.equals("科目一")){
            learning = "科目二";
        }else if (learning.equals("科目二")){
            learning = "科目三";
        }
        SPHelper.getInstant(context).putData2SP(context,"LEARNING",learning);
    }
}
