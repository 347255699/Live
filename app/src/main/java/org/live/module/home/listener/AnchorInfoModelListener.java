package org.live.module.home.listener;

/**
 * 主播信息逻辑处理层回调
 * Created by KAM on 2017/4/22.
 */

public interface AnchorInfoModelListener {
    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 返回
     */
    public void back();
}
