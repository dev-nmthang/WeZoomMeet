package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;

import com.thang.wesee.R;
import org.jetbrains.annotations.NotNull;

public class HomeActivity  extends AppCompatActivity
 implements View.OnClickListener{
    private Button btnvolunteer,btndisable;
    private UserController userController;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private CountDownTimer countDownTimer;
    private PreFerenceManager preFerenceManager;
    private static final int RC_LOCATION = 1111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitWidget();
        Init();
        HandleEvents();
    }

    private void HandleEvents() {
        btndisable.setOnClickListener(this);
        btnvolunteer.setOnClickListener(this);
    }

    private void Init() {
        preFerenceManager=new PreFerenceManager(this);
        GetLocation();
        userController=new UserController(this);
        preFerenceManager=new PreFerenceManager(this);




        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                preFerenceManager.putKeyToken(task.getResult());
                userController.SaveInfo(HomeActivity.this,task.getResult());
            }
        });


    }

    private void InitWidget() {
        btnvolunteer=findViewById(R.id.btnvolunteer);
        btndisable=findViewById(R.id.btndiasabled);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnvolunteer:
                userController.HandleUpdateTypePerson("VOLUNTEER");
                startActivity(new Intent(HomeActivity.this, WaitingActivity.class));break;
            case R.id.btndiasabled: startActivity(new Intent(HomeActivity.this,SupportPersionActivity.class));
                 userController.HandleUpdateTypePerson("DISABLE");
                break;
        }
    }
    private void GetLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        countDownTimer=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(!CheckPermisonLocation()){
                    RequestPermissonLocation();
                    countDownTimer.cancel();
                    countDownTimer.start();

                }else{
                    getsLastPositon();
                }

            }
        }.start();

    }


    private void getsLastPositon() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                double    latitude=location.getLatitude();
                  double  longitude=location.getLongitude();
                   preFerenceManager.PutXAndY(String.valueOf(longitude),String.valueOf(latitude));


                }else{
                    preFerenceManager.PutXAndY(String.valueOf(15),String.valueOf(20));
                }
            }
        });
    }
    private void RequestPermissonLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RC_LOCATION);
    }


    private boolean CheckPermisonLocation() {
        boolean resultlocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        return  resultlocation;
    }


}
