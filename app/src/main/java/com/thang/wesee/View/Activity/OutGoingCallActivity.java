package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.*;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.FCM.API;
import com.thang.wesee.FCM.APIServices;
import com.thang.wesee.Model.Data;
import com.thang.wesee.Model.Notification;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;
import com.wonderkiln.camerakit.CameraView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutGoingCallActivity  extends AppCompatActivity
         {
    private String invitetoken;
    private APIServices apiServices;
    private LottieAnimationView Imagecall;
    private String Room;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private  PreFerenceManager preFerenceManager;
    private UserController userController;
    private Intent intent1;
    private UserModel userModel;
    private CameraView cameraView;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_out);
        Imagecall=findViewById(R.id.cancel);
        cameraView=findViewById(R.id.camera);

       if(Build.VERSION.SDK_INT>=23){
           if(checkSelfPermission(Manifest.permission.CAMERA)
                   != PackageManager.PERMISSION_GRANTED){
               requestPermissions(new String[]{Manifest.permission.CAMERA},999);
           }else {
                   CameraViews();
           }
       }else{

       }
        Intent intent=getIntent();
        LocalBroadcastManager.getInstance(this).registerReceiver(getMessage,new IntentFilter("RESPONSIVE"));
        userController=new UserController(this);
        userModel= (UserModel) intent.getSerializableExtra("User");
        apiServices=API.getCilient().create(APIServices.class);
        preFerenceManager=new PreFerenceManager(this);
        Room= "ROOM"+ FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Data data=new Data("Calling","Invite",preFerenceManager.getToken(),Room,preFerenceManager.getLa(),
                preFerenceManager.getLo(),email);
        Notification notification=new Notification(data,userModel.getFCM_TOKEN());
        apiServices.sendRemoteMesage(notification)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.toString()!=null){

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
        mediaPlayer=MediaPlayer.create(this,R.raw.waitcall);
        mediaPlayer.start();
        Imagecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CancleCall();
                finish();
            }
        });
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                cameraView.stop();
                CancleCall();
                finish();

            }
        },15000);

    }

             private void CameraViews() {
        cameraView.start();

             }

             private  void CancleCall(){
        Data data=new Data("RESPONSIVE","CANCLE",preFerenceManager.getToken(),Room,"1","2",
                FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Notification notification=new Notification(data,userModel.getFCM_TOKEN());
        apiServices=API.getCilient().create(APIServices.class);
        apiServices.sendRemoteMesage(notification).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        userController.UpdateCall("ACTIVE",FirebaseAuth.getInstance().getCurrentUser().getEmail());

        finish();
    }
    private BroadcastReceiver getMessage=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String res=intent.getStringExtra("RESPONSIVE");
            if(res.equalsIgnoreCase("Cancel")){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                finish();
            }
            else  if(res.equalsIgnoreCase("Acept")) {
                intent1 = new Intent(OutGoingCallActivity.this, VideoCallActivity.class);
                intent1.putExtra("ROOM", Room);
                intent1.putExtra("VIDEO", "O");
                cameraView.stop();
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                startActivity(intent1);
                finish();

            }else if(res.equalsIgnoreCase("CANCLE")){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                finish();
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getMessage);
    }


}
