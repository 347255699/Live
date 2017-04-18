package org.live.module.home.view;

import android.content.Intent;

/**
 * ‘我的’模块视图接口
 * Created by KAM on 2017/4/16.
 */

public interface MeView {
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
     * @param requestCode 请求标识，返回时携带的标志
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
