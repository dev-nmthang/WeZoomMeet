package com.thang.wesee.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Map.MapActivity;
import com.thang.wesee.R;

public class GoogleActivity  extends AppCompatActivity
implements  View.OnClickListener{
    private PreFerenceManager preFerenceManager;
    private UserController userController;
    private TextView txtgoogle;
    private TextView txtexit;
    private LinearLayout c2;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_active);
        txtgoogle=findViewById(R.id.txtgoogle);
        txtexit=findViewById(R.id.txtexit);
        c2=findViewById(R.id.c2);
        preFerenceManager=new PreFerenceManager(this);
       txtexit.setOnClickListener(this::onClick);
       c2.setOnClickListener(this::onClick);
       txtgoogle.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtgoogle: case R.id.c2:
                if(preFerenceManager.getLo().equalsIgnoreCase("15")
                 && preFerenceManager.getLa().equalsIgnoreCase("20")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setMessage("Person  doesn't Share Postion");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }else{
                    startActivity(new Intent(GoogleActivity.this,
                            MapActivity.class));break;
                }
                break;

            case R.id.txtexit:
                finish();break;

        }
    }
}
