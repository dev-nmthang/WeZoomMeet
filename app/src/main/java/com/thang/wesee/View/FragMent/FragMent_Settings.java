package com.thang.wesee.View.FragMent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;
import com.thang.wesee.View.Activity.ChangePassActivity;
import com.thang.wesee.View.Activity.LoginActivity;
import org.jetbrains.annotations.NotNull;

public class FragMent_Settings  extends Fragment
{
    View view;
  private Button btnlougout;
  private UserController userController;
  private TextView txtname,txtchangepass;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_settings,container,false);
        btnlougout=view.findViewById(R.id.btnlogout);
        txtname=view.findViewById(R.id.txtemail);
        txtchangepass=view.findViewById(R.id.txtchange);
        userController=new UserController(getContext());
        Init();
        return  view;
    }

    private void Init() {
        txtname.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        btnlougout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userController.UpdateTokenLogOut(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();

            }
        });
        txtchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePassActivity.class));
            }
        });

    }

}
