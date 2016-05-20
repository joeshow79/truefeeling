package com.tf.truefeeling.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tf.truefeeling.ActionCallback;
import com.tf.truefeeling.fragment.BLEConnectionFragment;
import com.tf.truefeeling.fragment.ProfileFragment;
import com.tf.truefeeling.fragment.SetGoalFragment;
import com.tf.truefeeling.fragment.SlidingUpStatusFragment;
import com.tf.truefeeling.fragment.StatusFragment;
import com.tf.truefeeling.fragment.dummy.StatusContent;
import com.tf.truefeeling.MiBand;
import com.tf.truefeeling.R;
import com.tf.truefeeling.util.AVService;
import com.tf.truefeeling.util.Log;
import com.tf.truefeeling.bluetooth.MiBandWrapper;
import com.tf.truefeeling.bluetooth.NotificationConstants;
import com.tf.truefeeling.custom.FingerData;
import com.tf.truefeeling.custom.GoalData;
import com.tf.truefeeling.listener.NotifyListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainTabActivity extends AppCompatActivity implements StatusFragment.OnListFragmentInteractionListener, BLEConnectionFragment.OnListFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, NotifyListener, SetGoalFragment.OnSetGoalInteractionListener/*, BLEMediator_tbd.LeScanListener, BluetoothAdapter.LeScanCallback */ {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Handler mHandler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;

    private MiBand miBand;
    private boolean isConnected = false;
    private int BT_REQUEST_CODE = 1001;
    private SampleFragmentPagerAdapter pagerAdapter;

    private boolean mScanning;
    private boolean mPause = false;   //JJ: If activity is paused, no need to reconnect BLE, reconnection will be resume once the activity resumed
    private static final long SCAN_PERIOD = 10000;
    private String TAG = "MainActivity";

    protected Realm mRealm;

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

        pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        viewPager.setCurrentItem(1);

        miBand = MiBand.getInstance();

        RealmConfiguration config = new RealmConfiguration.Builder(this).name("tf.realm").build();
        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getInstance(config);

        //TODO: set default goal
        writeDefaultGoalToDB(5);

        //Test activity data
        writeActivityToDB(5);
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

        mPause = false;
    }

    @Override
    public void onPause() {
        disconnectToMiBand();

        mPause = true;

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
    public void onFragmentInteraction(int index, int value) {
        Log.d(TAG, "onFragmentInteraction, index: " + String.valueOf(index));
        if (0 > index) {
            new AlertDialog.Builder(this)
                    .setTitle(
                            this.getResources().getString(
                                    R.string.dialog_message_title))
                    .setMessage(
                            this.getResources().getString(
                                    R.string.action_logout_alert_message))
                    .setNegativeButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    logout();
                                }
                            })
                    .setPositiveButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();

        } else {
            SetGoalFragment dialog = SetGoalFragment.newInstance(index, value);
            dialog.setCancelable(true);
            dialog.show(getFragmentManager(), "Set goal");
        }
    }

    private void logout() {
        AVService.logout();
        Intent loginIntent = new Intent(this, TFLoginActivity.class);
        startActivity(loginIntent);
        this.finish();
    }

    @Override
    public void onSetGoalInteraction(int index, int value) {
        //delete first
        RealmQuery<GoalData> query = mRealm.where(GoalData.class);
        Log.d(TAG, "onSetGoalInteraction, index: " + String.valueOf(index) + " , value: " + String.valueOf(value));

        mRealm.beginTransaction();
        query.equalTo("index", index).findAll().clear();
        mRealm.commitTransaction();

        //insert new goal record to db
        mRealm.beginTransaction();
        GoalData goalData = mRealm.createObject(GoalData.class);
        goalData.setIndex(index);
        goalData.setValue(value);
        mRealm.commitTransaction();
    }

    //For the initial setting, there should be no default goal set
    //set the initial goal setting in db
    protected void writeDefaultGoalToDB(int objectCount) {
        RealmQuery<GoalData> query = mRealm.where(GoalData.class);
        RealmResults<GoalData> result = query.findAll();
        result.sort("index");

        //sanity checking
        boolean sanity = true;
        if (result.size() != objectCount) {
            sanity = false;
        } else {
            for (int i = 0; i < result.size(); ++i) {
                if (result.get(i).getIndex() != (i + 1)) {
                    sanity = false;
                    break;
                } else {
                    if (result.get(i).getValue() < 0) {
                        sanity = false;
                        break;
                    }
                }
            }
        }

        //sanity checking failed, then re-create the db class again
        if (false == sanity) {
            mRealm.beginTransaction();
            mRealm.clear(GoalData.class);
            mRealm.commitTransaction();

            for (int i = 0; i < objectCount; i++) {
                mRealm.beginTransaction();

                GoalData goalData = mRealm.createObject(GoalData.class);

                //TODO; appropriate value
                goalData.setValue(800);
                goalData.setIndex(i + 1);
                mRealm.commitTransaction();
            }
        }

        //if true== sanity, do nothing
    }

    protected void writeActivityToDB(int objectCount) {
        mRealm.beginTransaction();
        mRealm.clear(FingerData.class);
        mRealm.commitTransaction();


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());

        for (int i = 0; i < objectCount; i++) {
            mRealm.beginTransaction();

            FingerData fingerData = mRealm.createObject(FingerData.class);

            int value = (int) (40f + (float) (Math.random() * 60f));
            int planValue = (int) (40f + (float) (Math.random() * 60f));

            fingerData.setWhen(today);
            fingerData.setValue(value);
            fingerData.setPlanValue(planValue);
            fingerData.setxIndex(i + 1);
            mRealm.commitTransaction();
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        today = df.format(c.getTime());
        for (int i = 0; i < objectCount; i++) {
            mRealm.beginTransaction();

            FingerData fingerData = mRealm.createObject(FingerData.class);

            int value = (int) (40f + (float) (Math.random() * 60f));
            int planValue = (int) (40f + (float) (Math.random() * 60f));

            fingerData.setWhen(today);
            fingerData.setValue(value);
            fingerData.setPlanValue(planValue);
            fingerData.setxIndex(i + 1);
            mRealm.commitTransaction();
        }

        c.add(Calendar.DAY_OF_MONTH, -2);
        today = df.format(c.getTime());
        for (int i = 0; i < objectCount; i++) {
            mRealm.beginTransaction();

            FingerData fingerData = mRealm.createObject(FingerData.class);

            int value = (int) (40f + (float) (Math.random() * 60f));
            int planValue = (int) (40f + (float) (Math.random() * 60f));

            fingerData.setWhen(today);
            fingerData.setValue(value);
            fingerData.setPlanValue(planValue);
            fingerData.setxIndex(i + 1);
            mRealm.commitTransaction();
        }
    }

    @Override
    public void onListFragmentInteraction(BluetoothDevice item) {
        Log.d(TAG, "----------------------------------->");
        /*Intent intent = new Intent(getApplicationContext(), MiOverviewActivity_tbd.class);
        intent.putExtra("address", item.getAddress());
        startActivity(intent);*/

        //BLEMediator_tbd.getInstance().connectGATT(item.getAddress());
        if (null == item) {
            Intent intent = new Intent(getApplicationContext(), BindDeviceActivity.class);
            startActivity(intent);
        }

        if (!miBand.isConnected()) {
            miBand.connect(item.getAddress(), myConnectionCallback);
        }
    }


    @Override
    public void onNotify(byte[] data) {
        Log.d(TAG, "onNotify: TODO to reconnect");
        if (!mPause) {
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
                return ProfileFragment.newInstance();
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
