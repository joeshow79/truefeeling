package com.tf.truefeeling.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2016/4/10.
 */
public class LeDeviceItem_tbd implements Parcelable {
    public  String itemName;
    public  String itemAddr;

    public LeDeviceItem_tbd(){
        itemName="";
        itemAddr="";
    }

    public LeDeviceItem_tbd(String name, String addr){
        itemName=name;
        itemAddr=addr;
    }

    protected LeDeviceItem_tbd(Parcel in) {
        itemName = in.readString();
        itemAddr = in.readString();
    }
    public void setName(String name){
        itemName=name;
    }

    public void setAddr(String addr){
        itemAddr=addr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemAddr);

    }

    public static final Creator<LeDeviceItem_tbd> CREATOR = new Creator<LeDeviceItem_tbd>() {
        @Override
        public LeDeviceItem_tbd createFromParcel(Parcel in) {
            return new LeDeviceItem_tbd(in);
        }

        @Override
        public LeDeviceItem_tbd[] newArray(int size) {
            return new LeDeviceItem_tbd[size];
        }
    };
}
