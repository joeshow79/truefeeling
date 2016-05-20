package com.tf.truefeeling.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenggang on 15/3/8.
 */
public class TFUserInfo implements Data {

    private String id;
    private String email;
    private String nickname;
    private String password;
    private String avatar;
    private String avatarLocalPath;
    private String phone;
    private String salt;
    private String type;
    private String profile;
    private boolean searchDisable;
    private String gender /*= Contacts.SEX_MAIE*/ = "male";
    private int age;
    private String location;
    private String country;
    //TODO if server interface changed, will update this.
//    private List<String> hobby;
    private String hobby;
    private String sioeyeId;
    private String remark;
    private int liveNum;
    private int following;
    private int followers;

    public TFUserInfo() {

    }

    public TFUserInfo(TFUserInfo userInfo) {
        this.id = userInfo.id;
        this.email = userInfo.email;
        this.nickname = userInfo.nickname;
        this.password = userInfo.password;
        this.avatar = userInfo.avatar;
        this.avatarLocalPath = userInfo.avatarLocalPath;
        this.phone = userInfo.phone;
        this.salt = userInfo.salt;
        this.type = userInfo.type;
        this.profile = userInfo.profile;
        this.searchDisable = userInfo.searchDisable;
        this.gender = userInfo.gender;
        this.age = userInfo.age;
        this.location = userInfo.location;
        this.country = userInfo.country;
        this.hobby = userInfo.hobby;
        this.sioeyeId = userInfo.sioeyeId;
        this.remark = userInfo.remark;
        this.liveNum = userInfo.liveNum;
        this.following = userInfo.following;
        this.followers = userInfo.followers;
    }

    public TFUserInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public TFUserInfo(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        if (nickname == null) {
            return "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatarLocalPath() {
        return avatarLocalPath;
    }

    public void setAvatarLocalPath(String avatarLocalPath) {
        this.avatarLocalPath = avatarLocalPath;
    }

    public int getAge() {
        return age;
    }

    public List<String> getHobby() {
        return getHobby(hobby);
    }


    public static List<String> getHobby(String hobby) {
        if (TextUtils.isEmpty(hobby)) {
            ArrayList<String> result = new ArrayList<>(0);
            return result;
        }
        String[] hobbys = hobby.split(",");
        ArrayList<String> result = new ArrayList<>(hobbys.length);
        for (String h : hobbys) {
            result.add(h);
        }
        return result;
    }

    public String getHobbyString() {
        if (hobby == null) {
            return "";
        }
        return hobby;
    }

    public String getLocation() {
        if (location == null) {
            return "";
        }
        return location;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        //TODO
        /*if(gender == null) {
            gender = Contacts.SEX_UNKNOWN;
        }*/
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSearchDisable() {
        return searchDisable;
    }

    public void setSearchDisable(boolean searchDisable) {
        this.searchDisable = searchDisable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSioeyeId() {
        return sioeyeId;
    }

    public void setSioeyeId(String sioeyeId) {
        this.sioeyeId = sioeyeId;
    }

    /**
     * return female  or not.
     * Used for set default user icon.
     *
     * @return
     */
    public boolean isFemale() {
        //TODO
        return true;
        //return Contacts.SEX_FEMAIE.equalsIgnoreCase(gender);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFollowersNum() {
        return followers;
    }

    public int getFollowingNum() {
        return following;
    }

    public int getLiveNum() {
        return liveNum;
    }

    public void setFollowerNum(int followers) {
        this.followers = followers;
    }

    public void setFollowingNum(int following) {
        this.following = following;
    }

    public void setLiveNum(int liveNum) {
        this.liveNum = liveNum;
    }

    /**
     * @param other
     * @return
     */
    public boolean isSameUserIcon(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof TFUserInfo)) {
            return false;
        }

        TFUserInfo o = (TFUserInfo) other;
        if (!id.equals(o.getId())) {
            return false;
        }
        if (!type.equalsIgnoreCase(o.getType())) {
            return false;
        }
        if (!avatar.equalsIgnoreCase(o.getAvatar())) {
            return false;
        }
        return true;
    }

    public TFUserInfo get() {
        return new TFUserInfo(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(20);
        sb.append("name:");
        sb.append(nickname);
        sb.append(", gender:");
        sb.append(gender);
        sb.append(", location:");
        sb.append(location);
        sb.append(", email:");
        sb.append(email);
        sb.append(", avatar:");
        sb.append(avatar);
        sb.append(", sioeyeId:");
        sb.append(sioeyeId);
        sb.append(", remark:");
        sb.append(remark);
        sb.append(", id:");
        sb.append(id);
        return sb.toString();
    }
}
