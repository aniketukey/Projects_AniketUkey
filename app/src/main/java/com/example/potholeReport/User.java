package com.example.potholeReport;

public class User {

    public String username ,email ,contact,uid;
    public int isUser;
    public User (){

    }
    public User(String username,String email, String contact,int isUser){
        this.username = username;
        this.email=email;
        this.contact=contact;
        this.isUser=isUser;
        this.isUser = Integer.parseInt(String.valueOf(isUser));
    }
}
