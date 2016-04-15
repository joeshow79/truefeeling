package com.tf.truefeeling.Model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;
import android.os.Handler;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import com.tf.truefeeling.Activity.TFApplication;

public class BLEMediator implements Observer {

    private static final UUID UUID_MILI_SERVICE = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_pair = UUID.fromString("0000ff0f-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_CONTROL_POINT = UUID.fromString("0000ff05-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_REALTIME_STEPS = UUID.fromString("0000ff06-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_ACTIVITY = UUID.fromString("0000ff07-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_LE_PARAMS = UUID.fromString("0000ff09-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_DEVICE_NAME = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHAR_BATTERY = UUID.fromString("0000ff0c-0000-1000-8000-00805f9b34fb");

    // BLUETOOTH
    //private String mDeviceAddress;
    //private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothMi;
    private BluetoothGatt mGatt;

    private static MiBand mMiBand = new MiBand();
    private static BLEMediator mediator = null;

    //是否正在扫描蓝牙设备
    private boolean mScanning = false;
    //设置扫描时长
    private static final long SCAN_PERIOD = 5000;

    Handler mHandler = new Handler();
    //蓝牙扫描的返回
    BluetoothAdapter.LeScanCallback leScanCallback;
    LeScanListener leScanListener;


    private String TAG = "BLEMediator";

    protected BLEMediator() {
        mMiBand.addObserver(this);
        // mMiBand.mBTAddress = mDeviceAddress;

        //mBluetoothManager = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE));
        //mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //蓝牙搜索的回调
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

                Log.d(TAG, "onLeScan: name: " + device.getName() + ", uuid: " + device.getUuids() + ", add: " + device.getAddress() + ", type: " + device.getType() + ", bondState: " + device.getBondState() + ", rssi: " + rssi);

                BLEDeviceContent.listItems.add(device);

                if (leScanListener != null) {
                    leScanListener.leScanCallBack(BLEDeviceContent.listItems);
                }
            }
        };
    }

    public static BLEMediator getInstance() {
        if (null == mediator) {
            mediator = new BLEMediator();
        }
        return mediator;
    }

    public MiBand getmMiBand() {
        return mMiBand;
    }

    /**
     * 检查蓝牙是否打开并且启动打开蓝牙的方法
     */
    public void openBluetooth() {
        //判断蓝牙是否开启
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //mBluetoothAdapter.enable();
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            TFApplication.getAppContext().startActivity(enableIntent);
        }
    }

    /**
     * 开始（true）或结束（false）蓝牙扫描
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable && mScanning == false) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
            Log.d(TAG, "scanLeDevice");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    /**
     * 开始搜索蓝牙设备
     *
     * @param leScanListener 搜索蓝牙设备的回调（返回设备列表）
     */
    public void startScanLeDevice(final LeScanListener leScanListener) {
        BLEDeviceContent.listItems.clear();

        //Add bonded devices
        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
            Log.d(TAG, "Bonded device: name: " + device.getName() + ", uuid: " + device.getUuids() + ", add: " + device.getAddress() + ", type: " + device.getType() + ", bondState: " + device.getBondState());

            BLEDeviceContent.listItems.add(device);
        }

        //Connect to the first boneded device
        if (BLEDeviceContent.listItems.size() > 0) {
            Log.d(TAG, "Connect to the first bonded device in default.");
            connectGATT(BLEDeviceContent.listItems.get(0).getAddress());
        }

        this.leScanListener = leScanListener;

        scanLeDevice(true);
    }

    /**
     * 停止搜索设备
     */
    public void stopScanLeDevice() {
        if (leScanCallback == null)
            return;
        scanLeDevice(false);
    }

    /**
     * 搜索蓝牙的回调
     */
    public interface LeScanListener {
        void leScanCallBack(LeDeviceList l);
    }

    public void connectGATT(String addr) {
        Log.d(TAG, "connectGATT: " + addr);

        if (null != mBluetoothAdapter) {
            mBluetoothMi = mBluetoothAdapter.getRemoteDevice(addr);
            mGatt = mBluetoothMi.connectGatt(TFApplication.getAppContext(), false, mGattCallback);
            mGatt.connect();
        } else {
            Log.d(TAG, "mBluetoothAdapter not initialized!");
        }
    }

    public void disconnectGATT() {
        if (null != mGatt) {
            mGatt.disconnect();
            mGatt.close();

            mGatt = null;
        }
    }

    private void pair() {
        if (null != mGatt) {
            BluetoothGattCharacteristic chrt = getMiliService().getCharacteristic(UUID_CHAR_pair);
            chrt.setValue(new byte[]{2});
            mGatt.writeCharacteristic(chrt);
            Log.d(TAG, "-----------------pair sent--------------------");
//            try {
//                Thread.sleep(500);
//            } catch (java.lang.InterruptedException e) {
//                e.printStackTrace();
//            }

            getFirstRequest();
        }
    }

    private void getFirstRequest() {
        setNotification(true);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                request(UUID_CHAR_REALTIME_STEPS); // start with steps
            }
        }, 1000);
    }

    private void request(UUID what) {
        if (null != mGatt) {
            mGatt.readCharacteristic(getMiliService().getCharacteristic(what));
        }
    }

    private void setNotification(final boolean b) {
        if (null != mGatt) {
            Log.d(TAG, "-----------------setNotification--------------------");

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BluetoothGattCharacteristic theme = getMiliService().getCharacteristic(UUID_CHAR_CONTROL_POINT);
                    theme.setValue(new byte[]{3, (b ? (byte) 1 : (byte) 0)});
                    mGatt.writeCharacteristic(theme);

                    BluetoothGattCharacteristic stepChrt = getMiliService().getCharacteristic(UUID_CHAR_REALTIME_STEPS);
                    mGatt.setCharacteristicNotification(stepChrt, true);

                    List<BluetoothGattDescriptor> descriptors = stepChrt.getDescriptors();
                    for (BluetoothGattDescriptor dp : descriptors) {
                        Log.d(TAG, "BluetoothGattDescriptor getPermissions:" + dp.getPermissions());
                        Log.d(TAG, "BluetoothGattDescriptor getUuid:" + dp.getUuid());
                        Log.d(TAG, "BluetoothGattDescriptor getValue:" + dp.getValue());

                        dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                        if (mGatt.writeDescriptor(dp)) {
                            Log.d(TAG, "BluetoothGattDescriptor setValue succeed!");
                        }
                    }
                    Log.d(TAG, "-----------------setNotification done--------------------");

                }
            }, 10000);
        }
    }

    private void setStepNotification(boolean b) {
        if (null != mGatt) {
            Log.d(TAG, "-----------------setStepNotification--------------------");

            BluetoothGattCharacteristic stepChrt = getMiliService().getCharacteristic(UUID_CHAR_REALTIME_STEPS);
            mGatt.setCharacteristicNotification(stepChrt, true);

            List<BluetoothGattDescriptor> descriptors = stepChrt.getDescriptors();
            for (BluetoothGattDescriptor dp : descriptors) {
                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mGatt.writeDescriptor(dp);
            }

        }
    }

    private void setColor(byte r, byte g, byte b) {
        if (null != mGatt) {
            BluetoothGattCharacteristic theme = getMiliService().getCharacteristic(UUID_CHAR_CONTROL_POINT);
            theme.setValue(new byte[]{14, r, g, b, 0});
            mGatt.writeCharacteristic(theme);
        }
    }

    private BluetoothGattService getMiliService() {
        return mGatt.getService(UUID_MILI_SERVICE);
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        int state = 0;

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                pair();
            }
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            // this is called tight after pair()
            //setColor((byte)127, (byte)0, (byte)0);
            Log.d(TAG, "onCharacteristicWrite is called!*********************************");
            //request(UUID_CHAR_REALTIME_STEPS); // start with steps
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            byte[] b = characteristic.getValue();
            Log.i(TAG, characteristic.getUuid().toString() + "state: " + state + " value:" + Arrays.toString(b));

            // handle value
            if (characteristic.getUuid().equals(UUID_CHAR_REALTIME_STEPS))
                mMiBand.setSteps(0xff & b[0] | (0xff & b[1]) << 8);
            else if (characteristic.getUuid().equals(UUID_CHAR_BATTERY)) {
                Battery battery = Battery.fromByte(b);
                mMiBand.setBattery(battery);
            } else if (characteristic.getUuid().equals(UUID_CHAR_DEVICE_NAME)) {
                mMiBand.setName(new String(b));
            } else if (characteristic.getUuid().equals(UUID_CHAR_LE_PARAMS)) {
                LeParams params = LeParams.fromByte(b);
                mMiBand.setLeParams(params);
            }

            // proceed with state machine (called in the beginning)
            state++;

            switch (state) {
                case 0:
                    request(UUID_CHAR_REALTIME_STEPS);
                    break;
                case 1:
                    request(UUID_CHAR_BATTERY);
                    break;
                case 2:
                    request(UUID_CHAR_DEVICE_NAME);
                    break;
                case 3:
                    request(UUID_CHAR_LE_PARAMS);
                    break;
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "onCharacteristicChanged   ------------------------>");
            Log.d(TAG, "onCharacteristicChanged   ------------------------>");
            Log.d(TAG, "onCharacteristicChanged   ------------------------>");
        }
    };


    @Override
    public void update(Observable observable, Object data) {
        new Runnable() {
            @Override
            public void run() {
            }
        }.run();
    }
}
