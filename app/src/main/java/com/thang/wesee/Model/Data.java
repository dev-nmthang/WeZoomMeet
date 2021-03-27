package com.thang.wesee.Model;

public class Data {
    private String title;
    private  String message;
    private String token;
    private String room;
    private String la;
    private String lo;
    private String email;


    public  Data(String title,String message,String token,String room,String la,String lo,String email){
        this.title=title;
        this.message=message;
        this.token=token;
        this.room=room;
        this.la=la;
        this.lo=lo;
        this.email=email;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getLo() {
        return lo;
    }

    public String getLa() {
        return la;
    }

    public String getRoom() {
        return room;
    }

    public String getEmail() {
        return email;
    }
}
