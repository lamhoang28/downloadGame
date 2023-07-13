package com.example.proteam5.Model;

import java.io.Serializable;
import java.util.Date;

public class TinTuc implements Serializable {
    String maBaiViet, TieuDe, ImgShow, ImgReView, ImgReView1, urlVideo, NoiDung;
    Date ngaydang;

    public TinTuc(String maBaiViet, String tieuDe, String imgShow, String imgReView, String imgReView1, String urlVideo, String noiDung, Date ngaydang) {
        this.maBaiViet = maBaiViet;
        TieuDe = tieuDe;
        ImgShow = imgShow;
        ImgReView = imgReView;
        ImgReView1 = imgReView1;
        this.urlVideo = urlVideo;
        NoiDung = noiDung;
        this.ngaydang = ngaydang;
    }

    public Date getNgaydang() {
        return ngaydang;
    }

    public void setNgaydang(Date ngaydang) {
        this.ngaydang = ngaydang;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public TinTuc() {
    }

    public String getMaBaiViet() {
        return maBaiViet;
    }

    public void setMaBaiViet(String maBaiViet) {
        this.maBaiViet = maBaiViet;
    }


    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getImgShow() {
        return ImgShow;
    }

    public void setImgShow(String imgShow) {
        ImgShow = imgShow;
    }

    public String getImgReView() {
        return ImgReView;
    }

    public void setImgReView(String imgReView) {
        ImgReView = imgReView;
    }

    public String getImgReView1() {
        return ImgReView1;
    }

    public void setImgReView1(String imgReView1) {
        ImgReView1 = imgReView1;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }
}
