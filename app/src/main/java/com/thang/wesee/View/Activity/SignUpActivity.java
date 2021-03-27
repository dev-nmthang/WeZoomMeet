package com.thang.wesee.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;


public class SignUpActivity extends AppCompatActivity
implements  View.OnClickListener {
    private Toolbar toolbar;
    private EditText editemail,editpass,editrepeatpass;
    private Button btnsignup;
    private UserController userController;
    private PreFerenceManager preFerenceManager;
    private ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        InitWiget();
        Init();
        HandleEvents();
    }

    private void HandleEvents() {
        btnsignup.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void Init() {
        preFerenceManager=new PreFerenceManager(this);
        userController=new UserController(this);


    }

    private void InitWiget() {
        back=findViewById(R.id.back);
        btnsignup=findViewById(R.id.btnlogin);
        editemail=findViewById(R.id.editemail);
        editpass=findViewById(R.id.edtipass);
        editrepeatpass=findViewById(R.id.edtipass1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnlogin:
                String Email=editemail.getText().toString().trim();
                String Pass=editpass.getText().toString().trim();
                String Pass1=editrepeatpass.getText().toString().trim();
                userController.HandleCreateAccount(Email,Pass,Pass1);
                break;
            case R.id.back:finish();break;


        }
    }



}
