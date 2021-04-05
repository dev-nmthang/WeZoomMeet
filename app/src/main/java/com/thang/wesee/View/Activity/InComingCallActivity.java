package com.thang.wesee.View.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.react.modules.core.PermissionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.FCM.API;
import com.thang.wesee.FCM.APIServices;
import com.thang.wesee.Model.Data;
import com.thang.wesee.Model.Notification;
import com.thang.wesee.R;
import com.wonderkiln.camerakit.CameraView;

import org.jitsi.meet.sdk.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InComingCallActivity extends AppCompatActivity
        implements View.OnClickListener,JitsiMeetActivityInterface {

    private JitsiMeetConferenceOptions options;

    private LottieAnimationView ImageCanel,ImageAceept;
    private String token;
    private APIServices apiServices;
    private String Room;
    private UserController userController;
    private String lo,la,email,uid;
    private PreFerenceManager preFerenceManager;
    private MediaPlayer mediaPlayer;
    private CameraView cameraView;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_volunteer);
        InitWidget();
        Init();
        HandleEvents();




    }

    private void HandleEvents() {
       mediaPlayer=MediaPlayer.create(this,R.raw.medicall);
        mediaPlayer.start();

        ImageCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CancleCall();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CancleCall();

            }
        },15000);

        ImageAceept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data=new Data("RESPONSIVE","Acept",preFerenceManager.getToken(),Room,la,lo);
                Notification notification=new Notification(data,token);
                apiServices=API.getCilient().create(APIServices.class);
                apiServices.sendRemoteMesage(notification).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response!=null){
                            Toast.makeText(InComingCallActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
                Intent intent=new Intent(InComingCallActivity.this,VideoCallActivity.class);
                intent.putExtra("ROOM",Room);
                intent.putExtra("VIDEO","K");
                intent.putExtra("TOKEN",token);
                userController.UpdateCall("BUSY");
                startActivity(intent);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                cameraView.stop();
                finish();

            }



        });
    }
    private  void CancleCall(){
        Data data=new Data("RESPONSIVE","CANCLE",preFerenceManager.getToken(),Room,preFerenceManager.getLa(),preFerenceManager.getLo());
        Notification notification=new Notification(data,token);
        apiServices=API.getCilient().create(APIServices.class);
        apiServices.sendRemoteMesage(notification).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        cameraView.stop();
        userController.UpdateCall("active");
        finish();
    }


    private void Init() {
          cameraView.start();
        LocalBroadcastManager.getInstance(this).registerReceiver(getMessage,new IntentFilter("RESPONSIVE"));
        preFerenceManager=new PreFerenceManager(this);
        userController=new UserController(this);

        Intent intent=getIntent();
        token=intent.getStringExtra("TOKEN");
        Room=intent.getStringExtra("ROOM");
        lo=intent.getStringExtra("LO");
        la=intent.getStringExtra("LA");
        uid=intent.getStringExtra("uid");
        preFerenceManager.PutXAndY(lo,la);


       // Toast.makeText(this, " "+token, Toast.LENGTH_SHORT).show();


    }
    private BroadcastReceiver getMessage=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String res=intent.getStringExtra("RESPONSIVE");
            if(res.equalsIgnoreCase("CANCLE")){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                userController.UpdateCall("active");
                cameraView.stop();
                finish();
            }


        }
    };

    private void InitWidget() {
        cameraView=findViewById(R.id.camera);
        ImageCanel=findViewById(R.id.cancel);
        ImageAceept=findViewById(R.id.apcept);
    }

    @Override
    public void onClick(View v) {


    }


    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMessage);
    }

    @Override
    public void onBackPressed() {

    }
}
