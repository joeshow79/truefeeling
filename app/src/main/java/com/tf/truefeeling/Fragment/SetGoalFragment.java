package com.tf.truefeeling.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tf.truefeeling.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetGoalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetGoalFragment extends DialogFragment implements TextView.OnEditorActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "value";

    // TODO: Rename and change types of parameters
    private int mParam1;  //index
    private int mParam2; //value

    private OnSetGoalInteractionListener mListener;
    private TextView mEditText;

    public SetGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetGoalFragment newInstance(int index, int value) {
        SetGoalFragment fragment = new SetGoalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, index);
        args.putInt(ARG_PARAM2, value);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_goal, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView tvLabel = (TextView) view.findViewById(R.id.id_label_score);
        mEditText = (TextView) view.findViewById(R.id.id_txt_score);
        mEditText.setOnEditorActionListener(this);
        mEditText.requestFocus();

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        switch (mParam1) {
            case 1:
                tvLabel.setText(R.string.finger1_name);
                mEditText.setText(String.valueOf(mParam2));
                break;
            case 2:
                tvLabel.setText(R.string.finger2_name);
                mEditText.setText(String.valueOf(mParam2));
                break;
            case 3:
                tvLabel.setText(R.string.finger3_name);
                mEditText.setText(String.valueOf(mParam2));
                break;
            case 4:
                tvLabel.setText(R.string.finger4_name);
                mEditText.setText(String.valueOf(mParam2));
                break;
            case 5:
                tvLabel.setText(R.string.finger5_name);
                mEditText.setText(String.valueOf(mParam2));
                break;
        }
    }

    // Fires whenever the textfield has an action performed
    // In this case, when the "Done" button is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            OnSetGoalInteractionListener listener = (OnSetGoalInteractionListener ) getActivity();
            listener.onSetGoalInteraction(mParam1,Integer.valueOf(mEditText.getText().toString()));
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetGoalInteractionListener) {
            mListener = (OnSetGoalInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public interface OnSetGoalInteractionListener {
        // TODO: Update argument type and name
        void onSetGoalInteraction(int index,int value);
    }
}
