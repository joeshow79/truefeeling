package com.tf.truefeeling.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tf.truefeeling.R;
import com.tf.truefeeling.util.Log;
import com.tf.truefeeling.custom.GoalData;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private String TAG = "ProfileFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    protected Realm mRealm;
    private TextView tvF1;
    private TextView tvF2;
    private TextView tvF3;
    private TextView tvF4;
    private TextView tvF5;
    private TextView tvSignout;



    private OnFragmentInteractionListener mListener;
    private RealmChangeListener realmListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(/*String param1*/) {
        ProfileFragment fragment = new ProfileFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvF1 = (TextView) view.findViewById(R.id.finger_info_item_value_f1);
        tvF1.setOnClickListener(this);
        tvF2 = (TextView) view.findViewById(R.id.finger_info_item_value_f2);
        tvF2.setOnClickListener(this);
        tvF3 = (TextView) view.findViewById(R.id.finger_info_item_value_f3);
        tvF3.setOnClickListener(this);
        tvF4 = (TextView) view.findViewById(R.id.finger_info_item_value_f4);
        tvF4.setOnClickListener(this);
        tvF5 = (TextView) view.findViewById(R.id.finger_info_item_value_f5);
        tvF5.setOnClickListener(this);
        tvSignout= (TextView) view.findViewById(R.id.sign_out);
        tvSignout.setOnClickListener(this);

        RealmConfiguration config = new RealmConfiguration.Builder(this.getContext())
                .name("tf.realm")
                .build();

//        Realm.deleteRealm(config);

        Realm.setDefaultConfiguration(config);
        mRealm = Realm.getInstance(config);

        readGoalData();

        realmListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                readGoalData();
            }
        };
        mRealm.addChangeListener(realmListener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        mRealm.removeChangeListener(realmListener);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick ");

        if (null != mListener) {
            int index = 0;
            int value = 0;
            switch (v.getId()) {
                case R.id.finger_info_item_value_f1:
                    index = 1;
                    value = Integer.valueOf((String) (tvF1.getText())).intValue();
                    break;
                case R.id.finger_info_item_value_f2:
                    index = 2;
                    value = Integer.valueOf((String) (tvF2.getText())).intValue();
                    break;
                case R.id.finger_info_item_value_f3:
                    index = 3;
                    value = Integer.valueOf((String) (tvF3.getText())).intValue();
                    break;
                case R.id.finger_info_item_value_f4:
                    index = 4;
                    value = Integer.valueOf((String) (tvF4.getText())).intValue();
                    break;
                case R.id.finger_info_item_value_f5:
                    index = 5;
                    value = Integer.valueOf((String) (tvF5.getText())).intValue();
                    break;
                case R.id.sign_out:
                    index = -1;
                    break;
            }

            //TODO
            if (0 != index) {
                mListener.onFragmentInteraction(index, value);
            }
        }
    }

    public void readGoalData() {
        RealmResults<GoalData> result = mRealm.allObjects(GoalData.class);
        result.sort("index");

        tvF1.setText("");
        tvF2.setText("");
        tvF3.setText("");
        tvF4.setText("");
        tvF5.setText("");

        Log.d(TAG, "readGoalData");
        for (int i = 0; i < result.size(); i++) {
            Log.d(TAG, "Goal: " + result.get(i).getValue());

            switch (result.get(i).getIndex()) {
                case 1:
                    tvF1.setText(String.valueOf(result.get(i).getValue()));
                    break;
                case 2:
                    tvF2.setText(String.valueOf(result.get(i).getValue()));
                    break;
                case 3:
                    tvF3.setText(String.valueOf(result.get(i).getValue()));
                    break;
                case 4:
                    tvF4.setText(String.valueOf(result.get(i).getValue()));
                    break;
                case 5:
                    tvF5.setText(String.valueOf(result.get(i).getValue()));
                    break;
            }
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int index, int value);
    }
}
