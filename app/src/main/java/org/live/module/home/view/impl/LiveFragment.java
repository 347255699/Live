package org.live.module.home.view.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.live.R;
import org.live.module.anchor.view.impl.ApplyAnchorActivity;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.home.presenter.LivePresenter;
import org.live.module.home.presenter.impl.LivePresenterImpl;
import org.live.module.home.view.LiveView;
import org.live.module.login.domain.MobileUserVo;

/**
 * 进入直播的fragment
 * Created by Mr.wang on 2017/3/14.
 */

public class LiveFragment extends Fragment implements LiveView {

    public static final String TAG = "HOME";

    private View currentFragmentView = null;   //当前的fragment视图
    /**
     * 下拉刷新视图
     */
    private SwipeRefreshLayout lLiveSwipeRefreshLayout;
    /**
     * 成为主播按钮
     */
    private TextView lBeAnchorTextView;
    private LivePresenter livePresenter;
    private  OnHomeActivityEventListener homeActivityEventListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() instanceof OnHomeActivityEventListener){
            this.homeActivityEventListener = (OnHomeActivityEventListener) getActivity();
        }
        currentFragmentView = inflater.inflate(R.layout.fragment_live, null);
        livePresenter = new LivePresenterImpl(getActivity(), this);
        initUIElement();
        return currentFragmentView;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        lBeAnchorTextView = (TextView) currentFragmentView.findViewById(R.id.tv_live_be_anchor);
        lLiveSwipeRefreshLayout = (SwipeRefreshLayout) currentFragmentView.findViewById(R.id.srl_live);
        lLiveSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.themeColor),
                getResources().getColor(R.color.themeColor1)); // 调整下拉刷新视图样式
        lBeAnchorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ApplyAnchorActivity.class)); // 前往主播申请活动窗口
            }
        }); // 申请成为主播
        lLiveSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                livePresenter.checkIsAnchor(HomeActivity.mobileUserVo.getUserId()); // 检查用户主播身份
            }
        }); // 刷新视图
    }

    @Override
    public void closeRefreshing(boolean isAnchor) {
        lLiveSwipeRefreshLayout.setRefreshing(false); // 关闭下拉刷新视图
        if (isAnchor && !HomeActivity.mobileUserVo.isAnchorFlag()) {
            // 刷新视图
            homeActivityEventListener.replaceLiveFragment(); // 刷新视图
        }

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show(); // 显示提示消息
    }
}
