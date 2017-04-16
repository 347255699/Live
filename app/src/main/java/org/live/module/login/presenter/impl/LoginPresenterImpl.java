package org.live.module.login.presenter.impl;

import android.content.Context;

import org.live.module.login.listener.LoginModelListener;
import org.live.module.login.model.LoginModel;
import org.live.module.login.model.impl.LoginModelImpl;
import org.live.module.login.presenter.LoginPresenter;
import org.live.module.login.view.LoginView;

import java.util.Map;

/**
 * 登陆模块表示层实现类
 * Created by KAM on 2017/4/14.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginModelListener {
    private LoginModel loginModel; // model层引用
    private LoginView loginView; // view层引用

    public LoginPresenterImpl(Context context, LoginView loginView) {
        this.loginModel = new LoginModelImpl(context, this);
        this.loginView = loginView;
    }

    @Override
    public boolean validateForm(Map<String, String> vals, Map<String, String> labels, Map<String, Map<String, Object>> rules) {
        return loginModel.validateForm(vals, labels, rules); // 开启校验
    }

    @Override
    public void httpRequest(String ModelType, Map<String, Object> params) throws Exception {
        loginModel.httpRequest(ModelType, params); // 发起请求
    }

    @Override
    public void preLogin() {
        loginModel.preLogin(); // 预先登录
    }

    @Override
    public void showToast(String msg) {
        loginView.showToast(msg); // 提示信息
    }

    @Override
    public void toHome() {
        loginView.toHome(); // 前往首页
    }

    @Override
    public void popBackStack() {
        loginView.popBackStack(); // 回退至前一个fragment
    }

    @Override
    public void toLogin() {
        loginView.toLogin(); // 前往登录页
    }

    @Override
    public void finishSelf() {
        loginView.finishSelf();
    }

}
