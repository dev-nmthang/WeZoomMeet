package com.thang.wesee.FCM;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {
    private static Retrofit retrofit=null;
    public  static Retrofit getCilient(){

       if(retrofit==null){
           retrofit=new Retrofit.Builder()
                   .baseUrl("https://fcm.googleapis.com/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
       }
       return  retrofit;

    }



}
