package com.example.proteam5.Model;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HoaDon {
    String ID, ID_game, usermail, imgGame, nameGame, nameUser, linkdownload;
    int coin;
    Date ngaymua;

    public HoaDon(String ID, String ID_game, String usermail, String imgGame, String nameGame, String nameUser, String linkdownload, int coin, Date ngaymua) {
        this.ID = ID;
        this.ID_game = ID_game;
        this.usermail = usermail;
        this.imgGame = imgGame;
        this.nameGame = nameGame;
        this.nameUser = nameUser;
        this.linkdownload = linkdownload;
        this.coin = coin;
        this.ngaymua = ngaymua;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID_game() {
        return ID_game;
    }

    public void setID_game(String ID_game) {
        this.ID_game = ID_game;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getImgGame() {
        return imgGame;
    }

    public void setImgGame(String imgGame) {
        this.imgGame = imgGame;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLinkdownload() {
        return linkdownload;
    }

    public void setLinkdownload(String linkdownload) {
        this.linkdownload = linkdownload;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Date getNgaymua() {
        return ngaymua;
    }

    public void setNgaymua(Date ngaymua) {
        this.ngaymua = ngaymua;
    }

    public HoaDon() {
    }


}
