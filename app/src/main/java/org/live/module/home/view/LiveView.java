package org.live.module.home.view;

/**
 * 直播视图
 * Created by KAM on 2017/4/22.
 */

public interface LiveView {
    /**
     * 关闭刷新视图
     */
    public void closeRefreshing(boolean isAnchor);

    /**
     * 显示提示消息
     *
     * @param msg
     */
    public void showToast(String msg);
}
