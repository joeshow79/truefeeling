package com.tf.truefeeling.model;

import java.util.Observable;

import com.tf.truefeeling.MiBand;
import com.tf.truefeeling.Util.Log;
import com.tf.truefeeling.listener.RealtimeStepsNotifyListener;


public class MiBandData extends Observable  implements RealtimeStepsNotifyListener{

	public String mBTAddress;
	public int mSteps;
	public String mName;
	public Battery mBattery;
	public LeParams mLeParams;
	private String TAG="MiBandData";

	public static MiBandData mInstance=null;

	private MiBandData(){
	}

	public static MiBandData getInstance(){
		if(null==mInstance){
			mInstance=new MiBandData();
		}

		return mInstance;
	}

	public void setName(String name) {
		mName = name;
		setChanged();
		notifyObservers();
	}
	
	public void setSteps(int steps) {
		mSteps = steps;
		Log.d(TAG, "setting " + steps + " steps");
		setChanged();
		notifyObservers();
	}
	
	public void setBattery(Battery battery) {
		mBattery = battery;
		//L.i(battery.toString());
		setChanged();
		notifyObservers();
	}

	public void setLeParams(LeParams params) {
		mLeParams = params;
	}

	public void onNotify(int steps){
		setSteps(steps);
	}

}
