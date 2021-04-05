package com.thang.wesee.Model;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.*;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.Controller.Interface.UserView;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserModel implements Serializable {
    private String Email;
    private String DATE;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String FCM_TOKEN;
    private PreFerenceManager preFerenceManager;
    private String CALL;
    private String TYPE;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;



    private UserView callback;
    private String Email_Vali="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public UserModel(UserView callback){
        this.callback=callback;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance("https://zoommeeting-3f76b-default-rtdb.firebaseio.com/");
        databaseReference=firebaseDatabase.getReference();
        firebaseUser=firebaseAuth.getCurrentUser();


    }

    public UserModel(String Email,String DATE, String FCM_TOKEN,String CALL,String TYPE) {
        this.Email=Email;
        this.DATE = DATE;
        this.FCM_TOKEN = FCM_TOKEN;
        this.CALL=CALL;
        this.TYPE=TYPE;
    }

    public UserModel() {
    }

    public  void CreateAccount(String Email, String Pass, String Pass1){
        if(Email.length()>0 && Email.matches(Email_Vali)){
            if(Pass.length()>8 ||Pass1.length()>8){
                if(Pass.equals(Pass1)){
                    firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UpdateUser();
                                callback.ValidEmailAuth();
                            }else{
                                callback.OnFail();
                            }
                        }
                    });
                }else{
                    callback.OnSamePass();
                }
            }else{
                callback.OnLengthPass();
            }
        }else{
            callback.OnValid();
        }
    }
    public  void  HandleLoginAccount(String Email,String Pass){
        if(Email.length()>0 && Email.matches(Email_Vali)){
            if(Pass.length()>8 ){
                firebaseAuth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                callback.OnSucess();
                            }else{
                                callback.ValidEmailAuth();
                            }
                        }else{
                            callback.OnFail();
                        }
                    }
                });
            }else{
                callback.OnLengthPass();
            }
        }else{
            callback.OnValid();
        }
    }






    public  void UpdateUser(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM//yyyy");
        String date=simpleDateFormat.format(calendar.getTime());
        HashMap<String,String> User=new HashMap<>();
        User.put("DATE",date);
        User.put("EMAIL",firebaseAuth.getCurrentUser().getEmail());
        User.put("CALL","Active");
        User.put("TYPE","NONE");
        User.put("FCM_TOKEN","NONE");
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .setValue(User);


    }
    public void SaveInFo(Context context,String token){
        preFerenceManager=new PreFerenceManager(context);
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("FCM_TOKEN")
                .setValue(token);
    }

    public  void UpdateCall(String call){
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("CALL").setValue(call);
    }
    public  void HandleUpdateTypePerson(String type){
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("TYPE").setValue(type);
    }
    public  void HandleUpdateRoom(String room,String satus)
    {
        databaseReference.child(room).setValue(satus);
    }




    public  void UpdateToKenLogout(Context context){
        preFerenceManager=new PreFerenceManager(context);
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("FCM_TOKEN").setValue("");
    }



    public  void HandleSavePass(String pass){
        firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    callback.OnSucessPass();
                }
            }
        });
    }


    public String getFCM_TOKEN() {
        return FCM_TOKEN;
    }

    public String getDATE() {
        return DATE;
    }

    public String getEmail() {
        return Email;
    }

    public String getCALL() {
        return CALL;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void getDataType() {
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.getDataType(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child("User")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("TYPE").addListenerForSingleValueEvent(valueEventListener);

    }
}
