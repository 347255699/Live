package org.live.module.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.live.R;

/**
 *  分类直播的页面
 *
 * Created by Mr.wang on 2017/3/14.
 */

public class CategoryFragment extends Fragment {

    public static final String TAG = "CategoryFragment" ;

    private View currentFragmentView = null ;   //当前的fragment视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "执行了onCreateView") ;
        currentFragmentView = inflater.inflate(R.layout.fragment_category, null) ;
        return currentFragmentView ;

    }

}
