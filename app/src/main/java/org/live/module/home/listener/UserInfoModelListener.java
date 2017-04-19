package org.live.module.home.listener;

/**
 * 用户信息模块逻辑处理层回调
 * Created by KAM on 2017/4/18.
 */

public interface UserInfoModelListener {
    /**
     * 返回
     */
    public void back();

    /**
     * 显示提示消息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 关闭软键盘
     */
    public void closeKeyboard();
}
