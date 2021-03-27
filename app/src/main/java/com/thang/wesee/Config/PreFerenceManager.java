package com.thang.wesee.Config;

import android.content.Context;
import android.content.SharedPreferences;

public class PreFerenceManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public PreFerenceManager(Context context){
      sharedPreferences= context.getSharedPreferences("INFO",Context.MODE_PRIVATE);
      editor=sharedPreferences.edit();
    }
    public void putKeyValue(String key){
        editor.putString("USERID",key);
        editor.commit();

    }
    public void putKeyToken(String token){
        editor.putString("TOKEN",token);
        editor.commit();

    }
    public String getToken(){
        String token=sharedPreferences.getString("TOKEN","");
        return  token;
    }
    public  String getID(){
        String UserID=sharedPreferences.getString("USERID","");
        return  UserID;
    }
    public void PutXAndY(String latitude,String longtitude){
        editor.putString("LA",latitude);
        editor.putString("LO",longtitude);
        editor.commit();
    }
    public  String getLa(){
        String La=sharedPreferences.getString("LA","");
        return  La;
    }
    public String getLo(){
        String Lo=sharedPreferences.getString("LO","");
        return Lo;
    }



}
