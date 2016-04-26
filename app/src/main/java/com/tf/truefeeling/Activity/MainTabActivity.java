package com.tf.truefeeling.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tf.truefeeling.ActionCallback;
import com.tf.truefeeling.Fragment.BLEConnectionFragment;
import com.tf.truefeeling.Fragment.SlidingUpStatusFragment;
import com.tf.truefeeling.Fragment.StatusFragment;
import com.tf.truefeeling.Fragment.dummy.StatusContent;
import com.tf.truefeeling.MiBand;
import com.tf.truefeeling.R;
import com.tf.truefeeling.Util.Log;
import com.tf.truefeeling.bluetooth.MiBandWrapper;
import com.tf.truefeeling.bluetooth.NotificationConstants;
import com.tf.truefeeling.listener.NotifyListener;

public class MainTabActivity extends AppCompatActivity implements StatusFragment.OnListFragmentInteractionListener, BLEConnectionFragment.OnListFragmentInteractionListener, NotifyListener/*, BLEMediator_tbd.LeScanListener, BluetoothAdapter.LeScanCallback */ {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Handler mHandler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;

    private MiBand miBand;
    private boolean isConnected = false;
    private int BT_REQUEST_CODE = 1001;

    private boolean mScanning;
    private boolean mPause=false;   //JJ: If activity is paused, no need to reconnect BLE, reconnection will be resume once the activity resumed
    private static final long SCAN_PERIOD = 10000;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        TextView toolbar = (TextView) findViewById(R.id.toolbar);
        toolbar.setText(R.string.main_activity_title);
        //2016-04-2 jj: Replace action bar with
        /*setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        viewPager.setCurrentItem(0);

        miBand = MiBand.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean("service", false)) {
                //stopService(new Intent(MainActivity.this, MainActivity.class));
            } else {
                connectToMiBand();
            }
        } else {
            connectToMiBand();
        }

        isConnected = miBand.isConnected();

        Log.d(TAG, "onResume: miBand.isConnected(): " + String.valueOf(isConnected));

        if (isConnected) {
        } else {
        }

        mPause=false;
    }

    @Override
    public void onPause() {
        disconnectToMiBand();

        mPause=true;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MiBandWrapper.getInstance(MainTabActivity.this).sendAction(MiBandWrapper.ACTION_STOP_SYNC);

        super.onDestroy();
    }


    private void connectToMiBand() {
        Log.d(TAG, "connectToMiBand");

        if (!miBand.isConnected()) {
            miBand.connect(myConnectionCallback);
        }
    }

    private void disconnectToMiBand() {
        Log.d(TAG, "disconnectToMiBand");

        if (miBand.isConnected()) {
            miBand.disconnect();
        }
    }

    private ActionCallback myConnectionCallback = new ActionCallback() {
        @Override
        public void onSuccess(Object data) {
            Log.d(TAG, "Connected with Mi Band!");

            isConnected = true;

            //miBand.getStepData();
            //miBand.setRealtimeStepsNotifyListener(MainActivity.this);
            miBand.setDisconnectedListener(MainTabActivity.this);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    startMiBand();
                }
            });
        }

        @Override
        public void onFail(int errorCode, String msg) {
            Log.e(TAG, "Fail: " + msg);
            isConnected = false;

            if (errorCode == NotificationConstants.BLUETOOTH_OFF) {
                //turn on bluetooth
                Log.d(TAG, "turn on Bluetooth");
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BT_REQUEST_CODE, null);
            } else {
                Log.d(TAG, "not found");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }
    };

//    private void disconnectMiBand() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                MainNormalActivity.this);
//
//        // set title
//        alertDialogBuilder.setTitle("Disconnect to Mi Band");
//
//        // set dialog message
//        alertDialogBuilder
//                .setMessage("Are you sure you want to Disconnect to your Mi Band?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        miBand.disconnect();
//                        stopMiBand();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // if this button is clicked, just close
//                        // the dialog box and do nothing
//                        dialog.cancel();
//                    }
//                });
//
//        // create alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//
//        // show it
//        alertDialog.show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                connectToMiBand();
                //btn_connect.setEnabled(false);

                //textView_status.setText("Connecting...");
            } else {
//                stopMiBand();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(StatusContent.DummyItem item) {
    }

    @Override
    public void onListFragmentInteraction(BluetoothDevice item) {
        Log.d(TAG, "----------------------------------->");
        /*Intent intent = new Intent(getApplicationContext(), MiOverviewActivity_tbd.class);
        intent.putExtra("address", item.getAddress());
        startActivity(intent);*/

        //BLEMediator_tbd.getInstance().connectGATT(item.getAddress());

        if (!miBand.isConnected()) {
            miBand.connect(item.getAddress(), myConnectionCallback);
        }
    }

    @Override
    public void onNotify(byte[] data) {
        Log.d(TAG, "onNotify: TODO to reconnect");
        if(!mPause){
            //JJ, TODO
            //connectToMiBand();
        }
    }

    /*@Override
    public void leScanCallBack(LeDeviceList l) {
        //TODO
    }*/

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String TAG = "SampleFragmentPagerAdapter";

        private Context context;
        private String tabTitles[] = new String[]{getApplicationContext().getResources().getString(R.string.tab_connection), getApplicationContext().getResources().getString(R.string.tab_status), getApplicationContext().getResources().getString(R.string.tab_profile)};
        private int drawId[] = new int[]{R.drawable.main_tab_connection, R.drawable.main_tab_status, R.drawable.main_tab_profile};

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            //TODO
            //img.setImageResource(imageResId[position]);
            img.setImageResource(drawId[position]);
            return v;
        }

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (0 == position) {
                return BLEConnectionFragment.newInstance(1);
            }
            if (2 == position) {
                return StatusFragment.newInstance(1);
            }

            return SlidingUpStatusFragment.newInstance(1);
            //return PageFragment.newInstance(position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment f = (Fragment) super.instantiateItem(container, position);
            return f;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_page, container, false);
            TextView textView = (TextView) view;
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }
}
