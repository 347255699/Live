package org.live.module.home.view.impl;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.live.R;

/**
 *  进入‘我的’的fragment
 *
 * Created by Mr.wang on 2017/3/14.
 */

public class MeFragment extends Fragment {

    public static final String TAG = "HOME" ;

    private View currentFragmentView = null ;   //当前的fragment视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "执行了onCreateView") ;
        currentFragmentView = inflater.inflate(R.layout.fragment_me, null) ;
        return currentFragmentView ;

    }
}
