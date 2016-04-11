package com.tf.truefeeling.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.tf.truefeeling.Fragment.BLEConnectionFragment;
import com.tf.truefeeling.Fragment.StatusFragment;
import com.tf.truefeeling.Model.BLEDeviceContent;
import com.tf.truefeeling.Fragment.dummy.StatusContent;
import com.tf.truefeeling.Model.LeDeviceList;
import com.tf.truefeeling.R;

public class MainActivity extends AppCompatActivity implements StatusFragment.OnListFragmentInteractionListener, BLEConnectionFragment.OnListFragmentInteractionListener, BluetoothAdapter.LeScanCallback {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Handler mHandler = new Handler();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;

    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mBluetoothManager = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE));
        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();
        scanLeDevice(true);
    }


    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    public void onListFragmentInteraction(StatusContent.DummyItem item) {
    }

    @Override
    public void onListFragmentInteraction(BLEDeviceContent.BLEDeviceItem item) {
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device != null && device.getName() != null
                /*& device.getName().equals("MI")*/) {
            System.out.println(device.getAddress());
            System.out.println(device.getName());
            //scanLeDevice(false); // we only care about one miband so that's enough

            BLEDeviceContent.listItems.add(device.getName(), device.getAddress());

            //JJ
            /*Intent intent = new Intent(getApplicationContext(), MiOverviewActivity.class);
            intent.putExtra("address", device.getAddress());
            startActivity(intent);*/
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //mTextView.setText(R.string.looking_for_miband);
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(MainActivity.this);
                    //mTextView.setText(R.string.not_found);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(this);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(this);
        }
    }


    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;

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
            if (1 == position) {
                return StatusFragment.newInstance(1);
            }

            return PageFragment.newInstance(position + 1);
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
