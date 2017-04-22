package org.live.module.home.presenter.impl;

import android.content.Context;

import org.live.module.home.listener.LiveModelListener;
import org.live.module.home.model.LiveModel;
import org.live.module.home.model.impl.LiveModelImpl;
import org.live.module.home.presenter.LivePresenter;
import org.live.module.home.view.LiveView;

/**
 * 主播模块表示层实现
 * Created by KAM on 2017/4/22.
 */

public class LivePresenterImpl implements LivePresenter, LiveModelListener {
    private LiveView liveView;
    private LiveModel liveModel;

    public LivePresenterImpl(Context context, LiveView liveView) {
        this.liveView = liveView;
        this.liveModel = new LiveModelImpl(context, this);
    }

    @Override
    public void checkIsAnchor(String userId) {
        liveModel.checkIsAnchor(userId);
    }

    @Override
    public void closeRefreshing(boolean isAnchor) {
        liveView.closeRefreshing(isAnchor);
    }

    @Override
    public void showToast(String msg) {
        liveView.showToast(msg);
    }
}
