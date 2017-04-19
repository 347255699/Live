package org.live.module.home.view;

/**
 * 用户模块视图
 * Created by KAM on 2017/4/18.
 */

public interface UserInfoView {
    /**
     * 返回
     */
    public void back();

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 关闭软键盘
     */
    public void closeKeyboard();
}
