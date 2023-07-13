package com.example.proteam5.Model;

import java.io.Serializable;

public class Apps implements Serializable {
    String ID,TieuDe,NoiDung,ImgShow,ImgReview,ImgReview1,ImgReview2,FAQ,Link;

    public Apps(String ID, String tieuDe, String noiDung, String imgShow, String imgReview, String imgReview1, String imgReview2, String FAQ, String link) {
        this.ID = ID;
        TieuDe = tieuDe;
        NoiDung = noiDung;
        ImgShow = imgShow;
        ImgReview = imgReview;
        ImgReview1 = imgReview1;
        ImgReview2 = imgReview2;
        this.FAQ = FAQ;
        Link = link;
    }

    public Apps() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getImgShow() {
        return ImgShow;
    }

    public void setImgShow(String imgShow) {
        ImgShow = imgShow;
    }

    public String getImgReview() {
        return ImgReview;
    }

    public void setImgReview(String imgReview) {
        ImgReview = imgReview;
    }

    public String getImgReview1() {
        return ImgReview1;
    }

    public void setImgReview1(String imgReview1) {
        ImgReview1 = imgReview1;
    }

    public String getImgReview2() {
        return ImgReview2;
    }

    public void setImgReview2(String imgReview2) {
        ImgReview2 = imgReview2;
    }

    public String getFAQ() {
        return FAQ;
    }

    public void setFAQ(String FAQ) {
        this.FAQ = FAQ;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
