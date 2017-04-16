package org.live.module.login.view;

/**
 * 登录模块视图
 * Created by KAM on 2017/4/14.
 */

public interface LoginView {

    /**
     * 提示信息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 前往首页
     */
    public void toHome();

    /**
     * 返回上一个Fragment
     */
    public void popBackStack();

    /**
     * 前往登录页面
     */
    public void toLogin();

    /**
     * 摧毁自身
     */
    public void finishSelf();

}
