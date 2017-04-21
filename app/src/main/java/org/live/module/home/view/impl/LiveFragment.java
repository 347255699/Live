package org.live.module.home.view.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.live.R;
import org.live.module.anchor.view.impl.ApplyAnchorActivity;
import org.live.module.login.domain.MobileUserVo;

/**
 * 进入直播的fragment
 * Created by Mr.wang on 2017/3/14.
 */

public class LiveFragment extends Fragment {

    public static final String TAG = "HOME";

    private View currentFragmentView = null;   //当前的fragment视图

    /**
     * 成为主播按钮
     */
    private TextView lBeAnchorTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentFragmentView = inflater.inflate(R.layout.fragment_live, null);
        initUIElement();
        return currentFragmentView;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        lBeAnchorTextView = (TextView) currentFragmentView.findViewById(R.id.tv_live_be_anchor);
        lBeAnchorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ApplyAnchorActivity.class)); // 前往主播申请活动窗口
            }
        });
    }

}
