package org.live.module.login.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.module.login.listener.OnLoginActivityEventListener;

/**
 * 服务条框模块
 * Created by KAM on 2017/4/12.
 */

public class ServiceClauseFragment extends BackHandledFragment {
    private View view;
    private OnLoginActivityEventListener fragmentReplaceListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_clause, container, false);
        if (getActivity() instanceof OnLoginActivityEventListener) {
            fragmentReplaceListener = (OnLoginActivityEventListener) getActivity();
        }
        fragmentReplaceListener.setTitle("服务条款"); // 设置标题
        return view;
    }

    @Override
    public boolean onBackPressed() {
        fragmentReplaceListener.setTitle("注册"); // 设置标题
        return false;
    }
}
