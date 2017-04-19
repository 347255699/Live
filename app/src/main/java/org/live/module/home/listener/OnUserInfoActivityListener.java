package org.live.module.home.listener;

import android.graphics.drawable.Drawable;
import android.view.View;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.module.home.presenter.UserInfoPresenter;

/**
 * 用户信息活动窗口事件回调，用于在fragment调用activity中的方法或事件
 * Created by KAM on 2017/4/18.
 */

public interface OnUserInfoActivityListener {
    /**
     * 返回
     */
    public void back();

    /**
     * 初始化标题栏
     */
    public void initActionBar(View view, int toolBarId);

    /**
     * 取得图标
     *
     * @return
     */
    public Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color);

    /**
     * 替换成用户信息修改模块
     *
     * @param key 待修改信息key
     * @param val 待修改信息val
     */
    public void replaceUserInfoEditFragment(String key, String val);

    public UserInfoPresenter getUserInfoPresenter();

    /**
     * 关闭软键盘
     */
    public void closeKeyboard();
}
