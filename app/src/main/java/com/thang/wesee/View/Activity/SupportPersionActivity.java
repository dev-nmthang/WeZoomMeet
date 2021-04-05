package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SupportPersionActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        LocationListener {
    private Toolbar toolbar;
    private RoomAdapter roomAdapter;
    private UserController userController;
    private AppCompatSpinner spinnerMuc;
    private String[] Fodlers = {"Select ", "ROOM RANDOM", "SELECT ROOM"};
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
        PerMisSon();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        }

        preFerenceManager = new PreFerenceManager(SupportPersionActivity.this);

        userController = new UserController(SupportPersionActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                preFerenceManager.putKeyToken(task.getResult());
                userController.SaveInfo(SupportPersionActivity.this, task.getResult());
            }
        });
        fm = new FragMent_Home_Disable();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }



    private void InitWidget() {
        bottomNavigationView = findViewById(R.id.bottomnavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                fm = new FragMent_Home_Disable();
                break;
            case R.id.settings:
                fm = new FragMent_Settings();
                break;


        }
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fm).commit();
        return true;
    }

    private void GetLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                RequestPermissonLocation();
            } else {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
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
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1111) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            }
        }
    }

    private void RequestPermissonLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RC_LOCATION);
    }




    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("CHECKED",location.getLatitude()+" - "+location.getLongitude());
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
    private void PerMisSon() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if( !Settings.canDrawOverlays(this))
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+ getPackageName()));
                startActivityForResult(intent,9240);
            }

        }

    }

}
