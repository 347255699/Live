package org.live.module.chat.listener;

/**
 * 用于ChatService与ChatActivity通信的聊天消息回调
 * Created by KAM on 2017/3/20.
 */

public interface ChatMsgCallBack {
    public void onMsgAvailable(String msg); // 消息可用时
}
