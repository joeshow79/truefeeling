package com.tf.truefeeling.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.realm.implementation.RealmRadarDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tf.truefeeling.R;
import com.tf.truefeeling.fragment.dummy.StatusContent.DummyItem;
import com.tf.truefeeling.util.Log;
import com.tf.truefeeling.custom.FingerData;
import com.tf.truefeeling.custom.MyMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SlidingUpStatusFragment extends Fragment/*implements Observer */ {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private SlidingUpPanelLayout mLayout;
    private RadarChart mChart;
    private Typeface tf;
    protected Realm mRealm;
    private String TAG = "SlidingUpStatusFragment";
    private Calendar calendar;

    private TextView valueDate;
    private TextView f1Name;
    private TextView f1Value;
    private TextView f2Name;
    private TextView f2Value;
    private TextView f3Name;
    private TextView f3Value;
    private TextView f4Name;
    private TextView f4Value;
    private TextView f5Name;
    private TextView f5Value;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SlidingUpStatusFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SlidingUpStatusFragment newInstance(int columnCount) {
        SlidingUpStatusFragment fragment = new SlidingUpStatusFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        //mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RealmConfiguration config = new RealmConfiguration.Builder(this.getContext())
                .name("tf.realm")
                .build();

        //Realm.deleteRealm(config);

        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getInstance(config);

        View view = inflater.inflate(R.layout.fragment_sliding_up_status, container, false);
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.5f);

        valueDate = (TextView) view.findViewById(R.id.step_date);
        f1Name = (TextView) view.findViewById(R.id.f1_name);
        f1Value = (TextView) view.findViewById(R.id.f1_value);
        f2Name = (TextView) view.findViewById(R.id.f2_name);
        f2Value = (TextView) view.findViewById(R.id.f2_value);
        f3Name = (TextView) view.findViewById(R.id.f3_name);
        f3Value = (TextView) view.findViewById(R.id.f3_value);
        f4Name = (TextView) view.findViewById(R.id.f4_name);
        f4Value = (TextView) view.findViewById(R.id.f4_value);
        f5Name = (TextView) view.findViewById(R.id.f5_name);
        f5Value = (TextView) view.findViewById(R.id.f5_value);
        //mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

/*
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new StatusItemRecyclerViewAdapter(StatusContent.ITEMS, mListener));
        }*/

        mChart = (RadarChart) view.findViewById(R.id.radar_chart);
        //tf = Typeface.createFromAsset(view.getAgetAssets(), "OpenSans-Regular.ttf");
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.getYAxis().setEnabled(false);
        mChart.setWebAlpha(180);
        mChart.setWebColorInner(Color.DKGRAY);
        mChart.setWebColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this.getContext(), R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String today = df.format(calendar.getTime());

        setData(today);

        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        //xAxis.setTypeface(tf);
        xAxis.setTextSize(9f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(tf);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);


        ImageView preButton = (ImageView) view.findViewById(R.id.previous_day);
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String today = df.format(calendar.getTime());
                setData(today);
                mChart.notifyDataSetChanged();
            }
        });

        ImageView nextButton = (ImageView) view.findViewById(R.id.next_day);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String today = df.format(calendar.getTime());
                setData(today);
                mChart.notifyDataSetChanged();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
      /*  if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private String[] mParties = new String[]{
            "大拇指", "食指", "中指", "无名指", "小指"
    };

    public void setData(String today) {
        Log.d(TAG, "setData :" + today);

        valueDate.setText(today);
        f1Value.setText("");
        f2Value.setText("");
        f3Value.setText("");
        f4Value.setText("");
        f5Value.setText("");

        // RealmResults<FingerData> result = mRealm.allObjects(FingerData.class);
        RealmResults<FingerData> result = mRealm.where(FingerData.class).equalTo("when", today).findAll();
        result.sort("xIndex");

        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
        RealmRadarDataSet<FingerData> planSet = new RealmRadarDataSet<FingerData>(result, "planValue", "xIndex"); // stacked entries
        planSet.setLabel("目标");
        planSet.setDrawFilled(true);
        planSet.setColor(ColorTemplate.rgb("#009688"));
        planSet.setFillColor(ColorTemplate.rgb("#009688"));
        planSet.setFillAlpha(130);
        planSet.setLineWidth(2f);

        RealmRadarDataSet<FingerData> set = new RealmRadarDataSet<FingerData>(result, "value", "xIndex"); // stacked entries
        Log.d(TAG, "set size :" + String.valueOf(set.getResults().size()));
        for (int i = 0; i < set.getResults().size(); ++i) {
            Log.d(TAG, "Entry when:" + set.getResults().get(i).getWhen());
            Log.d(TAG, "Entry index:" + set.getResults().get(i).getxIndex());
            Log.d(TAG, "Entry value:" + set.getResults().get(i).getValue());
            Log.d(TAG, "Entry plan value:" + set.getResults().get(i).getPlanValue());
        }
        set.setLabel("达成");
        set.setDrawFilled(true);
        set.setColor(ColorTemplate.rgb("#968800"));
        set.setFillColor(ColorTemplate.rgb("#968800"));
        set.setFillAlpha(130);
        set.setLineWidth(2f);

        ArrayList<IRadarDataSet> dataSets = new ArrayList<IRadarDataSet>();
        dataSets.add(set); // add the dataset
        dataSets.add(planSet ); // add the dataset

        // create a data object with the dataset list
        RadarData data = new RadarData(mParties, dataSets);
        styleData(data);

        // set data
        mChart.setData(data);
        mChart.animateY(1400);

        for (FingerData fd : result) {
            switch (fd.getxIndex()) {
                case 1:
                    f1Name.setText(mParties[0]);
                    f1Value.setText(String.valueOf(fd.getValue()));
                    break;
                case 2:
                    f2Name.setText(mParties[1]);
                    f2Value.setText(String.valueOf(fd.getValue()));
                    break;
                case 3:
                    f3Name.setText(mParties[2]);
                    f3Value.setText(String.valueOf(fd.getValue()));
                    break;
                case 4:
                    f4Name.setText(mParties[3]);
                    f4Value.setText(String.valueOf(fd.getValue()));
                    break;
                case 5:
                    f5Name.setText(mParties[4]);
                    f5Value.setText(String.valueOf(fd.getValue()));
                    break;
            }
        }
    }

    protected void styleData(ChartData data) {
        //data.setValueTypeface(mTf);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

}
