package org.live.module.capture.listener;

import org.live.module.chat.service.AnchorChatService;

/**
 * Created by KAM on 2017/4/29.
 */

public interface OnCaptureActivityListener {
    /**
     * 退出直播
     */
    public void backLiving();

    /**
     * 获得聊天服务实体
     *
     * @return
     */
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder();

    /**
     * 注销服务
     */
    public void logoutService();
}
