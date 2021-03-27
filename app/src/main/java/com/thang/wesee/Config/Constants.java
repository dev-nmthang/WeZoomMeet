package com.thang.wesee.Config;

import java.util.HashMap;

public class Constants {

    public  static  final String KEY_FCM_TOKEN="FCM_TOKEN";
    public  static  final String SG_Authorization ="Authorization";
    public  static  final String SG_Content_Type ="Content-Type";
    public  static  final String REMOTE_MSG_TYPE="type";
    public static  final String REMOTE_MSG_INVITATION="INVITATION";
    public  static final String REMOTE_MEETING_TYPE="meetingtype";
    public static  final String REMOTE_MSG_INVITER_TOKEN="intivetoken";
    public  static  final String REMOTE_MSG_DATA="DATA";
    public  static  final String REMOTE_REGISTATION="REGIST";
    public  static HashMap<String,String> getRemoteMessage(){
        HashMap<String,String> headers=new HashMap<>();
        headers.put(SG_Authorization,"AAAA5vQpgYY:APA91bEYX9HYdp_q7oTPXXwI-2qRYHYS2lvyBe6dw95U5RwCz0hY6DySde0TAGdKN2myDB2RmF6SRoT-hc_EalAFeALm3zj9B0417g4tPrWToQB17IOowzZ-FZhzOOJZroKfgsscE9XM");
        headers.put(SG_Content_Type,"application/json");
        return  headers;
    }

}
