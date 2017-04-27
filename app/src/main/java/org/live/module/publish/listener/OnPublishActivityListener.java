package org.live.module.publish.listener;

import org.live.module.chat.service.AnchorChatService;

/**
 * 推流窗口
 * Created by KAM on 2017/4/24.
 */

public interface OnPublishActivityListener {


    /**
     * 取得websocket链接
     *
     * @return
     */
    public String getWsUrl();

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
