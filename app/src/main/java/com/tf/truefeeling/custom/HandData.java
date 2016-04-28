package com.tf.truefeeling.custom;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Demo class that encapsulates data stored in realm.io database.
 * This class represents data suitable for all chart-types.
 */
public class HandData extends RealmObject {

    private int f1;
    private int f1Plan;

    private int f2;
    private int f2Plan;

    private int f3;
    private int f3Plan;

    private int f4;
    private int f4Plan;

    private int f5;
    private int f5Plan;


    //private RealmList<FingerData> right;

    //private String xValue;

    // ofc there could me more fields here...

    public HandData() {
        f1=0;
        f1Plan=0;
        f2=0;
        f2Plan=0;
        f3=0;
        f3Plan=0;
        f4=0;
        f4Plan=0;
        f5=0;
        f5Plan=0;
    }

    public HandData(int f1,int f1Plan,int f2,int f2Plan,int f3,int f3Plan,int f4,int f4Plan,int f5,int f5Plan) {
        this.f1=f1;
        this.f1Plan=f1Plan;
        this.f2=f2;
        this.f2Plan=f2Plan;
        this.f3=f3;
        this.f3Plan=f3Plan;
        this.f4=f4;
        this.f4Plan=f4Plan;
        this.f5=f5;
        this.f5Plan=f5Plan;
    }

    public int getF1(){
        return f1;
    }


    public void setF1(int f1){
        this.f1=f1;
    }


    public int getF1Plan(){
        return f1Plan;
    }

    public void setF1Plan(int f1Plan)
    {
        this.f1Plan=f1Plan;
    }
    public int getF2(){
        return f2;
    }


    public void setF2(int f2){
        this.f2=f2;
    }


    public int getF2Plan(){
        return f2Plan;
    }

    public void setF2Plan(int f2Plan)
    {
        this.f2Plan=f2Plan;
    }

    public int getF3(){
        return f3;
    }


    public void setF3(int f3){
        this.f3=f3;
    }


    public int getF3Plan(){
        return f3Plan;
    }

    public void setF3Plan(int f3Plan)
    {
        this.f3Plan=f3Plan;
    }

    public int getF4(){
        return f4;
    }


    public void setF4(int f4){
        this.f4=f4;
    }


    public int getF4Plan(){
        return f4Plan;
    }

    public void setF4Plan(int f4Plan)
    {
        this.f4Plan=f4Plan;
    }

    public int getF5(){
        return f5;
    }

    public void setF5(int f5){
        this.f5=f5;
    }


    public int getF5Plan(){
        return f5Plan;
    }

    public void setF5Plan(int f5Plan)
    {
        this.f5Plan=f5Plan;
    }
//    public String getxValue() {
//        return xValue;
//    }
//
//    public void setxValue(String xValue) {
//        this.xValue = xValue;
//    }
}