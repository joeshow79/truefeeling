package com.tf.truefeeling.fragment;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tf.truefeeling.R;

public class FollowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = "FollowFragment";

    private Application app;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView listView;
    private View mMobileContactView;

    private BroadcastReceiver contactsListReceiver;

    private boolean isFollowing;

    @Override
    public void updateLogout() {
        stopReceiveBroadcast();
    }

    @Override
    public void updateLogin() {

    }

    public FollowFragment() {
        // Required empty public constructor

    }

    public static FollowFragment createFragment(boolean isFollowing) {
        // Required empty public constructor
        FollowFragment fragment = new FollowFragment();
        fragment.isFollowing = isFollowing;
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        Log.d(TAG, "call onCreateView isFollowing = " + isFollowing);
        findView(view);
        confirmData();
        return view;
    }

    private void findView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);

        listView = (ListView) view.findViewById(R.id.contacts_list);
    }

    private void confirmData() {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        app = (Application) getActivity().getApplication();

        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setCacheColorHint(Color.WHITE);
        listView.setOnItemClickListener(this);

        Log.v(TAG, "call confirmData isFollowing = " + isFollowing);
        if(isFollowing) {
            createMobileContactsHeaderView();
            //adapter.updateDatas(mContactsManager.getFolloweeList()); //obtain following
        } else {
           // adapter.updateDatas(mContactsManager.getFollowerList()); //obtain follower
        }
        //listView.setAdapter(adapter);
       // startContactsReceiveBroadcast();
    }

    private void createMobileContactsHeaderView() {
       /* mMobileContactView = setupHeaderView(getResources().getString(R.string.mobilecontacts), R.drawable.contacts_item_ic_mobile_contacts);
        mMobileContactView.findViewById(R.id.contact_list_item_devider).setVisibility(View.GONE);
        listView.addHeaderView(mMobileContactView);*/
    }

   /* private View setupHeaderView(String headerViewTitle, int iconId) {
        View headerView = View.inflate(getActivity(), R.layout.constacts_listview_item_layout, null);
        TextView flag = (TextView) headerView.findViewById(R.id.contacts_list_adapt_flag);
        flag.setVisibility(View.GONE);
        TextView name = (TextView) headerView.findViewById(R.id.contacts_list_adapt_name);
        name.setTypeface(ReplaceFonts.SANS_NORMAL);
        name.setText(headerViewTitle);
        ImageView avatar = (ImageView) headerView.findViewById(R.id.contacts_list_adapt_img);
        if (iconId == -1) {
            avatar.setVisibility(View.GONE);
        } else {
            avatar.setImageResource(iconId);
        }
        return headerView;
    }

    public ContactListAdapter getAdapter() {
        return this.adapter;
    }

    private void startContactsReceiveBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(app.FOLLOWINGLIST_ACTION);
        intentFilter.addAction(app.FOLLOWERLIST_ACTION);
        if (contactsListReceiver == null && getActivity() != null) {
            contactsListReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(app.FOLLOWINGLIST_ACTION)) {
                        Log.v(TAG, "call startContactsReceiveBroadcast action = "+action);
                        ArrayList<Contacts> following = mContactsManager.getFolloweeList();
                        if (following != null && adapter != null) {
                            if(isFollowing){
                                adapter.updateDatas(following, true, false);
                            }
                        }
                    }
                    if(action.equals(app.FOLLOWERLIST_ACTION)) {
                        Log.v(TAG, "call startContactsReceiveBroadcast action = "+action);
                        ArrayList<Contacts> follower = mContactsManager.getFollowerList();
                        if(follower != null && adapter != null) {
                            if(!isFollowing){
                                adapter.updateDatas(follower, true, false);
                            }
                        }
                    }
                }
            };
            getActivity().registerReceiver(contactsListReceiver, intentFilter);
        }
    }*/


    /*SwipeRefreshLayout refresh callback*/
    @Override
    public void onRefresh() {
        if(isFollowing) {
            refreshFollowing();
        } else {
            refreshFollower();
        }

    }

    private void refreshFollowing() {
        /*if(mContactsManager == null) {
            mContactsManager = ContactsManager.getInstance();
        }
        mContactsManager.requestFolloweeList(this);*/
    }

    private void refreshFollower() {
    /*    if(mContactsManager == null) {
            mContactsManager = ContactsManager.getInstance();
        }
        mContactsManager.requestFollowerList(this);*/
    }

    /*AzBar touch callback*/
    /*@Override
    public void onCharItemTouched(String item) {
        int pos = adapter.findCharPostion(item);
        listView.setSelection(pos + listView.getHeaderViewsCount());
    }*/

    /*ListView Item Click Callback*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView list = (ListView) parent;
        int headerCount = list.getHeaderViewsCount();
        if (headerCount > 0) {
            if (0 == position) {
                //startActivity(new Intent(getActivity(), MobileContactsActivity.class));
                return;
            }
        }
       /* Contacts contacts = (Contacts) list.getAdapter().getItem(position);
        //when on resume will update all contacts list.
        Contacts.startContactDetailsScreen(getActivity(), contacts);*/
    }

   /* @Override
    public void onFolloweeAdded(UIContact addedContact, ErrorCode errorCode) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(errorCode == null) {
            if (!isFollowing) {
                adapter.updateItem(addedContact);
            }
        }
    }

    @Override
    public void onFolloweeDelete(Contacts delContact, ErrorCode errorCode) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(errorCode == null) {
            if (!isFollowing) {
                adapter.updateItem(delContact);
            }
        }
    }

    @Override
    public void onFollowingUpdate(Contacts updated, ErrorCode errorCode) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(errorCode == null) {
            if (!isFollowing) {
                adapter.updateItem(updated);
            }
        }
    }

    @Override
    public void onAllFolloweesUpdate(List<Contacts> allList, ErrorCode errorCode) {
        Log.v(TAG, "call onAllFollowingsUpdate");
        mSwipeRefreshLayout.setRefreshing(false);
        if(errorCode == null) {
            if(isFollowing){
                adapter.updateDatas(mContactsManager.getFolloweeList());
            }
        }
    }

    @Override
    public void onAllFollowersUpdate(List<Contacts> allList, ErrorCode errorCode) {
        Log.v(TAG, "call onAllFollowersUpdate");
        mSwipeRefreshLayout.setRefreshing(false);
        if(errorCode == null) {
            if(!isFollowing){
                adapter.updateDatas(mContactsManager.getFollowerList());
            }
        }
    }*/

    @Override
    public void onDestroy() {
        stopReceiveBroadcast();
       // mContactsManager.unRegisterListener(this);
        super.onDestroy();
        Log.v(TAG, "call onDestroy");
    }

    public void stopReceiveBroadcast() {
        try {
            if (contactsListReceiver != null) {
                getActivity().unregisterReceiver(contactsListReceiver);
            }
        } catch (Exception e) {
            return;
        }
    }
}
