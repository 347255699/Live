package org.live.module.home.listener;

/**
 * '我的'模块逻辑处理层回调
 * Created by KAM on 2017/4/16.
 */

public interface MeModelListener {

    /**
     * 前往登陆页
     */
    public void toLogin();

    /**
     * 摧毁自己
     */
    public void finishSelf();
}
