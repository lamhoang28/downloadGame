package com.example.proteam5.Model;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThongBao {
    String ID, userGui, userNhan, noiDung, tenGame, imgGame, linkGame;
    int kieuThongBao;
    Date ngaygui;

    public ThongBao() {
    }

    public ThongBao(String ID, String userGui, String userNhan, String noiDung, String tenGame, String imgGame, String linkGame, int kieuThongBao, Date ngaygui) {
        this.ID = ID;
        this.userGui = userGui;
        this.userNhan = userNhan;
        this.noiDung = noiDung;
        this.tenGame = tenGame;
        this.imgGame = imgGame;
        this.linkGame = linkGame;
        this.kieuThongBao = kieuThongBao;
        this.ngaygui = ngaygui;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserGui() {
        return userGui;
    }

    public void setUserGui(String userGui) {
        this.userGui = userGui;
    }

    public String getUserNhan() {
        return userNhan;
    }

    public void setUserNhan(String userNhan) {
        this.userNhan = userNhan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTenGame() {
        return tenGame;
    }

    public void setTenGame(String tenGame) {
        this.tenGame = tenGame;
    }

    public String getImgGame() {
        return imgGame;
    }

    public void setImgGame(String imgGame) {
        this.imgGame = imgGame;
    }

    public String getLinkGame() {
        return linkGame;
    }

    public Date getNgaygui() {
        return ngaygui;
    }

    public void setNgaygui(Date ngaygui) {
        this.ngaygui = ngaygui;
    }

    public void setLinkGame(String linkGame) {
        this.linkGame = linkGame;
    }

    public int getKieuThongBao() {
        return kieuThongBao;
    }

    public void setKieuThongBao(int kieuThongBao) {
        this.kieuThongBao = kieuThongBao;
    }
}
