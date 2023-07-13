package com.example.proteam5.Model;

import java.util.Date;

public class Comment {
    String ID,Avatar,UserName,Gmail,IdBaiViet,Title;
    Date ngaycmt;

    public Comment(String ID, String avatar, String userName, String gmail, String idBaiViet, String title, Date ngaycmt) {
        this.ID = ID;
        Avatar = avatar;
        UserName = userName;
        Gmail = gmail;
        IdBaiViet = idBaiViet;
        Title = title;
        this.ngaycmt = ngaycmt;
    }

    public String getGmail() {
        return Gmail;
    }

    public Date getNgaycmt() {
        return ngaycmt;
    }

    public void setNgaycmt(Date ngaycmt) {
        this.ngaycmt = ngaycmt;
    }

    public void setGmail(String gmail) {
        Gmail = gmail;
    }

    public Comment() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getIdBaiViet() {
        return IdBaiViet;
    }

    public void setIdBaiViet(String idBaiViet) {
        IdBaiViet = idBaiViet;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
