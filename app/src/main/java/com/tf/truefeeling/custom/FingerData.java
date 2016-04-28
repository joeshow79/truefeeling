package com.tf.truefeeling.custom;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Demo class that encapsulates data stored in realm.io database.
 * This class represents data suitable for all chart-types.
 */
public class FingerData extends RealmObject {
    private String when;
    private float value;
    private float planValue;
    private int xIndex;

    //private String xValue;

    // ofc there could me more fields here...

    public FingerData() {

    }

    public FingerData(float value,float planValue, int xIndex, String when) {
        this.value = value;
        this.xIndex = xIndex;
        this.planValue=planValue;
        this.when=when;
    }

    public String getWhen(){
        return when;
    }

    public void setWhen(String when){
        this.when=when;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPlanValue() {
        return planValue;
    }

    public void setPlanValue(float value) {
        this.planValue = value;
    }

    public int getxIndex() {
        return xIndex;
    }

    public void setxIndex(int xIndex) {
        this.xIndex = xIndex;
    }

//    public String getxValue() {
//        return xValue;
//    }
//
//    public void setxValue(String xValue) {
//        this.xValue = xValue;
//    }
}