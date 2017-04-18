package org.live.module.home.presenter.impl;

import android.content.Context;

import org.live.module.home.listener.UserInfoModelListener;
import org.live.module.home.model.UserInfoModel;
import org.live.module.home.model.impl.UserInfoModelImpl;
import org.live.module.home.presenter.UserInfoPresenter;
import org.live.module.home.view.UserInfoView;

/**
 * 用户信息模块表示层实现
 * Created by KAM on 2017/4/18.
 */

public class UserInfoPresenterImpl implements UserInfoModelListener, UserInfoPresenter{

    private UserInfoModel userInfoModel;
    private UserInfoView userInfoView;
    public UserInfoPresenterImpl(Context context, UserInfoView userInfoView){
        this.userInfoModel = new UserInfoModelImpl(context, this);
        this.userInfoView = userInfoView;
    }

    @Override
    public void back() {
        userInfoView.back();
    }

    @Override
    public void showToast(String msg) {
        userInfoView.showToast(msg);
    }

    @Override
    public void editUserInfo(String key, String val) {
        userInfoModel.editUserInfo(key, val);
    }

    @Override
    public boolean validateInputItem(String key, String val, String label) {
        return userInfoModel.validateInputItem(key, val, label);
    }
}
