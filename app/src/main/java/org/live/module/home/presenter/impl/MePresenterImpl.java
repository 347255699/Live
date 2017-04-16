package org.live.module.home.presenter.impl;

import android.content.Context;

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
}
