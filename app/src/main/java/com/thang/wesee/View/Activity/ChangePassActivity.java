package com.thang.wesee.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;

public class ChangePassActivity  extends AppCompatActivity
 {
    private EditText editpass,editpass1;
    private Button btnsave;
    private Toolbar toolbar;

    private UserController userController;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        toolbar=findViewById(R.id.toolbar);
        editpass1=findViewById(R.id.editpass1);
        editpass=findViewById(R.id.editpass);
        btnsave=findViewById(R.id.btnsave);
        Init();

    }

    private void Init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userController=new UserController(this);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=editpass.getText().toString().trim();
                String pass1=editpass1.getText().toString().trim();
                if(pass.length()>6 && pass1.length()>6){
                    if(pass.equals(pass1)){
                        userController.HandleChangePass(pass);
                    }else{
                        Toast.makeText(ChangePassActivity.this, "Password not same", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangePassActivity.this,"password > 6 character",Toast.LENGTH_LONG).show();

                }

            }
        });
    }


}
