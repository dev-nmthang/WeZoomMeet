package com.thang.wesee.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.thang.wesee.Controller.Interface.UserView;
import com.thang.wesee.Model.UserModel;
import com.thang.wesee.View.Activity.HomeActivity;
import com.thang.wesee.View.Activity.SupportPersionActivity;
import com.thang.wesee.View.Activity.WaitingActivity;

public class UserController  implements UserView {
    private UserModel userModel;
    private String types;
    private Intent intent;

    private Context context;
    private FirebaseAuth firebaseAuth;

    public  UserController(Context context){
        this.context=context;
        userModel=new UserModel(this);
        firebaseAuth=FirebaseAuth.getInstance();
    }
    public  void HandleCreateAccount(String Email,String pass,String pass1){
        userModel.CreateAccount(Email,pass,pass1);

    }
    public  void HandleLoginAccount(String Email,String Pass){
        userModel.HandleLoginAccount(Email,Pass);
    }
    public  void HandleCreateZoom(String name,String date){
        userModel.HandleCreateRoom(name,date);
    }
    public void HandleAutologin(){

        if(firebaseAuth.getCurrentUser()!=null)
        {
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                OnSucess();
            }

        }
    }
    public  void SaveInfo(Context context){
        userModel.SaveInFo(context);
    }
    public  void UpdateFCMTOKEN(Context context,String token){
        userModel.SendUpdateFCM(context,token);
    }
    public  void HandleChangePass(String pass){
        userModel.HandleSavePass(pass);
    }
    public  void  UpdateTokenLogOut(String email){
        userModel.UpdateToKenLogout(context);
    }
    public  void UpdateCall(String type,String email){
        userModel.UpdateCall(type,email);
    }
    public  void UpdateTypePersion(Context context,String type){
        userModel.UpdateTypePerson(context,type);
    }

    public  void HandleUpdateStatus(String room,String status){
        userModel.HandleUpdateStatus(room,status);
    }


    public void getDataListRoom(Context context){
        userModel.getDataListRoom(context);
    }






    @Override
    public void OnValid() {
        Toast.makeText(context,"Email not Valid",Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnLengthPass() {
        Toast.makeText(context, "Pass has length  > 8 char", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSamePass() {
        Toast.makeText(context,"Password not same", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ValidEmailAuth() {
        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(context, "Sended", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(context, "Please go to gmail for authentication ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnFail() {
        Toast.makeText(context, "Fail ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnSucess() {
        userModel.getDataType(firebaseAuth.getCurrentUser().getEmail());
    }

    @Override
    public void OnSucessRoom() {
        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDataRoom(String name, String date) {

    }

    @Override
    public void getDataType(String type) {
        if(type!=null){
            switch (type){
                case "VOLUNTEER": intent=new Intent(context, WaitingActivity.class);break;
                case "DISABLE": intent=new Intent(context, SupportPersionActivity.class);break;
            }

        }else{
            intent=new Intent(context,HomeActivity.class);
        }
        context.startActivity(intent);
    }

    @Override
    public void getDataStatus(String call) {
        if(call.equalsIgnoreCase("cancle")){
            ((Activity)context).finish();
        }
    }

    @Override
    public void OnSucessPass() {
        Toast.makeText(context, "Save Sucess", Toast.LENGTH_SHORT).show();
    }
}
