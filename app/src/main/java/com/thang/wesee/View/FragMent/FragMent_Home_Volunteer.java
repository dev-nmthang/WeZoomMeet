package com.thang.wesee.View.FragMent;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FragMent_Home_Volunteer extends Fragment {
    View view;
    private RelativeLayout r1;
    private PreFerenceManager preFerenceManager;
    private UserController userController;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home_volunteer,container,false);
        preFerenceManager=new PreFerenceManager(getContext());
        preFerenceManager.PutXAndY("1","1");
        ShowDiaLogPermission();




        return  view;
    }
    private  void ShowDiaLogPermission(){
        Dexter.withActivity(getActivity())
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
