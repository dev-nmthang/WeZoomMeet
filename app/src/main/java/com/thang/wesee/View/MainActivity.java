package com.thang.wesee.View;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;
import com.thang.wesee.View.Activity.LoginActivity;

public class MainActivity extends AppCompatActivity {
private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userController=new UserController(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                        userController.HandleAutologin();

                    }
                }else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }



            }
        },3000);
    }
}