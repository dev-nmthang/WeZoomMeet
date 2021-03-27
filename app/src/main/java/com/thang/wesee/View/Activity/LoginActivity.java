package com.thang.wesee.View.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;


public class LoginActivity  extends AppCompatActivity
implements  View.OnClickListener {
    private Button btnlogin;
    private EditText editemail,editpass;
    private TextView txtforgetpassword;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView txtsignup;
    private UserController userController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitWidget();
        Init();
        HandlerEvents();
    }

    private void HandlerEvents() {
        btnlogin.setOnClickListener(this);
        txtforgetpassword.setOnClickListener(this);
        txtsignup.setOnClickListener(this);

    }

    private void Init() {

        userController=new UserController(this);

         userController.HandleAutologin();


    }

    private void InitWidget() {
        btnlogin=findViewById(R.id.btnlogin);
        editemail=findViewById(R.id.editemail);
        editpass=findViewById(R.id.edtipass);
        txtforgetpassword=findViewById(R.id.txtforgetpass);
        txtsignup=findViewById(R.id.txtsignup);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin:
                String Email=editemail.getText().toString().trim();
                String Pass=editpass.getText().toString().trim();
                userController.HandleLoginAccount(Email,Pass);break;
            case R.id.txtsignup:
                startActivity(new Intent(this,SignUpActivity.class));break;


        }
    }


}
