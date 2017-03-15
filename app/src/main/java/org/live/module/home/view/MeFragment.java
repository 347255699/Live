package org.live.module.home.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.live.R;

/**
 *  进入‘我的’的fragment
 *
 * Created by Mr.wang on 2017/3/14.
 */

public class MeFragment extends Fragment {

    private View currentFragmentView = null ;   //当前的fragment视图

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  currentFragmentView = inflater.inflate(R.layout.fragment_me, null) ;
        //return currentFragmentView ;

        TextView localTextView = new TextView(getContext());
        localTextView.setTextSize(30.0F);
        localTextView.setGravity(17);
        localTextView.setText("MeFragment");
        return localTextView;
    }
}
