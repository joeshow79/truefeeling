package com.tf.truefeeling.custom;


import io.realm.RealmObject;

/**
 * Demo class that encapsulates data stored in realm.io database.
 * This class represents data suitable for all chart-types.
 */
public class GoalData extends RealmObject {
    private int value;
    private int index;

    public GoalData() {

    }

    public GoalData(int value, int index) {
        this.value = value;
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}