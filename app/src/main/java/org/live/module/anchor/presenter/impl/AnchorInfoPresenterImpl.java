package org.live.module.anchor.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import org.live.module.anchor.model.AnchorInfoModel;
import org.live.module.anchor.model.impl.AnchorInfoModelImpl;
import org.live.module.anchor.presenter.AnchorInfoPresenter;
import org.live.module.anchor.view.AnchorInfoView;
import org.live.module.anchor.listener.AnchorInfoModelListener;

/**
 * 主播信息表示层实现
 * Created by KAM on 2017/4/23.
 */

public class AnchorInfoPresenterImpl implements AnchorInfoPresenter, AnchorInfoModelListener {

    private AnchorInfoModel anchorInfoModel;
    private AnchorInfoView anchorInfoView;

    public AnchorInfoPresenterImpl(Context context, AnchorInfoView anchorInfoView) {
        this.anchorInfoModel = new AnchorInfoModelImpl(context, this);
        this.anchorInfoView = anchorInfoView;
    }

    @Override
    public void postAnchorItemInfo(String key, String val) {
        anchorInfoModel.postAnchorItemInfo(key, val);
    }

    @Override
    public void postAnchorCoverImg(String filePath) {
        anchorInfoModel.postAnchorCoverImg(filePath);
    }

    @Override
    public void cropRoomCover(Uri uri) {
        anchorInfoModel.cropRoomCover(uri);
    }

    @Override
    public String setPicToSd(Bitmap bitmap) {
        return anchorInfoModel.setPicToSd(bitmap);
    }

    @Override
    public boolean validateInputItem(String key, String val) {
        return anchorInfoModel.validateInputItem(key, val);
    }

    @Override
    public void showToast(String msg) {
        anchorInfoView.showToast(msg);
    }

    @Override
    public void back() {
        anchorInfoView.back();
    }

    @Override
    public void cropRoomCover(Intent intent, int requestCode) {
        anchorInfoView.cropRoomCover(intent, requestCode);
    }

    @Override
    public void setRoomCover() {
        anchorInfoView.setRoomCover();
    }
}
