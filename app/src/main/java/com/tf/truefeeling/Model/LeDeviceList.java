package com.tf.truefeeling.Model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by admin on 2016/4/9.
 */
public class LeDeviceList extends Observable {
    private static LeDeviceList dl=null;
    private static ArrayList<BLEDeviceContent.BLEDeviceItem> dlList;

    private LeDeviceList(){
        dlList=new ArrayList<BLEDeviceContent.BLEDeviceItem>();
    }

    public static LeDeviceList getInstance(){
        if(null==dl){
            dl=new LeDeviceList();
        }

        return dl;
    }

    public void add(BLEDeviceContent.BLEDeviceItem item){
        for(BLEDeviceContent.BLEDeviceItem it: dlList){

            if(it.addr.equals(item.addr)){
                return;    //keep the list unik
            }
        }
        dlList.add(item);
    }

   public void add(String name,String addr){
       for(BLEDeviceContent.BLEDeviceItem it: dlList){

           if(it.addr.equals(addr)){
               return;    //keep the list uniq
           }

       }
        dlList.add(new BLEDeviceContent.BLEDeviceItem(name,addr));
    }

    public BLEDeviceContent.BLEDeviceItem get(int position){
        return dlList.get(position);
    }

    public int size(){
        return dlList.size();
    }

    public void clear(){
        dlList.clear();
    }

    public ArrayList<BLEDeviceContent.BLEDeviceItem> getAll(){
        return dlList;
    }
}
