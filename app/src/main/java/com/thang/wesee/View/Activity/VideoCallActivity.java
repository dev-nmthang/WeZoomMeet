package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.react.modules.core.PermissionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.FCM.API;
import com.thang.wesee.FCM.APIServices;

import org.jitsi.meet.sdk.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class VideoCallActivity  extends AppCompatActivity
        implements JitsiMeetViewListener,JitsiMeetActivityInterface{
    private Intent intent;
    private JitsiMeetConferenceOptions options1;
    private UserController userController;
    private String video;
    private PreFerenceManager preFerenceManager;
    private ImageView cancel;
    private TextView txtgoogle;
    private String la,lo;
    private CardView c1,c2;
    private JitsiMeetView view;
    private APIServices apiServices;
    private String token;
    private String email;
    private String Room;
    private  String VIDEO_MUTE_BUTTON_ENABLED = "video-mute.enabled";
    // pin yếu -> share gps



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiServices=API.getCilient().create(APIServices.class);


       view=new JitsiMeetView(this);
        intent=getIntent();
        preFerenceManager=new PreFerenceManager(this);
        userController=new UserController(this);
        Room=intent.getStringExtra("ROOM");
        token=intent.getStringExtra("TOKEN");
        video=intent.getStringExtra("VIDEO");
        userController.HandleUpdateStatus(Room,"BUSY");


        try {
            URL url=new URL("https://meet.jit.si/");
            JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url)
                    .setWelcomePageEnabled(false)
                    .build();


              JitsiMeet.setDefaultConferenceOptions(options);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(video.equalsIgnoreCase("O")){
            options1=new JitsiMeetConferenceOptions
                    .Builder().setRoom(Room)
                    .setVideoMuted(false)
                    .setAudioMuted(false)
                    .build();


        }else if(video.equalsIgnoreCase("K")){
            options1=new JitsiMeetConferenceOptions
                    .Builder().setRoom(Room)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setAudioMuted(false)
                    .build();

        }
        PermissonVIew();
        Handler handler=new Handler();

        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equalsIgnoreCase("cancle")){
                    userController.UpdateCall("active",FirebaseAuth.getInstance().getCurrentUser().getEmail());

                    Showfinish();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        FirebaseDatabase.getInstance().getReference()

                .child(Room).addValueEventListener(valueEventListener);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().getReference()

                        .child(Room).addValueEventListener(valueEventListener);
                handler.postDelayed(this,100);
            }
        },100);
    }

    private void PermissonVIew() {
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED){
         requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},123);
        }else{

            view.join(options1);
            view.setListener(this);
            setContentView(view);
        }

    }

    private void Showfinish() {
        this.finish();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        userController.HandleUpdateStatus(Room,"cancle");
        Log.d("HERE","DESTROY");
        if(video.equalsIgnoreCase("K")){
            startActivity(new Intent(this,GoogleActivity.class));
                 this.finish();

        }else{
            this.finish();
        }
 if(view!=null){
     view.leave();
 }
 JitsiMeetActivityDelegate.onHostDestroy(this);

    }




    @Override
    public void onConferenceJoined(Map<String, Object> map) {

    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map) {

             finish();
    }

    @Override
    public void onConferenceWillJoin(Map<String, Object> map) {

    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }
}
