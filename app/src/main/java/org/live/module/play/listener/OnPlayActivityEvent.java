package org.live.module.play.listener;

/**
 * 播放窗口事件
 * Created by KAM on 2017/4/26.
 */

public interface OnPlayActivityEvent {
    /**
     * 聊天输入框视图
     */
     void showChatInputView();

    /**
     * 注销服务
     */
     void logoutService();

    /**
     * 主播离开，替换fragment为直播结束的fragement
     */
    void replaceLiveOverFragment() ;
}
