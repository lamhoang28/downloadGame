package com.example.proteam5.Model;

public class LinkGame {
    String ID,Name,Coin,Link;

    public LinkGame() {
    }

    public LinkGame(String ID, String name, String coin, String link) {
        this.ID = ID;
        Name = name;
        Coin = coin;
        Link = link;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }
}
