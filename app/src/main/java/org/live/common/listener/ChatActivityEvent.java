package org.live.common.listener;

import org.live.module.chat.service.AnchorChatService;

/**
 * 聊天窗口事件
 * Created by KAM on 2017/4/26.
 */

public interface ChatActivityEvent {


    /**
     * 取得聊天服务引用
     *
     * @return
     */
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder();


}
