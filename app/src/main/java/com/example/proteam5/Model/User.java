package com.example.proteam5.Model;

public class User {
    String avatar;
    String UserName;
    String mail;
    int age;
    int coin;

    public User() {
    }

    public User(String avatar, String userName, String mail, int age, int coin) {
        this.avatar = avatar;
        this.UserName = userName;
        this.mail = mail;
        this.age = age;
        this.coin = coin;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }
}
