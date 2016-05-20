package com.tf.truefeeling.custom;


import android.media.Image;

import io.realm.RealmObject;

/**
 * Demo class that encapsulates data stored in realm.io database.
 * This class represents data suitable for all chart-types.
 */
public class ProfileData extends RealmObject {
    //private Image avatar;  //TODO
    private String nickname;
    private String birthday; //format: 1969-07-23
    private boolean male;
    private int height;
//    private int weight;

    public ProfileData() {

    }

    public ProfileData(/*Image avatar,*/String nickname, String birthday, boolean male, int height/*,int weight*/) {
        //this.avatar = avatar;
        this.nickname = nickname;
        this.birthday = birthday;
        this.male = male;
        this.height = height;
        //this.weight = weight;
    }

    /*public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(float value) {
        this.value = value;
    }*/

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String bd) {
        this.birthday = bd;
    }

    public boolean getMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

