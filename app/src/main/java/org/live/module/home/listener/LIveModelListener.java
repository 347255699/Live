package org.live.module.home.listener;

/**
 * '直播'模块逻辑处理层回调
 * Created by KAM on 2017/4/22.
 */

public interface LiveModelListener {
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
