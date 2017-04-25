package org.live.module.publish.listener;

import org.live.module.chat.service.AnchorChatService;

/**
 * 推流窗口
 * Created by KAM on 2017/4/24.
 */

public interface OnPublishActivityListener {
    /**
     * 取得主播聊天服务绑定实体
     *
     * @return
     */
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder();

    /**
     * 取得websocket链接
     * @return
     */
    public String getWsUrl();
}
