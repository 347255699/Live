package org.live.module.login.listener;

/**
 * 登陆逻辑处理层回调，由表示层实现
 * Created by KAM on 2017/4/14.
 */

public interface LoginModelListener {
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
     * 前往登录页
     */
    public void toLogin();

    /**
     * 摧毁自己
     */
    public void finishSelf();

}
