package com.tf.truefeeling.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tf.truefeeling.MiBand;
import com.tf.truefeeling.R;
import com.tf.truefeeling.util.Log;
import com.tf.truefeeling.model.BLEDeviceContent;

public class BindDeviceActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "BindDeviceActivity ";
    private ProgressBar pb;
    private TextView tvNewDevice;
    private TextView tvNewDevice2;
    private TextView tvNewDevice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bind_device);

        findViewById(R.id.bind_quit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick ");
            }
        });

        pb = (ProgressBar) findViewById(R.id.bind_progress);
        tvNewDevice = (TextView) findViewById(R.id.new_device_name);
        tvNewDevice2 = (TextView) findViewById(R.id.new_device_name2);
        tvNewDevice3 = (TextView) findViewById(R.id.new_device_name3);

        tvNewDevice.setOnClickListener(this);
        tvNewDevice2.setOnClickListener(this);
        tvNewDevice3.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvNewDevice.setText("");
        tvNewDevice2.setText("");
        tvNewDevice3.setText("");

        for (int i = 0; i < BLEDeviceContent.listItems.size() && i<3; ++i) {
            switch(i){
                case 0:
                    tvNewDevice.setText(BLEDeviceContent.listItems.get(i).getAddress());
                    break;
                case 1:
                    tvNewDevice2.setText(BLEDeviceContent.listItems.get(i).getAddress());
                    break;
                case 2:
                    tvNewDevice3.setText(BLEDeviceContent.listItems.get(i).getAddress());
                    break;
            }
        }
    }

    public void onClick(View v) {
        TextView tv=(TextView)v;

        if(null!=tv){
            if(tv.getText()!=null && (!tv.getText().equals(""))){
                Log.d(TAG,"Connect to "+tv.getText());
                MiBand.getInstance().connect((String)(tv.getText()), null);
            }
        }
    }

}
