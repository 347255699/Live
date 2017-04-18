package org.live.module.home.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import org.live.module.home.listener.MeModelListener;
import org.live.module.home.model.MeModel;
import org.live.module.home.model.impl.MeModelImpl;
import org.live.module.home.presenter.MePresenter;
import org.live.module.home.view.MeView;
import org.live.module.login.domain.MobileUserVo;

/**
 * '我的'模块表示层实现
 * Created by KAM on 2017/4/15.
 */

public class MePresenterImpl implements MePresenter, MeModelListener {
    private MeModel meModel;
    private MeView meView;

    public MePresenterImpl(Context context, MeView meView) {

        this.meModel = new MeModelImpl(context, meView);
        this.meView = meView;
    }

    /**
     * 取得用户数据
     *
     * @return
     */
    @Override
    public MobileUserVo getUserData() {
        return meModel.getUserData(); // 取得用户数据
    }

    /**
     * 注销登录
     *
     * @param account 账号
     * @param roomId  房间id
     */
    @Override
    public void logout(String account, String roomId) {
        meModel.logout(account, roomId);
    }

    @Override
    public void cropHeadImg(Uri uri) {
        meModel.cropHeadImg(uri);
    }

    @Override
    public String setPicToSd(Bitmap bitmap) {
        return meModel.setPicToSd(bitmap);
    }

    @Override
    public void postHeadImg(String fileName, String userId) {
        meModel.postHeadImag(fileName, userId);
    }

    /**
     * 前往登陆页
     */
    @Override
    public void toLogin() {
        meView.toLogin();
    }

    /**
     * 摧毁自己
     */
    @Override
    public void finishSelf() {
        meView.finishSelf();
    }

    @Override
    public void cropHeadImg(Intent intent, int requestCode) {
        meView.cropHeadImg(intent, requestCode);
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        meView.showToast(msg);
    }

    /**
     * 设置头像
     */
    @Override
    public void setHeadImg() {
        meView.setHeadImg();
    }
}
