package com.tf.truefeeling.Model;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by admin on 2016/4/9.
 */
public class LeDeviceList extends Observable {
    private static LeDeviceList dl=null;
    private static ArrayList<BluetoothDevice> dlList;

    private LeDeviceList(){
        dlList=new ArrayList<BluetoothDevice>();
    }

    public static LeDeviceList getInstance(){
        if(null==dl){
            dl=new LeDeviceList();
        }

        return dl;
    }

    public void add(BluetoothDevice item){
        for(BluetoothDevice it: dlList){

            if(it.getAddress().equals(item.getAddress())){
                return;    //keep the list unik
            }
        }
        dlList.add(item);
    }

   public void add(String name,BluetoothDevice item){
       for(BluetoothDevice it: dlList){

           if(it.getAddress().equals(item.getAddress())){
               return;    //keep the list uniq
           }

       }
        dlList.add(item);
    }

    public BluetoothDevice get(int position){
        return dlList.get(position);
    }

    public int size(){
        return dlList.size();
    }

    public void clear(){
        dlList.clear();
    }

    public ArrayList<BluetoothDevice> getAll(){
        return dlList;
    }
}
