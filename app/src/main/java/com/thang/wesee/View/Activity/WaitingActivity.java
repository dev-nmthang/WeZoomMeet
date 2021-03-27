package com.thang.wesee.View.Activity;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thang.wesee.Adapter.RoomAdapter;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;
import com.thang.wesee.View.FragMent.FragMent_Home_Volunteer;
import com.thang.wesee.View.FragMent.FragMent_Settings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_persion);
        InitWidget();
        inIt();
        preFerenceManager=new PreFerenceManager(WaitingActivity.this);

        userController=new UserController(WaitingActivity.this);
        userController.SaveInfo(WaitingActivity.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                     if(task.isSuccessful()){
                         preFerenceManager.putKeyToken(task.getResult());
                         Toast.makeText(WaitingActivity.this,preFerenceManager.getToken(),Toast.LENGTH_LONG).show();
                         new Handler().postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 userController.UpdateFCMTOKEN(WaitingActivity.this,preFerenceManager.getToken());

                             }
                         },1000);
                     }
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
}
