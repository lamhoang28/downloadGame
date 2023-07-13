package com.example.proteam5.Model;

import java.io.Serializable;

public class Game implements Serializable {
    String maBaiViet,tieuDe,gioiThieu,imgShow,imgReview,imgReview1,imgReview2,urlVideo,dieuKhoan,linkDownload;
    int OnlineorOffline,theLoai,giaTien, luotTai;
    public Game() {
    }

    public Game(String maBaiViet, String tieuDe, String gioiThieu, String imgShow, String imgReview, String imgReview1, String imgReview2, String urlVideo, String dieuKhoan, String linkDownload, int onlineorOffline, int theLoai, int giaTien, int luotTai) {
        this.maBaiViet = maBaiViet;
        this.tieuDe = tieuDe;
        this.gioiThieu = gioiThieu;
        this.imgShow = imgShow;
        this.imgReview = imgReview;
        this.imgReview1 = imgReview1;
        this.imgReview2 = imgReview2;
        this.urlVideo = urlVideo;
        this.dieuKhoan = dieuKhoan;
        this.linkDownload = linkDownload;
        OnlineorOffline = onlineorOffline;
        this.theLoai = theLoai;
        this.giaTien = giaTien;
        this.luotTai = luotTai;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public int getOnlineorOffline() {
        return OnlineorOffline;
    }

    public void setOnlineorOffline(int onlineorOffline) {
        OnlineorOffline = onlineorOffline;
    }

    public int getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(int theLoai) {
        this.theLoai = theLoai;
    }

    public String getMaBaiViet() {
        return maBaiViet;
    }

    public void setMaBaiViet(String maBaiViet) {
        this.maBaiViet = maBaiViet;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }

    public String getImgShow() {
        return imgShow;
    }

    public void setImgShow(String imgShow) {
        this.imgShow = imgShow;
    }

    public String getImgReview() {
        return imgReview;
    }

    public void setImgReview(String imgReview) {
        this.imgReview = imgReview;
    }

    public String getImgReview1() {
        return imgReview1;
    }

    public void setImgReview1(String imgReview1) {
        this.imgReview1 = imgReview1;
    }

    public String getImgReview2() {
        return imgReview2;
    }

    public void setImgReview2(String imgReview2) {
        this.imgReview2 = imgReview2;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getDieuKhoan() {
        return dieuKhoan;
    }

    public void setDieuKhoan(String dieuKhoan) {
        this.dieuKhoan = dieuKhoan;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    public int getLuotTai() {
        return luotTai;
    }

    public void setLuotTai(int luotTai) {
        this.luotTai = luotTai;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }
}
