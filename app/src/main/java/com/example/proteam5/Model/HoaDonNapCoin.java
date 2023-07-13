package com.example.proteam5.Model;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HoaDonNapCoin {
    String id, username, usermail, adminNap, noidung;
    Date ngaynap;
    int tiennap;

    public HoaDonNapCoin() {
    }

    public HoaDonNapCoin(String id, String username, String usermail, String adminNap, String noidung, Date ngaynap, int tiennap) {
        this.id = id;
        this.username = username;
        this.usermail = usermail;
        this.adminNap = adminNap;
        this.noidung = noidung;
        this.ngaynap = ngaynap;
        this.tiennap = tiennap;
    }

    public String getId() {
        return id;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getAdminNap() {
        return adminNap;
    }

    public void setAdminNap(String adminNap) {
        this.adminNap = adminNap;
    }

    public Date getNgaynap() {
        return ngaynap;
    }

    public void setNgaynap(Date ngaynap) {
        this.ngaynap = ngaynap;
    }

    public int getTiennap() {
        return tiennap;
    }

    public void setTiennap(int tiennap) {
        this.tiennap = tiennap;
    }
}
