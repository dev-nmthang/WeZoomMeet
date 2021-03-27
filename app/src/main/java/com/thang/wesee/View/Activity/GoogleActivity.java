package com.thang.wesee.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Map.MapActivity;
import com.thang.wesee.R;

public class GoogleActivity  extends AppCompatActivity {
    private PreFerenceManager preFerenceManager;
    private UserController userController;
    private TextView txtgoogle;
    private CardView c1;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_active);
        txtgoogle=findViewById(R.id.txtgoogle);
        c1=findViewById(R.id.c1);
        txtgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoogleActivity.this,
                        MapActivity.class));
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
