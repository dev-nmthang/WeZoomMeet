package com.thang.wesee.View.FragMent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;

import com.thang.wesee.View.Activity.OutGoingCallActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragMent_Home_Disable extends Fragment
 {
    View view;
    private RelativeLayout r1;
    private UserController userController;
    private Handler handler;
    private ArrayList<UserModel> arrayList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        r1=view.findViewById(R.id.r1);
        Init();
        return  view;
    }
    private void Init() {

       getDataUser();


        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrayList.size()>0){
                    Intent intent=new Intent(getContext(), OutGoingCallActivity.class);
                    for(UserModel m : arrayList){
                        if(!m.getCall().equalsIgnoreCase("calling")
                          && !m.getCall().equalsIgnoreCase("busy")){
                            intent.putExtra("User",arrayList.get(0));
                            startActivity(intent);
                        }
                    }




                }else{
                    Toast.makeText(getContext(), "Not person ! Sorry", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

     private void getDataUser() {
         userController=new UserController(getContext());
         arrayList=new ArrayList<>();
         PreFerenceManager preFerenceManager=new PreFerenceManager(getContext());
         FirebaseFirestore db=FirebaseFirestore.getInstance();
         db.collection("User").
                 whereEqualTo("TYPE","VOLUNTEER").
                 get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(QueryDocumentSnapshot q : queryDocumentSnapshots){
                     UserModel roomModel=q.toObject(UserModel.class);

                     arrayList.add(new UserModel(roomModel.getEmail(),roomModel.getDATE(),roomModel.getFCM_TOKEN(),roomModel.getCall(),roomModel.getType()));



                 }
             }
         });

     }

 }
