package org.live.module.login.listener;

import org.live.module.login.presenter.LoginPresenter;

/**
 * fragment替换回调
 * Created by KAM on 2017/4/11.
 */

public interface OnLoginActivityEventListener {
    /**
     * 替换成注册模块
     */
    public void replaceToRegisterFragment();

    /**
     * 替换成忘记密码模块
     */
    public void replaceToForgetPasswordFragment();

    /**
     * 替换成服务条款模块
     */
    public void replaceToServiceClauseFragment();

    /**
     * 设置标题
     */
    public void setTitle(String title);

    /**
     * 取得登录模块的表示器
     */
    public LoginPresenter getLoginPresenter();

    /**
     * 隐藏加载框
     */
    public void hideProgressBar();

    /**
     * 显示加载框
     */
    public void showProgressBar();

}
