package com.thang.wesee.FCM;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thang.wesee.Controller.UserController;
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
        String email=remoteMessage.getData().get("email");
        UserController userController=new UserController(this);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        userController.UpdateCall("Ringing",firebaseUser.getEmail());
        if(type!=null){
            if(type.equalsIgnoreCase("invite")){
                Intent intent=new Intent(getApplicationContext(), InComingCallActivity.class);
                intent.putExtra("TYPE", type);
                intent.putExtra("TOKEN",token);
                intent.putExtra("ROOM",room);
                intent.putExtra("LA",la);
                intent.putExtra("LO",lo);
                intent.putExtra("EMAIL",email);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(res.equalsIgnoreCase("RESPONSIVE")){

                Intent intent=new Intent("RESPONSIVE");
                intent.putExtra("RESPONSIVE",type);
                intent.putExtra("EMAIL",email);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
            else if(res.equalsIgnoreCase("ACTION")){

                Intent intent=new Intent("ACTION");
                intent.putExtra("ACTION",type);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }

        }


    }
}
