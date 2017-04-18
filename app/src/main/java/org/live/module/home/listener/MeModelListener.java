package org.live.module.home.listener;

import android.content.Intent;

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

    /**
     * 裁剪头像
     *
     * @param intent
     * @param requestCode 请求标识
     */
    public void cropHeadImg(Intent intent, int requestCode);

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 设置头像
     */
    public void setHeadImg();
}
