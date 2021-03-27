package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thang.wesee.Adapter.RoomAdapter;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;
import com.thang.wesee.View.FragMent.FragMent_Home_Disable;
import com.thang.wesee.View.FragMent.FragMent_Settings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SupportPersionActivity  extends AppCompatActivity
implements  BottomNavigationView.OnNavigationItemSelectedListener ,
        LocationListener {
    private Toolbar toolbar;
    private RoomAdapter roomAdapter;
    private UserController userController;
    private AppCompatSpinner spinnerMuc;
    private String[] Fodlers={"Select ","ROOM RANDOM","SELECT ROOM"};
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GridView gv;
    private ArrayAdapter adapter;
    private CardView c1;
    private ArrayList<UserModel> userModels;
    private BottomNavigationView bottomNavigationView;
    private Fragment fm;
    private PreFerenceManager preFerenceManager;
    private CountDownTimer countDownTimer;
    private static final int RC_LOCATION = 1111;
    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_persion);
        InitWidget();
        inIt();

    }

    private void inIt() {
        GetLocation();
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);




        preFerenceManager=new PreFerenceManager(SupportPersionActivity.this);

        userController=new UserController(SupportPersionActivity.this);
        userController.SaveInfo(SupportPersionActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(task.isSuccessful()){
                    if(preFerenceManager.getToken().equalsIgnoreCase(task.getResult())){

                    }else{
                        preFerenceManager.putKeyToken(task.getResult());
                        Toast.makeText(SupportPersionActivity.this,preFerenceManager.getToken(),Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                userController.UpdateFCMTOKEN(SupportPersionActivity.this,preFerenceManager.getToken());

                            }
                        },1000);
                    }

                }
            }
        });
        fm=new FragMent_Home_Disable();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }


    private void InitWidget() {
        bottomNavigationView=findViewById(R.id.bottomnavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:fm=new FragMent_Home_Disable();break;
            case R.id.settings:fm=new FragMent_Settings();break;


        }
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();
        return true;
    }
    private void GetLocation() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    !=PackageManager.PERMISSION_GRANTED){
                RequestPermissonLocation();
            }else{
                if(locationManager!=null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

                }
            }
        }else{
            if(locationManager!=null){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

            }
        }






    }

    private void RequestPermissonLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RC_LOCATION);
    }




    @Override
    public void onLocationChanged(@NonNull Location location) {
       preFerenceManager.PutXAndY(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


}
