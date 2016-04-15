package com.tf.truefeeling.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tf.truefeeling.Fragment.StatusFragment.OnListFragmentInteractionListener;
import com.tf.truefeeling.Fragment.dummy.StatusContent.DummyItem;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.tf.truefeeling.R;
import com.tf.truefeeling.Util.Log;
import com.tf.truefeeling.model.MiBandData;


/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StatusItemRecyclerViewAdapter extends RecyclerView.Adapter<StatusItemRecyclerViewAdapter.ViewHolder> implements Observer {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    private String TAG = "StatusItemRecyclerViewAdapter";

    public StatusItemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        //BLEMediator_tbd.getInstance().getMiBand().addObserver(this);
        MiBandData.getInstance().addObserver(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder ----------------------------------->");
        //Log.d(TAG, String.valueOf(BLEMediator_tbd.getInstance().getmMiBand().mSteps));
//        Log.d(TAG, String.valueOf(BLEMediator_tbd.getInstance().getmMiBand().mBattery));


        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        mValues.clear();
        mValues.add(new DummyItem("Step:", String.valueOf(MiBandData.getInstance().mSteps)));
//        new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "----------------------------------->");
//                mValues.clear();
//                mValues.add(new DummyItem("Step:", String.valueOf(MiBandData.getInstance().mSteps)));
////                mValues.add(new DummyItem("Battery:",String.valueOf(BLEMediator_tbd.getInstance().getmMiBand().mBattery.mBatteryLevel)));
//                //mValues.add(new DummyItem("Battery:",String.valueOf(BLEMediator_tbd.getInstance().getmMiBand().mBattery)));
//            }
//        }.run();
    }
}
