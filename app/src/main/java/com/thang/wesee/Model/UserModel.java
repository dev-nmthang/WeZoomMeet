package com.thang.wesee.Model;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private FirebaseFirestore db;
    private PreFerenceManager preFerenceManager;
    private String call;
    private String type;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private UserView callback;
    private String Email_Vali="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public UserModel(UserView callback){
        this.callback=callback;
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance("https://zoommeeting-3f76b-default-rtdb.firebaseio.com/");
        databaseReference=firebaseDatabase.getReference();
        firebaseUser=firebaseAuth.getCurrentUser();
        db=FirebaseFirestore.getInstance();

    }

    public UserModel(String email, String DATE, String FCM_TOKEN,String call,String type) {
        Email = email;
        this.DATE = DATE;
        this.FCM_TOKEN = FCM_TOKEN;
        this.call=call;
        this.type=type;
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

    public  void HandleCreateRoom(String name,String date){
        RoomModel roomModel=new RoomModel(name,date);
        db.collection("ROOM")
                .add(roomModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    callback.OnSucessRoom();
                }else{
                    callback.OnFail();
                }
            }
        });
    }

    public void   getDataListRoom(Context context){




    }
    public  void SendFCM(Context context){

        SendUpdateFCM(context,preFerenceManager.getToken());
    }

    public  void UpdateUser(){
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM//yyyy");
        String date=simpleDateFormat.format(calendar.getTime());
        HashMap<String,String> User=new HashMap<>();
        User.put("DATE",date);
        User.put("Email",firebaseAuth.getCurrentUser().getEmail());
        User.put("call","Active");//

        db.collection("User")
                .add(User).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {

            }
        });
    }
    public void SaveInFo(Context context){
        preFerenceManager=new PreFerenceManager(context);
        db.collection("User")
                .whereEqualTo("Email",firebaseUser.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot=queryDocumentSnapshots.getDocuments().get(0);

                if(preFerenceManager.getID().equalsIgnoreCase(documentSnapshot.getId())){

                }else{
                    preFerenceManager.putKeyValue(documentSnapshot.getId());
                }

            }
        });
    }
    public  void  getDataType(String email){
        db.collection("User")
                .whereEqualTo("Email",email)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot d :queryDocumentSnapshots){
                    callback.getDataType(d.getString("TYPE"));
                }
            }
        });
    }
    public  void UpdateCall(String type,String email){
        db.collection("User")
                .whereEqualTo("Email",email)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DocumentSnapshot documentSnapshot=queryDocumentSnapshots.getDocuments().get(0);
                SaveType(documentSnapshot.getId(),type);
            }
        });
    }

    private void SaveType(String id,String type) {
        db.collection("User").document(id)
                .update("call",type)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                    }
                });
    }


    public  void SendUpdateFCM(Context context,String token){
        preFerenceManager=new PreFerenceManager(context);
        db.collection("User").document(preFerenceManager.getID())
                .update("FCM_TOKEN",token)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                    }
                });
    }
    public  void UpdateToKenLogout(Context context){
        preFerenceManager=new PreFerenceManager(context);
        db.collection("User").document(preFerenceManager.getID())
                .update("FCM_TOKEN","")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }
    public  void UpdateTypePerson(Context context,String Type){
        preFerenceManager=new PreFerenceManager(context);
        db.collection("User").document(preFerenceManager.getID())
                .update("TYPE",Type)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public  void HandleUpdateStatus(String room,String status){
        databaseReference.child(room).setValue(status) ;

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

    public String getCall() {
        return call;
    }




    public String getType() {
        return type;
    }
}
