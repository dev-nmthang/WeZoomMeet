package com.thang.wesee.View.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thang.wesee.Adapter.RoomAdapter;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;
import com.thang.wesee.View.FragMent.FragMent_Home_Volunteer;
import com.thang.wesee.View.FragMent.FragMent_Settings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WaitingActivity extends AppCompatActivity
        implements  BottomNavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private RoomAdapter roomAdapter;
    private UserController userController;
    private AppCompatSpinner spinnerMuc;
    private String[] Fodlers={"Select ","ROOM RANDOM","SELECT ROOM"};

    private GridView gv;
    private ArrayAdapter adapter;
    private CardView c1;
    private ArrayList<UserModel> userModels;
    private BottomNavigationView bottomNavigationView;
    private Fragment fm;
    private PreFerenceManager preFerenceManager;
    private String[] camerarequest;
    private String [] AudioRequest;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_persion);
        InitWidget();
        inIt();
        camerarequest=new String[]{Manifest.permission.CAMERA};
        AudioRequest=new String[]{Manifest.permission.RECORD_AUDIO};
        preFerenceManager=new PreFerenceManager(WaitingActivity.this);
            ShowDiaLogPermission();
            PerMisSon();
        userController=new UserController(WaitingActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                preFerenceManager.putKeyToken(task.getResult());
                userController.SaveInfo(WaitingActivity.this,task.getResult());
            }
        });



    }

    private void inIt() {
        fm=new FragMent_Home_Volunteer();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }



    private void InitWidget() {
        bottomNavigationView=findViewById(R.id.bottomnavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:fm=new FragMent_Home_Volunteer();break;
            case R.id.settings:fm=new FragMent_Settings();break;


        }
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fm).commit();
        return true;
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
    private  void ShowDiaLogPermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).onSameThread().check();

    }



}
