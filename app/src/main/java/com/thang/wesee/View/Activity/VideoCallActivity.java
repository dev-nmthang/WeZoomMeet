package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.thang.wesee.Model.Data;
import com.thang.wesee.Model.Notification;

import org.jitsi.meet.sdk.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String Room;
    private  String VIDEO_MUTE_BUTTON_ENABLED = "video-mute.enabled";
    private JitsiMeetUserInfo userInfo;



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiServices=API.getCilient().create(APIServices.class);


        view=new JitsiMeetView(this);
        userInfo=new JitsiMeetUserInfo();

        intent=getIntent();
        preFerenceManager=new PreFerenceManager(this);
        userController=new UserController(this);
        Room=intent.getStringExtra("ROOM");
        token=intent.getStringExtra("TOKEN");
        video=intent.getStringExtra("VIDEO");
        userController.UpdateCall("BUSY");
       

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
            userInfo.setDisplayName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            options1=new JitsiMeetConferenceOptions
                    .Builder().setRoom(Room)
                    .setVideoMuted(false)
                    .setUserInfo(userInfo)
                    .setAudioMuted(false)
                    .build();


        }else if(video.equalsIgnoreCase("K")){
            userInfo.setDisplayName(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            options1=new JitsiMeetConferenceOptions
                    .Builder().setRoom(Room)
                    .setVideoMuted(false)
                    .setAudioOnly(false)
                    .setAudioMuted(false)
                    .build();

        }
        PermissonVIew();




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




//thắng ơi k nghe à


    @Override
    public void onConferenceJoined(Map<String, Object> map) {

    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map) {


        Data data=new Data("STATUS","cancle",preFerenceManager.getToken(),Room,la,lo);
        Notification notification=new Notification(data,token);
        apiServices=API.getCilient().create(APIServices.class);
        apiServices.sendRemoteMesage(notification).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        finish();



    }


    @Override
    public void onConferenceWillJoin(Map<String, Object> map) {

    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }
    private BroadcastReceiver getMessage=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String res=intent.getStringExtra("STATUS");

            if(res.equalsIgnoreCase("cancle")){
                finish();

            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userController.UpdateCall("active");
        if(video.equalsIgnoreCase("K")){
            startActivity(new Intent(VideoCallActivity.this,GoogleActivity.class));
            this.finish();

        }
        if(view!=null){
            view.leave();
        }
        this.finish();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMessage);
        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(getMessage,new IntentFilter("STATUS"));
    }
}
