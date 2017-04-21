package org.live.module.anchor.presenter.impl;

import android.content.Context;

import org.live.module.anchor.listener.ApplyAnchorModelListener;
import org.live.module.anchor.model.ApplyAnchorModel;
import org.live.module.anchor.model.impl.ApplyAnchorModelImpl;
import org.live.module.anchor.presenter.ApplyAnchorPresenter;
import org.live.module.anchor.view.ApplyAnchorView;
import org.live.module.home.domain.LiveCategoryVo;

import java.util.List;
import java.util.Map;

/**
 * 申请主播表示层实现
 * Created by KAM on 2017/4/21.
 */

public class ApplyAnchorPresenterImpl implements ApplyAnchorPresenter, ApplyAnchorModelListener {
    private ApplyAnchorView applyAnchorView;
    private ApplyAnchorModel applyAnchorModel;

    public ApplyAnchorPresenterImpl(Context context, ApplyAnchorView applyAnchorView) {
        this.applyAnchorView = applyAnchorView;
        this.applyAnchorModel = new ApplyAnchorModelImpl(context, this);
    }

    @Override
    public boolean validateInputItem(Map<String, String> labels, Map<String, Object> vals) {
        return applyAnchorModel.validateInputItem(labels, vals);
    }

    @Override
    public void postAnchorInfo(Map<String, Object> params) {
        applyAnchorModel.postAnchorInfo(params);
    }

    @Override
    public void requestCategoryList() {
        applyAnchorModel.requestCategoryList();
    }

    @Override
    public void showToast(String msg) {
        applyAnchorView.showToast(msg);
    }

    @Override
    public void back() {
        applyAnchorView.back();
    }

    @Override
    public void showCategoryList(List<LiveCategoryVo> liveCategoryVos) {
        applyAnchorView.showCategoryList(liveCategoryVos);
    }
}
