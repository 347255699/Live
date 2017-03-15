package org.live.module.home.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.live.R;

/**
 *
 *  进入直播的fragment
 * Created by Mr.wang on 2017/3/14.
 */

public class LiveFragment extends Fragment {

    private View currentFragmentView = null ;   //当前的fragment视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentFragmentView = inflater.inflate(R.layout.fragment_live, null) ;
        return currentFragmentView ;
    }
}
