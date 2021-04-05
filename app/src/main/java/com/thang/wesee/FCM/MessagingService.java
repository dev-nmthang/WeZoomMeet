package com.thang.wesee.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;
import com.thang.wesee.View.Activity.InComingCallActivity;


public class MessagingService  extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }
    private String token;


    @Override
    public void onMessageReceived(@NonNull  RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String res=remoteMessage.getData().get("title");
        String type=remoteMessage.getData().get("message");
        String room=remoteMessage.getData().get("room");
        String la=remoteMessage.getData().get("la");
        String lo=remoteMessage.getData().get("lo");
        token=remoteMessage.getData().get("token");

        UserController userController=new UserController(this);
        FirebaseApp.initializeApp(this);

        if(type!=null){

            if(type.equalsIgnoreCase("invite")){
                userController.UpdateCall("Ringing");



                Intent intent=new Intent(getApplicationContext(), InComingCallActivity.class);
                intent.putExtra("TYPE", type);
                intent.putExtra("TOKEN",token);
                intent.putExtra("ROOM",room);
                intent.putExtra("LA",la);
                intent.putExtra("LO",lo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(res.equalsIgnoreCase("RESPONSIVE")){

                Intent intent=new Intent("RESPONSIVE");
                intent.putExtra("RESPONSIVE",type);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            else if(res.equalsIgnoreCase("ACTION")){

                Intent intent=new Intent("ACTION");
                intent.putExtra("ACTION",type);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            else if(res.equalsIgnoreCase("STATUS")){

                Intent intent=new Intent("STATUS");
                intent.putExtra("STATUS",type);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

        }


    }
}
