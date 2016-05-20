package com.tf.truefeeling.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;

/**
 * TODO remove updateLogout/updateLogin
 */
public abstract class BaseFragment extends Fragment {

    private String mTitle;
    /*private ProgressDialog progressDialog;
    private ImageLoader bitmapLoader;*/
    private TextView mTitleView;

    public abstract void updateLogout();

    public abstract void updateLogin();

    public BaseFragment() {
        super();
        setUserVisibleHint(false);
    }

    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        if(mTitleView != null){
            mTitleView.setText(title);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    /*public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void showProgress() {
        if (null == progressDialog) {
            progressDialog = ProgressDialog.createDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }
        progressDialog.show();
    }

    public void hideProgress() {
        if(progressDialog != null){
            progressDialog.hide();
        }
    }

    public void closeProgress() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }*/

    @Override
    public void onDestroy() {
       /* if(progressDialog != null){
            progressDialog.dismiss();
        }*/
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateVisibility(boolean visibility){
        if(mTitleView != null){
            mTitleView.setSelected(visibility);
            mTitleView.setActivated(visibility);
//            Log.d("Main", "fragment update state: " +visibility + this.getClass().getName());
        } else{
//            Log.d("Main", "fragment update state, but view is null!" + this.getClass().getName()) ;
        }
    }

    /*public ImageLoader getBitmapLoader(){
        if(bitmapLoader == null){
            bitmapLoader = new ImageLoader(getActivity().getApplicationContext());
        }
        return  bitmapLoader;
    }*/

    public void setTitleView(TextView view){
        mTitleView = view;
        mTitleView.setText(mTitle);
    }

}
