package com.thang.wesee.View.FragMent;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.UserController;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.R;

import com.thang.wesee.View.Activity.OutGoingCallActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class FragMent_Home_Disable extends Fragment
{
    View view;
    private RelativeLayout r1;
    private UserController userController;
    private Handler handler;
    private ArrayList<UserModel> arrayList;
    private int k=0;
    private Random rd;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               try{
                   k=rd.nextInt(arrayList.size()-1)+0;
               }catch (Exception e){
                   k=0;


               }

            }
        },1500);
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrayList.size()>0){

                    Intent intent=new Intent(getContext(), OutGoingCallActivity.class);
                    intent.putExtra("User",arrayList.get(k));
                    startActivity(intent);






                }else{


                    Toast.makeText(getContext(), "Not person ! Sorry", Toast.LENGTH_SHORT).show();


                }


            }
        });

    }


    private void getDataUser() {
        userController=new UserController(getContext());
        arrayList=new ArrayList<>();

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance("https://zoommeeting-3f76b-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference=firebaseDatabase.getReference();

        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    UserModel userModel =d.getValue(UserModel.class);
                    if(userModel.getTYPE().equalsIgnoreCase("DISABLE")){
                        continue;
                    }
                    if(userModel.getCALL().equalsIgnoreCase("BUSY")
                            && userModel.getTYPE().equalsIgnoreCase("VOLUNTEER")
                            ||userModel.getCALL().equalsIgnoreCase("Ringing")
                            && userModel.getTYPE().equalsIgnoreCase("VOLUNTEER")){
                        continue;
                    }
                    arrayList.add(new UserModel(userModel.getEmail(),userModel.getDATE(),userModel.getFCM_TOKEN(),userModel.getCALL(),userModel.getTYPE()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child("User").addListenerForSingleValueEvent(valueEventListener);

    }



}
