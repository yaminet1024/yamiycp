package com.example.yami.yamiycp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPHelper {
    private String defaultModelName;

    private static SPHelper spHelperInstant;

    private SPHelper(){

    }

    public static  SPHelper getInstant(Context context){
        if (spHelperInstant == null){
            synchronized (SPHelper.class){
                if (spHelperInstant == null){
                    spHelperInstant = new SPHelper();
                    spHelperInstant.defaultModelName = "yamiycp";
                }
            }
        }
        return spHelperInstant;
    }

    public void putData2SP(Context context, String modelName, String key, Object value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(modelName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Boolean){
            editor.putBoolean(key,(Boolean)value);
        } else if (value instanceof Integer){
            editor.putInt(key, (Integer)value);
        } else if (value instanceof String){
            editor.putString(key, (String)value);
        } else if (value instanceof Long){
            editor.putLong(key, (Long)value);
        } else if (value instanceof Float){
            editor.putFloat(key, (Float)value);
        }else {
            return;
        }
        editor.apply();
    }

    public void putData2SP(Context context, String key, Object value){
        putData2SP(context,defaultModelName,key,value);
    }

    public String getStringFromSP(Context context,String key){
        return getStringFromSP(context,defaultModelName,key);
    }

    public Boolean getBooleanFromSP(Context context, String key){
        return getBooleanFromSP(context,defaultModelName,key);
    }

    public String getStringFromSP(Context context, String defaultModelName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(defaultModelName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public Boolean getBooleanFromSP(Context context, String defaultModelName, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(defaultModelName,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
