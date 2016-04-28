package com.tf.truefeeling.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.realm.implementation.RealmRadarDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tf.truefeeling.R;
import com.tf.truefeeling.Fragment.dummy.StatusContent;
import com.tf.truefeeling.Fragment.dummy.StatusContent.DummyItem;
import com.tf.truefeeling.custom.FingerData;
import com.tf.truefeeling.custom.HandData;
import com.tf.truefeeling.custom.MyMarkerView;
import com.tf.truefeeling.custom.RealmDemoData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        Realm.deleteRealm(config);

        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getInstance(config);

        View view = inflater.inflate(R.layout.fragment_sliding_up_status, container, false);
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayout.setAnchorPoint(0.5f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
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
三
        writeToDB(5);

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

        setData();

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

    public void setData() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(new Date());

        // RealmResults<FingerData> result = mRealm.allObjects(FingerData.class);
        RealmResults<FingerData> result = mRealm.where(FingerData.class).equalTo("when",today).findAll();

        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
        RealmRadarDataSet<FingerData> set = new RealmRadarDataSet<FingerData>(result, "planValue", "xIndex"); // stacked entries
        set.setLabel("目标");
        set.setDrawFilled(true);
        set.setColor(ColorTemplate.rgb("#009688"));
        set.setFillColor(ColorTemplate.rgb("#009688"));
        set.setFillAlpha(130);
        set.setLineWidth(2f);

        RealmRadarDataSet<FingerData> setPlan = new RealmRadarDataSet<FingerData>(result, "value", "xIndex"); // stacked entries
        setPlan.setLabel("达成");
        setPlan.setDrawFilled(true);
        setPlan.setColor(ColorTemplate.rgb("#968800"));
        setPlan.setFillColor(ColorTemplate.rgb("#968800"));
        setPlan.setFillAlpha(130);
        setPlan.setLineWidth(2f);

        ArrayList<IRadarDataSet> dataSets = new ArrayList<IRadarDataSet>();
        dataSets.add(set); // add the dataset
        dataSets.add(setPlan); // add the dataset

        // create a data object with the dataset list
        RadarData data = new RadarData(mParties, dataSets);
        styleData(data);

        // set data
        mChart.setData(data);
        mChart.animateY(1400);
    }

    protected void styleData(ChartData data) {
        //data.setValueTypeface(mTf);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    protected void writeToDB(int objectCount) {
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
            fingerData.setxIndex(i);
            mRealm.commitTransaction();
        }
    }
}
